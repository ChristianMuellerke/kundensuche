package de.cmuellerke.poc.service;


import de.cmuellerke.poc.payload.CustomerDTO;
import de.cmuellerke.poc.tenancy.TenantContext;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@Disabled
class OldIntegrationTest implements WithAssertions {

    @Autowired
    private CustomerService kundenService;

    @Autowired
    private CustomerSearchService customerSearchService;

    @Test
    void testKannEinenKundenAmTenantSpeichern() {
        TenantContext.setTenantId(Testdata.TENANT_1.getId());

        CustomerDTO neuerKunde = CustomerDTO.builder()
                .forename("Christian") //
                .familyname("Muellerke") //
                .build();

        CustomerDTO gespeicherterKunde = kundenService.save(neuerKunde);

        assertThat(gespeicherterKunde.getId()).isNotNull();
        assertThat(gespeicherterKunde.getForename()).isEqualTo("Christian");
        assertThat(gespeicherterKunde.getFamilyname()).isEqualTo("Muellerke");

        Optional<CustomerDTO> gelesenerKunde = kundenService.find(gespeicherterKunde.getId());
        assertThat(gelesenerKunde).isNotEmpty();
        assertThat(gelesenerKunde.get().getId()).isEqualTo(gespeicherterKunde.getId());

        TenantContext.setTenantId(Testdata.TENANT_2.getId());
        Optional<CustomerDTO> gelesenerKundeAndererTenant = kundenService.find(gespeicherterKunde.getId());
        assertThat(gelesenerKundeAndererTenant).isEmpty();
    }

    @Test
    void testKannEinenKundenAmTenant2_Speichern_UndDiesenDanachFinden() throws InterruptedException {
        TenantContext.setTenantId(Testdata.TENANT_2.getId());

        CustomerDTO neuerKunde = CustomerDTO.builder()
                .forename("Muelli") //
                .familyname("Muellerke") //
                .build();

        CustomerDTO gespeicherterKunde = kundenService.save(neuerKunde);

        assertThat(gespeicherterKunde.getId()).isNotNull();
        assertThat(gespeicherterKunde.getForename()).isEqualTo("Muelli");
        assertThat(gespeicherterKunde.getFamilyname()).isEqualTo("Muellerke");

        Thread.sleep(500);
        
        // suche nach diesem Kunden
        List<CustomerDTO> customersFound = customerSearchService.findByName("Muellerke");
        assertThat(customersFound).isNotEmpty();
        assertThat(customersFound.get(0).getFamilyname()).isEqualTo("Muellerke");

        // suche nach diesem Kunden, anderer Tenant
        TenantContext.setTenantId(Testdata.TENANT_3.getId());
        List<CustomerDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }

}
