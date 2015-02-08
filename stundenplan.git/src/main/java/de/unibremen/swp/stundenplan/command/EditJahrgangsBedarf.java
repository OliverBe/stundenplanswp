package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Bearbeiten von Jahrgangsbedarfen.
 * @author Roman
 *
 */
public class EditJahrgangsBedarf implements Command, EditCommand {

	/**
	 * Jahrgang, wie er vor dem Bearbeiten aussah.
	 */
	private Jahrgang urspruenglich;
	/**
	 * Jahrgang, wie er nach Bearbeiten aussehen soll.
	 */
	private Jahrgang bearbeitet;
	
	/**
	 * Leitet Edit-Anfrage an DB weiter. speichert den jetzigen Jahrgang in urspurenglich.
	 * Speichert den neuen Jahrgang in bearbeitet. Fuegt dieses EditCommandObjekt der CommandHistory
	 * hinzu.
	 * @param neu
	 * 		Jahrgangsbedarf, wie er nach bearbeiten sein soll.
	 */
	public void execute(Jahrgang neu) {
		urspruenglich = DataSchulklasse.getJahrgangByJahrgang(neu.getJahrgang());
		bearbeitet = neu;
		DataSchulklasse.editJahrgang(bearbeitet);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Fuegt alten Jahrgang hinzu.
	 */
	@Override
	public void undo() {
		DataSchulklasse.editJahrgang(urspruenglich);
	}

}
