package de.cmuellerke.kundenverwaltung.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.payload.response.PageWithUsersResponse;
import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
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

	/*
	 * Siehe auch https://github.com/bezkoder/spring-boot-jpa-paging-sorting/blob/master/src/main/java/com/bezkoder/spring/data/jpa/pagingsorting/controller/TutorialController.java
	 */

	/**
	 * Gibt eine Liste von Benutzern zurück. Diese ist ein Teil aller Benutzer zum aktuellen Tenant. 
	 * Liefert paginiert.
	 * 
	 * @param title
	 * @param page
	 * @param size
	 * @param sort
	 * @return
	 */
	@GetMapping("/all/paged")
	public ResponseEntity<PageWithUsersResponse> getAllUsersPage(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam(defaultValue = "id,desc") String[] sort) {	

		Page<UserDTO> pageWithUserDTO = userAdministrationService.getAllUsers(page, size, sort);

		List<UserDTO> users = new ArrayList<UserDTO>();
		users = pageWithUserDTO.getContent();
		
		PageWithUsersResponse pageResponse = PageWithUsersResponse.builder()
				.currentPage(pageWithUserDTO.getNumber())
				.totalItems(pageWithUserDTO.getTotalElements())
				.totalPages(pageWithUserDTO.getTotalPages())
				.users(users)
				.build();
		
		return new ResponseEntity<>(pageResponse, HttpStatus.OK);
	}

}
