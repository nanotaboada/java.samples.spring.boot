package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String isbn;
    private String title;
    private String subtitle;
    private String author;
    private String publisher;
    /**
     * Stored as Unix timestamp (INTEGER) in SQLite for robustness.
     * The converter handles LocalDate ↔ epoch seconds conversion.
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Convert(converter = UnixTimestampConverter.class)
    private LocalDate published;
    private int pages;
    /**
     * Maximum length set to 8192 (8^4 = 2^13) characters.
     * This power-of-two value provides ample space for book descriptions
     * while remaining compatible with both H2 (VARCHAR) and SQLite (TEXT).
     */
    @Column(length = 8192)
    private String description;
    private String website;
}
