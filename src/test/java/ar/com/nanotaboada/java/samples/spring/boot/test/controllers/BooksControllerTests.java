package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.BooksController;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;
import ar.com.nanotaboada.java.samples.spring.boot.test.BookDTOFakes;

@DisplayName("HTTP Methods on Controller")
@WebMvcTest(BooksController.class)
@AutoConfigureCache
class BooksControllerTests {

    private static final String PATH = "/books";

    @Autowired
    private MockMvc application;

    @MockitoBean
    private BooksService booksServiceMock;

    @MockitoBean
    private BooksRepository booksRepositoryMock;

    /*
     * -------------------------------------------------------------------------
     * HTTP POST
     * -------------------------------------------------------------------------
     */

    @Test
    void givenPost_whenRequestBodyIsValidButExistingBook_thenResponseStatusIsConflict()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
                .when(booksServiceMock.create(any(BookDTO.class)))
                .thenReturn(false); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).create(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

    }

    @Test
    void givenPost_whenRequestBodyIsValidAndNonExistentBook_thenResponseStatusIsCreated()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
                .when(booksServiceMock.create(any(BookDTO.class)))
                .thenReturn(true); // Non-existent
        Mockito
                .when(booksServiceMock.create(any(BookDTO.class)))
                .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        // Assert
        verify(booksServiceMock, times(1)).create(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull();
        assertThat(response.getHeader(HttpHeaders.LOCATION)).contains(PATH + "/" + bookDTO.getIsbn());

    }

    @Test
    void givenPost_whenRequestBodyIsInvalidBook_thenResponseStatusIsBadRequest()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneInvalid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, never()).create(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP GET
     * -------------------------------------------------------------------------
     */

    @Test
    void givenGetByIsbn_whenRequestPathVariableIsValidAndExistingISBN_thenResponseStatusIsOKAndResultIsBook()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String isbn = bookDTO.getIsbn();
        Mockito
                .when(booksServiceMock.retrieveByIsbn(anyString()))
                .thenReturn(bookDTO); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        BookDTO result = new ObjectMapper().readValue(content, BookDTO.class);
        // Assert
        verify(booksServiceMock, times(1)).retrieveByIsbn(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTO);
    }

    @Test
    void givenGetByIsbn_whenRequestPathVariableIsValidButNonExistentISBN_thenResponseStatusIsNotFound()
            throws Exception {
        // Arrange
        String isbn = "9781484242216";
        Mockito
                .when(booksServiceMock.retrieveByIsbn(anyString()))
                .thenReturn(null); // Non-existent
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).retrieveByIsbn(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void givenGetAll_whenRequestPathIsBooks_thenResponseIsOkAndResultIsBooks()
            throws Exception {
        // Arrange
        List<BookDTO> bookDTOs = BookDTOFakes.createManyValid();
        Mockito
                .when(booksServiceMock.retrieveAll())
                .thenReturn(bookDTOs);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        List<BookDTO> result = new ObjectMapper().readValue(content, new TypeReference<List<BookDTO>>() {
        });
        // Assert
        verify(booksServiceMock, times(1)).retrieveAll();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTOs);
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP PUT
     * -------------------------------------------------------------------------
     */

    @Test
    void givenPut_whenRequestBodyIsValidAndExistingBook_thenResponseStatusIsNoContent()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
                .when(booksServiceMock.update(any(BookDTO.class)))
                .thenReturn(true); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).update(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenPut_whenRequestBodyIsValidButNonExistentBook_thenResponseStatusIsNotFound()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
                .when(booksServiceMock.update(any(BookDTO.class)))
                .thenReturn(false); // Non-existent
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).update(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void givenPut_whenRequestBodyIsInvalidBook_thenResponseStatusIsBadRequest()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneInvalid();
        String body = new ObjectMapper().writeValueAsString(bookDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, never()).update(any(BookDTO.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP DELETE
     * -------------------------------------------------------------------------
     */

    @Test
    void givenDelete_whenPathVariableIsValidAndExistingISBN_thenResponseStatusIsNoContent()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String isbn = bookDTO.getIsbn();
        Mockito
                .when(booksServiceMock.delete(anyString()))
                .thenReturn(true); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).delete(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void givenDelete_whenPathVariableIsValidButNonExistentISBN_thenResponseStatusIsNotFound()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneValid();
        String isbn = bookDTO.getIsbn();
        Mockito
                .when(booksServiceMock.delete(anyString()))
                .thenReturn(false); // Non-existent
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, times(1)).delete(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void givenDelete_whenPathVariableIsInvalidISBN_thenResponseStatusIsBadRequest()
            throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOFakes.createOneInvalid();
        String isbn = bookDTO.getIsbn();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PATH + "/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, never()).delete(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP GET /books/search
     * -------------------------------------------------------------------------
     */

    @Test
    void givenSearchByDescription_whenRequestParamIsValidAndMatchingBooksExist_thenResponseStatusIsOKAndResultIsBooks()
            throws Exception {
        // Arrange
        List<BookDTO> bookDTOs = BookDTOFakes.createManyValid();
        String keyword = "Java";
        Mockito
                .when(booksServiceMock.searchByDescription(anyString()))
                .thenReturn(bookDTOs);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search")
                .param("description", keyword);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        List<BookDTO> result = new ObjectMapper().readValue(content, new TypeReference<List<BookDTO>>() {
        });
        // Assert
        verify(booksServiceMock, times(1)).searchByDescription(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).usingRecursiveComparison().isEqualTo(bookDTOs);
    }

    @Test
    void givenSearchByDescription_whenRequestParamIsValidAndNoMatchingBooks_thenResponseStatusIsOKAndResultIsEmptyList()
            throws Exception {
        // Arrange
        String keyword = "nonexistentkeyword";
        Mockito
                .when(booksServiceMock.searchByDescription(anyString()))
                .thenReturn(List.of());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search")
                .param("description", keyword);
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        List<BookDTO> result = new ObjectMapper().readValue(content, new TypeReference<List<BookDTO>>() {
        });
        // Assert
        verify(booksServiceMock, times(1)).searchByDescription(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).isEmpty();
    }

    @Test
    void givenSearchByDescription_whenRequestParamIsBlank_thenResponseStatusIsBadRequest()
            throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search")
                .param("description", "");
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, never()).searchByDescription(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void givenSearchByDescription_whenRequestParamIsMissing_thenResponseStatusIsBadRequest()
            throws Exception {
        // Arrange
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PATH + "/search");
        // Act
        MockHttpServletResponse response = application
                .perform(request)
                .andReturn()
                .getResponse();
        // Assert
        verify(booksServiceMock, never()).searchByDescription(anyString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
