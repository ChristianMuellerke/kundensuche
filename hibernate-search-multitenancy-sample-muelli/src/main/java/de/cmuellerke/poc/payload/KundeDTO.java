package de.cmuellerke.poc.payload;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KundeDTO {
	private String id;
	private String vorname;
	private String nachname;
	private LocalDateTime geburtsdatum;
}
