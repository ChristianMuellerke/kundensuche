package de.cmuellerke.testdata.persons.beans;

public class Konto {
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
