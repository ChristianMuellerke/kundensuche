package de.cmuellerke.poc.service;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.util.Arrays;
import org.awaitility.Awaitility;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import de.cmuellerke.poc.payload.KundeDTO;
import de.cmuellerke.poc.repository.KundenRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
class KundenServiceTest implements WithAssertions {

	/*
	 * Letzter Stand: ich wollte den Elastic Stack als Testcontainer laufen lassen.
	 * 
	 * Der Container ist da und man kann auch mit ihm kommunizieren. Aber unser 
	 * hibernate-search lässt sich momentan noch nicht auf den dynamischen Port mappen
	 * 
	 * 
	 * https://localhost:32827/_cat/indices
	 * 
	 * Username: elastic, Password: test
	 *
	 * 
	 * Zertifikatsgedöns:
	 * 
	 * https://github.com/testcontainers/testcontainers-java/blob/main/modules/elasticsearch/src/test/java/org/testcontainers/elasticsearch/ElasticsearchContainerTest.java#L315
	 */
	
    @Container
    static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.11.4")//

    	.withPassword("test")
    	.waitingFor(new HttpWaitStrategy().forPort(9200).usingTls().allowInsecure().withBasicCredentials("elastic", "test"))
    	.withStartupTimeout(Duration.ofMinutes(5))
    	.withLogConsumer(new Slf4jLogConsumer(log));
	
    @Autowired
    private KundenService kundenService;

    @Autowired
    private CustomerSearchService customerSearchService;
    
    @Autowired 
    private KundenRepository kundenRepository;

    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
    	log.info("Elastic is running under {}", elasticsearch.getHttpHostAddress());
    	log.info("Elastic is running under {}", elasticsearch.getFirstMappedPort());
    	log.info("Elastic is running under {}", elasticsearch.getPortBindings());
    	log.info("Elastic is running under {}", elasticsearch.getExposedPorts());
    	String adress = elasticsearch.getHost() + ":" + elasticsearch.getMappedPort(9200);
    	log.info("Elastic is running under {}", adress);
        registry.add("spring.jpa.properties.hibernate.search.backend.hosts", elasticsearch::getHttpHostAddress);
    }
    
	@TestConfiguration
	@Slf4j
	static class SSL {
		/**
		 * see application-test profile, setting hibernate.search.backend.client.configurer
		 * @return
		 */
		@Bean
		ElasticsearchHttpClientConfigurer customizer() {
			log.info("Configuring SSL Context");
			return new ElasticsearchHttpClientConfigurer() {
				@Override
				public void configure(ElasticsearchHttpClientConfigurationContext context) {
					HttpAsyncClientBuilder clientBuilder = context.clientBuilder();
					clientBuilder.setSSLContext(elasticsearch.createSslContextFromCa());
				}
			};
		}
	}

	@BeforeEach
	void init() {
		kundenRepository.deleteAll();
	}
	
    @Test
    void testInfrastructure() {
    	assertThat(elasticsearch.isCreated()).isTrue();
    	assertThat(elasticsearch.isRunning()).isTrue();
    }
    
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

        // TODO: is there a better way? its asynchronously, but this is very ugly
        // maybe using awaitaily?
        
        Awaitility.await().atMost(Duration.of(10, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	return !customerSearchService.findByName("Muellerke").isEmpty();
        });
        
        // suche nach diesem Kunden TODO: muss das in den await am besten mit rein?
        List<KundeDTO> customersFound = customerSearchService.findByName("Muellerke");
        assertThat(customersFound).isNotEmpty();
        assertThat(customersFound.get(0).getNachname()).isEqualTo("Muellerke");
        assertThat(customersFound.get(0).getTenantId()).isEqualTo(Testdata.TENANT_2);
        

        // suche nach diesem Kunden, anderer Tenant
        TenantContext.setTenantId(Testdata.TENANT_3);
        List<KundeDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }

    /*
     * Weitere Testmethoden
     * 
     * insert 100 customers in every tenant
     * do a mass index on all
     * 
     * TODO: 
     * refactor to english
     * evaluate tenants from outside
     * restart on tenant list changes?
     * search suggestion
     * search-as-you-type
     * implement web frontend
     * paging!
     */
    
    
}
