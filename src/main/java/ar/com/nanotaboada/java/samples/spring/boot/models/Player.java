package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import ar.com.nanotaboada.java.samples.spring.boot.converters.IsoDateConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JPA Entity representing a player in the Argentina 2022 FIFA World Cup squad.
 * <p>
 * This entity maps to the {@code players} table in the database and contains
 * biographical and team information for each player.
 * </p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 * <li>UUID primary key — generated at application level via {@code GenerationType.UUID}</li>
 * <li>Squad number natural key — unique domain identifier, used as path variable for mutations</li>
 * <li>ISO-8601 date storage for SQLite compatibility ({@link IsoDateConverter})</li>
 * <li>JSON serialization support for LocalDate fields</li>
 * </ul>
 *
 * @see PlayerDTO
 * @see IsoDateConverter
 * @since 4.0.2025
 */
@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    /**
     * Primary key — UUID generated at application level.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    /**
     * Natural key — unique domain identifier, path variable for PUT and DELETE.
     * Squad number (jersey number) is unique per team and stable.
     */
    @Column(name = "squadNumber", nullable = false, unique = true, updatable = false)
    private Integer squadNumber;

    private String firstName;
    private String middleName;
    private String lastName;
    /**
     * Stored as ISO-8601 TEXT in SQLite (e.g., "1992-09-02T00:00:00.000Z").
     * The converter handles LocalDate ↔ ISO string conversion.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Convert(converter = IsoDateConverter.class)
    private LocalDate dateOfBirth;

    private String position;
    private String abbrPosition;
    private String team;
    private String league;
    private Boolean starting11;
}
