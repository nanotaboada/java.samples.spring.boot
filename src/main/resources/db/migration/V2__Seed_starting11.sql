-- V2: Seed Starting XI players (starting11 = 1)
-- Argentina 2022 FIFA World Cup squad — 11 players who started the final.
-- Rolling back this migration removes only the Starting XI, leaving substitutes intact.

INSERT INTO players (id, squadNumber, firstName, middleName, lastName, dateOfBirth, position, abbrPosition, team, league, starting11) VALUES
    ('01772c59-43f0-5d85-b913-c78e4e281452', 23, 'Damián',  'Emiliano',         'Martínez',  '1992-09-02T00:00:00.000Z', 'Goalkeeper',      'GK', 'Aston Villa FC',           'Premier League', 1),
    ('da31293b-4c7e-5e0f-a168-469ee29ecbc4', 26, 'Nahuel',  NULL,               'Molina',    '1998-04-06T00:00:00.000Z', 'Right-Back',      'RB', 'Atlético Madrid',          'La Liga',        1),
    ('c096c69e-762b-5281-9290-bb9c167a24a0', 13, 'Cristian','Gabriel',          'Romero',    '1998-04-27T00:00:00.000Z', 'Centre-Back',     'CB', 'Tottenham Hotspur',        'Premier League', 1),
    ('d5f7dd7a-1dcb-5960-ba27-e34865b63358', 19, 'Nicolás', 'Hernán Gonzalo',   'Otamendi',  '1988-02-12T00:00:00.000Z', 'Centre-Back',     'CB', 'SL Benfica',               'Liga Portugal',  1),
    ('2f6f90a0-9b9d-5023-96d2-a2aaf03143a6',  3, 'Nicolás', 'Alejandro',        'Tagliafico','1992-08-31T00:00:00.000Z', 'Left-Back',       'LB', 'Olympique Lyon',           'Ligue 1',        1),
    ('b5b46e79-929e-5ed2-949d-0d167109c022', 11, 'Ángel',   'Fabián',           'Di María',  '1988-02-14T00:00:00.000Z', 'Right Winger',    'RW', 'SL Benfica',               'Liga Portugal',  1),
    ('0293b282-1da8-562e-998e-83849b417a42',  7, 'Rodrigo', 'Javier',           'de Paul',   '1994-05-24T00:00:00.000Z', 'Central Midfield','CM', 'Atlético Madrid',          'La Liga',        1),
    ('d3ba552a-dac3-588a-b961-1ea7224017fd', 24, 'Enzo',    'Jeremías',         'Fernández', '2001-01-17T00:00:00.000Z', 'Central Midfield','CM', 'SL Benfica',               'Liga Portugal',  1),
    ('9613cae9-16ab-5b54-937e-3135123b9e0d', 20, 'Alexis',  NULL,               'Mac Allister','1998-12-24T00:00:00.000Z','Central Midfield','CM','Brighton & Hove Albion',   'Premier League', 1),
    ('acc433bf-d505-51fe-831e-45eb44c4d43c', 10, 'Lionel',  'Andrés',           'Messi',     '1987-06-24T00:00:00.000Z', 'Right Winger',    'RW', 'Paris Saint-Germain',      'Ligue 1',        1),
    ('38bae91d-8519-55a2-b30a-b9fe38849bfb',  9, 'Julián',  NULL,               'Álvarez',   '2000-01-31T00:00:00.000Z', 'Centre-Forward',  'CF', 'Manchester City',          'Premier League', 1);
