# 🧪 RESTful API with Java and Spring Boot

[![Java CI with Maven](https://github.com/nanotaboada/java.samples.spring.boot/actions/workflows/maven-ci.yml/badge.svg)](https://github.com/nanotaboada/java.samples.spring.boot/actions/workflows/maven-ci.yml)
[![CodeQL Advanced](https://github.com/nanotaboada/java.samples.spring.boot/actions/workflows/codeql.yml/badge.svg)](https://github.com/nanotaboada/java.samples.spring.boot/actions/workflows/codeql.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nanotaboada_java.samples.spring.boot&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=nanotaboada_java.samples.spring.boot)
[![codecov](https://codecov.io/gh/nanotaboada/java.samples.spring.boot/branch/master/graph/badge.svg?token=D3FMNG0WOI)](https://codecov.io/gh/nanotaboada/java.samples.spring.boot)
[![CodeFactor](https://www.codefactor.io/repository/github/nanotaboada/java.samples.spring.boot/badge)](https://www.codefactor.io/repository/github/nanotaboada/java.samples.spring.boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-3DA639.svg)](https://opensource.org/licenses/MIT)
![Dependabot](https://img.shields.io/badge/Dependabot-contributing-025E8C?logo=dependabot&logoColor=white&labelColor=181818)
![GitHub Copilot](https://img.shields.io/badge/GitHub_Copilot-contributing-8662C5?logo=githubcopilot&logoColor=white&labelColor=181818)
![Claude](https://img.shields.io/badge/Claude-Sonnet_4.6-D97757?logo=claude&logoColor=white&labelColor=181818)

Proof of Concept for a RESTful Web Service built with **Spring Boot 4** targeting **JDK 25 (LTS)**. This project demonstrates best practices for building a layered, testable, and maintainable API implementing CRUD operations for a Players resource (Argentina 2022 FIFA World Cup squad).

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [API Reference](#api-reference)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
  - [Clone](#clone)
  - [Build](#build)
  - [Run](#run)
  - [Access](#access)
- [Testing](#testing)
- [Containers](#containers)
  - [Build and Start](#build-and-start)
  - [Stop](#stop)
  - [Reset Database](#reset-database)
- [Environment Variables](#environment-variables)
- [Command Summary](#command-summary)
- [Releases](#releases)
- [Contributing](#contributing)
- [Legal](#legal)

## Features

- 🔌 **RESTful API** - Full CRUD operations for Players resource
- 📚 **Clean Architecture** - Layered design with clear separation of concerns
- 🚦 **Input Validation** - Bean Validation (JSR-380) constraints
- ⚡ **Performance Caching** - Optimized data retrieval with cache annotations
- 🔍 **Advanced Search** - League search with JPQL and squad number lookup with derived queries
- 📝 **Interactive Documentation** - Live API exploration and testing interface
- 🩺 **Health Monitoring** - Application health and metrics endpoints
- ✅ **Comprehensive Testing** - High code coverage with automated reporting
- 🐳 **Containerized Deployment** - Multi-stage builds with pre-seeded database
- 🔄 **Automated Pipeline** - Continuous integration with automated testing and builds

## Tech Stack

| Component | Technology |
| --------- | ---------- |
| **Framework** | [Spring Boot](https://github.com/spring-projects/spring-boot) 4.0.0 |
| **Runtime** | [Java](https://github.com/openjdk/jdk) (JDK 25 LTS) |
| **Build Tool** | [Maven](https://github.com/apache/maven) |
| **Database (Runtime)** | [SQLite](https://github.com/sqlite/sqlite) |
| **Database (Tests)** | [SQLite](https://github.com/sqlite/sqlite) (in-memory) |
| **ORM** | [Hibernate](https://github.com/hibernate/hibernate-orm) / [Spring Data JPA](https://github.com/spring-projects/spring-data-jpa) |
| **API Documentation** | [SpringDoc OpenAPI](https://github.com/springdoc/springdoc-openapi) |
| **Testing** | [JUnit 5](https://github.com/junit-team/junit5) + [Mockito](https://github.com/mockito/mockito) + [AssertJ](https://github.com/assertj/assertj) |
| **Code Coverage** | [JaCoCo](https://github.com/jacoco/jacoco) |
| **Containerization** | [Docker](https://github.com/docker) + [Docker Compose](https://github.com/docker/compose) |
| **CI/CD** | [GitHub Actions](https://github.com/features/actions) |

> 💡 **Note:** Maven wrapper (`./mvnw`) is included, so Maven installation is optional.

## Project Structure

```tree
src/main/java/ar/com/nanotaboada/java/samples/spring/boot/
├── Application.java              # Main entry point, @SpringBootApplication
├── controllers/                  # REST endpoints (@RestController)
│   └── PlayersController.java
├── services/                     # Business logic (@Service, caching)
│   └── PlayersService.java
├── repositories/                 # Data access (@Repository, Spring Data JPA)
│   └── PlayersRepository.java
├── models/                       # Domain entities & DTOs
│   ├── Player.java               # JPA entity
│   └── PlayerDTO.java            # Data Transfer Object with validation
└── converters/                   # Infrastructure converters
    └── IsoDateConverter.java     # JPA converter for ISO-8601 dates

src/test/java/.../test/
├── controllers/                  # Controller tests (@WebMvcTest)
├── services/                     # Service layer tests
├── repositories/                 # Repository tests (@DataJpaTest)
├── PlayerFakes.java              # Test data factory for Player entities
└── PlayerDTOFakes.java           # Test data factory for PlayerDTO
```

## Architecture

Layered architecture with dependency injection via Spring Boot's IoC container and constructor injection using Lombok's `@RequiredArgsConstructor`.

```mermaid

%%{init: {
  "theme": "default",
  "themeVariables": {
    "fontFamily": "Fira Code, Consolas, monospace",
    "textColor": "#555",
    "lineColor": "#555",
    "clusterBkg": "#f5f5f5",
    "clusterBorder": "#ddd"
  }
}}%%

graph RL

    tests[tests]

    subgraph Layer1[" "]
        Application[Application]
        SpringBoot[Spring Boot]
        SpringDoc[SpringDoc]
    end

    subgraph Layer2[" "]
        controllers[controllers]
        SpringValidation[Spring Validation]
    end

    subgraph Layer3[" "]
        services[services]
        ModelMapper[ModelMapper]
        SpringCache[Spring Cache]
    end

    subgraph Layer4[" "]
        repositories[repositories]
        SpringDataJPA[Spring Data JPA]
    end

    models[models]
    JakartaPersistence[Jakarta Persistence]
    ProjectLombok[Lombok]

    %% Strong dependencies

    %% Layer 1
    SpringBoot --> Application
    SpringDoc --> Application
    controllers --> Application
    services --> Application
    repositories --> Application

    %% Layer 2
    SpringValidation --> controllers
    services --> controllers
    models --> controllers

    %% Layer 3
    SpringCache --> services
    ModelMapper --> services
    repositories --> services
    models --> services

    %% Layer 4
    SpringDataJPA --> repositories
    models --> repositories

    %% Soft dependencies

    controllers -.-> tests
    services -.-> tests
    repositories -.-> tests

    %% Cross-cutting

    JakartaPersistence --> models
    ProjectLombok --> models

    %% Node styling with stroke-width
    classDef core fill:#b3d9ff,stroke:#6db1ff,stroke-width:2px,color:#555,font-family:monospace;
    classDef feat fill:#ffffcc,stroke:#fdce15,stroke-width:2px,color:#555,font-family:monospace;
    classDef deps fill:#ffcccc,stroke:#ff8f8f,stroke-width:2px,color:#555,font-family:monospace;
    classDef test fill:#ccffcc,stroke:#53c45e,stroke-width:2px,color:#555,font-family:monospace;

    class Application,models,repositories,services,controllers core
    class SpringBoot,SpringDataJPA,SpringCache,SpringValidation feat
    class JakartaPersistence,ProjectLombok,ModelMapper,SpringDoc deps
    class tests test
```

*Simplified, conceptual view — not all components or dependencies are shown.*

### Arrow Semantics

Arrows follow the injection direction: `A --> B` means A is injected into B. Solid arrows (`-->`) represent active Spring dependencies — beans wired by the IoC container and invoked at runtime. Dotted arrows (`-.->`) represent test dependencies — test classes reference the types they exercise but are not injected into them.

### Composition Root Pattern

`Application` is the composition root: `@SpringBootApplication` triggers component scanning that discovers and registers all beans, `@EnableCaching` activates the caching infrastructure, and `@Bean ModelMapper` declares the mapping dependency explicitly. Constructor injection is enforced throughout via Lombok's `@RequiredArgsConstructor` on `final` fields.

### Layered Architecture

Four layers: Initialization (`Application`), HTTP (`controllers`), Business (`services`), and Data (`repositories`).

Spring and third-party packages are placed inside the subgraph of the layer that uses them — co-residency communicates the relationship without extra arrows: `Spring Boot` and `SpringDoc` in Initialization, `Spring Validation` in HTTP, `Spring Cache` and `ModelMapper` in Business, `Spring Data JPA` in Data.

`models` and `converters` are cross-cutting: `models` defines the JPA entity and DTOs shared across all layers; `converters` holds the `AttributeConverter` that handles ISO-8601 date serialization for `models`. `Jakarta Persistence` and `Lombok` are their respective dependencies. `Lombok` is also used in `services` and `controllers` via `@RequiredArgsConstructor` and `@Slf4j`, though those arrows are omitted for clarity.

### Color Coding

Blue = core application packages, yellow = Spring ecosystem, red = third-party libraries, green = tests.

## API Reference

Interactive API documentation is available via Swagger UI at `http://localhost:9000/swagger/index.html` when the server is running.

**Quick Reference:**

- `GET /players` - List all players
- `GET /players/{id}` - Get player by ID
- `GET /players/search/league/{league}` - Search players by league
- `GET /players/squadnumber/{squadNumber}` - Get player by squad number
- `POST /players` - Create new player
- `PUT /players/{id}` - Update existing player
- `DELETE /players/{id}` - Remove player
- `GET /actuator/health` - Health check

For complete endpoint documentation with request/response schemas, explore the [interactive Swagger UI](http://localhost:9000/swagger/index.html). You can also access the OpenAPI JSON specification at `http://localhost:9000/v3/api-docs`.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 25**
- **Maven 3.9+** (optional) - Project includes Maven wrapper (`./mvnw`)
- **Docker** (optional) - For containerized deployment

> 💡 **Note:** macOS users may need to set `JAVA_HOME`:
>
> ```bash
> export JAVA_HOME=$(/usr/libexec/java_home -v 25)
> ```

## Quick Start

### Clone

```bash
git clone https://github.com/nanotaboada/java.samples.spring.boot.git
cd java.samples.spring.boot
```

### Build

```bash
./mvnw clean package
```

### Run

```bash
./mvnw spring-boot:run
```

### Access

Once the application is running, you can access:

- **API Server**: `http://localhost:9000`
- **Swagger UI**: `http://localhost:9000/swagger/index.html`
- **OpenAPI Spec**: `http://localhost:9000/v3/api-docs`
- **Health Check**: `http://localhost:9001/actuator/health`

## Testing

Run the full test suite with coverage:

```bash
./mvnw verify
```

**View Coverage Report:**

```bash
open target/site/jacoco/index.html
```

**Test Structure:**

- **Unit Tests** - `@WebMvcTest`, `@DataJpaTest` for isolated layer testing (with `@AutoConfigureCache` for caching support)
- **Test Database** - SQLite in-memory (jdbc:sqlite::memory:) for fast, isolated test execution
- **Mocking** - Mockito with `@MockitoBean` for dependency mocking
- **Assertions** - AssertJ fluent assertions
- **Naming Convention** - `givenX_whenY_thenZ` BDD pattern:
  - `givenPlayersExist_whenGetAll_thenReturnsOkWithAllPlayers()`
  - `givenSquadNumberExists_whenPost_thenReturnsConflict()`
  - `givenPlayerExists_whenFindById_thenReturnsPlayer()`

**Coverage Targets:**

- Controllers: 100%
- Services: 100%
- Repositories: Custom query methods (interfaces excluded by JaCoCo design)

> 💡 **Note:** Dates are stored as ISO-8601 strings for SQLite compatibility. A JPA `AttributeConverter` handles LocalDate ↔ ISO-8601 string conversion transparently. Tests use SQLite in-memory database (jdbc:sqlite::memory:) - the converter works seamlessly with both file-based and in-memory SQLite.

## Containers

### Build and Start

```bash
docker compose up
# or detached mode
docker compose up -d
```

**Exposed Ports:**

- `9000` - Main API server
- `9001` - Actuator management endpoints

> 💡 **Note:** The Docker container uses a pre-seeded SQLite database with Argentina 2022 FIFA World Cup squad data. On first run, the database is copied from the image to a named volume (`java-samples-spring-boot_storage`) ensuring data persistence across container restarts.

### Stop

```bash
docker compose down
```

### Reset Database

To reset the database to its initial state:

```bash
docker compose down -v  # Remove volumes
docker compose up       # Fresh start with seed data
```

## Environment Variables

### Development (Local)

Configuration in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=9000
management.server.port=9001

# Database Configuration (SQLite)
spring.datasource.url=jdbc:sqlite:storage/players-sqlite3.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=none

# Caching
spring.cache.type=simple

# OpenAPI Documentation
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger/index.html
```

### Testing (Local)

Configuration in `src/test/resources/application.properties`:

```properties
# Test Database (SQLite in-memory)
spring.datasource.url=jdbc:sqlite::memory:
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=create-drop
```

> 💡 **Note:** Tests use SQLite in-memory database (jdbc:sqlite::memory:) for fast, isolated execution. The ISO-8601 date converter works identically with both file-based and in-memory SQLite.

## Command Summary

| Command | Description |
| ------- | ----------- |
| `./mvnw clean compile` | Clean and compile the project |
| `./mvnw test` | Run tests without coverage |
| `./mvnw verify` | Run tests with JaCoCo coverage |
| `./mvnw package` | Build JAR file |
| `./mvnw spring-boot:run` | Run application locally |
| `./mvnw package -DskipTests` | Build without running tests |
| `docker compose build` | Build Docker image |
| `docker compose up` | Start application container |
| `docker compose up -d` | Start in detached mode |
| `docker compose down` | Stop and remove containers |
| `docker compose down -v` | Stop and remove containers with volumes |
| `docker compose logs -f` | View container logs |

> 💡 **Note:** Always use the Maven wrapper (`./mvnw`) instead of system Maven to ensure consistent builds.

## Releases

This project uses **historic football clubs** as release codenames 🏆 (inspired by Ubuntu, Android, and macOS naming conventions).

### Release Naming Convention

Releases follow the pattern: `v{SEMVER}-{CLUB}` (e.g., `v1.0.0-arsenal`)

- **Semantic Version**: Standard versioning (MAJOR.MINOR.PATCH)
- **Club Name**: Alphabetically ordered codename from the [historic club list](CHANGELOG.md)

### Create a Release

To create a new release, follow this workflow:

#### 1. Create a Release Branch

Branch protection prevents direct pushes to `master`, so all release prep goes through a PR:

```bash
git checkout master && git pull
git checkout -b release/v1.0.0-arsenal
```

#### 2. Update CHANGELOG.md

Move items from `[Unreleased]` to a new release section in [CHANGELOG.md](CHANGELOG.md), then commit and push the branch:

```bash
# Move items from [Unreleased] to new release section
# Example: [1.0.0 - Arsenal] - 2026-XX-XX
git add CHANGELOG.md
git commit -m "docs(changelog): prepare release notes for v1.0.0-arsenal"
git push origin release/v1.0.0-arsenal
```

#### 3. Merge the Release PR

Open a pull request from `release/v1.0.0-arsenal` into `master` and merge it. The tag must be created **after** the merge so it points to the correct commit on `master`.

#### 4. Create and Push Tag

After the PR is merged, pull `master` and create the annotated tag:

```bash
git checkout master && git pull
git tag -a v1.0.0-arsenal -m "Release 1.0.0 - Arsenal"
git push origin v1.0.0-arsenal
```

#### 5. Automated CD Workflow

This triggers the CD workflow which automatically:

1. Validates the club name
2. Builds and tests the project with Maven
3. Publishes Docker images to GitHub Container Registry with three tags
4. Creates a GitHub Release with auto-generated changelog from commits

#### Pre-Release Checklist

- [ ] Release branch created from `master`
- [ ] `CHANGELOG.md` updated with release notes
- [ ] Changes committed and pushed on the release branch
- [ ] Release PR merged into `master`
- [ ] Tag created with correct format: `vX.Y.Z-club`
- [ ] Club name is valid (A-Z from the [historic club list](CHANGELOG.md))
- [ ] Tag pushed to trigger CD workflow

### Pull Docker Images

Each release publishes multiple tags for flexibility:

```bash
# By semantic version (recommended for production)
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:1.0.0

# By club name (memorable alternative)
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:arsenal

# Latest release
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:latest
```

> 💡 See [CHANGELOG.md](CHANGELOG.md) for the complete club list (A-Z) and release history.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details on:

- Code of Conduct
- Development workflow and best practices
- Commit message conventions (Conventional Commits)
- Pull request process and requirements

**Key guidelines:**

- Follow [Conventional Commits](https://www.conventionalcommits.org/) for commit messages
- Ensure all tests pass (`./mvnw verify`)
- Always use Maven wrapper (`./mvnw`), never system Maven
- Keep changes small and focused
- Review `.github/copilot-instructions.md` for architectural patterns

## Legal

This project is provided for educational and demonstration purposes and may be used in production at your own discretion. All trademarks, service marks, product names, company names, and logos referenced herein are the property of their respective owners and are used solely for identification or illustrative purposes.
