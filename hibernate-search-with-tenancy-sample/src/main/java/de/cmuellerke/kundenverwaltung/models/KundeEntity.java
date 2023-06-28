package de.cmuellerke.kundenverwaltung.models;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.TenantId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"}),
		})
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KundeEntity extends AbstractBaseEntity {

	@Id 
	@Column(name = "customer_id", updatable = false, nullable = false)
	private final UUID customerId = UUID.randomUUID();

	@NotBlank
	@Size(max = 40)
	private String vorname;

	@NotBlank
	@Size(max = 40)
	private String nachname;
	
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof KundeEntity otherKundeEntity)) {
            return false;
        }
        return customerId != null && customerId.equals(otherKundeEntity.customerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}