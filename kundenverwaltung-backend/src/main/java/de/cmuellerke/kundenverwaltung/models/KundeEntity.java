package de.cmuellerke.kundenverwaltung.models;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "customer_id"}),
		@UniqueConstraint(columnNames = { "tenant_id", "email"}) 
		})
@Getter
@Setter
@NoArgsConstructor
public class KundeEntity extends AbstractBaseEntity {

    @Builder
    public KundeEntity(Long id, String vorname, String nachname, Instant geburtsdatum, String tenantId) {
        super(tenantId);
        this.customerId = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
    }
	
//	@Id 
//	@Column(name = "customer_id", updatable = false, nullable = false)
//	private UUID customerId = UUID.randomUUID();

    @Id
    @Column(name = "customer_id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long customerId;
    
	@NotBlank
	@Size(max = 40)
	private String vorname;

	@NotBlank
	@Size(max = 40)
	private String nachname;

	private Instant geburtsdatum;
	
	@Size(max = 50)
	@Email
	private String email;

	@Override
	public String toString() {
		return "[T=" + getTenantId() + " ID=" + customerId + " - " + vorname + " " + nachname + "]";
	}
	
//	@OneToMany
//	@JoinColumn(name = "adresse_id", referencedColumnName = "customer_id")
//	List<AdresseEntity> adressen; // TODO umstellen auf Set? FetchMode?
}
