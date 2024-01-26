package de.cmuellerke.testdata.persons;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
			"Marvin", "Carl", "Mike", "Friedhelm", "Torben", "Muhammed", "Brad", "Lars", "Christoph", "Olaf");
	
	final static List<String> vornamenWeiblich = Arrays.asList("Gabi", "Birgit", "Pia", "Mia", "Conny", "Bibi", "Tanja",
			"Julia", "Emma", "Maria", "Emily", "Paula", "Kerstin", "Steffi", "Anna", "Annalena", "Lena", "Birgit",
			"Maxi", "Johanna", "Erna", "Sophia", "Lina", "Ella", "Clara", "Lea", "Mina", "Sabine", "Sina", "Silke",
			"Xantippe", "Agathe", "Stefanie", "Saskia", "Alina", "Maxima", "Gerda", "Gertrud", "Brunhilde", "Babette",
			"Emilia", "Hannah", "Nadine", "Claudia", "Kathy", "Kathleen", "Doreen", "Cindy", "Sofia", "Sophie",
			"Sophia", "Lina", "Ella", "Mila", "Clara", "Klara", "Lea", "Bärbel", "Steffi", "Stefanie", "Stephanie",
			"Carolin", "Caroline", "Caro", "Karlina", "Aishe", "Xenia", "Sia", "Lia", "Cia", "Eva", "Brigitte");
	
	final static List<String> nachnamen = Arrays.asList("Meier", "Müller", "Schulze", "Fischer", "Becker", "Goldsmith",
			"Simpson", "Beispiel", "Jones", "Brand", "Mauser", "Bäcker", "Holgerson", "Lehmann", "Mosel", "Schneider",
			"Meyer", "Wagner", "Hofmann", "Schulz", "Bauer", "Richter", "Klein", "Groß", "Schröder", "Wolf", "Neumann",
			"Schwarz", "Grünspecht", "Schmitz", "Schmidt", "Zimmermann", "Krüger", "Braun", "Schmitt", "Krause",
			"Hartmann", "Lange", "Werner", "Köhler", "Huber", "Fuchs", "Blocksberg", "Scholz", "Kaiser", "Jung",
			"Vogel", "Friedrich", "Günther", "Ginter", "Schubert", "Schuhmacher", "Roth", "Beck", "Lorenz", "Franke",
			"Holzmann", "Simon", "Schuster", "Krause", "Otto", "von der Schippe", "Plötzenich", "Pulonski", "Mülner", 
			"Milner", "Moier", "Küstenschreck", "Pistorius", "Pitt", "Schmitt", "Fetenkracher", "Pastor", "Palin", "Biden",
			"Snider");

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
			new Stadt("Mannheim", "33333"), //
			new Stadt("Bad Freienwalde", "16248"));

	final static List<String> strassen = Arrays.asList(//
			"Kappenberger Damm 100", //
			"Weseler Str. 444", //
			"Berliner Chaussee 123", //
			"Hauptstr. 342", //
			"Waldweg 3", //
			"Dorfstr. 43", //
			"Allee der Kosmonauten 443");

	public static void main(String args[]) throws IOException {
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
		
//		writeToFile(personen);
	}

	private static void writeToFile(List<Person> personen) throws IOException {
	    FileWriter fw = new FileWriter("personen.csv", true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    
	    for (Person person: personen) {
	    	bw.write(toCSV(person));
	    	bw.newLine();
	    }
	    
	    bw.close();
		
	}

	private static String toCSV(Person person) {
		List<String> values = new ArrayList<>();
		
		values.add(person.getPersonennummer().toString());
		values.add(person.getVorname());
		values.add(person.getNachname());
		values.add(person.getEmail());
		
		return String.join(";", values);
	}
	
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

class Person {
	private String vorname;
	private String nachname;
	private Long personennummer;
	private String email;
	private Adresse adresse;
	private LocalDate geburtsdatum;
	private List<Konto> konten;

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Long getPersonennummer() {
		return personennummer;
	}

	public void setPersonennummer(Long personennummer) {
		this.personennummer = personennummer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}

	public LocalDate getGeburtsdatum() {
		return geburtsdatum;
	}

	public void setGeburtsdatum(LocalDate geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public List<Konto> getKonten() {
		return konten;
	}

	public void setKonten(List<Konto> konten) {
		this.konten = konten;
	}

}

class Stadt {
	private String name;
	private String plz;

	public Stadt(String name, String plz) {
		this.setName(name);
		this.setPlz(plz);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}
}

class Konto {
	private int kontonummer;

	public Konto(int kontonummer) {
		this.kontonummer = kontonummer;
	}

	public int getKontonummer() {
		return kontonummer;
	}

	public void setKontonummer(int kontonummer) {
		this.kontonummer = kontonummer;
	}
}

class Adresse {
	String strasse;
	String ort;
	String postleitzahl;
	String land;

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getPostleitzahl() {
		return postleitzahl;
	}

	public void setPostleitzahl(String postleitzahl) {
		this.postleitzahl = postleitzahl;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}
}
