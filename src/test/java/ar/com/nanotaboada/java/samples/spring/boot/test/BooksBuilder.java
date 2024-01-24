package ar.com.nanotaboada.java.samples.spring.boot.test;

import java.time.LocalDate;

import ar.com.nanotaboada.java.samples.spring.boot.models.Book;

public class BooksBuilder {

    public static Book buildOneValid() {
        Book book = new Book();
        book.setIsbn("978-1484200773");
        book.setTitle("Pro Git");
        book.setSubtitle("Everything you neeed to know about Git");
        book.setAuthor("Scott Chacon and Ben Straub");
        book.setPublisher("lulu.com; First Edition");
        book.setPublished(LocalDate.of(2014, 11, 18));
        book.setPages(458);
        book.setDescription("""
                Pro Git (Second Edition) is your fully-updated guide to Git and its \
                usage in the modern world. Git has come a long way since it was first developed by \
                Linus Torvalds for Linux kernel development. It has taken the open source world by \
                storm since its inception in 2005, and this book teaches you how to use it like a \
                pro.\
                """);
        book.setWebsite("https://git-scm.com/book/en/v2");
        return book;
    }

    public static Book buildOneInvalid() {
        Book book = new Book();
        book.setIsbn("978-1234567890"); // Invalid (invalid ISBN)
        book.setTitle("Title");
        book.setSubtitle("Sub Title");
        book.setAuthor("Author");
        book.setPublisher("Publisher");
        book.setPublished(LocalDate.now()); // Invalid (must be a past date)
        book.setPages(123);
        book.setDescription("Description");
        book.setWebsite("https://domain.com/");
        return book;
    }
}
