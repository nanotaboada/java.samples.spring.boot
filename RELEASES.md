# Releases

Releases follow the pattern `v{SEMVER}-{CLUB}` (e.g., `v1.0.0-arsenal`). Codenames are drawn alphabetically from the [historic club list](CHANGELOG.md) of famous football clubs.

## Workflow

### 1. Create a Release Branch

Branch protection prevents direct pushes to `master`, so all release prep goes through a PR:

```bash
git checkout master && git pull
git checkout -b release/v1.0.0-arsenal
```

### 2. Update CHANGELOG.md

Move items from `[Unreleased]` to a new release section in [CHANGELOG.md](CHANGELOG.md), then commit and push the branch:

```bash
# Move items from [Unreleased] to new release section
# Example: [1.0.0 - Arsenal] - 2026-XX-XX
git add CHANGELOG.md
git commit -m "docs(changelog): prepare release notes for v1.0.0-arsenal"
git push origin release/v1.0.0-arsenal
```

### 3. Merge the Release PR

Open a pull request from `release/v1.0.0-arsenal` into `master` and merge it. The tag must be created **after** the merge so it points to the correct commit on `master`.

### Pre-release Checklist

Before creating the tag, verify all of the following:

- [ ] `CHANGELOG.md` `[Unreleased]` section is moved to a new versioned release entry
- [ ] Release PR is merged into `master`
- [ ] `./mvnw clean install` passes
- [ ] Club name is valid and follows alphabetical order (see [historic club list](CHANGELOG.md))
- [ ] All CI checks on `master` are green

### 4. Create and Push Tag

After the PR is merged, pull `master` and create the annotated tag:

```bash
git checkout master && git pull
git tag -a v1.0.0-arsenal -m "Release 1.0.0 - Arsenal"
git push origin v1.0.0-arsenal
```

### 5. Automated CD Workflow

Pushing the tag triggers the CD workflow which automatically:

1. Validates tag format (semver and club name)
2. Builds and tests the project with Maven
3. Publishes Docker images to GitHub Container Registry with three tags
4. Creates a GitHub Release with auto-generated changelog from commits

## Docker Pull

Each release publishes multiple tags for flexibility:

```bash
# By semantic version (recommended for production)
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:1.0.0

# By club name (memorable alternative)
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:arsenal

# Latest release
docker pull ghcr.io/nanotaboada/java-samples-spring-boot:latest
```
