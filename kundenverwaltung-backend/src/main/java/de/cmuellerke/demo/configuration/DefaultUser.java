package de.cmuellerke.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.data.entity.UserDAO;
import de.cmuellerke.demo.repository.UserRepository;
import de.cmuellerke.demo.service.UserService;
import de.cmuellerke.demo.tenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultUser implements CommandLineRunner {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {
		TenantContext.setTenantId("0000");
		log.debug("inserting initial user to tenant 0000");
		UserDTO defaultUser = UserDTO.builder().userName("default").password("default").build();
		userService.save(defaultUser);

		TenantContext.setTenantId("0001");
		log.debug("inserting initial user to tenant 0001");
		UserDTO defaultUser2 = UserDTO.builder().userName("default2").password("default").build();
		userService.save(defaultUser2);
	}

}
