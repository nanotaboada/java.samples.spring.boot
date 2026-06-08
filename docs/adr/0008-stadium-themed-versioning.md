# ADR-0008: Use Stadium-Themed Versioning

Date: 2026-06-07

## Status

Accepted

## Context

The project uses Semantic Versioning (MAJOR.MINOR.PATCH) for release numbers. Release names — appended as a suffix to the version tag — need a convention that is memorable, consistent across the six sibling repositories in the cross-language comparison series, and thematically appropriate for a football-domain project. Options considered: standard semver only (no names), animal names (arbitrary, no thematic link), city names (too generic), and names drawn from the football domain (players, coaches, clubs, stadiums).

## Decision

We will use historically significant football clubs in alphabetical order as release codenames, appended to the semver tag (e.g. `v2.0.0-barcelona`, `v2.0.1-chelsea`). The same A–Z sequence is applied consistently across all six sibling repositories in the comparison series.

## Consequences

- The alphabetical progression makes release ordering unambiguous at a glance, even without reading dates.
- The football-club theme is culturally universal and directly related to the project's domain (Argentina 2022 FIFA World Cup squad).
- The convention is shared across all six language repositories, making cross-project comparisons easy to orient in time.
- The codename is appended as a hyphenated suffix to the version tag. Git tags, CHANGELOG entries, Docker image tags, and release notes all include the codename for consistency.
- The convention is purely cosmetic — semver carries the semantic meaning. Contributors should not infer anything about the release scope from the club name.
- The A–Z sequence has 26 slots before a wrap-around or convention change is needed.
