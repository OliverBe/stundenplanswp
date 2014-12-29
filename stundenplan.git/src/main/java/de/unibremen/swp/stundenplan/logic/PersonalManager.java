package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;
import java.util.Collection;

import de.unibremen.swp.stundenplan.data.*;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;

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
	
	/**
	 * Übergibt Personal an DB, dort wird Personal hinzugefügt.
	 * @param personal
	 * 		Person die hinzugefügt werden soll.
	 */
	public static void addPersonalToDb(final Personal personal){
		System.out.println("adding Personal...");
		Data.addPersonal(personal);
		System.out.println("added Personal: "+personal);
	}
	
	/**
     * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Personal gesucht wird.
     * 
     * @param Acronym der gesuchten Person
     * @return gefundene Person mit Acronym
     */
    public static Personal getPersonalByKuerzel(final String kuerz) {
    	System.out.println("Searching for Personal with acro: "+kuerz+"...");
    	Personal p = Data.getPersonalByKuerzel(kuerz);
    	if(p!=null){
    		System.out.println("Found: "+p);
    	}else{
    		System.out.println("not found");
    	}
        return p;        
    }
    
    /**
     * löscht Person mit angegebenem Kürzel aus der DB. Leutet Kürzel an DB weiter.
     * @param kuerz
     * 		Kürzel, das gesucht werden soll
     */
    public static void deletePersonalFromDB(final String kuerz)	{
    	if(getPersonalByKuerzel(kuerz)!= null){
    		System.out.println("Deleting...");
    		Data.deletePersonalByKuerzel(kuerz);
    	}else{
    		System.out.println("Kuerzel "+kuerz+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurück. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Personal> getAllPersonalFromDB(){
    	System.out.println()
    }
}
