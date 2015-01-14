package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class DeleteRaumfunktionFromDB implements Command {

	private Raumfunktion r;
	
	public DeleteRaumfunktionFromDB(){
		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(String rName){
		r = DataRaum.getRaumfunktionByName(rName);
		DataRaum.deleteRaumfunktionByName(rName);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataRaum.addRaumfunktion(r);
	}

}
