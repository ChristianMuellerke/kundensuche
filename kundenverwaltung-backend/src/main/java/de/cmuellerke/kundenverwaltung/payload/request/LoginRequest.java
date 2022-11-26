package de.cmuellerke.kundenverwaltung.payload.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import de.cmuellerke.kundenverwaltung.payload.response.JwtResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String tenantId;
}
