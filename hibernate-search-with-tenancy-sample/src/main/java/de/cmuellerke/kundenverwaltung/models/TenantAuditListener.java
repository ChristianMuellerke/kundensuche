package de.cmuellerke.kundenverwaltung.models;

import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantAuditListener {
	
	@PrePersist
    @PreUpdate
    @PreRemove
    private void logTenant(AbstractBaseEntity entityMetadata) {
        log.info("[PRE_PERSIST] : DB access using tenant " + entityMetadata.getTenantId() + " " + entityMetadata.getCreatedAt() + " for tenant " + TenantContext.getTenantInfo());
    }

	@PostPersist
    private void postPersist(AbstractBaseEntity entityMetadata) {
        log.info("[POST_PERSIST] : DB access using tenant " + entityMetadata.getTenantId() + " " + entityMetadata.getCreatedAt() + " for tenant " + TenantContext.getTenantInfo());
    }
	
    @PostLoad
    private void postLoad(AbstractBaseEntity entityMetadata) {
        log.info("[POSTLOAD] : DB access using tenant " + entityMetadata.getTenantId() + " " + entityMetadata.getCreatedAt() + " for tenant " + TenantContext.getTenantInfo());
    }

}