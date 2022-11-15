package de.cmuellerke.kundenverwaltung.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.Tenant;
import de.cmuellerke.kundenverwaltung.models.User;
import de.cmuellerke.kundenverwaltung.payload.tenant.TenantDTO;
import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
import de.cmuellerke.kundenverwaltung.repository.TenantRepository;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;

@Service
public class UserAdministrationService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	public List<UserDTO> getAllUsers() {
		List<User> allUsers = userRepository.findByTenantId(TenantContext.getTenantId());

		return allUsers.stream().map(user ->  {
			UserDTO userDTO = new UserDTO();
			modelMapper.map(user, userDTO);
			return userDTO;
		}).collect(Collectors.toList());
	}

//	public TenantDTO getTenant(Long id) {
//		Tenant tenant = tenantRepository.getReferenceById(id);
//		TenantDTO tenantDTO = new TenantDTO();
//		modelMapper.map(tenant, tenantDTO);
//		return tenantDTO;
//	}
}
