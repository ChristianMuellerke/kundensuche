package de.cmuellerke.kundenverwaltung.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import de.cmuellerke.kundenverwaltung.tenancy.TenantAware;
import de.cmuellerke.kundenverwaltung.tenancy.TenantListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = String.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@EntityListeners(TenantListener.class)
public abstract class AbstractBaseEntity implements TenantAware, Serializable {
	private static final long serialVersionUID = 1L;

	@Size(max = 30)
	@Column(name = "tenant_id")
	@NotBlank
	private String tenantId;

//	@CreatedDate
//	@Column(name = "DATE_CREATED", nullable = false, updatable = false)
//	private LocalDateTime createdAt;
//
//	@Column(name = "DATE_MODIFIED")
//	@LastModifiedDate
//	private LocalDateTime modifiedAt;

	public AbstractBaseEntity(String tenantId) {
		this.tenantId = tenantId;
	}
}
