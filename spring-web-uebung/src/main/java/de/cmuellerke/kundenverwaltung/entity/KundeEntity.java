package de.cmuellerke.kundenverwaltung.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customers", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"}),
		@UniqueConstraint(columnNames = { "tenant_id", "email"}) 
		})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KundeEntity extends AbstractBaseEntity {

	@Id 
	@Column(name = "customer_id", updatable = false, nullable = false)
	@Builder.Default
	private final UUID customerId = UUID.randomUUID();

	@NotBlank
	@Size(max = 40)
	private String name;

	@Size(max = 50)
	@Email
	private String email;
}
