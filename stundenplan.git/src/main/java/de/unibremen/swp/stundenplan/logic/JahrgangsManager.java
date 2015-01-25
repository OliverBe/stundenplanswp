package de.unibremen.swp.stundenplan.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import de.unibremen.swp.stundenplan.command.AddJahrgangToDB;
import de.unibremen.swp.stundenplan.command.DeleteJahrgangsBedarfFromDB;
import de.unibremen.swp.stundenplan.command.EditJahrgangsBedarf;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class JahrgangsManager {

	public JahrgangsManager() {

	}

	public static void addBedarfToJahrgang(final Jahrgang jahrgang) {
		AddJahrgangToDB addJrg = new AddJahrgangToDB();
		addJrg.execute(jahrgang);
	}

	public static void editBedarfFromJahrgang(final Jahrgang jahrgang) {
		EditJahrgangsBedarf editJBdf = new EditJahrgangsBedarf();
		editJBdf.execute(jahrgang);
	}

	public static void deleteBedarfFromJahrgang(final Jahrgang jahrgang,
			final String kuerzelVonInhalt) {
		DeleteJahrgangsBedarfFromDB deleteBdf = new DeleteJahrgangsBedarfFromDB();
		deleteBdf.execute(jahrgang, kuerzelVonInhalt);
	}

	/**
	 * Gibt Liste mit allen Jahrgaengen in der DB zurück. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Jahrgang> getAllJahrgangFromDB() {
		ArrayList<Jahrgang> jahrgang = DataSchulklasse.getAllJahrgang();

		if (jahrgang.size() == 0) {
			System.out.println("No Jahrgang in DB");
		} else {
			System.out.println("Jahrgang in DB: ");
			for (int i = 0; i < jahrgang.size(); i++) {
				System.out.println(jahrgang.get(i));
			}
		}
		return jahrgang;
	}
	
	public static Jahrgang getJahrgangByJahrgangFromDB(int jahrgang) {
		Jahrgang jg = DataSchulklasse.getJahrgangByJahrgang(jahrgang);

		if (jg == null) {
			System.out.println("No Jahrgang in DB");
		} else {
			System.out.println("Jahrgang in DB: ");
			System.out.println(jg);
		}
		return jg;
	}
	
	public static Jahrgang getJahrgangByJundSkuerzelFromDB(int jahrgang, String stundeninhalt_kuerzel) {
		Jahrgang jg = DataSchulklasse.getJahrgangByJundSkuerzel(jahrgang, stundeninhalt_kuerzel);

		if (jg == null) {
			System.out.println("No Jahrgang in DB");
		} else {
			System.out.println("Jahrgang in DB: ");
			System.out.println(jg);
		}
		return jg;
	}
}
