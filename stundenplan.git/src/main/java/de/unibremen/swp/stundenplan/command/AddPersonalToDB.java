package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;

/**
 * Command-Klasse zum Einf�gen einer Person in die Datenbank.
 * @author Roman
 *
 */
public class AddPersonalToDB implements Command {
	/**
	 * Person, die eingef�gt wird.
	 */
	private Personal personal;

    public AddPersonalToDB(){
    }
    
    /**
     * Execute-Methode dieser Klasse. F�gt dieses Command- Objekt zur CommandHistory hinzu
     * Speichert eingef�gte Person als Attribut. Leitet Einf�gen an Datenbank weiter.
     * @param p
     * 		Personal das hinzugef�gt werden soll.
     */
	public void execute(Personal p) {
		DataPersonal.addPersonal(p);
		CommandHistory.addCommand(this);
		personal = p;
	}

	
	/**
	 * Undo-Methoder dieser Klasse. Leitet L�schanfrage an Datenbank weiter.
	 */
	@Override
	public void undo(){	
		DataPersonal.deletePersonalByKuerzel(personal.getKuerzel());
	}
}
