# Copilot Instructions for java.samples.spring.boot

## Project Overview

This is a RESTful Web Service proof-of-concept built with **Spring Boot 3** targeting **JDK 21 (LTS)**. The application demonstrates a clean, layered architecture implementing a CRUD API for managing books. It uses an in-memory H2 database for data persistence and includes comprehensive test coverage.

**Key URLs:**

- API Server: `http://localhost:9000`
- Swagger/OpenAPI Docs: `http://localhost:9000/swagger/index.html`
- Actuator Health: `http://localhost:9001/actuator/health`

## Tech Stack

### Core Framework & Runtime

- **Java**: JDK 21 (LTS) - use modern Java features where appropriate
- **Spring Boot**: 3.4.4 with starter dependencies (Web, Data JPA, Validation, Cache, Actuator)
- **Build Tool**: Maven 3.9+ (use `./mvnw` wrapper, NOT system Maven)
- **Database**: H2 in-memory database (runtime scope)

### Key Dependencies

- **Lombok** 1.18.42: Auto-generate boilerplate code (getters, setters, constructors)
- **ModelMapper** 3.2.6: Entity-to-DTO mapping
- **SpringDoc OpenAPI** 2.8.14: API documentation (Swagger UI)
- **JaCoCo** 0.8.14: Code coverage reporting
- **AssertJ** 3.27.6: Fluent test assertions

### Testing

- **JUnit 5** (Jupiter): Test framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: `@WebMvcTest`, `@DataJpaTest`, etc.
- **AssertJ**: Preferred assertion library (use over standard JUnit assertions)

### DevOps & CI/CD

- **Docker**: Multi-stage build with Eclipse Temurin Alpine images
- **Docker Compose**: Local containerized deployment
- **GitHub Actions**: CI pipeline with coverage reporting (Codecov, Codacy)
- **Azure Pipelines**: Alternative CI configuration

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
    └── BooksDataInitializer.java # Seed data

src/test/java/.../test/
├── controllers/                  # Controller tests (@WebMvcTest)
├── services/                     # Service layer tests
├── repositories/                 # Repository tests (@DataJpaTest)
├── BookFakes.java                # Test data factory for Book entities
└── BookDTOFakes.java            # Test data factory for BookDTO

src/main/resources/
├── application.properties        # Application configuration
└── logback-spring.xml           # Logging configuration

scripts/
├── entrypoint.sh                 # Docker container entrypoint
└── healthcheck.sh               # Docker health check using Actuator
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
- **Modern Java**: Leverage JDK 21 features (records, pattern matching, etc.) where beneficial
- **Comments**: Section dividers used in controllers/services (e.g., `/* HTTP POST */`)

### Testing Conventions

- **Test Class Naming**: `<ClassName>Tests` (plural, e.g., `BooksControllerTests`)
- **Test Method Naming**: `given<Condition>_when<Action>_then<Expected>` (BDD style)
- **Assertions**: Use AssertJ fluent assertions (`assertThat(...).isEqualTo(...)`)
- **Mocking**: Use `@MockBean` for Spring beans, verify interactions with `verify()`
- **Test Data**: Use fake data factories (`BookFakes`, `BookDTOFakes`) for consistent test data
- **Display Names**: Use `@DisplayName` for readable test descriptions

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
- **JDK 21 is REQUIRED**: The project targets JDK 21 (LTS). Using newer JDKs (22+) will cause Mockito/ByteBuddy compatibility issues in tests
- **JAVA_HOME must be set**: Maven wrapper requires JAVA_HOME pointing to JDK 21 installation

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
- **Tests failing**: Check if H2 database is properly initialized; review `BooksDataInitializer.seed()`
- **Port already in use**: Change `server.port` in `application.properties` or kill process using ports 9000/9001
- **JAVA_HOME not set**: Run `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` on macOS or set to JDK 21 path on other systems
- **JDK version errors**: Project requires JDK 21 (LTS). Using JDK 22+ causes Mockito/ByteBuddy failures like "Java 25 (69) is not supported"

### Docker Issues

- **Container health check failing**: Verify Actuator is accessible at `http://localhost:9001/actuator/health`
- **Build context too large**: Ensure `.dockerignore` excludes `target/` and `.git/`

### Common Pitfalls

- **Don't use system Maven**: Always use `./mvnw` wrapper
- **Don't use newer JDKs**: Stick to JDK 21 - newer versions break Mockito in tests
- **Don't modify `Application.java` coverage**: It's excluded by design
- **Don't test Lombok-generated code**: Focus on business logic
- **Repository interfaces**: Custom query methods may not show in coverage (JaCoCo limitation)

## CI/CD Pipeline

### GitHub Actions Workflow (`.github/workflows/maven.yml`)

1. **Verify Job**: Compile, test, generate coverage with `mvn verify`
2. **Coverage Job**: Upload JaCoCo reports to Codecov and Codacy
3. **Container Job**: Build and push Docker image to GitHub Container Registry (on `master` push only)

**Required Secrets**:

- `CODECOV_TOKEN`: Codecov integration token
- `CODACY_PROJECT_TOKEN`: Codacy integration token
- `GITHUB_TOKEN`: Automatically provided for GHCR push

### Azure Pipelines (`azure-pipelines.yml`)

- Runs on `ubuntu-latest` with JDK 21
- Executes `mvn package` and publishes test results

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
