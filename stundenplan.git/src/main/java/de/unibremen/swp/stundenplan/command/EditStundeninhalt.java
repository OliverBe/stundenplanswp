package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class EditStundeninhalt implements Command {

	private Stundeninhalt urspruenglich;
	private Stundeninhalt bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(String pKuerzel, Stundeninhalt inhalt){
		urspruenglich = DataStundeninhalt.getStundeninhaltByKuerzel(pKuerzel);
		bearbeitet = inhalt;
		DataStundeninhalt.editStundeninhalt(pKuerzel, inhalt);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataStundeninhalt.editStundeninhalt(bearbeitet.getKuerzel(), urspruenglich);
	}

}
