package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;
import java.util.ArrayList;

public class BooksBuilder {

    public static Book buildOne() {
        
        Book book = new Book();
        book.setIsbn("0359044522");
        book.setTitle("Practical Guide to Building an API Back End with Spring Boot");
        book.setSubtitle(null);
        book.setAuthor("Wim Deblauwe");
        book.setPublisher("lulu.com; First Edition");
        book.setPublished(LocalDate.of(2019, 1, 15));
        book.setPages(172);
        book.setDescription("Starting your first project with Spring Boot can be a bit daunting given the vast options that it provides. This book will guide you step-by-step along the way to be a Spring Boot hero in no time.");
        book.setWebsite("https://www.baeldung.com/rest-api-spring-guide");
        
        return book;
    }
	
    public static ArrayList<Book> buildMany() {
        
        // TODO: Implement collection of Fakes.
        throw new UnsupportedOperationException();
        
	}
}
