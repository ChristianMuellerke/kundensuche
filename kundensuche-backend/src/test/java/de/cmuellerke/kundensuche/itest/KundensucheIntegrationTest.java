/*
 * Copyright 2022 the original author or authors.
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
package de.cmuellerke.kundensuche.itest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.cmuellerke.kundensuche.entity.Kunde;
import de.cmuellerke.kundensuche.service.KundensucheService;
import de.cmuellerke.kundensuche.service.SearchService;

@de.cmuellerke.kundensuche.itest.common.SpringIntegrationTest
class KundensucheIntegrationTest {

	@Autowired
	private KundensucheService kundensucheService;
	
	@Test
	void savesKunde() {
		Kunde kunde = new Kunde();

		kunde.setKurzname("Test Tester");
		kunde.setVorname("Test");
		kunde.setNachname("Tester");

		kundensucheService.saveToIndex(kunde );
	}
}
