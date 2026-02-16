# Agent Instructions

## Project Overview

This is a Spring Boot 4 REST API for managing football players, built as a learning-focused proof-of-concept demonstrating modern Java patterns, clean layered architecture, and Spring Boot best practices. The application uses SQLite for development simplicity, Spring Data JPA for data access, and follows a strict DTO pattern to separate concerns between API contracts and domain models. It's designed to showcase enterprise patterns (caching, validation, OpenAPI docs, health monitoring) in a beginner-friendly, self-contained package.

## Folder Structure

```tree
/src/main/java/                   - Application code (layered architecture)
  ├── Application.java            - @SpringBootApplication entry point
  ├── controllers/                - REST endpoints (@RestController)
  ├── services/                   - Business logic (@Service, caching)
  ├── repositories/               - Data access (Spring Data JPA)
  ├── models/                     - Entities and DTOs
  └── converters/                 - JPA converters (ISO-8601 date handling)
/src/test/java/                   - Test classes (mirrors main structure)
  ├── controllers/                - Integration tests (@SpringBootTest, MockMvc)
  ├── services/                   - Service unit tests (mocked dependencies)
  ├── repositories/               - Repository tests (in-memory SQLite)
  └── test/                       - Test data factories (PlayerFakes, PlayerDTOFakes)
/src/main/resources/              - Configuration files
  ├── application.properties       - Runtime config (SQLite file-based)
  └── logback-spring.xml          - Logging configuration
/src/test/resources/              - Test configuration
  ├── application.properties       - Test config (SQLite in-memory)
  ├── ddl.sql                     - Test database schema
  └── dml.sql                     - Test seed data
/storage/                         - SQLite database file (runtime)
/scripts/                         - Docker entrypoint and healthcheck
/.github/workflows/               - CI/CD pipelines (maven.yml)
pom.xml                           - Maven dependencies and build config
compose.yaml                      - Docker Compose setup
Dockerfile                        - Container image definition
```

## Common Workflows

### Adding a new endpoint

1. Create DTO in `/src/main/java/.../models/` (add validation annotations)
2. Add method to `PlayersService.java` (add @Transactional if mutating)
3. Add endpoint to `PlayersController.java` (use @RestController, OpenAPI annotations)
4. Add integration test in `/src/test/java/.../controllers/PlayersControllerTests.java`
5. Ensure proper HTTP status codes (200/201/204/404/409) and `ResponseEntity<T>`
6. Run `./mvnw clean test` to verify all tests pass

### Modifying database schema

1. Update `Player.java` entity (JPA annotations: @Column, @Id, etc.)
2. Update `PlayerDTO.java` to match (add validation: @NotNull, @Size, etc.)
3. Update fake data generators in `/src/test/java/.../test/` (PlayerFakes, PlayerDTOFakes)
4. **Important**: SQLite schema NOT auto-generated (spring.jpa.hibernate.ddl-auto=none)
   - For runtime: Manually update `/storage/players-sqlite3.db` or recreate from backup
   - For tests: Update `/src/test/resources/ddl.sql` and `/src/test/resources/dml.sql`
5. Add tests for schema changes (repository and service layers)
6. Update affected services and controllers
7. Run `./mvnw clean install` to ensure everything compiles and tests pass

### Running tests

- **All tests**: `./mvnw test`
- **With coverage**: `./mvnw clean test jacoco:report` (view: `open target/site/jacoco/index.html`)
- **Single test class**: `./mvnw test -Dtest=PlayersControllerTests`
- **Single test method**: `./mvnw test -Dtest=PlayersControllerTests#getAll_playersExist_returnsOkWithAllPlayers`
- **Integration tests require**: In-memory SQLite (automatic, no docker needed)
- **Full CI pipeline locally**: `./mvnw clean install` (compile + test + package + jacoco)

### Running the application

1. **Local development**: `./mvnw spring-boot:run` (with auto-reload via Spring Boot DevTools)
2. **Packaged JAR**: `./mvnw clean package && java -jar target/java.samples.spring.boot-*.jar`
3. **Docker**: `docker compose up` (or `docker compose up -d` for background)
4. **Application URLs**:
   - API: `http://localhost:8080/players`
   - Swagger: `http://localhost:8080/swagger-ui.html`
   - Health: `http://localhost:8080/actuator/health`

### Adding a search/query endpoint

1. Add repository method in `PlayersRepository.java`:
   - Derived query: `Optional<Player> findBySquadNumber(Integer squadNumber);`
   - Custom JPQL: `@Query("SELECT p FROM Player p WHERE ...") List<Player> customQuery(...);`
