package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;
import ar.com.nanotaboada.java.samples.spring.boot.test.BookFakes;

@DisplayName("Derived Query Methods on Repository")
@DataJpaTest
@AutoConfigureCache
class BooksRepositoryTests {

    @Autowired
    private BooksRepository repository;

    @Test
    void givenFindByIsbn_whenISBNAlreadyExists_thenShouldReturnExistingBook() {
        // Arrange
        Book existing = BookFakes.createOneValid();
        repository.save(existing); // Exists
        // Act
        Optional<Book> actual = repository.findByIsbn(existing.getIsbn());
        // Assert
        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(existing);
    }

    @Test
    void givenFindByIsbn_whenISBNDoesNotExist_thenShouldReturnEmptyOptional() {
        // Arrange
        Book expected = BookFakes.createOneValid();
        // Act
        Optional<Book> actual = repository.findByIsbn(expected.getIsbn());
        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void givenFindByDescriptionContainingIgnoreCase_whenKeywordMatchesDescription_thenShouldReturnMatchingBooks() {
        // Arrange
        List<Book> books = BookFakes.createManyValid();
        for (Book book : books) {
            repository.save(book);
        }
        // Act
        List<Book> actual = repository.findByDescriptionContainingIgnoreCase("Java");
        // Assert
        assertThat(actual).isNotEmpty();
        assertThat(actual).allMatch(book -> book.getDescription().toLowerCase().contains("java"));
    }

    @Test
    void givenFindByDescriptionContainingIgnoreCase_whenKeywordDoesNotMatch_thenShouldReturnEmptyList() {
        // Arrange
        Book book = BookFakes.createOneValid();
        repository.save(book);
        // Act
        List<Book> actual = repository.findByDescriptionContainingIgnoreCase("nonexistentkeyword");
        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void givenFindByDescriptionContainingIgnoreCase_whenKeywordIsDifferentCase_thenShouldStillMatch() {
        // Arrange
        Book book = BookFakes.createOneValid();
        book.setDescription("This book covers Advanced PRAGMATISM topics");
        repository.save(book);
        // Act
        List<Book> actual = repository.findByDescriptionContainingIgnoreCase("pragmatism");
        // Assert
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getIsbn()).isEqualTo(book.getIsbn());
    }
}
