package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.gui.RaumbelegungsplanPanel;

public class DataRaum {
	
	private static Statement stmt = Data.stmt;
	private static String sql;
	
	private DataRaum() {}

	public static void addRaum(Room raum) {
		try {
			for(Room rm : getAllRaum()) {
				if(rm.getName().equals(raum.getName())){ 
					throw new SQLException("DB - ERROR Raum already in Database");
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public static void deleteRaumByName(String pName) {
		try {
			sql = "DELETE FROM Raum WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM raum_Raumfunktion WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Raum WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editRaum(String pName, Room newRaum) {
		try {
			sql = "DELETE FROM Raum WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM raum_Raumfunktion WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Raum SET raum_name = '" + newRaum.getName() + "' WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			addRaum(newRaum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addRaumfunktion(Raumfunktion rf) {
		try {
			for(int i=0;i<rf.getStundeninhalte().size();i++) {
				sql = "INSERT INTO Raumfunktion "
						+ "VALUES ('" + rf.getName() + "', '" + rf.getStundeninhalte().get(i) + "');";
				stmt.executeUpdate(sql);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public static void deleteRaumfunktionByName(String name) {
		try {
			sql = "DELETE FROM Raumfunktion WHERE name = '" + name + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM raum_Raumfunktion WHERE raumfunktion_name = '" + name + "';";
			stmt.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editRaumfunktion(String name, Raumfunktion rf) {
		try {
			sql = "DELETE FROM Raumfunktion WHERE name = '" + name + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE raum_Raumfunktion SET raumfunktion_name = '" + rf.getName() + "' WHERE raumfunktion_name = '" + name + "';";
			stmt.executeUpdate(sql);
			addRaumfunktion(rf);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
