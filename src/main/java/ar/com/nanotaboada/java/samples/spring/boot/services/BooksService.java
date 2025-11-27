package ar.com.nanotaboada.java.samples.spring.boot.services;

import java.util.List;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

@Service
@RequiredArgsConstructor
public class BooksService {

    private final BooksRepository booksRepository;
    private final ModelMapper modelMapper;

    /*
     * -------------------------------------------------------------------------
     * Create
     * -------------------------------------------------------------------------
     */

    @CachePut(value = "books", key = "#bookDTO.isbn")
    public boolean create(BookDTO bookDTO) {
        if (booksRepository.existsById(bookDTO.getIsbn())) {
            return false;
        } else {
            Book book = mapFrom(bookDTO);
            booksRepository.save(book);
            return true;
        }

    }

    /*
     * -------------------------------------------------------------------------
     * Retrieve
     * -------------------------------------------------------------------------
     */

    @Cacheable(value = "books", key = "#isbn")
    public BookDTO retrieveByIsbn(String isbn) {
        return booksRepository.findByIsbn(isbn)
                .map(this::mapFrom)
                .orElse(null);
    }

    @Cacheable(value = "books")
    public List<BookDTO> retrieveAll() {
        return StreamSupport.stream(booksRepository.findAll().spliterator(), false)
                .map(this::mapFrom)
                .toList();
    }

    /*
     * -------------------------------------------------------------------------
     * Search
     * -------------------------------------------------------------------------
     */

    public List<BookDTO> searchByDescription(String keyword) {
        return booksRepository.findByDescriptionContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapFrom)
                .toList();
    }

    /*
     * -------------------------------------------------------------------------
     * Update
     * -------------------------------------------------------------------------
     */

    @CachePut(value = "books", key = "#bookDTO.isbn")
    public boolean update(BookDTO bookDTO) {
        if (booksRepository.existsById(bookDTO.getIsbn())) {
            Book book = mapFrom(bookDTO);
            booksRepository.save(book);
            return true;
        } else {
            return false;
        }
    }

    /*
     * -------------------------------------------------------------------------
     * Delete
     * -------------------------------------------------------------------------
     */

    @CacheEvict(value = "books", key = "#isbn")
    public boolean delete(String isbn) {
        if (booksRepository.existsById(isbn)) {
            booksRepository.deleteById(isbn);
            return true;
        } else {
            return false;
        }
    }

    private BookDTO mapFrom(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book mapFrom(BookDTO dto) {
        return modelMapper.map(dto, Book.class);
    }
}
