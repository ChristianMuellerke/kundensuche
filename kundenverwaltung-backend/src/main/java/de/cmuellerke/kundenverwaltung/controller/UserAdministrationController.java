package de.cmuellerke.kundenverwaltung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.payload.tenant.TenantDTO;
import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
import de.cmuellerke.kundenverwaltung.service.TenantService;
import de.cmuellerke.kundenverwaltung.service.UserAdministrationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserAdministrationController {
	@Autowired
	UserAdministrationService userAdministrationService;

	/**
	 * Liefere alle Benutzer des aktuellen Tenants zurück
	 * 
	 * @return
	 */
	@GetMapping("/all")
	public ResponseEntity<List<UserDTO>> allUsers() {
		List<UserDTO> allUsers = userAdministrationService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}

	
}
