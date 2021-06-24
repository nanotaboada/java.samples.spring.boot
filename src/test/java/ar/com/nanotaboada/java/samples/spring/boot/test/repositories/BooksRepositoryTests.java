package ar.com.nanotaboada.java.samples.spring.boot.test.repositories;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void givenSave_whenInvokedWithBook_thenShouldCreateBookIntoRepository() {
        
        // Arrange
        Book expected = BooksBuilder.buildOne();
        
        // Act
        Book actual = repository.save(expected);
        
        // Assert
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        assertThat(repository.count()).isEqualTo(1L);
    }
}
