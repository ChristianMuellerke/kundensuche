package de.cmuellerke.kundenverwaltung.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tenants", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id"}) 
		})
public class Tenant {
	@Id 
	@Column(name = "tenant_id", updatable = false, nullable = false)
	private UUID tenantId = UUID.randomUUID();
	
	@NotBlank
	@Size(max = 20)
	private String name;

	public Tenant() {
	}

	public Tenant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getTenantId() {
		return tenantId;
	}

	public void setTenantId(UUID tenantId) {
		this.tenantId = tenantId;
	}
}
