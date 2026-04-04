Before running the checklist, run `git fetch origin`. If the current branch is behind `origin/master`, stop and rebase before proceeding.

Run the pre-commit checklist for this project:

1. Update `CHANGELOG.md` `[Unreleased]` section — add an entry under the appropriate subsection (Added / Changed / Fixed / Removed) describing the changes made, referencing the issue number.
2. Run `./mvnw clean install` — must succeed with no compilation warnings and all tests passing.
3. Remind me to open `target/site/jacoco/index.html` to verify coverage after the build completes.
4. If `coderabbit` CLI is installed, run `coderabbit review --type uncommitted --prompt-only`:
   - If actionable/serious findings are reported, stop and address them before proposing the commit.
   - If only nitpick-level findings, report them and continue to the commit proposal.
   - If `coderabbit` is not installed, skip this step with a note.

Run steps 1–3, report the results clearly, then run step 4 (CodeRabbit review) if available, then propose a branch name and commit message for my approval using the format `type(scope): description (#issue)` (max 80 chars; types: `feat` `fix` `chore` `docs` `test` `refactor` `ci` `perf`). Do not create the branch or commit until I explicitly confirm.
