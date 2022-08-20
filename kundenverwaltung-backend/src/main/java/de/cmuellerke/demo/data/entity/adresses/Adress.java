package de.cmuellerke.demo.data.entity.adresses;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import de.cmuellerke.demo.data.entity.AbstractBaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ADRESSES")
//@Builder
@Getter
@Setter
@NoArgsConstructor
public class Adress extends AbstractBaseEntity {

//	public Adress(Long id, AdressType type, String city, String street, String postalcode, String tenantId) {
//		super(tenantId);
//		this.id = id;
//		this.type = type;
//		this.city = city;
//		this.street = street;
//		this.postalcode = postalcode;
//		this.setTenantId(tenantId);
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Enumerated(EnumType.STRING)
	private AdressType type;

	@Column(name = "STREET")
	@Size(max = 140)
	String street;

	@Column(name = "CITY")
	@Size(max = 42)
	String city;

	@Column(name = "POSTALCODE")
	@Size(max = 5)
	String postalcode;
}
