package de.cmuellerke.kundenverwaltung.service;

import java.time.LocalDateTime;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
		"spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
public class KundenRepositoryTest implements WithAssertions {
	@Autowired
	private KundenRepository kundenRepository;
	
	private static final String TENANT_1 = "tenant1";
	private static final String TENANT_2 = "tenant2";

	@AfterEach
	public void resetDB() {
		kundenRepository.deleteAll();
	}

	@Test
	void testKannEinenKundenSpeichern() {
		TenantContext.setTenantId(TENANT_1);
		KundeEntity saved1T1 = kundenRepository.save(createKunde("T1", "C1"));
		kundenRepository.save(createKunde("T1", "C2"));
		assertThat(kundenRepository.findAll()).hasSize(2);
		
		TenantContext.setTenantId(TENANT_2);
		kundenRepository.save(createKunde("T2", "C1")); 
		kundenRepository.save(createKunde("T2", "C2"));
		assertThat(kundenRepository.findAll()).hasSize(2);

		kundenRepository.deleteById(saved1T1.getCustomerId());
		
//		TenantContext.setTenantId(TENANT_1); 
//		kundenRepository.deleteAll();
//		assertThat(kundenRepository.findAll()).hasSize(2);
	}

	private KundeEntity createKunde(String vorname, String nachname) {
		return KundeEntity.builder()
				.vorname(vorname)
				.nachname(nachname)
				.build();
	}

}

