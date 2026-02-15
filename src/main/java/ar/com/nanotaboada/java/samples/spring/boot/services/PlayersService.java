package ar.com.nanotaboada.java.samples.spring.boot.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;
import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.PlayersRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service layer for managing Player business logic.
 * <p>
 * This service acts as an intermediary between the PlayersController and {@link PlayersRepository},
 * providing CRUD operations, search functionality, and caching.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 * <li><b>Caching:</b> Uses Spring Cache abstraction for improved performance</li>
 * <li><b>DTO Mapping:</b> Converts between {@link Player} entities and {@link PlayerDTO} objects</li>
 * <li><b>Business Logic:</b> Encapsulates domain-specific operations</li>
 * </ul>
 *
 * <h3>Cache Strategy:</h3>
 * <ul>
 * <li><b>@Cacheable:</b> Retrieval operations (read-through cache)</li>
 * <li><b>@CacheEvict(allEntries=true):</b> Mutating operations (create/update/delete) - invalidates entire cache to maintain
 * consistency</li>
 * </ul>
 *
 * <p>
 * <b>Why invalidate all entries?</b> The {@code retrieveAll()} method caches the full player list under a single key.
 * Individual cache evictions would leave this list stale. Using {@code allEntries=true} ensures both individual
 * player caches and the list cache stay synchronized after any data modification.
 * </p>
 *
 * @see PlayersRepository
 * @see PlayerDTO
 * @see Player
 * @see org.modelmapper.ModelMapper
 * @since 4.0.2025
 */
@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayersRepository playersRepository;
    private final ModelMapper modelMapper;

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Create
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Creates a new player and stores it in the database.
     * <p>
     * This method converts the PlayerDTO to a Player entity, persists it, and returns the saved player with its auto-generated
     * ID. The result is automatically cached using the player's ID as the cache key.
     * </p>
     * <p>
     * <b>Conflict Detection:</b> Before creating, checks if a player with the same squad number already exists.
     * Squad numbers are unique identifiers (jersey numbers).
     * </p>
     *
     * @param playerDTO the player data to create (must not be null)
     * @return the created player with auto-generated ID, or null if squad number already exists
     * @see org.springframework.cache.annotation.CacheEvict
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public PlayerDTO create(PlayerDTO playerDTO) {
        // Check if squad number already exists
        if (playersRepository.findBySquadNumber(playerDTO.getSquadNumber()).isPresent()) {
            return null; // Conflict: squad number already taken
        }
        Player player = mapFrom(playerDTO);
        Player savedPlayer = playersRepository.save(player);
        return mapFrom(savedPlayer);
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Retrieve
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Retrieves a player by their unique identifier.
     * <p>
     * This method uses caching to improve performance. If the player is found in the cache, it will be returned without
     * hitting the database. Otherwise, it queries the database and caches the result.
     * Null results (player not found) are not cached to avoid serving stale misses.
     * </p>
     *
     * @param id the unique identifier of the player (must not be null)
     * @return the player DTO if found, null otherwise
     * @see org.springframework.cache.annotation.Cacheable
     */
    @Cacheable(value = "players", key = "#id", unless = "#result == null")
    public PlayerDTO retrieveById(Long id) {
        return playersRepository.findById(id)
                .map(this::mapFrom)
                .orElse(null);
    }

    /**
     * Retrieves all players from the database.
     * <p>
     * This method returns the complete Argentina 2022 FIFA World Cup squad (26 players).
     * Results are cached to improve performance on subsequent calls.
     * </p>
     *
     * @return a list of all players (empty list if none found)
     * @see org.springframework.cache.annotation.Cacheable
     */
    @Cacheable(value = "players")
    public List<PlayerDTO> retrieveAll() {
        return playersRepository.findAll()
                .stream()
                .map(this::mapFrom)
                .toList();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Search
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Searches for players by league name (case-insensitive, partial match).
     * <p>
     * This method performs a wildcard search on the league field, matching any player whose league name contains the search
     * term (e.g., "Premier" matches "Premier League").
     * </p>
     *
     * @param league the league name to search for (must not be null or blank)
     * @return a list of matching players (empty list if none found)
     */
    public List<PlayerDTO> searchByLeague(String league) {
        return playersRepository.findByLeagueContainingIgnoreCase(league)
                .stream()
                .map(this::mapFrom)
                .toList();
    }

    /**
     * Searches for a player by their squad number.
     * <p>
     * This method performs an exact match on the squad number field. Squad numbers are jersey numbers that users recognize
     * (e.g., Messi is #10).
     * </p>
     *
     * @param squadNumber the squad number to search for (jersey number, typically 1-99)
     * @return the player DTO if found, null otherwise
     */
    public PlayerDTO searchBySquadNumber(Integer squadNumber) {
        return playersRepository.findBySquadNumber(squadNumber)
                .map(this::mapFrom)
                .orElse(null);
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Update
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Updates an existing player's information.
     * <p>
     * This method performs a full update (PUT semantics) of the player entity. If the player exists, it updates all fields and
     * refreshes the cache. If the player doesn't exist, returns false without making changes.
     * </p>
     *
     * @param playerDTO the player data to update (must include a valid ID)
     * @return true if the player was updated successfully, false if not found
     * @see org.springframework.cache.annotation.CacheEvict
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public boolean update(PlayerDTO playerDTO) {
        if (playerDTO.getId() != null && playersRepository.existsById(playerDTO.getId())) {
            Player player = mapFrom(playerDTO);
            playersRepository.save(player);
            return true;
        } else {
            return false;
        }
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Delete
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Deletes a player by their unique identifier.
     * <p>
     * This method removes the player from the database and evicts it from the cache. If the player doesn't exist, returns
     * false without making changes.
     * </p>
     *
     * @param id the unique identifier of the player to delete (must not be null)
     * @return true if the player was deleted successfully, false if not found
     * @see org.springframework.cache.annotation.CacheEvict
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public boolean delete(Long id) {
        if (playersRepository.existsById(id)) {
            playersRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private PlayerDTO mapFrom(Player player) {
        return modelMapper.map(player, PlayerDTO.class);
    }

    private Player mapFrom(PlayerDTO dto) {
        return modelMapper.map(dto, Player.class);
    }
}
