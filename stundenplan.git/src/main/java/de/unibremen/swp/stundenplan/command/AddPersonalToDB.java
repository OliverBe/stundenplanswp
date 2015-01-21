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
     * 
     */
	public void execute(Personal p) {
		DataPersonal.addPersonal(p);
		CommandHistory.addCommand(this);
		personal = p;
	}

	@Override
	public void execute(){
	}
	
	@Override
	public void undo(){	
		DataPersonal.deletePersonalByKuerzel(personal.getKuerzel());
	}
}
