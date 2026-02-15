# AGENTS.md

> **⚡ Token Efficiency Note**: This file contains complete operational instructions (~2,500 tokens).
> **Auto-loaded**: NO (load explicitly with `#file:AGENTS.md` when you need detailed procedures)
> **When to load**: Complex workflows, troubleshooting, CI/CD setup, detailed architecture questions
> **Related files**: See `#file:.github/copilot-instructions.md` for quick context (auto-loaded, ~500 tokens)

---

## Quick Start

```bash
# Build project with Maven
./mvnw clean install

# Run application
./mvnw spring-boot:run
# Server starts on http://localhost:8080

# View API documentation
# Open http://localhost:8080/swagger-ui.html in browser

# View health and metrics
# Open http://localhost:8080/actuator/health
```

## Java Version

This project requires **JDK 25 (LTS)** specified in `.java-version`.

Maven wrapper (`./mvnw`) is included, so Maven installation is optional.

## Development Workflow

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report (JaCoCo, matches CI)
./mvnw clean test jacoco:report

# View coverage report in browser
open target/site/jacoco/index.html

# Run specific test class
./mvnw test -Dtest=PlayersControllerTests

# Run specific test method
./mvnw test -Dtest=PlayersControllerTests#getAll_playersExist_returnsOkWithAllPlayers

# Run tests without rebuilding
./mvnw surefire:test
```

**Coverage requirement**: Tests must maintain high coverage. JaCoCo reports are generated in `target/site/jacoco/`.

### Code Quality

```bash
# Compile without running tests
./mvnw compile

# Verify code (compile + test)
./mvnw verify

# Clean build artifacts
./mvnw clean

# Run full build (clean + compile + test + package)
./mvnw clean install

# Package as JAR (without tests, faster)
./mvnw clean package -DskipTests
```

**Pre-commit checklist**:

1. Run `./mvnw clean install` - must pass all tests and build successfully
2. Check JaCoCo coverage report - maintain existing coverage levels
3. Ensure code compiles without warnings

**Spring Boot DevTools**: Auto-restart on code changes when running with `./mvnw spring-boot:run` (built-in hot reload).

### Build Artifacts

After building, JAR file is located at:

```
target/java.samples.spring.boot-{version}.jar
```

Run the JAR directly:

```bash
java -jar target/java.samples.spring.boot-*.jar
```

### Database Management

This project uses **SQLite in-memory database for tests** and **SQLite for runtime**.

**Runtime (SQLite)**:

```bash
# Database auto-initializes on first startup
# Pre-seeded database ships in storage/players-sqlite3.db

# To reset database to seed state
rm storage/players-sqlite3.db
# WARNING: spring.jpa.hibernate.ddl-auto=none disables schema generation
# Deleting the DB will cause startup failure - restore from backup or manually reinitialize

# Database location: storage/players-sqlite3.db
```

**Tests (SQLite)**:

- In-memory database per test run (jdbc:sqlite::memory:)
- Automatically cleared after each test
- Configuration in `src/test/resources/application.properties`

## Docker Workflow

```bash
# Build container image
docker compose build

# Start application in container
docker compose up

# Start in detached mode (background)
docker compose up -d

# View logs
docker compose logs -f

# Stop application
docker compose down

# Stop and remove database volume (full reset)
docker compose down -v

# Health check (when running)
curl http://localhost:8080/actuator/health
```

**First run behavior**: Container initializes SQLite database with seed data. Volume persists data between runs.

## CI/CD Pipeline

### Continuous Integration (maven.yml)

**Trigger**: Push to `master` or PR to `master`

**Jobs**:

1. **Setup**: JDK 25 installation, Maven dependency caching
2. **Lint**: Commit message validation (commitlint)
3. **Build**: `./mvnw clean install` (compile + test + package)
4. **Test**: Tests already run during install, coverage reports generated
5. **Coverage**: JaCoCo report upload to Codecov

**Local validation** (run this before pushing):

```bash
# Matches CI exactly
./mvnw clean install

# This runs: clean → compile → test → package
# Coverage report automatically generated at target/site/jacoco/
```

## Project Architecture

**Structure**: Layered architecture (Controller → Service → Repository)

```
src/main/java/ar/com/nanotaboada/java/samples/spring/boot/
├── Application.java              # @SpringBootApplication entry point

├── controllers/                  # REST endpoints
│   └── PlayersController.java    # @RestController, OpenAPI annotations

├── services/                     # Business logic
│   └── PlayersService.java       # @Service, @Cacheable

├── repositories/                 # Data access
│   └── PlayersRepository.java    # @Repository, Spring Data JPA

├── models/                       # Domain models
│   ├── Player.java               # @Entity, JPA model
│   ├── PlayerDTO.java            # Data Transfer Object, validation
│   └── IsoDateConverter.java     # JPA converter for ISO-8601 dates

