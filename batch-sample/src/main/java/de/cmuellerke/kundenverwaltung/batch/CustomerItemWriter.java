package de.cmuellerke.kundenverwaltung.batch;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import de.cmuellerke.kundenverwaltung.models.OutputEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.CustomerDTO;
import de.cmuellerke.kundenverwaltung.repository.OutputRepository;

public class CustomerItemWriter<T> implements ItemWriter<CustomerDTO> {

	private Integer tenantId;
	private StepExecution stepExecution;
	
	@Autowired
	private OutputRepository outputRepository;
	
	public CustomerItemWriter() {
		
	}
	
	public CustomerItemWriter(Integer tenantId, StepExecution se) {
		this.tenantId = tenantId;
		this.stepExecution = se;
	}
	
	@Override
	public void write(Chunk<? extends CustomerDTO> chunk) throws Exception {
		List<? extends CustomerDTO> items = chunk.getItems();
		for (int i = 0; i < items.size(); i++) {
			CustomerDTO customer = items.get(0);
			
			OutputEntity outputEntity = OutputEntity
					.builder()
					.vorname(customer.getVorname())
					.nachname(customer.getVorname())
					.build();
			
			outputRepository.save(outputEntity);
		}
	}

}
