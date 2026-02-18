package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.PlayersRepository;
import ar.com.nanotaboada.java.samples.spring.boot.test.PlayerFakes;

@DisplayName("Derived Query Methods on Repository")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureCache
class PlayersRepositoryTests {

    @Autowired
    private PlayersRepository repository;

    /**
     * Given a player exists in the database
     * When findById() is called with that player's ID
     * Then the player is returned
     */
    @Test
    void givenPlayerExists_whenFindById_thenReturnsPlayer() {
        // Given
        Player expected = repository.save(PlayerFakes.createOneValid());
        // When
        Optional<Player> actual = repository.findById(expected.getId());
        // Then
        then(actual).isPresent();
        then(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Given the database does not contain a player with a specific ID
     * When querying by that ID
     * Then an empty Optional is returned
     */
    @Test
    void givenPlayerDoesNotExist_whenFindById_thenReturnsEmpty() {
        // Given
        Long nonExistentId = 999L;
        // When
        Optional<Player> actual = repository.findById(nonExistentId);
        // Then
        then(actual).isEmpty();
    }

    /**
     * Given players exist in a specific league (pre-seeded from dml.sql)
     * When searching by league name
     * Then a list of matching players is returned
     */
    @Test
    void givenPlayersExist_whenFindByLeague_thenReturnsList() {
        // Given
        String leagueName = "Premier";
        // When
        List<Player> actual = repository.findByLeagueContainingIgnoreCase(leagueName);
        // Then
        then(actual).isNotEmpty()
                .allMatch(player -> player.getLeague().toLowerCase().contains(leagueName.toLowerCase()));
    }

    /**
     * Given the database does not contain players in a specific league
     * When searching by that league name
     * Then an empty list is returned
     */
    @Test
    void givenNoPlayersExist_whenFindByLeague_thenReturnsEmptyList() {
        // Given
        String nonExistentLeague = "Nonexistent League";
        // When
        List<Player> actual = repository.findByLeagueContainingIgnoreCase(nonExistentLeague);
        // Then
        then(actual).isEmpty();
    }

    /**
     * Given a player with a specific squad number exists (pre-seeded from dml.sql)
     * When querying by that squad number
     * Then the player record is returned
     */
    @Test
    void givenPlayerExists_whenFindBySquadNumber_thenReturnsPlayer() {
        // Given
        Integer messiSquadNumber = 10; // Pre-seeded from dml.sql
        String expectedLastName = "Messi";
        // When
        Optional<Player> actual = repository.findBySquadNumber(messiSquadNumber);
        // Then
        then(actual).isPresent();
        then(actual.get().getSquadNumber()).isEqualTo(messiSquadNumber);
        then(actual.get().getLastName()).isEqualTo(expectedLastName);
    }

    /**
     * Given the database does not contain a player with a specific squad number
     * When querying by that squad number
     * Then an empty Optional is returned
     */
    @Test
    void givenPlayerDoesNotExist_whenFindBySquadNumber_thenReturnsEmpty() {
        // Given
        Integer nonExistentSquadNumber = 99;
        // When
        Optional<Player> actual = repository.findBySquadNumber(nonExistentSquadNumber);
        // Then
        then(actual).isEmpty();
    }
}
