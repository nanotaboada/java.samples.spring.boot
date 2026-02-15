# GitHub Copilot Instructions

> **Token Efficiency Note**: This is a minimal pointer file (~500 tokens, auto-loaded by Copilot).
> For complete operational details, reference: `#file:AGENTS.md` (~2,500 tokens, loaded on-demand)
> For specialized knowledge, use: `#file:SKILLS/<skill-name>/SKILL.md` (loaded on-demand when needed)

## Quick Context

**Project**: Spring Boot REST API demonstrating modern Java patterns
**Stack**: Java 25 (LTS) • Spring Boot 4 • JPA/Hibernate • SQLite • Maven • Docker
**Pattern**: Controller → Service → Repository → JPA (layered architecture)
**Philosophy**: Learning-focused PoC emphasizing Spring Boot best practices

## Core Conventions

- **Naming**: camelCase (methods/variables), PascalCase (classes)
- **Annotations**: Use Spring stereotypes (@RestController, @Service, @Repository)
- **Lombok**: Reduce boilerplate (@Data, @Builder, @AllArgsConstructor)
- **Dependency Injection**: Constructor injection (Lombok @RequiredArgsConstructor)
- **Testing**: JUnit 5 + AssertJ for fluent assertions
- **Build**: Use `./mvnw` wrapper, NOT system Maven
- **Commit Messages**: Follow Conventional Commits with issue number suffix
  - Format: `type(scope): description (#issue)` (max 80 chars)
  - Example: `docs: optimize AI agent instructions for token efficiency (#259)`
  - Types: `feat`, `fix`, `chore`, `docs`, `test`, `refactor`

## Test Naming Convention

**Pattern**: `method_scenario_outcome`

- **method**: The method being tested (e.g., `post`, `findById`, `create`)
- **scenario**: The context or condition (e.g., `playerExists`, `invalidData`, `noMatches`)
- **outcome**: The expected result (e.g., `returnsPlayer`, `returnsConflict`, `returnsEmpty`)

**Examples**:
```java
// Controller: post_squadNumberExists_returnsConflict()
// Service:    create_noConflict_returnsPlayerDTO()
// Repository: findById_playerExists_returnsPlayer()
```

**JavaDoc**: Use proper BDD (Given/When/Then) structure in comments:
```java
/**
 * Given a player with squad number 5 already exists in the database
 * When POST /players is called with a new player using squad number 5
 * Then response status is 409 Conflict
 */
@Test
void post_squadNumberExists_returnsConflict() { ... }
```

**Benefits**: Concise method names for IDE test runners, full BDD context in JavaDoc for code readability.

## Architecture at a Glance

```
Controller → Service → Repository → JPA → Database
     ↓          ↓           ↓
Validation  Cache    Query Methods
```

- **Controllers**: REST endpoints with @RestController
- **Services**: Business logic with @Service + caching
- **Repositories**: JpaRepository with derived queries
- **DTOs**: ModelMapper for entity ↔ DTO transformations
- **Cache**: Spring Cache abstraction (1-hour TTL)

## Copilot Should

- Generate idiomatic Spring Boot code with proper annotations
- Use JPA repository patterns (derived queries, @Query)
- Follow REST conventions with ResponseEntity<T>
- Write tests with @SpringBootTest and MockMvc
- Apply Lombok annotations to reduce boilerplate
- Use ModelMapper for DTO transformations
- Implement proper exception handling with @ControllerAdvice

## Copilot Should Avoid

- Field injection (use constructor injection)
- Using `new` for services (breaks DI)
- Missing @Transactional on service methods
- Exposing entities directly in controllers (use DTOs)
- System.out.println (use SLF4J logging)
- Hardcoded configuration (use @Value or application.yml)

## Quick Commands

```bash
# Run with hot reload
./mvnw spring-boot:run

# Test with coverage
./mvnw clean test jacoco:report

# Docker
docker compose up

# Swagger: http://localhost:9000/swagger-ui/index.html
# Actuator: http://localhost:9001/actuator/health
```

## Need More Detail?

**For operational procedures**: Load `#file:AGENTS.md`
**For Docker expertise**: *(Planned)* `#file:SKILLS/docker-containerization/SKILL.md`
**For testing patterns**: *(Planned)* `#file:SKILLS/testing-patterns/SKILL.md`

---

**Why this structure?** Copilot auto-loads this file on every chat (~500 tokens). Loading `AGENTS.md` or `SKILLS/` explicitly gives you deep context only when needed, saving 80% of your token budget!
