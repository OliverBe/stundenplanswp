package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;

public final class PlanungseinheitManager {

	private PlanungseinheitManager(){
	}
	
	/**
	 *  Soll Planungseinheiten in einer Liste  von einem Personal an einem Tag zurückgeben.
	 *  Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * @param pWeekday der Tag der Planungeinheiten
	 * @param pPerson das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForPersonalbyWeekday(Weekday pWeekday,final Personal pPerson){
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		
		return pes;
	}
	
	/**
	 *  Soll Planungseinheiten in einer Liste  von einem Personal an einem Tag zurückgeben.
	 *  Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * @param pWeekday der Tag der Planungeinheiten
	 * @param pPerson das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForSchoolclassbyWeekday(Weekday pWeekday,final Schoolclass pSchoolclass){
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		
		return pes;
	}
	
	/**
	 *  Soll Planungseinheiten in einer Liste  von einem Raum an einem Tag zurückgeben.
	 *  Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * @param pWeekday der Tag der Planungeinheiten
	 * @param pRoom Den Raum den in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForRoombyWeekday(Weekday pWeekday,final Room pRoom){
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		
		return pes;
	}
}
