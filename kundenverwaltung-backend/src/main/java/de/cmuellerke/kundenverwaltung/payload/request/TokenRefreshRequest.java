package de.cmuellerke.kundenverwaltung.payload.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
public class TokenRefreshRequest {
	@NotBlank
	private String refreshToken;
	
//	@NotBlank
//	private String tenantId;
}
