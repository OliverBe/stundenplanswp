package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;

/**
 * Command-Klasse zum Einfuegen einer Person in die Datenbank.
 * @author Roman
 *
 */
public class AddPersonalToDB implements Command {
	/**
	 * Person, die eingefuegt wird.
	 */
	private Personal personal;
    
    /**
     * Execute-Methode dieser Klasse. Fuegt dieses Command- Objekt zur CommandHistory hinzu
     * Speichert eingefuegte Person als Attribut. Leitet Einfuegen an Datenbank weiter.
     * @param p
     * 		Personal das hinzugefuegt werden soll.
     */
	public void execute(Personal p) {
		DataPersonal.addPersonal(p);
		CommandHistory.addCommand(this);
		personal = p;
	}
	
	/**
	 * Undo-Methoder dieser Klasse. Leitet Loeschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo(){	
		DataPersonal.deletePersonalByKuerzel(personal.getKuerzel());
	}
}
