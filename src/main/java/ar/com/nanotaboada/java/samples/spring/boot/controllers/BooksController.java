package ar.com.nanotaboada.java.samples.spring.boot.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;

@RestController
public class BooksController {

    @Autowired
    private BooksService service;

    // HTTP POST
    @PostMapping("/book")
    public ResponseEntity<String> postBook(@RequestBody Book book) {
        if (service.retrieveByIsbn(book.getIsbn()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            if (service.create(book)) {
                URI location = MvcUriComponentsBuilder
                    .fromMethodName(BooksController.class, "getBook", book.getIsbn())
                    .build()
                    .toUri();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(location);
                return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    // HTTP GET
    @GetMapping("/book/{isbn}")
    public ResponseEntity<Book> getBook(@PathVariable String isbn) {
        Book book = service.retrieveByIsbn(isbn);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // HTTP PUT
    @PutMapping("/book")
    public ResponseEntity<String> putBook(@RequestBody Book book) {
        if (service.retrieveByIsbn(book.getIsbn()) != null)   {
            if (service.update(book)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // HTTP DELETE
    @DeleteMapping("/book/{isbn}")
    public ResponseEntity<String> deleteBook(@PathVariable String isbn) {
        if (service.retrieveByIsbn(isbn) != null) {
            if (service.delete(isbn)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}