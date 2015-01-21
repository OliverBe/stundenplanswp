package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
/**
 * Command-Klasse zum L�schen einer Person aus der Datenbank.
 * @author Roman
 *
 */
public class DeletePersonalFromDB implements Command {
	/**
	 * Personal, das gel�scht werden soll.
	 */
	private Personal personal;
	
	public DeletePersonalFromDB(){		
	}
	
	/**
	 * Leitet L�schanfrage an die Datenbank weiter, mit dem �bergebenem
	 * Kuerzel. F�gt dieses Objekt der CommandHistory hinzu.
	 * @param kuerz
	 * 		Kuerzel der Person, die gel�scht werden soll.
	 */
	public void execute(String kuerz){
		personal = PersonalManager.getPersonalByKuerzel(kuerz);
		DataPersonal.deletePersonalByKuerzel(kuerz);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einf�gen-Anfrage an Datenbank weiter mit der Person, die gel�scht wurde.
	 */
	@Override
	public void undo() {
		DataPersonal.addPersonal(personal);
	}

}
