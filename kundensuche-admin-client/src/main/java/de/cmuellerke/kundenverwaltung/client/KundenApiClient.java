package de.cmuellerke.kundenverwaltung.client;

import java.util.List;

import de.cmuellerke.kundenverwaltung.handler.ApiException;
import de.cmuellerke.kundenverwaltung.handler.KundenControllerApi;
import de.cmuellerke.kundenverwaltung.model.Kunde;

public class KundenApiClient {

	public void getKunden() {
		try {
			KundenControllerApi api = new KundenControllerApi();
			api.getApiClient().setBasePath("http://localhost:8080");
			List<Kunde> kunden = api.getKunden();
			kunden.forEach(kunde -> {
				System.out.println("Kunde " + kunde.getId() + " " + kunde.getVorname() + " " + kunde.getNachname());
			});
		} catch (ApiException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
