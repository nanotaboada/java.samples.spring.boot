package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.Assertions.assertThat;

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
     * Given Leandro Paredes is created and saved to the database with an auto-generated ID
     * When findById() is called with that ID
     * Then the player is returned
     */
    @Test
    void findById_playerExists_returnsPlayer() {
        // Arrange
        Player leandro = PlayerFakes.createOneValid();
        leandro.setId(null);
        Player saved = repository.save(leandro);
        // Act
        Optional<Player> actual = repository.findById(saved.getId());
        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(saved);
    }

    /**
     * Given the database does not contain a player with ID 999
     * When findById(999) is called
     * Then an empty Optional is returned
     */
    @Test
    void findById_playerNotFound_returnsEmpty() {
        // Act
        Optional<Player> actual = repository.findById(999L);
        // Assert
        assertThat(actual).isEmpty();
    }

    /**
     * Given the database contains 7 players in Premier League (pre-seeded in-memory database)
     * When findByLeagueContainingIgnoreCase("Premier") is called
     * Then a list of matching players is returned
     */
    @Test
    void findByLeague_matchingPlayersExist_returnsList() {
        // Act
        List<Player> actual = repository.findByLeagueContainingIgnoreCase("Premier");
        // Assert
        assertThat(actual).isNotEmpty()
                .allMatch(player -> player.getLeague().toLowerCase().contains("premier"));
    }

    /**
     * Given the database does not contain players in "nonexistentleague"
     * When findByLeagueContainingIgnoreCase("nonexistentleague") is called
     * Then an empty list is returned
     */
    @Test
    void findByLeague_noMatches_returnsEmptyList() {
        // Act
        List<Player> actual = repository.findByLeagueContainingIgnoreCase("nonexistentleague");
        // Assert
        assertThat(actual).isEmpty();
    }

    /**
     * Given the database contains Messi with squad number 10 (pre-seeded at ID 10)
     * When findBySquadNumber(10) is called
     * Then Messi's player record is returned
     */
    @Test
    void findBySquadNumber_playerExists_returnsPlayer() {
        // Act
        Optional<Player> actual = repository.findBySquadNumber(10);
        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get())
                .extracting(Player::getSquadNumber, Player::getLastName)
                .containsExactly(10, "Messi");
    }

    /**
     * Given the database does not contain a player with squad number 99
     * When findBySquadNumber(99) is called
     * Then an empty Optional is returned
     */
    @Test
    void findBySquadNumber_playerNotFound_returnsEmpty() {
        // Act
        Optional<Player> actual = repository.findBySquadNumber(99);
        // Assert
        assertThat(actual).isEmpty();
    }
}
