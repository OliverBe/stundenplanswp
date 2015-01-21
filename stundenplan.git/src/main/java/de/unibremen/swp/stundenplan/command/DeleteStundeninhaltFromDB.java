package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum L�schen eines Stundeninhalts aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteStundeninhaltFromDB implements Command{
	
	/**
	 * SI, der gel�scht werden soll.
	 */
	private Stundeninhalt st;
	
	/**
	 * Leitet L�schanfrage an die Datenbank weiter, mit dem �bergebenem
	 * Kuerzel. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param s
	 * 		Kuerzel des SI, der gel�scht werden soll.
	 */
	public void execute(String s){
		st = DataStundeninhalt.getStundeninhaltByKuerzel(s);
		DataStundeninhalt.deleteStundeninhaltByKuerzel(s);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einf�gen-Anfrage an Datenbank weiter mit des SI, die gel�scht wurde.
	 */
	@Override
	public void undo() {
		DataStundeninhalt.addStundeninhalt(st);
	}
}
