package de.unibremen.swp.stundenplan.command;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

public class AddPlanungseinheitToDB implements Command {

	private Planungseinheit einheit;
	
	public AddPlanungseinheitToDB(){
		
	}
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
	}
	
	public void execute(Planungseinheit pl){
		einheit = pl;
		DataPlanungseinheit.addPlanungseinheit(pl);
		CommandHistory.addCommand(this);
	}

	@Override
	public void undo() {
		DataPlanungseinheit.deletePlanungseinheitById(einheit.getId());
	}

}
