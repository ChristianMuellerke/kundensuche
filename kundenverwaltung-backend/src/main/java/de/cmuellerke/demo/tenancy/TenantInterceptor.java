package de.cmuellerke.demo.tenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TenantInterceptor implements WebRequestInterceptor {

	private final String defaultTenant;

	@Autowired
	public TenantInterceptor(@Value("${multitenancy.tenant.default-tenant:#{null}}") String defaultTenant) {
		this.defaultTenant = defaultTenant;
	}

	@Override
	public void preHandle(WebRequest request) {
		String tenantId;
		if (request.getHeader("X-TENANT-ID") != null) {
			tenantId = request.getHeader("X-TENANT-ID");
		} else if (this.defaultTenant != null) {
			log.warn("No TenantId in Request! Using DefaultTenant {}", this.defaultTenant);
			tenantId = this.defaultTenant;
		} else {
			tenantId = ((ServletWebRequest) request).getRequest().getServerName().split("\\.")[0];
			log.warn("No TenantId in Request and no DefaultTenant specified! Using Tenant {}", tenantId);
		}
		TenantContext.setTenantId(tenantId);
	}

	@Override
	public void postHandle(@NonNull WebRequest request, ModelMap model) {
		TenantContext.clear();
	}

	@Override
	public void afterCompletion(@NonNull WebRequest request, Exception ex) {
		// NOOP
	}
}