package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;

/**
 * Command-Klasse fuer das Bearbeiten einer Person in der DB.
 * @author Roman
 *
 */
public class EditPersonal implements Command, EditCommand {

	/**
	 * Person vor dem Bearbeiten.
	 */
	private Personal urspruenglich;
	/**
	 * Person, wie sie nach dem Bearbeiten aussehen soll.
	 */
	private Personal bearbeitet;

	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. Fuegt dieses EditCOmmand Objekt an
	 * CommandHistory an.
	 * @param kuerz
	 * 		Kuerzel der Person, die bearbeitet werden soll
	 * @param perso
	 *		Objekt-Zustand, den zu bearbeitende Person nach Bearbeiten erreichen soll.
	 */		
	public void execute(String kuerz, Personal perso){
		urspruenglich = DataPersonal.getPersonalByKuerzel(kuerz);
		bearbeitet = perso;
		DataPersonal.editPersonal(kuerz, perso);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, ueberschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		DataPersonal.editPersonal(bearbeitet.getKuerzel(), urspruenglich);
	}
}
