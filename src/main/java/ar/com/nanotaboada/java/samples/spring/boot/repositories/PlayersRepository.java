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
 * Extends {@link JpaRepository} for CRUD operations keyed on the UUID primary key.
 * </p>
 *
 * <h3>Provided Methods:</h3>
 * <ul>
 * <li><b>Inherited from JpaRepository:</b> save, findAll, findById(UUID), existsById, deleteById, etc.</li>
 * <li><b>Derived Queries:</b> findBySquadNumber, findByLeagueContainingIgnoreCase</li>
 * </ul>
 *
 * @see Player
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 4.0.2025
 */
@Repository
public interface PlayersRepository extends JpaRepository<Player, UUID> {

    /**
     * Finds a player by their squad number (exact match).
     * <p>
     * Squad numbers are unique jersey numbers (e.g., Messi is #10).
     * Used as the natural key for mutation endpoints (PUT, DELETE).
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
