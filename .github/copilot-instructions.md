# Copilot Instructions for java.samples.spring.boot

## Project Overview

This is a RESTful Web Service proof-of-concept built with **Spring Boot 4** targeting **JDK 25 (LTS)**. The application demonstrates a clean, layered architecture implementing a CRUD API for managing books. It uses a **SQLite database** for runtime persistence (with a pre-seeded database in Docker) and **H2 in-memory** for fast test execution.

**Key URLs:**

- API Server: `http://localhost:9000`
- Swagger/OpenAPI Docs: `http://localhost:9000/swagger/index.html`
- Actuator Health: `http://localhost:9001/actuator/health`

## Tech Stack

### Core Framework & Runtime

- **Java**: JDK 25 (LTS) - use modern Java features where appropriate
- **Spring Boot**: 4.0.0 with modular starter dependencies (WebMVC, Data JPA, Validation, Cache, Actuator)
- **Build Tool**: Maven 3.9+ (use `./mvnw` wrapper, NOT system Maven)
- **Database**: SQLite (runtime) with Xerial JDBC driver; H2 in-memory (test scope only)

### Key Dependencies

- **Lombok** 1.18.42: Auto-generate boilerplate code (getters, setters, constructors)
- **ModelMapper** 3.2.6: Entity-to-DTO mapping
- **SpringDoc OpenAPI** 2.8.14: API documentation (Swagger UI)
- **JaCoCo** 0.8.14: Code coverage reporting
- **AssertJ** 3.27.6: Fluent test assertions
- **SQLite JDBC** 3.47.1.0: SQLite database driver (Xerial)
- **Hibernate Community Dialects**: Provides `SQLiteDialect` for JPA/Hibernate

### Testing

- **JUnit 5** (Jupiter): Test framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: `@WebMvcTest`, `@DataJpaTest`, `@AutoConfigureCache`, etc.
- **AssertJ**: Preferred assertion library (use over standard JUnit assertions)

### DevOps & CI/CD

- **Docker**: Multi-stage build with Eclipse Temurin Alpine images and pre-seeded SQLite database
- **Docker Compose**: Local containerized deployment with persistent storage volume
- **GitHub Actions**: CI pipeline with coverage reporting (Codecov, Codacy)

## Project Structure

```
src/main/java/ar/com/nanotaboada/java/samples/spring/boot/
├── Application.java              # Main entry point, @SpringBootApplication
├── controllers/                  # REST endpoints (@RestController)
│   └── BooksController.java
├── services/                     # Business logic (@Service, caching)
│   └── BooksService.java
├── repositories/                 # Data access (@Repository, Spring Data JPA)
│   └── BooksRepository.java
└── models/                       # Domain entities & DTOs
    ├── Book.java                 # JPA entity
    ├── BookDTO.java              # Data Transfer Object with validation
    └── UnixTimestampConverter.java # JPA converter for LocalDate ↔ Unix timestamp

src/test/java/.../test/
├── controllers/                  # Controller tests (@WebMvcTest)
├── services/                     # Service layer tests
├── repositories/                 # Repository tests (@DataJpaTest)
├── BookFakes.java                # Test data factory for Book entities
└── BookDTOFakes.java            # Test data factory for BookDTO

src/main/resources/
├── application.properties        # Application configuration (SQLite)
└── logback-spring.xml           # Logging configuration

src/test/resources/
└── application.properties        # Test configuration (H2 in-memory)

scripts/
├── entrypoint.sh                 # Docker entrypoint (copies seed DB on first run)
└── healthcheck.sh               # Docker health check using Actuator

storage/
└── books-sqlite3.db             # Pre-seeded SQLite database with sample books
```

**Package Naming Convention**: `ar.com.nanotaboada.java.samples.spring.boot.<layer>`
**Test Package Convention**: Add `.test` before layer name (e.g., `...samples.spring.boot.test.controllers`)

## Coding Guidelines

### Architecture & Design Patterns

