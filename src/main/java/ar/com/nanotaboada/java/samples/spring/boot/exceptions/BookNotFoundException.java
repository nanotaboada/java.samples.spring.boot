package ar.com.nanotaboada.java.samples.spring.boot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookNotFoundException(String isbn) {
        
        super(String.format("Could not find Book with ISBN %s", isbn));

    }
}
