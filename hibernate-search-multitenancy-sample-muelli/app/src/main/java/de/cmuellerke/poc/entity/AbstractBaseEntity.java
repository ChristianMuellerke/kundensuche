package de.cmuellerke.poc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.TenantId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @KeywordField()
    private String tenantId;

    @CreatedDate
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "DATE_MODIFIED")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public AbstractBaseEntity(String tenantId) {
        log.debug("cr with tenantid {}", tenantId);
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
