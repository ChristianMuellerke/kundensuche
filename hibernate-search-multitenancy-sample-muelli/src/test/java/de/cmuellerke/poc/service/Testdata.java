package de.cmuellerke.poc.service;

import de.cmuellerke.poc.payload.CustomerDTO;

public class Testdata {
    public static final String TENANT_1 = "t1";
    public static final String TENANT_2 = "t2";
    public static final String TENANT_3 = "t3";
    
    public static final CustomerDTO CUSTOMER_1 = createCustomer1();

	private static CustomerDTO createCustomer1() {
        return CustomerDTO.builder()
                .forename("Muelli") //
                .familyname("Muellerke") //
                .build();
	}
}
