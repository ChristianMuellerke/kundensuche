package de.cmuellerke.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.cmuellerke.demo.data.entity.UserDAO;
import de.cmuellerke.demo.repository.UserRepository;
import de.cmuellerke.demo.tenancy.TenantContext;

@Component
public class StartupActions implements CommandLineRunner {

	private UserRepository userRepository;

	private static Logger LOGGER = LoggerFactory.getLogger(StartupActions.class);

	@Autowired
	public StartupActions(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.debug("running startup actions");
	}

}
