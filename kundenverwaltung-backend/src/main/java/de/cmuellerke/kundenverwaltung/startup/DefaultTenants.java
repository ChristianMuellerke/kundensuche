package de.cmuellerke.kundenverwaltung.startup;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.models.Role;
import de.cmuellerke.kundenverwaltung.models.Tenant;
import de.cmuellerke.kundenverwaltung.models.User;
import de.cmuellerke.kundenverwaltung.repository.TenantRepository;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultTenants implements CommandLineRunner {

	private TenantRepository tenantRepository;
	
	private UserRepository userRepository;
	
	private PasswordEncoder encoder;

	@Autowired
	public void DefaultTenants(TenantRepository tenantRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.tenantRepository = tenantRepository;
		this.userRepository = userRepository;
		this.encoder = passwordEncoder;
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.debug("setting up default tenants");

		tenantRepository.save(new Tenant("Testbank 1"));
		tenantRepository.save(new Tenant("Testbank 2"));
		tenantRepository.save(new Tenant("Testbank 3"));
		tenantRepository.save(new Tenant("Testbank 4"));
		tenantRepository.save(new Tenant("Testbank 5"));
		
		addDefaultUsers();
	}

	private void addDefaultUsers() {
		tenantRepository.findAll().forEach(tenant -> {
			TenantContext.setTenantId(tenant.getId().toString());
			userRepository.save(new User("user1", "user1@muellix.de", encoder.encode("11112222"), LocalDateTime.now()));
			userRepository.save(new User("user2", "user2@muellix.de", encoder.encode("11112222"), LocalDateTime.now()));
			userRepository.save(new User("user3", "user3@muellix.de", encoder.encode("11112222"), LocalDateTime.now()));
			
			log.debug("adding 100 testusers...to tenant " + tenant.getName());
			for (int i = 0; i < 100; i++) {
				userRepository.save(new User("Testbenutzer " + i, "testuser_" + i + "@muellix.de", encoder.encode("11112222"), LocalDateTime.now()));
			}
		});
		
	}
}
