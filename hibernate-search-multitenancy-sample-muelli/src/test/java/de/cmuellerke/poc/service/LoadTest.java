package de.cmuellerke.poc.service;


import de.cmuellerke.poc.payload.CustomerDTO;
import de.cmuellerke.poc.tenancy.TenantContext;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Disabled
class LoadTest implements WithAssertions {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerSearchService customerSearchService;

    @Test
    void testLoadingAndSearching() throws InterruptedException {
    	TenantContext.setTenantId(Testdata.TENANT_2);
    	List<Person> testPersonen = new Personengenerator().erzeugePersonen();

    	List<CustomerDTO> customersToBeCreated = new ArrayList<CustomerDTO>();
    	
    	for (int i = 0; i < 5000; i++) {
    		Person person = testPersonen.get(i);

    		CustomerDTO newCustomer = CustomerDTO.builder()
                    .forename(person.getVorname()) //
                    .familyname(person.getNachname()) //
                    .build();
    		
    		customersToBeCreated.add(newCustomer);
    	}
    	
    	List<CustomerDTO> savedCustomers = customerService.save(customersToBeCreated);
    	
    	// now search
    	
    }

}
