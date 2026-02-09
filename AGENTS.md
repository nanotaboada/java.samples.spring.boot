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
./mvnw test -Dtest=BooksControllerTest

# Run specific test method
./mvnw test -Dtest=BooksControllerTest#testGetAllBooks

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

This project uses **H2 in-memory database for tests** and **SQLite for runtime**.

**Runtime (SQLite)**:

```bash
# Database auto-initializes on first startup
# Pre-seeded database ships in storage/books-sqlite3.db

# To reset database to seed state
rm storage/books-sqlite3.db
# WARNING: spring.jpa.hibernate.ddl-auto=none disables schema generation
# Deleting the DB will cause startup failure - restore from backup or manually reinitialize

# Database location: storage/books-sqlite3.db
```

**Tests (H2)**:

- In-memory database per test run
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
│   └── BooksController.java      # @RestController, OpenAPI annotations

├── services/                     # Business logic
│   └── BooksService.java         # @Service, @Cacheable

├── repositories/                 # Data access
│   └── BooksRepository.java      # @Repository, Spring Data JPA

└── models/                       # Domain models
    ├── Book.java                 # @Entity, JPA model
    ├── BookDTO.java              # Data Transfer Object, validation
    └── UnixTimestampConverter.java  # JPA converter

src/test/java/                    # Test classes
  ├── BooksControllerTest.java
  ├── BooksServiceTest.java
  └── BooksRepositoryTest.java
```

**Key patterns**:

- Spring Boot 4 with Spring MVC
- Spring Data JPA for database operations
- Custom validation annotations for ISBN and URL
- OpenAPI 3.0 annotations for Swagger docs
- `@Cacheable` for in-memory caching
- DTOs with Bean Validation (JSR-380)
- Actuator for health monitoring and metrics
- Maven multi-module support ready

## API Endpoints

**Base URL**: `http://localhost:8080`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/books` | Get all books |
| `GET` | `/books/{id}` | Get book by ID |
| `POST` | `/books` | Create new book |
| `PUT` | `/books/{id}` | Update book |
| `DELETE` | `/books/{id}` | Delete book |
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
rm storage/books.db
```

### Test failures

```bash
# Run tests with verbose output
./mvnw test -X

# Run single test for debugging
./mvnw test -Dtest=BooksControllerTest#testGetAllBooks -X
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

# Get all books
curl http://localhost:8080/books

# Get book by ID
curl http://localhost:8080/books/1

# Create book
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "9780132350884",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "published": 1217548800,
    "pages": 464,
    "description": "A Handbook of Agile Software Craftsmanship",
    "website": "https://www.pearson.com/en-us/subject-catalog/p/clean-code-a-handbook-of-agile-software-craftsmanship/P200000009044"
  }'

# Update book
curl -X PUT http://localhost:8080/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "9780132350884",
    "title": "Clean Code - Updated",
    "author": "Robert C. Martin",
    "published": 1217548800,
    "pages": 464,
    "description": "Updated description",
    "website": "https://www.pearson.com/example"
  }'

# Delete book
curl -X DELETE http://localhost:8080/books/1
```

## Important Notes

- **Never commit secrets**: No API keys, tokens, or credentials in code
- **Test coverage**: Maintain high coverage (use JaCoCo reports)
- **Commit messages**: Follow conventional commits (enforced by commitlint)
- **Java version**: Must use JDK 25 for consistency with CI/CD
- **Maven wrapper**: Always use `./mvnw` instead of `mvn` for consistency
- **Database**: SQLite is for demo/development only - not production-ready
- **H2 for tests**: Tests use in-memory H2, runtime uses SQLite
- **OpenAPI annotations**: Required for all new endpoints (Swagger docs)
- **Caching**: Uses Spring's `@Cacheable` - clears on updates/deletes
- **Validation**: Custom ISBN and URL validators in BookDTO
- **Unix timestamps**: Published dates stored as Unix timestamps (seconds since epoch)
