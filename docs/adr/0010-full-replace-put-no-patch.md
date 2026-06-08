# ADR-0010: Full-Replace PUT, No PATCH

Date: 2026-06-07

## Status

Accepted

## Context

HTTP defines PUT (full replacement of a resource) and PATCH (partial update). The choice affects DTO design (all fields required vs. optional), service layer logic (overwrite all vs. merge), error handling (what does an absent field mean?), and client contract. Options considered: PATCH only (requires a patch format — JSON Merge Patch or JSON Patch — and merge logic), both PUT and PATCH (doubles the endpoint surface and the test matrix), PUT with optional fields (ambiguous: does an absent field mean "keep current value" or "set to null?"), and PUT as a strict full replacement with all fields required.

## Decision

We will implement PUT as a full replacement: the request body represents the complete new state of the resource. No PATCH endpoint is provided.

## Consequences

- No merge logic is required in the service layer: the existing entity is replaced wholesale with the incoming DTO's values.
- All DTO fields carry Bean Validation constraints, making partial updates a client-side concern (load current state, modify, submit full body).
- The contract is unambiguous: absent fields are not allowed, not silently ignored.
- Consistent with the same decision made in the Python/FastAPI and Go/Gin sibling repositories in the cross-language comparison series.
- Clients must fetch the full current state before making a partial modification, which results in an extra GET call for field-level updates. For a PoC with a small, flat domain, this is not a meaningful burden.
- If partial updates are needed in the future, a PATCH endpoint can be added as a non-breaking addition without changing the existing PUT semantics.