- **Layered Architecture**: Controller → Service → Repository → Entity
- **DTOs**: Use `BookDTO` for API requests/responses; `Book` entity is internal
- **Dependency Injection**: Use constructor injection (Lombok `@RequiredArgsConstructor` or explicit constructors)
- **Caching**: Service layer uses Spring Cache annotations (`@Cacheable`, `@CachePut`, `@CacheEvict`)
- **Validation**: Use Jakarta Bean Validation annotations on DTOs (`@NotBlank`, `@ISBN`, `@URL`, etc.)

### Java Style

- **Lombok**: Prefer `@Data`, `@RequiredArgsConstructor`, `@NoArgsConstructor`, `@AllArgsConstructor` over manual code
- **Streams**: Use Java Streams API for collection processing (see `BooksService.retrieveAll()`)
- **Modern Java**: Leverage JDK 25 features (records, pattern matching, sealed classes, etc.) where beneficial
- **Comments**: Section dividers used in controllers/services (e.g., `/* HTTP POST */`)

### Testing Conventions

- **Test Class Naming**: `<ClassName>Tests` (plural, e.g., `BooksControllerTests`)
- **Test Method Naming**: `given<Condition>_when<Action>_then<Expected>` (BDD style)
- **Assertions**: Use AssertJ fluent assertions (`assertThat(...).isEqualTo(...)`)
- **Mocking**: Use `@MockitoBean` for Spring beans (new in Spring Boot 4.0), verify interactions with `verify()`
- **Test Data**: Use fake data factories (`BookFakes`, `BookDTOFakes`) for consistent test data
- **Display Names**: Use `@DisplayName` for readable test descriptions
- **Caching in Tests**: Add `@AutoConfigureCache` to slice tests (`@WebMvcTest`, `@DataJpaTest`) when caching is needed

### Coverage Exclusions

JaCoCo excludes from coverage (see `pom.xml` and `codecov.yml`):

- `Application.java` (main class)
- `models/**` package (POJOs with Lombok)
- Test files and resources

## Build & Run Commands

### Maven Commands (ALWAYS use wrapper)

```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Run tests with coverage
./mvnw verify

# Package application
./mvnw package

# Run application locally
./mvnw spring-boot:run

# Skip tests during build (use sparingly)
./mvnw package -DskipTests
```

**Critical Requirements**:

- **ALWAYS use `./mvnw` wrapper** (Unix/macOS) or `mvnw.cmd` (Windows), NOT `mvn`
- **JDK 25 is REQUIRED**: The project targets JDK 25 (LTS)
- **JAVA_HOME must be set**: Maven wrapper requires JAVA_HOME pointing to JDK 25 installation

### Docker Commands

```bash
# Build image
docker compose build

# Start application container
docker compose up

# Start in detached mode
docker compose up -d

# Stop and remove containers
docker compose down

# View logs
docker compose logs -f
```

**Exposed Ports**:

- `9000`: Main API server
- `9001`: Actuator management endpoints

**Persistent Storage**:

The Docker container uses a "hold" pattern for the pre-seeded SQLite database:

1. Build stage copies `storage/books-sqlite3.db` to `/app/hold/` in the image
2. On first container run, `entrypoint.sh` copies the database to `/storage/` volume
3. Subsequent runs use the existing database from the volume
4. To reset: `docker compose down -v` removes volumes, next `up` restores seed data

## Common Tasks & Patterns

### Adding a New REST Endpoint

1. Add method to `BooksController` with appropriate HTTP mapping (`@GetMapping`, `@PostMapping`, etc.)
2. Annotate with `@Operation` and `@ApiResponses` for OpenAPI documentation
3. Implement business logic in `BooksService`
4. Add/update repository method if needed
5. Write controller tests using `@WebMvcTest` and `MockMvc`
6. Write service tests with mocked repository

### Adding a New Entity/Resource

