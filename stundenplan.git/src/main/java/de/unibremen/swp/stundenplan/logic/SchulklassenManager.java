package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddSchulklasseToDB;
import de.unibremen.swp.stundenplan.command.DeleteSchulklasseFromDB;
import de.unibremen.swp.stundenplan.command.EditPersonal;
import de.unibremen.swp.stundenplan.command.EditSchulklasse;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class SchulklassenManager {

	private SchulklassenManager() {
		
		
	}
	
	/**
	 * Übergibt Personal an DB, dort wird Personal hinzugefügt.
	 * @param personal
	 * 		Person die hinzugefügt werden soll.
	 */
	public static void addSchoolclassToDb(final Schoolclass schulklasse){
		System.out.println("adding Schoolclass...");
		AddSchulklasseToDB addClass = new AddSchulklasseToDB();
		addClass.execute(schulklasse);
		System.out.println("added Schoolclass: "+schulklasse.getName());
	}
	
	/**
	 * Bearbeitet eine Schulklasse aus der DB. Bearbeiten findet im wörtlichen Sinne nicht statt,
	 * das ausgewählte Objekt wird mit einem neuen überschrieben.
	 * @param name 
	 * 			Der Name der Klasse, die bearbeitet werden soll.
	 * @param s
	 * 			Die Llasse, mit der die alte Klasse überschrieben wird.
	 */
	public static void editSchoolclass(final String name, final Schoolclass s){
			System.out.println("Editing: "+name);
			EditSchulklasse editS = new EditSchulklasse();
			editS.execute(name, s);
			System.out.println(s.getName()+ "was edited.");
		}
	
	
	/**
     * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Personal gesucht wird.
     * 
     * @param Acronym der gesuchten Person
     * @return gefundene Person mit Acronym
     */
    public static Schoolclass getSchoolclassByName(final String name) {
    	System.out.println("Searching for Schoolclasses with Name: "+name+"...");
    	Schoolclass  sc = DataSchulklasse.getSchulklasseByName(name);
    	if(sc!=null){
    		System.out.println("Found: "+sc);
    	}else{
    		System.out.println("not found");
    	}
        return sc;        
    }
    
    /**
     * löscht Person mit angegebenem Kürzel aus der DB. Leutet Kürzel an DB weiter.
     * @param kuerz
     * 		Kürzel, das gesucht werden soll
     */
    public static void deleteSchulklasseFromDB(final String name)	{
    	if(getSchoolclassByName(name)!= null){
    		System.out.println("Deleting...");
    		DeleteSchulklasseFromDB deleteClass = new DeleteSchulklasseFromDB();
    		deleteClass.execute(name);
    	}else{
    		System.out.println("Name "+name+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurück. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Schoolclass> getAllSchulklassenFromDB(){
    	ArrayList<Schoolclass> schulklasse = DataSchulklasse.getAllSchulklasse();
    	
    	if(schulklasse.size() == 0){
    		System.out.println("No Schoolclass in DB");
    	}else{
    	System.out.println("Schoolclass in DB: ");
    		for(int i=0; i<schulklasse.size(); i++){
    			System.out.println(schulklasse.get(i));
    		}
    	}
    	
		return schulklasse;
    }
}
