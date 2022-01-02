package ir.soodeh.mancala;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
//disable the white label error page to see our custom error
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class MancalaApplication {

    public static void main(String[] args) {
        SpringApplication.run ( MancalaApplication.class, args );
    }

}
