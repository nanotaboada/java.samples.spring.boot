package ar.com.nanotaboada.java.samples.spring.boot.test;

import java.time.LocalDate;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;

public final class PlayerFakes {

    private PlayerFakes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Leandro Paredes - Test data for CREATE operations
     *
     * Usage:
     * - Repository tests: Insert into real in-memory DB (gets ID 19)
     * - Service tests: Mock expected data for playersService.create()
     * - Controller tests: Mock expected data for POST /players
     *
     * Note: Not pre-seeded in test DB (ID 19 slot is empty)
     */
    public static Player createOneValid() {
        Player player = new Player();
        player.setId(null); // Will be auto-generated as 19
        player.setFirstName("Leandro");
        player.setMiddleName("Daniel");
        player.setLastName("Paredes");
        player.setDateOfBirth(LocalDate.of(1994, 6, 29));
        player.setSquadNumber(5);
        player.setPosition("Defensive Midfield");
        player.setAbbrPosition("DM");
        player.setTeam("AS Roma");
        player.setLeague("Serie A");
        player.setStarting11(false);
        return player;
    }

    /**
     * Damián Martínez - Player ID 1 BEFORE update
     *
     * Usage:
     * - Service tests: Mock expected data for playersService.retrieveById(1L)
     * - Controller tests: Mock expected data for GET /players/1
     *
     * Note: Repository tests query DB directly (pre-seeded in dml.sql)
     */
    public static Player createOneForUpdate() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("Damián");
        player.setMiddleName("Emiliano");
        player.setLastName("Martínez");
        player.setDateOfBirth(LocalDate.of(1992, 9, 2));
        player.setSquadNumber(23);
        player.setPosition("Goalkeeper");
        player.setAbbrPosition("GK");
        player.setTeam("Aston Villa FC");
        player.setLeague("Premier League");
        player.setStarting11(true);
        return player;
    }

    /**
     * Emiliano Martínez - Expected result AFTER updating player ID 1
     *
     * Usage:
     * - Service tests: Mock expected data after playersService.update()
     * - Controller tests: Mock expected data after PUT /players/1
     *
     * Update changes:
     * - firstName: "Damián" → "Emiliano"
     * - middleName: "Emiliano" → null
     *
     * Note: Repository tests should query DB directly for before/after states
     */
    public static Player createOneUpdated() {
        Player player = new Player();
        player.setId(1L);
        player.setFirstName("Emiliano");
        player.setMiddleName(null);
        player.setLastName("Martínez");
        player.setDateOfBirth(LocalDate.of(1992, 9, 2));
        player.setSquadNumber(23);
        player.setPosition("Goalkeeper");
        player.setAbbrPosition("GK");
        player.setTeam("Aston Villa FC");
        player.setLeague("Premier League");
        player.setStarting11(true);
        return player;
    }

    /**
     * Invalid player data - Test data for validation failure scenarios
     *
     * Usage:
     * - Controller tests: Verify validation annotations work
     * (@NotBlank, @Past, @Positive)
     *
     * Violations: blank names, future date, negative squad number, blank fields
     */
    public static Player createOneInvalid() {
        Player player = new Player();
        player.setId(999L);
        player.setFirstName(""); // Invalid (blank)
        player.setMiddleName(null);
        player.setLastName(""); // Invalid (blank)
        player.setDateOfBirth(LocalDate.now()); // Invalid (must be a past date)
        player.setSquadNumber(-1); // Invalid (must be positive)
        player.setPosition(""); // Invalid (blank)
        player.setAbbrPosition(null);
        player.setTeam(""); // Invalid (blank)
        player.setLeague(null);
        player.setStarting11(null);
        return player;
    }

    /**
     * ALL 26 players - Complete Argentina 2022 FIFA World Cup squad
     *
     * Usage:
     * - Service tests: Mock expected data for playersService.retrieveAll()
     * - Controller tests: Mock expected data for GET /players
     *
     * Includes:
     * - 25 players pre-seeded in test DB (IDs 1-26, excluding 19)
     * - Leandro Paredes (ID 19, created during tests)
     *
     * Note: Repository tests query real in-memory DB directly (25 players
     * pre-seeded)
     */
    public static java.util.List<Player> createAll() {
        return java.util.Arrays.asList(
                // Starting 11
                createPlayerWithId(1L, "Damián", "Emiliano", "Martínez", LocalDate.of(1992, 9, 2), 23, "Goalkeeper",
                        "GK", "Aston Villa FC", "Premier League", true),
                createPlayerWithId(2L, "Nahuel", null, "Molina", LocalDate.of(1998, 4, 6), 26, "Right-Back", "RB",
                        "Atlético Madrid", "La Liga", true),
                createPlayerWithId(3L, "Cristian", "Gabriel", "Romero", LocalDate.of(1998, 4, 27), 13, "Centre-Back",
                        "CB", "Tottenham Hotspur", "Premier League", true),
                createPlayerWithId(4L, "Nicolás", "Hernán Gonzalo", "Otamendi", LocalDate.of(1988, 2, 12), 19,
                        "Centre-Back", "CB", "SL Benfica", "Liga Portugal", true),
                createPlayerWithId(5L, "Nicolás", "Alejandro", "Tagliafico", LocalDate.of(1992, 8, 31), 3, "Left-Back",
                        "LB", "Olympique Lyon", "Ligue 1", true),
                createPlayerWithId(6L, "Ángel", "Fabián", "Di María", LocalDate.of(1988, 2, 14), 11, "Right Winger",
                        "RW", "SL Benfica", "Liga Portugal", true),
                createPlayerWithId(7L, "Rodrigo", "Javier", "de Paul", LocalDate.of(1994, 5, 24), 7, "Central Midfield",
                        "CM", "Atlético Madrid", "La Liga", true),
                createPlayerWithId(8L, "Enzo", "Jeremías", "Fernández", LocalDate.of(2001, 1, 17), 24,
                        "Central Midfield", "CM", "Chelsea FC", "Premier League", true),
                createPlayerWithId(9L, "Alexis", null, "Mac Allister", LocalDate.of(1998, 12, 24), 20,
                        "Central Midfield", "CM", "Liverpool FC", "Premier League", true),
                createPlayerWithId(10L, "Lionel", "Andrés", "Messi", LocalDate.of(1987, 6, 24), 10, "Right Winger",
                        "RW", "Inter Miami CF", "Major League Soccer", true),
                createPlayerWithId(11L, "Julián", null, "Álvarez", LocalDate.of(2000, 1, 31), 9, "Centre-Forward", "CF",
                        "Manchester City", "Premier League", true),
                // Substitutes
                createPlayerWithId(12L, "Franco", "Daniel", "Armani", LocalDate.of(1986, 10, 16), 1, "Goalkeeper", "GK",
                        "River Plate", "Copa de la Liga", false),
                createPlayerWithId(13L, "Gerónimo", null, "Rulli", LocalDate.of(1992, 5, 20), 12, "Goalkeeper", "GK",
                        "Ajax Amsterdam", "Eredivisie", false),
                createPlayerWithId(14L, "Juan", "Marcos", "Foyth", LocalDate.of(1998, 1, 12), 2, "Right-Back", "RB",
                        "Villarreal", "La Liga", false),
                createPlayerWithId(15L, "Gonzalo", "Ariel", "Montiel", LocalDate.of(1997, 1, 1), 4, "Right-Back", "RB",
                        "Nottingham Forest", "Premier League", false),
                createPlayerWithId(16L, "Germán", "Alejo", "Pezzella", LocalDate.of(1991, 6, 27), 6, "Centre-Back",
                        "CB", "Real Betis Balompié", "La Liga", false),
                createPlayerWithId(17L, "Marcos", "Javier", "Acuña", LocalDate.of(1991, 10, 28), 8, "Left-Back", "LB",
                        "Sevilla FC", "La Liga", false),
                createPlayerWithId(18L, "Lisandro", null, "Martínez", LocalDate.of(1998, 1, 18), 25, "Centre-Back",
                        "CB", "Manchester United", "Premier League", false),
                // Leandro Paredes (ID 19) - created during tests
                createPlayerWithId(19L, "Leandro", "Daniel", "Paredes", LocalDate.of(1994, 6, 29), 5,
                        "Defensive Midfield", "DM", "AS Roma", "Serie A", false),
                createPlayerWithId(20L, "Exequiel", "Alejandro", "Palacios", LocalDate.of(1998, 10, 5), 14,
                        "Central Midfield", "CM", "Bayer 04 Leverkusen", "Bundesliga", false),
                createPlayerWithId(21L, "Alejandro", "Darío", "Gómez", LocalDate.of(1988, 2, 15), 17, "Left Winger",
                        "LW", "AC Monza", "Serie A", false),
                createPlayerWithId(22L, "Guido", null, "Rodríguez", LocalDate.of(1994, 4, 12), 18, "Defensive Midfield",
                        "DM", "Real Betis Balompié", "La Liga", false),
                createPlayerWithId(23L, "Ángel", "Martín", "Correa", LocalDate.of(1995, 3, 9), 15, "Right Winger", "RW",
                        "Atlético Madrid", "La Liga", false),
                createPlayerWithId(24L, "Thiago", "Ezequiel", "Almada", LocalDate.of(2001, 4, 26), 16,
                        "Attacking Midfield", "AM", "Atlanta United FC", "Major League Soccer", false),
                createPlayerWithId(25L, "Paulo", "Exequiel", "Dybala", LocalDate.of(1993, 11, 15), 21, "Second Striker",
                        "SS", "AS Roma", "Serie A", false),
                createPlayerWithId(26L, "Lautaro", "Javier", "Martínez", LocalDate.of(1997, 8, 22), 22,
                        "Centre-Forward", "CF", "Inter Milan", "Serie A", false));
    }

    private static Player createPlayerWithId(Long id, String firstName, String middleName, String lastName,
            LocalDate dateOfBirth, Integer squadNumber, String position,
            String abbrPosition, String team, String league, Boolean starting11) {
        Player player = new Player();
        player.setId(id);
        player.setFirstName(firstName);
        player.setMiddleName(middleName);
        player.setLastName(lastName);
        player.setDateOfBirth(dateOfBirth);
        player.setSquadNumber(squadNumber);
        player.setPosition(position);
        player.setAbbrPosition(abbrPosition);
        player.setTeam(team);
        player.setLeague(league);
        player.setStarting11(starting11);
        return player;
    }
}
