package de.cmuellerke.kundenverwaltung.tenancy;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class TenantListener {

	@PreUpdate
	@PreRemove
	@PrePersist
	public void setTenant(TenantAware entity) {
		final String tenantId = TenantContext.getTenantId();
		entity.setTenantId(tenantId);
	}
}