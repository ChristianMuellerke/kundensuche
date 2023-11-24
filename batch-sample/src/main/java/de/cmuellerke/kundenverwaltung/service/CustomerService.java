package de.cmuellerke.kundenverwaltung.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.CustomerEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.CustomerDTO;
import de.cmuellerke.kundenverwaltung.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CustomerRepository kundenRepository;
	
	public List<CustomerDTO> legeKundenAn(List<CustomerDTO> neueKunden) {
		
		log.debug("Start Kunden anlegen - Size=" + neueKunden.size());
		
		List<CustomerEntity> neuAnzulegendeKunden = neueKunden.stream().map(kundeDTO -> {
			
			CustomerEntity neuerKunde = CustomerEntity.builder()
				.nachname(kundeDTO.getNachname())
				.vorname(kundeDTO.getVorname())
				.build();

			return neuerKunde;
		}).collect(Collectors.toList());
		
		List<CustomerEntity> neuAngelegteKunden = kundenRepository.saveAll(neuAnzulegendeKunden);
		
		log.debug("Ende Kunden anlegen - Size=" + neuAngelegteKunden.size());
		
		return neuAngelegteKunden.stream().map(this::toKundeDTO).collect(Collectors.toList());
	}
	
	public CustomerDTO speichereKunde(CustomerDTO kundeDTO) {
		
		Optional<CustomerEntity> foundKunde = Optional.empty();
		
		if (kundeDTO.getId() != null) {
			foundKunde = kundenRepository.findById(UUID.fromString(kundeDTO.getId()));
		}
		
		CustomerEntity zuSpeichernderKunde = foundKunde.map(kunde -> {
			kunde.setNachname(kundeDTO.getNachname());
			kunde.setVorname(kundeDTO.getVorname());
			return kunde;
		}).orElseGet(() -> {
			CustomerEntity neuerKunde = CustomerEntity.builder()
					.nachname(kundeDTO.getNachname())
					.vorname(kundeDTO.getVorname())
					.build();
			
			return neuerKunde;
		});
		
		CustomerEntity gespeicherterKunde = kundenRepository.save(zuSpeichernderKunde);
		
		log.debug("Kunde {} gespeichert", gespeicherterKunde.getCustomerId());
		
		return toKundeDTO(gespeicherterKunde);
	}

	public Optional<CustomerDTO> getKunde(String kundeId) {
		Optional<CustomerEntity> foundKunde = kundenRepository.findById(UUID.fromString(kundeId));
		return foundKunde.map(this::toKundeDTO);
	}
	
	public List<CustomerDTO> getAlleKunden() {
		List<CustomerEntity> gefundeneKunden = kundenRepository.findAll();
		return gefundeneKunden.stream().map(this::toKundeDTO).collect(Collectors.toList());
	}
	
	private CustomerDTO toKundeDTO(CustomerEntity kundeEntity) {
		return CustomerDTO.builder()
				.vorname(kundeEntity.getVorname()) //
				.nachname(kundeEntity.getNachname()) //
				.id(kundeEntity.getCustomerId().toString()) //
				.build();
	}
}
