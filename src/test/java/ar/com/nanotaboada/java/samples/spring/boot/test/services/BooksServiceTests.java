package ar.com.nanotaboada.java.samples.spring.boot.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
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
class BooksServiceTests {

    @Mock
    private BooksRepository booksRepositoryMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private ModelMapper modelMapperMock;

    @InjectMocks
    private BooksService booksService;

    /*
     * -------------------------------------------------------------------------
     * Create
     * -------------------------------------------------------------------------
     */

    @Test
    void givenCreate_whenBookExistsInRepositoryAndIsValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(new HashSet<ConstraintViolation<BookDTO>>());
        // Act
        result = booksService.create(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    @Test
    void givenCreate_whenBookExistsInRepositoryAndIsNotValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<BookDTO>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(constraintViolations);
        // Act
        result = booksService.create(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    @Test
    void givenCreate_whenBookdDoesNotExistsInRepositoryAndIsValid_thenShouldSaveBookIntoRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(new HashSet<ConstraintViolation<BookDTO>>());
        Mockito
                .when(modelMapperMock.map(bookDTO, Book.class))
                .thenReturn(book);
        // Act
        result = booksService.create(bookDTO);
        // Assert
        verify(modelMapperMock, times(1)).map(bookDTO, Book.class);
        verify(booksRepositoryMock, times(1)).save(any(Book.class));
        assertThat(result).isTrue();
    }

    @Test
    void givenCreate_whenBookDoesNotExistsInRepositoryAndIsNotValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<BookDTO>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(constraintViolations);
        // Act
        result = booksService.create(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    /*
     * -------------------------------------------------------------------------
     * Retrieve
     * -------------------------------------------------------------------------
     */

    @Test
    void givenRetrieveByIsbn_whenIsbnIsFoundInRepository_thenShouldReturnBook() {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.findByIsbn(anyString()))
                .thenReturn(Optional.of(book));
        Mockito
                .when(modelMapperMock.map(book, BookDTO.class))
                .thenReturn(bookDTO);
        // Act
        BookDTO result = booksService.retrieveByIsbn(bookDTO.getIsbn());
        // Assert
        verify(modelMapperMock, times(1)).map(book, BookDTO.class);
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTO);
        verify(booksRepositoryMock, times(1)).findByIsbn(anyString());
    }

    @Test
    void givenRetrieveByIsbn_whenIsbnIsNotFoundInRepository_thenShouldReturnNull() {
        // Arrange
        String isbn = "9781484242216";
        Mockito
                .when(booksRepositoryMock.findByIsbn(anyString()))
                .thenReturn(Optional.empty());
        // Act
        BookDTO result = booksService.retrieveByIsbn(isbn);
        // Assert
        assertThat(result).isNull();
        verify(modelMapperMock, never()).map(any(Book.class), any(BookDTO.class));
        verify(booksRepositoryMock, times(1)).findByIsbn(anyString());
    }

    @Test
    void givenRetrieveAll_whenRepositoryHasCollectionOfBooks_thenShouldReturnAllBooks() {
        // Arrange
        List<BookDTO> expected = BookDTOsBuilder.buildManyValid();
        List<Book> existing = BooksBuilder.buildManyValid();
        Mockito
                .when(booksRepositoryMock.findAll())
                .thenReturn(existing);
        for (int index = 0; index < existing.size(); index++) {
            Mockito
                    .when(modelMapperMock.map(existing.get(index), BookDTO.class))
                    .thenReturn(expected.get(index));
        }
        // Act
        List<BookDTO> actual = booksService.retrieveAll();
        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        for (Book book : existing) {
            verify(modelMapperMock, times(1)).map(book, BookDTO.class);
        }
        verify(booksRepositoryMock, times(1)).findAll();
    }

    /*
     * -------------------------------------------------------------------------
     * Update
     * -------------------------------------------------------------------------
     */

    @Test
    void givenUpdate_whenBookExistsInRepositoryAndIsValid_thenShouldSaveBookIntoRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Book book = BooksBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(new HashSet<ConstraintViolation<BookDTO>>());
        Mockito
                .when(modelMapperMock.map(bookDTO, Book.class))
                .thenReturn(book);
        // Act
        result = booksService.update(bookDTO);
        // Assert
        verify(modelMapperMock, times(1)).map(bookDTO, Book.class);
        verify(booksRepositoryMock, times(1)).save(any(Book.class));
        assertThat(result).isTrue();
    }

    @Test
    void givenUodate_whenBookExistsInRepositoryAndIsNotValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<BookDTO>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(constraintViolations);
        // Act
        result = booksService.update(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    @Test
    void givenUpdate_whenBookDoesNotExistsInRepositoryAndIsValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(new HashSet<ConstraintViolation<BookDTO>>());
        // Act
        result = booksService.update(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    @Test
    void givenUodate_whenBookDoesNotExistsInRepositoryAndIsNotValid_thenShouldNeverSaveBookIntoRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<BookDTO>> constraintViolations = Set.of(mock(ConstraintViolation.class));
        Mockito
                .when(validatorMock.validate(any(BookDTO.class)))
                .thenReturn(constraintViolations);
        // Act
        result = booksService.update(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        assertThat(result).isFalse();
    }

    /*
     * -------------------------------------------------------------------------
     * Delete
     * -------------------------------------------------------------------------
     */

    @Test
    void givenDelete_whenIsbnIsBlank_thenShouldNeverDeleteBookFromRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        Book book = BooksBuilder.buildOneValid();
        book.setIsbn("");
        // Act
        result = booksService.delete(book.getIsbn());
        // Assert
        verify(booksRepositoryMock, never()).deleteById(anyString());
        assertThat(result).isFalse();
    }

    @Test
    void givenDelete_whenIsbnIsNotBlankButDoesNotExistInRepository_thenShouldNeverDeleteBookFromRepositoryAndReturnFalse() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        // Act
        result = booksService.delete(bookDTO.getIsbn());
        // Assert
        verify(booksRepositoryMock, never()).deleteById(anyString());
        assertThat(result).isFalse();
    }

    @Test
    void givenDelete_whenIsbnIsNotBlankAndExistInRepository_thenShouldDeleteBookFromRepositoryAndReturnTrue() {
        // Arrange
        boolean result = false;
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        // Act
        result = booksService.delete(bookDTO.getIsbn());
        // Assert
        verify(booksRepositoryMock, times(1)).deleteById(anyString());
        assertThat(result).isTrue();
    }
}
