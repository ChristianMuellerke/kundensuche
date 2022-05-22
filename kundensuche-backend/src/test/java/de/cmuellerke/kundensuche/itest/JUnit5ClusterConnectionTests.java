package de.cmuellerke.kundensuche.itest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.cmuellerke.kundensuche.itest.common.ClusterConnectionInfo;
import de.cmuellerke.kundensuche.itest.common.IntegrationTest;

/**
 * Testing the setup and parameter injection of the CusterConnectionInfo.
 *
 * @author Peter-Josef Meisch
 */
@IntegrationTest
@DisplayName("a sample JUnit 5 test with a bare cluster connection")
public class JUnit5ClusterConnectionTests {

	@Test
	@DisplayName("should have the connection info injected")
	void shouldHaveTheConnectionInfoInjected(ClusterConnectionInfo clusterConnectionInfo) {
		assertThat(clusterConnectionInfo).isNotNull();
	}
}
