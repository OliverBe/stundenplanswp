package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;

public class DeleteSchulklasseFromDB implements Command {
	
	private Schoolclass schoolClass;

	public DeleteSchulklasseFromDB(){
		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(String name){
		schoolClass = DataSchulklasse.getSchulklasseByName(name);
		DataSchulklasse.deleteSchulklasseByName(name);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataSchulklasse.addSchulklasse(schoolClass);
	}
}
