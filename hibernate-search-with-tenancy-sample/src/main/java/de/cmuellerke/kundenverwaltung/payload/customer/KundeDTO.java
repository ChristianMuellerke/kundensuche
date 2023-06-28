package de.cmuellerke.kundenverwaltung.payload.customer;

import jakarta.annotation.Nonnull;
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
public class KundeDTO {
	private String id;
	@Nonnull
	@NotBlank
	private String vorname;
	@Nonnull
	@NotBlank
	private String nachname;
	private String tenantId;
}
