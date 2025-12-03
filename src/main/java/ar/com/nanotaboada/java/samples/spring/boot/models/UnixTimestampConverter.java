package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA AttributeConverter that converts between LocalDate and Unix timestamp
 * (seconds since epoch).
 *
 * This converter stores dates as INTEGER in SQLite, which is more robust than
 * TEXT-based date formats because:
 * - No parsing ambiguity or locale-dependent formatting issues
 * - Works consistently across all SQLite clients and tools
 * - More efficient for date comparisons and indexing
 * - Avoids the need for date_string_format in the JDBC connection URL
 */
@Converter
public class UnixTimestampConverter implements AttributeConverter<LocalDate, Long> {

    @Override
    public Long convertToDatabaseColumn(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
    }

    @Override
    public LocalDate convertToEntityAttribute(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC).toLocalDate();
    }
}
