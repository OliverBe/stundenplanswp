package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse zum Loeschen eines Stundeninhalts aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteStundeninhaltFromDB implements Command{
	
	/**
	 * SI, der geloescht werden soll.
	 */
	private Stundeninhalt st;
	
	/**
	 * Leitet Loeschanfrage an die Datenbank weiter, mit dem Uebergebenem
	 * Kuerzel. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param s
	 * 		Kuerzel des SI, der geloescht werden soll.
	 */
	public void execute(String s){
		st = DataStundeninhalt.getStundeninhaltByKuerzel(s);
		DataStundeninhalt.deleteStundeninhaltByKuerzel(s);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfuegen-Anfrage an Datenbank weiter mit des SI, die geloescht wurde.
	 */
	@Override
	public void undo() {
		DataStundeninhalt.addStundeninhalt(st);
	}
}
