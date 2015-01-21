package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Löschen einer Schulklasse aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteSchulklasseFromDB implements Command {
	
	/**
	 * Schulklasse, die gelöscht werden soll.
	 */
	private Schoolclass schoolClass;

	public DeleteSchulklasseFromDB(){
		
	}

	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param name
	 * 		Name der Schulklasse, die gelöscht werden soll.
	 */
	public void execute(String name){
		schoolClass = DataSchulklasse.getSchulklasseByName(name);
		DataSchulklasse.deleteSchulklasseByName(name);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit der Schulklasse, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		DataSchulklasse.addSchulklasse(schoolClass);
	}
}
