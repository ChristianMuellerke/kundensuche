package de.cmuellerke.kundenverwaltung.tenancy;

import java.util.Map;
import java.util.Optional;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

// https://github.com/lealceldeiro/springboot-multitenant-hibernate/tree/main
// https://github.com/spring-projects/spring-data-examples/tree/main/jpa/multitenant
// see sample under https://medium.com/deviniti-technology-driven-blog/implementing-multitenancy-architecture-spring-boot-jpa-hibernate-flyway-8fb19b312a10

//https://dzone.com/articles/an-alternative-approach-to-threadlocal-using-sprin-1

@Component
@Slf4j
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {
	
    private static final String UNKNOWN = "unknown";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantInfo = Optional.ofNullable(TenantContext.getTenantInfo()).orElse(UNKNOWN);
        log.info("Actual Tenant = {}", tenantInfo);
        return tenantInfo;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}