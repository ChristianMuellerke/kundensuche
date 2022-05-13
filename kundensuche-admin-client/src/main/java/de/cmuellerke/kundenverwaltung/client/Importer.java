package de.cmuellerke.kundenverwaltung.client;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.cmuellerke.kundenverwaltung.handler.ApiException;
import de.cmuellerke.kundenverwaltung.handler.KundenControllerApi;
import de.cmuellerke.kundenverwaltung.model.Kunde;
import de.cmuellerke.testdata.persons.Personengenerator;
import de.cmuellerke.testdata.persons.beans.Person;

public class Importer {

	public void kundenImportieren() {
		kundenImportieren(-1);
	}

	public void kundenImportieren(int maxKunden) {
		List<Person> personen = new Personengenerator().erzeugePersonen();
		System.out.println("Anzahl erzeugte Testpersonen: " + personen.size());

		if (maxKunden > 0) {
			System.out.println("Es werden aber nur die ersten : " + maxKunden + " importiert!");
			personen = personen.subList(0, maxKunden);
		}

		KundenControllerApi api = new KundenControllerApi();
		api.getApiClient().setBasePath("http://localhost:8080");

		AtomicInteger counter = new AtomicInteger();
		personen.stream().forEach(person -> {
			Kunde kunde = new Kunde();
			kunde.setVorname(person.getVorname());
			kunde.setNachname(person.getNachname());
			try {
				String id = api.createKunde(kunde);
				System.out.println(
						"Kunde mit Id " + id + "angelegt. (imported " + counter.incrementAndGet() + " persons)");
			} catch (ApiException e) {
				System.out.println("Import failed: " + e.getMessage());
			}
		});
	}
}
