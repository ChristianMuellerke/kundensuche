package de.cmuellerke.poc.service;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.assertj.core.api.WithAssertions;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
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

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.repository.CustomerRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/*
 * In this test we prove the ability of handling 500 (!) tenants by hibernate search.
 */

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
@Disabled	
@DisplayName("Tests with many Tenants")
class CustomerSearchServiceMultiTenancyIntegrationTest implements WithAssertions {
	
	final static int TENANT_COUNT = 500;
	final static int CUSTOMERS_PER_TENANT_COUNT = 100;
	
    @Container
    static ElasticsearchContainer elasticsearch = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.11.4")//
    	.withPassword("test")
    	.waitingFor(new HttpWaitStrategy().forPort(9200).usingTls().allowInsecure().withBasicCredentials("elastic", "test"))
    	.withStartupTimeout(Duration.ofMinutes(5))
    	.withLogConsumer(new Slf4jLogConsumer(log));
	
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSearchService customerSearchService;
    
    @Autowired 
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;
    
    @DynamicPropertySource
    static void setupProperties(DynamicPropertyRegistry registry) {
    	log.info("Elastic is running under {}", elasticsearch.getHttpHostAddress());
    	log.info("Elastic is running under {}", elasticsearch.getFirstMappedPort());
    	log.info("Elastic is running under {}", elasticsearch.getPortBindings());
    	log.info("Elastic is running under {}", elasticsearch.getExposedPorts());
    	String adress = elasticsearch.getHost() + ":" + elasticsearch.getMappedPort(9200);
    	log.info("Elastic is running under {}", adress);
        registry.add("spring.jpa.properties.hibernate.search.backend.hosts", elasticsearch::getHttpHostAddress);
        registry.add("spring.jpa.properties.hibernate.search.multi_tenancy.tenant_ids", () -> getTestTenantsAsCSV());
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

	static String getTestTenantsAsCSV() {
		List<TenantDTO> testTenants = getTestTenants();
		List<@NonNull String> tenantIds = testTenants.stream().map(tenant -> tenant.getId()).toList();
		String csv = String.join(",", tenantIds);
		log.info("TestTenants: {}", csv);
		return csv;
	}
	
	static List<TenantDTO> getTestTenants() {
		List<TenantDTO> tenants = new ArrayList<>();
    	for (int i = 0; i <= TENANT_COUNT; i++) {
    		TenantDTO tenant = TenantDTO.builder().id("TestTenant_"+i).name("Test Tenant "+i).build();
    		tenants.add(tenant);
    	}
		return tenants;
	}
	
	@BeforeEach
	void init() {
		customerRepository.deleteAll();
	}
	
    @Test
    void testInfrastructure() {
    	assertThat(elasticsearch.isCreated()).isTrue();
    	assertThat(elasticsearch.isRunning()).isTrue();
    }
    
    @Test
    void canHandleManyTenants() throws InterruptedException {
    	List<TenantDTO> testTenants = getTestTenants();
    	
    	testTenants.forEach(tenant -> {
    		TenantContext.setTenantId(tenant.getId());

    		List<Person> testPersonen = new Personengenerator().erzeugePersonen();
        	List<CustomerDTO> customersToBeCreated = new ArrayList<CustomerDTO>();
        	
        	for (int i = 0; i < CUSTOMERS_PER_TENANT_COUNT; i++) {
        		Person person = testPersonen.get(i);

        		CustomerDTO newCustomer = CustomerDTO.builder()
                        .forename(person.getVorname()) //
                        .familyname(person.getNachname()) //
                        .build();
        		
        		customersToBeCreated.add(newCustomer);
        	}
        	
        	List<CustomerDTO> savedCustomers = customerService.save(customersToBeCreated);
        	log.info("[{}] saved {} customers", tenant.getId(), savedCustomers.size());
    	});
    }
}
