package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;
import java.util.ArrayList;

public class BooksBuilder {

    public static Book buildOneExistingValid() {
        Book book = new Book();
        book.setIsbn("978-1484200773");
        book.setTitle("Pro Git");
        book.setSubtitle("Everything you neeed to know about Git");
        book.setAuthor("Scott Chacon and Ben Straub");
        book.setPublisher("lulu.com; First Edition");
        book.setPublished(LocalDate.of(2014, 11, 18));
        book.setPages(458);
        book.setDescription("Pro Git (Second Edition) is your fully-updated guide to Git and its "
            + "usage in the modern world. Git has come a long way since it was first developed by "
            + "Linus Torvalds for Linux kernel development. It has taken the open source world by "
            + "storm since its inception in 2005, and this book teaches you how to use it like a "
            + "pro.");
        book.setWebsite("https://git-scm.com/book/en/v2");
        return book;
    }

    public static Book buildOneExistingInvalid() {
        Book book = new Book();
        book.setIsbn("978-1484200773");
        book.setTitle("Pro Git");
        book.setSubtitle("Everything you neeed to know about Git");
        return book;
    }

    public static Book buildOneNewValid() {
        Book book = new Book();
        book.setIsbn("978-1234567890");
        book.setTitle("Title");
        book.setSubtitle("Sub Title");
        book.setAuthor("Author");
        book.setPublisher("Publisher");
        book.setPublished(LocalDate.now());
        book.setPages(123);
        book.setDescription("Descrition");
        book.setWebsite("https://domain.com/");
        return book;
    }
    
    public static Book buildOneNewInvalid() {
        Book book = new Book();
        book.setIsbn("978-1234567890");
        book.setTitle("Title");
        book.setSubtitle("Sub Title");
        book.setAuthor("Author");
        book.setPublisher("Publisher");
        book.setPublished(LocalDate.now());
        book.setPages(123);
        book.setDescription("Descrition");
        book.setWebsite("https://domain.com/");
        return book;
    }

    public static ArrayList<Book> buildMany() {
        throw new UnsupportedOperationException();
    }
}
