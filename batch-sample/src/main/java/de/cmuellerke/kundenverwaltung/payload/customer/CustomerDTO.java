package de.cmuellerke.kundenverwaltung.payload.customer;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
	private String id;
	private String vorname;
	private String nachname;
	private LocalDateTime geburtsdatum;
}
