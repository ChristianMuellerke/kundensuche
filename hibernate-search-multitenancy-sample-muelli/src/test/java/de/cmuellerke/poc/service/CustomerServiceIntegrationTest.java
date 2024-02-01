package de.cmuellerke.poc.service;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.assertj.core.api.WithAssertions;
import org.awaitility.Awaitility;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurer;
import org.junit.jupiter.api.BeforeEach;
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

import de.cmuellerke.poc.payload.CustomerDTO;
import de.cmuellerke.poc.repository.CustomerRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
class CustomerServiceIntegrationTest implements WithAssertions {

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
    private CustomerService customerService;

    @Autowired
    private CustomerSearchService customerSearchService;
    
    @Autowired 
    private CustomerRepository customerRepository;

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
		customerRepository.deleteAll();
	}
	
    @Test
    void testInfrastructure() {
    	assertThat(elasticsearch.isCreated()).isTrue();
    	assertThat(elasticsearch.isRunning()).isTrue();
    }
    
    @Test
    void testCanSaveCustomerOnTenant() {
        TenantContext.setTenantId(Testdata.TENANT_1);

        CustomerDTO newCustomer = CustomerDTO.builder()
                .forename("Christian") //
                .familyname("Muellerke") //
                .build();

        CustomerDTO savedCustomer = customerService.save(newCustomer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getForename()).isEqualTo("Christian");
        assertThat(savedCustomer.getFamilyname()).isEqualTo("Muellerke");

        Optional<CustomerDTO> foundCustomer = customerService.find(savedCustomer.getId());
        assertThat(foundCustomer).isNotEmpty();
        assertThat(foundCustomer.get().getId()).isEqualTo(savedCustomer.getId());

        TenantContext.setTenantId(Testdata.TENANT_2);
        Optional<CustomerDTO> savedCustomerOnOtherTenant = customerService.find(savedCustomer.getId());
        assertThat(savedCustomerOnOtherTenant).isEmpty();
    }

    @Test
    void testCanSaveCustomerOnTenantAndCanSearchFor() throws InterruptedException {
        TenantContext.setTenantId(Testdata.TENANT_2);

        CustomerDTO newCustomer = CustomerDTO.builder()
                .forename("Muelli") //
                .familyname("Muellerke") //
                .build();

        CustomerDTO savedCustomer = customerService.save(newCustomer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getForename()).isEqualTo("Muelli");
        assertThat(savedCustomer.getFamilyname()).isEqualTo("Muellerke");

        Awaitility.await().atMost(Duration.of(10, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	
            List<CustomerDTO> customersFound = customerSearchService.findByName("Muellerke");
            if (!customersFound.isEmpty()) {
            	assertThat(customersFound.get(0).getFamilyname()).isEqualTo("Muellerke");
            	assertThat(customersFound.get(0).getTenantId()).isEqualTo(Testdata.TENANT_2);
            	return true;
            }
            
            return false;
        });
        
        // search for this customer on other tenant
        TenantContext.setTenantId(Testdata.TENANT_3);
        List<CustomerDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }

    @Test
    void testLoadingAndSearching() throws InterruptedException {
    	TenantContext.setTenantId(Testdata.TENANT_2);
    	List<Person> testPersonen = new Personengenerator().erzeugePersonen();

    	List<CustomerDTO> customersToBeCreated = new ArrayList<CustomerDTO>();
    	
    	for (int i = 0; i < 5000; i++) {
    		Person person = testPersonen.get(i);

    		CustomerDTO newCustomer = CustomerDTO.builder()
                    .forename(person.getVorname()) //
                    .familyname(person.getNachname()) //
                    .build();
    		
    		customersToBeCreated.add(newCustomer);
    	}
    	
    	List<CustomerDTO> savedCustomers = customerService.save(customersToBeCreated);
    	
    	// now search TODO
    	
    }

    
    /*
     * Weitere Testmethoden
     * 
     * insert 100 customers in every tenant
     * do a mass index on all
     * 
     * TODO: 
     * refactor to english    						-> done
     * evaluate tenants from outside
     * restart on tenant list changes?
     * search suggestion
     * search-as-you-type
     * implement web frontend
     * paging!
     */
    
    
}
