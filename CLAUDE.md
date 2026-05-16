# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

@.github/copilot-instructions.md

## Claude Code

- Run `/pre-commit` to execute the full pre-commit checklist for this project.
- Base package: `ar.com.nanotaboada.java.samples.spring.boot` — all source lives under this path.

### Running a single test

```bash
./mvnw test -Dtest=PlayersControllerTests          # one test class
./mvnw test -Dtest=PlayersServiceTests#givenX_whenY_thenZ  # one test method
```
