package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;
import java.util.Collection;

import de.unibremen.swp.stundenplan.command.AddPersonalToDB;
import de.unibremen.swp.stundenplan.command.DeletePersonalFromDB;
import de.unibremen.swp.stundenplan.command.EditPersonal;
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
	 * �bergibt Personal an DB, dort wird Personal hinzugef�gt.
	 * @param personal
	 * 		Person die hinzugef�gt werden soll.
	 */
	public static void addPersonalToDb(final Personal personal){
		System.out.println("adding Personal...");
		AddPersonalToDB addPerso = new AddPersonalToDB();
		addPerso.execute(personal);
		System.out.println("added Personal: "+personal);
	}
	
	/**
	 * Bearbeitet eine Person aus der DB.Bearbeiten findet im w�rtlichen Sinne nicht statt,
	 * das ausgew�hlte Objekt wird mit einem neuen �berschrieben.
	 * @param zuBearbeitendesKuerz 
	 * 			Das Kuerzel der Person, die bearbeitet werden soll.
	 * @param neuesPersonal
	 * 			Die Person, mit der die alte Person �berschrieben wird.
	 */
	public static void editPersonal(final String zuBearbeitendesKuerz, final Personal neuesPersonal){
		System.out.println("Editing: "+zuBearbeitendesKuerz);
		EditPersonal editP = new EditPersonal();
		editP.execute(zuBearbeitendesKuerz, neuesPersonal);
		System.out.println(neuesPersonal.getKuerzel()+ "was edited.");
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
     * l�scht Person mit angegebenem K�rzel aus der DB. Leutet K�rzel an DB weiter.
     * @param kuerz
     * 		K�rzel, das gesucht werden soll
     */
    public static void deletePersonalFromDB(final String kuerz)	{
    	if(getPersonalByKuerzel(kuerz)!= null){
    		System.out.println("Deleting Personal from DB...");
    		DeletePersonalFromDB deletePerso = new DeletePersonalFromDB();
    		deletePerso.execute(kuerz);
    	}else{
    		System.out.println("Kuerzel "+kuerz+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zur�ck. Leitet Anfrage an DB weiter.
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
    
    /**
     * Gibt Liste mit allen K�rzeln des Personals zur�ck.
     */
    public static ArrayList<String> getAllKuerzel(){
    	ArrayList<String> acros = DataPersonal.getAllAcronymsFromPersonal();
  
    	if(acros.size() == 0){
    		System.out.println("No Personal in DB");
    	}else{
    	System.out.println("Personal in DB: ");
    		for(int i=0; i<acros.size(); i++){
    			System.out.println(acros.get(i));
    		}
    	}
    	
		return acros;
    }
   
}
