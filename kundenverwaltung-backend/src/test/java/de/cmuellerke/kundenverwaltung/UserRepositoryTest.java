package de.cmuellerke.kundenverwaltung;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import de.cmuellerke.kundenverwaltung.tenancy.TenantIdentifierResolver;
import lombok.RequiredArgsConstructor;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
public class UserRepositoryTest implements WithAssertions {

	private static final String TENANT_1 = "t1";
	private static final String TENANT_2 = "t2";
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	void injectedComponentsAreNotNull() {
		assertThat(userRepository).isNotNull();
	}
	
	@Test
	void canSaveUser() {
		TenantContext.setTenantId(TENANT_1);
		
		UserEntity savedUser = userRepository.save(UserEntity.builder()
				.username("u1")
				.email("u1@unittest.de")
				.password("12345679")
				.build()
		);
		
		assertThat(savedUser.getTenantId()).isEqualTo(TENANT_1);
		
		Optional<UserEntity> userSearchResult = userRepository.findByUsername("u1");
		
		assertThat(userSearchResult).isNotEmpty();
	}
	
	@Test
	void canSaveUsersFromDifferentTenants() {
		TenantContext.setTenantId(TENANT_1);
		userRepository.deleteAll();

		UserEntity user = UserEntity.builder()
				.username("u1")
				.email("u1@unittest.de")
				.password("12345679")
				.build();
		UserEntity savedUser1 = userRepository.save(user);


		
		assertThat(savedUser1.getTenantId()).isEqualTo(TENANT_1);
		
		TenantContext.setTenantId(TENANT_2);

		UserEntity user2 = UserEntity.builder()
				.username("u1")
				.email("u1@unittest.de")
				.password("12345679")
				.build();

		
		UserEntity savedUser2 = userRepository.save(user2);
		assertThat(savedUser2.getTenantId()).isEqualTo(TENANT_2);

		// id is unique
		TenantContext.setTenantId(TENANT_1);
		Optional<UserEntity> userFromTenant1 = userRepository.findById(savedUser1.getId());
		assertThat(userFromTenant1).isPresent();
		assertThat(userFromTenant1.get().getTenantId()).isEqualTo(TENANT_1);

		TenantContext.setTenantId(TENANT_2);
		Optional<UserEntity> userFromTenant2 = userRepository.findById(savedUser2.getId());
		assertThat(userFromTenant2).isPresent();
		assertThat(userFromTenant2.get().getTenantId()).isEqualTo(TENANT_2);

		// name requires correct tenant context
		TenantContext.setTenantId(TENANT_1);
		userFromTenant1 = userRepository.findByUsername(savedUser1.getUsername());
		assertThat(userFromTenant1).isPresent();
		assertThat(userFromTenant1.get().getTenantId()).isEqualTo(TENANT_1);

		TenantContext.setTenantId(TENANT_2);
		userFromTenant2 = userRepository.findByUsername(savedUser2.getUsername());
		assertThat(userFromTenant2).isPresent();
		assertThat(userFromTenant2.get().getTenantId()).isEqualTo(TENANT_2);
	}

}
