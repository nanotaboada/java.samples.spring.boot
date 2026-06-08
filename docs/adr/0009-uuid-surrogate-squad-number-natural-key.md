# ADR-0009: Use UUID as Primary Key and Squad Number as Natural Key

Date: 2026-06-07

## Status

Accepted

## Context

The `Player` entity originally used a sequential `Long` as its primary key — predictable, not globally unique, and semantically meaningless in the football domain. Squad numbers are the stable, domain-meaningful identifier for players within a team. UUID provides global uniqueness without the predictability of sequential counters, but it is opaque to API consumers and not a natural mutation key. The original design exposed `id` (Long) in all mutation endpoints, which is an anti-pattern: internal surrogate keys should not leak into the public API contract. Implemented in v2.0.0-barcelona (issue #268).

## Decision

We will use UUID as the database primary key (`@Id`) — unique, non-updatable, generated at the application level via `@GeneratedValue(strategy = GenerationType.UUID)`. `squadNumber` becomes the natural key exposed in REST endpoints and is used as the path variable for `PUT /players/{squadNumber}` and `DELETE /players/{squadNumber}`. `squadNumber` carries a unique constraint (`@Column(unique = true)`) at the database level. A `GET /players/{id}` endpoint (UUID) is retained for internal/admin lookup.

## Consequences

- `squadNumber` is a domain-meaningful natural key: unique per team, stable over a career, and meaningful to API consumers. As the routing key in REST URLs (`PUT /players/{squadNumber}`, `DELETE /players/{squadNumber}`), it is more readable than an opaque UUID path.
- UUID is the entity primary key (`@Id`) and functions as a generated surrogate identifier — globally unique, opaque to consumers, and free of sequential leakage. It provides global uniqueness for distributed or federated scenarios without exposing sequential counters that hint at dataset size.
- The `@GeneratedValue`-assigned UUID (surrogate) is set once on creation and never updated, ensuring immutability of the primary key.
- `GET /players/{id}` exposes the UUID primary key for internal traceability and integration with external systems that store the UUID reference.
- `squadNumber` as routing key is stable within a fixed tournament squad but may change across seasons. For this PoC (fixed 2022 World Cup squad), stability is guaranteed. A more general player registry would need to account for squad number reuse.
- The `squadNumber` natural key carries a `@Column(unique = true)` constraint enforced at the database level, providing a 409 Conflict response on duplicate POST.
