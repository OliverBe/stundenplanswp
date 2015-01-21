package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

/**
 * Command-Klasse zum L�schen einer Raumfunktion aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteRaumfunktionFromDB implements Command {

	/**
	 * RF, die gel�scht werden soll.
	 */
	private Raumfunktion r;
	
	public DeleteRaumfunktionFromDB(){
		
	}
	
	/**
	 * Leitet L�schanfrage an die Datenbank weiter, mit dem �bergebenem
	 * Kuerzel. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param rName
	 * 		Name der RF, die gel�scht werden soll.
	 */
	public void execute(String rName){
		r = DataRaum.getRaumfunktionByName(rName);
		DataRaum.deleteRaumfunktionByName(rName);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einf�gen-Anfrage an Datenbank weiter mit der Person, die gel�scht wurde.
	 */
	@Override
	public void undo() {
		DataRaum.addRaumfunktion(r);
	}

}
