package ar.com.nanotaboada.java.samples.spring.boot.services;

import jakarta.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        if (validator.validate(bookDTO).isEmpty()
            && !repository.existsById(bookDTO.getIsbn())) {
            Book book = mapper.map(bookDTO, Book.class);
            repository.save(book);
            return true;
        }
        return false;
    }

    /* --------------------------------------------------------------------------------------------
     * Retrieve
     * ----------------------------------------------------------------------------------------- */

    @Cacheable(value = "books", key = "#isbn")
    public BookDTO retrieveByIsbn(String isbn) {
        return repository.findByIsbn(isbn)
            .map(book -> mapper.map(book, BookDTO.class))
            .orElse(null);
    }

    @Cacheable(value = "books")
    public List<BookDTO> retrieveAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(book -> mapper.map(book, BookDTO.class))
            .collect(Collectors.toList());
    }

    /* --------------------------------------------------------------------------------------------
     * Update
     * ----------------------------------------------------------------------------------------- */

    @CachePut(value = "books", key = "#bookDTO.isbn")
    public boolean update(BookDTO bookDTO) {
        if (validator.validate(bookDTO).isEmpty()
            && repository.existsById(bookDTO.getIsbn())) {
            Book book = mapper.map(bookDTO, Book.class);
            repository.save(book);
            return true;
        }
        return false;
    }

    /* --------------------------------------------------------------------------------------------
     * Delete
     * ----------------------------------------------------------------------------------------- */

    @CacheEvict(value = "books", key = "#isbn")
    public boolean delete(String isbn) {
        if (repository.existsById(isbn)) {
            repository.deleteById(isbn);
            return true;
        }
        return false;
    }
}
