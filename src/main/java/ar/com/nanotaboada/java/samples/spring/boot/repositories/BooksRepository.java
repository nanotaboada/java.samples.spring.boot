package ar.com.nanotaboada.java.samples.spring.boot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;

import java.util.Optional;

@Repository
public interface BooksRepository extends CrudRepository<Book, String> {
    
    Optional<Book> findByIsbn(String isbn);

}
