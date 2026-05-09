package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * Data Transfer Object for partial updates to a {@link Player} resource (HTTP PATCH).
 * <p>
 * All fields are nullable. Only non-null fields are applied to the existing entity;
 * absent fields (null) are left unchanged — following RFC 7396 (JSON Merge Patch) semantics.
 * </p>
 *
 * <h3>Immutable Fields:</h3>
 * <ul>
 * <li>{@code id} — surrogate UUID key, must not be present in PATCH requests</li>
 * <li>{@code squadNumber} — natural key, must not be present in PATCH requests</li>
 * </ul>
 * <p>
 * If either immutable field is present in the request body, the controller returns
 * {@code 400 Bad Request}.
 * </p>
 *
 * @see Player
 * @see PlayerDTO
 * @since 4.0.2025
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerPatchDTO {

    private UUID id;
    private Integer squadNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String position;
    private String abbrPosition;
    private String team;
    private String league;
    private Boolean starting11;
}