package de.itch.multimedia;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Multimedia Screen", version = "1.0", description = "API für den Multimedia Screen der Immo Eppendorf GmbH <br><br> Erstellt im LF2 der Itch Hamburg"))
public class MultimediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultimediaApplication.class, args);
	}

}
