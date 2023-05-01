package de.cmuellerke.kundenverwaltung.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestPropertySource(properties = {
        "spring.jpa.generate-ddl=true", "spring.jpa.show-sql=true" })
class KundenServiceTest implements WithAssertions {

	@Autowired
	private KundenService kundenService;
	
	@Test
	void testKannEinenKundenSpeichern() {
		KundeDTO neuerKunde = KundeDTO.builder()
				.geburtsdatum(LocalDateTime.now()) //
				.vorname("Christian") //
				.nachname("Muellerke") //
				.build();
		
		KundeDTO gespeicherterKunde = kundenService.speichereKunde(neuerKunde);
		
		assertThat(gespeicherterKunde.getId()).isNotNull();
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
