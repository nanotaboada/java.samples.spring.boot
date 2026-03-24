Run the pre-commit checklist for this project:

1. Remind me to update `CHANGELOG.md` `[Unreleased]` section (Added / Changed / Fixed / Removed) — I must do this manually.
2. Run `./mvnw clean install` — must succeed with no compilation warnings and all tests passing.
3. Remind me to open `target/site/jacoco/index.html` to verify coverage after the build completes.

Run step 2, report the results clearly, then propose a branch name and commit message for my approval using the format `type(scope): description (#issue)` (max 80 chars; types: `feat` `fix` `chore` `docs` `test` `refactor` `ci` `perf`). Do not create the branch or commit until I explicitly confirm.
