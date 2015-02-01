package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Loeschen einer Schulklasse aus der Datenbank.
 * @author Roman
 *
 */
public class DeleteSchulklasseFromDB implements Command {
	
	/**
	 * Schulklasse, die geloescht werden soll.
	 */
	private Schoolclass schoolClass;

	public DeleteSchulklasseFromDB(){
		
	}

	/**
	 * Leitet Loeschanfrage an die Datenbank weiter, mit dem Uebergebenem
	 * Kuerzel. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param name
	 * 		Name der Schulklasse, die geloescht werden soll.
	 */
	public void execute(String name){
		schoolClass = DataSchulklasse.getSchulklasseByName(name);
		DataSchulklasse.deleteSchulklasseByName(name);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfuegen-Anfrage an Datenbank weiter mit der Schulklasse, die geloescht wurde.
	 */
	@Override
	public void undo() {
		DataSchulklasse.addSchulklasse(schoolClass);
	}
}
