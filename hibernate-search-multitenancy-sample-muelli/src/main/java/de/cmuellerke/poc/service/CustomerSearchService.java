package de.cmuellerke.poc.service;

import de.cmuellerke.poc.entity.KundeEntity;
import de.cmuellerke.poc.payload.KundeDTO;
import de.cmuellerke.poc.tenancy.TenantContext;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class CustomerSearchService {

    private final KundenService kundenService;

    private final EntityManager entityManager;

    @Transactional(readOnly=true)
    public List<KundeDTO> findByName(String nachname) {
        SearchSession searchSession = Search.session(entityManager);

        String tenantId = TenantContext.getTenantId();

        log.info("{} Searching for {}", tenantId, nachname);

        SearchResult<KundeEntity> searchResult = searchSession.search(KundeEntity.class)
        		.where(f -> f.and()
	        		.add(f.match().field("nachname").matching(nachname))
//	        		.add(f.match().field("tenantId").matching(tenantId))
        		)
        		.fetchAll();

        return searchResult.hits().stream().map(kundenService::toKundeDTO).toList();
    }
}
