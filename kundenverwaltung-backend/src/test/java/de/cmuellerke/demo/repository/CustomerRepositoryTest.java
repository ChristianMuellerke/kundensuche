package de.cmuellerke.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.cmuellerke.demo.DatabaseIntegrationTest;
import de.cmuellerke.demo.data.entity.Customer;
import de.cmuellerke.demo.data.entity.Customer;

@DatabaseIntegrationTest
public class CustomerRepositoryTest implements WithAssertions {
	
	private final static LocalDate DAY_OF_BIRTH = LocalDate.of(1980, 03, 15);
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private CustomerRepository customerRepository;

	@BeforeEach
	void deleteAllUsers() {
		customerRepository.deleteAll();
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(customerRepository).isNotNull();
	}

	@Test
	void savesNewCustomer() {

		Customer newCustomer = createCustomer();
		Customer savedCustomer = customerRepository.save(newCustomer);

		assertThat(savedCustomer.getId()).isGreaterThan(-1);
		assertThat(savedCustomer.getFirstName()).isEqualTo(newCustomer.getFirstName());
		assertThat(savedCustomer.getLastName()).isEqualTo(newCustomer.getLastName());
	}

	@Test
	void savesNewTenantAndReloadsIt() {

		Customer newCustomer = createCustomer();
		Customer savedCustomer = customerRepository.save(newCustomer);
		Optional<Customer> reloadedCustomer = customerRepository.findById(savedCustomer.getId());

		assertThat(reloadedCustomer).isNotEmpty();
		assertThat(reloadedCustomer.get().getId()).isEqualTo(savedCustomer.getId());
	}

	@Test
	void savesNewTenantAndDeletesIt() {

		Customer newCustomer = createCustomer();
		Customer savedCustomer = customerRepository.save(newCustomer);
		Optional<Customer> reloadedCustomer = customerRepository.findById(savedCustomer.getId());

		assertThat(reloadedCustomer).isNotEmpty();
		assertThat(reloadedCustomer.get().getId()).isEqualTo(savedCustomer.getId());

		customerRepository.delete(reloadedCustomer.get());
	}

	@Test
	void savesNewTenantAndModiefiesIt() {
		Customer newCustomer = createCustomer();
		Customer savedCustomer = customerRepository.save(newCustomer);
		Optional<Customer> reloadedCustomer = customerRepository.findById(savedCustomer.getId());

		assertThat(reloadedCustomer).isNotEmpty();
		assertThat(reloadedCustomer.get().getId()).isEqualTo(savedCustomer.getId());

		Customer modifiedTenant = reloadedCustomer.get();
		modifiedTenant.setFirstName("modified");

		customerRepository.save(reloadedCustomer.get());

		modifiedTenant = customerRepository.getById(modifiedTenant.getId());
		assertThat(modifiedTenant.getFirstName()).isEqualTo("modified");
	}

	private Customer createCustomer() {
		return Customer.builder().firstName("CustFirstName").lastName("CustLastName").dayOfBirth(DAY_OF_BIRTH).build();
	}

}
