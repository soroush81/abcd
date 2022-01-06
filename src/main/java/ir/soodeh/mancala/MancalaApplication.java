package ir.soodeh.mancala;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

//disable the white label error page to see the custom errors
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
public class MancalaApplication {

    public static void main(String[] args) {
        SpringApplication.run ( MancalaApplication.class, args );
    }



}
