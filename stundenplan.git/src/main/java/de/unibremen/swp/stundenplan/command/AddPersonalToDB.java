package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class AddPersonalToDB implements Command {
	
	private String kuerz;

    public AddPersonalToDB(){
    }
    
	public void execute(Personal p) {
		DataPersonal.addPersonal(p);
		CommandHistory.addCommand(this);
		kuerz = p.getKuerzel();
	}

	@Override
	public void execute(){
		CommandHistory.addCommand(this);
	}
	
	@Override
	public void undo(){	
		PersonalManager.deletePersonalFromDB(kuerz);
	}
}
