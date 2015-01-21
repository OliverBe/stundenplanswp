package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
/**
 * Command-Klasse zum Löschen einer Person aus der Datenbank.
 * @author Roman
 *
 */
public class DeletePlanungseinheitFromDB implements Command {
	
	/**
	 * PE, die gelöscht werden soll.
	 */
	private Planungseinheit pl;

	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param i
	 * 		ID der PE, die gelöscht werden soll.
	 */
	public void execute(int i){
		ArrayList<Planungseinheit> pEinheiten = DataPlanungseinheit.getAllPlanungseinheit();
		for(Planungseinheit p : pEinheiten){
			if(p.getId() == i) pl = p;
		}
		DataPlanungseinheit.deletePlanungseinheitById(i);
		CommandHistory.addCommand(this);
	}
	
	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit der PE, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataPlanungseinheit.addPlanungseinheit(pl);
	}

}
