package de.cmuellerke.kundenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.payload.tenant.TenantDTO;
import de.cmuellerke.kundenverwaltung.service.TenantService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/tenants")
public class TenantController {
	@Autowired
	TenantService tenantService;

	@GetMapping("/all")
	public ResponseEntity<List<TenantDTO>> allTenants() {
		List<TenantDTO> allTenants = tenantService.getAllTenants();
		return ResponseEntity.ok(allTenants);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TenantDTO> getById(@PathVariable("id") String id) {
		TenantDTO tenant = tenantService.getTenant(Long.valueOf(id));
		return ResponseEntity.ok(tenant);
	}

}
