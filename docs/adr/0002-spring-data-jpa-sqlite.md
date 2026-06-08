# ADR-0002: Use Spring Data JPA with SQLite

Date: 2026-06-07

## Status

Accepted

## Context

The project requires a persistence layer with ORM support and the ability to run without an external database service. Options considered: plain JDBC (low-level, no ORM), MyBatis (SQL-centric, half-ORM), jOOQ (type-safe SQL DSL, commercial for some databases), Hibernate standalone (ORM without Spring Data), Spring Data JPA + Hibernate (full abstraction), and PostgreSQL as the primary database. SQLite is file-based and requires no server process, making it ideal for a self-contained PoC. An in-memory SQLite variant allows fast, isolated test runs without any cleanup logic.

## Decision

We will use Spring Data JPA backed by Hibernate with SQLite as the database — file-based at runtime and in-memory for tests.

## Consequences

- Spring Data JPA derived queries (`findBySquadNumber`, `findByLeague`) replace hand-written SQL for standard CRUD operations, demonstrating the pattern without boilerplate.
- SQLite requires no external service or Docker dependency, making local setup a single `./mvnw spring-boot:run`.
- In-memory SQLite (`:memory:`) auto-clears between test runs, providing isolation without `@Transactional` rollback tricks or manual truncation.
- The community Hibernate SQLite dialect bridges the gap between Hibernate's standard DDL generation and SQLite's type system — this is a third-party dependency not maintained by Hibernate.
- SQLite's concurrency model (single writer) is not representative of production JPA targets such as PostgreSQL; migration to a server-based database will require dialect and configuration changes.
- JPA abstractions hide SQL, which can produce unexpected query plans (N+1, missing indexes). In a simple, flat domain this is not a practical concern, but learners should be aware of the trade-off.
