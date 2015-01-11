package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class DeleteStundeninhaltFromDB implements Command{
	
	private Stundeninhalt st;

	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(String s){
		st = DataStundeninhalt.getStundeninhaltByKuerzel(s);
		DataStundeninhalt.deleteStundeninhaltByKuerzel(s);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataStundeninhalt.addStundeninhalt(st);
	}
}
