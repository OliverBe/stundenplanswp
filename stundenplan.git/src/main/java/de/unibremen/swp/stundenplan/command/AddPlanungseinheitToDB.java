package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

/**
 * Command-Klasse zum Einfügen einer Planungseinheit in die Datenbank.
 * @author Roman
 *
 */
public class AddPlanungseinheitToDB implements Command {

	/**
	 * PE, die eingefügt wird.
	 */
	private Planungseinheit einheit;
	
	public AddPlanungseinheitToDB(){
		
	}
	
	/**
     * Execute-Methode dieser Klasse. Fügt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefügte PE als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param p1
     * 		PE die hinzugefügt werden soll.
     */
	public void execute(Planungseinheit pl){
		einheit = pl;
		DataPlanungseinheit.addPlanungseinheit(pl);
		CommandHistory.addCommand(this);
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataPlanungseinheit.deletePlanungseinheitById(einheit.getId());
	}

}
