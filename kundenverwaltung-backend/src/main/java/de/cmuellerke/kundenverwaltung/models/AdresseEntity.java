package de.cmuellerke.kundenverwaltung.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "adressen", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "adresse_id"}),
		})
@Getter
@Setter
@NoArgsConstructor
public class AdresseEntity extends AbstractBaseEntity {
	@Id 
	@Column(name = "adresse_id", updatable = false, nullable = false)
	private UUID id = UUID.randomUUID();

	@NotBlank
	@Size(max = 40)
	private String strasse;
	
	@NotBlank
	@Size(max = 40)
	private String ort;
	
	@NotBlank
	@Size(max = 10)
	private String plz;

	@NotBlank
	@Size(max = 4)
	private String land;
	
	@Enumerated(EnumType.ORDINAL)
	private AdressTyp adressTyp;
}
