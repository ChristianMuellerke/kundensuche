package de.cmuellerke.poc.tenancy;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    // see sample under https://medium.com/deviniti-technology-driven-blog/implementing-multitenancy-architecture-spring-boot-jpa-hibernate-flyway-8fb19b312a10

    @Override
    public String resolveCurrentTenantIdentifier() {

        if (TenantContext.getTenantId() == null) {
            log.debug("TenantId in TenantContext is empty, returning default");
            return "DEFAULT";
        }

        log.debug("TenantId in TenantContext: {}", TenantContext.getTenantId());

        return TenantContext.getTenantId();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}