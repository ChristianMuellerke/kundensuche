package de.cmuellerke.testdata.persons;

import java.util.List;

import de.cmuellerke.testdata.persons.beans.Person;

public class Main {

	public static void main(String args[]) {
		List<Person> personen = new Personengenerator().erzeugePersonen();

		personen.forEach(person -> {
			System.out.println(person.getPersonennummer() + " # " //
					+ person.getVorname() + " " //
					+ person.getNachname() + "    " //
					+ person.getAdresse().getPostleitzahl() + " " //
					+ person.getAdresse().getOrt() + " " //
					+ person.getAdresse().getStrasse() + "");
		});

		System.out.println("generated " + personen.size() + " persons");
	}
}
