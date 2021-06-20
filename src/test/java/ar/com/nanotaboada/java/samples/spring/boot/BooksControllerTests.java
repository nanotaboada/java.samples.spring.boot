package ar.com.nanotaboada.java.samples.spring.boot;

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
        Book book = BooksBuilder.buildOne();
        Mockito.when(service.retrieveByIsbn(book.getIsbn())).thenReturn(book);
        
        RequestBuilder request = MockMvcRequestBuilders.get("/books/{isbn}", book.getIsbn());     
        
        // Act
        MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
        
        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        // TODO: Improve response content assertions
        assertThat(response.getContentAsString().contains(book.getIsbn())).isTrue();
    }
}
