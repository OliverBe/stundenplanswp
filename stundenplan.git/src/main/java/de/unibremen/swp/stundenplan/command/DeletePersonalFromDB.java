package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class DeletePersonalFromDB implements Command {

	private Personal personal;
	
	public DeletePersonalFromDB(){		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(String kuerz){
		personal = PersonalManager.getPersonalByKuerzel(kuerz);
		DataPersonal.deletePersonalByKuerzel(kuerz);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataPersonal.addPersonal(personal);
	}

}
