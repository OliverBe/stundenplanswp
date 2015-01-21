package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum Löschen einer Raumfunktion aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumfunktionFromDB implements Command {

	/**
	 * RF, die gelöscht werden soll.
	 */
	private Raumfunktion r;
	
	public DeleteRaumfunktionFromDB(){
		
	}
	
	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name der RF, die gelöscht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumfunktionByName(rName);
		DataRaum.deleteRaumfunktionByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit der Person, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaumfunktion(r);
	}

}
