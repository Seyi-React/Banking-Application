package com.oluwaseyi.Bank_Demo_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Oluwaseyi's Bank Application",
		description = "BACKEND REST API's FOR SEYII'S BANK ",
		version = "v1.0",
		contact = @Contact(
			name = "Adeoti Oluwaseyi",
			email = "oluwaseyiadeoti825@gmail.com",
			url = "https://github.com/seyi-react/seyi_bank_app"
		),
		license = @License (
			name = "Oluwaseyi Bank",
			url = "https://github.com/seyi-react/seyi_bank_app"
		)
		
	),
	externalDocs = @ExternalDocumentation (
		description = " Oluwaseyi's Bank Application Documentation",
		url = "https://github.com/seyi-react/seyi_bank_app"
	)
)
public class BankDemoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankDemoBackendApplication.class, args);
	}

}
