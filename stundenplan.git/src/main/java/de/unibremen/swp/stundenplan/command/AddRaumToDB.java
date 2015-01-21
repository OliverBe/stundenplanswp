package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einf�gen eines Raumes in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumToDB implements Command {
	
	/**
	 * Raum der eingef�gt wird.
	 */
	private Room r;
	
	public AddRaumToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. F�gt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingef�gten Raum als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param room
     * 		Raum dier hinzugef�gt werden soll.
     */
	public void execute(Room room){
		r = room;
		DataRaum.addRaum(room);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumByName(r.getName());
	}

}
