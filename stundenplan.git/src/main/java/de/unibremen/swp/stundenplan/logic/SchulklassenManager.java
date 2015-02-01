package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddSchulklasseToDB;
import de.unibremen.swp.stundenplan.command.DeleteSchulklasseFromDB;
import de.unibremen.swp.stundenplan.command.EditSchulklasse;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class SchulklassenManager {

	private SchulklassenManager() {
		
		
	}
	
	/**
	 * Uebergibt Personal an DB, dort wird Personal hinzugefuegt.
	 * @param personal
	 * 		Person die hinzugefuegt werden soll.
	 */
	public static void addSchoolclassToDb(final Schoolclass schulklasse){
		AddSchulklasseToDB addClass = new AddSchulklasseToDB();
		addClass.execute(schulklasse);
	}
	
	/**
	 * Bearbeitet eine Schulklasse aus der DB. Bearbeiten findet im woertlichen Sinne nicht statt,
	 * das ausgewaehlte Objekt wird mit einem neuen ueberschrieben.
	 * @param name 
	 * 			Der Name der Klasse, die bearbeitet werden soll.
	 * @param s
	 * 			Die Llasse, mit der die alte Klasse ueberschrieben wird.
	 */
	public static void editSchoolclass(final String name, final Schoolclass s){
			EditSchulklasse editS = new EditSchulklasse();
			editS.execute(name, s);
		}
	
	
	/**
     * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Personal gesucht wird.
     * 
     * @param Acronym der gesuchten Person
     * @return gefundene Person mit Acronym
     */
    public static Schoolclass getSchoolclassByName(final String name) {
    	Schoolclass  sc = DataSchulklasse.getSchulklasseByName(name);
        return sc;        
    }
    
    /**
     * loescht Person mit angegebenem Kuerzel aus der DB. Leutet Kuerzel an DB weiter.
     * @param kuerz
     * 		Kuerzel, das gesucht werden soll
     */
    public static void deleteSchulklasseFromDB(final String name)	{
    	if(getSchoolclassByName(name)!= null){
    		DeleteSchulklasseFromDB deleteClass = new DeleteSchulklasseFromDB();
    		deleteClass.execute(name);
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurueck. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Schoolclass> getAllSchulklassenFromDB(){
    	ArrayList<Schoolclass> schulklasse = DataSchulklasse.getAllSchulklasse();
		return schulklasse;
    }
}
