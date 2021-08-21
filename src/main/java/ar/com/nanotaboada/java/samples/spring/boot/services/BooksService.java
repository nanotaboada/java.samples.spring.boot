package ar.com.nanotaboada.java.samples.spring.boot.services;

import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@Service
public class BooksService {

    @Autowired
    private BooksRepository repository;

    @Autowired
    private Validator validator;
    
    @Autowired
    private ModelMapper mapper;

    // Create
    public boolean create(BookDTO bookDTO) {
        boolean created = false;
        Book book = mapper.map(bookDTO, Book.class);
        if (validator.validate(book).isEmpty() && !repository.existsById(book.getIsbn())) {
            repository.save(book);
            created = true;
        }
        return created;
    }

    // Retrieve
    public BookDTO retrieveByIsbn(String isbn) {
        BookDTO bookDTO = null;
        Book book = repository.findByIsbn(isbn).orElse(null);
        if (book != null) {
            bookDTO = mapper.map(book, BookDTO.class);
        }
        return bookDTO;
    }

    // Update
    public boolean update(BookDTO bookDTO) {
        boolean updated = false;
        Book book = mapper.map(bookDTO, Book.class);
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
