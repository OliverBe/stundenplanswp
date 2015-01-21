package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;
/**
 * Command-Klasse zum L�schen eines Raums aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumFromDB implements Command {

	/**
	 * Raum, der gel�scht werden soll.
	 */
	private Room r;
	
	public DeleteRaumFromDB(){
		
	}
		
	/**
	 * Leitet L�schanfrage an die Datenbank weiter, mit dem �bergebenem
	 * Kuerzel. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name des Raums, der gel�scht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumByName(rName);
		DataRaum.deleteRaumByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einf�gen-Anfrage an Datenbank weiter mit der Person, die gel�scht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaum(r);
	}
}
