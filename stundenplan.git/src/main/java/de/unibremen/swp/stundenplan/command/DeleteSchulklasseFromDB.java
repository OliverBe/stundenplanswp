package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum L�schen einer Schulklasse aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteSchulklasseFromDB implements Command {
	
	/**
	 * Schulklasse, die gel�scht werden soll.
	 */
	private Schoolclass schoolClass;

	public DeleteSchulklasseFromDB(){
		
	}

	/**
	 * Leitet L�schanfrage an die Datenbank weiter, mit dem �bergebenem
	 * Kuerzel. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param name
	 * 		Name der Schulklasse, die gel�scht werden soll.
	 */
	public void execute(String name){
		schoolClass = DataSchulklasse.getSchulklasseByName(name);
		DataSchulklasse.deleteSchulklasseByName(name);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einf�gen-Anfrage an Datenbank weiter mit der Schulklasse, die gel�scht wurde.
	 */
	@Override
	public void undo() {
		DataSchulklasse.addSchulklasse(schoolClass);
	}
}
