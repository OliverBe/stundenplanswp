package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

/**
 * Command-Klasse zum Einfuegen einer Planungseinheit in die Datenbank.
 * @author Roman
 *
 */
public class AddPlanungseinheitToDB implements Command {

	/**
	 * PE, die eingefuegt wird.
	 */
	private Planungseinheit einheit;
	
	/**
     * Execute-Methode dieser Klasse. Fuegt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefuegte PE als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param p1
     * 		PE die hinzugefuegt werden soll.
     */
	public void execute(Planungseinheit pl){
		einheit = pl;
		DataPlanungseinheit.addPlanungseinheit(pl);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataPlanungseinheit.deletePlanungseinheitById(einheit.getId());
	}

}
