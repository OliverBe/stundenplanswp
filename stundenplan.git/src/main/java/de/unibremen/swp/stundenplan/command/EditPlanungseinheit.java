package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

/**
 * Command-Klasse fuer das Bearbeiten einer Planungseinheit in der DB.
 * @author Roman
 *
 */
public class EditPlanungseinheit implements Command, EditCommand {
	/**
	 * PE vor dem Bearbeiten.
	 */
	private Planungseinheit urspruenglich;
	/**
	 * PE, wie sie nach dem Bearbeiten aussehen soll.
	 */
	private Planungseinheit bearbeitet;
	
	/**
	 * Leitet Edit-Anfrage an Datenbank weiter. Speichert urspruengliches Objekt und
	 * das Objekt, wie es nach Bearbeiten sein soll. Fuegt dieses EditCommand Objekt an
	 * CommandHistory an.
	 * @param iD
	 * 		ID der PE, die bearbeitet werden soll
	 * @param pl
	 *		Objekt-Zustand, den zu bearbeitende Person nach Bearbeiten erreichen soll.
	 */	
	public void execute(int iD, Planungseinheit pl){
		urspruenglich = DataPlanungseinheit.getPlanungseinheitById(iD);
		bearbeitet = pl;
		DataPlanungseinheit.editPlanungseinheit(iD, pl);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Edit-Anfrage an DB weiter. Bearbeitet neues Objekt, ueberschreibt es mit vorigem
	 * Objekt-Zustand.
	 */
	@Override
	public void undo() {
		DataPlanungseinheit.editPlanungseinheit(bearbeitet.getId(), urspruenglich);
	}

}
