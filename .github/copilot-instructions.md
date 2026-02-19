# GitHub Copilot Instructions

## Overview

REST API for managing football players built with Java and Spring Boot. Implements CRUD operations with a layered architecture, Spring Data JPA + SQLite, Bean Validation, Spring Cache, and Swagger documentation. Part of a cross-language comparison study (.NET, Go, Python, Rust, TypeScript).

## Tech Stack

- **Language**: Java 25 (LTS, required)
- **Framework**: Spring Boot 4.0.0 (Spring MVC)
- **ORM**: Spring Data JPA + Hibernate
- **Database**: SQLite (file-based runtime, in-memory for tests)
- **Build**: Maven 3 — always use `./mvnw` wrapper
- **Validation**: Bean Validation (JSR-380)
- **Caching**: Spring `@Cacheable` (1-hour TTL)
- **Mapping**: ModelMapper
- **Logging**: SLF4J
- **Testing**: JUnit 5 + AssertJ + MockMvc + Mockito
- **Coverage**: JaCoCo
- **API Docs**: SpringDoc OpenAPI 3 (Swagger)
- **Boilerplate**: Lombok
- **Containerization**: Docker

## Structure

```text
src/main/java/
├── controllers/        — HTTP handlers; delegate to services, no business logic        [HTTP layer]
├── services/           — Business logic + @Cacheable caching                           [business layer]
├── repositories/       — Spring Data JPA with derived queries                          [data layer]
├── models/             — Player entity + DTOs
└── converters/         — ModelMapper entity ↔ DTO transformations
src/main/resources/     — application.properties, Logback config
src/test/java/          — test classes mirroring main structure
src/test/resources/     — test config, schema (ddl.sql), seed data (dml.sql)
storage/                — SQLite database file (runtime)
```

**Layer rule**: `Controller → Service → Repository → JPA`. Controllers must not access repositories directly. Business logic must not live in controllers.

## Coding Guidelines

- **Naming**: camelCase (methods/variables), PascalCase (classes), UPPER_SNAKE_CASE (constants)
- **Files**: class name matches file name
- **DI**: Constructor injection via Lombok `@RequiredArgsConstructor`; never field injection
- **Annotations**: `@RestController`, `@Service`, `@Repository`, `@Entity`, `@Data`/`@Builder`/`@AllArgsConstructor` (Lombok)
- **Transactions**: `@Transactional(readOnly = true)` on read service methods; `@Transactional` on writes
- **Errors**: `@ControllerAdvice` for global exception handling
- **Logging**: SLF4J only; never `System.out.println`
- **DTOs**: Never expose entities directly in controllers — always use DTOs
- **Tests**: BDD Given-When-Then naming (`givenX_whenY_thenZ`); AssertJ BDD style (`then(result).isNotNull()`); in-memory SQLite auto-clears after each test
- **Avoid**: field injection, `new` for Spring beans, missing `@Transactional`, exposing entities in controllers, hardcoded configuration

## Commands

### Quick Start

```bash
./mvnw spring-boot:run                  # port 9000
./mvnw clean test                       # run tests
./mvnw clean test jacoco:report         # tests + coverage
open target/site/jacoco/index.html      # view coverage report
docker compose up
docker compose down -v
```

### Pre-commit Checks

1. `./mvnw clean install` — must succeed
2. All tests pass
3. Check coverage at `target/site/jacoco/index.html`
4. No compilation warnings
5. Commit message follows Conventional Commits format (enforced by commitlint)

### Commits

Format: `type(scope): description (#issue)` — max 80 chars
Types: `feat` `fix` `chore` `docs` `test` `refactor` `ci` `perf`
Example: `feat(api): add player stats endpoint (#42)`

## Agent Mode

### Proceed freely

- Route handlers and controller endpoints
- Service layer business logic
- Repository custom queries
- Unit and integration tests
- Exception handling in `@ControllerAdvice`
- Documentation updates, bug fixes, and refactoring
- Utility classes and helpers

### Ask before changing

- Database schema (entity fields, relationships)
- Dependencies (`pom.xml`)
- CI/CD configuration (`.github/workflows/`)
- Docker setup
- Application properties
- API contracts (breaking DTO changes)
- Caching strategy or TTL values
- Package structure

### Never modify

- `.java-version` (JDK 25 required)
- Maven wrapper scripts (`mvnw`, `mvnw.cmd`)
- Port configuration (9000/9001)
- Test database configuration (in-memory SQLite)
- Production configurations or deployment secrets

### Key workflows

**Add an endpoint**: Define DTO in `models/` with Bean Validation → add service method in `services/` with `@Transactional` → create controller endpoint with `@Operation` annotation → add tests → run `./mvnw clean test jacoco:report`.

**Modify schema**: Update `@Entity` in `models/Player.java` → update DTOs if API changes → manually update `storage/players-sqlite3.db` (preserve 26 players) → update service, repository, and tests → run `./mvnw clean test`.

**After completing work**: Suggest a branch name (e.g. `feat/add-player-stats`) and a commit message following Conventional Commits including co-author line:

```text
feat(scope): description (#issue)

Co-authored-by: Copilot <175728472+Copilot@users.noreply.github.com>
```
