package de.cmuellerke.poc.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.cmuellerke.poc.entity.KundeEntity;
import de.cmuellerke.poc.payload.KundeDTO;
import de.cmuellerke.poc.repository.KundenRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
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
		
		Optional<KundeEntity> foundKunde = Optional.empty();
		
		if (kundeDTO.getId() != null) {
			foundKunde = kundenRepository.findById(UUID.fromString(kundeDTO.getId()));
		}
		
		KundeEntity zuSpeichernderKunde = foundKunde.map(kunde -> {
			kunde.setNachname(kundeDTO.getNachname());
			kunde.setVorname(kundeDTO.getVorname());
			return kunde;
		}).orElseGet(() -> {
			KundeEntity neuerKunde = KundeEntity.builder()
					.nachname(kundeDTO.getNachname())
					.vorname(kundeDTO.getVorname())
					.build();
			
			return neuerKunde;
		});
		
		KundeEntity gespeicherterKunde = kundenRepository.save(zuSpeichernderKunde);
		
		log.debug("[{}] Kunde {} in Tenant {} gespeichert", TenantContext.getTenantId(), gespeicherterKunde.getCustomerId(), gespeicherterKunde.getTenantId());
		
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
		log.debug("{} Mapping KundeEntity to DTO, TenantId from Entity is {}", TenantContext.getTenantId(), kundeEntity.getTenantId());
		
		return KundeDTO.builder()
				.vorname(kundeEntity.getVorname()) //
				.nachname(kundeEntity.getNachname()) //
				.id(kundeEntity.getCustomerId().toString()) //
				.build();
	}
}
