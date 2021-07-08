package ar.com.nanotaboada.java.samples.spring.boot.services;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@Service
public class BooksService {

    @Autowired
    private BooksRepository repository;

    @Autowired
    private Validator validator;

    // Create
    public boolean create(Book book) {
        boolean created = false;
        if (validator.validate(book).isEmpty() && !repository.existsById(book.getIsbn())) {
            repository.save(book);
            created = true;
        }
        return created;
    }

    // Retrieve
    public Book retrieveByIsbn(String isbn) {
        return repository.findByIsbn(isbn).orElse(null);
    }

    // Update
    public boolean update(Book book) {
        boolean updated = false;
        if (validator.validate(book).isEmpty() && repository.existsById(book.getIsbn())) {
            repository.save(book);
            updated = true;
        }
        return updated;
    }

    // Delete
    public boolean delete(String isbn) {
        boolean deleted = false;
        if (!isbn.isBlank() && repository.existsById(isbn)) {
            repository.deleteById(isbn);
            deleted = true;
        }
        return deleted;
    }
}
