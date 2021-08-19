package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;
import ar.com.nanotaboada.java.samples.spring.boot.test.BooksBuilder;

@DisplayName("Derived Query Methods on Repository")
@DataJpaTest
class BooksRepositoryTests {

    @Autowired
    private BooksRepository repository;

    @Test
    void givenFindByIsbn_whenIsbnAlreadyExists_thenShouldReturnExistingBook() {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        repository.save(expected); // Exists
        // Act
        Optional<Book> actual = repository.findByIsbn(expected.getIsbn());
        // Assert
        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void givenFindByIsbn_whenIsbnDoesNotExist_thenShouldReturnEmptyOptional() {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        // Act
        Optional<Book> actual = repository.findByIsbn(expected.getIsbn());
        // Assert
        assertThat(actual).isEmpty();
    }
}
