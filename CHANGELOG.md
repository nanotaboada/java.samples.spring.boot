# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Release names follow the **historic football clubs** naming convention (A–Z):

| Tag | Club | Country | Founded |
| --- | ---- | ------- | ------- |
| `arsenal` | Arsenal | England | 1886 |
| `barcelona` | Barcelona | Spain | 1899 |
| `chelsea` | Chelsea | England | 1905 |
| `dortmund` | Borussia Dortmund | Germany | 1909 |
| `everton` | Everton | England | 1878 |
| `flamengo` | Flamengo | Brazil | 1895 |
| `galatasaray` | Galatasaray | Turkey | 1905 |
| `hamburg` | Hamburg SV | Germany | 1887 |
| `inter` | Internazionale | Italy | 1908 |
| `juventus` | Juventus | Italy | 1897 |
| `kaiserslautern` | Kaiserslautern | Germany | 1900 |
| `liverpool` | Liverpool | England | 1892 |
| `manchesterutd` | Manchester United | England | 1878 |
| `napoli` | Napoli | Italy | 1926 |
| `olympique` | Olympique Marseille | France | 1899 |
| `psg` | Paris Saint-Germain | France | 1970 |
| `qpr` | Queens Park Rangers | England | 1882 |
| `realmadrid` | Real Madrid | Spain | 1902 |
| `sevilla` | Sevilla | Spain | 1890 |
| `tottenham` | Tottenham Hotspur | England | 1882 |
| `union` | Union Berlin | Germany | 1966 |
| `valencia` | Valencia | Spain | 1919 |
| `werder` | Werder Bremen | Germany | 1899 |
| `xerez` | Xerez CD | Spain | 1947 |
| `youngboys` | Young Boys | Switzerland | 1898 |
| `zenit` | Zenit | Russia | 1925 |

---

## [Unreleased]

### Changed

- `Player` entity: `id` demoted from `@Id Long` to surrogate `UUID` (non-PK,
  `updatable=false`, `unique`, generated via `@PrePersist`); `squadNumber`
  promoted to `@Id Integer` (natural key) (#268)
- `PlayerDTO`: `id` type changed from `Long` to `UUID` (#268)
- `PlayersRepository`: keyed on `Integer` (squad number as PK); added
  `findById(UUID)` overload for admin/internal lookup (#268)
- `PlayersService`: `retrieveById` now accepts `UUID`; `update` keyed on
  `Integer squadNumber`; `delete` renamed to `deleteBySquadNumber(Integer)` (#268)
- `PUT /players/{squadNumber}` and `DELETE /players/{squadNumber}`: path
  variable changed from `Long id` to `Integer squadNumber` (#268)
- `GET /players/{id}`: path variable changed from `Long` to `UUID` (admin use) (#268)
- `storage/players-sqlite3.db`: schema migrated to `id VARCHAR(36) NOT NULL UNIQUE`,
  `squadNumber INTEGER PRIMARY KEY`; 25 players preserved with generated UUIDs (#268)
- `ddl.sql` and `dml.sql`: test schema and seed data updated for new structure (#268)

### Added

- `.sonarcloud.properties`: SonarCloud Automatic Analysis configuration —
  sources, tests, coverage exclusions aligned with `codecov.yml` (#293)
- `.dockerignore`: added `.claude/`, `CLAUDE.md`, `.coderabbit.yaml`,
  `.sonarcloud.properties`, `CHANGELOG.md`, `README.md` (#293)

---

## [1.0.0 - Arsenal] - 2026-03-29

Initial release. See [README.md](README.md) for complete feature list and documentation.
