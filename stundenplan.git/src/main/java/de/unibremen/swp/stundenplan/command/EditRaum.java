package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class EditRaum implements Command {

	private Room urspruenglich;
	private Room bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(String name, Room raum){
		urspruenglich = DataRaum.getRaumByName(name);
		bearbeitet = raum;
		DataRaum.editRaum(name, raum);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataRaum.editRaum(bearbeitet.getName(), urspruenglich);
	}

}
