package ar.com.nanotaboada.java.samples.spring.boot.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;

import java.util.Optional;

@Repository
public interface BooksRepository extends CrudRepository<Book, String> {
    /**
     * Query creation from method names <br />
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */
    // Non-default methods in interfaces are not shown in coverage reports
    // https://www.jacoco.org/jacoco/trunk/doc/faq.html
    Optional<Book> findByIsbn(String isbn);
}
