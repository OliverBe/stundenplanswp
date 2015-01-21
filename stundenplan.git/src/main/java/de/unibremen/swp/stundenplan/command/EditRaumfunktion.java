package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class EditRaumfunktion implements Command, EditCommand {

	private Raumfunktion urspruenglich;
	private Raumfunktion bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(String name, Raumfunktion rf){
		urspruenglich = DataRaum.getRaumfunktionByName(name);
		bearbeitet = rf;
		DataRaum.editRaumfunktion(name, rf);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataRaum.editRaumfunktion(bearbeitet.getName(), urspruenglich);
	}

}
