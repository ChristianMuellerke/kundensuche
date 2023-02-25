package de.cmuellerke.kundenverwaltung;

import java.time.LocalDateTime;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionTemplate;

import de.cmuellerke.kundenverwaltung.entity.KundeEntity;
import de.cmuellerke.kundenverwaltung.entity.TenantIdentifierResolver;
import de.cmuellerke.kundenverwaltung.repository.KundenRepository;


@SpringBootTest
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
class ApplicationTests implements WithAssertions {
	@Autowired
	KundenRepository kunden;

	@Autowired
	TransactionTemplate txTemplate;

	@Autowired
	TenantIdentifierResolver currentTenant;

	public static final String TENANT_1 = "T1";
	public static final String TENANT_2 = "T2";

	@AfterEach
	void afterEach() {
		currentTenant.setCurrentTenant(TENANT_2);
		kunden.deleteAll();
		currentTenant.setCurrentTenant(TENANT_1);
		kunden.deleteAll();
	}

	@Test
	void saveAndLoadPerson() {

		final KundeEntity cm = createKunde(TENANT_1, "CM", "cm@unittest.de");
		final KundeEntity jm = createKunde(TENANT_2, "JM", "jm@unittest.de");

		assertThat(cm.getTenantId()).isEqualTo(TENANT_1);
		assertThat(jm.getTenantId()).isEqualTo(TENANT_2);

		currentTenant.setCurrentTenant(TENANT_2);
		assertThat(kunden.findAll()).extracting(KundeEntity::getName).containsExactly("JM");

		currentTenant.setCurrentTenant(TENANT_1);
		assertThat(kunden.findAll()).extracting(KundeEntity::getName).containsExactly("CM");
	}

	private KundeEntity createKunde(String tenant, String name, String email) {
		currentTenant.setCurrentTenant(tenant);

		KundeEntity kunde = txTemplate.execute(tx -> {
			KundeEntity person = KundeEntity.builder().name(name).email(email)/*.createdAt(LocalDateTime.now()).modifiedAt(LocalDateTime.now())*/.build();
			return kunden.save(person);
		});

		assertThat(kunde.getCustomerId()).isNotNull();
		return kunde;
	}
	
	@Test
	void findById() {

		final KundeEntity cm = createKunde(TENANT_1, "CM", "cm@unittest.de");
		final KundeEntity jm = createKunde(TENANT_2, "JM", "jm@unittest.de");

		currentTenant.setCurrentTenant(TENANT_2);
		assertThat(kunden.findById(jm.getCustomerId()).get().getTenantId()).isEqualTo(TENANT_2);
		assertThat(kunden.findById(cm.getCustomerId())).isEmpty();
	}
	
	@Test
	void queryJPQL() {

		final KundeEntity cm = createKunde(TENANT_1, "CM", "cm@unittest.de");
		final KundeEntity jm = createKunde(TENANT_2, "JM", "jm@unittest.de");
		final KundeEntity em = createKunde(TENANT_2, "EM", "em@unittest.de");

		
		currentTenant.setCurrentTenant(TENANT_1);
		assertThat(kunden.findJpqlByEmail(cm.getEmail()).getTenantId()).isEqualTo(TENANT_1);

		currentTenant.setCurrentTenant(TENANT_2);
		assertThat(kunden.findJpqlByEmail(cm.getEmail())).isNull();
	}
	
	@Test
	void querySQL() {

		final KundeEntity cm = createKunde(TENANT_1, "CM", "cm@unittest.de");
		final KundeEntity jm = createKunde(TENANT_2, "CM", "cm@unittest.de");

		currentTenant.setCurrentTenant(TENANT_1);
		assertThatThrownBy(() -> kunden.findSqlByEmail(cm.getEmail())).isInstanceOf(IncorrectResultSizeDataAccessException.class);
	}
}
