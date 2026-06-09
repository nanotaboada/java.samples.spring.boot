# ADR-0006: Use SpringDoc OpenAPI 3 for API Documentation

Date: 2026-06-07

## Status

Accepted

## Context

The API needs interactive, standards-compliant documentation that stays in sync with the implementation. Options considered: springfox (historically popular, unmaintained since 2020, incompatible with Spring Boot 3+), hand-written OpenAPI YAML (accurate but manual and drift-prone), no documentation (insufficient for a learning-focused PoC), and SpringDoc OpenAPI 3 (actively maintained, auto-generates from Spring MVC annotations, Spring Boot 3+/4+ compatible).

## Decision

We will use SpringDoc OpenAPI 3 to generate the OpenAPI specification and serve the Swagger UI at `/swagger/index.html`. The JSON spec is available at `/docs`.

## Consequences

- The OpenAPI spec is derived from `@Operation`, `@ApiResponse`, and controller annotations — documentation is co-located with the code it describes and stays accurate as endpoints change.
- Swagger UI provides an interactive testing interface without any external tooling, useful for learners exploring the API.
- SpringDoc is actively maintained and explicitly supports Spring Boot 3+ and 4+, unlike springfox.
- Auto-generation from annotations produces verbose output; `@Operation` and schema annotations add noise to controller code. This is an accepted trade-off for a project where documentation is a first-class concern.
- The Swagger UI path (`/swagger/index.html`) and spec path (`/docs`) are configured in `application.properties` and can be changed without code modifications.
