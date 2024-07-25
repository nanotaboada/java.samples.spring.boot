package ar.com.nanotaboada.java.samples.spring.boot.controllers;

import java.net.URI;
import java.util.List;

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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.services.BooksService;

@RestController
@Tag(name = "Books")
@OpenAPIDefinition(
    info = @Info(
        title = "java.samples.spring.boot",
        version = "1.0",
        description = "🧪 Proof of Concept for a RESTful Web Service made with JDK 21 (LTS) and Spring Boot 3",
        contact = @Contact(
            name = "GitHub",
            url = "https://github.com/nanotaboada/java.samples.spring.boot"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    )
)
public class BooksController {

    private final BooksService service;

    public BooksController(BooksService service) {
        this.service = service;
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP POST
     * ----------------------------------------------------------------------------------------- */

    @PostMapping("/books")
    @Operation(summary = "Creates a new book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created", content = @Content),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public ResponseEntity<String> post(@RequestBody BookDTO bookDTO) {
        if (service.retrieveByIsbn(bookDTO.getIsbn()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            if (service.create(bookDTO)) {
                URI location = MvcUriComponentsBuilder
                    .fromMethodName(BooksController.class, "getByIsbn", bookDTO.getIsbn())
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

    /* --------------------------------------------------------------------------------------------
     * HTTP GET
     * ----------------------------------------------------------------------------------------- */

    @GetMapping("/books/{isbn}")
    @Operation(summary = "Retrieves a book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<BookDTO> getByIsbn(@PathVariable String isbn) {
        BookDTO bookDTO = service.retrieveByIsbn(isbn);
        if (bookDTO != null) {
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books")
    @Operation(summary = "Retrieves all books")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = BookDTO[].class)))
    })
    public ResponseEntity<List<BookDTO>> getAll() {
        List<BookDTO> books = service.retrieveAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP PUT
     * ----------------------------------------------------------------------------------------- */

    @PutMapping("/books")
    @Operation(summary = "Updates (entirely) a book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<String> put(@RequestBody BookDTO bookDTO) {
        if (service.retrieveByIsbn(bookDTO.getIsbn()) != null)   {
            if (service.update(bookDTO)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* --------------------------------------------------------------------------------------------
     * HTTP DELETE
     * ----------------------------------------------------------------------------------------- */

    @DeleteMapping("/books/{isbn}")
    @Operation(summary = "Deletes a book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<String> delete(@PathVariable String isbn) {
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
