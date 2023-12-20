package ar.com.nanotaboada.java.samples.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A configuration class that declares one or more Bean methods and also triggers auto-configuration
 * and component scanning.
 * */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}