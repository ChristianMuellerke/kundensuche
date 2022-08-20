package de.cmuellerke.demo.security;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.cmuellerke.demo.data.dto.ApplicationUser;
import de.cmuellerke.demo.data.entity.UserDAO;
import de.cmuellerke.demo.repository.UserRepository;
import de.cmuellerke.demo.service.UserService;
import de.cmuellerke.demo.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl  implements org.springframework.security.core.userdetails.UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	public ApplicationUser loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("TenantId = {}", getTenantId());
		
		if ("javainuse".equals(username)) {
			log.debug("Testuser is used, thats not ok");
			ApplicationUser user = new ApplicationUser("javainuse", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", getTenantId(),
					new ArrayList<>());
			return user;
		} else {
			log.debug("loading user {} from db", username);
			Optional<UserDAO> userFromDB = userRepository.findByUserNameAndTenantId(username, getTenantId());
			
			return userFromDB.map(user -> {
				return new ApplicationUser(user.getUserName(), user.getPassword(), user.getTenantId(), new ArrayList<>());
			})
			.orElseThrow(() -> new UsernameNotFoundException("[" + getTenantId() + "] User " + username + " not found."));
		}
	}

	private String getTenantId() {
		return TenantContext.getTenantId();
	}
}
