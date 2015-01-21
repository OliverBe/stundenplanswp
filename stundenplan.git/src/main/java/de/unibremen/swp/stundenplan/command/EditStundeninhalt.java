package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Command-Klasse für das Bearbeiten eines SI in der DB.
 * @author Roman
 *
 */
public class EditStundeninhalt implements Command, EditCommand {

	/**
	 * SI vor dem Bearbeiten.
	 */
	private Stundeninhalt urspruenglich;
	/**
	 * SI, wie er nach dem Bearbeiten aussehen soll.
	 */
	private Stundeninhalt bearbeitet;
	
	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. Fügt dieses EditCOmmand Objekt an
	 * CommandHistory an.
	 * @param pKuerzel
	 * 		Kuerzel des SI, der bearbeitet werden soll
	 * @param inhalt
	 *		Objekt-Zustand, den zu bearbeitende SI nach Bearbeiten erreichen soll.
	 */	
	public void execute(String pKuerzel, Stundeninhalt inhalt){
		urspruenglich = DataStundeninhalt.getStundeninhaltByKuerzel(pKuerzel);
		bearbeitet = inhalt;
		DataStundeninhalt.editStundeninhalt(pKuerzel, inhalt);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, überschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.editStundeninhalt(bearbeitet.getKuerzel(), urspruenglich);
	}

}