2. Add service method in `PlayersService.java` (add @Cacheable if appropriate)
3. Add controller endpoint with OpenAPI annotations
4. Add integration test in `PlayersControllerTests.java`
5. Run tests: `./mvnw clean test`

### Fixing a bug or adding validation

1. Identify the layer (controller, service, repository, model)
2. Write a failing test first (TDD approach)
3. Implement fix in the appropriate layer
4. Ensure test passes and coverage is maintained
5. Check side effects: `./mvnw clean install`
6. Review JaCoCo report: `open target/site/jacoco/index.html`

### Troubleshooting common issues

**Port already in use (8080)**:

```bash
lsof -ti:8080 | xargs kill -9
```

**Maven dependency issues**:

```bash
./mvnw clean install -U  # Force update dependencies
```

**Database locked errors**:

```bash
pkill -f "spring-boot:run"  # Stop all instances
rm storage/players-sqlite3.db  # Reset database (WARNING: see schema notes above)
```

**Compilation errors**:

```bash
java --version  # Verify Java 25
./mvnw clean compile  # Clean rebuild
```

**Test failures**:

```bash
./mvnw test -X -Dtest=FailingTestClass  # Verbose output for debugging
```

**Docker issues**:

```bash
docker compose down -v && docker compose build --no-cache && docker compose up
```

## Autonomy Levels

### Proceed freely (no approval needed)

- Add new REST endpoints (GET, POST, PUT, DELETE)
- Add service layer methods (with @Transactional where needed)
- Add repository queries (derived or custom JPQL)
- Write unit and integration tests
- Add or modify DTOs (with validation annotations)
- Fix bugs in controllers, services, or repositories
- Update JavaDoc and inline comments
- Add Lombok annotations to reduce boilerplate
- Add OpenAPI/Swagger annotations
- Refactor code within existing patterns (e.g., extract method, rename variables)
- Update README or documentation files
- Add logging statements (using SLF4J)

### Ask before changing

- Database schema modifications (entity field changes, new tables)
  - Must update both `/storage/players-sqlite3.db` AND `/src/test/resources/ddl.sql`
  - Schema is NOT auto-generated (spring.jpa.hibernate.ddl-auto=none)
- Maven dependencies in `pom.xml` (new libraries, version upgrades)
- Docker configuration (`Dockerfile`, `compose.yaml`)
- CI/CD workflows (`.github/workflows/maven.yml`)
- Spring Boot configuration (`application.properties`, major setting changes)
- Caching strategy changes (TTL, cache names, eviction policies)
- Adding new architectural layers or patterns (e.g., event system, message queue)
- Changing test framework or runner configuration
- Major refactorings that span multiple layers

### Never modify without explicit permission

- `.env` files (if present)
- Production configurations
- Deployment secrets or credentials
- Git hooks or repository settings
- License files (`LICENSE`, `CODE_OF_CONDUCT.md`, `CONTRIBUTING.md`)
- Existing seed data in `/storage/players-sqlite3.db` (can read, but don't overwrite)

## Pre-commit Checks

Before committing code, ensure these pass:

1. **Full build**: `./mvnw clean install`
   - Compiles all code without warnings
   - All tests pass (unit + integration)
   - JaCoCo coverage report generated

2. **Code coverage**: `open target/site/jacoco/index.html`
   - Maintain or improve existing coverage levels
   - New code should have corresponding tests

3. **Commit message format**: `type(scope): description (#issue)`
   - Valid types: `feat`, `fix`, `chore`, `docs`, `test`, `refactor`
   - Max 80 characters
   - Example: `feat(api): add squad number search endpoint (#123)`
   - Enforced by commitlint in CI

4. **No compilation warnings**: Code must compile cleanly

5. **Manual smoke test** (optional but recommended):
   - Start app: `./mvnw spring-boot:run`
   - Test endpoint in Swagger UI: `http://localhost:8080/swagger-ui.html`
   - Verify health endpoint: `http://localhost:8080/actuator/health`

## Cross-repo Context

This repository is part of a larger "samples" project demonstrating the same REST API pattern across multiple technology stacks. Other implementations include Node.js/Express, Python/Flask, C#/.NET, Go, Rust, etc.

**Consistency requirements**:

- Same API contract (endpoints, request/response formats)
- Same domain model (Player entity with identical fields)
- Same seed data (21 football players)
- Similar layered architecture (Controller → Service → Repository)
- Equivalent test coverage and structure

**When making changes**, consider:

- Is this change relevant to all stack implementations?
- Should this pattern be documented for reuse in other stacks?
- Does this maintain the learning-first philosophy of the project?
