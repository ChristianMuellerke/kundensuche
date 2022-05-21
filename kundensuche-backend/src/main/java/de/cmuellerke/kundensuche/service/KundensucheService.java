package de.cmuellerke.kundensuche.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import de.cmuellerke.kundensuche.entity.Kunde;
import de.cmuellerke.kundensuche.repository.KundenRepository;

@Service
public class KundensucheService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KundensucheService.class);

	@Autowired
	private KundenRepository kundenRepository;

	public Iterable<Kunde> saveToIndex(final List<Kunde> kunden) {
		LOGGER.debug("speichere Kunden...");
		kunden.parallelStream().forEach(this::aufbereiten);
		return kundenRepository.saveAll(kunden);
	}

	public Kunde saveToIndex(final Kunde kunde) {
		LOGGER.debug("speichere Kunde {}", kunde.getKurzname());
		aufbereiten(kunde);
		return kundenRepository.save(kunde);
	}

	public List<Kunde> gibAlleKunden() {
		Iterable<Kunde> kunden = kundenRepository.findAll();
		return StreamSupport.stream(kunden.spliterator(), false).collect(Collectors.toList());
	}

	public void deleteAll() {
		LOGGER.debug("Alle Kunden in der Datenbank werden gelöscht");
		kundenRepository.deleteAll();
	}

	public Page<Kunde> gibAlleKundenPaged(Pageable pageable) {
		return kundenRepository.findAll(pageable);
	}

	private void aufbereiten(Kunde kunde) {
		LOGGER.debug("Kurzname = {}", kunde.getKurzname());
		
		Completion suggest = new Completion(new String[] { kunde.getKurzname(), kunde.getNachname(), kunde.getVorname() });
		kunde.setSuggest(suggest);
		kunde.setKurznameSAYT(kunde.getKurzname());
		kunde.setKurznameForSAYT(kunde.getKurzname());
	}
}
