package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;

@Data
public class BookDTO {
    @ISBN
    @NotBlank
    private String isbn;
    @NotBlank
    private String title;
    private String subtitle;
    @NotBlank
    private String author;
    private String publisher;
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate published;
    private int pages;
    @NotBlank
    private String description;
    @URL
    private String website;
}
