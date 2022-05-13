package de.cmuellerke.testdata.persons.beans;

public class Stadt {
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
