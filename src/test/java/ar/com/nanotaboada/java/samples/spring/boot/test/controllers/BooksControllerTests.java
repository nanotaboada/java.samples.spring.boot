package ar.com.nanotaboada.java.samples.spring.boot.test.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.BooksController;
import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BooksBuilder;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BooksController.class)
public class BooksControllerTests {
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean                           
    private BooksService service;
    
    @Test
    public void givenHttpGetVerb_WhenRequestParameterIdentifiesExistingBook_ThenShouldReturnStatusOkAndTheBook() throws Exception {
        
        // Arrange
        Book expected = BooksBuilder.buildOne();
        
        Mockito
            .when(service.retrieveByIsbn(expected.getIsbn()))
            .thenReturn(expected);

        RequestBuilder request = MockMvcRequestBuilders.get("/books/{isbn}", expected.getIsbn());     
        
        // Act
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
        String content = response.getContentAsString();
        Book actual = new ObjectMapper().readValue(content, Book.class);
        
        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void givenHttpGetVerb_WhenRequestParameterDoesNotIdentifyAnExistingBook_ThenShouldReturnStatusNotFound()
        throws Exception {

        // Arrange
        String isbn = "978-0000000000";

        Mockito
            .when(service.retrieveByIsbn(Mockito.anyString()))
            .thenReturn(Mockito.any(Book.class));

        RequestBuilder request = MockMvcRequestBuilders.get("/books/{isbn}", isbn);

        // Act
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();

        // Assert
        assertThat(response.getStatus()).isEqualTo(404);
    }
}
