package de.cmuellerke.testdata.persons.beans;

import java.time.LocalDate;
import java.util.List;

public class Person {
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
