package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class AddRaumfunktionToDB implements Command {
	
	private Raumfunktion r;
	
	public AddRaumfunktionToDB(){
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(Raumfunktion rf){
		r = rf;
		DataRaum.addRaumfunktion(rf);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataRaum.deleteRaumfunktionByName(r.getName());
	}

}
