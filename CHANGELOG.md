# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Release names follow the **historic football clubs** naming convention (A–Z):

| Tag | Club | Country | Founded |
| --- | ---- | ------- | ------- |
| `arsenal` | Arsenal | England | 1886 |
| `barcelona` | Barcelona | Spain | 1899 |
| `chelsea` | Chelsea | England | 1905 |
| `dortmund` | Borussia Dortmund | Germany | 1909 |
| `everton` | Everton | England | 1878 |
| `flamengo` | Flamengo | Brazil | 1895 |
| `galatasaray` | Galatasaray | Turkey | 1905 |
| `hamburg` | Hamburg SV | Germany | 1887 |
| `inter` | Internazionale | Italy | 1908 |
| `juventus` | Juventus | Italy | 1897 |
| `kaiserslautern` | Kaiserslautern | Germany | 1900 |
| `liverpool` | Liverpool | England | 1892 |
| `manchesterutd` | Manchester United | England | 1878 |
| `napoli` | Napoli | Italy | 1926 |
| `olympique` | Olympique Marseille | France | 1899 |
| `psg` | Paris Saint-Germain | France | 1970 |
| `qpr` | Queens Park Rangers | England | 1882 |
| `realmadrid` | Real Madrid | Spain | 1902 |
| `sevilla` | Sevilla | Spain | 1890 |
| `tottenham` | Tottenham Hotspur | England | 1882 |
| `union` | Union Berlin | Germany | 1966 |
| `valencia` | Valencia | Spain | 1919 |
| `werder` | Werder Bremen | Germany | 1899 |
| `xerez` | Xerez CD | Spain | 1947 |
| `youngboys` | Young Boys | Switzerland | 1898 |
| `zenit` | Zenit | Russia | 1925 |

---

## [Unreleased]

### Added

- Integrate Flyway for database schema versioning and automated migrations;
  add `spring-boot-starter-flyway` (Spring Boot 4.0 requires this dedicated
  starter for autoconfiguration — `flyway-core` alone is insufficient) and
  `flyway-database-postgresql` to `pom.xml`; create
  migration directory `src/main/resources/db/migration/` with three versioned
  scripts: `V1__Create_players_table.sql` (schema), `V2__Seed_starting11.sql`
  (11 Starting XI players), `V3__Seed_substitutes.sql` (15 substitute players);
  configure `spring.flyway.enabled=true` with `baseline-on-migrate=true` for
  backwards compatibility with existing databases; disable Flyway in test
  environment which continues to use SQLite in-memory with `ddl.sql`/`dml.sql`;
  switch `spring.jpa.hibernate.ddl-auto` from `none` to `validate` so Hibernate
  verifies entity mappings against the Flyway-managed schema (#130)

### Changed

- Switch runtime base image from `eclipse-temurin:25-jdk-alpine` to
  `eclipse-temurin:25-jre-alpine` to reduce image size by dropping compiler
  toolchain (`javac`, `jshell`, debug utilities) while retaining the full JVM
  (#307)

### Fixed

### Removed

---

## [2.0.0 - Barcelona] - 2026-04-06

### Changed

- Normalize data-state vocabulary in BDD-style test names: rename 12 `given*`
  methods across `PlayersServiceTests`, `PlayersControllerTests`, and
  `PlayersRepositoryTests` to use canonical terms (`existing`, `nonexistent`,
  `unknown`); add class-level Javadoc to `PlayerFakes` documenting the
  three-term convention (#303)
- Normalize player dataset: seed Leandro Paredes (squad 5) permanently; correct
  Enzo Fernández (squad 24) to SL Benfica / Liga Portugal, Alexis Mac Allister
  (squad 20) to Brighton & Hove Albion, and Lionel Messi (squad 10) to Paris
  Saint-Germain / Ligue 1 — all reflecting November 2022 World Cup squads (#288)
- Align CRUD test fixtures: Giovani Lo Celso (squad 27) for Create and Delete,
  Lionel Messi (squad 10) for Retrieve, Damián Martínez (squad 23) for Update;
  canonical UUID v5 strings applied across `dml.sql`, `PlayerFakes`,
  `PlayerDTOFakes`, and `storage/players-sqlite3.db` (#288)
- `Player` entity: `id` (UUID) is the database primary key — `@Id` with
  `GenerationType.UUID`; `squadNumber` (Integer) carries `@Column(unique=true)`
  and serves as the natural-key route identifier for `PUT` and `DELETE` (#268)
- `PlayerDTO`: `id` type changed from `Long` to `UUID` (#268)
- `PlayersRepository`: keyed on `UUID`; `findBySquadNumber(Integer)` derived
  query resolves the UUID PK before update/delete operations (#268)
- `PlayersService`: `retrieveById` accepts `UUID`; `update` and
  `deleteBySquadNumber` use `findBySquadNumber` to look up the UUID PK (#268)
- `PUT /players/{squadNumber}` and `DELETE /players/{squadNumber}`: path
  variable changed from `Long id` to `Integer squadNumber` (#268)
- `GET /players/{id}`: path variable changed from `Long` to `UUID` (#268)

  **BREAKING CHANGE**: `PUT /players/{id}` and `DELETE /players/{id}` routes
  replaced by `PUT /players/{squadNumber}` and `DELETE /players/{squadNumber}`;
  `GET /players/{id}` path variable type changed from `Long` to `UUID` (#268)
- `storage/players-sqlite3.db`: schema migrated to `id VARCHAR(36) PRIMARY KEY`,
  `squadNumber INTEGER NOT NULL UNIQUE`; 25 players preserved (#268)
- `ddl.sql` and `dml.sql`: test schema and seed data updated for new structure (#268)

### Added

- Repository delete test: `givenPlayerExists_whenDelete_thenPlayerIsRemoved`
  covers Lo Celso inline save + delete pattern (#288)
- JaCoCo `check` goal added to Maven build enforcing 80% instruction and branch
  coverage — replaces manual HTML report step in `/pre-commit` (#268)
- `.sonarcloud.properties`: SonarCloud Automatic Analysis configuration —
  sources, tests, coverage exclusions aligned with `codecov.yml` (#293)
- `.dockerignore`: added `.claude/`, `CLAUDE.md`, `.coderabbit.yaml`,
  `.sonarcloud.properties`, `CHANGELOG.md`, `README.md` (#293)

---

## [1.0.0 - Arsenal] - 2026-03-29

Initial release. See [README.md](README.md) for complete feature list and documentation.
