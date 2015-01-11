package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddRaumToDB;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class RaumManager {

private RaumManager() {
		
		
	}
	
	/**
	 * Übergibt Personal an DB, dort wird Personal hinzugefügt.
	 * @param personal
	 * 		Person die hinzugefügt werden soll.
	 */
	public static void addRaumToDb(final Room room){
		System.out.println("adding Room...");
		AddRaumToDB addRaum = new AddRaumToDB();
		addRaum.execute(room);
		System.out.println("added Room: "+room);
	}
	
	/**
     * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB weiter,
     * wo Personal gesucht wird.
     * 
     * @param Acronym der gesuchten Person
     * @return gefundene Person mit Acronym
     */
    public static Room getRoomByName(final String name) {
    	System.out.println("Searching for Rooms with Name: "+name+"...");
    	Room  room = DataRaum.getRaumByName(name);
    	if(room!=null){
    		System.out.println("Found: "+room);
    	}else{
    		System.out.println("not found");
    	}
        return room;        
    }
    
    /**
     * löscht Person mit angegebenem Kürzel aus der DB. Leutet Kürzel an DB weiter.
     * @param kuerz
     * 		Kürzel, das gesucht werden soll
     */
    public static void deleteRoomFromDB(final String name)	{
    	if(getRoomByName(name)!= null){
    		System.out.println("Deleting...");
    		DataRaum.deleteRaumByName(name);
    	}else{
    		System.out.println("Name "+name+" not found.");
    	}
    }
	
    /**
     * Gibt Liste mit allem Personal in der DB zurück. Leitet Anfrage an DB weiter.
     */
    public static ArrayList<Room> getAllRoomsFromDB(){
    	ArrayList<Room> rooms = DataRaum.getAllRaum();
    	
    	if(rooms.size() == 0){
    		System.out.println("No Rooms in DB");
    	}else{
    	System.out.println("Rooms in DB: ");
    		for(int i=0; i<rooms.size(); i++){
    			System.out.println(rooms.get(i));
    		}
    	}
    	
		return rooms;
    }
}
