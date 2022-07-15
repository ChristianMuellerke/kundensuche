package de.cmuellerke.demo.data.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.cmuellerke.demo.data.entity.adresses.Adress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TENANT")
@Builder
@Getter
@Setter
@NoArgsConstructor
public class Tenant {
	
	@Builder
	public Tenant(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "NAME")
	@NotBlank(message = "Name des Tenants muss vorhanden sein")
	@Size(min = 2, max = 100, message = "Name muss zwischen 2 und 100 Zeichen lang sein")
	String name;
}
