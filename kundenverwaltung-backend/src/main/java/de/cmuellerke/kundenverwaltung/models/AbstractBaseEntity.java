package de.cmuellerke.kundenverwaltung.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(TenantAuditListener.class)
@Slf4j
public abstract class AbstractBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "tenant_id")
	@TenantId
	private String tenantId;

	@CreatedDate
	@Column(name = "DATE_CREATED", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "DATE_MODIFIED")
	@LastModifiedDate
	private LocalDateTime modifiedAt;

	public AbstractBaseEntity(String tenantId) {
		this.tenantId = tenantId;
	}
	
	@PostLoad
    private void postLoad() {
    	log.debug("Loading for Tenant {}", tenantId);
    }
	
    @PreUpdate
    private void beforeAnyUpdate() {
    	setModifiedAt(LocalDateTime.now());
    }
    
    @PrePersist
    private void beforePersisting() {
    	setCreatedAt(LocalDateTime.now());
    	setModifiedAt(LocalDateTime.now());
    }
}
