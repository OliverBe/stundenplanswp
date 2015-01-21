package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse für das Bearbeiten einer Person in der DB.
 * @author Roman
 *
 */
public class EditRaum implements Command, EditCommand {

	/**
	 * Raum vor dem Bearbeiten.
	 */
	private Room urspruenglich;
	/**
	 * Raum, wie er nach dem Bearbeiten aussehen soll.
	 */
	private Room bearbeitet;
	
	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. Fügt dieses EditCOmmand Objekt an
	 * CommandHistory an.
	 * @param name
	 * 		Name des Raums, der bearbeitet werden soll
	 * @param perso
	 *		Objekt-Zustand, den zu bearbeitende Raum nach Bearbeiten erreichen soll.
	 */	
	public void execute(String name, Room raum){
		urspruenglich = DataRaum.getRaumByName(name);
		bearbeitet = raum;
		DataRaum.editRaum(name, raum);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, überschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		DataRaum.editRaum(bearbeitet.getName(), urspruenglich);
	}

}
