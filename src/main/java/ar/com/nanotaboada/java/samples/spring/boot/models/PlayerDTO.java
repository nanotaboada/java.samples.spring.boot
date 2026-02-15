package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Player entities.
 * <p>
 * This DTO is used to transfer player data between the API layer and the
 * service layer,
 * providing a decoupled representation of the {@link Player} entity with
 * validation rules.
 * </p>
 *
 * <h3>Validation Constraints:</h3>
 * <ul>
 * <li><b>firstName:</b> Required (not blank)</li>
 * <li><b>lastName:</b> Required (not blank)</li>
 * <li><b>dateOfBirth:</b> Must be a past date</li>
 * <li><b>squadNumber:</b> Must be a positive integer</li>
 * <li><b>position:</b> Required (not blank)</li>
 * <li><b>team:</b> Required (not blank)</li>
 * </ul>
 *
 * <h3>JSON Serialization:</h3>
 * <p>
 * Uses Jackson to serialize/deserialize LocalDate fields to ISO-8601 format.
 * </p>
 *
 * @see Player
 * @see jakarta.validation.constraints.NotBlank
 * @see jakarta.validation.constraints.Past
 * @see jakarta.validation.constraints.Positive
 * @since 4.0.2025
 */
@Data
public class PlayerDTO {
    private Long id;
    @NotBlank
    private String firstName;
    private String middleName;
    @NotBlank
    private String lastName;
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    @Positive
    private Integer squadNumber;
    @NotBlank
    private String position;
    private String abbrPosition;
    @NotBlank
    private String team;
    private String league;
    private Boolean starting11;
}
