package de.cmuellerke.kundenverwaltung.itest;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import de.cmuellerke.kundenverwaltung.WebIntegrationTest;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import lombok.extern.slf4j.Slf4j;

@WebIntegrationTest
@Slf4j
@AutoConfigureWebTestClient
public class KundenControllerIntegrationTest implements WithAssertions {
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private WebTestClient webTestClient;

    @BeforeEach
    public void setup() throws Exception {
    	webTestClient = MockMvcWebTestClient
    			.bindToApplicationContext(this.context)
    			.build();
    	
		log.debug("server configured...");
    }

    private static final String NEW_CUSTOMER_ENDPOINT = "/kunden/new";
    private static final String GET_CUSTOMER_ENDPOINT = "/kunden/";
    private static final String TENANT_1 = "tenant1";
    private static final String TENANT_2 = "tenant2";
    
    @Test
    void canSaveNewKunde() {

    	String tenantId = TENANT_1;
    	
    	KundeDTO kundeDTO = KundeDTO.builder()//
    			.vorname("c")//
    			.nachname("m")//
    			.build();
    	
		KundeDTO gespeicherterKunde = this.webTestClient//
				.post()//
				.uri(NEW_CUSTOMER_ENDPOINT)//
				.body(BodyInserters.fromValue(kundeDTO))//
				.header("X-TENANT-ID", tenantId) //
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(KundeDTO.class)//
				.returnResult()//
				.getResponseBody();	
		
		assertThat(gespeicherterKunde.getVorname()).isEqualTo("c");
		assertThat(gespeicherterKunde.getNachname()).isEqualTo("m");
		assertThat(gespeicherterKunde.getId()).isNotNull();
		assertThat(gespeicherterKunde.getTenantId()).isEqualTo(tenantId);
		
		log.info("Kunde mit Id {} gespeichert", gespeicherterKunde.getId());
		
		// Kunde aus DB lesen -> Tenant prüfen
		
		KundeDTO gelesenerKunde = this.webTestClient//
			.get() //
			.uri(GET_CUSTOMER_ENDPOINT + gespeicherterKunde.getId()) //
			.header("X-TENANT-ID", tenantId) //
			.accept(MediaType.APPLICATION_JSON)//
			.exchange()//
			.expectStatus().isOk() //
			.expectBody(KundeDTO.class)//
			.returnResult()//
			.getResponseBody();	
			
		assertThat(gelesenerKunde.getVorname()).isEqualTo("c");
		assertThat(gelesenerKunde.getNachname()).isEqualTo("m");
		assertThat(gelesenerKunde.getId()).isNotNull();
		assertThat(gelesenerKunde.getTenantId()).isEqualTo(tenantId);
    }
}
