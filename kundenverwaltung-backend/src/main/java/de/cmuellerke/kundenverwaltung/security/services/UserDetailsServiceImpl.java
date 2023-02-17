package de.cmuellerke.kundenverwaltung.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String tenantId = TenantContext.getTenantId();

		log.debug("Obtaining User {} from Tenant {}", username, tenantId);
		
		UserEntity user = userRepository.findByUsernameAndTenantId(username, tenantId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

		return UserDetailsImpl.build(user);
	}

}
