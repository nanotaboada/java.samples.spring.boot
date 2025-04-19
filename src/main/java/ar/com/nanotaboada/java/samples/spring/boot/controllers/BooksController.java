package ar.com.nanotaboada.java.samples.spring.boot.controllers;

import static org.springframework.http.HttpHeaders.LOCATION;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.hibernate.validator.constraints.ISBN;
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
@OpenAPIDefinition(info = @Info(title = "java.samples.spring.boot", version = "1.0", description = "🧪 Proof of Concept for a RESTful Web Service made with JDK 21 (LTS) and Spring Boot 3", contact = @Contact(name = "GitHub", url = "https://github.com/nanotaboada/java.samples.spring.boot"), license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")))
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP POST
     * -------------------------------------------------------------------------
     */

    @PostMapping("/books")
    @Operation(summary = "Creates a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content)
    })
    public ResponseEntity<Void> post(@RequestBody @Valid BookDTO bookDTO) {
        boolean created = booksService.create(bookDTO);
        if (!created) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        URI location = MvcUriComponentsBuilder
                .fromMethodCall(MvcUriComponentsBuilder.on(BooksController.class).getByIsbn(bookDTO.getIsbn()))
                .build()
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(LOCATION, location.toString())
                .build();
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP GET
     * -------------------------------------------------------------------------
     */

    @GetMapping("/books/{isbn}")
    @Operation(summary = "Retrieves a book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<BookDTO> getByIsbn(@PathVariable String isbn) {
        BookDTO bookDTO = booksService.retrieveByIsbn(isbn);
        return (bookDTO != null)
                ? ResponseEntity.status(HttpStatus.OK).body(bookDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/books")
    @Operation(summary = "Retrieves all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookDTO[].class)))
    })
    public ResponseEntity<List<BookDTO>> getAll() {
        List<BookDTO> books = booksService.retrieveAll();
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP PUT
     * -------------------------------------------------------------------------
     */

    @PutMapping("/books")
    @Operation(summary = "Updates (entirely) a book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> put(@RequestBody @Valid BookDTO bookDTO) {
        boolean updated = booksService.update(bookDTO);
        return (updated)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*
     * -------------------------------------------------------------------------
     * HTTP DELETE
     * -------------------------------------------------------------------------
     */

    @DeleteMapping("/books/{isbn}")
    @Operation(summary = "Deletes a book by its ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    public ResponseEntity<Void> delete(@PathVariable @ISBN String isbn) {
        boolean deleted = booksService.delete(isbn);
        return (deleted)
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