1. Create JPA entity in `models/` with `@Entity`, `@Table`, Lombok annotations
2. Create corresponding DTO with validation annotations
3. Create repository interface extending `CrudRepository<Entity, ID>`
4. Create service class with `@Service` and caching annotations
5. Create controller with `@RestController` and OpenAPI annotations
6. Create test data factories (e.g., `EntityFakes.java`)
7. Write comprehensive tests for all layers

### Updating Dependencies

- Dependencies are managed by Dependabot (daily checks)
- Spring Boot dependencies are grouped and ignored for individual updates
- Manually update versions in `pom.xml` `<properties>` section if needed

### Running Coverage Reports

```bash
./mvnw clean verify
# Report available at: target/site/jacoco/index.html
```

## Troubleshooting

### Build Failures

- **Lombok not working**: Ensure annotation processor is enabled in IDE and `maven-compiler-plugin` includes Lombok path
- **Tests failing**: Tests use H2 in-memory database via `src/test/resources/application.properties`
- **Port already in use**: Change `server.port` in `application.properties` or kill process using ports 9000/9001
- **JAVA_HOME not set**: Run `export JAVA_HOME=$(/usr/libexec/java_home -v 25)` on macOS or set to JDK 25 path on other systems
- **CacheManager errors in tests**: Add `@AutoConfigureCache` annotation to slice tests (`@WebMvcTest`, `@DataJpaTest`)
- **SQLite file not found**: Ensure `storage/books-sqlite3.db` exists for local development

### Docker Issues

- **Container health check failing**: Verify Actuator is accessible at `http://localhost:9001/actuator/health`
- **Build context too large**: Ensure `.dockerignore` excludes `target/` and `.git/`
- **Database not persisting**: Check that `java-samples-spring-boot_storage` volume exists (`docker volume ls`)
- **Stale seed data**: Run `docker compose down -v` to remove volumes and restore fresh seed data on next `up`

### Common Pitfalls

- **Don't use system Maven**: Always use `./mvnw` wrapper
- **Don't modify `Application.java` coverage**: It's excluded by design
- **Don't test Lombok-generated code**: Focus on business logic
- **Repository interfaces**: Custom query methods may not show in coverage (JaCoCo limitation)
- **Spring Boot 4.0 modular packages**: Test annotations like `@WebMvcTest`, `@DataJpaTest`, and `@AutoConfigureCache` are now in modular packages (e.g., `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`)

### SQLite Configuration Notes

- **Date storage**: LocalDate fields are stored as Unix timestamps (INTEGER) for robustness - no parsing issues
- **Converter**: `UnixTimestampConverter` handles LocalDate ↔ epoch seconds conversion via JPA `@Convert`
- **DDL auto**: Use `ddl-auto=none` since the database is pre-seeded (SQLite has limited ALTER TABLE support)
- **Tests use H2**: The converter works seamlessly with both H2 and SQLite databases

## CI/CD Pipeline

### GitHub Actions Workflow (`.github/workflows/maven.yml`)

1. **Verify Job**: Compile, test, generate coverage with `mvn verify`
2. **Coverage Job**: Upload JaCoCo reports to Codecov and Codacy
3. **Container Job**: Build and push Docker image to GitHub Container Registry (on `master` push only)

**Required Secrets**:

- `CODECOV_TOKEN`: Codecov integration token
- `CODACY_PROJECT_TOKEN`: Codacy integration token
- `GITHUB_TOKEN`: Automatically provided for GHCR push

## Contributing

Follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` for new features
- `fix:` for bug fixes
- `chore:` for maintenance tasks

**Commit Style**: Keep commits logical and atomic. Squash checkpoint commits before PR.

**PR Requirements**:

- All tests must pass
- Coverage should not decrease significantly
- Follow existing code style and patterns
- Update API documentation if endpoints change

## Additional Resources

- **Code of Conduct**: See `CODE_OF_CONDUCT.md`
- **Detailed Contributing Guide**: See `CONTRIBUTING.md`
- **Project Philosophy**: Small, incremental changes over large rewrites (per Linus Torvalds quote in CONTRIBUTING.md)
