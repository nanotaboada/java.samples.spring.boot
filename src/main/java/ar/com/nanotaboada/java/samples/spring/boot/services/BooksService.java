package ar.com.nanotaboada.java.samples.spring.boot.services;

import jakarta.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@Service
public class BooksService {

    private final BooksRepository repository;
    private final Validator validator;
    private final ModelMapper mapper;

    public BooksService(BooksRepository repository, Validator validator, ModelMapper mapper) {
        this.repository =  repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    /* --------------------------------------------------------------------------------------------
     * Create
     * ----------------------------------------------------------------------------------------- */

    @CachePut(value = "books", key = "#bookDTO.isbn")
    public boolean create(BookDTO bookDTO) {
        boolean created = false;
        Book book = mapper.map(bookDTO, Book.class);
        if (validator.validate(book).isEmpty() && !repository.existsById(book.getIsbn())) {
            repository.save(book);
            created = true;
        }
        return created;
    }

    /* --------------------------------------------------------------------------------------------
     * Retrieve
     * ----------------------------------------------------------------------------------------- */

    @Cacheable(value = "books", key = "#isbn")
    public BookDTO retrieveByIsbn(String isbn) {
        BookDTO bookDTO = null;
        Book book = repository.findByIsbn(isbn).orElse(null);
        if (book != null) {
            bookDTO = mapper.map(book, BookDTO.class);
        }
        return bookDTO;
    }

    /* --------------------------------------------------------------------------------------------
     * Update
     * ----------------------------------------------------------------------------------------- */

    @CachePut(value = "books", key = "#bookDTO.isbn")
    public boolean update(BookDTO bookDTO) {
        boolean updated = false;
        Book book = mapper.map(bookDTO, Book.class);
        if (validator.validate(book).isEmpty() && repository.existsById(book.getIsbn())) {
            repository.save(book);
            updated = true;
        }
        return updated;
    }

    /* --------------------------------------------------------------------------------------------
     * Delete
     * ----------------------------------------------------------------------------------------- */

    @CacheEvict(value = "books", key = "#isbn")
    public boolean delete(String isbn) {
        boolean deleted = false;
        if (!isbn.isBlank() && repository.existsById(isbn)) {
            repository.deleteById(isbn);
            deleted = true;
        }
        return deleted;
    }
}
