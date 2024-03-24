package de.cmuellerke.poc.service;

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import de.cmuellerke.poc.payload.TenantDTO;

public class Testdata {
    public static final CustomerDTO CUSTOMER_1 = createCustomer1();

    public static final TenantDTO TENANT_1 = TenantDTO.builder().id("t1").name("TestTenant 1").build();
    public static final TenantDTO TENANT_2 = TenantDTO.builder().id("t2").name("TestTenant 2").build();
    public static final TenantDTO TENANT_3 = TenantDTO.builder().id("t3").name("TestTenant 3").build();
    
	private static CustomerDTO createCustomer1() {
        return CustomerDTO.builder()
                .forename("Muelli") //
                .familyname("Muellerke") //
                .build();
	}
}
