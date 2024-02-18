package de.cmuellerke.poc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.service.TenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/tenants")
@Slf4j
@AllArgsConstructor
public class TenantController {
	
	private final TenantService tenantService;
	
	@GetMapping("/all")
	public List<TenantDTO> getAllTenants() {
		log.info("getting all Tenants");
		return tenantService.getAllTenants();
	}
}
