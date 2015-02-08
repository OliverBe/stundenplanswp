package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
/**
 * Comand-Klasse zum Loeschen von Jahrgangsbedarfen.
 * @author Roman
 *
 */
public class DeleteJahrgangsBedarfFromDB implements Command {
	/**
	 * Jahrgang, fuer den der neue Bedarf festgelegt werden soll.
	 */
	private Jahrgang j;
	
	/**
	 * Leitet Loeschanfrage an Datenbank weiter. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param jahrgang
	 * 		Der Jahrgang, fuer den der Bedarf geloescht werden soll
	 * @param kuerzel
	 *		Kuerzel des Stundeininhalts, dessen Bedarf geloescht werden soll.
	 */
	public void execute(Jahrgang jahrgang, String kuerzel){
		j = jahrgang;
		DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(jahrgang.getJahrgang(), kuerzel);
		CommandHistory.addCommand(this);
	}

	/**
	 * Fuegt den Jahrgang inklusive Jahrgangsbedarf wieder der DB hinzu, so wie er war,
	 * bevor der Bedarf geloescht wurde.
	 */
	@Override
	public void undo() {
		DataSchulklasse.addJahrgang(j);
	}

}
