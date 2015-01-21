package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Einfügen eines Raumes in die Datenbank.
 * @author Roman
 *
 */
public class AddRaumToDB implements Command {
	
	/**
	 * Raum der eingefügt wird.
	 */
	private Room r;
	
	public AddRaumToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. Fügt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefügten Raum als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param room
     * 		Raum dier hinzugefügt werden soll.
     */
	public void execute(Room room){
		r = room;
		DataRaum.addRaum(room);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataRaum.deleteRaumByName(r.getName());
	}

}
