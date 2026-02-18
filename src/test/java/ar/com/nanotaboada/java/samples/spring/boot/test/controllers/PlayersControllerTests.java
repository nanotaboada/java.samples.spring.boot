package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.BDDAssertions.then;
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
     * When creating a new player
     * Then response status is 201 Created and Location header points to the new resource
     */
    @Test
    void givenValidPlayer_whenPost_thenReturnsCreated()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        PlayerDTO savedDTO = PlayerDTOFakes.createOneValid();
        savedDTO.setId(19L); // Simulating auto-generated ID
        String content = objectMapper.writeValueAsString(dto);
        Mockito
                .when(playersServiceMock.create(any(PlayerDTO.class)))
                .thenReturn(savedDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).create(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        then(response.getHeader(HttpHeaders.LOCATION)).isNotNull();
        then(response.getHeader(HttpHeaders.LOCATION)).contains(PATH + "/19");
    }

    /**
     * Given invalid player data is provided (validation fails)
     * When attempting to create a player
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void givenInvalidPlayer_whenPost_thenReturnsBadRequest()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneInvalid();
        String content = objectMapper.writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, never()).create(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given a player with the same squad number already exists
     * When attempting to create a player with a duplicate squad number
     * Then response status is 409 Conflict
     */
    @Test
    void givenSquadNumberExists_whenPost_thenReturnsConflict()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        String content = objectMapper.writeValueAsString(dto);
        Mockito
                .when(playersServiceMock.create(any(PlayerDTO.class)))
                .thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).create(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP GET
     * -------------------------------------------------------------------------
     */

    /**
     * Given all players exist in the database
     * When requesting all players
     * Then response status is 200 OK and all players are returned
     */
    @Test
    void givenPlayersExist_whenGetAll_thenReturnsOkWithAllPlayers()
            throws Exception {
        // Given
        List<PlayerDTO> expected = PlayerDTOFakes.createAll();
        Mockito
                .when(playersServiceMock.retrieveAll())
                .thenReturn(expected);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> actual = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Then
        then(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).retrieveAll();
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Given a player exists
     * When requesting that player by ID
     * Then response status is 200 OK and the player data is returned
     */
    @Test
    void givenPlayerExists_whenGetById_thenReturnsOk()
            throws Exception {
        // Given
        PlayerDTO expected = PlayerDTOFakes.createOneForUpdate();
        Mockito
                .when(playersServiceMock.retrieveById(1L))
                .thenReturn(expected);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{id}", 1L);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        PlayerDTO actual = objectMapper.readValue(content, PlayerDTO.class);
        // Then
        then(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).retrieveById(1L);
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Given a player with a specific ID does not exist
     * When requesting that player by ID
     * Then response status is 404 Not Found
     */
    @Test
    void givenPlayerDoesNotExist_whenGetById_thenReturnsNotFound()
            throws Exception {
        // Given
        Long id = 999L;
        Mockito
                .when(playersServiceMock.retrieveById(anyLong()))
                .thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{id}", id);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).retrieveById(anyLong());
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given a player with a specific squad number exists
     * When requesting that player by squad number
     * Then response status is 200 OK and the player data is returned
     */
    @Test
    void givenPlayerExists_whenGetBySquadNumber_thenReturnsOk()
            throws Exception {
        // Given
        Integer squadNumber = 10;
        PlayerDTO expected = PlayerDTOFakes.createAll().stream()
                .filter(player -> squadNumber.equals(player.getSquadNumber()))
                .findFirst()
                .orElseThrow();
        Mockito
                .when(playersServiceMock.retrieveBySquadNumber(squadNumber))
                .thenReturn(expected);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/squadnumber/{squadNumber}", squadNumber);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        PlayerDTO actual = objectMapper.readValue(content, PlayerDTO.class);
        // Then
        then(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).retrieveBySquadNumber(squadNumber);
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(actual).usingRecursiveComparison().isEqualTo(expected);
        then(actual.getSquadNumber()).isEqualTo(squadNumber);
    }

    /**
     * Given no player with a specific squad number exists
     * When requesting a player by that squad number
     * Then response status is 404 Not Found
     */
    @Test
    void givenPlayerDoesNotExist_whenGetBySquadNumber_thenReturnsNotFound()
            throws Exception {
        // Given
        Integer squadNumber = 99;
        Mockito
                .when(playersServiceMock.retrieveBySquadNumber(squadNumber))
                .thenReturn(null);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/squadnumber/{squadNumber}", squadNumber);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).retrieveBySquadNumber(squadNumber);
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given players exist in a specific league
     * When searching for players by league name
     * Then response status is 200 OK and matching players are returned
     */
    @Test
    void givenPlayersExist_whenSearchByLeague_thenReturnsOk()
            throws Exception {
        // Given
        String league = "Premier";
        List<PlayerDTO> expected = PlayerDTOFakes.createAll().stream()
                .filter(p -> p.getLeague().contains(league))
                .toList();
        Mockito
                .when(playersServiceMock.searchByLeague(any()))
                .thenReturn(expected);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/league/{league}", league);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> actual = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Then
        then(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).searchByLeague(any());
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    /**
     * Given no players exist in a specific league
     * When searching for players by that league name
     * Then response status is 200 OK and an empty list is returned
     */
    @Test
    void givenNoPlayersExist_whenSearchByLeague_thenReturnsOk()
            throws Exception {
        // Given
        String league = "Nonexistent League";
        Mockito
                .when(playersServiceMock.searchByLeague(any()))
                .thenReturn(List.of());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search/league/{league}", league);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        String content = response.getContentAsString();
        List<PlayerDTO> actual = objectMapper.readValue(content, new TypeReference<List<PlayerDTO>>() {
        });
        // Then
        then(response.getContentType()).contains("application/json");
        verify(playersServiceMock, times(1)).searchByLeague(any());
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(actual).isEmpty();
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP PUT
     * -------------------------------------------------------------------------
     */

    /**
     * Given a player exists and valid update data is provided
     * When updating that player
     * Then response status is 204 No Content
     */
    @Test
    void givenPlayerExists_whenPut_thenReturnsNoContent()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(1L); // Set ID for update operation
        String content = objectMapper.writeValueAsString(dto);
        Mockito
                .when(playersServiceMock.update(any(PlayerDTO.class)))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", dto.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).update(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given a player with the provided ID does not exist
     * When attempting to update that player
     * Then response status is 404 Not Found
     */
    @Test
    void givenPlayerDoesNotExist_whenPut_thenReturnsNotFound()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(999L); // Set ID for update operation
        String content = objectMapper.writeValueAsString(dto);
        Mockito
                .when(playersServiceMock.update(any(PlayerDTO.class)))
                .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", dto.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).update(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given invalid player data is provided (validation fails)
     * When attempting to update a player
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void givenInvalidPlayer_whenPut_thenReturnsBadRequest()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneInvalid();
        String content = objectMapper.writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", dto.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, never()).update(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given the path ID does not match the body ID
     * When attempting to update a player
     * Then response status is 400 Bad Request and service is never called
     */
    @Test
    void givenIdMismatch_whenPut_thenReturnsBadRequest()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(999L); // Body has different ID
        Long pathId = 1L; // Path has different ID
        String content = objectMapper.writeValueAsString(dto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", pathId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, never()).update(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given the body ID is null (ID only in path)
     * When updating a player
     * Then the ID is set from the path and the update proceeds normally
     */
    @Test
    void givenNullBodyId_whenPut_thenSetsIdFromPath()
            throws Exception {
        // Given
        PlayerDTO dto = PlayerDTOFakes.createOneValid();
        dto.setId(null); // Body has null ID
        Long pathId = 1L;
        String content = objectMapper.writeValueAsString(dto);
        Mockito
                .when(playersServiceMock.update(any(PlayerDTO.class)))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH + "/{id}", pathId)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).update(any(PlayerDTO.class));
        then(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP DELETE
     * -------------------------------------------------------------------------
     */

    /**
     * Given a player exists
     * When deleting that player by ID
     * Then response status is 204 No Content
     */
    @Test
    void givenPlayerExists_whenDelete_thenReturnsNoContent()
            throws Exception {
        // Given
        Mockito
                .when(playersServiceMock.delete(1L))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{id}", 1L);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).delete(1L);
        then(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given a player with a specific ID does not exist
     * When attempting to delete that player by ID
     * Then response status is 404 Not Found
     */
    @Test
    void givenPlayerDoesNotExist_whenDelete_thenReturnsNotFound()
            throws Exception {
        // Given
        Long id = 999L;
        Mockito
                .when(playersServiceMock.delete(id))
                .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{id}", id);
        // When
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Then
        verify(playersServiceMock, times(1)).delete(id);
        then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
