package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.BooksController;
import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BooksBuilder;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;

@DisplayName("HTTP Verbs on Controller")
@ExtendWith(SpringExtension.class)
@WebMvcTest(BooksController.class)
public class BooksControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksService service;

    @Test
    public void givenHttpPostVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusConflict()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(book);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(book); // Existing
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
    public void givenHttpPostVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusCreatedAndLocationHeader()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneValid();
        String content = new ObjectMapper().writeValueAsString(book);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        Mockito
            .when(service.create(any(Book.class)))
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
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isNotNull();
        assertThat(response.getHeader(HttpHeaders.LOCATION)).contains("/book/" + book.getIsbn());
        verify(service, times(1)).retrieveByIsbn(anyString());
        verify(service, times(1)).create(any(Book.class));
    }

    @Test
    public void givenHttpPostVerb_whenRequestBodyContainsInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(book);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        Mockito
            .when(service.create(any(Book.class)))
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
        verify(service, times(1)).create(any(Book.class));
    }

    @Test
    public void givenHttpGetVerb_whenRequestParameterIdentifiesExistingBook_thenShouldReturnStatusOkAndTheBook()
        throws Exception {
        // Arrange
        Book expected = BooksBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(expected); // Existing
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/book/{isbn}", expected.getIsbn());
        // Act
        MockHttpServletResponse response = mockMvc
            .perform(request)
            .andReturn()
            .getResponse();
        String content = response.getContentAsString();
        Book actual = new ObjectMapper().readValue(content, Book.class);
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(service, times(1)).retrieveByIsbn(anyString());
    }

    @Test
    public void givenHttpGetVerb_whenRequestParameterDoesNotIdentifyAnExistingBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        String isbn = BooksBuilder.buildOneValid().getIsbn();
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
    public void givenHttpPutVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusNoContent()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneValid();
        String content = new ObjectMapper().writeValueAsString(book);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(book); // Existing
        Mockito
            .when(service.update(any(Book.class)))
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
        verify(service, times(1)).update(any(Book.class));
    }

    @Test
    public void givenHttpPutVerb_whenRequestBodyContainsExistingInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneInvalid();
        String content = new ObjectMapper().writeValueAsString(book);
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(book); // Existing
        Mockito
            .when(service.update(any(Book.class)))
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
        verify(service, times(1)).update(any(Book.class));
    }

    @Test
    public void givenHttpPutVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneValid();
        String content = new ObjectMapper().writeValueAsString(book);
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

    @Test
    public void givenHttpDeleteVerb_whenRequestBodyContainsExistingValidBook_thenShouldReturnStatusNoContent()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(book); // Existing
        Mockito
            .when(service.delete(anyString()))
            .thenReturn(true);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", book.getIsbn());
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
    public void givenHttpDeleteVerb_whenRequestBodyContainsExistingInvalidBook_thenShouldReturnStatusBadRequest()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneInvalid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(book); // Existing
        Mockito
            .when(service.delete(anyString()))
            .thenReturn(false);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", book.getIsbn());
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
    public void givenHttpDeleteVerb_whenRequestBodyContainsNewValidBook_thenShouldReturnStatusNotFound()
        throws Exception {
        // Arrange
        Book book = BooksBuilder.buildOneValid();
        Mockito
            .when(service.retrieveByIsbn(anyString()))
            .thenReturn(null); // New
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/book/{isbn}", book.getIsbn());
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
