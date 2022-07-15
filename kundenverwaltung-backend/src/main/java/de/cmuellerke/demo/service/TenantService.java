package de.cmuellerke.demo.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cmuellerke.demo.data.dto.CustomerDTO;
import de.cmuellerke.demo.data.dto.TenantDTO;
import de.cmuellerke.demo.data.entity.Customer;
import de.cmuellerke.demo.data.entity.Tenant;
import de.cmuellerke.demo.repository.TenantRepository;

@Service
public class TenantService {

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	private ModelMapper modelMapper;

	public TenantDTO create(TenantDTO tenant) {
		return convertToDto(tenantRepository.save(convertToEntity(tenant)));
	}

	public TenantDTO update(Long id, TenantDTO tenant) {
		Tenant tenantFromRepo = tenantRepository.getById(id);
		modelMapper.map(tenant, tenantFromRepo);
		return convertToDto(tenantRepository.save(tenantFromRepo));
	}

	public void delete(Long tenantId) {
		tenantRepository.deleteById(tenantId);
	}

	public TenantDTO getById(Long id) throws TenantNotFoundException {
		Optional<Tenant> queryResult = tenantRepository.findById(id);
		return queryResult.map(this::convertToDto).orElseThrow(() -> new TenantNotFoundException());
	}
	
	public Iterable<TenantDTO> getAll() {
		return tenantRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private TenantDTO convertToDto(Tenant tenant) {
		TenantDTO tenantDto = modelMapper.map(tenant, TenantDTO.class);
		return tenantDto;
	}

	private Tenant convertToEntity(TenantDTO tenantDto) {
		Tenant user = modelMapper.map(tenantDto, Tenant.class);

		return user;
	}

}
