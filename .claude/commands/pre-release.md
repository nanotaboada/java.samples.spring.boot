Run the pre-release checklist for this project. Work through all three phases
in order, pausing for explicit confirmation at each decision point before
proceeding. Never create a branch, commit, tag, or push without approval.

---

## Phase 1 — Determine next release

1. Run `git status` and confirm the working tree is clean and on `master`.
   If not, stop and report the problem.

2. Run `git tag --sort=-v:refname` to list existing tags. Identify the most
   recent tag matching `v*.*.*-*` and extract its club codename.

3. Read the A–Z club table from `CHANGELOG.md` to find the next club:
   - **No tags yet**: start at `A` (first club in the table).
   - **Normal case**: use the club that follows the last used codename
     alphabetically. If letters were skipped, pick the next after the
     highest existing codename — do not backfill gaps.
   - **Last codename is `Z`** (Zenit): the list is finite. Stop and flag
     that the naming convention needs to be revisited before proceeding.

4. Read the `[Unreleased]` section of `CHANGELOG.md` and infer the version
   bump using these rules (applied in order — first match wins):
   - Any entry contains the word **BREAKING** (case-insensitive), a
     `BREAKING CHANGE:` token in a commit footer, or a `!` suffix after
     the commit type/scope (e.g. `feat!:` or `feat(scope)!:`) → **major** bump
   - Any `### Added` subsection has entries → **minor** bump
   - Otherwise (only `### Changed`, `### Fixed`, `### Removed`) → **patch** bump

5. Compute the next version by applying the bump to the current latest tag's
   semver (e.g. `v1.0.0-arsenal` + minor → `1.1.0`).

6. Present a summary for confirmation before continuing:
   - Last tag and club
   - Next version and club codename
   - Bump type and the reasoning (what triggered it)
   - Proposed tag: `vX.Y.Z-{club}`
   - Proposed branch: `release/vX.Y.Z-{club}`

   **Wait for explicit approval before proceeding to Phase 2.**

---

## Phase 2 — Prepare release branch

1. Create branch `release/vX.Y.Z-{club}` from `master`.

2. Edit `CHANGELOG.md`:
   - Replace `## [Unreleased]` with `## [X.Y.Z - ClubName] - YYYY-MM-DD`
     (use today's date; use the club's display name from the table, e.g.
     "Barcelona", "Chelsea").
   - Consolidate duplicate subsection headings (e.g. two `### Added` blocks
     should be merged into one).
   - Add a new empty `## [Unreleased]` section at the top (above the new
     versioned heading) with the standard subsections.
   - Update the compare links at the bottom of the file:
     - `[unreleased]` → `.../compare/vX.Y.Z-{club}...HEAD`
     - Add `[X.Y.Z - ClubName]` → `.../compare/v{prev-tag}...vX.Y.Z-{club}`

3. Show the full diff of `CHANGELOG.md`.

4. If `coderabbit` CLI is installed, run `coderabbit review --type uncommitted --prompt-only`
   on the uncommitted CHANGELOG changes:
   - If actionable/serious findings are reported, stop and address them before proceeding.
   - If only nitpick-level findings, report them and continue.
   - If `coderabbit` is not installed, skip with a note.

5. Propose this commit message:

   ```text
   docs(changelog): prepare release notes for vX.Y.Z-{club} (#issue)
   ```

   **Wait for explicit approval before committing.**

6. Run `./mvnw clean install` — must succeed with no compilation warnings, all
   tests passing, and the JaCoCo check reporting `All coverage checks have been met.`

7. If Docker is running, run `docker compose build` — must succeed with no errors.
   Skip with a note if Docker Desktop is not running.

8. Stage `CHANGELOG.md` and commit using the approved message from step 5.

9. Propose opening a PR from `release/vX.Y.Z-{club}` into `master`.
   **Wait for explicit approval before opening.**

10. Open the PR with:
    - Title: `docs(changelog): prepare release notes for vX.Y.Z-{club}`
    - Body summarising what is included in this release.

---

## Phase 3 — Tag and release

1. Wait — do not proceed until the user confirms:
   - CI is green
   - The PR has been merged into `master`

2. Once confirmed, run:
   ```bash
   git checkout master && git pull origin master
   ```
   and show the resulting `git log --oneline -3`.

3. Propose the annotated tag:
   ```bash
   git tag -a vX.Y.Z-{club} -m "Release X.Y.Z - ClubName"
   ```

   **Wait for explicit approval before creating the tag.**

4. Create the tag, then propose:
   ```bash
   git push origin vX.Y.Z-{club}
   ```

   **Wait for explicit approval before pushing.** Remind the user that pushing
   the tag triggers the CD workflow which will build, publish the Docker image,
   and create the GitHub Release.
