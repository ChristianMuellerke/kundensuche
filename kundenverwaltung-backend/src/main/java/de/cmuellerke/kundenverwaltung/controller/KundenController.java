package de.cmuellerke.kundenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.service.KundenService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/kunden")
public class KundenController {

	@Autowired
	private KundenService kundenService;

	@GetMapping("/all")
	public ResponseEntity<List<KundeDTO>> alleKunden() {
		List<KundeDTO> alleKunden = kundenService.getAlleKunden();
		return ResponseEntity.ok(alleKunden);
	}

	@GetMapping("/{id}")
	public ResponseEntity<KundeDTO> getKunde(@PathVariable("id") String id) {
		return kundenService.getKunde(id).map(kunde -> {
			return ResponseEntity.ok(kunde);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/new")
	public ResponseEntity<KundeDTO> speichereKunde(KundeDTO kunde) {
		return ResponseEntity.ok(kundenService.speichereKunde(kunde));
		
	}
}
