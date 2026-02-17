package ar.com.nanotaboada.java.samples.spring.boot.test.services;

import static org.assertj.core.api.BDDAssertions.then;
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
     * Given no existing player with the same squad number
     * When create() is called with valid player data
     * Then the player is saved and a PlayerDTO is returned
     */
    @Test
    void givenNoExistingPlayer_whenCreate_thenReturnsPlayerDTO() {
        // Given
        Player entity = PlayerFakes.createOneValid();
        PlayerDTO expected = PlayerDTOFakes.createOneValid();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(expected.getSquadNumber()))
                .thenReturn(Optional.empty()); // No conflict
        Mockito
                .when(modelMapperMock.map(expected, Player.class))
                .thenReturn(entity);
        Mockito
                .when(playersRepositoryMock.save(any(Player.class)))
                .thenReturn(entity);
        Mockito
                .when(modelMapperMock.map(entity, PlayerDTO.class))
                .thenReturn(expected);
        // When
        PlayerDTO actual = playersService.create(expected);
        // Then
        verify(playersRepositoryMock, times(1)).findBySquadNumber(expected.getSquadNumber());
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        verify(modelMapperMock, times(1)).map(expected, Player.class);
        verify(modelMapperMock, times(1)).map(entity, PlayerDTO.class);
        then(actual).isEqualTo(expected);
    }

    /**
     * Given a player with the same squad number already exists
     * When create() is called
     * Then null is returned (conflict detected)
     */
    @Test
    void givenPlayerAlreadyExists_whenCreate_thenReturnsNull() {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        Integer expectedSquadNumber = 10;
        Player existingPlayer = PlayerFakes.createAll().stream()
                .filter(player -> expectedSquadNumber.equals(player.getSquadNumber()))
                .findFirst()
                .orElseThrow();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(dto.getSquadNumber()))
                .thenReturn(Optional.of(existingPlayer));
        // When
        PlayerDTO actual = playersService.create(dto);
        // Then
        verify(playersRepositoryMock, times(1)).findBySquadNumber(dto.getSquadNumber());
        verify(playersRepositoryMock, never()).save(any(Player.class));
        then(actual).isNull();
    }

    /**
     * Given a race condition occurs where another request creates the same squad number
     * When create() is called and save() throws DataIntegrityViolationException
     * Then null is returned (conflict detected via exception)
     */
    @Test
    void givenRaceCondition_whenCreate_thenReturnsNull() {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        Player entity = PlayerFakes.createOneValid();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(dto.getSquadNumber()))
                .thenReturn(Optional.empty()); // No conflict initially
        Mockito
                .when(modelMapperMock.map(dto, Player.class))
                .thenReturn(entity);
        Mockito
                .when(playersRepositoryMock.save(any(Player.class)))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));
        // When
        PlayerDTO actual = playersService.create(dto);
        // Then
        verify(playersRepositoryMock, times(1)).findBySquadNumber(dto.getSquadNumber());
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        then(actual).isNull();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Retrieve
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given all players exist in the repository
     * When retrieveAll() is called
     * Then a list of all player DTOs is returned
     */
    @Test
    void givenAllPlayersExist_whenRetrieveAll_thenReturns26Players() {
        // Given
        List<Player> entities = PlayerFakes.createAll();
        List<PlayerDTO> dtos = PlayerDTOFakes.createAll();
        Mockito
                .when(playersRepositoryMock.findAll())
                .thenReturn(entities);
        // Mock modelMapper to convert each player correctly
        for (int i = 0; i < entities.size(); i++) {
            Mockito
                    .when(modelMapperMock.map(entities.get(i), PlayerDTO.class))
                    .thenReturn(dtos.get(i));
        }
        // When
        List<PlayerDTO> actual = playersService.retrieveAll();
        // Then
        verify(playersRepositoryMock, times(1)).findAll();
        then(actual).hasSize(26);
        then(actual).usingRecursiveComparison().isEqualTo(dtos);
    }

    /**
     * Given a player exists with a specific ID
     * When retrieving that player by ID
     * Then the corresponding PlayerDTO is returned
     */
    @Test
    void givenPlayerExists_whenRetrieveById_thenReturnsPlayerDTO() {
        // Given
        Player entity = PlayerFakes.createOneForUpdate();
        PlayerDTO expected = PlayerDTOFakes.createOneForUpdate();
        Mockito
                .when(playersRepositoryMock.findById(1L))
                .thenReturn(Optional.of(entity));
        Mockito
                .when(modelMapperMock.map(entity, PlayerDTO.class))
                .thenReturn(expected);
        // When
        PlayerDTO actual = playersService.retrieveById(1L);
        // Then
        verify(playersRepositoryMock, times(1)).findById(1L);
        verify(modelMapperMock, times(1)).map(entity, PlayerDTO.class);
        then(actual).isEqualTo(expected);
    }

    /**
     * Given no player exists with a specific ID
     * When retrieving by that ID
     * Then null is returned
     */
    @Test
    void givenPlayerDoesNotExist_whenRetrieveById_thenReturnsNull() {
        // Given
        Long id = 999L;
        Mockito
                .when(playersRepositoryMock.findById(anyLong()))
                .thenReturn(Optional.empty());
        // When
        PlayerDTO actual = playersService.retrieveById(id);
        // Then
        verify(playersRepositoryMock, times(1)).findById(anyLong());
        verify(modelMapperMock, never()).map(any(Player.class), any());
        then(actual).isNull();
    }

    /**
     * Given a player with a specific squad number exists
     * When retrieving by that squad number
     * Then the corresponding PlayerDTO is returned
     */
    @Test
    void givenPlayerExists_whenRetrieveBySquadNumber_thenReturnsPlayerDTO() {
        // Given
        Integer squadNumber = 10;
        Player entity = PlayerFakes.createAll().stream()
                .filter(player -> squadNumber.equals(player.getSquadNumber()))
                .findFirst()
                .orElseThrow();
        PlayerDTO expected = PlayerDTOFakes.createAll().stream()
                .filter(player -> squadNumber.equals(player.getSquadNumber()))
                .findFirst()
                .orElseThrow();
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(squadNumber))
                .thenReturn(Optional.of(entity));
        Mockito
                .when(modelMapperMock.map(entity, PlayerDTO.class))
                .thenReturn(expected);
        // When
        PlayerDTO actual = playersService.retrieveBySquadNumber(squadNumber);
        // Then
        verify(playersRepositoryMock, times(1)).findBySquadNumber(squadNumber);
        verify(modelMapperMock, times(1)).map(entity, PlayerDTO.class);
        then(actual).isEqualTo(expected);
        then(actual.getSquadNumber()).isEqualTo(squadNumber);
    }

    /**
     * Given no player exists with a specific squad number
     * When retrieving by that squad number
     * Then null is returned
     */
    @Test
    void givenPlayerDoesNotExist_whenRetrieveBySquadNumber_thenReturnsNull() {
        // Given
        Integer squadNumber = 99;
        Mockito
                .when(playersRepositoryMock.findBySquadNumber(squadNumber))
                .thenReturn(Optional.empty());
        // When
        PlayerDTO actual = playersService.retrieveBySquadNumber(squadNumber);
        // Then
        verify(playersRepositoryMock, times(1)).findBySquadNumber(squadNumber);
        verify(modelMapperMock, never()).map(any(Player.class), any());
        then(actual).isNull();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Search
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given players exist in a specific league
     * When searching by league name
     * Then a list of matching player DTOs is returned
     */
    @Test
    void givenPlayersExist_whenSearchByLeague_thenReturns7Players() {
        // Given
        String league = "Premier";
        List<Player> entities = PlayerFakes.createAll().stream()
                .filter(player -> player.getLeague().contains(league))
                .toList();
        List<PlayerDTO> expected = PlayerDTOFakes.createAll().stream()
                .filter(player -> player.getLeague().contains(league))
                .toList();
        Mockito
                .when(playersRepositoryMock.findByLeagueContainingIgnoreCase(any()))
                .thenReturn(entities);
        // Mock modelMapper to convert each player correctly
        for (int i = 0; i < entities.size(); i++) {
            Mockito
                    .when(modelMapperMock.map(entities.get(i), PlayerDTO.class))
                    .thenReturn(expected.get(i));
        }
        // When
        List<PlayerDTO> actual = playersService.searchByLeague(league);
        // Then
        verify(playersRepositoryMock, times(1)).findByLeagueContainingIgnoreCase(any());
        then(actual)
                .hasSize(7)
                .allSatisfy(dto -> then(dto.getLeague()).contains(league));
    }

    /**
     * Given no players exist in a specific league
     * When searching by that league name
     * Then an empty list is returned
     */
    @Test
    void givenNoPlayersExist_whenSearchByLeague_thenReturnsEmptyList() {
        // Given
        String league = "Nonexistent League";
        Mockito
                .when(playersRepositoryMock.findByLeagueContainingIgnoreCase(any()))
                .thenReturn(List.of());
        // When
        List<PlayerDTO> actual = playersService.searchByLeague(league);
        // Then
        verify(playersRepositoryMock, times(1)).findByLeagueContainingIgnoreCase(any());
        verify(modelMapperMock, never()).map(any(Player.class), any());
        then(actual).isEmpty();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Update
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given a player exists
     * When update() is called with modified player data
     * Then the player is updated and true is returned
     */
    @Test
    void givenPlayerExists_whenUpdate_thenReturnsTrue() {
        // Given
        Player entity = PlayerFakes.createOneUpdated();
        PlayerDTO dto = PlayerDTOFakes.createOneUpdated();
        Mockito
                .when(playersRepositoryMock.existsById(1L))
                .thenReturn(true);
        Mockito
                .when(modelMapperMock.map(dto, Player.class))
                .thenReturn(entity);
        // When
        boolean actual = playersService.update(dto);
        // Then
        verify(playersRepositoryMock, times(1)).existsById(1L);
        verify(playersRepositoryMock, times(1)).save(any(Player.class));
        verify(modelMapperMock, times(1)).map(dto, Player.class);
        then(actual).isTrue();
    }

    /**
     * Given no player exists with the specified ID
     * When update() is called
     * Then false is returned without saving
     */
    @Test
    void givenPlayerDoesNotExist_whenUpdate_thenReturnsFalse() {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(999L);
        Mockito
                .when(playersRepositoryMock.existsById(999L))
                .thenReturn(false);
        // When
        boolean actual = playersService.update(dto);
        // Then
        verify(playersRepositoryMock, times(1)).existsById(999L);
        verify(playersRepositoryMock, never()).save(any(Player.class));
        verify(modelMapperMock, never()).map(dto, Player.class);
        then(actual).isFalse();
    }

    /**
     * Given a PlayerDTO has null ID
     * When update() is called
     * Then false is returned without checking repository or saving
     */
    @Test
    void givenNullId_whenUpdate_thenReturnsFalse() {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(null);
        // When
        boolean actual = playersService.update(dto);
        // Then
        verify(playersRepositoryMock, never()).existsById(any());
        verify(playersRepositoryMock, never()).save(any(Player.class));
        verify(modelMapperMock, never()).map(any(), any());
        then(actual).isFalse();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * Delete
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Given a player exists
     * When deleting that player
     * Then the player is deleted and true is returned
     */
    @Test
    void givenPlayerExists_whenDelete_thenReturnsTrue() {
        // Given
        Mockito
                .when(playersRepositoryMock.existsById(21L))
                .thenReturn(true);
        // When
        boolean actual = playersService.delete(21L);
        // Then
        verify(playersRepositoryMock, times(1)).existsById(21L);
        verify(playersRepositoryMock, times(1)).deleteById(21L);
        then(actual).isTrue();
    }

    /**
     * Given no player exists with a specific ID
     * When attempting to delete that player
     * Then false is returned without deleting
     */
    @Test
    void givenPlayerDoesNotExist_whenDelete_thenReturnsFalse() {
        // Given
        Mockito
                .when(playersRepositoryMock.existsById(999L))
                .thenReturn(false);
        // When
        boolean actual = playersService.delete(999L);
        // Then
        verify(playersRepositoryMock, times(1)).existsById(999L);
        verify(playersRepositoryMock, never()).deleteById(anyLong());
        then(actual).isFalse();
    }
}
