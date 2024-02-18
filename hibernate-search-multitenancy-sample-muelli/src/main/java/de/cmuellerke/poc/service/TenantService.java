package de.cmuellerke.poc.service;

import de.cmuellerke.poc.entity.Tenant;
import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.repository.TenantRepository;
import jakarta.annotation.Nonnull;
import lombok.NonNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantService {
    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TenantDTO> getAllTenants() {
        List<Tenant> allTenants = tenantRepository.findAll();

        return allTenants.stream()
        		.map(tenant -> modelMapper.map(tenant, TenantDTO.class))
        		.collect(Collectors.toList());
    }

    public TenantDTO getTenant(@NonNull String id) {
        return tenantRepository.findById(id)
        		.map(tenantEntity -> modelMapper.map(tenantEntity, TenantDTO.class))
        		.orElseThrow(() -> new TenantNotFoundException("Tenant not found"));
    }

	public TenantDTO createOrUpdate(TenantDTO tenant) {
		Optional<Tenant> tenantFromDatabase = tenantRepository.findById(tenant.getId());
		
		Tenant savedTenant = tenantFromDatabase.map(existingTenant -> update(existingTenant, tenant)).orElseGet(() -> create(tenant));
		
		return TenantDTO.builder().id(savedTenant.getId()).name(savedTenant.getName()).build();
	}

	private Tenant create(TenantDTO tenant) {
		Tenant tenantEntity = new Tenant(tenant.getId(), tenant.getName());
		return tenantRepository.save(tenantEntity);
	}

	private Tenant update(Tenant existingTenant, TenantDTO tenant) {
		existingTenant.setName(tenant.getName());
		return tenantRepository.save(existingTenant);
	}
}
