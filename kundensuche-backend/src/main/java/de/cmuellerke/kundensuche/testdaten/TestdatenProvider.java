package de.cmuellerke.kundensuche.testdaten;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.cmuellerke.kundensuche.entity.Kunde;

@Component
public class TestdatenProvider {
	public List<Kunde> createTestkunden() {
		List<Kunde> kunden = new ArrayList<>();
		kunden.add(createKunde("Christian", "Müllerke"));
		kunden.add(createKunde("Christine", "Müller"));
		kunden.add(createKunde("Chris", "Norman"));
		kunden.add(createKunde("Christiane", "Meier"));
		kunden.add(createKunde("Julia", "Müllerke"));
		kunden.add(createKunde("Xaver", "Demitropolis"));
		kunden.add(createKunde("Thomas", "Müllerke"));
		kunden.add(createKunde("Kai", "Müller"));
		kunden.add(createKunde("Uwe", "Muellermann"));
		kunden.add(createKunde("Pia", "Mustermann"));
		kunden.add(createKunde("Tia", "Schulz"));
		kunden.add(createKunde("Lia", "Demmer"));
		return kunden;
	}

	public Kunde createKunde(String vorname, String nachname) {
		Kunde kunde = new Kunde();
		kunde.setKurzname(vorname + " " + nachname);
		kunde.setVorname(vorname);
		kunde.setNachname(nachname);
		return kunde;
	}
}
