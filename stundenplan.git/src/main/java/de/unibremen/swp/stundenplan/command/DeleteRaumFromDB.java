package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;
/**
 * Command-Klasse zum Löschen eines Raums aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumFromDB implements Command {

	/**
	 * Raum, der gelöscht werden soll.
	 */
	private Room r;
	
	public DeleteRaumFromDB(){
		
	}
		
	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name des Raums, der gelöscht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumByName(rName);
		DataRaum.deleteRaumByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit der Person, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaum(r);
	}
}
