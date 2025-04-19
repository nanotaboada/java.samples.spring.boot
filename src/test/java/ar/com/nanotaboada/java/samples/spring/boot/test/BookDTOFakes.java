package ar.com.nanotaboada.java.samples.spring.boot.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.com.nanotaboada.java.samples.spring.boot.models.BookDTO;

public final class BookDTOFakes {

    private BookDTOFakes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static BookDTO createOneValid() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("978-1484200773");
        bookDTO.setTitle("Pro Git");
        bookDTO.setSubtitle("Everything you neeed to know about Git");
        bookDTO.setAuthor("Scott Chacon and Ben Straub");
        bookDTO.setPublisher("lulu.com; First Edition");
        bookDTO.setPublished(LocalDate.of(2014, 11, 18));
        bookDTO.setPages(458);
        bookDTO.setDescription(
                """
                        Pro Git (Second Edition) is your fully-updated guide to Git and its \
                        usage in the modern world. Git has come a long way since it was first developed by \
                        Linus Torvalds for Linux kernel development. It has taken the open source world by \
                        storm since its inception in 2005, and this book teaches you how to use it like a \
                        pro.""");
        bookDTO.setWebsite("https://git-scm.com/book/en/v2");
        return bookDTO;
    }

    public static BookDTO createOneInvalid() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn("978-1234567890"); // Invalid (invalid ISBN)
        bookDTO.setTitle("Title");
        bookDTO.setSubtitle("Sub Title");
        bookDTO.setAuthor("Author");
        bookDTO.setPublisher("Publisher");
        bookDTO.setPublished(LocalDate.now()); // Invalid (must be a past date)
        bookDTO.setPages(123);
        bookDTO.setDescription("Description");
        bookDTO.setWebsite("https://domain.com/");
        return bookDTO;
    }

    public static List<BookDTO> createManyValid() {
        ArrayList<BookDTO> bookDTOs = new ArrayList<>();
        BookDTO bookDTO9781838986698 = new BookDTO();
        bookDTO9781838986698.setIsbn("9781838986698");
        bookDTO9781838986698.setTitle("The Java Workshop");
        bookDTO9781838986698
                .setSubtitle("Learn object-oriented programming and kickstart your career in software development");
        bookDTO9781838986698.setAuthor("David Cuartielles, Andreas Göransson, Eric Foster-Johnson");
        bookDTO9781838986698.setPublisher("Packt Publishing");
        bookDTO9781838986698.setPublished(LocalDate.of(2019, 10, 31));
        bookDTO9781838986698.setPages(606);
        bookDTO9781838986698.setDescription(
                """
                        Java is a versatile, popular programming language used across a wide range of \
                        industries. Learning how to write effective Java code can take your career to \
                        the next level, and The Java Workshop will help you do just that. This book is \
                        designed to take the pain out of Java coding and teach you everything you need \
                        to know to be productive in building real-world software. The Workshop starts by \
                        showing you how to use classes, methods, and the built-in Collections API to \
                        manipulate data structures effortlessly. You'll dive right into learning about \
                        object-oriented programming by creating classes and interfaces and making use of \
                        inheritance and polymorphism. After learning how to handle exceptions, you'll \
                        study the modules, packages, and libraries that help you organize your code. As \
                        you progress, you'll discover how to connect to external databases and web \
                        servers, work with regular expressions, and write unit tests to validate your \
                        code. You'll also be introduced to functional programming and see how to \
                        implement it using lambda functions. By the end of this Workshop, you'll be \
                        well-versed with key Java concepts and have the knowledge and confidence to \
                        tackle your own ambitious projects with Java.""");
        bookDTO9781838986698.setWebsite("https://www.packtpub.com/free-ebook/the-java-workshop/9781838986698");
        bookDTOs.add(bookDTO9781838986698);
        BookDTO bookDTO9781789613476 = new BookDTO();
        bookDTO9781789613476.setIsbn("9781789613476");
        bookDTO9781789613476.setTitle("Hands-On Microservices with Spring Boot and Spring Cloud");
        bookDTO9781789613476
                .setSubtitle("Build and deploy Java microservices using Spring Cloud, Istio, and Kubernetes");
        bookDTO9781789613476.setAuthor("Magnus Larsson");
        bookDTO9781789613476.setPublisher("Packt Publishing");
        bookDTO9781789613476.setPublished(LocalDate.of(2019, 9, 20));
        bookDTO9781789613476.setPages(668);
        bookDTO9781789613476.setDescription(
                """
                        Microservices architecture allows developers to build and maintain applications \
                        with ease, and enterprises are rapidly adopting it to build software using \
                        Spring Boot as their default framework. With this book, you'll learn how to \
                        efficiently build and deploy microservices using Spring Boot. This microservices \
                        book will take you through tried and tested approaches to building distributed \
                        systems and implementing microservices architecture in your organization. \
                        Starting with a set of simple cooperating microservices developed using Spring \
                        Boot, you'll learn how you can add functionalities such as persistence, make \
                        your microservices reactive, and describe their APIs using Swagger/OpenAPI. As \
                        you advance, you'll understand how to add different services from Spring Cloud \
                        to your microservice system. The book also demonstrates how to deploy your \
                        microservices using Kubernetes and manage them with Istio for improved security \
                        and traffic management. Finally, you'll explore centralized log management using \
                        the EFK stack and monitor microservices using Prometheus and Grafana. By the end \
                        of this book, you'll be able to build microservices that are scalable and robust \
                        using Spring Boot and Spring Cloud.""");
        bookDTO9781789613476.setWebsite(
                "https://www.packtpub.com/free-ebook/hands-on-microservices-with-spring-boot-and-spring-cloud/9781789613476");
        bookDTOs.add(bookDTO9781789613476);
        BookDTO bookDTO9781838555726 = new BookDTO();
        bookDTO9781838555726.setIsbn("9781838555726");
        bookDTO9781838555726.setTitle("Mastering Kotlin");
        bookDTO9781838555726.setSubtitle(
                "Learn advanced Kotlin programming techniques to build apps for Android, iOS, and the web");
        bookDTO9781838555726.setAuthor("Nate Ebel");
        bookDTO9781838555726.setPublisher("Packt Publishing");
        bookDTO9781838555726.setPublished(LocalDate.of(2019, 10, 11));
        bookDTO9781838555726.setPages(434);
        bookDTO9781838555726.setDescription(
                """
                        Using Kotlin without taking advantage of its power and interoperability is like \
                        owning a sports car and never taking it out of the garage. While documentation \
                        and introductory resources can help you learn the basics of Kotlin, the fact \
                        that it's a new language means that there are limited learning resources and \
                        code bases available in comparison to Java and other established languages. This \
                        Kotlin book will show you how to leverage software designs and concepts that \
                        have made Java the most dominant enterprise programming language. You'll \
                        understand how Kotlin is a modern approach to object-oriented programming (OOP). \
                        This book will take you through the vast array of features that Kotlin provides \
                        over other languages. These features include seamless interoperability with \
                        Java, efficient syntax, built-in functional programming constructs, and support \
                        for creating your own DSL. Finally, you will gain an understanding of \
                        implementing practical design patterns and best practices to help you master the \
                        Kotlin language. By the end of the book, you'll have obtained an advanced \
                        understanding of Kotlin in order to be able to build production-grade \
                        applications.""");
        bookDTO9781838555726.setWebsite("https://www.packtpub.com/free-ebook/mastering-kotlin/9781838555726");
        bookDTOs.add(bookDTO9781838555726);
        BookDTO bookDTO9781484242216 = new BookDTO();
        bookDTO9781484242216.setIsbn("9781484242216");
        bookDTO9781484242216.setTitle("Rethinking Productivity in Software Engineering");
        bookDTO9781484242216.setAuthor("Caitlin Sadowski, Thomas Zimmermann");
        bookDTO9781484242216.setPublisher("Apress");
        bookDTO9781484242216.setPublished(LocalDate.of(2019, 5, 7));
        bookDTO9781484242216.setPages(301);
        bookDTO9781484242216.setDescription(
                """
                        Get the most out of this foundational reference and improve the productivity of \
                        your software teams. This open access book collects the wisdom of the 2017 \
                        "Dagstuhl" seminar on productivity in software engineering, a meeting of \
                        community leaders, who came together with the goal of rethinking traditional \
                        definitions and measures of productivity.""");
        bookDTO9781484242216.setWebsite("https://link.springer.com/book/10.1007/978-1-4842-4221-6");
        bookDTOs.add(bookDTO9781484242216);
        return bookDTOs;
    }
}
