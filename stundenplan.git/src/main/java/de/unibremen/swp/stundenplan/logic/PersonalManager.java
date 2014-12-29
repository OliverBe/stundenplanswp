package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;
import java.util.Collection;

import de.unibremen.swp.stundenplan.data.*;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPersonal;
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
     * istZeit wird in Minuten angegeben. Methode rechnet die Zeit in Stunden um.
     * @return
     * 		IstZeit in Stunden
     */
    public double berechneIstZeitInStunden(Personal p){
	
    	double ergebnis=0;
	
    	if(p.isLehrer()){
    		ergebnis = p.getIstZeit()/45;
    		return ergebnis;
    	}else{
    		ergebnis = p.getIstZeit()/60;
    		return ergebnis;
    	}
	}
    
    /**
     * Berechnet Sollzeit von Minuten in Stunden.
     */
    public double berechneSollZeitInStunden(Personal p){
    	double ergebnis=0;
    	
    	if(p.isLehrer()){
    		ergebnis = p.getSollZeit()/45;
    		return ergebnis;
    	}else{
    		ergebnis = p.getSollZeit()/60;
    		return ergebnis;
    	}
    }
	
	/**
	 * Übergibt Personal an DB, dort wird Personal hinzugefügt.
	 * @param personal
	 * 		Person die hinzugefügt werden soll.
	 */
	public static void addPersonalToDb(final Personal personal){
		System.out.println("adding Personal...");
		DataPersonal.addPersonal(personal);
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
    	Personal p = DataPersonal.getPersonalByKuerzel(kuerz);
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
    		DataPersonal.deletePersonalByKuerzel(kuerz);
    	}else{
    		System.out.println("Kuerzel "+kuerz+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurück. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Personal> getAllPersonalFromDB(){
    	ArrayList<Personal> personal = DataPersonal.getAllPersonal();
    	
    	if(personal.size() == 0){
    		System.out.println("No Personal in DB");
    	}else{
    	System.out.println("Personal in DB: ");
    		for(int i=0; i<personal.size(); i++){
    			System.out.println(personal.get(i));
    		}
    	}
    	
		return personal;
    }
}
