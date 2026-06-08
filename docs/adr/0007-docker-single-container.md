# ADR-0007: Single-Container Docker Deployment

Date: 2026-06-07

## Status

Accepted

## Context

The project requires a containerisation strategy. Options considered: Docker Compose multi-service (separate containers for the API and a database server — appropriate for PostgreSQL but unnecessary for SQLite), Kubernetes (orchestration overhead far exceeds PoC requirements), multi-stage build without Compose (single image, no volume mount), and a single container with a bind-mounted volume for the SQLite database file.

## Decision

We will deploy a single Docker container built with a multi-stage Dockerfile. The SQLite database file is persisted via a bind-mounted volume so that data survives container restarts. `docker compose up` is the standard invocation.

## Consequences

- A single container is the simplest possible deployment unit; contributors only need Docker installed, not a database server.
- The multi-stage build (builder + runtime image) keeps the final image small by excluding Maven and the JDK build toolchain.
- The bind-mounted volume (`./storage`) persists the SQLite file on the host, making data recoverable without entering the container.
- The `docker compose down -v` command removes the volume and resets data — callers must be intentional about this.
- When PostgreSQL support is added in the future, the single-container model will need to evolve to a multi-service Compose file. This ADR will be superseded at that point.
- This strategy is not representative of production Java deployments, which typically externalise the database. That is an accepted trade-off for a PoC.
