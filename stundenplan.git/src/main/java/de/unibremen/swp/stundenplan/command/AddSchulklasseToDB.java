package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

/**
 * Command-Klasse zum Einfuegen einer Schulklasse in die Datenbank.
 * @author Roman
 *
 */
public class AddSchulklasseToDB implements Command {

	/**
	 * SK, die eingefuegt wird.
	 */
	private Schoolclass schoolclass;
	
	public AddSchulklasseToDB(){
	}
	
	/**
     * Execute-Methode dieser Klasse. Fuegt dieses Comand- Objekt zur CommandHistory hinzu
     * Speichert eingefuegte SK als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param s
     * 		SK die hinzugefuegt werden soll.
     */
	public void execute(Schoolclass s){
		DataSchulklasse.addSchulklasse(s);
		CommandHistory.addCommand(this);
		schoolclass = s;
	}

	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo() {
		DataSchulklasse.deleteSchulklasseByName(schoolclass.getName());
	}

}
