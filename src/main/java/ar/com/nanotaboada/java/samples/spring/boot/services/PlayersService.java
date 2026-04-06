package ar.com.nanotaboada.java.samples.spring.boot.services;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;
import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.PlayersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
     * Converts the PlayerDTO to a Player entity, persists it (UUID generated via
     * {@code GenerationType.UUID}), and returns the saved player with its assigned UUID.
     * </p>
     * <p>
     * <b>Conflict Detection:</b> Checks if a player with the same squad number already exists.
     * If a race condition occurs between check and save, DataIntegrityViolationException is caught
     * and null is returned to indicate conflict.
     * </p>
     *
     * @param playerDTO the player data to create (must not be null)
     * @return the created player with generated UUID, or null if squad number already exists
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public PlayerDTO create(PlayerDTO playerDTO) {
        log.debug("Creating new player with squad number: {}", playerDTO.getSquadNumber());

        if (playersRepository.findBySquadNumber(playerDTO.getSquadNumber()).isPresent()) {
            log.warn("Cannot create player - squad number {} already exists", playerDTO.getSquadNumber());
            return null;
        }

        try {
            Player player = mapFrom(playerDTO);
            Player savedPlayer = playersRepository.save(player);
            PlayerDTO result = mapFrom(savedPlayer);
            log.info("Player created successfully - ID: {}, Squad Number: {}", result.getId(), result.getSquadNumber());
            return result;
        } catch (DataIntegrityViolationException _) {
            log.warn("Cannot create player - squad number {} already exists (race condition)", playerDTO.getSquadNumber());
            return null;
        }
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Retrieve
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Retrieves all players from the database.
     *
     * @return a list of all players (empty list if none found)
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "players")
    public List<PlayerDTO> retrieveAll() {
        return playersRepository.findAll()
                .stream()
                .map(this::mapFrom)
                .toList();
    }

    /**
     * Retrieves a player by their UUID primary key.
     * <p>
     * Uses caching to improve performance. Null results are not cached.
     * </p>
     *
     * @param id the UUID primary key (must not be null)
     * @return the player DTO if found, null otherwise
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "players", key = "#id", unless = "#result == null")
    public PlayerDTO retrieveById(UUID id) {
        return playersRepository.findById(id)
                .map(this::mapFrom)
                .orElse(null);
    }

    /**
     * Retrieves a player by their squad number.
     * <p>
     * Squad numbers are unique jersey numbers (e.g., Messi is #10). Results are cached.
     * </p>
     *
     * @param squadNumber the squad number to retrieve (jersey number, typically 1-99)
     * @return the player DTO if found, null otherwise
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "players", key = "'squad-' + #squadNumber", unless = "#result == null")
    public PlayerDTO retrieveBySquadNumber(Integer squadNumber) {
        return playersRepository.findBySquadNumber(squadNumber)
                .map(this::mapFrom)
                .orElse(null);
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Search
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Searches for players by league name (case-insensitive, partial match).
     *
     * @param league the league name to search for (must not be null or blank)
     * @return a list of matching players (empty list if none found)
     */
    @Transactional(readOnly = true)
    public List<PlayerDTO> searchByLeague(String league) {
        return playersRepository.findByLeagueContainingIgnoreCase(league)
                .stream()
                .map(this::mapFrom)
                .toList();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Update
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Updates an existing player identified by their squad number.
     * <p>
     * Looks up the existing player by squad number to retrieve the UUID primary key,
     * maps the DTO to an entity, preserves the UUID, and saves. Returns false if not found.
     * </p>
     *
     * @param squadNumber the squad number (natural key) of the player to update
     * @param playerDTO the player data to update
     * @return true if the player was updated successfully, false if not found
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public boolean update(Integer squadNumber, PlayerDTO playerDTO) {
        log.debug("Updating player with squad number: {}", squadNumber);

        if (squadNumber == null) {
            log.warn("Cannot update player - squad number is null");
            return false;
        }

        return playersRepository.findBySquadNumber(squadNumber)
                .map(existing -> {
                    Player player = mapFrom(playerDTO);
                    player.setId(existing.getId());
                    playersRepository.save(player);
                    log.info("Player updated successfully - Squad Number: {}", squadNumber);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Cannot update player - squad number {} not found", squadNumber);
                    return false;
                });
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Delete
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Deletes a player by their squad number.
     * <p>
     * Looks up the player by squad number to retrieve the UUID primary key, then deletes by UUID.
     * Returns false if the player doesn't exist.
     * </p>
     *
     * @param squadNumber the squad number of the player to delete (must not be null)
     * @return true if the player was deleted successfully, false if not found
     */
    @Transactional
    @CacheEvict(value = "players", allEntries = true)
    public boolean deleteBySquadNumber(Integer squadNumber) {
        log.debug("Deleting player with squad number: {}", squadNumber);

        if (squadNumber == null) {
            log.warn("Cannot delete player - squad number is null");
            return false;
        }

        return playersRepository.findBySquadNumber(squadNumber)
                .map(existing -> {
                    playersRepository.delete(existing);
                    log.info("Player deleted successfully - Squad Number: {}", squadNumber);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Cannot delete player - squad number {} not found", squadNumber);
                    return false;
                });
    }

    private PlayerDTO mapFrom(Player player) {
        return modelMapper.map(player, PlayerDTO.class);
    }

    private Player mapFrom(PlayerDTO dto) {
        return modelMapper.map(dto, Player.class);
    }
}
