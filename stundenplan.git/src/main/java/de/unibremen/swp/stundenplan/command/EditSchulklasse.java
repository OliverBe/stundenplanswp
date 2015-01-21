package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse f�r das Bearbeiten einer Schulklasse in der DB.
 * @author Roman
 *
 */
public class EditSchulklasse implements Command, EditCommand {

	/**
	 * SK vor dem Bearbeiten.
	 */
	private Schoolclass urspruenglich;
	/**
	 * Person, wie sie nach dem Bearbeiten aussehen soll.
	 */
	private Schoolclass bearbeitet;

	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. F�gt dieses EditCOmmand Objekt an
	 * CommandHistory an.
	 * @param name
	 * 		Name der Sk, die bearbeitet werden soll
	 * @param schoolclass
	 *		Objekt-Zustand, den zu bearbeitende SK nach Bearbeiten erreichen soll.
	 */	
	public void execute(String name, Schoolclass schoolclass){
		urspruenglich = DataSchulklasse.getSchulklasseByName(name);
		bearbeitet = schoolclass;
		DataSchulklasse.editSchulklasse(name, schoolclass);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, �berschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		DataSchulklasse.editSchulklasse(bearbeitet.getName(), urspruenglich);
	}

}
