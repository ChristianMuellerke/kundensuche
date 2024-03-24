package de.cmuellerke.poc.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.hibernate.search.backend.elasticsearch.ElasticsearchBackend;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.backend.common.DocumentReference;
import org.hibernate.search.engine.search.predicate.dsl.SimpleBooleanPredicateClausesStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.cmuellerke.poc.entity.CustomerEntity;
import de.cmuellerke.poc.payload.PageDTO;
import de.cmuellerke.poc.payload.PageableSearchRequestDTO;
import de.cmuellerke.poc.payload.SearchRequestDTO;
import de.cmuellerke.poc.tenancy.TenantContext;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class CustomerSearchService {

    private final CustomerService customerService;

    private final EntityManager entityManager;

    @Transactional(readOnly=true)
    public List<CustomerDTO> findByName(String familyname) {
        SearchSession searchSession = Search.session(entityManager);

        String tenantId = TenantContext.getTenantId();

        log.info("{} Searching for {}", tenantId, familyname);

        SearchResult<CustomerEntity> searchResult = searchSession.search(CustomerEntity.class)
        		.where(f -> f.and()
	        		.add(f.match().field("familyname").matching(familyname))
        		)
        		.fetchAll();

        return searchResult.hits().stream().map(customerService::toCustomerDTO).toList();
    }

    @Transactional(readOnly=true)
    public PageDTO<CustomerDTO> findBy(PageableSearchRequestDTO searchRequest) {
        SearchSession searchSession = Search.session(entityManager);

        String tenantId = TenantContext.getTenantId();

        log.info("{} Searching for {}", tenantId, searchRequest);

        /*
         * TODO
         * 
         * seems that we can`t search by prefix, eg. Muel does not find Muelli -> we have to investigate more here
         */
        
        SearchResult<CustomerEntity> searchResult = searchSession.search(CustomerEntity.class)
        		.where(f -> { 
        			return f.match().field("forename").matching(searchRequest.getForename());
        		})
        		.fetch(searchRequest.getPageOffset(), searchRequest.getLimit());

        List<CustomerDTO> foundCustomerDTOs = searchResult.hits().stream().map(customerService::toCustomerDTO).toList();
        
        SearchResult<DocumentReference> searchResult2 = searchSession.search(CustomerEntity.class)
        		.select(f -> f.documentReference())
        		.where(f -> { 
        			return f.match().field("forename").matching(searchRequest.getForename());
        		})
        		.fetchAll();

        if (!searchResult2.hits().isEmpty()) {
    		ElasticsearchBackend elasticBackend = Search
    				.mapping(entityManager.getEntityManagerFactory())
    				.backend()
    				.unwrap(ElasticsearchBackend.class);
    		
    		RestClient restClient = elasticBackend.client(RestClient.class);
    		
    		try {
				Response response = restClient.performRequest( new Request( "GET", "/_cat/indices" ) );
				byte[] bytes = response.getEntity().getContent().readAllBytes();
				String s = new String(bytes, StandardCharsets.UTF_8);
				log.info("indizes: {}", s);
			
				// customerentity-000001
				
				response = restClient.performRequest( new Request( "GET", "/customerentity-000001/_termvectors/t2_" + searchResult2.hits().get(0).id()));
				bytes = response.getEntity().getContent().readAllBytes();
				s = new String(bytes, StandardCharsets.UTF_8);
				log.info("terms: {}", s);

			} catch (IOException e) {
				log.error("does not work");
			}

        }
        
        return new PageDTO<CustomerDTO>(foundCustomerDTOs, searchResult.total().hitCount());
    }

    /*
     * terms: {"_index":"customerentity-000001","_id":"t2_5300a0b4-1b25-483c-a26c-cc260bdba66e","_version":1,"found":true,"took":3,"term_vectors":{}}
     * terms: {"_index":"customerentity-000001","_id":"t2_0d5907e6-8e69-4acc-81d1-85912912e8b7","_version":1,"found":true,"took":6,"term_vectors":{"familyname":{"field_statistics":{"sum_doc_freq":7,"doc_count":1,"sum_ttf":7},"terms":{"ell":{"term_freq":1},"erk":{"term_freq":1},"ler":{"term_freq":1},"lle":{"term_freq":1},"mue":{"term_freq":1},"rke":{"term_freq":1},"uel":{"term_freq":1}}},"forename":{"field_statistics":{"sum_doc_freq":4,"doc_count":1,"sum_ttf":4},"terms":{"ell":{"term_freq":1},"lli":{"term_freq":1},"mue":{"term_freq":1},"uel":{"term_freq":1}}}}}
     */
    
    
    @Transactional(readOnly=true)
	public List<CustomerDTO> searchAsYouType(String fullname) {
		SearchSession searchSession = Search.session(entityManager);
		String tenantId = TenantContext.getTenantId();
		log.info("{} Searching for {}", tenantId, fullname);

        String saytClause = """
        		{
				    "multi_match": {
				      "query": "%s",
				      "type": "bool_prefix",
				      "fields": [
				        "fullname",
				        "fullname._2gram",
				        "fullname._3gram"
				      ]
				    }
				  }
				
        		""".formatted(fullname);
        
		SearchResult<CustomerEntity> searchResult = searchSession.search(CustomerEntity.class)
        		.extension(ElasticsearchExtension.get())
        		.where(f -> f.fromJson(saytClause))
        		.fetchAll();

        return searchResult.hits().stream().map(customerService::toCustomerDTO).toList();
	}
}
