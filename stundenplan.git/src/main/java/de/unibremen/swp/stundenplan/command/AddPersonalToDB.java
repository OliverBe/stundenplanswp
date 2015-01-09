package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class AddPersonalToDB implements Command {
	
	private String kuerz;

    public AddPersonalToDB(){
    }
    
	public void execute(Personal p) {
		PersonalManager.addPersonalToDb(p);
		kuerz = p.getKuerzel();
	}

	@Override
	public void execute(){
		CommandHistory.addCommand(this);
		return;
	}
	
	@Override
	public void undo(){	
		PersonalManager.deletePersonalFromDB(kuerz);
	}
}
