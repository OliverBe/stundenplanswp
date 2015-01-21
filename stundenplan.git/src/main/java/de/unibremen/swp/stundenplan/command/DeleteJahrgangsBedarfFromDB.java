package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
/**
 * Comand-Klasse zum L�schen von Jahrgangsbedarfen.
 * @author Roman
 *
 */
public class DeleteJahrgangsBedarfFromDB implements Command {
	/**
	 * Jahrgang, f�r den der neue Bedarf festgelegt werden soll.
	 */
	private Jahrgang j;
	
	/**
	 * Leitet L�schanfrage an Datenbank weiter. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param jahrgang
	 * 		Der Jahrgang, f�r den der Bedarf gel�scht werden soll
	 * @param kuerzel
	 *		Kuerzel des Stundeininhalts, dessen Bedarf gel�scht werden soll.
	 */
	public void execute(Jahrgang jahrgang, String kuerzel){
		j = jahrgang;
		DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(jahrgang.getJahrgang(), kuerzel);
		CommandHistory.addCommand(this);
	}

	/**
	 * F�gt den Jahrgang inklusive Jahrgangsbedarf wieder der DB hinzu, so wie er war,
	 * bevor der Bedarf gel�scht wurde.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataSchulklasse.addJahrgang(j);
	}

}
