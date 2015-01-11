package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class AddPersonalToDB implements Command {
	
	private Personal personal;

    public AddPersonalToDB(){
    }
    
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
		DataPersonal.addPersonal(personal);
	}
}
