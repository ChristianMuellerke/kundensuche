package de.cmuellerke.demo.data.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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
@Table(name = "USERS", indexes = @Index(columnList = "USERNAME"))
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserDAO extends AbstractBaseEntity {

	@Builder
	public UserDAO(Long id, String userName, String tenantId) {
		super(tenantId);
		this.id = id;
		this.userName = userName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(name = "USERNAME", unique = true)
	@NotBlank(message = "Benutzername muss gesetzt sein.")
	@Size(min = 2, max = 100, message = "Benutzername muss zwischen 2 und 100 Zeichen lang sein")
	String userName;

	@Column(name = "PASSWORD")
	@Size(min = 2, max = 100, message = "Passwort muss zwischen 10 und 100 Zeichen lang sein")
	String password;
}
