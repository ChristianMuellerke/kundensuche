package de.cmuellerke.poc.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
@Disabled
public class TenantServiceIntegrationTest implements WithAssertions {
    @Autowired 
    private TenantService tenantService;

    @Autowired 
    private TenantRepository tenantRepository;

    @Test
	@DisplayName("can save and get a tenant")
    void tenantsCanBeSavedByTenantService() throws InterruptedException {
    	// GIVEN
    	TenantDTO tenant = Testdata.TENANT_1;
		// WHEN
    	TenantDTO savedTenant = tenantService.createOrUpdate(tenant);
    	// THEN
    	assertThat(savedTenant.getId()).isEqualTo(tenant.getId());
    	assertThat(savedTenant.getName()).isEqualTo(tenant.getName());
    }

}
