package de.cmuellerke.kundenverwaltung.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = { 
		@UniqueConstraint(columnNames = { "tenant_id", "username"}),
		@UniqueConstraint(columnNames = { "tenant_id", "email"}) 
		})
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends AbstractBaseEntity {

    @Builder
    public UserEntity(Long id, String username, String email, String password, String tenantId) {
        super(tenantId);
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }
	
//	@Id 
//	@Column(name = "id", updatable = false, nullable = false)
//	private UUID id = UUID.randomUUID(); 

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    
	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
}
