package ar.com.nanotaboada.java.samples.spring.boot.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;

/**
 * Spring Data JPA Repository for {@link Player} entities.
 * <p>
 * Provides data access methods for the {@code players} table using Spring Data's repository abstraction.
 * Extends {@link CrudRepository} for basic CRUD operations and defines custom queries for advanced search functionality.
 * </p>
 *
 * <h3>Provided Methods:</h3>
 * <ul>
 * <li><b>Inherited from CrudRepository:</b> save, findAll, findById, delete, etc.</li>
 * <li><b>Custom Query Methods:</b> League search with case-insensitive wildcard matching</li>
 * <li><b>Derived Queries:</b> findBySquadNumber (method name conventions)</li>
 * </ul>
 *
 * <h3>Query Strategies:</h3>
 * <ul>
 * <li><b>@Query:</b> Explicit JPQL for complex searches (findByLeagueContainingIgnoreCase)</li>
 * <li><b>Method Names:</b> Spring Data derives queries from method names (Query Creation)</li>
 * </ul>
 *
 * @see Player
 * @see org.springframework.data.repository.CrudRepository
 * @see <a href=
 * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation">Query
 * Creation from Method Names</a>
 * @since 4.0.2025
 */
@Repository
public interface PlayersRepository extends CrudRepository<Player, Long> {
    /**
     * Finds a player by their unique identifier.
     * <p>
     * This is a derived query method - Spring Data JPA automatically implements it based on the method name convention.
     * </p>
     *
     * @param id the unique identifier of the player
     * @return an Optional containing the player if found, empty Optional otherwise
     * @see <a href=
     * "https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation">Query
     * Creation from Method Names</a>
     */
    // Non-default methods in interfaces are not shown in coverage reports https://www.jacoco.org/jacoco/trunk/doc/faq.html
    Optional<Player> findById(Long id);

    /**
     * Finds players by league name using case-insensitive wildcard matching.
     * <p>
     * This method uses a custom JPQL query with LIKE operator for partial matches.
     * For example, searching for "Premier" will match "Premier League".
     * </p>
     *
     * @param league the league name to search for (partial matches allowed)
     * @return a list of players whose league name contains the search term (empty
     * list if none found)
     */
    @Query("SELECT p FROM Player p WHERE LOWER(p.league) LIKE LOWER(CONCAT('%', :league, '%'))")
    List<Player> findByLeagueContainingIgnoreCase(@Param("league") String league);

    /**
     * Finds a player by their squad number (exact match).
     * <p>
     * This is a derived query method - Spring Data JPA generates the query automatically.
     * Squad numbers are jersey numbers that users recognize (e.g., Messi is #10).
     * This demonstrates Spring Data's method name query derivation with a natural key.
     * </p>
     *
     * @param squadNumber the squad number to search for (jersey number, typically
     * 1-99)
     * @return an Optional containing the player if found, empty Optional otherwise
     */
    Optional<Player> findBySquadNumber(Integer squadNumber);
}
