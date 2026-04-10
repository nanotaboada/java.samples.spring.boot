-- V1: Create players table
-- Compatible with both SQLite (local dev) and PostgreSQL (see #286).
-- TEXT columns use TEXT affinity in SQLite and the unlimited TEXT type in PostgreSQL.
-- INTEGER is used for squadNumber (natural key) and starting11 (boolean flag: 1/0).
-- UUID primary key is stored as VARCHAR(36) and generated at the application level.

CREATE TABLE IF NOT EXISTS players (
    id           VARCHAR(36)  NOT NULL,
    squadNumber  INTEGER      NOT NULL,
    firstName    TEXT         NOT NULL,
    middleName   TEXT,
    lastName     TEXT         NOT NULL,
    dateOfBirth  TEXT         NOT NULL,
    position     TEXT         NOT NULL,
    abbrPosition TEXT         NOT NULL,
    team         TEXT         NOT NULL,
    league       TEXT         NOT NULL,
    starting11   BOOLEAN      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (squadNumber)
);
