package de.cmuellerke.kundenverwaltung.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KundenService {

	private final KundenRepository kundenRepository;

	public List<KundeDTO> legeKundenAn(List<KundeDTO> neueKunden) {
		
		log.debug("Start Kunden anlegen - Size=" + neueKunden.size());
		
		List<KundeEntity> neuAnzulegendeKunden = neueKunden.stream().map(kundeDTO -> {
			
			KundeEntity neuerKunde = KundeEntity.builder()
				.geburtsdatum(kundeDTO.getGeburtsdatum().toInstant(ZoneOffset.UTC))
				.nachname(kundeDTO.getNachname())
				.vorname(kundeDTO.getVorname())
				.build();
			
			return neuerKunde;
		}).collect(Collectors.toList());
		
		List<KundeEntity> neuAngelegteKunden = kundenRepository.saveAll(neuAnzulegendeKunden);
		
		log.debug("Ende Kunden anlegen - Size=" + neuAngelegteKunden.size());
		
		return neuAngelegteKunden.stream().map(this::toKundeDTO).collect(Collectors.toList());
	}
	
	public KundeDTO speichereKunde(KundeDTO kundeDTO) {
		Optional<KundeEntity> foundKunde = kundenRepository.findByCustomerIdAndTenantId(kundeDTO.getId(), TenantContext.getTenantId());
		
		KundeEntity zuSpeichernderKunde = foundKunde.map(kunde -> {
			kunde.setGeburtsdatum(kundeDTO.getGeburtsdatum().toInstant(ZoneOffset.UTC));
			kunde.setNachname(kundeDTO.getNachname());
			kunde.setVorname(kundeDTO.getVorname());
			return kunde;
		}).orElseGet(() -> {
			KundeEntity neuerKunde = KundeEntity.builder()
					.geburtsdatum(kundeDTO.getGeburtsdatum().toInstant(ZoneOffset.UTC))
					.nachname(kundeDTO.getNachname())
					.vorname(kundeDTO.getVorname())
					.build();
			
			return neuerKunde;
		});
		
		KundeEntity gespeicherterKunde = kundenRepository.save(zuSpeichernderKunde);
		
		log.debug("Kunde {} gespeichert", gespeicherterKunde.getCustomerId());
		
		return toKundeDTO(gespeicherterKunde);
	}

	public Optional<KundeDTO> getKunde(String kundeId) {
		Optional<KundeEntity> foundKunde = kundenRepository.findByCustomerIdAndTenantId(kundeId, TenantContext.getTenantId());
		return foundKunde.map(this::toKundeDTO);
	}
	
	public List<KundeDTO> getAlleKunden() {
		List<KundeEntity> gefundeneKunden = kundenRepository.findByTenantId(TenantContext.getTenantId());
		return gefundeneKunden.stream().map(this::toKundeDTO).collect(Collectors.toList());
	}
	
	private KundeDTO toKundeDTO(KundeEntity kundeEntity) {
		return KundeDTO.builder()
				.geburtsdatum(LocalDateTime.ofInstant(kundeEntity.getGeburtsdatum(), ZoneOffset.UTC))
				.vorname(kundeEntity.getVorname()) //
				.nachname(kundeEntity.getNachname()) //
				.id(kundeEntity.getCustomerId().toString()) //
				.tenantId(kundeEntity.getTenantId()) //
				.build();
	}
}
