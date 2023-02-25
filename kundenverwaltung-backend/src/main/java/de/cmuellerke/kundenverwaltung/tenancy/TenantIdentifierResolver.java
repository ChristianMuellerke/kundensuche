package de.cmuellerke.kundenverwaltung.tenancy;

import java.util.Map;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

//	@Autowired
//	private TenantContextAccess tenantContextAccess;
	
	// see sample under https://medium.com/deviniti-technology-driven-blog/implementing-multitenancy-architecture-spring-boot-jpa-hibernate-flyway-8fb19b312a10
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "default";
		
		log.debug("Current tenantId = {}", tenantId);
		
		return tenantId;
		
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		if (requestAttributes != null) {
//			String tenantId = (String) requestAttributes.getAttribute(CURRENT_TENANT_IDENTIFIER, RequestAttributes.SCOPE_REQUEST);
//			if (tenantId != null) {
//				return tenantId;
//			}
//		}
//		return DEFAULT_TENANT_ID;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
	}
	
//	@Bean
//	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//	public TenantContextAccess tenantOnRequest() {
//		return new TenantContextAccess();
//	}
}