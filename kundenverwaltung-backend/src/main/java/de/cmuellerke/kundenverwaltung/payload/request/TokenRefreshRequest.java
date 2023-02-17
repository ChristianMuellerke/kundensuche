package de.cmuellerke.kundenverwaltung.payload.request;

import jakarta.validation.constraints.NotBlank;
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
