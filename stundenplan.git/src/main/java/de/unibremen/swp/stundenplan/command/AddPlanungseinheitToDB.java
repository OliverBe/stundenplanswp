package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

/**
 * Command-Klasse zum Einf�gen einer Planungseinheit in die Datenbank.
 * @author Roman
 *
 */
public class AddPlanungseinheitToDB implements Command {

	/**
	 * PE, die eingef�gt wird.
	 */
	private Planungseinheit einheit;
	
	public AddPlanungseinheitToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. F�gt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingef�gte PE als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param p1
     * 		PE die hinzugef�gt werden soll.
     */
	public void execute(Planungseinheit pl){
		einheit = pl;
		DataPlanungseinheit.addPlanungseinheit(pl);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataPlanungseinheit.deletePlanungseinheitById(einheit.getId());
	}

}
