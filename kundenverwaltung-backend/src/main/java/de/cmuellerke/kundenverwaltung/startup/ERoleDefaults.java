package de.cmuellerke.kundenverwaltung.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.models.Role;
import de.cmuellerke.kundenverwaltung.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ERoleDefaults implements CommandLineRunner {

	private RoleRepository roleRepository;
	
	@Autowired
	public void ERoleDefaults(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.debug("setting up roles");

		Role adminRole = new Role();
		adminRole.setName(ERole.ROLE_ADMIN);
		roleRepository.save(adminRole);
		
		Role userRole = new Role();
		userRole.setName(ERole.ROLE_USER);
		roleRepository.save(userRole);
	}
}
