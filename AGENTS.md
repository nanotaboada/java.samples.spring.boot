# Agent Instructions

## Project Structure

```tree
/src/main/java/...         - Application code
  ├── controllers/         - REST endpoints (@RestController)
  ├── services/            - Business logic (@Service, @Cacheable)
  ├── repositories/        - Data access (JpaRepository)
  ├── models/              - Domain entities (@Entity) and DTOs
  └── converters/          - JPA attribute converters
/src/test/java/...         - Test classes (mirrors main structure)
/src/main/resources/       - application.properties, logback config
/src/test/resources/       - Test config, schema (ddl.sql), seed data (dml.sql)
/storage/                  - SQLite database file (runtime)
/target/                   - Build artifacts, test reports, coverage
/.github/workflows/        - CI/CD (maven.yml)
```

## Common Workflows

### Adding a new endpoint

1. **Define DTO** (if needed) in `models/` with Bean Validation:

   ```java
   @Data
   @Builder
   public class PlayerStatsDTO {
       @NotNull private Integer wins;
       @NotNull private Integer losses;
   }
   ```

2. **Add service method** in `services/PlayersService.java`:

   ```java
   @Transactional(readOnly = true)
   public PlayerStatsDTO getPlayerStats(Long id) {
       Player player = repository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException(...));
       // Business logic
       return PlayerStatsDTO.builder()...build();
   }
   ```

3. **Create controller endpoint** in `controllers/PlayersController.java`:

   ```java
   @Operation(summary = "Get player statistics")
   @GetMapping("/{id}/stats")
   public ResponseEntity<PlayerStatsDTO> getPlayerStats(@PathVariable Long id) {
       return ResponseEntity.ok(service.getPlayerStats(id));
   }
   ```

4. **Add tests** in `test/controllers/PlayersControllerTests.java`:

   ```java
   @Test
   void getPlayerStats_playerExists_returnsStats() {
       // Given/When/Then with MockMvc
   }
   ```

5. **Validate**: Run `./mvnw clean test jacoco:report` - all tests pass, coverage maintained

### Modifying database schema

⚠️ **Manual process** (no migrations framework):

1. Update `@Entity` class in `models/Player.java` (add/modify fields)
2. Update DTOs in `models/PlayerDTO.java` if API contract changes
3. Manually update `storage/players-sqlite3.db` (SQLite CLI or DB Browser)
   - Preserve existing 26 players
   - Or delete and reinitialize from seed data
4. Update service layer methods in `services/PlayersService.java`
5. Update repository queries if needed in `repositories/PlayersRepository.java`
6. Update tests (test data, assertions)
7. Run `./mvnw clean test` to verify

### Running tests

```bash
# All tests
./mvnw test

# With coverage report (matches CI)
./mvnw clean test jacoco:report

# View coverage in browser
open target/site/jacoco/index.html

# Specific test class
./mvnw test -Dtest=PlayersControllerTests

# Specific test method
./mvnw test -Dtest=PlayersControllerTests#post_validPlayer_returnsCreated

# Verbose output for debugging
./mvnw test -X
```

## Autonomy Levels

### Proceed freely

- Route handlers and controller endpoints (following Spring patterns)
- Service layer business logic
- Repository custom queries (derived or `@Query`)
- Unit and integration tests (maintain naming convention)
- Exception handling in `@ControllerAdvice`
- Documentation updates (README, JavaDoc, OpenAPI)
- Bug fixes in business logic
- Refactoring within existing layers
- Utility classes and helper methods

### Ask before changing

- Database schema (entity fields, relationships)
- Dependencies (`pom.xml` - adding new libraries)
- CI/CD configuration (`.github/workflows/maven.yml`)
- Docker setup (`Dockerfile`, `compose.yaml`)
- Application properties (`application.properties` - global config)
- API contracts (breaking changes to DTOs)
- Security configuration
- Caching strategy or TTL values
- Package structure reorganization

### Never modify

- Production configurations
- Deployment secrets or credentials
- `.java-version` (JDK 25 required)
- Maven wrapper scripts (`mvnw`, `mvnw.cmd`)
- Core architectural patterns (layered architecture)
- Test database configuration (in-memory SQLite)
- Port configuration (9000/9001 without discussion)

## Pre-commit Checks

1. **Build**: `./mvnw clean install` - must pass without errors
2. **Tests**: All tests pass (included in `clean install`)
3. **Coverage**: Check `target/site/jacoco/index.html` - maintain existing levels
4. **No warnings**: Compilation completes without warnings
5. **Commit format**: `type(scope): description (#issue)` (max 80 chars, commitlint validates)
6. **Code style**: Follow conventions (Lombok, constructor injection, etc.)

**CI validation**: Push triggers GitHub Actions (`.github/workflows/maven.yml`):

- Lint commit messages (commitlint)
- Build: `./mvnw clean install`
- Test coverage upload to Codecov

## API Testing

```bash
# Health check
curl http://localhost:9001/actuator/health

# Get all players
curl http://localhost:9000/players

# Get player by ID
curl http://localhost:9000/players/1

# Search by league
curl http://localhost:9000/players/search/league/Premier

# Create player
curl -X POST http://localhost:9000/players \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Lionel","lastName":"Messi",...}'

# Swagger UI (recommended)
http://localhost:9000/swagger/index.html
```

## Troubleshooting

### Port 9000 in use

```bash
lsof -ti:9000 | xargs kill -9
```

### Maven dependency issues

```bash
./mvnw clean install -U          # Force update
rm -rf ~/.m2/repository          # Nuclear option
```

### Database locked

```bash
pkill -f "spring-boot:run"       # Stop all instances
rm storage/players-sqlite3.db    # Reset (WARNING: loses data)
```

### Test failures

```bash
./mvnw test -X -Dtest=PlayersControllerTests#specific_test
```

---

For quick reference and code conventions, see `.github/copilot-instructions.md` (auto-loaded).
