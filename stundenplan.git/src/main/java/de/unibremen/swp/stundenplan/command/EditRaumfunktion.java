package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse für das Bearbeiten einer Raumfunktion in der DB.
 * @author Roman
 *
 */
public class EditRaumfunktion implements Command, EditCommand {

	/**
	 * RF vor dem Bearbeiten.
	 */
	private Raumfunktion urspruenglich;
	/**
	 * Person, wie sie nach dem Bearbeiten aussehen soll.
	 */
	private Raumfunktion bearbeitet;
	
	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. Fügt dieses EditCOmmand Objekt an
	 * CommandHistory an.
	 * @param name
	 * 		Name der RF, die bearbeitet werden soll.
	 * @param rf
	 *		Objekt-Zustand, den zu bearbeitende RF nach Bearbeiten erreichen soll.
	 */		
	public void execute(String name, Raumfunktion rf){
		urspruenglich = DataRaum.getRaumfunktionByName(name);
		bearbeitet = rf;
		DataRaum.editRaumfunktion(name, rf);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, überschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataRaum.editRaumfunktion(bearbeitet.getName(), urspruenglich);
	}

}
