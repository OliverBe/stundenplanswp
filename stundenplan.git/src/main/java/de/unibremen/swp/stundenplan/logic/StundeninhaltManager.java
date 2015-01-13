package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddStundeninhaltToDB;
import de.unibremen.swp.stundenplan.command.DeleteStundeninhaltFromDB;
import de.unibremen.swp.stundenplan.command.EditSchulklasse;
import de.unibremen.swp.stundenplan.command.EditStundeninhalt;
import de.unibremen.swp.stundenplan.data.*;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Verwaltet das Stundeninhalt
 * 
 */
public class StundeninhaltManager {

	private StundeninhaltManager(){
	
	}
	
	/**
	 * �bergibt Stundeninhalt an DB, dort wird Stundeninhalt hinzugef�gt.
	 * @param stundeninhalt
	 * 		Stundeninhalt der hinzugef�gt werden soll.
	 */
	public static void addStundeninhaltToDb(final Stundeninhalt stundeninhalt){
		System.out.println("adding Stundeninhalt...");
		AddStundeninhaltToDB addInhalt = new AddStundeninhaltToDB();
		addInhalt.execute(stundeninhalt);
		System.out.println("added Stundeninhalt: "+stundeninhalt);
	}
	
	/**
	 * Bearbeitet einen Stundeninhalt aus der DB. Bearbeiten findet im w�rtlichen Sinne nicht statt,
	 * das ausgew�hlte Objekt wird mit einem neuen �berschrieben.
	 * @param name 
	 * 			K�rzel des SI, die bearbeitet werden soll.
	 * @param s
	 * 			Der SI, mit dem der alte SI �berschrieben wird.
	 */
	public static void editStundeninhalt(final String kuerzel, final Stundeninhalt s){
			System.out.println("Editing: "+kuerzel);
			EditStundeninhalt editS = new EditStundeninhalt();
			editS.execute(kuerzel, s);
			System.out.println(s.getKuerzel()+ "was edited.");
		}
	
	/**
     * Sucht nach Stundeninhalt anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Stundeninhalt gesucht wird.
     * 
     * @param Acronym des gesuchten Stundeninhaltes
     * @return gefundener Stundeninhalt
     */
    public static Stundeninhalt getStundeninhaltByKuerzel(final String kuerz) {
    	System.out.println("Searching for Stundeninhalt with Kuerzel: "+kuerz+"...");
    	Stundeninhalt s = DataStundeninhalt.getStundeninhaltByKuerzel(kuerz);
    	if(s!=null){
    		System.out.println("Found: "+s);
    	}else{
    		System.out.println("not found");
    	}
        return s;        
    }
    
    /**
     * l�scht Person mit angegebenem K�rzel aus der DB. Leutet K�rzel an DB weiter.
     * @param kuerz
     * 		K�rzel, das gesucht werden soll
     */
    public static void deleteStundeninhaltFromDB(final String kuerz)	{
    	if(getStundeninhaltByKuerzel(kuerz)!= null){
    		System.out.println("Deleting...");
    		DeleteStundeninhaltFromDB deleteInhalt = new DeleteStundeninhaltFromDB();
    		deleteInhalt.execute(kuerz);
    	}else{
    		System.out.println("Kuerzel "+kuerz+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Stundeninhalt in der DB zur�ck. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Stundeninhalt> getAllStundeninhalteFromDB(){
    	ArrayList<Stundeninhalt> stundeninhalt = DataStundeninhalt.getAllStundeninhalte();
    	
    	if(stundeninhalt.size() == 0){
    		System.out.println("No Stundeninhalt in DB");
    	}else{
    	System.out.println("Stundeninhalt in DB: ");
    		for(int i=0; i<stundeninhalt.size(); i++){
    			System.out.println(stundeninhalt.get(i));
    		}
    	}
    	
		return stundeninhalt;
    }
}
