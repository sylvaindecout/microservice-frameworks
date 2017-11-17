package test.sdc.service.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Entry point of the application.
 */
@SpringBootApplication
@ComponentScan("test.sdc.service.springboot")
public class Application {

    /**
     * Default constructor.
     */
    public Application() {
    }

    /**
     * Main method.
     *
     * @param args start-up arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}