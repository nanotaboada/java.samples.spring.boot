-- Test Database Schema (DDL - Data Definition Language)
-- SQLite in-memory database for testing
-- Matches production schema exactly (compatibility guaranteed)

DROP TABLE IF EXISTS players;

CREATE TABLE players (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstName TEXT NOT NULL,
    middleName TEXT,
    lastName TEXT NOT NULL,
    dateOfBirth TEXT NOT NULL,
    squadNumber INTEGER NOT NULL UNIQUE,
    position TEXT NOT NULL,
    abbrPosition TEXT NOT NULL,
    team TEXT NOT NULL,
    league TEXT NOT NULL,
    starting11 INTEGER NOT NULL
);
