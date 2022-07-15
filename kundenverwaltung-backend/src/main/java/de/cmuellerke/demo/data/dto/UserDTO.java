package de.cmuellerke.demo.data.dto;

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
public class UserDTO {
	long id;

	@NotBlank(message = "Benutzername muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Benutzername muss zwischen 2 und 100 Zeichen lang sein")
	String userName;

	@NotBlank(message = "Password muss gesetzt sein.")
	@Size(min = 12, max = 100, message = "Password muss zwischen 12 und 100 Zeichen lang sein")
	String password;
}
