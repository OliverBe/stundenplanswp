package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
/**
 * Command-Klasse zum Loeschen einer Person aus der Datenbank.
 * @author Roman
 *
 */
public class DeletePersonalFromDB implements Command {
	/**
	 * Personal, das geloescht werden soll.
	 */
	private Personal personal;
	
	/**
	 * Leitet Loeschanfrage an die Datenbank weiter, mit dem Uebergebenem
	 * Kuerzel. Fuegt dieses Objekt der CommandHistory hinzu.
	 * @param kuerz
	 * 		Kuerzel der Person, die geloescht werden soll.
	 */
	public void execute(String kuerz){
		personal = PersonalManager.getPersonalByKuerzel(kuerz);
		DataPersonal.deletePersonalByKuerzel(kuerz);
		CommandHistory.addCommand(this);
	}

	/**
	 * Leitet Einfuegen-Anfrage an Datenbank weiter mit der Person, die geloescht wurde.
	 */
	@Override
	public void undo() {
		DataPersonal.addPersonal(personal);
	}

}
