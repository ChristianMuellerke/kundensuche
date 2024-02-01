package de.cmuellerke.poc.service;

import java.util.List;

import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.cmuellerke.poc.entity.CustomerEntity;
import de.cmuellerke.poc.payload.CustomerDTO;
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
}
