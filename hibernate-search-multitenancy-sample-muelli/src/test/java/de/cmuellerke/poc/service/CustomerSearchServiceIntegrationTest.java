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

import de.cmuellerke.poc.payload.CustomerDTO;
import de.cmuellerke.poc.payload.PageDTO;
import de.cmuellerke.poc.payload.PageableSearchRequestDTO;
import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.repository.CustomerRepository;
import de.cmuellerke.poc.repository.TenantRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

/*
 * https://localhost:32827/_cat/indices
 * Username: elastic, Password: test
 */

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
class CustomerSearchServiceIntegrationTest implements WithAssertions {
	
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
    
    @Autowired 
    private TenantService tenantService;

    @Autowired 
    private TenantRepository tenantRepository;

    
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
    @DisplayName("Customer is saved/loaded with a tenant context")
    void testCanSaveCustomerOnTenant() {
        TenantContext.setTenantId(Testdata.TENANT_1.getId());

        CustomerDTO newCustomer = Testdata.CUSTOMER_1;
        
        CustomerDTO savedCustomer = customerService.save(newCustomer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getForename()).isEqualTo("Muelli");
        assertThat(savedCustomer.getFamilyname()).isEqualTo("Muellerke");

        Optional<CustomerDTO> foundCustomer = customerService.find(savedCustomer.getId());
        assertThat(foundCustomer).isNotEmpty();
        assertThat(foundCustomer.get().getId()).isEqualTo(savedCustomer.getId());

        TenantContext.setTenantId(Testdata.TENANT_2.getId());
        Optional<CustomerDTO> savedCustomerOnOtherTenant = customerService.find(savedCustomer.getId());
        assertThat(savedCustomerOnOtherTenant).isEmpty();
    }

    @Test
    void testCanSaveCustomerOnTenantAndCanSearchFor() throws InterruptedException {
        TenantContext.setTenantId(Testdata.TENANT_2.getId());

        CustomerDTO newCustomer = Testdata.CUSTOMER_1;
        
        CustomerDTO savedCustomer = customerService.save(newCustomer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getForename()).isEqualTo("Muelli");
        assertThat(savedCustomer.getFamilyname()).isEqualTo("Muellerke");

        Awaitility.await().atMost(Duration.of(10, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	
            List<CustomerDTO> customersFound = customerSearchService.findByName("Muellerke");
            if (!customersFound.isEmpty()) {
            	assertThat(customersFound.get(0).getFamilyname()).isEqualTo("Muellerke");
            	assertThat(customersFound.get(0).getTenantId()).isEqualTo(Testdata.TENANT_2.getId());
            	return true;
            }
            
            return false;
        });
        
        // search for this customer on other tenant
        TenantContext.setTenantId(Testdata.TENANT_3.getId());
        List<CustomerDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }

    @Test
	@DisplayName("Supports SearchAsYouType for full names")
    void testSearchAsYouTypeByFullname() throws InterruptedException {
    	TenantDTO testTenant = Testdata.TENANT_2;
        TenantContext.setTenantId(testTenant.getId());

        CustomerDTO newCustomer = Testdata.CUSTOMER_1;

        CustomerDTO savedCustomer = customerService.save(newCustomer);

        Awaitility.await().atMost(Duration.of(20, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	
            List<CustomerDTO> customersFound = customerSearchService.searchAsYouType("Muel");
            if (!customersFound.isEmpty()) {
            	assertThat(customersFound.get(0).getFamilyname()).isEqualTo("Muellerke");
            	assertThat(customersFound.get(0).getTenantId()).isEqualTo(testTenant.getId());
            	return true;
            }
            
            return false;
        });
        
        // search for this customer on other tenant
        TenantContext.setTenantId(Testdata.TENANT_3.getId());
        List<CustomerDTO> customersFoundForTenant3 = customerSearchService.findByName("Muellerke");
        assertThat(customersFoundForTenant3).isEmpty();
    }
    
    @Test
	@DisplayName("can save 5000 customers and search for them")
    void testLoadingAndSearching() throws InterruptedException {
    	TenantDTO testTenant = Testdata.TENANT_2;
    	TenantContext.setTenantId(testTenant.getId());
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
    	
        Awaitility.await().atMost(Duration.of(200, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	
        	PageableSearchRequestDTO pageableSearchRequest = PageableSearchRequestDTO.builder()
        			.forename(savedCustomers.get(0).getForename())
        			.familyname(savedCustomers.get(0).getFamilyname())
        			.fullname(null)
        			.pageOffset(0)
        			.limit(10)
        			.build();

        	PageDTO<CustomerDTO> resultPage = customerSearchService.findBy(pageableSearchRequest);
        	
        	if (!resultPage.getContent().isEmpty()) {
        		assertThat(resultPage.getContent().get(0).getForename()).isEqualTo(savedCustomers.get(0).getForename());
            	assertThat(resultPage.getContent().get(0).getFamilyname()).isEqualTo(savedCustomers.get(0).getFamilyname());
            	assertThat(resultPage.getContent().get(0).getTenantId()).isEqualTo(testTenant.getId());
            	
            	log.info("Customer ID={}/ DocumentId=NIL", resultPage.getContent().get(0).getId());

            	return true;
        	}
            
            return false;
        });
    }
    
