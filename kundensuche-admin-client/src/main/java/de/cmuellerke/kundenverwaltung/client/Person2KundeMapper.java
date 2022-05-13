package de.cmuellerke.kundenverwaltung.client;

import de.cmuellerke.kundenverwaltung.model.Kunde;
import de.cmuellerke.testdata.persons.beans.Person;

public class Person2KundeMapper {

	public static Kunde toKunde(Person person) {
		Kunde kunde = new Kunde();

		kunde.setVorname(person.getVorname());
		kunde.setNachname(person.getNachname());
		kunde.setKurzname(person.getVorname() + " " + person.getNachname());
		kunde.setOrt(person.getAdresse().getOrt());
		kunde.setPostleitzahl(Integer.valueOf(person.getAdresse().getPostleitzahl()));
		kunde.setStrasse(person.getAdresse().getStrasse());
		kunde.setPersonennummer(Long.valueOf(person.getPersonennummer()));
		kunde.setKonten(person.getKonten().get(0).toString());

		return kunde;
	}
}
