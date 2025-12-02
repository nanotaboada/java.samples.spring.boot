package ar.com.nanotaboada.java.samples.spring.boot.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksDataInitializer {
    // Utility classes should not have public constructors (java:S1118)
    // https://www.baeldung.com/java-sonar-hide-implicit-constructor
    private BooksDataInitializer() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Book> seed() {
        ArrayList<Book> books = new ArrayList<>();
        Book book9781838986698 = new Book();
        book9781838986698.setIsbn("9781838986698");
        book9781838986698.setTitle("The Java Workshop");
        book9781838986698
                .setSubtitle("Learn object-oriented programming and kickstart your career in software development");
        book9781838986698.setAuthor("David Cuartielles, Andreas Göransson, Eric Foster-Johnson");
        book9781838986698.setPublisher("Packt Publishing");
        book9781838986698.setPublished(LocalDate.of(2019, 10, 31));
        book9781838986698.setPages(606);
        book9781838986698.setDescription(
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
        book9781838986698.setWebsite("https://www.packtpub.com/free-ebook/the-java-workshop/9781838986698");
        books.add(book9781838986698);
        Book book9781789613476 = new Book();
        book9781789613476.setIsbn("9781789613476");
        book9781789613476.setTitle("Hands-On Microservices with Spring Boot and Spring Cloud");
        book9781789613476.setSubtitle("Build and deploy Java microservices using Spring Cloud, Istio, and Kubernetes");
        book9781789613476.setAuthor("Magnus Larsson");
        book9781789613476.setPublisher("Packt Publishing");
        book9781789613476.setPublished(LocalDate.of(2019, 9, 20));
        book9781789613476.setPages(668);
        book9781789613476.setDescription(
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
        book9781789613476.setWebsite(
                "https://www.packtpub.com/free-ebook/hands-on-microservices-with-spring-boot-and-spring-cloud/9781789613476");
        books.add(book9781789613476);
        Book book9781838555726 = new Book();
        book9781838555726.setIsbn("9781838555726");
        book9781838555726.setTitle("Mastering Kotlin");
        book9781838555726.setSubtitle(
                "Learn advanced Kotlin programming techniques to build apps for Android, iOS, and the web");
        book9781838555726.setAuthor("Nate Ebel");
        book9781838555726.setPublisher("Packt Publishing");
        book9781838555726.setPublished(LocalDate.of(2019, 10, 11));
        book9781838555726.setPages(434);
        book9781838555726.setDescription(
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
        book9781838555726.setWebsite("https://www.packtpub.com/free-ebook/mastering-kotlin/9781838555726");
        books.add(book9781838555726);
        Book book9781484242216 = new Book();
        book9781484242216.setIsbn("9781484242216");
        book9781484242216.setTitle("Rethinking Productivity in Software Engineering");
        book9781484242216.setAuthor("Caitlin Sadowski, Thomas Zimmermann");
        book9781484242216.setPublisher("Apress");
        book9781484242216.setPublished(LocalDate.of(2019, 5, 7));
        book9781484242216.setPages(301);
        book9781484242216.setDescription(
                """
                        Get the most out of this foundational reference and improve the productivity of \
                        your software teams. This open access book collects the wisdom of the 2017 \
                        "Dagstuhl" seminar on productivity in software engineering, a meeting of \
                        community leaders, who came together with the goal of rethinking traditional \
                        definitions and measures of productivity.""");
        book9781484242216.setWebsite("https://link.springer.com/book/10.1007/978-1-4842-4221-6");
        books.add(book9781484242216);
        Book book9781642002232 = new Book();
        book9781642002232.setIsbn("9781642002232");
        book9781642002232.setTitle("Database Design Succinctly");
        book9781642002232.setAuthor("Joseph D. Booth");
        book9781642002232.setPublisher("Syncfusion");
        book9781642002232.setPublished(LocalDate.of(2022, 5, 25));
        book9781642002232.setPages(87);
        book9781642002232.setDescription(
                """
                        The way a user might perceive and use data and the optimal way a computer \
                        system might store it are often very different. In this Database Design \
                        Succinctly®, learn how to model the user's information into data in a computer \
                        database system in such a way as to allow the system to produce useful results \
                        for the end user. Joseph D. Booth will cover how to design a database system to \
                        allow businesses to get better reporting and control over their information, as \
                        well as how to improve their data to make sure it is as accurate as possible.""");
        book9781642002232.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/database-design-succinctly");
        books.add(book9781642002232);
        Book book9781642001174 = new Book();
        book9781642001174.setIsbn("9781642001174");
        book9781642001174.setTitle("SOLID Principles Succinctly");
        book9781642001174.setAuthor("Gaurav Kumar Arora");
        book9781642001174.setPublisher("Syncfusion");
        book9781642001174.setPublished(LocalDate.of(2016, 10, 31));
        book9781642001174.setPages(78);
        book9781642001174.setDescription(
                """
                        There is always room for improving one's coding ability, and SOLID design \
                        principles offer one way to see marked improvements in final output. With SOLID \
                        Principles Succinctly®, author Gaurav Kumar Arora will instruct you in how to \
                        use SOLID principles to take your programming skills to the next level.""");
        book9781642001174.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/solidprinciplessuccinctly");
        books.add(book9781642001174);
        Book book9781642001440 = new Book();
        book9781642001440.setIsbn("9781642001440");
        book9781642001440.setTitle("Java Succinctly Part 1");
        book9781642001440.setAuthor("Christopher Rose");
        book9781642001440.setPublisher("Syncfusion");
        book9781642001440.setPublished(LocalDate.of(2017, 8, 29));
        book9781642001440.setPages(125);
        book9781642001440.setDescription(
                """
                        Java is a high-level, cross-platform, object-oriented programming language that \
                        allows applications to be written once and run on a multitude of different \
                        devices. Java applications are ubiquitous, and the language is consistently \
                        ranked as one of the most popular and dominant in the world. Christopher Rose's \
                        Java Succinctly® Part 1 describes the foundations of Java—from printing a line \
                        of text to the console, to inheritance hierarchies in object-oriented \
                        programming. The e-book covers practical aspects of programming, such as \
                        debugging and using an IDE, as well as the core mechanics of the language.""");
        book9781642001440.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/java-succinctly-part-1");
        books.add(book9781642001440);
        Book book9781642001457 = new Book();
        book9781642001457.setIsbn("9781642001457");
        book9781642001457.setTitle("Java Succinctly Part 2");
        book9781642001457.setAuthor("Christopher Rose");
        book9781642001457.setPublisher("Syncfusion");
        book9781642001457.setPublished(LocalDate.of(2017, 9, 5));
        book9781642001457.setPages(134);
        book9781642001457.setDescription(
                """
                        In this second e-book on Java, Christopher Rose takes readers through some of \
                        the more advanced features of the language. Java Succinctly® Part 2 explores \
                        powerful and practical features of Java, such as multithreading, building GUI \
                        applications, and 2-D graphics and game programming. Then learn techniques for \
                        using these mechanisms in coherent projects by building a calculator app and a \
                        simple game with the author.""");
        book9781642001457.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/java-succinctly-part-2");
        books.add(book9781642001457);
        Book book9781642001495 = new Book();
        book9781642001495.setIsbn("9781642001495");
        book9781642001495.setTitle("Scala Succinctly");
        book9781642001495.setAuthor("Chris Rose");
        book9781642001495.setPublisher("Syncfusion");
        book9781642001495.setPublished(LocalDate.of(2017, 10, 16));
        book9781642001495.setPages(110);
        book9781642001495.setDescription(
                """
                        Learning a new programming language can be a daunting task, but Scala \
                        Succinctly® makes it a simple matter. Author Chris Rose guides readers through \
                        the basics of Scala, from installation to syntax shorthand, so that they can \
                        get up and running quickly.""");
        book9781642001495.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/scala-succinctly");
        books.add(book9781642001495);
        Book book9781642001242 = new Book();
        book9781642001242.setIsbn("9781642001242");
        book9781642001242.setTitle("SQL Queries Succinctly");
        book9781642001242.setAuthor("Nick Harrison");
        book9781642001242.setPublisher("Syncfusion");
        book9781642001242.setPublished(LocalDate.of(2017, 2, 4));
        book9781642001242.setPages(102);
        book9781642001242.setDescription(
                """
                        SQL is the language of data, and therefore the intermediary language for those \
                        who straddle the line between technology and business. Every business \
                        application needs a database and SQL is the key to working with these databases. \
                        Nick Harrison's SQL Queries Succinctly® will show you how to craft queries in \
                        SQL, from basic CRUD statements and slicing and dicing the data, to applying \
                        filters and using aggregate functions to summarize the data. You will look at \
                        solving common problems, navigating hierarchical data, and exploring the data \
                        dictionary.""");
        book9781642001242.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/sql-queries-succinctly");
        books.add(book9781642001242);
        Book book9781642001563 = new Book();
        book9781642001563.setIsbn("9781642001563");
        book9781642001563.setTitle("Docker Succinctly");
        book9781642001563.setAuthor("Elton Stoneman");
        book9781642001563.setPublisher("Syncfusion");
        book9781642001563.setPublished(LocalDate.of(2018, 1, 16));
        book9781642001563.setPages(98);
        book9781642001563.setDescription(
                """
                        Containers have revolutionized software development, allowing developers to \
                        bundle their applications with everything they need, from the operating system \
                        up, into a single package. Docker is one of the most popular platforms for \
                        containers, allowing them to be hosted on-premises or on the cloud, and to run \
                        on Linux, Windows, and Mac machines. With Docker Succinctly® by Elton Stoneman, \
                        learn the basics of building Docker images, sharing them on the Docker Hub, \
                        orchestrating containers to deliver large applications, and much more.""");
        book9781642001563.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/docker-succinctly");
        books.add(book9781642001563);
        Book book9781642001792 = new Book();
        book9781642001792.setIsbn("9781642001792");
        book9781642001792.setTitle("Kubernetes Succinctly");
        book9781642001792.setAuthor("Rahul Rai, Tarun Pabbi");
        book9781642001792.setPublisher("Syncfusion");
        book9781642001792.setPublished(LocalDate.of(2019, 3, 1));
        book9781642001792.setPages(121);
        book9781642001792.setDescription(
                """
                        With excellent orchestration and routing capabilities, Kubernetes is an \
                        enterprise-grade platform for building microservices applications. Kubernetes is \
                        evolving as the de facto container management tool used by organizations and \
                        cloud vendors all over the world. Kubernetes Succinctly® by Rahul Rai and Tarun \
                        Pabbi is your guide to learning Kubernetes and leveraging its many capabilities \
                        for developing, validating, and maintaining your applications.""");
        book9781642001792.setWebsite("https://www.syncfusion.com/succinctly-free-ebooks/kubernetes-succinctly");
        books.add(book9781642001792);
        Book book9781838820756 = new Book();
        book9781838820756.setIsbn("9781838820756");
        book9781838820756.setTitle("The Kubernetes Workshop");
        book9781838820756
                .setAuthor("Zachary Arnold, Sahil Dua, Wei Huang, Faisal Masood, Mélony Qin, Mohammed Abu Taleb");
        book9781838820756.setPublisher("Packt");
        book9781838820756.setPublished(LocalDate.of(2020, 9, 1));
        book9781838820756.setPages(780);
        book9781838820756.setDescription(
                """
                        Thanks to its extensive support for managing hundreds of containers that run \
                        cloud-native applications, Kubernetes is the most popular open source container \
                        orchestration platform that makes cluster management easy. This workshop adopts a \
                        practical approach to get you acquainted with the Kubernetes environment and its \
                        applications. Starting with an introduction to the fundamentals of Kubernetes, \
                        you'll install and set up your Kubernetes environment. You'll understand how to \
                        write YAML files and deploy your first simple web application container using \
                        Pod. You'll then assign human-friendly names to Pods, explore various Kubernetes \
                        entities and functions, and discover when to use them. As you work through the \
                        chapters, this Kubernetes book will show you how you can make full-scale use of \
                        Kubernetes by applying a variety of techniques for designing components and \
                        deploying clusters. You'll also get to grips with security policies for limiting \
                        access to certain functions inside the cluster. Toward the end of the book, \
                        you'll get a rundown of Kubernetes advanced features for building your own \
                        controller and upgrading to a Kubernetes cluster without downtime.""");
        book9781838820756.setWebsite("https://www.packtpub.com/free-ebook/the-kubernetes-workshop/9781838820756");
        books.add(book9781838820756);
        return books;
    }
}