    @Test
	@DisplayName("pageable search results work")
    void testPageableSearch() throws InterruptedException {
    	TenantDTO testTenant = Testdata.TENANT_2;
        TenantContext.setTenantId(testTenant.getId());
        customerService.save(Testdata.CUSTOMER_1);

        Awaitility.await().atMost(Duration.of(200, ChronoUnit.SECONDS)).until(() -> {
        	log.info("polling...");
        	
        	PageableSearchRequestDTO pageableSearchRequest = PageableSearchRequestDTO.builder()
        			.forename("Muelli")
        			.familyname(null)
        			.fullname(null)
        			.pageOffset(0)
        			.limit(10)
        			.build();

        	PageDTO<CustomerDTO> resultPage = customerSearchService.findBy(pageableSearchRequest);
        	
        	if (!resultPage.getContent().isEmpty()) {
        		assertThat(resultPage.getContent().get(0).getForename()).isEqualTo("Muelli");
            	assertThat(resultPage.getContent().get(0).getFamilyname()).isEqualTo("Muellerke");
            	assertThat(resultPage.getContent().get(0).getTenantId()).isEqualTo(testTenant.getId());
            	assertThat(resultPage.getTotal()).isEqualTo(1);
            	
            	log.info("Customer ID={}/ DocumentId=NIL", resultPage.getContent().get(0).getId());

            	return true;
        	}
            
            return false;
        });
        
        // now search by abbrevation
    	PageableSearchRequestDTO pageableSearchRequest = PageableSearchRequestDTO.builder()
    			.forename("Mue")
    			.familyname(null)
    			.fullname(null)
    			.pageOffset(0)
    			.limit(10)
    			.build();
        
        PageDTO<CustomerDTO> resultPage = customerSearchService.findBy(pageableSearchRequest);

        assertThat(resultPage.getContent()).isNotEmpty();
        assertThat(resultPage.getContent().get(0).getForename()).isEqualTo("Muelli");
    	assertThat(resultPage.getContent().get(0).getFamilyname()).isEqualTo("Muellerke");
    	assertThat(resultPage.getContent().get(0).getTenantId()).isEqualTo(testTenant.getId());
    	assertThat(resultPage.getTotal()).isEqualTo(1);

    }

//	private void check() throws IOException {
//		ElasticsearchBackend elasticBackend = Search
//				.mapping(entityManager.getEntityManagerFactory())
//				.backend()
//				.unwrap(ElasticsearchBackend.class);
//		
//		RestClient restClient = elasticBackend.client(RestClient.class);
//		
//		Response response = restClient.performRequest( new Request( "GET", "/" ) );
//		assertThat( response.getStatusLine().getStatusCode() ).isEqualTo( 200 );
//	}

	
    @Test
	@DisplayName("can save and get a tenant")
    void tenantsCanBeSavedByTenantService() throws InterruptedException {
    	// GIVEN
    	TenantDTO testTenant = Testdata.TENANT_1;
		// WHEN
    	TenantDTO savedTenant = tenantService.createOrUpdate(testTenant);
    	// THEN
    	assertThat(savedTenant.getId()).isEqualTo(testTenant.getId());
    	assertThat(savedTenant.getName()).isEqualTo(testTenant.getName());
    }

    @Test
	@DisplayName("can save and then update a tenant")
    void tenantsCanBeUpdatedByTenantService() throws InterruptedException {
    	// GIVEN
    	TenantDTO testTenant = Testdata.TENANT_2;
    	TenantDTO savedTenant = tenantService.createOrUpdate(testTenant);
    	assertThat(savedTenant.getName()).isEqualTo(testTenant.getName());
		// WHEN
    	savedTenant.setName("new name");
    	TenantDTO updatedTenant = tenantService.createOrUpdate(savedTenant);
    	// THEN
    	assertThat(updatedTenant.getId()).isEqualTo(testTenant.getId());
    	assertThat(updatedTenant.getName()).isEqualTo("new name");
    }

}
