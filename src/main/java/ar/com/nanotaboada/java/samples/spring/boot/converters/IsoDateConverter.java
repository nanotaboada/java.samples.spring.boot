package ar.com.nanotaboada.java.samples.spring.boot.converters;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA AttributeConverter for converting between {@link LocalDate} and ISO-8601
 * date strings.
 * <p>
 * This converter handles dates stored as TEXT in SQLite databases using the
 * ISO-8601 format
 * (e.g., "1992-09-02T00:00:00.000Z"). It ensures seamless conversion between
 * Java's LocalDate
 * and SQLite's TEXT-based date storage.
 * </p>
 *
 * <h3>Database Column Conversion:</h3>
 * <ul>
 * <li><b>To Database:</b> LocalDate → "YYYY-MM-DDTHH:mm:ss.SSSZ" (ISO-8601 with
 * time component)</li>
 * <li><b>From Database:</b> "YYYY-MM-DDTHH:mm:ss.SSSZ" or "YYYY-MM-DD" →
 * LocalDate</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>
 * {
 *     &#64;code
 *     &#64;Entity
 *     public class Player {
 *         @Convert(converter = IsoDateConverter.class)
 *         private LocalDate dateOfBirth;
 *     }
 * }
 * </pre>
 *
 * @see jakarta.persistence.AttributeConverter
 * @see java.time.LocalDate
 * @since 4.0.2025
 */
@Converter
public class IsoDateConverter implements AttributeConverter<LocalDate, String> {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * Converts a {@link LocalDate} to an ISO-8601 formatted string for database
     * storage.
     *
     * @param date the LocalDate to convert (may be null)
     * @return ISO-8601 formatted string (e.g., "1992-09-02T00:00:00Z"), or null if
     * input is null
     */
    @Override
    public String convertToDatabaseColumn(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay().atOffset(ZoneOffset.UTC).format(ISO_FORMATTER);
    }

    /**
     * Converts an ISO-8601 formatted string from the database to a
     * {@link LocalDate}.
     * <p>
     * Handles both full ISO-8601 format ("YYYY-MM-DDTHH:mm:ss.SSSZ") and simple
     * date format ("YYYY-MM-DD").
     * </p>
     *
     * @param dateString the ISO-8601 formatted string from the database (may be
     * null or blank)
     * @return the corresponding LocalDate, or null if input is null or blank
     */
    @Override
    public LocalDate convertToEntityAttribute(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        // Handle both "YYYY-MM-DD" and "YYYY-MM-DDTHH:mm:ss.SSSZ" formats
        if (dateString.contains("T")) {
            return OffsetDateTime.parse(dateString, ISO_FORMATTER).toLocalDate();
        }
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
    }
}
