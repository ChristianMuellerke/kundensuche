package de.cmuellerke.kundenverwaltung.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;
import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
class KundenServiceTest implements WithAssertions {

	@Autowired
	private KundenService kundenService;
	
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

		KundeDTO neuerKunde = createKunde("Christian", "Müllerke"); 

		KundeDTO gespeicherterKunde = kundenService.speichereKunde(neuerKunde);
		
		assertThat(gespeicherterKunde.getId()).isNotNull();
		assertThat(gespeicherterKunde.getTenantId()).isEqualTo(TENANT_1);
		assertThat(gespeicherterKunde.getVorname()).isEqualTo("Christian");
		assertThat(gespeicherterKunde.getNachname()).isEqualTo("Müllerke");
	}

	@Test
	void testTenancyFunktioniert() {
		TenantContext.setTenantId(TENANT_1);

		KundeDTO gespeicherterKundeTenant1 = kundenService.speichereKunde(createKunde("Christian", "Müllerke"));
		assertThat(gespeicherterKundeTenant1.getTenantId()).isEqualTo(TENANT_1);

		TenantContext.setTenantId(TENANT_2);
		KundeDTO gespeicherterKundeTenant2 = kundenService.speichereKunde(createKunde("Emma", "Meier"));
		assertThat(gespeicherterKundeTenant2.getTenantId()).isEqualTo(TENANT_2);
		
		TenantContext.setTenantId(TENANT_1);
		List<KundeEntity> alleKunden = kundenRepository.findAll();
		
		assertThat(alleKunden).hasSize(1);
	}

	
	private KundeDTO createKunde(String vorname, String nachname) {
		return KundeDTO.builder()
				.geburtsdatum(LocalDateTime.now()) //
				.vorname(vorname) //
				.nachname(nachname) //
				.build();
	}
	
	@Test
	void testMehrereKundenNeuAnlegen() {
		List<KundeDTO> kunden = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			KundeDTO neuerKunde = KundeDTO.builder()
					.geburtsdatum(LocalDateTime.now()) //
					.vorname("Christian_" + i) //
					.nachname("Muellerke_" + i) //
					.build();

			kunden.add(neuerKunde);
		}
		
		List<KundeDTO> gespeicherteKunden = kundenService.legeKundenAn(kunden);
	}
}
