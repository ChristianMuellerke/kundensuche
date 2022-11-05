package de.cmuellerke.kundenverwaltung.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.Tenant;
import de.cmuellerke.kundenverwaltung.payload.tenant.TenantDTO;
import de.cmuellerke.kundenverwaltung.repository.TenantRepository;

@Service
public class TenantService {
	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<TenantDTO> getAllTenants() {
		List<Tenant> allTenants = tenantRepository.findAll();

		return allTenants.stream().map(tenant ->  {
			TenantDTO tenantDTO = new TenantDTO();
			modelMapper.map(tenant, tenantDTO);
			return tenantDTO;
		}).collect(Collectors.toList());
	}

	public TenantDTO getTenant(Long id) {
		Tenant tenant = tenantRepository.getReferenceById(id);
		TenantDTO tenantDTO = new TenantDTO();
		modelMapper.map(tenant, tenantDTO);
		return tenantDTO;
	}
}
