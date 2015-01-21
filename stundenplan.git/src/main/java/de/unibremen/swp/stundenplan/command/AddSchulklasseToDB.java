package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Einfügen einer Schulklasse in die Datenbank.
 * @author Roman
 *
 */
public class AddSchulklasseToDB implements Command {

	/**
	 * SK, die eingefügt wird.
	 */
	private Schoolclass schoolclass;
	
	public AddSchulklasseToDB(){
	}
	
	/**
     * Execute-Methode dieser Klasse. Fügt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefügte SK als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param s
     * 		SK die hinzugefügt werden soll.
     */
	public void execute(Schoolclass s){
		DataSchulklasse.addSchulklasse(s);
		CommandHistory.addCommand(this);
		schoolclass = s;
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataSchulklasse.deleteSchulklasseByName(schoolclass.getName());
	}

}
