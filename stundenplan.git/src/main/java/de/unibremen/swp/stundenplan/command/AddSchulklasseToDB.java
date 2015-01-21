package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Einf�gen einer Schulklasse in die Datenbank.
 * @author Roman
 *
 */
public class AddSchulklasseToDB implements Command {

	/**
	 * SK, die eingef�gt wird.
	 */
	private Schoolclass schoolclass;
	
	public AddSchulklasseToDB(){
	}
	
	/**
     * Execute-Methode dieser Klasse. F�gt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingef�gte SK als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param s
     * 		SK die hinzugef�gt werden soll.
     */
	public void execute(Schoolclass s){
		DataSchulklasse.addSchulklasse(s);
		CommandHistory.addCommand(this);
		schoolclass = s;
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataSchulklasse.deleteSchulklasseByName(schoolclass.getName());
	}

}
