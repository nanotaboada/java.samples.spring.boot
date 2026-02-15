package ar.com.nanotaboada.java.samples.spring.boot.test;

import java.time.LocalDate;

import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;

public final class PlayerDTOFakes {

    private PlayerDTOFakes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Leandro Paredes - Test data for CREATE operations
     *
     * Usage:
     * - Service tests: Mock expected data for playersService.create()
     * - Controller tests: Mock expected data for POST /players
     *
     * Note: Not pre-seeded in test DB (ID 19 slot is empty)
     */
    public static PlayerDTO createOneValid() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(null); // Will be auto-generated as 19
        playerDTO.setFirstName("Leandro");
        playerDTO.setMiddleName("Daniel");
        playerDTO.setLastName("Paredes");
        playerDTO.setDateOfBirth(LocalDate.of(1994, 6, 29));
        playerDTO.setSquadNumber(5);
        playerDTO.setPosition("Defensive Midfield");
        playerDTO.setAbbrPosition("DM");
        playerDTO.setTeam("AS Roma");
        playerDTO.setLeague("Serie A");
        playerDTO.setStarting11(false);
        return playerDTO;
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
    public static PlayerDTO createOneForUpdate() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setFirstName("Damián");
        playerDTO.setMiddleName("Emiliano");
        playerDTO.setLastName("Martínez");
        playerDTO.setDateOfBirth(LocalDate.of(1992, 9, 2));
        playerDTO.setSquadNumber(23);
        playerDTO.setPosition("Goalkeeper");
        playerDTO.setAbbrPosition("GK");
        playerDTO.setTeam("Aston Villa FC");
        playerDTO.setLeague("Premier League");
        playerDTO.setStarting11(true);
        return playerDTO;
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
     */
    public static PlayerDTO createOneUpdated() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setFirstName("Emiliano");
        playerDTO.setMiddleName(null);
        playerDTO.setLastName("Martínez");
        playerDTO.setDateOfBirth(LocalDate.of(1992, 9, 2));
        playerDTO.setSquadNumber(23);
        playerDTO.setPosition("Goalkeeper");
        playerDTO.setAbbrPosition("GK");
        playerDTO.setTeam("Aston Villa FC");
        playerDTO.setLeague("Premier League");
        playerDTO.setStarting11(true);
        return playerDTO;
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
    public static PlayerDTO createOneInvalid() {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(999L);
        playerDTO.setFirstName(""); // Invalid (blank)
        playerDTO.setMiddleName(null);
        playerDTO.setLastName(""); // Invalid (blank)
        playerDTO.setDateOfBirth(LocalDate.now()); // Invalid (must be a past date)
        playerDTO.setSquadNumber(-1); // Invalid (must be positive)
        playerDTO.setPosition(""); // Invalid (blank)
        playerDTO.setAbbrPosition(null);
        playerDTO.setTeam(""); // Invalid (blank)
        playerDTO.setLeague(null);
        playerDTO.setStarting11(null);
        return playerDTO;
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
    public static java.util.List<PlayerDTO> createAll() {
        return java.util.Arrays.asList(
                // Starting 11
                createPlayerDTOWithId(1L, "Damián", "Emiliano", "Martínez", LocalDate.of(1992, 9, 2), 23, "Goalkeeper",
                        "GK", "Aston Villa FC", "Premier League", true),
                createPlayerDTOWithId(2L, "Nahuel", null, "Molina", LocalDate.of(1998, 4, 6), 26, "Right-Back", "RB",
                        "Atlético Madrid", "La Liga", true),
                createPlayerDTOWithId(3L, "Cristian", "Gabriel", "Romero", LocalDate.of(1998, 4, 27), 13, "Centre-Back",
                        "CB", "Tottenham Hotspur", "Premier League", true),
                createPlayerDTOWithId(4L, "Nicolás", "Hernán Gonzalo", "Otamendi", LocalDate.of(1988, 2, 12), 19,
                        "Centre-Back", "CB", "SL Benfica", "Liga Portugal", true),
                createPlayerDTOWithId(5L, "Nicolás", "Alejandro", "Tagliafico", LocalDate.of(1992, 8, 31), 3,
                        "Left-Back", "LB", "Olympique Lyon", "Ligue 1", true),
                createPlayerDTOWithId(6L, "Ángel", "Fabián", "Di María", LocalDate.of(1988, 2, 14), 11, "Right Winger",
                        "LW", "SL Benfica", "Liga Portugal", true),
                createPlayerDTOWithId(7L, "Rodrigo", "Javier", "de Paul", LocalDate.of(1994, 5, 24), 7,
                        "Central Midfield", "CM", "Atlético Madrid", "La Liga", true),
                createPlayerDTOWithId(8L, "Enzo", "Jeremías", "Fernández", LocalDate.of(2001, 1, 17), 24,
                        "Central Midfield", "CM", "Chelsea FC", "Premier League", true),
                createPlayerDTOWithId(9L, "Alexis", null, "Mac Allister", LocalDate.of(1998, 12, 24), 20,
                        "Central Midfield", "CM", "Liverpool FC", "Premier League", true),
                createPlayerDTOWithId(10L, "Lionel", "Andrés", "Messi", LocalDate.of(1987, 6, 24), 10, "Right Winger",
                        "RW", "Inter Miami CF", "Major League Soccer", true),
                createPlayerDTOWithId(11L, "Julián", null, "Álvarez", LocalDate.of(2000, 1, 31), 9, "Centre-Forward",
                        "CF", "Manchester City", "Premier League", true),
                // Substitutes
                createPlayerDTOWithId(12L, "Franco", "Daniel", "Armani", LocalDate.of(1986, 10, 16), 1, "Goalkeeper",
                        "GK", "River Plate", "Copa de la Liga", false),
                createPlayerDTOWithId(13L, "Gerónimo", null, "Rulli", LocalDate.of(1992, 5, 20), 12, "Goalkeeper", "GK",
                        "Ajax Amsterdam", "Eredivisie", false),
                createPlayerDTOWithId(14L, "Juan", "Marcos", "Foyth", LocalDate.of(1998, 1, 12), 2, "Right-Back", "RB",
                        "Villareal", "La Liga", false),
                createPlayerDTOWithId(15L, "Gonzalo", "Ariel", "Montiel", LocalDate.of(1997, 1, 1), 4, "Right-Back",
                        "RB", "Nottingham Forrest", "Premier League", false),
                createPlayerDTOWithId(16L, "Germán", "Alejo", "Pezzella", LocalDate.of(1991, 6, 27), 6, "Centre-Back",
                        "CB", "Real Betis Balompié", "La Liga", false),
                createPlayerDTOWithId(17L, "Marcos", "Javier", "Acuña", LocalDate.of(1991, 10, 28), 8, "Left-Back",
                        "LB", "Sevilla FC", "La Liga", false),
                createPlayerDTOWithId(18L, "Lisandro", null, "Martínez", LocalDate.of(1998, 1, 18), 25, "Centre-Back",
                        "CB", "Manchester United", "Premier League", false),
                // Leandro Paredes (ID 19) - created during tests
                createPlayerDTOWithId(19L, "Leandro", "Daniel", "Paredes", LocalDate.of(1994, 6, 29), 5,
                        "Defensive Midfield", "DM", "AS Roma", "Serie A", false),
                createPlayerDTOWithId(20L, "Exequiel", "Alejandro", "Palacios", LocalDate.of(1998, 10, 5), 14,
                        "Central Midfield", "CM", "Bayer 04 Leverkusen", "Bundesliga", false),
                createPlayerDTOWithId(21L, "Alejandro", "Darío", "Gómez", LocalDate.of(1988, 2, 15), 17, "Left Winger",
                        "LW", "AC Monza", "Serie A", false),
                createPlayerDTOWithId(22L, "Guido", null, "Rodríguez", LocalDate.of(1994, 4, 12), 18,
                        "Defensive Midfield", "DM", "Real Betis Balompié", "La Liga", false),
                createPlayerDTOWithId(23L, "Ángel", "Martín", "Correa", LocalDate.of(1995, 3, 9), 15, "Right Winger",
                        "RW", "Atlético Madrid", "La Liga", false),
                createPlayerDTOWithId(24L, "Thiago", "Ezequiel", "Almada", LocalDate.of(2001, 4, 26), 16,
                        "Attacking Midfield", "AM", "Atlanta United FC", "Major League Soccer", false),
                createPlayerDTOWithId(25L, "Paulo", "Exequiel", "Dybala", LocalDate.of(1993, 11, 15), 21,
                        "Second Striker", "SS", "AS Roma", "Serie A", false),
                createPlayerDTOWithId(26L, "Lautaro", "Javier", "Martínez", LocalDate.of(1997, 8, 22), 22,
                        "Centre-Forward", "CF", "Inter Milan", "Serie A", false));
    }

    private static PlayerDTO createPlayerDTOWithId(Long id, String firstName, String middleName, String lastName,
            LocalDate dateOfBirth, Integer squadNumber, String position,
            String abbrPosition, String team, String league, Boolean starting11) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(id);
        playerDTO.setFirstName(firstName);
        playerDTO.setMiddleName(middleName);
        playerDTO.setLastName(lastName);
        playerDTO.setDateOfBirth(dateOfBirth);
        playerDTO.setSquadNumber(squadNumber);
        playerDTO.setPosition(position);
        playerDTO.setAbbrPosition(abbrPosition);
        playerDTO.setTeam(team);
        playerDTO.setLeague(league);
        playerDTO.setStarting11(starting11);
        return playerDTO;
    }
}
