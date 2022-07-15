package de.cmuellerke.demo.data.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {
	long id;

	@NotBlank(message = "Vorname muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Vorname muss zwischen 2 und 100 Zeichen lang sein")
	String firstName;

	@NotBlank(message = "Nachname muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Nachname muss zwischen 2 und 100 Zeichen lang sein")
	String lastName;

	LocalDate dayOfBirth;

}
