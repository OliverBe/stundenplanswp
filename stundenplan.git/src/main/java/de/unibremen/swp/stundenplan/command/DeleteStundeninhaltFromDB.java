package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum Löschen eines Stundeninhalts aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteStundeninhaltFromDB implements Command{
	
	/**
	 * SI, der gelöscht werden soll.
	 */
	private Stundeninhalt st;
	
	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param s
	 * 		Kuerzel des SI, der gelöscht werden soll.
	 */
	public void execute(String s){
		st = DataStundeninhalt.getStundeninhaltByKuerzel(s);
		DataStundeninhalt.deleteStundeninhaltByKuerzel(s);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit des SI, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		DataStundeninhalt.addStundeninhalt(st);
	}
}
