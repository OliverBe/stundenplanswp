package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class AddSchulklasseToDB implements Command {

	private Schoolclass schoolclass;
	
	public AddSchulklasseToDB(){
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(Schoolclass s){
		DataSchulklasse.addSchulklasse(s);
		CommandHistory.addCommand(this);
		schoolclass = s;
	}

	@Override
	public void undo() {
		DataSchulklasse.deleteSchulklasseByName(schoolclass.getName());
	}

}
