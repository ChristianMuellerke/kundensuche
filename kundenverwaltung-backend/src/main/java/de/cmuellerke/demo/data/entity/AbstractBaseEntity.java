package de.cmuellerke.demo.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.cmuellerke.demo.tenancy.TenantAware;
import de.cmuellerke.demo.tenancy.TenantListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "tenantFilter", parameters = { @ParamDef(name = "tenantId", type = "string") })
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners({ TenantListener.class, AuditingEntityListener.class })
public abstract class AbstractBaseEntity implements TenantAware, Serializable {
	private static final long serialVersionUID = 1L;

	@Size(max = 30)
	@Column(name = "tenant_id", nullable = false)
	private String tenantId;

	public AbstractBaseEntity(String tenantId) {
		this.tenantId = tenantId;
	}

	@CreatedDate
	@Column(name = "DATE_CREATED", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "DATE_MODIFIED")
	@LastModifiedDate
	private LocalDateTime modifiedDate;
}
