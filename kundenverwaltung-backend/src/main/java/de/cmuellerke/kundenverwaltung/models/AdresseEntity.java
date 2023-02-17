package de.cmuellerke.kundenverwaltung.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "adressen", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "adresse_id"}),
		})
public class AdresseEntity extends AbstractBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adresse_id")
	private Long id;

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
