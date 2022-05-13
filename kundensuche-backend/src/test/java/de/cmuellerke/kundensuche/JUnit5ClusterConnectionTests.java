/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cmuellerke.kundensuche;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
