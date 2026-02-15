-- Test Database Data (DML - Data Manipulation Language)
-- Contains all 26 players from production database EXCEPT Leandro Paredes (ID 19)
-- Leandro Paredes will be created during tests (for POST/create operations)
-- Damián Emiliano Martínez (ID 1) will be updated during tests
-- Alejandro Gómez (ID 21) will be deleted during tests

-- Starting 11 (IDs 1-11, excluding 19 if applicable)
INSERT INTO players (id, firstName, middleName, lastName, dateOfBirth, squadNumber, position, abbrPosition, team, league, starting11) VALUES
(1, 'Damián', 'Emiliano', 'Martínez', '1992-09-02T00:00:00.000Z', 23, 'Goalkeeper', 'GK', 'Aston Villa FC', 'Premier League', 1),
(2, 'Nahuel', NULL, 'Molina', '1998-04-06T00:00:00.000Z', 26, 'Right-Back', 'RB', 'Atlético Madrid', 'La Liga', 1),
(3, 'Cristian', 'Gabriel', 'Romero', '1998-04-27T00:00:00.000Z', 13, 'Centre-Back', 'CB', 'Tottenham Hotspur', 'Premier League', 1),
(4, 'Nicolás', 'Hernán Gonzalo', 'Otamendi', '1988-02-12T00:00:00.000Z', 19, 'Centre-Back', 'CB', 'SL Benfica', 'Liga Portugal', 1),
(5, 'Nicolás', 'Alejandro', 'Tagliafico', '1992-08-31T00:00:00.000Z', 3, 'Left-Back', 'LB', 'Olympique Lyon', 'Ligue 1', 1),
(6, 'Ángel', 'Fabián', 'Di María', '1988-02-14T00:00:00.000Z', 11, 'Right Winger', 'RW', 'SL Benfica', 'Liga Portugal', 1),
(7, 'Rodrigo', 'Javier', 'de Paul', '1994-05-24T00:00:00.000Z', 7, 'Central Midfield', 'CM', 'Atlético Madrid', 'La Liga', 1),
(8, 'Enzo', 'Jeremías', 'Fernández', '2001-01-17T00:00:00.000Z', 24, 'Central Midfield', 'CM', 'Chelsea FC', 'Premier League', 1),
(9, 'Alexis', NULL, 'Mac Allister', '1998-12-24T00:00:00.000Z', 20, 'Central Midfield', 'CM', 'Liverpool FC', 'Premier League', 1),
(10, 'Lionel', 'Andrés', 'Messi', '1987-06-24T00:00:00.000Z', 10, 'Right Winger', 'RW', 'Inter Miami CF', 'Major League Soccer', 1),
(11, 'Julián', NULL, 'Álvarez', '2000-01-31T00:00:00.000Z', 9, 'Centre-Forward', 'CF', 'Manchester City', 'Premier League', 1);

-- Substitutes (IDs 12-26, excluding 19)
INSERT INTO players (id, firstName, middleName, lastName, dateOfBirth, squadNumber, position, abbrPosition, team, league, starting11) VALUES
(12, 'Franco', 'Daniel', 'Armani', '1986-10-16T00:00:00.000Z', 1, 'Goalkeeper', 'GK', 'River Plate', 'Copa de la Liga', 0),
(13, 'Gerónimo', NULL, 'Rulli', '1992-05-20T00:00:00.000Z', 12, 'Goalkeeper', 'GK', 'Ajax Amsterdam', 'Eredivisie', 0),
(14, 'Juan', 'Marcos', 'Foyth', '1998-01-12T00:00:00.000Z', 2, 'Right-Back', 'RB', 'Villarreal', 'La Liga', 0),
(15, 'Gonzalo', 'Ariel', 'Montiel', '1997-01-01T00:00:00.000Z', 4, 'Right-Back', 'RB', 'Nottingham Forest', 'Premier League', 0),
(16, 'Germán', 'Alejo', 'Pezzella', '1991-06-27T00:00:00.000Z', 6, 'Centre-Back', 'CB', 'Real Betis Balompié', 'La Liga', 0),
(17, 'Marcos', 'Javier', 'Acuña', '1991-10-28T00:00:00.000Z', 8, 'Left-Back', 'LB', 'Sevilla FC', 'La Liga', 0),
(18, 'Lisandro', NULL, 'Martínez', '1998-01-18T00:00:00.000Z', 25, 'Centre-Back', 'CB', 'Manchester United', 'Premier League', 0),
-- ID 19 (Leandro Paredes) intentionally skipped - will be created during tests
(20, 'Exequiel', 'Alejandro', 'Palacios', '1998-10-05T00:00:00.000Z', 14, 'Central Midfield', 'CM', 'Bayer 04 Leverkusen', 'Bundesliga', 0),
(21, 'Alejandro', 'Darío', 'Gómez', '1988-02-15T00:00:00.000Z', 17, 'Left Winger', 'LW', 'AC Monza', 'Serie A', 0),
(22, 'Guido', NULL, 'Rodríguez', '1994-04-12T00:00:00.000Z', 18, 'Defensive Midfield', 'DM', 'Real Betis Balompié', 'La Liga', 0),
(23, 'Ángel', 'Martín', 'Correa', '1995-03-09T00:00:00.000Z', 15, 'Right Winger', 'RW', 'Atlético Madrid', 'La Liga', 0),
(24, 'Thiago', 'Ezequiel', 'Almada', '2001-04-26T00:00:00.000Z', 16, 'Attacking Midfield', 'AM', 'Atlanta United FC', 'Major League Soccer', 0),
(25, 'Paulo', 'Exequiel', 'Dybala', '1993-11-15T00:00:00.000Z', 21, 'Second Striker', 'SS', 'AS Roma', 'Serie A', 0),
(26, 'Lautaro', 'Javier', 'Martínez', '1997-08-22T00:00:00.000Z', 22, 'Centre-Forward', 'CF', 'Inter Milan', 'Serie A', 0);
