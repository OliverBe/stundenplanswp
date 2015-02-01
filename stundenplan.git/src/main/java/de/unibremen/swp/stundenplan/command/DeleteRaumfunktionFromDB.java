package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Loeschen einer Raumfunktion aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumfunktionFromDB implements Command {

	/**
	 * RF, die geloescht werden soll.
	 */
	private Raumfunktion r;
	
	public DeleteRaumfunktionFromDB(){
		
	}
	
	/**
	 * Leitet Loeschanfrage an die Datenbank weiter, mit dem Uebergebenem
	 * Kuerzel. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name der RF, die geloescht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumfunktionByName(rName);
		DataRaum.deleteRaumfunktionByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfuegen-Anfrage an Datenbank weiter mit der Person, die geloescht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaumfunktion(r);
	}

}
