# CLAUDE.md

## Overview

REST API for managing football players built with Java and Spring Boot. Implements CRUD operations with a layered architecture, Spring Data JPA + SQLite, Bean Validation, Spring Cache, and Swagger documentation. Part of a cross-language comparison study (.NET, Go, Python, Rust, TypeScript).

## Tech Stack

- **Language**: Java 25 (LTS, required)
- **Framework**: Spring Boot 4.0.0 (Spring MVC)
- **ORM**: Spring Data JPA + Hibernate
- **Database**: SQLite (file-based runtime, in-memory for tests)
- **Build**: Maven 3 — always use `./mvnw` wrapper
- **Validation**: Bean Validation (JSR-380)
- **Caching**: Spring `@Cacheable` (simple in-memory, no expiry)
- **Mapping**: ModelMapper
- **Migrations**: Flyway (versioned SQL under `db/migration/`; disabled in tests — tests use Spring SQL init instead)
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
└── converters/         — JPA AttributeConverter for ISO-8601 date handling
src/main/resources/     — application.properties, Logback config
src/main/resources/db/migration/ — Flyway versioned SQL (V1 schema, V2+V3 seed data; add V{N}__ here to change schema)
src/test/java/          — test classes mirroring main structure
src/test/resources/     — test config, schema (ddl.sql), seed data (dml.sql); Flyway disabled in tests
storage/                — SQLite database file (runtime, created and populated by Flyway on first start)
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

Run `/pre-commit` to execute the full pre-commit checklist for this project before proposing a commit.

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

### Creating Issues

This project uses Spec-Driven Development (SDD): discuss in Plan mode first, create a GitHub Issue as the spec artifact, then implement. Always offer to draft an issue before writing code. See the `create-issue` skill for the feature/bug issue templates.

### Key workflows

See the `key-workflows` skill for the step-by-step guides to adding an endpoint, modifying the schema, and what to propose after finishing work.

## Invariants (never change without explicit discussion)

- Port: 9000
- API contract: endpoints, HTTP status codes, and response shapes are fixed; do not change them without explicit discussion
- Commit format: `type(scope): description (#issue)` — max 80 chars
- Conventional Commits types: `feat` `fix` `chore` `docs` `test` `refactor` `ci` `perf`
- `CHANGELOG.md` `[Unreleased]` section must be updated before every commit

## Architecture Decision Records

Architectural decisions are documented in [`docs/adr/`](docs/adr/README.md).
When proposing structural changes, check both this file and the relevant ADR.
When a decision changes, update this file and create or amend the relevant ADR.

## Additional Resources

- **Architecture Decision Records**: [`docs/adr/`](docs/adr/README.md) — 14 ADRs documenting the "why" behind major architectural and technology choices in this project.
- New architecturally significant decisions (framework changes, persistence strategy, API contract changes, test strategy shifts) should include a new ADR in `docs/adr/` following the template in [`docs/adr/template.md`](docs/adr/template.md).

## Claude Code

- Run `/pre-commit` to execute the full pre-commit checklist for this project.
