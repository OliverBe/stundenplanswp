package de.unibremen.swp.stundenplan.logic;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddStundeninhaltToDB;
import de.unibremen.swp.stundenplan.command.DeleteStundeninhaltFromDB;
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
	 * Uebergibt Stundeninhalt an DB, dort wird Stundeninhalt hinzugefuegt.
	 * @param stundeninhalt
	 * 		Stundeninhalt der hinzugefuegt werden soll.
	 */
	public static void addStundeninhaltToDb(final Stundeninhalt stundeninhalt) {
		AddStundeninhaltToDB addInhalt = new AddStundeninhaltToDB();
		addInhalt.execute(stundeninhalt);
	}
	
	/**
	 * Bearbeitet einen Stundeninhalt aus der DB. Bearbeiten findet im woertlichen Sinne nicht statt,
	 * das ausgewaehlte Objekt wird mit einem neuen ueberschrieben.
	 * @param name 
	 * 			Kuerzel des SI, die bearbeitet werden soll.
	 * @param s
	 * 			Der SI, mit dem der alte SI ueberschrieben wird.
	 */
	public static void editStundeninhalt(final String kuerzel, final Stundeninhalt s){
			EditStundeninhalt editS = new EditStundeninhalt();
			editS.execute(kuerzel, s);
		}
	
	/**
     * Sucht nach Stundeninhalt anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Stundeninhalt gesucht wird.
     * 
     * @param Acronym des gesuchten Stundeninhaltes
     * @return gefundener Stundeninhalt
     */
    public static Stundeninhalt getStundeninhaltByKuerzel(final String kuerz) {
    	Stundeninhalt s = DataStundeninhalt.getStundeninhaltByKuerzel(kuerz);
        return s;        
    }
    
    /**
     * loescht Person mit angegebenem Kuerzel aus der DB. Leutet Kuerzel an DB weiter.
     * @param kuerz
     * 		Kuerzel, das gesucht werden soll
     */
    public static void deleteStundeninhaltFromDB(final String kuerz)	{
    	if(getStundeninhaltByKuerzel(kuerz)!= null){
    		DeleteStundeninhaltFromDB deleteInhalt = new DeleteStundeninhaltFromDB();
    		deleteInhalt.execute(kuerz);
    	}
    }
	
    /**
     * Gibt Liste mit allem Stundeninhalt in der DB zurueck. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Stundeninhalt> getAllStundeninhalteFromDB(){
    	ArrayList<Stundeninhalt> stundeninhalt = DataStundeninhalt.getAllStundeninhalte();
		return stundeninhalt;
    }
}
