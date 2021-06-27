package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BooksBuilder;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@DataJpaTest
class BooksRepositoryTests {

    @Autowired
    private BooksRepository repository;

    @Test
    public void givenSave_whenBookIsInvalid_thenShouldThrowConstraintViolationException() {
        // Arrange
        Book expected = BooksBuilder.buildOneInvalid();
        // Act
        Throwable thrown = catchThrowable(() -> {
            repository.save(expected);
            repository.count(); // Throws the Exception
        });
        // Assert
        assertThat(thrown)
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("invalid ISBN")
            .hasMessageContaining("must be a past date");
    }

    @Test
    public void givenSave_whenBookIsValid_thenShouldSaveBookIntoRepository() {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        // Act
        Book actual = repository.save(expected);
        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(repository.count()).isEqualTo(1L);
    }

    @Test
    public void givenFindByIsbn_whenIsbnAlreadyExists_thenShouldReturnExistingBook() {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        repository.save(expected); // Exists
        // Act
        Optional<Book> actual = repository.findByIsbn(expected.getIsbn());
        // Assert
        assertThat(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void givenFindByIsbn_whenIsbnDoesNotExist_thenShouldReturnEmptyOptional() {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        // Act
        Optional<Book> actual = repository.findByIsbn(expected.getIsbn());
        // Assert
        assertThat(actual).isEmpty();
    }
}
