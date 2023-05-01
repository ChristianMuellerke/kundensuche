package de.cmuellerke.kundenverwaltung.security.services;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
class UserDetailsServiceImplTest implements WithAssertions {

	@Autowired
	private UserDetailsServiceImpl sut;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	void test() {
		TenantContext.setTenantId("T1");
		
		UserEntity userEntity = UserEntity.builder()//
				.email("cm@cm.de")
				.password("password1234")
				.username("username1")
				.build();
		
		userRepository.save(userEntity);
		
		UserDetails userDetails = sut.loadUserByUsername("username1");
		
		assertThat(userDetails.getUsername()).isEqualTo("username1");
		assertThat(userDetails.getPassword()).isEqualTo("password1234");
	}

}
