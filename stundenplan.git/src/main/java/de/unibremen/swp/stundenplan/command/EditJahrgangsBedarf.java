package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class EditJahrgangsBedarf implements Command, EditCommand {

	private Jahrgang urspruenglich;
	private Jahrgang bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public EditJahrgangsBedarf(){
		
	}
	
	public void execute(Jahrgang neu) {
		urspruenglich = DataSchulklasse.getJahrgangByJahrgang(neu.getJahrgang());
		bearbeitet = neu;
		DataSchulklasse.addJahrgang(bearbeitet);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataSchulklasse.addJahrgang(urspruenglich);
	}

}
