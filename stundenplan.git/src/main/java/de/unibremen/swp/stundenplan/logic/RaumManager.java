package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddRaumToDB;
import de.unibremen.swp.stundenplan.command.AddRaumfunktionToDB;
import de.unibremen.swp.stundenplan.command.DeletePersonalFromDB;
import de.unibremen.swp.stundenplan.command.DeleteRaumFromDB;
import de.unibremen.swp.stundenplan.command.DeleteRaumfunktionFromDB;
import de.unibremen.swp.stundenplan.command.EditRaum;
import de.unibremen.swp.stundenplan.command.EditRaumfunktion;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class RaumManager {

	private RaumManager() {

	}

	/**
	 * Übergibt Personal an DB, dort wird Personal hinzugefügt.
	 * 
	 * @param personal
	 *            Person die hinzugefügt werden soll.
	 */
	public static void addRaumToDB(final Room room) {
		System.out.println("adding Room...");
		AddRaumToDB addRaum = new AddRaumToDB();
		addRaum.execute(room);
		System.out.println("added Room: " + room);
	}

	/**
	 * Bearbeitet einen Raum aus der DB. Bearbeiten findet im wörtlichen Sinne nicht statt,
	 * das ausgewählte Objekt wird mit einem neuen überschrieben.
	 * @param s 
	 * 			Der Name des Raums, der bearbeitet werden soll.
	 * @param room
	 * 			Der Raum, mit dem der alte Raum überschrieben wird.
	 */
	public static void editRaum(final String s, Room room) {
		System.out.println("editing Room [" + s + "]...");
		EditRaum editR = new EditRaum();
		editR.execute(s, room);
		System.out.println("Room [" + s + "] edited.");
	}

	/**
	 * Sucht nach Personal anhand des Acronyms. Gibt das Acronym an die DB
	 * weiter, wo Personal gesucht wird.
	 * 
	 * @param Acronym
	 *            der gesuchten Person
	 * @return gefundene Person mit Acronym
	 */
	public static Room getRoomByName(final String name) {
		System.out.println("Searching for Rooms with Name: " + name + "...");
		Room room = DataRaum.getRaumByName(name);
		if (room != null) {
			System.out.println("Found: " + room);
		} else {
			System.out.println("not found");
		}
		return room;
	}

	/**
	 * löscht Person mit angegebenem Kürzel aus der DB. Leutet Kürzel an DB
	 * weiter.
	 * 
	 * @param kuerz
	 *            Kürzel, das gesucht werden soll
	 */
	public static void deleteRoomFromDB(final String name) {
		if (getRoomByName(name) != null) {
			System.out.println("Deleting...");
			DeleteRaumFromDB deleteR = new DeleteRaumFromDB();
			deleteR.execute(name);
		} else {
			System.out.println("Name " + name + " not found.");
		}
	}

	/**
	 * Gibt Liste mit allem Personal in der DB zurück. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Room> getAllRoomsFromDB() {
		ArrayList<Room> rooms = DataRaum.getAllRaum();

		if (rooms.size() == 0) {
			System.out.println("No Rooms in DB");
		} else {
			System.out.println("Rooms in DB: ");
			for (int i = 0; i < rooms.size(); i++) {
				System.out.println(rooms.get(i));
			}
		}

		return rooms;
	}
	
	/**
	 * Übergibt Raumfunktion an DB, dort wird Rf hinzugefügt.
	 * 
	 * @param rf
	 *            rf die hinzugefügt werden soll.
	 */
	public static void addRaumfunktionToDB(final Raumfunktion rf) {
		System.out.println("adding Raumfunktion...");
		AddRaumfunktionToDB addRf = new AddRaumfunktionToDB();
		addRf.execute(rf);
		System.out.println("added Raumfunktion: " + rf);
	}

	/**
	 * Bearbeitet eine Raumfunktion aus der DB. Bearbeiten findet im wörtlichen Sinne nicht statt,
	 * das ausgewählte Objekt wird mit einem neuen überschrieben.
	 * @param s 
	 * 			Der Name der Rf, der bearbeitet werden soll.
	 * @param rf
	 * 			Rf, mit dem die alte Rf überschrieben wird.
	 */
	public static void editRaumfunktion(final String s, Raumfunktion rf) {
		System.out.println("editing Raumfunktion [" + s + "]...");
		EditRaumfunktion editRf = new EditRaumfunktion();
		editRf.execute(s, rf);
		System.out.println("Raumfunktion [" + s + "] edited.");
	}

	/**
	 * Sucht nach rf anhand des Acronyms. Gibt das Acronym an die DB
	 * weiter, wo rf gesucht wird.
	 * 
	 * @param name
	 *            der gesuchten rf
	 * @return gefundene rf mit name
	 */
	public static Raumfunktion getRaumfunktionByName(final String name) {
		System.out.println("Searching for Raumfunktionen with Name: " + name + "...");
		Raumfunktion rf = DataRaum.getRaumfunktionByName(name);
		if (rf != null) {
			System.out.println("Found: " + rf);
		} else {
			System.out.println("not found");
		}
		return rf;
	}

	/**
	 * löscht rf mit angegebenem Kürzel aus der DB. Leitet Kürzel an DB
	 * weiter.
	 * 
	 * @param name
	 *            Kürzel, das gesucht werden soll
	 */
	public static void deleteRaumfunktionFromDB(final String name) {
		if (getRaumfunktionByName(name) != null) {
			System.out.println("Deleting..."+ name);
			DeleteRaumfunktionFromDB deleteRf = new DeleteRaumfunktionFromDB();
			deleteRf.execute(name);
		} else {
			System.out.println("Raumfunktion " + name + " not found.");
		}
	}

	/**
	 * Gibt Liste mit allem rf in der DB zurück. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Raumfunktion> getAllRaumfunktionFromDB() {
		ArrayList<Raumfunktion> rfs = DataRaum.getAllRaumfunktion();

		if (rfs.size() == 0) {
			System.out.println("No Raumfunktionen in DB");
		} else {
			System.out.println("Raumfunktionen in DB: ");
			for (int i = 0; i < rfs.size(); i++) {
				System.out.println(rfs.get(i));
			}
		}

		return rfs;
	}
}
