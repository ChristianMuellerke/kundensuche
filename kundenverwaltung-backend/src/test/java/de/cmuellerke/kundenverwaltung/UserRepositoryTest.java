package de.cmuellerke.kundenverwaltung;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.models.User;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
public class UserRepositoryTest implements WithAssertions {

	private static final String TENANT_1 = "t1";
	private static final String TENANT_2 = "t2";
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void injectedComponentsAreNotNull() {
		assertThat(userRepository).isNotNull();
	}
	
	@Test
	void canSaveUser() {
		TenantContext.setTenantId(TENANT_1);
		User user = new User("u1", "u1@unittest.de", "12345679", LocalDateTime.now());
		User savedUser = userRepository.save(user);
		
		assertThat(savedUser.getTenantId()).isEqualTo(TENANT_1);
	}
	
	@Test
	void canSaveUsersFromDifferentTenants() {
		TenantContext.setTenantId(TENANT_1);
		userRepository.deleteAll();
		User user = new User("u1", "u1@unittest.de", "12345679", LocalDateTime.now());
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getTenantId()).isEqualTo(TENANT_1);
		
		TenantContext.setTenantId(TENANT_2);
		User user2 = new User("u1", "u1@unittest.de", "12345679", LocalDateTime.now());
		User savedUser2 = userRepository.save(user2);
		assertThat(savedUser2.getTenantId()).isEqualTo(TENANT_2);

		// id is unique
		Optional<User> userFromTenant1 = userRepository.findById(savedUser.getId());
		assertThat(userFromTenant1).isPresent();
		assertThat(userFromTenant1.get().getTenantId()).isEqualTo(TENANT_1);

		Optional<User> userFromTenant2 = userRepository.findById(savedUser2.getId());
		assertThat(userFromTenant2).isPresent();
		assertThat(userFromTenant2.get().getTenantId()).isEqualTo(TENANT_2);

		// name requires correct tenant context
		userFromTenant1 = userRepository.findByUsernameAndTenantId(savedUser.getUsername(), TENANT_1);
		assertThat(userFromTenant1).isPresent();
		assertThat(userFromTenant1.get().getTenantId()).isEqualTo(TENANT_1);

		userFromTenant2 = userRepository.findByUsernameAndTenantId(savedUser2.getUsername(), TENANT_2);
		assertThat(userFromTenant2).isPresent();
		assertThat(userFromTenant2.get().getTenantId()).isEqualTo(TENANT_2);
	}

}
