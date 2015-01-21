package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
/**
 * Comand-Klasse zum Löschen von Jahrgangsbedarfen.
 * @author Roman
 *
 */
public class DeleteJahrgangsBedarfFromDB implements Command {
	/**
	 * Jahrgang, für den der neue Bedarf festgelegt werden soll.
	 */
	private Jahrgang j;
	
	/**
	 * Leitet Löschanfrage an Datenbank weiter. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param jahrgang
	 * 		Der Jahrgang, für den der Bedarf gelöscht werden soll
	 * @param kuerzel
	 *		Kuerzel des Stundeininhalts, dessen Bedarf gelöscht werden soll.
	 */
	public void execute(Jahrgang jahrgang, String kuerzel){
		j = jahrgang;
		DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(jahrgang.getJahrgang(), kuerzel);
		CommandHistory.addCommand(this);
	}

	/**
	 * Fügt den Jahrgang inklusive Jahrgangsbedarf wieder der DB hinzu, so wie er war,
	 * bevor der Bedarf gelöscht wurde.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataSchulklasse.addJahrgang(j);
	}

}
