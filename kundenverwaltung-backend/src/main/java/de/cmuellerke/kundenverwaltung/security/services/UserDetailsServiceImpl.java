package de.cmuellerke.kundenverwaltung.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import de.cmuellerke.kundenverwaltung.tenancy.TenantIdentifierResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String tenantId = TenantContext.getTenantId();

		log.debug("Obtaining User {} from Tenant {}", username, tenantId);
		
		try {
			UserEntity user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username + " for tenant " + tenantId));

			return UserDetailsImpl.build(user);
		} catch (Exception e) {
			log.error("User konnte nicht aus der Datenbank geladen werden.", e);
			throw e;
		}
	}

}
