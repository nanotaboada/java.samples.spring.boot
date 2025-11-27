package ar.com.nanotaboada.java.samples.spring.boot.test.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

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
import ar.com.nanotaboada.java.samples.spring.boot.test.BookDTOFakes;
import ar.com.nanotaboada.java.samples.spring.boot.test.BookFakes;

@DisplayName("CRUD Operations on Service")
@ExtendWith(MockitoExtension.class)
class BooksServiceTests {

    @Mock
    private BooksRepository booksRepositoryMock;

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
    void givenCreate_whenRepositoryExistsByIdReturnsTrue_thenRepositoryNeverSaveBookAndResultIsFalse() {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        // Act
        boolean result = booksService.create(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        verify(modelMapperMock, never()).map(bookDTO, Book.class);
        assertThat(result).isFalse();
    }

    @Test
    void givenCreate_whenRepositoryExistsByIdReturnsFalse_thenRepositorySaveBookAndResultIsTrue() {
        // Arrange
        Book book = BookFakes.createOneValid();
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        Mockito
                .when(modelMapperMock.map(bookDTO, Book.class))
                .thenReturn(book);
        // Act
        boolean result = booksService.create(bookDTO);
        // Assert
        verify(booksRepositoryMock, times(1)).save(any(Book.class));
        verify(modelMapperMock, times(1)).map(bookDTO, Book.class);
        assertThat(result).isTrue();
    }

    /*
     * -------------------------------------------------------------------------
     * Retrieve
     * -------------------------------------------------------------------------
     */

    @Test
    void givenRetrieveByIsbn_whenRepositoryFindByIdReturnsBook_thenResultIsEqualToBook() {
        // Arrange
        Book book = BookFakes.createOneValid();
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        Mockito
                .when(booksRepositoryMock.findByIsbn(anyString()))
                .thenReturn(Optional.of(book));
        Mockito
                .when(modelMapperMock.map(book, BookDTO.class))
                .thenReturn(bookDTO);
        // Act
        BookDTO result = booksService.retrieveByIsbn(bookDTO.getIsbn());
        // Assert
        verify(booksRepositoryMock, times(1)).findByIsbn(anyString());
        verify(modelMapperMock, times(1)).map(book, BookDTO.class);
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTO);
    }

    @Test
    void givenRetrieveByIsbn_whenRepositoryFindByIdReturnsEmpty_thenResultIsNull() {
        // Arrange
        String isbn = "9781484242216";
        Mockito
                .when(booksRepositoryMock.findByIsbn(anyString()))
                .thenReturn(Optional.empty());
        // Act
        BookDTO result = booksService.retrieveByIsbn(isbn);
        // Assert
        verify(booksRepositoryMock, times(1)).findByIsbn(anyString());
        verify(modelMapperMock, never()).map(any(Book.class), any(BookDTO.class));
        assertThat(result).isNull();
    }

    @Test
    void givenRetrieveAll_whenRepositoryFindAllReturnsBooks_thenResultIsEqualToBooks() {
        // Arrange
        List<Book> books = BookFakes.createManyValid();
        List<BookDTO> bookDTOs = BookDTOFakes.createManyValid();
        Mockito
                .when(booksRepositoryMock.findAll())
                .thenReturn(books);
        for (int index = 0; index < books.size(); index++) {
            Mockito
                    .when(modelMapperMock.map(books.get(index), BookDTO.class))
                    .thenReturn(bookDTOs.get(index));
        }
        // Act
        List<BookDTO> result = booksService.retrieveAll();
        // Assert
        verify(booksRepositoryMock, times(1)).findAll();
        for (Book book : books) {
            verify(modelMapperMock, times(1)).map(book, BookDTO.class);
        }
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTOs);
    }

    /*
     * -------------------------------------------------------------------------
     * Update
     * -------------------------------------------------------------------------
     */

    @Test
    void givenUpdate_whenRepositoryExistsByIdReturnsTrue_thenRepositorySaveBookAndResultIsTrue() {
        // Arrange
        Book book = BookFakes.createOneValid();
        BookDTO bookDTO = BookDTOFakes.createOneValid();

        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        Mockito
                .when(modelMapperMock.map(bookDTO, Book.class))
                .thenReturn(book);
        // Act
        boolean result = booksService.update(bookDTO);
        // Assert
        verify(booksRepositoryMock, times(1)).save(any(Book.class));
        verify(modelMapperMock, times(1)).map(bookDTO, Book.class);
        assertThat(result).isTrue();
    }

    @Test
    void givenUpdate_whenRepositoryExistsByIdReturnsFalse_thenRepositoryNeverSaveBookAndResultIsFalse() {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        // Act
        boolean result = booksService.update(bookDTO);
        // Assert
        verify(booksRepositoryMock, never()).save(any(Book.class));
        verify(modelMapperMock, never()).map(bookDTO, Book.class);
        assertThat(result).isFalse();
    }

    /*
     * -------------------------------------------------------------------------
     * Delete
     * -------------------------------------------------------------------------
     */

    @Test
    void givenDelete_whenRepositoryExistsByIdReturnsFalse_thenRepositoryNeverDeleteBookAndResultIsFalse() {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneInvalid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(false);
        // Act
        boolean result = booksService.delete(bookDTO.getIsbn());
        // Assert
        verify(booksRepositoryMock, never()).deleteById(anyString());
        verify(modelMapperMock, never()).map(bookDTO, Book.class);
        assertThat(result).isFalse();
    }

    @Test
    void givenDelete_whenRepositoryExistsByIdReturnsTrue_thenRepositoryDeleteBookAndResultIsTrue() {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        Mockito
                .when(booksRepositoryMock.existsById(anyString()))
                .thenReturn(true);
        // Act
        boolean result = booksService.delete(bookDTO.getIsbn());
        // Assert
        verify(booksRepositoryMock, times(1)).deleteById(anyString());
        verify(modelMapperMock, never()).map(bookDTO, Book.class);
        assertThat(result).isTrue();
    }

    /*
     * -------------------------------------------------------------------------
     * Search
     * -------------------------------------------------------------------------
     */

    @Test
    void givenSearchByDescription_whenRepositoryReturnsMatchingBooks_thenResultIsEqualToBooks() {
        // Arrange
        List<Book> books = BookFakes.createManyValid();
        List<BookDTO> bookDTOs = BookDTOFakes.createManyValid();
        String keyword = "Java";
        Mockito
                .when(booksRepositoryMock.findByDescriptionContainingIgnoreCase(keyword))
                .thenReturn(books);
        for (int index = 0; index < books.size(); index++) {
            Mockito
                    .when(modelMapperMock.map(books.get(index), BookDTO.class))
                    .thenReturn(bookDTOs.get(index));
        }
        // Act
        List<BookDTO> result = booksService.searchByDescription(keyword);
        // Assert
        verify(booksRepositoryMock, times(1)).findByDescriptionContainingIgnoreCase(keyword);
        for (Book book : books) {
            verify(modelMapperMock, times(1)).map(book, BookDTO.class);
        }
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTOs);
    }

    @Test
    void givenSearchByDescription_whenRepositoryReturnsEmptyList_thenResultIsEmptyList() {
        // Arrange
        String keyword = "nonexistentkeyword";
        Mockito
                .when(booksRepositoryMock.findByDescriptionContainingIgnoreCase(keyword))
                .thenReturn(List.of());
        // Act
        List<BookDTO> result = booksService.searchByDescription(keyword);
        // Assert
        verify(booksRepositoryMock, times(1)).findByDescriptionContainingIgnoreCase(keyword);
        assertThat(result).isEmpty();
    }
}
