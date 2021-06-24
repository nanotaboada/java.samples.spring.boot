package ar.com.nanotaboada.java.samples.spring.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@Service
public class BooksService {

    @Autowired
    private BooksRepository repository;
    
    public Book retrieveByIsbn(String isbn) {
        
        return repository.findByIsbn(isbn).orElse(null);
    }
}
