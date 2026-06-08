# ADR-0004: Adopt Layered Architecture

Date: 2026-06-07

## Status

Accepted

## Context

The project needs a structural pattern that separates HTTP concerns, business logic, and data access. Options considered: hexagonal (ports and adapters) architecture (technology-agnostic domain, higher complexity), CQRS (separate read/write models, overkill for simple CRUD), vertical slice architecture (feature-per-folder, unfamiliar to most Spring Boot learners), and the classic 3-layer model (Controller → Service → Repository) which maps directly to Spring Boot's stereotype annotations.

## Decision

We will adopt a strict 3-layer architecture: controllers handle HTTP and delegate to services; services own business logic and transaction boundaries; repositories handle data access. No layer may skip the one immediately below it — controllers must not access repositories directly.

## Consequences

- The structure maps 1:1 to Spring Boot's `@RestController`, `@Service`, and `@Repository` stereotypes, making it immediately recognisable to any Spring Boot practitioner.
- Each layer is independently testable: controllers via MockMvc + Mockito-mocked services, services via unit tests with mocked repositories, repositories via in-memory SQLite integration tests.
- The layer rule is enforced by convention and code review, not by the compiler or a framework boundary — a motivated developer can still inject a repository into a controller.
- For the flat, CRUD-only domain in this project (26 players, no aggregate complexity), a hexagonal or onion architecture would add indirection without benefit.
- As domain complexity grows, the service layer risks becoming a thin pass-through or a bloated transaction script. At that point, domain-driven patterns would be worth revisiting.