└── converters/                   # Infrastructure converters
    └── IsoDateConverter.java     # JPA converter

src/test/java/                    # Test classes
  ├── PlayersControllerTests.java
  ├── PlayersServiceTests.java
  └── PlayersRepositoryTests.java
```

**Key patterns**:

- Spring Boot 4 with Spring MVC
- Spring Data JPA for database operations
- Custom validation annotations for PlayerDTO
- OpenAPI 3.0 annotations for Swagger docs
- `@Cacheable` for in-memory caching
- DTOs with Bean Validation (JSR-380)
- Actuator for health monitoring and metrics
- JPA derived queries and custom JPQL examples
- Maven multi-module support ready

## API Endpoints

**Base URL**: `http://localhost:8080`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/players` | Get all players |
| `GET` | `/players/{id}` | Get player by ID |
| `GET` | `/players/search/league/{league}` | Search players by league |
| `GET` | `/players/search/squadnumber/{squadNumber}` | Get player by squad number |
| `POST` | `/players` | Create new player |
| `PUT` | `/players` | Update player |
| `DELETE` | `/players/{id}` | Delete player |
| `GET` | `/actuator/health` | Health check |
| `GET` | `/swagger-ui.html` | API documentation |

## Troubleshooting

### Port already in use

```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Maven dependency issues

```bash
# Force update dependencies
./mvnw clean install -U

# Clear local Maven repository cache (nuclear option)
rm -rf ~/.m2/repository
./mvnw clean install
```

### Compilation errors

```bash
# Verify Java version
java --version  # Should be 25.x

# Clean and rebuild
./mvnw clean compile

# Skip tests if needed to check compilation
./mvnw clean compile -DskipTests
```

### Database locked errors

```bash
# Stop all running instances
pkill -f "spring-boot:run"

# Reset database
rm storage/players-sqlite3.db
```

### Test failures

```bash
# Run tests with verbose output
./mvnw test -X

# Run single test for debugging
./mvnw test -Dtest=PlayersControllerTests#getAll_playersExist_returnsOkWithAllPlayers -X
```

### Maven wrapper issues

```bash
# Make wrapper executable
chmod +x mvnw

# Or use system Maven
mvn clean install
```

### Docker issues

```bash
# Clean slate
docker compose down -v
docker compose build --no-cache
docker compose up
```

## Testing the API

### Using Swagger UI (Recommended)

Open <http://localhost:8080/swagger-ui.html> - Interactive documentation with "Try it out"

### Using curl

```bash
# Health check
curl http://localhost:8080/actuator/health

# Get all players
curl http://localhost:8080/players

# Get player by ID
curl http://localhost:8080/players/1

# Search players by league (Premier League)
curl http://localhost:8080/players/search/league/Premier

# Get player by squad number (Messi #10)
curl http://localhost:8080/players/search/squadnumber/10

# Create player
curl -X POST http://localhost:8080/players \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Leandro",
    "middleName": "Daniel",
    "lastName": "Paredes",
    "dateOfBirth": "1994-06-29",
    "squadNumber": 5,
    "position": "Defensive Midfield",
    "abbrPosition": "DM",
    "team": "AS Roma",
    "league": "Serie A",
    "starting11": false
  }'

# Update player
curl -X PUT http://localhost:8080/players \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Emiliano",
    "middleName": null,
    "lastName": "Martínez",
    "dateOfBirth": "1992-09-02",
    "squadNumber": 23,
    "position": "Goalkeeper",
    "abbrPosition": "GK",
    "team": "Aston Villa FC",
    "league": "Premier League",
    "starting11": true
  }'

# Delete player
curl -X DELETE http://localhost:8080/players/21
```

## Important Notes

- **Never commit secrets**: No API keys, tokens, or credentials in code
- **Test coverage**: Maintain high coverage (use JaCoCo reports)
- **Commit messages**: Follow conventional commits (enforced by commitlint)
- **Java version**: Must use JDK 25 for consistency with CI/CD
- **Maven wrapper**: Always use `./mvnw` instead of `mvn` for consistency
- **Database**: SQLite is for demo/development only - not production-ready
- **SQLite for tests**: Tests use in-memory SQLite (jdbc:sqlite::memory:), runtime uses file-based SQLite
- **OpenAPI annotations**: Required for all new endpoints (Swagger docs)
- **Caching**: Uses Spring's `@Cacheable` - clears on updates/deletes
- **Validation**: Bean Validation (JSR-380) annotations in PlayerDTO
- **ISO-8601 dates**: Dates stored as ISO-8601 strings for SQLite compatibility
- **Search methods**: Demonstrates JPA derived queries (findBySquadNumber) and custom JPQL (findByLeagueContainingIgnoreCase)
- **Squad numbers**: Jersey numbers (natural key) separate from database IDs
