package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class AddStundeninhaltToDB implements Command {

	private Stundeninhalt inhalt;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(Stundeninhalt st){
		inhalt = st;
		DataStundeninhalt.addStundeninhalt(st);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.deleteStundeninhaltByKuerzel(inhalt.getKuerzel());
	}

}
