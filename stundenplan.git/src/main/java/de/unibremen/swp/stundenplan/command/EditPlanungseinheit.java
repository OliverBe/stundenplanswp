package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

public class EditPlanungseinheit implements Command {

	private Planungseinheit urspruenglich;
	private Planungseinheit bearbeitet;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public void execute(int iD, Planungseinheit pl){
		urspruenglich = DataPlanungseinheit.getPlanungseinheitById(iD);
		bearbeitet = pl;
		DataPlanungseinheit.editPlanungseinheit(iD, pl);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataPlanungseinheit.editPlanungseinheit(bearbeitet.getId(), urspruenglich);
	}

}
