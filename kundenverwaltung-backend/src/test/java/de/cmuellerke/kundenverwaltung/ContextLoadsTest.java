package de.cmuellerke.kundenverwaltung;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@WebIntegrationTest
public class ContextLoadsTest implements WithAssertions {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	void contextsLoads() {
		assertThat(applicationContext).isNotNull();
	}
}
