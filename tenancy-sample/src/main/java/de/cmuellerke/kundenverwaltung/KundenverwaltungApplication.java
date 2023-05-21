package de.cmuellerke.kundenverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "de.cmuellerke.kundenverwaltung")
public class KundenverwaltungApplication {

	public static void main(String[] args) {
		SpringApplication.run(KundenverwaltungApplication.class, args);
	}

}
