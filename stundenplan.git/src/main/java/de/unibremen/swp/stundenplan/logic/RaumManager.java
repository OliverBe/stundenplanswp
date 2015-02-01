package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;

import de.unibremen.swp.stundenplan.command.AddRaumToDB;
import de.unibremen.swp.stundenplan.command.AddRaumfunktionToDB;
import de.unibremen.swp.stundenplan.command.DeleteRaumFromDB;
import de.unibremen.swp.stundenplan.command.DeleteRaumfunktionFromDB;
import de.unibremen.swp.stundenplan.command.EditRaum;
import de.unibremen.swp.stundenplan.command.EditRaumfunktion;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.db.DataRaum;

public class RaumManager {

	private RaumManager() {

	}

	/**
	 * Uebergibt Personal an DB, dort wird Personal hinzugefuegt.
	 * 
	 * @param personal
	 *            Person die hinzugefuegt werden soll.
	 */
	public static void addRaumToDB(final Room room) {
		AddRaumToDB addRaum = new AddRaumToDB();
		addRaum.execute(room);
	}

	/**
	 * Bearbeitet einen Raum aus der DB. Bearbeiten findet im woertlichen Sinne nicht statt,
	 * das ausgewaehlte Objekt wird mit einem neuen ueberschrieben.
	 * @param s 
	 * 			Der Name des Raums, der bearbeitet werden soll.
	 * @param room
	 * 			Der Raum, mit dem der alte Raum ueberschrieben wird.
	 */
	public static void editRaum(final String s, Room room) {
		EditRaum editR = new EditRaum();
		editR.execute(s, room);
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
		Room room = DataRaum.getRaumByName(name);
		return room;
	}

	/**
	 * loescht Person mit angegebenem Kuerzel aus der DB. Leutet Kuerzel an DB
	 * weiter.
	 * 
	 * @param kuerz
	 *            Kurzel, das gesucht werden soll
	 */
	public static void deleteRoomFromDB(final String name) {
		if (getRoomByName(name) != null) {
			DeleteRaumFromDB deleteR = new DeleteRaumFromDB();
			deleteR.execute(name);
		}
	}

	/**
	 * Gibt Liste mit allem Personal in der DB zurueck. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Room> getAllRoomsFromDB() {
		ArrayList<Room> rooms = DataRaum.getAllRaum();
		return rooms;
	}
	
	/**
	 * Uebergibt Raumfunktion an DB, dort wird Rf hinzugefuegt.
	 * 
	 * @param rf
	 *            rf die hinzugefuegt werden soll.
	 */
	public static void addRaumfunktionToDB(final Raumfunktion rf) {
		AddRaumfunktionToDB addRf = new AddRaumfunktionToDB();
		addRf.execute(rf);
	}

	/**
	 * Bearbeitet eine Raumfunktion aus der DB. Bearbeiten findet im woertlichen Sinne nicht statt,
	 * das ausgewaehlte Objekt wird mit einem neuen ueberschrieben.
	 * @param s 
	 * 			Der Name der Rf, der bearbeitet werden soll.
	 * @param rf
	 * 			Rf, mit dem die alte Rf ueberschrieben wird.
	 */
	public static void editRaumfunktion(final String s, Raumfunktion rf) {
		EditRaumfunktion editRf = new EditRaumfunktion();
		editRf.execute(s, rf);
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
		Raumfunktion rf = DataRaum.getRaumfunktionByName(name);
		return rf;
	}

	/**
	 * loescht rf mit angegebenem Kuerzel aus der DB. Leitet Kuerzel an DB
	 * weiter.
	 * 
	 * @param name
	 *            Kuerzel, das gesucht werden soll
	 */
	public static void deleteRaumfunktionFromDB(final String name) {
		if (getRaumfunktionByName(name) != null) {
			DeleteRaumfunktionFromDB deleteRf = new DeleteRaumfunktionFromDB();
			deleteRf.execute(name);
		}
	}

	/**
	 * Gibt Liste mit allem rf in der DB zurueck. Leitet Anfrage an DB
	 * weiter.
	 */
	public static ArrayList<Raumfunktion> getAllRaumfunktionFromDB() {
		ArrayList<Raumfunktion> rfs = DataRaum.getAllRaumfunktion();
		return rfs;
	}
}
