package de.cmuellerke.poc.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "customers", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"})
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
	private String vorname;

	@NotBlank
	@Size(max = 40)
	private String nachname;
	
}
