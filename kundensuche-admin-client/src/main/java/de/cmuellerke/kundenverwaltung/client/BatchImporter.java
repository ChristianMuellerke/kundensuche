package de.cmuellerke.kundenverwaltung.client;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

import de.cmuellerke.kundenverwaltung.handler.ApiException;
import de.cmuellerke.kundenverwaltung.handler.KundenControllerApi;
import de.cmuellerke.kundenverwaltung.model.Kunde;
import de.cmuellerke.testdata.persons.Personengenerator;
import de.cmuellerke.testdata.persons.beans.Person;

public class BatchImporter {
	public void kundenImportieren() {
		List<Person> personen = new Personengenerator().erzeugePersonen();
		System.out.println("Anzahl erzeugte Testpersonen: " + personen.size());
		KundenControllerApi api = new KundenControllerApi();
		api.getApiClient().setBasePath("http://localhost:8080");

		// nur 1000 nehmen
		// personen = personen.subList(0, 1000);

		List<List<Person>> personenStapel = ListUtils.partition(personen, 100);

		personenStapel.stream().forEach(stapel -> {
			List<Kunde> kunden = stapel.stream().map(Person2KundeMapper::toKunde).collect(Collectors.toList());

			try {
				List<String> ids = api.createKunden(kunden);
				StringBuilder sb = new StringBuilder();
				sb.append("Die folgenden " + ids.size() + " Kunden wurden angelegt: ");
				ids.forEach(id -> sb.append(" " + id));
				System.out.println(sb.toString());
			} catch (ApiException e) {
				System.out.println("Import failed.");
			}

		});
	}
}
