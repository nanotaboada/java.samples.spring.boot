-- Test Database Data (DML - Data Manipulation Language)
-- Contains all 26 players from the 2022 FIFA World Cup Argentina squad
-- Giovani Lo Celso (squadNumber 27) will be created and deleted during tests
-- Damián Emiliano Martínez (squadNumber 23) will be updated during tests

-- Starting 11 (id is PRIMARY KEY, squadNumber is UNIQUE)
INSERT INTO players (id, squadNumber, firstName, middleName, lastName, dateOfBirth, position, abbrPosition, team, league, starting11) VALUES
('01772c59-43f0-5d85-b913-c78e4e281452', 23, 'Damián', 'Emiliano', 'Martínez', '1992-09-02T00:00:00.000Z', 'Goalkeeper', 'GK', 'Aston Villa FC', 'Premier League', 1),
('da31293b-4c7e-5e0f-a168-469ee29ecbc4', 26, 'Nahuel', NULL, 'Molina', '1998-04-06T00:00:00.000Z', 'Right-Back', 'RB', 'Atlético Madrid', 'La Liga', 1),
('c096c69e-762b-5281-9290-bb9c167a24a0', 13, 'Cristian', 'Gabriel', 'Romero', '1998-04-27T00:00:00.000Z', 'Centre-Back', 'CB', 'Tottenham Hotspur', 'Premier League', 1),
('d5f7dd7a-1dcb-5960-ba27-e34865b63358', 19, 'Nicolás', 'Hernán Gonzalo', 'Otamendi', '1988-02-12T00:00:00.000Z', 'Centre-Back', 'CB', 'SL Benfica', 'Liga Portugal', 1),
('2f6f90a0-9b9d-5023-96d2-a2aaf03143a6',  3, 'Nicolás', 'Alejandro', 'Tagliafico', '1992-08-31T00:00:00.000Z', 'Left-Back', 'LB', 'Olympique Lyon', 'Ligue 1', 1),
('b5b46e79-929e-5ed2-949d-0d167109c022', 11, 'Ángel', 'Fabián', 'Di María', '1988-02-14T00:00:00.000Z', 'Right Winger', 'RW', 'SL Benfica', 'Liga Portugal', 1),
('0293b282-1da8-562e-998e-83849b417a42',  7, 'Rodrigo', 'Javier', 'de Paul', '1994-05-24T00:00:00.000Z', 'Central Midfield', 'CM', 'Atlético Madrid', 'La Liga', 1),
('d3ba552a-dac3-588a-b961-1ea7224017fd', 24, 'Enzo', 'Jeremías', 'Fernández', '2001-01-17T00:00:00.000Z', 'Central Midfield', 'CM', 'SL Benfica', 'Liga Portugal', 1),
('9613cae9-16ab-5b54-937e-3135123b9e0d', 20, 'Alexis', NULL, 'Mac Allister', '1998-12-24T00:00:00.000Z', 'Central Midfield', 'CM', 'Brighton & Hove Albion', 'Premier League', 1),
('acc433bf-d505-51fe-831e-45eb44c4d43c', 10, 'Lionel', 'Andrés', 'Messi', '1987-06-24T00:00:00.000Z', 'Right Winger', 'RW', 'Paris Saint-Germain', 'Ligue 1', 1),
('38bae91d-8519-55a2-b30a-b9fe38849bfb',  9, 'Julián', NULL, 'Álvarez', '2000-01-31T00:00:00.000Z', 'Centre-Forward', 'CF', 'Manchester City', 'Premier League', 1);

-- Substitutes
INSERT INTO players (id, squadNumber, firstName, middleName, lastName, dateOfBirth, position, abbrPosition, team, league, starting11) VALUES
('5a9cd988-95e6-54c1-bc34-9aa08acca8d0',  1, 'Franco', 'Daniel', 'Armani', '1986-10-16T00:00:00.000Z', 'Goalkeeper', 'GK', 'River Plate', 'Copa de la Liga', 0),
('5fdb10e8-38c0-5084-9a3f-b369a960b9c2',  2, 'Juan', 'Marcos', 'Foyth', '1998-01-12T00:00:00.000Z', 'Right-Back', 'RB', 'Villarreal', 'La Liga', 0),
('bbd441f7-fcfb-5834-8468-2a9004b64c8c',  4, 'Gonzalo', 'Ariel', 'Montiel', '1997-01-01T00:00:00.000Z', 'Right-Back', 'RB', 'Nottingham Forest', 'Premier League', 0),
('9d140400-196f-55d8-86e1-e0b96a375c83',  5, 'Leandro', 'Daniel', 'Paredes', '1994-06-29T00:00:00.000Z', 'Defensive Midfield', 'DM', 'AS Roma', 'Serie A', 0),
('d8bfea25-f189-5d5e-b3a5-ed89329b9f7c',  6, 'Germán', 'Alejo', 'Pezzella', '1991-06-27T00:00:00.000Z', 'Centre-Back', 'CB', 'Real Betis Balompié', 'La Liga', 0),
('dca343a8-12e5-53d6-89a8-916b120a5ee4',  8, 'Marcos', 'Javier', 'Acuña', '1991-10-28T00:00:00.000Z', 'Left-Back', 'LB', 'Sevilla FC', 'La Liga', 0),
('c62f2ac1-41e8-5d34-b073-2ba0913d0e31', 12, 'Gerónimo', NULL, 'Rulli', '1992-05-20T00:00:00.000Z', 'Goalkeeper', 'GK', 'Ajax Amsterdam', 'Eredivisie', 0),
('d3b0e8e8-2c34-531a-b608-b24fed0ef986', 14, 'Exequiel', 'Alejandro', 'Palacios', '1998-10-05T00:00:00.000Z', 'Central Midfield', 'CM', 'Bayer 04 Leverkusen', 'Bundesliga', 0),
('b1306b7b-a3a4-5f7c-90fd-dd5bdbed57ba', 15, 'Ángel', 'Martín', 'Correa', '1995-03-09T00:00:00.000Z', 'Right Winger', 'RW', 'Atlético Madrid', 'La Liga', 0),
('ecec27e8-487b-5622-b116-0855020477ed', 16, 'Thiago', 'Ezequiel', 'Almada', '2001-04-26T00:00:00.000Z', 'Attacking Midfield', 'AM', 'Atlanta United FC', 'Major League Soccer', 0),
('7cc8d527-56a2-58bd-9528-2618fc139d30', 17, 'Alejandro', 'Darío', 'Gómez', '1988-02-15T00:00:00.000Z', 'Left Winger', 'LW', 'AC Monza', 'Serie A', 0),
('191c82af-0c51-526a-b903-c3600b61b506', 18, 'Guido', NULL, 'Rodríguez', '1994-04-12T00:00:00.000Z', 'Defensive Midfield', 'DM', 'Real Betis Balompié', 'La Liga', 0),
('7941cd7c-4df1-5952-97e8-1e7f5d08e8aa', 21, 'Paulo', 'Exequiel', 'Dybala', '1993-11-15T00:00:00.000Z', 'Second Striker', 'SS', 'AS Roma', 'Serie A', 0),
('79c96f29-c59f-5f98-96b8-3a5946246624', 22, 'Lautaro', 'Javier', 'Martínez', '1997-08-22T00:00:00.000Z', 'Centre-Forward', 'CF', 'Inter Milan', 'Serie A', 0),
('98306555-a466-5d18-804e-dc82175e697b', 25, 'Lisandro', NULL, 'Martínez', '1998-01-18T00:00:00.000Z', 'Centre-Back', 'CB', 'Manchester United', 'Premier League', 0);
