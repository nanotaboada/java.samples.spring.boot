-- Test Database Data (DML - Data Manipulation Language)
-- Contains all 26 players from production database EXCEPT Leandro Paredes (squadNumber 5)
-- Leandro Paredes will be created during tests (for POST/create operations)
-- Damián Emiliano Martínez (squadNumber 23) will be updated during tests
-- Alejandro Gómez (squadNumber 17) will be deleted during tests

-- Starting 11 (id is PRIMARY KEY, squadNumber is UNIQUE)
INSERT INTO players (id, squadNumber, firstName, middleName, lastName, dateOfBirth, position, abbrPosition, team, league, starting11) VALUES
('00000000-0000-0000-0000-000000000001', 23, 'Damián', 'Emiliano', 'Martínez', '1992-09-02T00:00:00.000Z', 'Goalkeeper', 'GK', 'Aston Villa FC', 'Premier League', 1),
('00000000-0000-0000-0000-000000000002', 26, 'Nahuel', NULL, 'Molina', '1998-04-06T00:00:00.000Z', 'Right-Back', 'RB', 'Atlético Madrid', 'La Liga', 1),
('00000000-0000-0000-0000-000000000003', 13, 'Cristian', 'Gabriel', 'Romero', '1998-04-27T00:00:00.000Z', 'Centre-Back', 'CB', 'Tottenham Hotspur', 'Premier League', 1),
('00000000-0000-0000-0000-000000000004', 19, 'Nicolás', 'Hernán Gonzalo', 'Otamendi', '1988-02-12T00:00:00.000Z', 'Centre-Back', 'CB', 'SL Benfica', 'Liga Portugal', 1),
('00000000-0000-0000-0000-000000000005',  3, 'Nicolás', 'Alejandro', 'Tagliafico', '1992-08-31T00:00:00.000Z', 'Left-Back', 'LB', 'Olympique Lyon', 'Ligue 1', 1),
('00000000-0000-0000-0000-000000000006', 11, 'Ángel', 'Fabián', 'Di María', '1988-02-14T00:00:00.000Z', 'Right Winger', 'RW', 'SL Benfica', 'Liga Portugal', 1),
('00000000-0000-0000-0000-000000000007',  7, 'Rodrigo', 'Javier', 'de Paul', '1994-05-24T00:00:00.000Z', 'Central Midfield', 'CM', 'Atlético Madrid', 'La Liga', 1),
('00000000-0000-0000-0000-000000000008', 24, 'Enzo', 'Jeremías', 'Fernández', '2001-01-17T00:00:00.000Z', 'Central Midfield', 'CM', 'Chelsea FC', 'Premier League', 1),
('00000000-0000-0000-0000-000000000009', 20, 'Alexis', NULL, 'Mac Allister', '1998-12-24T00:00:00.000Z', 'Central Midfield', 'CM', 'Liverpool FC', 'Premier League', 1),
('00000000-0000-0000-0000-000000000010', 10, 'Lionel', 'Andrés', 'Messi', '1987-06-24T00:00:00.000Z', 'Right Winger', 'RW', 'Inter Miami CF', 'Major League Soccer', 1),
('00000000-0000-0000-0000-000000000011',  9, 'Julián', NULL, 'Álvarez', '2000-01-31T00:00:00.000Z', 'Centre-Forward', 'CF', 'Manchester City', 'Premier League', 1);

-- Substitutes (squad number 5 intentionally skipped - Leandro Paredes will be created during tests)
INSERT INTO players (id, squadNumber, firstName, middleName, lastName, dateOfBirth, position, abbrPosition, team, league, starting11) VALUES
('00000000-0000-0000-0000-000000000012',  1, 'Franco', 'Daniel', 'Armani', '1986-10-16T00:00:00.000Z', 'Goalkeeper', 'GK', 'River Plate', 'Copa de la Liga', 0),
('00000000-0000-0000-0000-000000000013', 12, 'Gerónimo', NULL, 'Rulli', '1992-05-20T00:00:00.000Z', 'Goalkeeper', 'GK', 'Ajax Amsterdam', 'Eredivisie', 0),
('00000000-0000-0000-0000-000000000014',  2, 'Juan', 'Marcos', 'Foyth', '1998-01-12T00:00:00.000Z', 'Right-Back', 'RB', 'Villarreal', 'La Liga', 0),
('00000000-0000-0000-0000-000000000015',  4, 'Gonzalo', 'Ariel', 'Montiel', '1997-01-01T00:00:00.000Z', 'Right-Back', 'RB', 'Nottingham Forest', 'Premier League', 0),
('00000000-0000-0000-0000-000000000016',  6, 'Germán', 'Alejo', 'Pezzella', '1991-06-27T00:00:00.000Z', 'Centre-Back', 'CB', 'Real Betis Balompié', 'La Liga', 0),
('00000000-0000-0000-0000-000000000017',  8, 'Marcos', 'Javier', 'Acuña', '1991-10-28T00:00:00.000Z', 'Left-Back', 'LB', 'Sevilla FC', 'La Liga', 0),
('00000000-0000-0000-0000-000000000018', 25, 'Lisandro', NULL, 'Martínez', '1998-01-18T00:00:00.000Z', 'Centre-Back', 'CB', 'Manchester United', 'Premier League', 0),
('00000000-0000-0000-0000-000000000020', 14, 'Exequiel', 'Alejandro', 'Palacios', '1998-10-05T00:00:00.000Z', 'Central Midfield', 'CM', 'Bayer 04 Leverkusen', 'Bundesliga', 0),
('00000000-0000-0000-0000-000000000021', 17, 'Alejandro', 'Darío', 'Gómez', '1988-02-15T00:00:00.000Z', 'Left Winger', 'LW', 'AC Monza', 'Serie A', 0),
('00000000-0000-0000-0000-000000000022', 18, 'Guido', NULL, 'Rodríguez', '1994-04-12T00:00:00.000Z', 'Defensive Midfield', 'DM', 'Real Betis Balompié', 'La Liga', 0),
('00000000-0000-0000-0000-000000000023', 15, 'Ángel', 'Martín', 'Correa', '1995-03-09T00:00:00.000Z', 'Right Winger', 'RW', 'Atlético Madrid', 'La Liga', 0),
('00000000-0000-0000-0000-000000000024', 16, 'Thiago', 'Ezequiel', 'Almada', '2001-04-26T00:00:00.000Z', 'Attacking Midfield', 'AM', 'Atlanta United FC', 'Major League Soccer', 0),
('00000000-0000-0000-0000-000000000025', 21, 'Paulo', 'Exequiel', 'Dybala', '1993-11-15T00:00:00.000Z', 'Second Striker', 'SS', 'AS Roma', 'Serie A', 0),
('00000000-0000-0000-0000-000000000026', 22, 'Lautaro', 'Javier', 'Martínez', '1997-08-22T00:00:00.000Z', 'Centre-Forward', 'CF', 'Inter Milan', 'Serie A', 0);
