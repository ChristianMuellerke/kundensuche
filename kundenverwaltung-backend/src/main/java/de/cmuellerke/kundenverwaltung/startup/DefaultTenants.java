package de.cmuellerke.kundenverwaltung.startup;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.repository.TenantRepository;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import de.cmuellerke.kundenverwaltung.tenancy.TenantIdentifierResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultTenants implements CommandLineRunner {

	private final TenantRepository tenantRepository;
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder encoder;
	
	@Override
	public void run(String... args) throws Exception {
		log.debug("setting up default tenants");

//		tenantRepository.save(new Tenant("Testbank 1"));
//		tenantRepository.save(new Tenant("Testbank 2"));
//		tenantRepository.save(new Tenant("Testbank 3"));
//		tenantRepository.save(new Tenant("Testbank 4"));
//		tenantRepository.save(new Tenant("Testbank 5"));
		
		addDefaultUsers();
	}

	private void addDefaultUsers() {
		tenantRepository.findAll().forEach(tenant -> {
			TenantContext.setTenantId(tenant.getId().toString());
			
			userRepository.save(UserEntity.builder()
					.username("user1")
					.email("user1@muellix.de")
					.password(encoder.encode("11112222"))
					.build()
			);

			userRepository.save(UserEntity.builder()
					.username("user2")
					.email("user2@muellix.de")
					.password(encoder.encode("11112222"))
					.build()
			);

			userRepository.save(UserEntity.builder()
					.username("user3")
					.email("user3@muellix.de")
					.password(encoder.encode("11112222"))
					.build()
			);

			
			log.debug("adding 2 testusers...to tenant " + tenant.getName());
			for (int i = 0; i < 2; i++) {
				userRepository.save(UserEntity.builder()
						.username("Testbenutzer " + i)
						.email("testuser_" + i + "@muellix.de")
						.password(encoder.encode("11112222"))
						.build()
				);
			}
		});
		
	}
}
