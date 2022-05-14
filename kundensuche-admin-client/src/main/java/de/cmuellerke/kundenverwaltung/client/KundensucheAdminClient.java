package de.cmuellerke.kundenverwaltung.client;

public class KundensucheAdminClient {

	static final String LADE_ALLE_KUNDEN = "getKunden";
	static final String BATCHIMPORT = "batchImport";
	static final String IMPORT = "import";
	static final String LADE_KUNDE = "getKunde";

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			switch (args[0]) {
			case LADE_ALLE_KUNDEN:
				new KundenApiClient().getKunden();
				break;
			case IMPORT:
				System.out.println("importiere testdaten...");
				int maxKunden = 0;
				if (args.length > 1) {
					maxKunden = Integer.valueOf(args[1]).intValue();
				}
				new Importer().kundenImportieren(maxKunden);
				break;
			case BATCHIMPORT:
				System.out.println("importiere testdaten (batch)...");
				new BatchImporter().kundenImportieren();
				break;
			default: {
				showHelpAndExit();
			}
			}
		} else {
			showHelpAndExit();
		}
	}

	private static void showHelpAndExit() {
		System.out.println("Benutzung: ");
		System.out.println("  " + LADE_ALLE_KUNDEN + ": Alle Kunden laden und hier ausgeben");
		System.out.println("  " + IMPORT + "<AnzahlKunden>: Kunden importieren");
		System.out.println("  " + BATCHIMPORT + ": Alle Kunden importieren (volle Generierung)");
	}

}
