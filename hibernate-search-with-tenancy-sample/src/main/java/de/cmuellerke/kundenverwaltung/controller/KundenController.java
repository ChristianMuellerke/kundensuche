package de.cmuellerke.kundenverwaltung.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.cmuellerke.kundenverwaltung.payload.customer.KundeDTO;
import de.cmuellerke.kundenverwaltung.service.KundenService;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/kunden")
@Slf4j
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
	public ResponseEntity<KundeDTO> speichereKunde(@Valid @RequestBody KundeDTO kunde) {
		String kundeAsJson = new GsonBuilder().setPrettyPrinting().create().toJson(kunde);
		log.info("Input: {}", kundeAsJson);
		
		String tenantInfo = TenantContext.getTenantInfo();
		
		log.info("Tenant from TenantInfo: {}", tenantInfo);
		
		Objects.requireNonNull(kunde.getVorname());
		Objects.requireNonNull(kunde.getNachname());
		
		//return ResponseEntity.ok(kunde);
		return ResponseEntity.ok(kundenService.speichereKunde(kunde));
	}
}
