package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unibremen.swp.stundenplan.Stundenplan;
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
		// hier muss die Liste geholt werden              
		order(pes);
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
		
		// hier muss die Liste geholt werden              
		order(pes);
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
		
		// hier muss die Liste geholt werden              
		order(pes);
		return pes;
	}

	public static void order(List<Planungseinheit> pPE){
	    	    Collections.sort(pPE, new Comparator<Planungseinheit>() {
	    	        @Override public int compare(Planungseinheit p1, Planungseinheit p2) {
	    	            if(p1.getStartHour()!=p2.getStartHour()){
	    	        	return p1.getStartHour() - p2.getStartHour(); // Ascending
	    	            }else{
	    	            	return p1.getStartminute() - p2.getStartminute();
	    	            }
	    	        }
	    	    });
		}
	
	/**
	 * Testet order()
	 */
	public static void ordertest(){
		Planungseinheit pe1 = new Planungseinheit();
		pe1.setStarthour(8);
		pe1.setStartminute(0);
		Planungseinheit pe2 = new Planungseinheit();
		pe2.setStarthour(8);
		pe2.setStartminute(30);
		Planungseinheit pe3 = new Planungseinheit();
		pe3.setStarthour(9);
		pe3.setStartminute(30);
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		pes.add(pe2);
		pes.add(pe3);
		pes.add(pe1);
		for(Planungseinheit p: pes){
			System.out.println(p.getStartHour());
			System.out.println(p.getStartminute());
		}
		order(pes);
		for(Planungseinheit p: pes){
			System.out.println(p.getStartHour());
			System.out.println(p.getStartminute());
		}
	}
	
}
