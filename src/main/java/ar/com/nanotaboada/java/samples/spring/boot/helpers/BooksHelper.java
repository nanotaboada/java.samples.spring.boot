package ar.com.nanotaboada.java.samples.spring.boot.helpers;

import java.time.LocalDate;
import java.util.ArrayList;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;

public class BooksHelper {

    public static Book buildOne() {
        
        Book book = new Book();
        book.setIsbn("978-1484200773");
        book.setTitle("Pro Git");
        book.setSubtitle("Everything you neeed to know about Git");
        book.setAuthor("Scott Chacon and Ben Straub");
        book.setPublisher("lulu.com; First Edition");
        book.setPublished(LocalDate.of(2014, 11, 18));
        book.setPages(458);
        book.setDescription("Pro Git (Second Edition) is your fully-updated guide to Git and its usage in the modern world. "
            + "Git has come a long way since it was first developed by Linus Torvalds for Linux kernel development. "
            + "It has taken the open source world by storm since its inception in 2005, and this book teaches you how to use it like a pro.");
        book.setWebsite("https://git-scm.com/book/en/v2");
        
        return book;
    }
	
    public static ArrayList<Book> buildMany() {
        
        // TODO: Implement collection of Fakes.
        throw new UnsupportedOperationException();
        
	}
}
