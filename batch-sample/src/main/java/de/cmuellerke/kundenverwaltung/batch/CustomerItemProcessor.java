package de.cmuellerke.kundenverwaltung.batch;

import org.springframework.batch.item.ItemProcessor;

import de.cmuellerke.kundenverwaltung.models.CustomerEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.CustomerDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<CustomerEntity, CustomerDTO> {

	@Override
	public CustomerDTO process(CustomerEntity item) throws Exception {
		
		CustomerDTO customer = CustomerDTO	.builder()
					.vorname(item.getVorname())
					.nachname(item.getNachname())
					.build();
		
		log.info("Converting ");

		return customer;
	}

}