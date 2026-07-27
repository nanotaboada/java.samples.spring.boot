---
name: key-workflows
description: Step-by-step guides for adding an endpoint or modifying the schema in this Spring Boot project, plus the branch/commit proposal to make after finishing work.
---

**Add an endpoint**: Define DTO in `models/` with Bean Validation → add service method in `services/` with `@Transactional` → create controller endpoint with `@Operation` annotation → add tests → run `./mvnw clean test jacoco:report`.

**Modify schema**: Create a new Flyway migration `src/main/resources/db/migration/V{N}__description.sql` (production path) → update `@Entity` in `models/Player.java` → update DTOs if API changes → also update `src/test/resources/ddl.sql` and `dml.sql` (tests use Spring SQL init, not Flyway) → update service, repository, and tests → run `./mvnw clean test`. Do not manually edit the SQLite file in `storage/`; Flyway owns it.

**After completing work**: Suggest a branch name (e.g. `feat/113-add-player-stats`) and a commit message following Conventional Commits including co-author line:

```text
feat(scope): description (#issue)

Co-authored-by: Claude Sonnet 5 <noreply@anthropic.com>
```
