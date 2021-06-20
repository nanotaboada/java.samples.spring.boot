package ar.com.nanotaboada.java.samples.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ar.com.nanotaboada.java.samples.spring.boot.controllers.BooksController;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private BooksController controller;

    @Test
    public void contextLoads() throws Exception {
        
        assertThat(controller).isNotNull();
        
    }
	
}
