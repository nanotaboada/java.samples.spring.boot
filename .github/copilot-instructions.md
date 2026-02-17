# Copilot Instructions

## Project Overview

REST API for managing football players built with Java 25 and Spring Boot 4. Demonstrates layered architecture (Controller → Service → Repository), Spring Data JPA with SQLite, comprehensive validation, caching, and Swagger documentation.

## Quick Start

```bash
# Development
./mvnw spring-boot:run                      # Run with hot reload (port 9000)
./mvnw clean test                           # Run tests
./mvnw clean test jacoco:report             # Test with coverage

# Docker
docker compose up                           # Start in container
docker compose down -v                      # Reset database

# Documentation
http://localhost:9000/swagger/index.html    # API docs
http://localhost:9001/actuator/health       # Health check
```

## Stack

- Java 25 (LTS, required)
- Spring Boot 4.0.0 (Spring MVC)
- Spring Data JPA + Hibernate
- SQLite (file-based runtime, in-memory tests)
- Maven 3 (use `./mvnw` wrapper)
- Bean Validation (JSR-380)
- Spring Cache (1-hour TTL)
- JUnit 5 + AssertJ + MockMvc
- JaCoCo (coverage)
- SpringDoc OpenAPI 3 (Swagger)
- Lombok (boilerplate reduction)

## Project Patterns

- **Architecture**: Layered (Controller → Service → Repository → JPA)
- **Dependency Injection**: Constructor injection via Lombok `@RequiredArgsConstructor`
- **Error Handling**: `@ControllerAdvice` for global exception handling
- **Validation**: Bean Validation annotations in DTOs (`@NotNull`, `@Min`, etc.)
- **Caching**: Spring `@Cacheable` on service layer (1-hour TTL)
- **DTO Pattern**: ModelMapper for entity ↔ DTO transformations
- **Repository**: Spring Data JPA with derived queries and custom JPQL

## Code Conventions

- **Naming**: camelCase (methods/variables), PascalCase (classes), UPPER_SNAKE_CASE (constants)
- **Files**: Class name matches file name (e.g., `PlayersController.java`)
- **Package Structure**:
  - `controllers/` - REST endpoints (`@RestController`)
  - `services/` - Business logic (`@Service`)
  - `repositories/` - Data access (`@Repository`, extends `JpaRepository`)
  - `models/` - Domain entities (`@Entity`) and DTOs
  - `converters/` - JPA attribute converters
- **Annotations**:
  - Controllers: `@RestController`, `@RequestMapping`, `@Operation` (OpenAPI)
  - Services: `@Service`, `@Transactional`, `@Cacheable`
  - Repositories: `@Repository` (Spring Data JPA)
  - DTOs: `@NotNull`, `@Min`, `@Max` (Bean Validation)
  - Entities: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
- **Lombok**: `@Data`, `@Builder`, `@AllArgsConstructor`, `@RequiredArgsConstructor`
- **Logging**: SLF4J (never `System.out.println`)

## Testing

- **Structure**: `*Tests.java` in `src/test/java/` (mirrors main package structure)
- **Naming Pattern**: `method_scenario_outcome`
  - `method`: Method under test (e.g., `post`, `findById`, `create`)
  - `scenario`: Context (e.g., `playerExists`, `invalidData`, `noMatches`)
  - `outcome`: Expected result (e.g., `returnsPlayer`, `returnsConflict`, `returnsEmpty`)
- **Examples**:

  ```java
  // Controller
  void post_squadNumberExists_returnsConflict()

  // Service
  void create_noConflict_returnsPlayerDTO()

  // Repository
  void findById_playerExists_returnsPlayer()
  ```

- **JavaDoc**: BDD Given/When/Then structure in test comments

  ```java
  /**
   * Given a player with squad number 5 already exists in the database
   * When POST /players is called with a new player using squad number 5
   * Then response status is 409 Conflict
   */
  @Test
  void post_squadNumberExists_returnsConflict() { ... }
  ```

- **Annotations**: `@SpringBootTest`, `@AutoConfigureMockMvc`, `@Test`
- **Assertions**: AssertJ (fluent, e.g., `assertThat(result).isNotNull()`)
- **Mocking**: Mockito for service layer tests
- **Database**: Tests use in-memory SQLite (auto-cleared after each test)
- **Coverage**: Target high coverage (JaCoCo reports in `target/site/jacoco/`)

## Avoid

- Field injection (use constructor injection)
- Using `new` for Spring beans (breaks DI)
- Missing `@Transactional` on service methods that modify data
- Exposing entities directly in controllers (use DTOs)
- `System.out.println` (use SLF4J logging)
- Hardcoded configuration (use `@Value` or `application.properties`)
- Ignoring exceptions (always handle or propagate)
- Testing implementation details (test behavior, not internals)

## Commit Messages

Follow Conventional Commits format (enforced by commitlint in CI):

**Format**: `type(scope): description (#issue)` (max 80 chars)

**Types**: `feat`, `fix`, `docs`, `test`, `refactor`, `chore`, `ci`, `perf`, `style`, `build`

**Examples**:

- `feat(api): add player stats endpoint (#42)`
- `fix(service): resolve cache invalidation bug (#88)`
- `test: adopt BDD Given-When-Then pattern across all tests (#266)`

---

For detailed workflows, troubleshooting, and CI/CD setup, load `#file:AGENTS.md`.
