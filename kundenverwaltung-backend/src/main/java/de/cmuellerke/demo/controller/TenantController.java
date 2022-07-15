package de.cmuellerke.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.demo.data.dto.TenantDTO;
import de.cmuellerke.demo.service.TenantService;

@RestController
@RequestMapping("tenants")
@Validated
public class TenantController {

	@Autowired
	TenantService tenantService;

	@PutMapping(path = "/create")
	public ResponseEntity<TenantDTO> createUser(@Valid @RequestBody TenantDTO user) {
		TenantDTO savedTenant = tenantService.create(user);
		return ResponseEntity.ok(savedTenant);
	}

	@PostMapping(path = "/{tenantId}")
	public ResponseEntity<TenantDTO> updateTenant(@PathVariable Long tenantId, @Valid @RequestBody TenantDTO user) {
		TenantDTO savedTenant = tenantService.update(tenantId, user);
		return ResponseEntity.ok(savedTenant);
	}

	@GetMapping(path = "/")
	public ResponseEntity<Iterable<TenantDTO>> getAllTenants() {
		return ResponseEntity.ok(tenantService.getAll());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Map<String, String> handleBindExceptions(BindException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
