package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;

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
			int gebaeude = rs.getInt("gebaeude");
			return new Room(name, gebaeude);
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
				int gebaeude = rs.getInt("gebaeude");
				allRaum.add(new Room(name, gebaeude));
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
//			sql = "DELETE FROM Raumfunktion WHERE raum_name = '" + pName + "';";
//			stmt.executeUpdate(sql);
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
//			sql = "UPDATE Raumfunktion SET raum_name = '" + newRaum.getName() + "' WHERE raum_name = '" + pName + "';";
//			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Raum SET raum_name = '" + newRaum.getName() + "' WHERE raum_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			addRaum(newRaum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addRaumfunktion(Raumfunktion rf) {
		try {
			sql = "INSERT INTO Raumfunktion "
					+ "VALUES ('" + rf.getName() + "');";
			stmt.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Raumfunktion getRaumfunktionByName(String pName) {
		try	{
			sql = "SELECT * FROM Raumfunktion WHERE name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			return new Raumfunktion(name);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Raumfunktion> getAllRaumfunktion() {
		try{
			ArrayList<Raumfunktion> rfs = new ArrayList<Raumfunktion>();
			sql = "SELECT * FROM Raumfunktion";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString("name");
				rfs.add(new Raumfunktion(name));
			}
			return rfs;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deleteRaumfunktionByName(String name) {
		try {
			sql = "DELETE FROM Raumfunktion WHERE name = '" + name + "';";
			stmt.executeUpdate(sql);
		}catch(SQLException e) {
			
		}
	}
}
