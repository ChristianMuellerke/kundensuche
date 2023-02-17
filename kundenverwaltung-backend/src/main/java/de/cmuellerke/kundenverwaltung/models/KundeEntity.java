package de.cmuellerke.kundenverwaltung.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"}),
		@UniqueConstraint(columnNames = { "tenant_id", "email"}) 
		})
@Getter
@Setter
public class KundeEntity extends AbstractBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;

	@NotBlank
	@Size(max = 40)
	private String vorname;

	@NotBlank
	@Size(max = 40)
	private String nachname;

	private Instant geburtsdatum;
	
	@Size(max = 50)
	@Email
	private String email;
	
	@OneToMany
	@JoinColumn(name = "adresse_id", referencedColumnName = "customer_id")
	List<AdresseEntity> adressen; // TODO umstellen auf Set? FetchMode?
}
