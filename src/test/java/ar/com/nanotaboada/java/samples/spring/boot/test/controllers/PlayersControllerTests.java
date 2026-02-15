package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.PlayersController;
import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.PlayersRepository;
import ar.com.nanotaboada.java.samples.spring.boot.services.PlayersService;
import ar.com.nanotaboada.java.samples.spring.boot.test.PlayerDTOFakes;

@DisplayName("HTTP Methods on Controller")
@WebMvcTest(PlayersController.class)
@AutoConfigureCache
@AutoConfigureJsonTesters
class PlayersControllerTests {

    private static final String PATH = "/players";

    @Autowired
    private MockMvc application;

    @MockitoBean
    private PlayersService playersServiceMock;

    @MockitoBean
    private PlayersRepository playersRepositoryMock;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ObjectMapperTestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP POST
     * -------------------------------------------------------------------------
     */

    /**
     * Given valid player data is provided
     * When POST /players is called and the service successfully creates the player
     * Then response status is 201 Created and Location header points to the new resource
     */
    @Test
    void post_validPlayer_returnsCreated()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        PlayerDTO savedPlayer = PlayerDTOFakes.createOneValid();
        savedPlayer.setId(19L); // Simulating auto-generated ID
        String body = objectMapper.writeValueAsString(playerDTO);
        Mockito
                .when(playersServiceMock.create(any(PlayerDTO.class)))
                .thenReturn(savedPlayer);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).create(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull();
        assertThat(response.getHeader(HttpHeaders.LOCATION)).contains(PATH + "/19");
    }

    /**
     * Given invalid player data is provided (validation fails)
     * When POST /players is called
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void post_invalidPlayer_returnsBadRequest()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneInvalid();
        String body = objectMapper.writeValueAsString(playerDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, never()).create(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given a player with the same squad number already exists
     * When POST /players is called and the service detects a conflict
     * Then response status is 409 Conflict
     */
    @Test
    void post_squadNumberExists_returnsConflict()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        String body = objectMapper.writeValueAsString(playerDTO);
        Mockito
                .when(playersServiceMock.create(any(PlayerDTO.class)))
                .thenReturn(null); // Conflict: squad number already exists
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).create(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP GET
     * -------------------------------------------------------------------------
     */

    /**
     * Given a player with ID 1 exists (Damián Martínez)
     * When GET /players/1 is called and the service returns the player
     * Then response status is 200 OK and the player data is returned
     */
    @Test
    void getById_playerExists_returnsOkWithPlayer()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneForUpdate();
        Long id = 1L;
        Mockito
                .when(playersServiceMock.retrieveById(id))
                .thenReturn(playerDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{id}", id);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        PlayerDTO result = objectMapper.readValue(content, PlayerDTO.class);
        // Assert
        assertThat(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).retrieveById(id);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).usingRecursiveComparison().isEqualTo(playerDTO);
    }

    /**
     * Given a player with ID 999 does not exist
     * When GET /players/999 is called and the service returns null
     * Then response status is 404 Not Found
     */
    @Test
    void getById_playerNotFound_returnsNotFound()
            throws Exception {
        // Arrange
        Long id = 999L;
        Mockito
                .when(playersServiceMock.retrieveById(anyLong()))
                .thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{id}", id);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).retrieveById(anyLong());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 26 players exist in the database
     * When GET /players is called and the service returns all players
     * Then response status is 200 OK and all 26 players are returned
     */
    @Test
    void getAll_playersExist_returnsOkWithAllPlayers()
            throws Exception {
        // Arrange
        List<PlayerDTO> playerDTOs = PlayerDTOFakes.createAll();
        Mockito
                .when(playersServiceMock.retrieveAll())
                .thenReturn(playerDTOs);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> result = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Assert
        assertThat(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).retrieveAll();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).hasSize(26);
        assertThat(result).usingRecursiveComparison().isEqualTo(playerDTOs);
    }

    /**
     * Given 7 players exist in Premier League
     * When GET /players/search/league/Premier is called and the service returns matching players
     * Then response status is 200 OK and 7 players are returned
     */
    @Test
    void searchByLeague_matchingPlayersExist_returnsOkWithList()
            throws Exception {
        // Arrange
        List<PlayerDTO> playerDTOs = PlayerDTOFakes.createAll().stream()
                .filter(p -> p.getLeague().contains("Premier"))
                .toList();
        Mockito
                .when(playersServiceMock.searchByLeague(any()))
                .thenReturn(playerDTOs);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/league/{league}", "Premier");
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> result = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Assert
        assertThat(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).searchByLeague(any());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).hasSize(7);
        assertThat(result).usingRecursiveComparison().isEqualTo(playerDTOs);
    }

    /**
     * Given no players exist in "NonexistentLeague"
     * When GET /players/search/league/NonexistentLeague is called and the service returns an empty list
     * Then response status is 200 OK and an empty list is returned
     */
    @Test
    void searchByLeague_noMatches_returnsOkWithEmptyList()
            throws Exception {
        // Arrange
        Mockito
                .when(playersServiceMock.searchByLeague(any()))
                .thenReturn(List.of());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/league/{league}", "NonexistentLeague");
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> result = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Assert
        assertThat(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).searchByLeague(any());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).isEmpty();
    }

    /**
     * Given a player with squad number 10 exists (Messi)
     * When GET /players/search/squadnumber/10 is called and the service returns the player
     * Then response status is 200 OK and the player data is returned
     */
    @Test
    void searchBySquadNumber_playerExists_returnsOkWithPlayer()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createAll().get(9); // Messi is at index 9
        Mockito
                .when(playersServiceMock.searchBySquadNumber(10))
                .thenReturn(playerDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/squadnumber/{squadNumber}", 10);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        PlayerDTO result = objectMapper.readValue(content, PlayerDTO.class);
        // Assert
        assertThat(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).searchBySquadNumber(10);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).usingRecursiveComparison().isEqualTo(playerDTO);
        assertThat(result.getSquadNumber()).isEqualTo(10);
    }

    /**
     * Given no player with squad number 99 exists
     * When GET /players/search/squadnumber/99 is called and the service returns null
     * Then response status is 404 Not Found
     */
    @Test
    void searchBySquadNumber_playerNotFound_returnsNotFound()
            throws Exception {
        // Arrange
        Mockito
                .when(playersServiceMock.searchBySquadNumber(99))
                .thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/squadnumber/{squadNumber}", 99);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).searchBySquadNumber(99);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP PUT
     * -------------------------------------------------------------------------
     */

    /**
     * Given a player exists and valid update data is provided
     * When PUT /players/{id} is called and the service successfully updates the player
     * Then response status is 204 No Content
     */
    @Test
    void put_playerExists_returnsNoContent()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        playerDTO.setId(1L); // Set ID for update operation
        Long id = playerDTO.getId();
        String body = objectMapper.writeValueAsString(playerDTO);
        Mockito
                .when(playersServiceMock.update(any(PlayerDTO.class)))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).update(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given a player with the provided ID does not exist
     * When PUT /players/{id} is called and the service returns false
     * Then response status is 404 Not Found
     */
    @Test
    void put_playerNotFound_returnsNotFound()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        playerDTO.setId(999L); // Set ID for update operation
        Long id = playerDTO.getId();
        String body = objectMapper.writeValueAsString(playerDTO);
        Mockito
                .when(playersServiceMock.update(any(PlayerDTO.class)))
                .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).update(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given invalid player data is provided (validation fails)
     * When PUT /players/{id} is called
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void put_invalidPlayer_returnsBadRequest()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneInvalid();
        Long id = playerDTO.getId();
        String body = objectMapper.writeValueAsString(playerDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, never()).update(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given the path ID does not match the body ID
     * When PUT /players/{id} is called
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void put_idMismatch_returnsBadRequest()
            throws Exception {
        // Arrange
        PlayerDTO playerDTO = PlayerDTOFakes.createOneValid();
        playerDTO.setId(999L); // Body has different ID
        Long pathId = 1L; // Path has different ID
        String body = objectMapper.writeValueAsString(playerDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", pathId)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, never()).update(any(PlayerDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP DELETE
     * -------------------------------------------------------------------------
     */

    /**
     * Given a player with ID 1 exists
     * When DELETE /players/1 is called and the service successfully deletes the player
     * Then response status is 204 No Content
     */
    @Test
    void delete_playerExists_returnsNoContent()
            throws Exception {
        // Arrange
        Long id = 1L;
        Mockito
                .when(playersServiceMock.delete(anyLong()))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{id}", id);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).delete(anyLong());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given a player with ID 999 does not exist
     * When DELETE /players/999 is called and the service returns false
     * Then response status is 404 Not Found
     */
    @Test
    void delete_playerNotFound_returnsNotFound()
            throws Exception {
        // Arrange
        Long id = 999L;
        Mockito
                .when(playersServiceMock.delete(anyLong()))
                .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{id}", id);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(playersServiceMock, times(1)).delete(anyLong());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
