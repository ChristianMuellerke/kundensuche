package de.cmuellerke.kundenverwaltung.models;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

@Entity
@Table(name = "outputs", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"})
		})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputEntity {

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

	@Column(name = "tenant_id")
	private String tenantId;

	@CreatedDate
	@Column(name = "DATE_CREATED", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "DATE_MODIFIED")
	@LastModifiedDate
	private LocalDateTime modifiedAt;
}
