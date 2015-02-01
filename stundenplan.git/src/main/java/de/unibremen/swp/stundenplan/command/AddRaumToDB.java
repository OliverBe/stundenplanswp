package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einfuegen eines Raumes in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumToDB implements Command {
	
	/**
	 * Raum der eingefuegt wird.
	 */
	private Room r;
	
	public AddRaumToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. Fuegt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefuegten Raum als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param room
     * 		Raum dier hinzugefuegt werden soll.
     */
	public void execute(Room room){
		r = room;
		DataRaum.addRaum(room);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumByName(r.getName());
	}

}
