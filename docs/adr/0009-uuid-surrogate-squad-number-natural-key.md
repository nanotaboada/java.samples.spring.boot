# ADR-0009: Demote UUID to Surrogate Key and Promote Squad Number to Natural Key

Date: 2026-06-07

## Status

Accepted

## Context

The `Player` entity originally used a sequential `Long` as its primary key — predictable, not globally unique, and semantically meaningless in the football domain. Squad numbers are the stable, domain-meaningful identifier for players within a team. UUID provides global uniqueness without the predictability of sequential counters, but it is opaque to API consumers and not a natural mutation key. The original design exposed `id` (Long) in all mutation endpoints, which is an anti-pattern: internal surrogate keys should not leak into the public API contract. Implemented in v2.0.0-barcelona (issue #268).

## Decision

We will demote UUID to a non-primary surrogate key — unique, non-updatable, generated at the application level via `@PrePersist`. `squadNumber` becomes the `@Id` (primary key) and is used as the path variable for `PUT /players/{squadNumber}` and `DELETE /players/{squadNumber}`. A `GET /players/{id}` endpoint (UUID) is retained for internal/admin lookup.

## Consequences

- Squad numbers are natural keys: unique per team, stable over a career, and meaningful to API consumers. Using them as path variables makes URLs readable (`/players/10`) rather than opaque (`/players/550e8400-e29b-41d4-a716-446655440000`).
- UUID as a surrogate key provides global uniqueness for distributed or federated scenarios without exposing sequential counters that hint at dataset size.
- The `@PrePersist`-generated UUID is set once on creation and never updated, ensuring immutability of the surrogate.
- Retaining `GET /players/{id}` (UUID) preserves internal traceability and supports integration with external systems that may store the UUID.
- Squad numbers are stable within a single tournament squad but may change across seasons. For this PoC (fixed 2022 World Cup squad), stability is guaranteed. A more general player registry would need to account for squad number reuse.
- The `squadNumber` field carries a `@Column(unique = true)` constraint enforced at the database level, providing a 409 Conflict response on duplicate POST.
