package de.cmuellerke.kundenverwaltung.service;

import java.util.Optional;

import org.assertj.core.api.WithAssertions;
import org.hibernate.type.descriptor.JdbcBindingLogging;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionTemplate;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import de.cmuellerke.kundenverwaltung.tenancy.TenantIdentifierResolver;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
class KundenServiceTest implements WithAssertions {

	private static final String TENANT1 = "TEST1";
	private static final String TENANT2 = "TEST2";
	
	@Autowired
	private KundenService kundenService;
	
    @Autowired
    TransactionTemplate txTemplate;
    
    @Autowired
    TenantIdentifierResolver tenantIdentifierResolver;


	@Autowired
	private KundenRepository kundenRepository;
	
	// https://stackoverflow.com/questions/9419606/unit-testing-a-method-dependent-to-the-request-context	
	
//	@BeforeAll
//	static void init() {
//		TenantContext.setTenantId(TENANT1);
//	}
	@Test
	void testKannEinenKundenImRepoSpeichern() {
		KundeEntity gespeicherterKunde = createKundeEntity(TENANT1, "vn1", "nn1");
		assertThat(gespeicherterKunde.getTenantId()).isEqualTo(TENANT1);
		assertThat(gespeicherterKunde.getVorname()).isEqualTo("vn1");
		
		KundeEntity gespeicherterKunde2 = createKundeEntity(TENANT2, "vn2", "nn2");
		assertThat(gespeicherterKunde2.getTenantId()).isEqualTo(TENANT2);
		assertThat(gespeicherterKunde2.getVorname()).isEqualTo("vn2");
	}

    private KundeEntity createKundeEntity(String tenantId, String vorname, String nachname) {
    	System.out.println("TenantCtx=" + TenantContext.getTenantInfo());
        TenantContext.setTenantInfo(tenantId);

        KundeEntity kunde = txTemplate.execute(tx -> kundenRepository.save(new KundeEntity(vorname, nachname)));

        assertThat(kunde).isNotNull();
        assertThat(kunde.getCustomerId()).isNotNull();

        return kunde;
    }

	@Test
	void testResolver() {
		TenantContext.setTenantInfo(TENANT2);
		assertThat(tenantIdentifierResolver.resolveCurrentTenantIdentifier()).isEqualTo(TENANT2);
	}
	
//	funktioniert doch, nach der Transaktion (!) ist der Datensatz vorhanden und in der Datenbank steht auch die tenantid
//	vielleicht kommt das nur in der session nicht mit zurueck?
	
	@Test
	void testKannEinenKundenSpeichern() {
		//assertThat(JdbcBindingLogging.TRACE_ENABLED).isTrue();
		
		TenantContext.setTenantInfo(TENANT1);
		
		KundeDTO neuerKunde = KundeDTO.builder()
				.vorname("Christian") //
				.nachname("Muellerke") //
				.tenantId(TENANT1) //
				.build();
		
		KundeDTO gespeicherterKunde = kundenService.speichereKunde(neuerKunde);
		
		Optional<KundeDTO> gelesenerKunde = kundenService.getKunde(gespeicherterKunde.getId());

		assertThat(gelesenerKunde).isNotEmpty();
		assertThat(gelesenerKunde.get().getTenantId()).isEqualTo(TENANT1);

		
		assertThat(gespeicherterKunde.getId()).isNotNull();
		assertThat(gespeicherterKunde.getTenantId()).isEqualTo(TENANT1);
	}

//	@Test
//	void testMehrereKundenNeuAnlegen() {
//		List<KundeDTO> kunden = new ArrayList<>();
//
//		for (int i = 0; i < 10; i++) {
//			KundeDTO neuerKunde = KundeDTO.builder()
//					.vorname("Christian_" + i) //
//					.nachname("Muellerke_" + i) //
//					.tenantId(TENANT2) //
//					.build();
//			kunden.add(neuerKunde);
//		}
//		
//		List<KundeDTO> gespeicherteKunden = kundenService.legeKundenAn(kunden);
//		
//		gespeicherteKunden.stream().forEach(gespeicherterKunde -> {
//			assertThat(gespeicherterKunde.getId()).isNotNull();
//			assertThat(gespeicherterKunde.getTenantId()).isEqualTo(TENANT1);
//		});
//	}
}
