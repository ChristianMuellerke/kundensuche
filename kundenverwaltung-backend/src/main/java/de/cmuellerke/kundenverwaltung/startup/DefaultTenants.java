package de.cmuellerke.kundenverwaltung.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.models.Role;
import de.cmuellerke.kundenverwaltung.models.Tenant;
import de.cmuellerke.kundenverwaltung.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultTenants implements CommandLineRunner {

	private TenantRepository tenantRepository;
	
	@Autowired
	public void DefaultTenants(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.debug("setting up default tenants");

		tenantRepository.save(new Tenant("Testbank 1"));
		tenantRepository.save(new Tenant("Testbank 2"));
		tenantRepository.save(new Tenant("Testbank 3"));
		tenantRepository.save(new Tenant("Testbank 4"));
		tenantRepository.save(new Tenant("Testbank 5"));
	}
}
