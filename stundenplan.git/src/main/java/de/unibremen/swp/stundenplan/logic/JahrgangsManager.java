package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;
import de.unibremen.swp.stundenplan.command.AddJahrgangToDB;
import de.unibremen.swp.stundenplan.command.DeleteJahrgangsBedarfFromDB;
import de.unibremen.swp.stundenplan.command.EditJahrgangsBedarf;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Uebergang von Jahrgängen zu DB und History durch diesen Manager
 * @doc Oliver
 *
 */
public class JahrgangsManager {

	/**
	 * 
	 */
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
	 * Gibt Liste mit allen Jahrgaengen in der DB zurueck. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Jahrgang> getAllJahrgangFromDB() {
		return DataSchulklasse.getAllJahrgang();
	}
	
	public static Jahrgang getJahrgangByJahrgangFromDB(int jahrgang) {
		return DataSchulklasse.getJahrgangByJahrgang(jahrgang);
	}
	
	public static Jahrgang getJahrgangByJundSkuerzelFromDB(int jahrgang, String stundeninhalt_kuerzel) {
		return DataSchulklasse.getJahrgangByJundSkuerzel(jahrgang, stundeninhalt_kuerzel);
	}
}
