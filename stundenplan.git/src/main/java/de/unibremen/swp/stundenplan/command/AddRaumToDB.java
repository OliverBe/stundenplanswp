package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class AddRaumToDB implements Command {
	
	private Room r;
	
	public AddRaumToDB(){
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(Room room){
		r = room;
		DataRaum.addRaum(room);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataRaum.deleteRaumByName(r.getName());
	}

}
