package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class DeleteJahrgangsBedarfFromDB implements Command {

	private Jahrgang j;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(Jahrgang jahrgang, String kuerzel){
		j = jahrgang;
		DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(jahrgang.getJahrgang(), kuerzel);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataSchulklasse.addJahrgang(j);
	}

}
