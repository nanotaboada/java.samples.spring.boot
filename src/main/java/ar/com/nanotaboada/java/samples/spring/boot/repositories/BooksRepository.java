package ar.com.nanotaboada.java.samples.spring.boot.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;

@Repository
public interface BooksRepository extends CrudRepository<Book, String> {
    /**
     * Query creation from method names <br />
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */
    // Non-default methods in interfaces are not shown in coverage reports
    // https://www.jacoco.org/jacoco/trunk/doc/faq.html
    Optional<Book> findByIsbn(String isbn);

    /**
     * Finds books whose description contains the given keyword (case-insensitive).
     * SQLite handles TEXT fields natively, so no CAST operation is needed.
     *
     * @param keyword the keyword to search for in the description
     * @return a list of books matching the search criteria
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> findByDescriptionContainingIgnoreCase(@Param("keyword") String keyword);
}
