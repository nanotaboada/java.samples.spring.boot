package ar.com.nanotaboada.java.samples.spring.boot;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * A configuration class that declares one or more Bean methods and also
 * triggers auto-configuration and component scanning.
 */
@SpringBootApplication
@EnableCaching
public class Application {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
