package de.cmuellerke.demo.data.entity;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.cmuellerke.demo.data.entity.adresses.Adress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CUSTOMER")
@Builder
@Getter
@Setter
@NoArgsConstructor
public class Customer extends AbstractBaseEntity {

	@Builder
	public Customer(Long id, String firstName, String lastName, String tenantId, Set<Adress> adresses) {
		super(tenantId);
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.adresses = adresses;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "FIRSTNAME")
	@NotBlank(message = "Vorname muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Vorname muss zwischen 2 und 100 Zeichen lang sein")
	String firstName;

	@Column(name = "LASTNAME")
	@NotBlank(message = "Nachname muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Nachname muss zwischen 2 und 100 Zeichen lang sein")
	String lastName;

	@Column(name = "DAYOFBIRTH")
	LocalDate dayOfBirth;

	@OneToMany(fetch = FetchType.LAZY)
	Set<Adress> adresses;
}
