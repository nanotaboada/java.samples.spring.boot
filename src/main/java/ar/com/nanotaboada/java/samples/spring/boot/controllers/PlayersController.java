package ar.com.nanotaboada.java.samples.spring.boot.controllers;

import static org.springframework.http.HttpHeaders.LOCATION;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ar.com.nanotaboada.java.samples.spring.boot.models.PlayerDTO;
import ar.com.nanotaboada.java.samples.spring.boot.services.PlayersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for managing Player resources.
 * <p>
 * Provides HTTP endpoints for CRUD operations and search functionality on the Argentina 2022 FIFA World Cup squad.
 * All endpoints return JSON responses and follow RESTful conventions.
 * </p>
 *
 * <h3>Base Path:</h3>
 * <ul>
 * <li><b>GET</b> {@code /players} - Retrieve all players</li>
 * <li><b>GET</b> {@code /players/{id}} - Retrieve player by ID</li>
 * <li><b>GET</b> {@code /players/search/league/{league}} - Search players by league name</li>
 * <li><b>GET</b> {@code /players/search/squadnumber/{number}} - Search player by squad number</li>
 * <li><b>POST</b> {@code /players} - Create a new player</li>
 * <li><b>PUT</b> {@code /players/{id}} - Update an existing player</li>
 * <li><b>DELETE</b> {@code /players/{id}} - Delete a player by ID</li>
 * </ul>
 *
 * <h3>Response Codes:</h3>
 * <ul>
 * <li><b>200 OK:</b> Successful retrieval</li>
 * <li><b>201 Created:</b> Successful creation (with Location header)</li>
 * <li><b>204 No Content:</b> Successful update/delete</li>
 * <li><b>400 Bad Request:</b> Validation failure</li>
 * <li><b>404 Not Found:</b> Resource not found</li>
 * </ul>
 *
 * @see PlayersService
 * @see PlayerDTO
 * @since 4.0.2025
 */
@RestController
@Tag(name = "Players")
@RequiredArgsConstructor
public class PlayersController {

    private final PlayersService playersService;

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * HTTP POST
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Creates a new player resource.
     * <p>
     * Validates the request body and creates a new player in the database. Returns a 201 Created response with a Location
     * header pointing to the new resource.
     * </p>
     * <p>
     * <b>Conflict Detection:</b> If a player with the same squad number already exists, returns 409 Conflict.
     * Squad numbers must be unique (jersey numbers like Messi's #10).
     * </p>
     *
     * @param playerDTO the player data to create (validated with JSR-380 constraints)
     * @return 201 Created with Location header, 400 Bad Request if validation fails, or 409 Conflict if squad number exists
     */
    @PostMapping("/players")
    @Operation(summary = "Creates a new player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation failure", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - Squad number already exists", content = @Content)
    })
    public ResponseEntity<Void> post(@RequestBody @Valid PlayerDTO playerDTO) {
        PlayerDTO createdPlayer = playersService.create(playerDTO);
        if (createdPlayer == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlayer.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(LOCATION, location.toString())
                .build();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * HTTP GET
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Retrieves a single player by their unique identifier.
     *
     * @param id the unique identifier of the player
     * @return 200 OK with player data, or 404 Not Found if player doesn't exist
     */
    @GetMapping("/players/{id}")
    @Operation(summary = "Retrieves a player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<PlayerDTO> getById(@PathVariable Long id) {
        PlayerDTO playerDTO = playersService.retrieveById(id);
        return (playerDTO != null)
                ? ResponseEntity.status(HttpStatus.OK).body(playerDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Retrieves all players in the squad.
     * <p>
     * Returns the complete Argentina 2022 FIFA World Cup squad (26 players).
     * </p>
     *
     * @return 200 OK with array of all players (empty array if none found)
     */
    @GetMapping("/players")
    @Operation(summary = "Retrieves all players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTO[].class)))
    })
    public ResponseEntity<List<PlayerDTO>> getAll() {
        List<PlayerDTO> players = playersService.retrieveAll();
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    /**
     * Searches for players by league name (case-insensitive partial match).
     * <p>
     * Example: {@code /players/search/league/Premier} matches "Premier League"
     * </p>
     *
     * @param league the league name to search for
     * @return 200 OK with matching players (empty array if none found)
     */
    @GetMapping("/players/search/league/{league}")
    @Operation(summary = "Searches players by league name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Returns matching players (or empty array if none found)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTO[].class)))
    })
    public ResponseEntity<List<PlayerDTO>> searchByLeague(@PathVariable String league) {
        List<PlayerDTO> players = playersService.searchByLeague(league);
        return ResponseEntity.status(HttpStatus.OK).body(players);
    }

    /**
     * Searches for a player by their squad number.
     * <p>
     * Squad numbers are jersey numbers that users recognize (e.g., Messi is #10).
     * Example: {@code /players/search/squadnumber/10} returns Lionel Messi
     * </p>
     *
     * @param squadNumber the squad number to search for (jersey number, typically 1-99)
     * @return 200 OK with player data, or 404 Not Found if no player has that
     * number
     */
    @GetMapping("/players/search/squadnumber/{squadNumber}")
    @Operation(summary = "Searches for a player by squad number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<PlayerDTO> searchBySquadNumber(@PathVariable Integer squadNumber) {
        PlayerDTO player = playersService.searchBySquadNumber(squadNumber);
        return (player != null)
                ? ResponseEntity.status(HttpStatus.OK).body(player)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * HTTP PUT
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Updates an existing player resource (full update).
     * <p>
     * Performs a complete replacement of the player entity. The ID in the path must match the ID in the request body.
     * </p>
     *
     * @param id the unique identifier of the player to update
     * @param playerDTO the complete player data (must pass validation)
     * @return 204 No Content if successful, 404 Not Found if player doesn't exist, or 400 Bad Request if validation fails or
     * ID mismatch
     */
    @PutMapping("/players/{id}")
    @Operation(summary = "Updates (entirely) a player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> put(@PathVariable Long id, @RequestBody @Valid PlayerDTO playerDTO) {
        // Ensure path ID matches body ID
        if (playerDTO.getId() != null && !playerDTO.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        playerDTO.setId(id); // Set ID from path to ensure consistency
        boolean updated = playersService.update(playerDTO);
        return (updated)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*
     * -----------------------------------------------------------------------------------------------------------------------
     * HTTP DELETE
     * -----------------------------------------------------------------------------------------------------------------------
     */

    /**
     * Deletes a player resource by their unique identifier.
     *
     * @param id the unique identifier of the player to delete
     * @return 204 No Content if successful, or 404 Not Found if player doesn't exist
     */
    @DeleteMapping("/players/{id}")
    @Operation(summary = "Deletes a player by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = playersService.delete(id);
        return (deleted)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
