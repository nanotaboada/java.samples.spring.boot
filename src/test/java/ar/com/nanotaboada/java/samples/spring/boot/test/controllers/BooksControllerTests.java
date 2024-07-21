package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.BooksController;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;
import ar.com.nanotaboada.java.samples.spring.boot.test.BookDTOsBuilder;

@DisplayName("HTTP Verbs on Controller")
@WebMvcTest(BooksController.class)
class BooksControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksService service;

    @MockBean
    private BooksRepository repository;

    /* --------------------------------------------------------------------------------------------
     * HTTP POST
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenHttpPostVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusConflict()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
    }

    @Test
    void givenHttpPostVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusCreatedAndLocationHeader()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        Mockito
            .when(service.create(any(BookDTO.class)))
            .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull();
        assertThat(response.getHeader(HttpHeaders.LOCATION)).contains("/book/" + bookDTO.getIsbn());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).create(any(BookDTO.class));
    }

    @Test
    void givenHttpPostVerb_whenRequestBodyContainsInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        Mockito
            .when(service.create(any(BookDTO.class)))
            .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).create(any(BookDTO.class));
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP GET
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenHttpGetVerb_whenRequestParameterIdentifiesExistingBook_thenShouldReturnStatusOkAndTheBook()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/book/{isbn}", bookDTO.getIsbn());
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        BookDTO actual = new ObjectMapper().readValue(content, BookDTO.class);
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison().isEqualTo(bookDTO);
        verify(service, times(1)).retrieveByIsbn(anyString());
    }

    @Test
    void givenHttpGetVerb_whenRequestParameterDoesNotIdentifyAnExistingBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        String isbn = "9781484242216";
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/book/{isbn}", isbn);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
    }

    @Test
    void givenHttpGetVerb_whenRequestPathIsBooks_thenShouldReturnStatusOkAndCollectionOfBooks()
        throws Exception {
        // Arrange
        List<BookDTO> expected = BookDTOsBuilder.buildManyValid();
        Mockito
            .when(service.retrieveAll())
            .thenReturn(expected);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/books");
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        response.setContentType("application/json;charset=UTF-8");
        String content = response.getContentAsString();
        List<BookDTO> actual = new ObjectMapper().readValue(content, new TypeReference<List<BookDTO>>() {});
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(service, times(1)).retrieveAll();
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP PUT
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenHttpPutVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusNoContent()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        Mockito
            .when(service.update(any(BookDTO.class)))
            .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).update(any(BookDTO.class));
    }

    @Test
    void givenHttpPutVerb_whenRequestBodyContainsExistingInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        Mockito
            .when(service.update(any(BookDTO.class)))
            .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).update(any(BookDTO.class));
    }

    @Test
    void givenHttpPutVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        String content = new ObjectMapper().writeValueAsString(bookDTO);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/book")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON);
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP DELETE
     * ----------------------------------------------------------------------------------------- */

    @Test
    void givenHttpDeleteVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusNoContent()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        Mockito
            .when(service.delete(anyString()))
            .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", bookDTO.getIsbn());
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).delete(anyString());
    }

    @Test
    void givenHttpDeleteVerb_whenRequestBodyContainsExistingInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneInvalid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(bookDTO); // Existing
        Mockito
            .when(service.delete(anyString()))
            .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", bookDTO.getIsbn());
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).delete(anyString());
    }

    @Test
    void givenHttpDeleteVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        BookDTO bookDTO = BookDTOsBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", bookDTO.getIsbn());
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(service, times(1)).retrieveByIsbn(anyString());
    }
}
