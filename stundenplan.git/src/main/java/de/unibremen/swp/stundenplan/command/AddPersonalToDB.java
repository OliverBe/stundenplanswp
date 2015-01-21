package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;

/**
 * Command-Klasse zum Einfügen einer Person in die Datenbank.
 * @author Roman
 *
 */
public class AddPersonalToDB implements Command {
	/**
	 * Person, die eingefügt wird.
	 */
	private Personal personal;

    public AddPersonalToDB(){
    }
    
    /**
     * Execute-Methode dieser Klasse. Fügt dieses Command- Objekt zur CommandHistory hinzu
     * Speichert eingefügte Person als Attribut. Leitet Einfügen an Datenbank weiter.
     * @param p
     * 		Personal das hinzugefügt werden soll.
     */
	public void execute(Personal p) {
		DataPersonal.addPersonal(p);
		CommandHistory.addCommand(this);
		personal = p;
	}

	
	/**
	 * Undo-Methoder dieser Klasse. Leitet Löschanfrage an Datenbank weiter.
	 */
	@Override
	public void undo(){	
		DataPersonal.deletePersonalByKuerzel(personal.getKuerzel());
	}
}
