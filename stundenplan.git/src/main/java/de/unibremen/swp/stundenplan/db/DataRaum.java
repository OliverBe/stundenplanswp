package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;
import de.unibremen.swp.stundenplan.exceptions.DeleteException;
import de.unibremen.swp.stundenplan.gui.RaumbelegungsplanPanel;

/**
 * Klasse stellt die Verbindung von Room und Raumfunktion zur Datenbank dar.
 * 
 * @author Kim-Long
 *
 */
public class DataRaum {
	/**
	 * Speichert das Statement, das in der Data Klasse erstellt wurde.
	 */
	private static Statement stmt = Data.stmt;
	/**
	 * Speichert den SQL-Befehl.
	 */
	private static String sql;
	
	/**
     * Privater Konstruktor, sodass kein Object dieser Klasse erstellt werden kann.
     */
	private DataRaum() {}
	
	/**
	 * Fuegt den uebergebenen Raum zur Datenbank hinzu.
	 * 
	 * @param raum
	 * 		der Raum, welcher in die Datenbank gespeichert werden soll
	 */
	public static void addRaum(Room raum) {
		try {
			for(Room rm : getAllRaum()) {
				if(rm.getName().equals(raum.getName())){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "INSERT INTO Raum "
					+ "VALUES ('" + raum.getName() + "',"
					+ raum.getGebaeude() + ");";
			stmt.executeUpdate(sql);
			for(String kuerzel : raum.getMoeglicheFunktionen()) {
				sql = "INSERT INTO raum_Raumfunktion "
					+ "VALUES ('" + raum.getName() + "', '" + kuerzel + "');";
				stmt.executeUpdate(sql);
			}
			RaumbelegungsplanPanel.updateLists();
			Data.setSaved(false);
		} catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt den Raum mit dem gegebenen Namen zurueck.
	 * 
	 * @param pName
	 * 		der Name des Raums, nach dem gesucht werden soll
	 * @return	der Raum mit dem gegebenen Namen
	 */
	public static Room getRaumByName(String pName){
		try	{
			sql = "SELECT * FROM Raum WHERE name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int gebaeude = rs.getInt("gebaeudennr");
			Room raum = new Room(name, gebaeude, new ArrayList<String>());
			sql = "SELECT * FROM raum_Raumfunktion WHERE raum_name = '" + name + "';";
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String rf_name = rs.getString("raumfunktion_name");
				raum.getMoeglicheFunktionen().add(rf_name);
			}
			return raum;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Methode gibt eine Liste aller Raeume zurueck, die in der Datenbank gespeichert sind.
	 * 
	 * @return	eine ArrayList mit allen Raeumen
	 */
	public static ArrayList<Room> getAllRaum() {
		ArrayList<Room> allRaum = new ArrayList<Room>();
		try {
			sql = "SELECT * FROM Raum";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				int gebaeude = rs.getInt("gebaeudennr");
				allRaum.add(new Room(name, gebaeude, new ArrayList<String>()));
			}
			for(int i=0; i<allRaum.size(); i++) {
				sql = "SELECT * FROM raum_Raumfunktion WHERE raum_name = '" + allRaum.get(i).getName() + "';";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String rf_name = rs.getString("raumfunktion_name");
					allRaum.get(i).getMoeglicheFunktionen().add(rf_name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allRaum;
	}
	
	/**
	 * Gibt eine Liste von Namen aller Raeume zurueck.
	 * 
	 * @return	eine ArrayList der Namen aller Raeume
	 */
	public static ArrayList<String> getAllNameFromRaum(){
		try{ 
			sql = "SELECT name FROM Raum";
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<String> names = new ArrayList<>();
			while(rs.next()){
				names.add(rs.getString("name"));
			}
			
			return names;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loescht den Raum mit dem gegebenen Namen.
	 * 
	 * @param pName
	 * 		der Name des zu loeschenden Raumes
	 */
	public static void deleteRaumByName(String pName) {
		try {
			sql = "SELECT * FROM planungseinheit_Raum WHERE raum_name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean yes = DeleteException.delete("Der Raum ist in einer Planungseinheit eingetragen.\nSoll der Raum trotzdem gelöscht werden?");
				if(yes) {
					ArrayList<Integer> pIds = new ArrayList<Integer>();
					do {
						int pId = rs.getInt("planungseinheit_id");
						pIds.add(pId);
					}while (rs.next());
					deleteRSQL(pName);
					for(int pId : pIds) {
						DataPlanungseinheit.deleteIfEmpty(pId);
					}
				}
			}else deleteRSQL(pName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Hilfsmethode fuer das Loeschen eines Raumes.
	 * 
	 * @param pName
	 * 		der Name des Raumes, der geloescht werden soll
	 * @throws SQLException
	 */
	private static void deleteRSQL(String pName) throws SQLException {
		sql = "DELETE FROM Raum WHERE name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM raum_Raumfunktion WHERE raum_name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM planungseinheit_Raum WHERE raum_name = '" + pName + "';";
		stmt.executeUpdate(sql);
	}
	
	/**
	 * Methode bearbeitet den Raum mit dem gegebenen Namen
	 * 
	 * @param pName
	 * 		der Name des Raumes
	 * @param newRaum
	 * 		der bearbeitete Raum
	 */
	public static void editRaum(String pName, Room newRaum) {
		try {
			for(Room rm : getAllRaum()) {
				if(rm.getName().equals(newRaum.getName()) && !rm.getName().equals(pName)){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "DELETE FROM Raum WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM raum_Raumfunktion WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Raum SET raum_name = '" + newRaum.getName() + "' WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			addRaum(newRaum);
		} catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fuegt die gegebene Raumfunktion der Datenbank hinzu.
	 * 
	 * @param rf
	 * 		die Raumfunktion, die in die Datenbank gespeichert werden soll
	 */
	public static void addRaumfunktion(Raumfunktion rf) {
		try {
			for(Raumfunktion rmf : getAllRaumfunktion()) {
				if(rmf.getName().equals(rf.getName())){ 
					throw new BereitsVorhandenException();
				}
			}
			for(int i=0;i<rf.getStundeninhalte().size();i++) {
				sql = "INSERT INTO Raumfunktion "
						+ "VALUES ('" + rf.getName() + "', '" + rf.getStundeninhalte().get(i) + "');";
				stmt.executeUpdate(sql);
			}
			Data.setSaved(false);
		}catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode gibt die Raumfunktion zurueck, die den gegebenen Namen besitzt.
	 * 
	 * @param pName
	 * 		der Name der Raumfunktion
	 * @return	die Raumfunktion mit dem gegebenen Namen, falls nicht vorhanden null
	 */
	public static Raumfunktion getRaumfunktionByName(String pName) {
		try	{
			ArrayList<String> stundeninhalte = new ArrayList<String>();
			String name = "";
			sql = "SELECT * FROM Raumfunktion WHERE name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				name = rs.getString("name");
				String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
				stundeninhalte.add(stundeninhalt_kuerzel);
			}
			return new Raumfunktion(name, stundeninhalte);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gibt eine Liste aller Raumfunktionen in der Datenbank zurueck.
	 * 
	 * @return	eine ArrayList von Raumfunktionen
	 */
	public static ArrayList<Raumfunktion> getAllRaumfunktion() {
		try{
			ArrayList<Raumfunktion> rfs = new ArrayList<Raumfunktion>();
			sql = "SELECT DISTINCT name FROM Raumfunktion";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString("name");
				rfs.add(new Raumfunktion(name, new ArrayList<String>()));
			}
			for(int i=0;i<rfs.size();i++) {
				sql = "SELECT * FROM Raumfunktion WHERE name = '" + rfs.get(i).getName() + "';";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					rfs.get(i).getStundeninhalte().add(stundeninhalt_kuerzel);
				}
			}
			return rfs;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loescht die Raumfunktion mit dem gegebenen Namen.
	 * 
	 * @param pName
	 * 		der Name der Raumfunktion, die geloescht werden soll
	 */
	public static void deleteRaumfunktionByName(String pName) {
		try {
			sql = "SELECT * FROM raum_Raumfunktion WHERE raumfunktion_name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean yes = DeleteException.delete("Die Raumfunktion ist mit einem Raum verbunden.\nSoll die Raumfunktion trotzdem gelöscht werden?");
				if(yes) deleteRfSQL(pName);
			}else deleteRfSQL(pName);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Hilfsmethode fuer das Loeschen einer Raumfunktion.
	 * 
	 * @param pName
	 * 		der Name der Raumfunktion, die geloescht werden soll
	 * @throws SQLException
	 */
	private static void deleteRfSQL(String pName) throws SQLException {
		sql = "DELETE FROM Raumfunktion WHERE name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM raum_Raumfunktion WHERE raumfunktion_name = '" + pName + "';";
		stmt.executeUpdate(sql);
	}
	
	/**
	 * Methode bearbeitet 
	 * 
	 * @param pName
	 * @param rf
	 */
	public static void editRaumfunktion(String pName, Raumfunktion rf) {
		try {
			for(Raumfunktion rmf : getAllRaumfunktion()) {
				if(rmf.getName().equals(rf.getName()) && !rmf.getName().equals(pName)){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "DELETE FROM Raumfunktion WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE raum_Raumfunktion SET raumfunktion_name = '" + rf.getName() + "' WHERE raumfunktion_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			addRaumfunktion(rf);
		}catch(SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	protected static void deleteRfIfEmtpy(String pName) {
		try {
			sql = "SELECT * FROM raum_Raumfunktion WHERE raumfunktion_name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.next()) deleteRaumfunktionByName(pName);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
