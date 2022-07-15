package de.cmuellerke.demo.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.cmuellerke.demo.DatabaseIntegrationTest;
import de.cmuellerke.demo.data.entity.UserDAO;

@DatabaseIntegrationTest
class UserRepositoryTest implements WithAssertions {

	private static final String PASSWORD = "xxx xxx xxx xxx";
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void deleteAllUsers() {
		userRepository.deleteAll();
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(userRepository).isNotNull();
	}

	@Test
	void savesNewUser() {

		UserDAO newUser = createUser();
		UserDAO savedUser = userRepository.save(newUser);

		assertThat(savedUser.getId()).isGreaterThan(-1);
		assertThat(savedUser.getUserName()).isEqualTo(newUser.getUserName());
	}

	@Test
	void savesNewUserAndReloadsIt() {

		UserDAO newUser = createUser();
		UserDAO savedUser = userRepository.save(newUser);
		Optional<UserDAO> reloadedUser = userRepository.findById(savedUser.getId());

		assertThat(reloadedUser).isNotEmpty();
		assertThat(reloadedUser.get().getId()).isEqualTo(savedUser.getId());
	}

	@Test
	void savesNewUserAndDeletesIt() {

		UserDAO newUser = createUser();
		UserDAO savedUser = userRepository.save(newUser);
		Optional<UserDAO> reloadedUser = userRepository.findById(savedUser.getId());

		assertThat(reloadedUser).isNotEmpty();
		assertThat(reloadedUser.get().getId()).isEqualTo(savedUser.getId());

		userRepository.delete(reloadedUser.get());
	}

	@Test
	void savesNewUserAndModiefiesIt() {

		UserDAO newUser = createUser();
		UserDAO savedUser = userRepository.save(newUser);
		Optional<UserDAO> reloadedUser = userRepository.findById(savedUser.getId());

		assertThat(reloadedUser).isNotEmpty();
		assertThat(reloadedUser.get().getId()).isEqualTo(savedUser.getId());

		UserDAO modifiedUser = reloadedUser.get();
		modifiedUser.setUserName("modified");
		modifiedUser.setUserName("modified");

		userRepository.save(reloadedUser.get());

		modifiedUser = userRepository.getById(modifiedUser.getId());
		assertThat(modifiedUser.getUserName()).isEqualTo("modified");
		assertThat(modifiedUser.getUserName()).isEqualTo("modified");
	}

	private UserDAO createUser() {
		return UserDAO.builder().userName("Tom").password(PASSWORD).build();
	}
}