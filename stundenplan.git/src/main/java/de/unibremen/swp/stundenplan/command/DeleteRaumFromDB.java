package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.RaumManager;

public class DeleteRaumFromDB implements Command {

	private Room r;
	
	public DeleteRaumFromDB(){
		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(String rName){
		r = DataRaum.getRaumByName(rName);
		DataRaum.deleteRaumByName(rName);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataRaum.addRaum(r);
	}

}
