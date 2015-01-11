package de.unibremen.swp.stundenplan.command;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

public class DeletePlanungseinheitFromDB implements Command {
	
	private Planungseinheit pl;

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	public void execute(int i){
		ArrayList<Planungseinheit> pEinheiten = DataPlanungseinheit.getAllPlanungseinheit();
		for(Planungseinheit p : pEinheiten){
			if(p.getId() == i) pl = p;
		}
		DataPlanungseinheit.deletePlanungseinheitById(i);
		CommandHistory.addCommand(this);
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		DataPlanungseinheit.addPlanungseinheit(pl);
	}

}
