package ar.com.nanotaboada.java.samples.spring.boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;

@RestController
public class BooksController {

    @Autowired
    private BooksService service;

    @GetMapping("/books/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        
        return service.retrieveByIsbn(isbn);
    }
}