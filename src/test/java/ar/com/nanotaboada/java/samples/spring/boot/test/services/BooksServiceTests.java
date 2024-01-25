package ar.com.nanotaboada.java.samples.spring.boot.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;
import ar.com.nanotaboada.java.samples.spring.boot.test.BookDTOsBuilder;
import ar.com.nanotaboada.java.samples.spring.boot.test.BooksBuilder;

@DisplayName("CRUD Operations on Service")
@ExtendWith(MockitoExtension.class)
public class BooksServiceTests {

    @Mock
    private BooksRepository repository;

    @Mock
    private Validator validator;
    
    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private BooksService service;

    /* --------------------------------------------------------------------------------------------
     * Create
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenCreate_whenBookIsInvalid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Book book = BooksBuilder.buildOneInvalid();
        // Simplify creation of ConstraintViolationExceptions
        // https://hibernate.atlassian.net/browse/BVAL-198
        Set<? extends ConstraintViolation<?>> errors = new HashSet<ConstraintViolation<Book>>();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenThrow(new ConstraintViolationException(errors));
        // Act
        try {
            result = service.create(bookDTO);
        } catch (Exception exception) {
        // Assert
            assertThat(exception).isInstanceOf(ConstraintViolationException.class);
        } finally {
            verify(mapper, times(1)).map(bookDTO, Book.class);
            verify(repository, never()).save(any(Book.class));
            assertThat(result).isFalse();
        }
    }

    @Test
    void givenCreate_whenBookIsValidButAlreadyExistsInRepository_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Set<ConstraintViolation<Book>> errors = new HashSet<ConstraintViolation<Book>>(); // isEmpty();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenReturn(errors);
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(true);
        // Act
        result = service.create(bookDTO);
        // Assert
        verify(mapper, times(1)).map(bookDTO, Book.class);
        verify(repository, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    @Test
    void givenCreate_whenBookIsValidAndDoesNotExistInRepository_thenShouldSaveBookIntoRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Set<ConstraintViolation<Book>> errors = new HashSet<ConstraintViolation<Book>>(); // isEmpty();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenReturn(errors);
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(false);
        // Act
        result = service.create(bookDTO);
        // Assert
        verify(mapper, times(1)).map(bookDTO, Book.class);
        verify(repository, times(1)).save(any(Book.class));
        assertThat(result).isTrue();
    }

    /* --------------------------------------------------------------------------------------------
     * Retrieve
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenRetrieveByIsbn_whenIsbnIsFoundInRepository_thenShouldReturnBook() {
        // Arrange
        BookDTO expected = BookDTOsBuilder.buildOneValid();
        Book existing = BooksBuilder.buildOneValid();
        Mockito
            .when(repository.findByIsbn(anyString()))
            .thenReturn(Optional.of(existing));
        Mockito
            .when(mapper.map(existing, BookDTO.class))
            .thenReturn(expected);
        // Act
        BookDTO actual = service.retrieveByIsbn(expected.getIsbn());
        // Assert
        verify(mapper, times(1)).map(existing, BookDTO.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(repository, times(1)).findByIsbn(anyString());
    }

    @Test
    void givenRetrieveByIsbn_whenIsbnIsNotFoundInRepository_thenShouldReturnNull() {
        // Arrange
        BookDTO expected = BookDTOsBuilder.buildOneValid();
        Mockito
            .when(repository.findByIsbn(anyString()))
            .thenReturn(Optional.empty());
        // Act
        BookDTO actual = service.retrieveByIsbn(expected.getIsbn());
        // Assert
        assertThat(actual).isNull();
        verify(mapper, never()).map(any(Book.class), any(BookDTO.class));
        verify(repository, times(1)).findByIsbn(anyString());
    }

    /* --------------------------------------------------------------------------------------------
     * Update
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenUpdate_whenBookIsInvalid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Book book = BooksBuilder.buildOneInvalid();
        // Simplify creation of ConstraintViolationExceptions
        // https://hibernate.atlassian.net/browse/BVAL-198
        Set<? extends ConstraintViolation<?>> errors = new HashSet<ConstraintViolation<Book>>();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenThrow(new ConstraintViolationException(errors));
        // Act
        try {
            result = service.update(bookDTO);
        } catch (Exception exception) {
        // Assert
            assertThat(exception).isInstanceOf(ConstraintViolationException.class);
        } finally {
            verify(mapper, times(1)).map(bookDTO, Book.class);
            verify(repository, never()).save(any(Book.class));
            assertThat(result).isFalse();
        }
    }

    @Test
    void givenUpdate_whenBookIsValidAndExistInRepository_thenShouldSaveBookIntoRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Set<ConstraintViolation<Book>> errors = new HashSet<ConstraintViolation<Book>>(); // isEmpty();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenReturn(errors);
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(true);
        // Act
        result = service.update(bookDTO);
        // Assert
        verify(mapper, times(1)).map(bookDTO, Book.class);
        verify(repository, times(1)).save(any(Book.class));
        assertThat(result).isTrue();
    }

    @Test
    void givenUpdate_whenBookIsValidButDoesNotExistInRepository_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Set<ConstraintViolation<Book>> errors = new HashSet<ConstraintViolation<Book>>(); // isEmpty();
        Mockito
            .when(mapper.map(bookDTO, Book.class))
            .thenReturn(book);
        Mockito
            .when(validator.validate(any(Book.class)))
            .thenReturn(errors);
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(false);
        // Act
        result = service.update(bookDTO);
        // Assert
        verify(mapper, times(1)).map(bookDTO, Book.class);
        verify(repository, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    /* --------------------------------------------------------------------------------------------
     * Delete
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenDelete_whenIsbnIsBlank_thenShouldNeverDeleteBookFromRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        Book book = BooksBuilder.buildOneValid();
        book.setIsbn("");
        // Act
        result = service.delete(book.getIsbn());
        // Assert
        verify(repository, never()).deleteById(anyString());
        assertThat(result).isFalse();
    }

    @Test
    void givenDelete_whenIsbnIsNotBlankButDoesNotExistInRepository_thenShouldNeverDeleteBookFromRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(false);
        // Act
        result = service.delete(bookDTO.getIsbn());
        // Assert
        verify(repository, never()).deleteById(anyString());
        assertThat(result).isFalse();
    }

    @Test
    void givenDelete_whenIsbnIsNotBlankAndExistInRepository_thenShouldDeleteBookFromRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
            .when(repository.existsById(anyString()))
            .thenReturn(true);
        // Act
        result = service.delete(bookDTO.getIsbn());
        // Assert
        verify(repository, times(1)).deleteById(anyString());
        assertThat(result).isTrue();
    }
}
