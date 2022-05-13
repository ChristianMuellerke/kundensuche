package de.cmuellerke.kundensuche.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundensuche.entity.Kunde;
import de.cmuellerke.kundensuche.service.KundensucheService;

@RestController
public class KundenController {

	@Autowired
	KundensucheService kundensucheService;

	@GetMapping("/kunden")
	public List<Kunde> getKunden() {
		return kundensucheService.gibAlleKunden();
	}

	@GetMapping("/kunden/paged")
	public Page<Kunde> getKunden(Pageable pageable) {
		return kundensucheService.gibAlleKundenPaged(pageable);
	}

	@PostMapping("/kunde/create")
	@ResponseStatus(HttpStatus.CREATED)
	public String createKunde(@RequestBody Kunde kunde) {
		return kundensucheService.saveToIndex(kunde).getId();
	}

	@PutMapping("/kunden/create")
	@ResponseStatus(HttpStatus.CREATED)
	public List<String> createKunden(@RequestBody List<Kunde> kunden) {
		Iterable<Kunde> gespeicherteKunden = kundensucheService.saveToIndex(kunden);
		List<String> gespeicherteKundenIds = new ArrayList<>();
		gespeicherteKunden.forEach(kunde -> {
			gespeicherteKundenIds.add(kunde.getId());
		});
		return gespeicherteKundenIds;
	}

	@PostMapping("/kunden/deleteAll")
	@ResponseStatus(HttpStatus.OK)
	public String deleteAll() {
		kundensucheService.deleteAll();
		return "OK";
	}

}