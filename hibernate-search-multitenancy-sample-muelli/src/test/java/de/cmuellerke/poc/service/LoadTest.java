package de.cmuellerke.poc.service;


import de.cmuellerke.poc.payload.KundeDTO;
import de.cmuellerke.poc.tenancy.TenantContext;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
class LoadTest implements WithAssertions {

    @Autowired
    private KundenService kundenService;

    @Autowired
    private CustomerSearchService customerSearchService;

    @Test
    void testLoadingAndSearching() throws InterruptedException {
    	TenantContext.setTenantId(Testdata.TENANT_2);
    	List<Person> testPersonen = new Personengenerator().erzeugePersonen();

    	List<KundeDTO> anzulegendeKunden = new ArrayList<KundeDTO>();
    	
    	for (int i = 0; i < 5000; i++) {
    		Person person = testPersonen.get(i);

    		KundeDTO neuerKunde = KundeDTO.builder()
                    .vorname(person.getVorname()) //
                    .nachname(person.getNachname()) //
                    .build();
    		
    		anzulegendeKunden.add(neuerKunde);
    	}
    	
    	List<KundeDTO> gespeicherteKunden = kundenService.speichereKunden(anzulegendeKunden);
    	
    	// now search
    	
    }

}
