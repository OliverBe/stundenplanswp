package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;
import de.unibremen.swp.stundenplan.command.AddPersonalToDB;
import de.unibremen.swp.stundenplan.command.DeletePersonalFromDB;
import de.unibremen.swp.stundenplan.command.EditPersonal;
import de.unibremen.swp.stundenplan.data.*;
import de.unibremen.swp.stundenplan.db.DataPersonal;

/**
 * Verwaltet das Personal
 * 
 * @author Rom
 * @version 0.8
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
	 * Uebergibt Personal an DB, dort wird Personal hinzugefuegt.
	 * @param personal
	 * 		Person die hinzugefuegt werden soll.
	 */
	public static void addPersonalToDb(final Personal personal){
		AddPersonalToDB addPerso = new AddPersonalToDB();
		addPerso.execute(personal);
	}
	
	/**
	 * Bearbeitet eine Person aus der DB.Bearbeiten findet im woertlichen Sinne nicht statt,
	 * das ausgewaehlte Objekt wird mit einem neuen ueberschrieben.
	 * @param zuBearbeitendesKuerz 
	 * 			Das Kuerzel der Person, die bearbeitet werden soll.
	 * @param neuesPersonal
	 * 			Die Person, mit der die alte Person ueberschrieben wird.
	 */
	public static void editPersonal(final String zuBearbeitendesKuerz, final Personal neuesPersonal){
		EditPersonal editP = new EditPersonal();
		editP.execute(zuBearbeitendesKuerz, neuesPersonal);
	}
	
	/**
     * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Personal gesucht wird.
     * 
     * @param Acronym der gesuchten Person
     * @return gefundene Person mit Acronym
     */
    public static Personal getPersonalByKuerzel(final String kuerz) {
    	Personal p = DataPersonal.getPersonalByKuerzel(kuerz);
        return p;        
    }
    
    /**
     * loescht Person mit angegebenem Kuerzel aus der DB. Leutet Kuerzel an DB weiter.
     * @param kuerz
     * 		Kuerzel, das gesucht werden soll
     */
    public static void deletePersonalFromDB(final String kuerz)	{
    	if(getPersonalByKuerzel(kuerz)!= null){
    		DeletePersonalFromDB deletePerso = new DeletePersonalFromDB();
    		deletePerso.execute(kuerz);
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurueck. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Personal> getAllPersonalFromDB(){
    	ArrayList<Personal> personal = DataPersonal.getAllPersonal();
		return personal;
    }
    
    /**
     * Gibt Liste mit allen Kuerzeln des Personals zurueck.
     */
    public static ArrayList<String> getAllKuerzel(){
    	ArrayList<String> acros = DataPersonal.getAllAcronymsFromPersonal();
		return acros;
    }
   
}
