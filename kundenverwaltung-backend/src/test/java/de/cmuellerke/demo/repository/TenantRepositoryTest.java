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
import de.cmuellerke.demo.data.entity.Tenant;

@DatabaseIntegrationTest
public class TenantRepositoryTest implements WithAssertions {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private TenantRepository tenantRepository;

	@BeforeEach
	void deleteAllUsers() {
		tenantRepository.deleteAll();
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(tenantRepository).isNotNull();
	}

	@Test
	void savesNewTen() {

		Tenant newTenant = createTenant();
		Tenant savedTenant = tenantRepository.save(newTenant);

		assertThat(savedTenant.getId()).isGreaterThan(-1);
		assertThat(savedTenant.getName()).isEqualTo(newTenant.getName());
	}

	@Test
	void savesNewTenantAndReloadsIt() {

		Tenant newTenant = createTenant();
		Tenant savedTenant = tenantRepository.save(newTenant);
		Optional<Tenant> reloadedUser = tenantRepository.findById(savedTenant.getId());

		assertThat(reloadedUser).isNotEmpty();
		assertThat(reloadedUser.get().getId()).isEqualTo(savedTenant.getId());
	}

	@Test
	void savesNewTenantAndDeletesIt() {

		Tenant newUser = createTenant();
		Tenant savedTenant = tenantRepository.save(newUser);
		Optional<Tenant> reloadedTenant = tenantRepository.findById(savedTenant.getId());

		assertThat(reloadedTenant).isNotEmpty();
		assertThat(reloadedTenant.get().getId()).isEqualTo(savedTenant.getId());

		tenantRepository.delete(reloadedTenant.get());
	}

	@Test
	void savesNewTenantAndModiefiesIt() {

		Tenant newTenant = createTenant();
		Tenant savedTenant = tenantRepository.save(newTenant);
		Optional<Tenant> reloadedTenant = tenantRepository.findById(savedTenant.getId());

		assertThat(reloadedTenant).isNotEmpty();
		assertThat(reloadedTenant.get().getId()).isEqualTo(savedTenant.getId());

		Tenant modifiedTenant = reloadedTenant.get();
		modifiedTenant.setName("modified");

		tenantRepository.save(reloadedTenant.get());

		modifiedTenant = tenantRepository.getById(modifiedTenant.getId());
		assertThat(modifiedTenant.getName()).isEqualTo("modified");
	}

	private Tenant createTenant() {
		return Tenant.builder().name("Tenant 1").build();
	}

}
