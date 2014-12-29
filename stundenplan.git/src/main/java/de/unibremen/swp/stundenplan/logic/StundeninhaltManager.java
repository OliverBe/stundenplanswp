//package de.unibremen.swp.stundenplan.logic;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import de.unibremen.swp.stundenplan.data.*;
//import de.unibremen.swp.stundenplan.db.Data;
//import de.unibremen.swp.stundenplan.exceptions.DatasetException;
//
///**
// * Verwaltet das Stundeninhalt
// * 
// * @author Oli
// * @version 0.1
// * 
// */
//public class StundeninhaltManager {
//
//	private StundeninhaltManager(){
//	
//	}
//	
//	/**
//	 * Übergibt Stundeninhalt an DB, dort wird Stundeninhalt hinzugefügt.
//	 * @param stundeninhalt
//	 * 		Stundeninhalt der hinzugefügt werden soll.
//	 */
//	public static void addStundeninhaltToDb(final Stundeninhalt stundeninhalt){
//		System.out.println("adding Stundeninhalt...");
//		Data.addStundeninhalt(stundeninhalt);
//		System.out.println("added Stundeninhalt: "+stundeninhalt);
//	}
//	
//	/**
//     * Sucht nach Stundeninhalt anhand des Acronyms. Gibt das Acronym an die DB weiter,
//     * wo Stundeninhalt gesucht wird.
//     * 
//     * @param Acronym der gesuchten Person
//     * @return gefundene Person mit Acronym
//     */
//    public static Stundeninhalt getStundeninhaltByKuerzel(final String kuerz) {
//    	System.out.println("Searching for Stundeninhalt with Kuerzel: "+kuerz+"...");
//    	Stundeninhalt s = Data.getStundeninhaltByKuerzel(kuerz);
//    	if(s!=null){
//    		System.out.println("Found: "+s);
//    	}else{
//    		System.out.println("not found");
//    	}
//        return s;        
//    }
//    
//    /**
//     * löscht Person mit angegebenem Kürzel aus der DB. Leutet Kürzel an DB weiter.
//     * @param kuerz
//     * 		Kürzel, das gesucht werden soll
//     */
//    public static void deleteStundeninhaltFromDB(final String kuerz)	{
//    	if(getStundeninhaltByKuerzel(kuerz)!= null){
//    		System.out.println("Deleting...");
//    		Data.deleteStundeninhaltByKuerzel(kuerz);
//    	}else{
//    		System.out.println("Kuerzel "+kuerz+" not found.");
//    	}
//    }
//	
//    /**
//     * Gibt Liste mit allem Stundeninhalt in der DB zurück. Leitet Anfrage an DB weiter.
//     */
//    public static ArrayList<Stundeninhalt> getAllStundeninhalteFromDB(){
//    	ArrayList<Stundeninhalt> stundeninhalt = Data.getAllStundeninhalte();
//    	
//    	if(stundeninhalt.size() == 0){
//    		System.out.println("No Stundeninhalt in DB");
//    	}else{
//    	System.out.println("Stundeninhalt in DB: ");
//    		for(int i=0; i<stundeninhalt.size(); i++){
//    			System.out.println(stundeninhalt.get(i));
//    		}
//    	}
//    	
//		return stundeninhalt;
//    }
//}
