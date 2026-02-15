package ar.com.nanotaboada.java.samples.spring.boot.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import ar.com.nanotaboada.java.samples.spring.boot.models.Player;
import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.PlayersRepository;
import ar.com.nanotaboada.java.samples.spring.boot.services.PlayersService;
import ar.com.nanotaboada.java.samples.spring.boot.test.PlayerDTOFakes;
import ar.com.nanotaboada.java.samples.spring.boot.test.PlayerFakes;

@DisplayName("CRUD Operations on Service")
@ExtendWith(MockitoExtension.class)
class PlayersServiceTests {

    @Mock
    private PlayersRepository playersRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @InjectMocks
    private PlayersService playersService;

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Create
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the repository does not contain a player with the same squad number
     * When create() is called with valid player data
     * Then the player is saved and a PlayerDTO is returned
     */
    @Test
    void create_noConflict_returnsPlayerDTO() {
        // Arrange
        Player player = PlayerFakes.createOneValid();
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(playerDTO.getSquadNumber()))
                .thenReturn(Optional.empty()); // No conflict
        Mockito
                .when(modelMapperMock.map(playerDTO, Player.class))
                .thenReturn(player);
        Mockito
                .when(playersRepositoryMock.save(any(Player.class)))
                .thenReturn(player);
        Mockito
                .when(modelMapperMock.map(player, PlayerDTO.class))
                .thenReturn(playerDTO);
        // Act
        PlayerDTO result = playersService.create(playerDTO);
        // Assert
        verify(playersRepositoryMock, times(1)).findBySquadNumber(playerDTO.getSquadNumber());
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        verify(modelMapperMock, times(1)).map(playerDTO, Player.class);
        verify(modelMapperMock, times(1)).map(player, PlayerDTO.class);
        assertThat(result).isEqualTo(playerDTO);
    }

    /**
     * Given the repository finds an existing player with the same squad number
     * When create() is called
     * Then null is returned (conflict detected)
     */
    @Test
    void create_squadNumberExists_returnsNull() {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        Player existingPlayer = PlayerFakes.createAll().stream()
                .filter(player -> player.getSquadNumber() == 10)
                .findFirst()
                .orElseThrow();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(playerDTO.getSquadNumber()))
                .thenReturn(Optional.of(existingPlayer));
        // Act
        PlayerDTO result = playersService.create(playerDTO);
        // Assert
        verify(playersRepositoryMock, times(1)).findBySquadNumber(playerDTO.getSquadNumber());
        verify(playersRepositoryMock, never()).save(any(Player.class));
        assertThat(result).isNull();
    }

    /**
     * Given a race condition occurs where another request creates the same squad number
     * When create() is called and save() throws DataIntegrityViolationException
     * Then null is returned (conflict detected via exception)
     */
    @Test
    void create_raceConditionOnSave_returnsNull() {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        Player player = PlayerFakes.createOneValid();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(playerDTO.getSquadNumber()))
                .thenReturn(Optional.empty()); // No conflict initially
        Mockito
                .when(modelMapperMock.map(playerDTO, Player.class))
                .thenReturn(player);
        Mockito
                .when(playersRepositoryMock.save(any(Player.class)))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));
        // Act
        PlayerDTO result = playersService.create(playerDTO);
        // Assert
        verify(playersRepositoryMock, times(1)).findBySquadNumber(playerDTO.getSquadNumber());
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        assertThat(result).isNull();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Retrieve
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the repository finds an existing player with ID 1 (Damián Martínez)
     * When retrieveById(1) is called
     * Then the corresponding PlayerDTO is returned
     */
    @Test
    void retrieveById_playerExists_returnsPlayerDTO() {
        // Arrange
        Player player = PlayerFakes.createOneForUpdate();
        PlayerDTO playerDTO = PlayerDTOFakes.createOneForUpdate();
        Mockito
                .when(playersRepositoryMock.findById(1L))
                .thenReturn(Optional.of(player));
        Mockito
                .when(modelMapperMock.map(player, PlayerDTO.class))
                .thenReturn(playerDTO);
        // Act
        PlayerDTO result = playersService.retrieveById(1L);
        // Assert
        verify(playersRepositoryMock, times(1)).findById(1L);
        verify(modelMapperMock, times(1)).map(player, PlayerDTO.class);
        assertThat(result).isEqualTo(playerDTO);
    }

    /**
     * Given the repository does not find a player with ID 999
     * When retrieveById(999) is called
     * Then null is returned
     */
    @Test
    void retrieveById_playerNotFound_returnsNull() {
        // Arrange
        Mockito
                .when(playersRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());
        // Act
        PlayerDTO result = playersService.retrieveById(999L);
        // Assert
        verify(playersRepositoryMock, times(1)).findById(anyLong());
        verify(modelMapperMock, never()).map(any(Player.class), any());
        assertThat(result).isNull();
    }

    /**
     * Given the repository returns all 26 players
     * When retrieveAll() is called
     * Then a list of 26 PlayerDTOs is returned
     */
    @Test
    void retrieveAll_playersExist_returnsListOfPlayerDTOs() {
        // Arrange
        List<Player> players = PlayerFakes.createAll();
        List<PlayerDTO> playerDTOs = PlayerDTOFakes.createAll();
        Mockito
                .when(playersRepositoryMock.findAll())
                .thenReturn(players);
        // Mock modelMapper to convert each player correctly
        for (int i = 0; i < players.size(); i++) {
            Mockito
                    .when(modelMapperMock.map(players.get(i), PlayerDTO.class))
                    .thenReturn(playerDTOs.get(i));
        }
        // Act
        List<PlayerDTO> result = playersService.retrieveAll();
        // Assert
        verify(playersRepositoryMock, times(1)).findAll();
        assertThat(result).hasSize(26);
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Search
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the repository returns 7 players matching "Premier" league
     * When searchByLeague("Premier") is called
     * Then a list of 7 PlayerDTOs is returned
     */
    @Test
    void searchByLeague_matchingPlayersExist_returnsListOfPlayerDTOs() {
        // Arrange
        List<Player> players = PlayerFakes.createAll().stream()
                .filter(p -> p.getLeague().contains("Premier"))
                .toList();
        List<PlayerDTO> playerDTOs = PlayerDTOFakes.createAll().stream()
                .filter(p -> p.getLeague().contains("Premier"))
                .toList();
        Mockito
                .when(playersRepositoryMock.findByLeagueContainingIgnoreCase(any()))
                .thenReturn(players);
        // Mock modelMapper to convert each player correctly
        for (int i = 0; i < players.size(); i++) {
            Mockito
                    .when(modelMapperMock.map(players.get(i), PlayerDTO.class))
                    .thenReturn(playerDTOs.get(i));
        }
        // Act
        List<PlayerDTO> result = playersService.searchByLeague("Premier");
        // Assert
        verify(playersRepositoryMock, times(1)).findByLeagueContainingIgnoreCase(any());
        assertThat(result).hasSize(7);
    }

    /**
     * Given the repository returns an empty list for "NonexistentLeague"
     * When searchByLeague("NonexistentLeague") is called
     * Then an empty list is returned
     */
    @Test
    void searchByLeague_noMatches_returnsEmptyList() {
        // Arrange
        Mockito
                .when(playersRepositoryMock.findByLeagueContainingIgnoreCase(any()))
                .thenReturn(List.of());
        // Act
        List<PlayerDTO> result = playersService.searchByLeague("NonexistentLeague");
        // Assert
        verify(playersRepositoryMock, times(1)).findByLeagueContainingIgnoreCase(any());
        verify(modelMapperMock, never()).map(any(Player.class), any());
        assertThat(result).isEmpty();
    }

    /**
     * Given the repository finds Messi with squad number 10
     * When searchBySquadNumber(10) is called
     * Then the corresponding PlayerDTO is returned
     */
    @Test
    void searchBySquadNumber_playerExists_returnsPlayerDTO() {
        // Arrange
        Player player = PlayerFakes.createAll().stream()
                .filter(p -> p.getSquadNumber() == 10)
                .findFirst()
                .orElseThrow();
        PlayerDTO playerDTO = PlayerDTOFakes.createAll().stream()
                .filter(p -> p.getSquadNumber() == 10)
                .findFirst()
                .orElseThrow();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(10))
                .thenReturn(Optional.of(player));
        Mockito
                .when(modelMapperMock.map(player, PlayerDTO.class))
                .thenReturn(playerDTO);
        // Act
        PlayerDTO result = playersService.searchBySquadNumber(10);
        // Assert
        verify(playersRepositoryMock, times(1)).findBySquadNumber(10);
        verify(modelMapperMock, times(1)).map(player, PlayerDTO.class);
        assertThat(result).isEqualTo(playerDTO);
        assertThat(result.getSquadNumber()).isEqualTo(10);
    }

    /**
     * Given the repository does not find a player with squad number 99
     * When searchBySquadNumber(99) is called
     * Then null is returned
     */
    @Test
    void searchBySquadNumber_playerNotFound_returnsNull() {
        // Arrange
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(99))
                .thenReturn(Optional.empty());
        // Act
        PlayerDTO result = playersService.searchBySquadNumber(99);
        // Assert
        verify(playersRepositoryMock, times(1)).findBySquadNumber(99);
        verify(modelMapperMock, never()).map(any(Player.class), any());
        assertThat(result).isNull();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Update
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the repository confirms player with ID 1 exists
     * When update() is called with updated data (Damián→Emiliano Martínez)
     * Then the player is saved and true is returned
     */
    @Test
    void update_playerExists_savesAndReturnsTrue() {
        // Arrange
        Player playerUpdated = PlayerFakes.createOneUpdated();
        PlayerDTO playerDTO = PlayerDTOFakes.createOneUpdated();
        Mockito
                .when(playersRepositoryMock.existsById(1L))
                .thenReturn(true);
        Mockito
                .when(modelMapperMock.map(playerDTO, Player.class))
                .thenReturn(playerUpdated);
        // Act
        boolean result = playersService.update(playerDTO);
        // Assert
        verify(playersRepositoryMock, times(1)).existsById(1L);
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        verify(modelMapperMock, times(1)).map(playerDTO, Player.class);
        assertThat(result).isTrue();
    }

    /**
     * Given the repository confirms player with ID 999 does not exist
     * When update() is called
     * Then false is returned without saving
     */
    @Test
    void update_playerNotFound_returnsFalse() {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        playerDTO.setId(999L);
        Mockito
                .when(playersRepositoryMock.existsById(999L))
                .thenReturn(false);
        // Act
        boolean result = playersService.update(playerDTO);
        // Assert
        verify(playersRepositoryMock, times(1)).existsById(999L);
        verify(playersRepositoryMock, never()).save(any(Player.class));
        verify(modelMapperMock, never()).map(playerDTO, Player.class);
        assertThat(result).isFalse();
    }

    /**
     * Given a PlayerDTO has null ID
     * When update() is called
     * Then false is returned without checking repository or saving
     */
    @Test
    void update_nullId_returnsFalse() {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        playerDTO.setId(null);
        // Act
        boolean result = playersService.update(playerDTO);
        // Assert
        verify(playersRepositoryMock, never()).existsById(any());
        verify(playersRepositoryMock, never()).save(any(Player.class));
        verify(modelMapperMock, never()).map(any(), any());
        assertThat(result).isFalse();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Delete
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given the repository confirms player with ID 21 exists (Alejandro Gómez)
     * When delete(21) is called
     * Then the player is deleted and true is returned
     */
    @Test
    void delete_playerExists_deletesAndReturnsTrue() {
        // Arrange
        Mockito
                .when(playersRepositoryMock.existsById(21L))
                .thenReturn(true);
        // Act
        boolean result = playersService.delete(21L);
        // Assert
        verify(playersRepositoryMock, times(1)).existsById(21L);
        verify(playersRepositoryMock, times(1)).deleteById(21L);
        assertThat(result).isTrue();
    }

    /**
     * Given the repository confirms player with ID 999 does not exist
     * When delete(999) is called
     * Then false is returned without deleting
     */
    @Test
    void delete_playerNotFound_returnsFalse() {
        // Arrange
        Mockito
                .when(playersRepositoryMock.existsById(999L))
                .thenReturn(false);
        // Act
        boolean result = playersService.delete(999L);
        // Assert
        verify(playersRepositoryMock, times(1)).existsById(999L);
        verify(playersRepositoryMock, never()).deleteById(anyLong());
        assertThat(result).isFalse();
    }
}
