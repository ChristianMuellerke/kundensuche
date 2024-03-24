package de.cmuellerke.poc.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenant_id"})
})
@Getter
@Setter
public class Tenant {
	
    public Tenant() {
	}

    public Tenant(String id2, String name2) {
		this.id = id2;
		this.name = name2;
	}

	@Id
    @Column(name = "tenant_id")
    private String id;

    @NotBlank
    @Size(max = 20)
    private String name;
}
