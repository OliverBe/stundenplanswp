package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;
/**
 * Command-Klasse zum Loeschen eines Raums aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumFromDB implements Command {

	/**
	 * Raum, der geloescht werden soll.
	 */
	private Room r;
	
	public DeleteRaumFromDB(){
		
	}
		
	/**
	 * Leitet Loeschanfrage an die Datenbank weiter, mit dem Uebergebenem
	 * Kuerzel. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name des Raums, der geloescht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumByName(rName);
		DataRaum.deleteRaumByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfuegen-Anfrage an Datenbank weiter mit der Person, die geluescht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaum(r);
	}
}
