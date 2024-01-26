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

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
class KundenServiceIntegrationTest implements WithAssertions {

    @Autowired
    private KundenService kundenService;

    @Autowired
    private CustomerSearchService customerSearchService;

    @Test
    void testKannEinenKundenAmTenantSpeichern() {
        TenantContext.setTenantId(Testdata.TENANT_1);

        KundeDTO neuerKunde = KundeDTO.builder()
                .vorname("Christian") //
                .nachname("Muellerke") //
                .build();

        KundeDTO gespeicherterKunde = kundenService.speichereKunde(neuerKunde);

        assertThat(gespeicherterKunde.getId()).isNotNull();
        assertThat(gespeicherterKunde.getVorname()).isEqualTo("Christian");
        assertThat(gespeicherterKunde.getNachname()).isEqualTo("Muellerke");

        Optional<KundeDTO> gelesenerKunde = kundenService.getKunde(gespeicherterKunde.getId());
        assertThat(gelesenerKunde).isNotEmpty();
        assertThat(gelesenerKunde.get().getId()).isEqualTo(gespeicherterKunde.getId());

        TenantContext.setTenantId(Testdata.TENANT_2);
        Optional<KundeDTO> gelesenerKundeAndererTenant = kundenService.getKunde(gespeicherterKunde.getId());
        assertThat(gelesenerKundeAndererTenant).isEmpty();
    }

    @Test
    void testKannEinenKundenAmTenant2_Speichern_UndDiesenDanachFinden() throws InterruptedException {
        TenantContext.setTenantId(Testdata.TENANT_2);

        KundeDTO neuerKunde = KundeDTO.builder()
                .vorname("Muelli") //
                .nachname("Muellerke") //
                .build();

        KundeDTO gespeicherterKunde = kundenService.speichereKunde(neuerKunde);

        assertThat(gespeicherterKunde.getId()).isNotNull();
        assertThat(gespeicherterKunde.getVorname()).isEqualTo("Muelli");
        assertThat(gespeicherterKunde.getNachname()).isEqualTo("Muellerke");

        Thread.sleep(500);
        
        // suche nach diesem Kunden
        List<KundeDTO> customersFound = customerSearchService.findByName("Muellerke");
        assertThat(customersFound).isNotEmpty();
        assertThat(customersFound.get(0).getNachname()).isEqualTo("Muellerke");

        // suche nach diesem Kunden, anderer Tenant
        TenantContext.setTenantId(Testdata.TENANT_3);
        List<KundeDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }

}
