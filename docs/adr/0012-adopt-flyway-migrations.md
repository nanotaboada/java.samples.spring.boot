# ADR-0012: Adopt Flyway for Database Migrations

Date: 2026-06-07

## Status

Accepted

## Context

The project initially used manual `ddl.sql` and `dml.sql` scripts executed at startup for schema creation and seed data. This was sufficient for a PoC with a stable schema and a single committed SQLite file, but it provides no migration history, no versioning, and no safe path to evolve the schema over time. When PostgreSQL support was introduced (issue #286), versioned schema migrations became necessary. Options considered: Flyway (versioned, reversible migrations, first-class Spring Boot auto-configuration, Apache 2.0 license), Liquibase (XML/YAML/SQL changelogs, broader database support, more complex configuration), and retaining manual scripts (no migration history, not viable with multiple databases). Implemented in v2.0.1-chelsea (issue #130).

## Decision

We will adopt Flyway for versioned schema migrations, superseding the manual `ddl.sql`/`dml.sql` approach. Migration scripts are stored under `src/main/resources/db/migration/` following Flyway's `V{version}__{description}.sql` naming convention. Spring Boot's Flyway auto-configuration applies migrations at startup.

## Consequences

- Flyway provides a full migration history: every schema change is a versioned, auditable SQL file. Rollback requires a new migration rather than an undo, which is the correct model for production systems.
- Spring Boot's auto-configuration wires Flyway automatically when the dependency is present; no explicit bean definition is required.
- The migration from manual scripts to Flyway was a one-time conversion: the original `ddl.sql` and `dml.sql` content became V1 and V2 (or V3) Flyway scripts, preserving the 26-player seed dataset.
- Flyway enforces a checksum on applied migrations; modifying an already-applied migration file causes a startup failure. This is a safety mechanism, not a limitation.
- The in-memory SQLite test database also runs Flyway migrations, ensuring the test schema stays in sync with the production schema automatically.
- Flyway's Apache 2.0 license is compatible with the project's MIT license. The Flyway Teams/Enterprise editions are not required for this use case.
