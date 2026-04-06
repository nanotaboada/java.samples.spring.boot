package ar.com.nanotaboada.java.samples.spring.boot.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;

/**
 * Spring Data JPA Repository for {@link Player} entities.
 * <p>
 * Provides data access methods for the {@code players} table using Spring Data's repository abstraction.
 * Extends {@link JpaRepository} for CRUD operations, batch operations, and query methods.
 * The repository is keyed on {@link Integer} (squad number) as the natural primary key.
 * </p>
 *
 * <h3>Provided Methods:</h3>
 * <ul>
 * <li><b>Inherited from JpaRepository:</b> save, findAll, findById(Integer), existsById, deleteById, etc.</li>
 * <li><b>UUID admin lookup:</b> findById(UUID) — for admin/internal use via GET /players/{id}</li>
 * <li><b>Derived Queries:</b> findBySquadNumber, findByLeagueContainingIgnoreCase</li>
 * </ul>
 *
 * @see Player
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 4.0.2025
 */
@Repository
public interface PlayersRepository extends JpaRepository<Player, Integer> {

    /**
     * Finds a player by their surrogate UUID (admin/internal use only).
     * <p>
     * This is a derived query method on the non-PK {@code id} field.
     * Intended for admin lookups via {@code GET /players/{id}}.
     * </p>
     *
     * @param id the UUID surrogate key
     * @return an Optional containing the player if found, empty Optional otherwise
     */
    Optional<Player> findById(UUID id);

    /**
     * Finds a player by their squad number (exact match).
     * <p>
     * Squad numbers are jersey numbers that users recognize (e.g., Messi is #10).
     * This is a derived query on the natural key field.
     * </p>
     *
     * @param squadNumber the squad number to search for (jersey number, typically 1-99)
     * @return an Optional containing the player if found, empty Optional otherwise
     */
    Optional<Player> findBySquadNumber(Integer squadNumber);

    /**
     * Finds players by league name using case-insensitive wildcard matching.
     *
     * @param league the league name to search for (partial matches allowed)
     * @return a list of players whose league name contains the search term
     */
    List<Player> findByLeagueContainingIgnoreCase(String league);
}
