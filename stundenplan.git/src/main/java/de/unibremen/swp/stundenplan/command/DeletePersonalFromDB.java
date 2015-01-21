package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
/**
 * Command-Klasse zum Löschen einer Person aus der Datenbank.
 * @author Roman
 *
 */
public class DeletePersonalFromDB implements Command {
	/**
	 * Personal, das gelöscht werden soll.
	 */
	private Personal personal;
	
	public DeletePersonalFromDB(){		
	}
	
	/**
	 * Leitet Löschanfrage an die Datenbank weiter, mit dem übergebenem
	 * Kuerzel. Fügt dieses Objekt der CommandHistory hinzu.
	 * @param kuerz
	 * 		Kuerzel der Person, die gelöscht werden soll.
	 */
	public void execute(String kuerz){
		personal = PersonalManager.getPersonalByKuerzel(kuerz);
		DataPersonal.deletePersonalByKuerzel(kuerz);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfügen-Anfrage an Datenbank weiter mit der Person, die gelöscht wurde.
	 */
	@Override
	public void undo() {
		DataPersonal.addPersonal(personal);
	}

}
