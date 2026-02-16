# Copilot Instructions

## Project Overview

This is a Spring Boot REST API for managing football players, demonstrating modern Java patterns and clean architecture. It's a learning-focused proof-of-concept that follows Spring Boot best practices with layered architecture (Controller → Service → Repository).

## Stack

- **Java**: 25 (LTS, required for consistency)
- **Spring Boot**: 4.0.0 (with Spring MVC)
- **ORM**: Spring Data JPA with Hibernate
- **Database**: SQLite (file-based for runtime, in-memory for tests)
- **Build Tool**: Maven 3 (use `./mvnw` wrapper, NOT system Maven)
- **Containers**: Docker with Docker Compose
- **Logging**: SLF4J with Logback
- **Monitoring**: Spring Boot Actuator
- **API Docs**: SpringDoc OpenAPI 3 (Swagger UI)

## Project Patterns

- **Layered architecture**: Controller → Service → Repository → Database
- **Dependency injection**: Constructor injection via Lombok's @RequiredArgsConstructor
- **DTO pattern**: Never expose entities in controllers, use DTOs with ModelMapper
- **Caching**: Spring Cache abstraction (@Cacheable) with 1-hour TTL
- **Error handling**: @ControllerAdvice for global exception handling
- **Validation**: Bean Validation (JSR-380) in DTOs
- **Repository queries**: Mix of derived queries (findBySquadNumber) and custom JPQL (@Query)
- **Date handling**: ISO-8601 strings with custom JPA converter for SQLite compatibility

## Code Conventions

### Naming

- **Classes**: PascalCase (e.g., `PlayersController`, `PlayerDTO`)
- **Methods/Variables**: camelCase (e.g., `findById`, `squadNumber`)
- **Test methods**: `method_scenario_outcome` (e.g., `post_squadNumberExists_returnsConflict`)

### Annotations

- **Controllers**: @RestController (never @Controller for REST APIs)
- **DTOs**: Lombok @Data, @Builder, @AllArgsConstructor
- **Services**: @Service + @Transactional on mutating methods
- **Repositories**: @Repository (extend JpaRepository)
- **Entities**: @Entity with @Table, @Id, @GeneratedValue

### Lombok usage

- Reduce boilerplate with @Data, @Builder, @AllArgsConstructor
- Use @RequiredArgsConstructor for constructor injection
- Never use field injection

### REST conventions

- Return ResponseEntity<T> from controllers
- Use proper HTTP status codes (201 Created, 204 No Content, 409 Conflict)
- OpenAPI annotations required on all endpoints

### Logging

- Use SLF4J (injected via Lombok @Slf4j)
- Never use System.out.println

### Configuration

- Externalize via @Value or application.yml
- Never hardcode values in code

## Testing

### Framework

- **Unit tests**: JUnit 5 with AssertJ for fluent assertions
- **Integration tests**: @SpringBootTest with MockMvc
- **Coverage**: JaCoCo reports (must maintain high coverage)

### Test naming

**Pattern**: `givenX_whenY_thenZ()` (BDD Given-When-Then)

**Naming philosophy:**

Use **semantic naming** that describes the business/domain state and behavior, not technical implementation details:

- **Given** (precondition): Describe the state of the system or data
  - `givenPlayerExists` - A player is present in the system
  - `givenNoExistingPlayer` - No player matching criteria
  - `givenInvalidPlayer` - Invalid data provided
  - `givenRaceCondition` - Concurrent modification scenario
  - `givenNullId` - Missing required identifier
- **When** (action): The method/operation being tested
  - `whenCreate` - Creating a new entity
  - `whenRetrieveById` - Fetching by identifier
  - `whenUpdate` - Updating existing entity
  - `whenDelete` - Removing an entity
  - `whenSearchByLeague` - Searching/filtering operation
- **Then** (outcome): Expected result or behavior
  - `thenReturnsPlayerDTO` - Returns the DTO object
  - `thenReturnsNull` - Returns null (not found/conflict)
  - `thenReturnsTrue` / `thenReturnsFalse` - Boolean success indicator
  - `thenReturns26Players` - Specific count of results
  - `thenReturnsEmptyList` - No results found
  - `thenReturnsOk` - HTTP 200 response
  - `thenReturnsNotFound` - HTTP 404 response
  - `thenReturnsCreated` - HTTP 201 response

