package de.cmuellerke.testdata.persons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import de.cmuellerke.testdata.persons.beans.Adresse;
import de.cmuellerke.testdata.persons.beans.Konto;
import de.cmuellerke.testdata.persons.beans.Person;
import de.cmuellerke.testdata.persons.beans.Stadt;

public class Personengenerator {

	final static List<String> vornamenMaennlich = Arrays.asList("Hans", "Peter", "Thomas", "Michael", "Tom", "Uwe",
			"Kai", "Nils", "Frank", "Christian", "Niklas", "Berthold", "Randolf", "Erwin", "Oliver", "Mario", "Steffen",
			"Markus", "Otwin", "Joachim", "Roger", "Tim", "Theo", "Toni", "Albrecht", "Axel", "Ronny", "Stefan",
			"Anton", "Emil", "Linus", "Nathan", "Max", "Leroy", "Miro", "Maik", "Donald", "Dagobert", "Milan", "Moritz",
			"Hannes", "Nikolaus", "Chris", "Stefan", "Stephan", "August", "Ekki", "Elias", "Luis", "Louis", "Linus",
			"Ulf", "Arnold", "Alfred", "Noah", "Finn", "Ben", "Benjamin", "Frederik", "Leon", "Elias", "Paul",
			"Karsten", "Carsten", "Henry", "Bert", "Bertram", "Bertold", "Benedikt", "Friedemann", "Fritz", "Otto",
			"Karl", "Gerold", "Jonas", "Yusuf", "Mohamed", "Mohammed", "Mario", "Kristian", "Hans-Peter", "Karl-Gustav",
			"Gustav", "Stefan", "Stephan", "Moritz", "Max Moritz", "Antonius", "Waldemar", "Norman", "Ivan", "Martin", 
			"Marvin", "Carl", "Mike", "Friedhelm");
	final static List<String> vornamenWeiblich = Arrays.asList("Gabi", "Birgit", "Pia", "Mia", "Conny", "Bibi", "Tanja",
			"Julia", "Emma", "Maria", "Emily", "Paula", "Kerstin", "Steffi", "Anna", "Annalena", "Lena", "Birgit",
			"Maxi", "Johanna", "Erna", "Sophia", "Lina", "Ella", "Clara", "Lea", "Mina", "Sabine", "Sina", "Silke",
			"Xantippe", "Agathe", "Stefanie", "Saskia", "Alina", "Maxima", "Gerda", "Gertrud", "Brunhilde", "Babette",
			"Emilia", "Hannah", "Nadine", "Claudia", "Kathy", "Kathleen", "Doreen", "Cindy", "Sofia", "Sophie",
			"Sophia", "Lina", "Ella", "Mila", "Clara", "Klara", "Lea", "Bärbel", "Steffi", "Stefanie", "Stephanie");
	final static List<String> nachnamen = Arrays.asList("Meier", "Müller", "Schulze", "Fischer", "Becker", "Goldsmith",
			"Simpson", "Beispiel", "Jones", "Brand", "Mauser", "Bäcker", "Holgerson", "Lehmann", "Mosel", "Schneider",
			"Meyer", "Wagner", "Hofmann", "Schulz", "Bauer", "Richter", "Klein", "Groß", "Schröder", "Wolf", "Neumann",
			"Schwarz", "Grünspecht", "Schmitz", "Schmidt", "Zimmermann", "Krüger", "Braun", "Schmitt", "Krause",
			"Hartmann", "Lange", "Werner", "Köhler", "Huber", "Fuchs", "Blocksberg", "Scholz", "Kaiser", "Jung",
			"Vogel", "Friedrich", "Günther", "Ginter", "Schubert", "Schuhmacher", "Roth", "Beck", "Lorenz", "Franke",
			"Holzmann", "Simon", "Schuster", "Krause", "Otto", "von der Schippe", "Plötzenich");

	final static List<Stadt> staedte = Arrays.asList(//
			new Stadt("Berlin", "12527"), //
			new Stadt("Köln", "50667"), //
			new Stadt("München", "80331"), //
			new Stadt("Bremen", "28195"), //
			new Stadt("Buxtehude", "21614"), //
			new Stadt("Warendorf", "48231"), //
			new Stadt("Münster", "48163"), //
			new Stadt("Kiel", "24103"), //
			new Stadt("Bremerhaven", "27570"), //
			new Stadt("Hamburg", "20095"), //
			new Stadt("Frankfurt", "60306"), //
			new Stadt("Düsseldorf", "40210"), //
			new Stadt("Greven", "48268"), //
			new Stadt("Stuttgart", "70173"), //
			new Stadt("Bernau", "16321"), //
			new Stadt("Wriezen", "16269"), //
			new Stadt("Bad Freienwalde", "16248"));

	final static List<String> strassen = Arrays.asList(//
			"Kappenberger Damm 100", //
			"Weseler Str. 444", //
			"Berliner Chaussee 123", //
			"Hauptstr. 342", //
			"Waldweg 3", //
			"Dorfstr. 43", //
			"Allee der Kosmonauten 443");

	public List<Person> erzeugePersonen() {
		List<Person> personen = new ArrayList<>();
		AtomicInteger personenzaehler = new AtomicInteger(0);

		for (int staedteIndex = 0; staedteIndex < staedte.size(); staedteIndex++) {
			for (int nachnamenIndex = 0; nachnamenIndex < nachnamen.size(); nachnamenIndex++) {
				for (int vornamenIndex = 0; vornamenIndex < vornamenMaennlich.size(); vornamenIndex++) {
					int aktuellePersonennummer = personenzaehler.incrementAndGet();
					aktuellePersonennummer = aktuellePersonennummer + 1200000;
					personen.add(erzeugePerson(aktuellePersonennummer, vornamenMaennlich.get(vornamenIndex),
							nachnamen.get(nachnamenIndex), staedte.get(staedteIndex)));
				}

				for (int vornamenIndex = 0; vornamenIndex < vornamenWeiblich.size(); vornamenIndex++) {
					int aktuellePersonennummer = personenzaehler.incrementAndGet();
					aktuellePersonennummer = aktuellePersonennummer + 1200000;
					personen.add(erzeugePerson(aktuellePersonennummer, vornamenWeiblich.get(vornamenIndex),
							nachnamen.get(nachnamenIndex), staedte.get(staedteIndex)));
				}
			}
		}

		return personen;
	}

	private Person erzeugePerson(int personenummer, String vorname, String nachname, Stadt stadt) {
		Person person = new Person();

		person.setPersonennummer(Long.valueOf(personenummer));
		person.setVorname(vorname);
		person.setNachname(nachname);

		Adresse adresse = new Adresse();
		adresse.setOrt(stadt.getName());
		adresse.setPostleitzahl(stadt.getPlz());
		adresse.setStrasse(strassen.get(new Random().nextInt(strassen.size())));
		person.setAdresse(adresse);

		List<Konto> konten = Arrays.asList(new Konto(personenummer + 100000000));
		person.setKonten(konten);

		return person;
	}
}
