package de.cmuellerke.kundenverwaltung.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;
import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KundenService {

	private final KundenRepository kundenRepository;
	
	@Transactional
	public List<KundeDTO> legeKundenAn(List<KundeDTO> neueKunden) {
		
		log.debug("Start Kunden anlegen - Size=" + neueKunden.size());
		
		List<KundeEntity> neuAnzulegendeKunden = neueKunden.stream().map(kundeDTO -> {
			//TenantContext.setTenantId(kundeDTO.getTenantId());
			
//			KundeEntity neuerKunde = KundeEntity.builder()
//				.nachname(kundeDTO.getNachname())
//				.vorname(kundeDTO.getVorname())
//				.build();

			KundeEntity neuerKunde = new KundeEntity();
			neuerKunde.setVorname(kundeDTO.getVorname());
			neuerKunde.setNachname(kundeDTO.getNachname());
			
			return neuerKunde;
		}).collect(Collectors.toList());
		
		List<KundeEntity> neuAngelegteKunden = kundenRepository.saveAll(neuAnzulegendeKunden);

		
		log.debug("Ende Kunden anlegen - Size=" + neuAngelegteKunden.size());
		
		return neuAngelegteKunden.stream()//
				.map(this::toKundeDTO)//
				.collect(Collectors.toList());
	}
	
	@Transactional
	public KundeDTO speichereKunde(KundeDTO kundeDTO) {
		log.info("Saving kunde, tenantId={} Vorname={} Nachname={}", TenantContext.getTenantInfo(), kundeDTO.getVorname(), kundeDTO.getNachname());
		
		Optional<KundeEntity> foundKunde = Optional.empty();
		
		if (kundeDTO.getId() != null) {
			foundKunde = kundenRepository.findById(UUID.fromString(kundeDTO.getId()));
		}
		
		KundeEntity zuSpeichernderKunde = foundKunde.map(kunde -> {
			kunde.setNachname(kundeDTO.getNachname());
			kunde.setVorname(kundeDTO.getVorname());
			return kunde;
		}).orElseGet(() -> {
			return KundeEntity.builder()
					.nachname(kundeDTO.getNachname())
					.vorname(kundeDTO.getVorname())
					.build();
		});
		
		KundeEntity gespeicherterKunde = kundenRepository.save(zuSpeichernderKunde);
		
		// TODO warum ist das so? 
		gespeicherterKunde.setTenantId(TenantContext.getTenantInfo());
		
		log.info("[Tenant={}] Kunde {} gespeichert (Tenant an Entity ist {})", gespeicherterKunde.getTenantId(), gespeicherterKunde.getCustomerId(), gespeicherterKunde.getTenantId());
		
		return toKundeDTO(gespeicherterKunde);
	}

	public Optional<KundeDTO> getKunde(String kundeId) {
		Optional<KundeEntity> foundKunde = kundenRepository.findById(UUID.fromString(kundeId));
		return foundKunde.map(this::toKundeDTO);
	}
	
	public List<KundeDTO> getAlleKunden() {
		List<KundeEntity> gefundeneKunden = kundenRepository.findAll();
		return gefundeneKunden.stream().map(this::toKundeDTO).collect(Collectors.toList());
	}
	
	private KundeDTO toKundeDTO(KundeEntity kundeEntity) {
		return KundeDTO.builder()
				.vorname(kundeEntity.getVorname()) //
				.nachname(kundeEntity.getNachname()) //
				.id(kundeEntity.getCustomerId().toString()) //
				.tenantId(kundeEntity.getTenantId()) //
				.build();
	}
}
