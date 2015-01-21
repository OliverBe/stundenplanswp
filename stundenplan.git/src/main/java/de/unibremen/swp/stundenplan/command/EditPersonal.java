package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.DataPersonal;

public class EditPersonal implements Command, EditCommand {

	private Personal urspruenglich;
	private Personal bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(String kuerz, Personal perso){
		urspruenglich = DataPersonal.getPersonalByKuerzel(kuerz);
		bearbeitet = perso;
		DataPersonal.editPersonal(kuerz, perso);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataPersonal.editPersonal(bearbeitet.getKuerzel(), urspruenglich);
	}
}
