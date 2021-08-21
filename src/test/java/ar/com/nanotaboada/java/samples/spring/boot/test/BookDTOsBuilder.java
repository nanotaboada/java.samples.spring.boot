package ar.com.nanotaboada.java.samples.spring.boot.test;

import java.time.LocalDate;

import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;

public class BookDTOsBuilder {

    public static BookDTO buildOneValid() {
        BookDTO bookDto = new BookDTO();
        bookDto.setIsbn("978-1484200773");
        bookDto.setTitle("Pro Git");
        bookDto.setSubtitle("Everything you neeed to know about Git");
        bookDto.setAuthor("Scott Chacon and Ben Straub");
        bookDto.setPublisher("lulu.com; First Edition");
        bookDto.setPublished(LocalDate.of(2014, 11, 18));
        bookDto.setPages(458);
        bookDto.setDescription("Pro Git (Second Edition) is your fully-updated guide to Git and its "
            + "usage in the modern world. Git has come a long way since it was first developed by "
            + "Linus Torvalds for Linux kernel development. It has taken the open source world by "
            + "storm since its inception in 2005, and this book teaches you how to use it like a "
            + "pro.");
        bookDto.setWebsite("https://git-scm.com/book/en/v2");
        return bookDto;
    }

    public static BookDTO buildOneInvalid() {
        BookDTO bookDto = new BookDTO();
        bookDto.setIsbn("978-1234567890"); // Invalid (invalid ISBN)
        bookDto.setTitle("Title");
        bookDto.setSubtitle("Sub Title");
        bookDto.setAuthor("Author");
        bookDto.setPublisher("Publisher");
        bookDto.setPublished(LocalDate.now()); // Invalid (must be a past date)
        bookDto.setPages(123);
        bookDto.setDescription("Description");
        bookDto.setWebsite("https://domain.com/");
        return bookDto;
    }
}
