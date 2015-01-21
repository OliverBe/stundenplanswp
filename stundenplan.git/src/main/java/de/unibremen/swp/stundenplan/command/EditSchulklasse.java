package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class EditSchulklasse implements Command, EditCommand {

	private Schoolclass urspruenglich;
	private Schoolclass bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(String name, Schoolclass schoolclass){
		urspruenglich = DataSchulklasse.getSchulklasseByName(name);
		bearbeitet = schoolclass;
		DataSchulklasse.editSchulklasse(name, schoolclass);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataSchulklasse.editSchulklasse(bearbeitet.getName(), urspruenglich);
	}

}
