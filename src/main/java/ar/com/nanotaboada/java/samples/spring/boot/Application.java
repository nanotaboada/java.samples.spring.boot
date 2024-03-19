package ar.com.nanotaboada.java.samples.spring.boot;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;
import ar.com.nanotaboada.java.samples.spring.boot.models.BooksDataInitializer;
import ar.com.nanotaboada.java.samples.spring.boot.repositories.BooksRepository;

/**
 * A configuration class that declares one or more Bean methods and also triggers auto-configuration
 * and component scanning.
 * */
@SpringBootApplication
public class Application {

    private final BooksRepository repository;

    public Application(BooksRepository repository) {
        this.repository = repository;
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner seed() {
        return args -> {
            List<Book> books = BooksDataInitializer.seed();
            if (books != null) {
                repository.saveAll(books);
            }
        };
    }
}