**Code conventions:**

- Comments: Use **Given/When/Then** (BDD pattern)
- Assertions: Use `then()` from AssertJ BDDAssertions
- Variables:
  - Use `actual` for operation results
  - Use `expected` for comparison values when verifying equality
  - Use `existing` for pre-saved entities in database

**Why BDD Given-When-Then?**

- ✅ **Readable**: Natural language flow, accessible to all stakeholders
- ✅ **Behavior-focused**: Tests business logic, not implementation details
- ✅ **Self-documenting**: Method name clearly states test scenario
- ✅ **Framework-aligned**: Matches Cucumber/SpecFlow patterns

```java
// Examples across layers:
givenNoExistingPlayer_whenCreate_thenReturnsPlayerDTO()     // Service: success case
givenPlayerAlreadyExists_whenCreate_thenReturnsNull()       // Service: conflict case
givenPlayerExists_whenFindById_thenReturnsPlayer()          // Repository: found
givenPlayerDoesNotExist_whenFindById_thenReturnsEmpty()     // Repository: not found
givenValidPlayer_whenPost_thenReturnsCreated()              // Controller: HTTP 201
givenPlayerDoesNotExist_whenGetById_thenReturnsNotFound()   // Controller: HTTP 404
```

### Test structure

Use BDD (Given/When/Then) consistently in JavaDoc comments, method names, and code sections:

```java
/**
 * Given no existing player with the same squad number
 * When create() is called with valid player data
 * Then the player is saved and a PlayerDTO is returned
 */
@Test
void givenNoExistingPlayer_whenCreate_thenReturnsPlayerDTO() {
    // Given
    Player player = PlayerFakes.createOneValid();
    PlayerDTO expected = PlayerDTOFakes.createOneValid();
    // When
    PlayerDTO actual = playersService.create(expected);
    // Then
    then(actual).isEqualTo(expected);
}
```

### Test requirements

- Unit tests required for all service and repository methods
- Integration tests required for all controller endpoints
- Tests use in-memory SQLite (jdbc:sqlite::memory:)
- All tests must pass before PR (`./mvnw clean install`)
- **Assertion quality**: Verify actual behavior, not just counts (e.g., verify league content, not just `hasSize()`)

## Avoid

- **Field injection** - Always use constructor injection
- **Using `new` for Spring beans** - Breaks dependency injection
- **Missing @Transactional** - Required on service methods that modify data
- **Exposing entities in controllers** - Always use DTOs
- **System.out.println** - Use SLF4J logging
- **Hardcoded config** - Use @Value or application.yml
- **System Maven** - Always use `./mvnw` wrapper for consistency
- **SQLite in production** - This is a demo/development-only setup

## Folder Structure

```tree
src/main/java/                    # Application code
  ├── Application.java            # @SpringBootApplication entry point
  ├── controllers/                # REST endpoints
  ├── services/                   # Business logic + caching
  ├── repositories/               # Data access (Spring Data JPA)
  ├── models/                     # Entities (Player) and DTOs (PlayerDTO)
  └── converters/                 # JPA converters (ISO date handling)
src/test/java/                    # Test classes (mirrors main structure)
storage/                          # SQLite database file (runtime)
scripts/                          # Docker entrypoint and healthcheck
```

## Quick Reference

```bash
# Development
./mvnw spring-boot:run              # Run with hot reload
./mvnw clean test jacoco:report     # Test with coverage

# Docker
docker compose up                   # Start in container

# Documentation
http://localhost:8080/swagger-ui.html       # API docs
http://localhost:8080/actuator/health       # Health check
```

## Commit Messages

Follow Conventional Commits with issue number suffix:

- **Format**: `type(scope): description (#issue)` (max 80 chars)
- **Types**: feat, fix, chore, docs, test, refactor
- **Example**: `feat(api): add squad number search endpoint (#123)`

## Additional Context

For detailed operational procedures, workflows, and troubleshooting, see `AGENTS.md`.
