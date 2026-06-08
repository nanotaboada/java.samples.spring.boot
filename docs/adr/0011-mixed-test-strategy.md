# ADR-0011: Mixed Test Strategy

Date: 2026-06-07

## Status

Accepted

## Context

Testing strategies range from fully mocked unit tests (fast, isolated, risk of mock/production divergence) to fully integrated tests against a real database (high confidence, slower). Two distinct scenario categories exist: happy paths and expected error branches (400 Bad Request, 404 Not Found, 409 Conflict) that can be triggered with a real database and real constraints; and infrastructure-level error branches (500 Internal Server Error from service or repository failures) that require controlled failure injection because a healthy database will not produce them. Options considered: mocks only (fast but false confidence — demonstrated as a risk in the TypeScript sibling where mocked Sequelize tests passed while real queries failed), integration tests only (TestContainers + PostgreSQL adds setup cost and CI image pull time), and a mixed strategy with an explicit boundary.

## Decision

We will use MockMvc + Mockito for controller and service unit tests, including 500-level error branches that cannot be reached with a healthy database. We will use in-memory SQLite (`:memory:`) for integration tests that exercise the full request/response cycle for all reachable paths. The boundary is explicit: if a scenario can be triggered with a real database and real data, it must use the real database.

## Consequences

- In-memory SQLite gives near-zero test infrastructure cost: no Docker, no external process, no cleanup. Schema is applied via `ddl.sql` and seed data via `dml.sql` before each test class.
- Mockito covers error branches (e.g. repository throwing an exception) that cannot be triggered otherwise without test-environment side effects.
- The test naming convention (`givenX_whenY_thenZ`) and AssertJ BDD style (`then(result).isNotNull()`) make the intent of each test clear.
- The mixed strategy avoids the false confidence documented in the TypeScript sibling project, where mocked ORM tests passed while the real query implementation was broken.
- In-memory SQLite auto-clears between test classes; no `@Transactional` rollback or manual truncation is required.
- The boundary between mock and real-database tests must be maintained by convention. A test that mocks the database for a scenario reachable with real data is a latent gap in coverage.
