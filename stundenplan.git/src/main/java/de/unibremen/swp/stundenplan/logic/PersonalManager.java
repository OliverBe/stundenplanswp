package de.unibremen.swp.stundenplan.logic;
import de.unibremen.swp.stundenplan.data.*;
import de.unibremen.swp.stundenplan.db.Data;

/**
 * Verwaltet das Personal
 * 
 * @author Rom
 * @version 0.1
 * 
 */
public class PersonalManager {

	private PersonalManager(){
	
	}
	
	public static void addPersonalToDb(final Personal personal){
		Data.addPersonal(personal);
	}
}
