package de.cmuellerke.poc.service;

import de.cmuellerke.poc.entity.Tenant;
import de.cmuellerke.poc.payload.TenantDTO;
import de.cmuellerke.poc.repository.TenantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantService {
    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TenantDTO> getAllTenants() {
        List<Tenant> allTenants = tenantRepository.findAll();

        return allTenants.stream().map(tenant -> {
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
