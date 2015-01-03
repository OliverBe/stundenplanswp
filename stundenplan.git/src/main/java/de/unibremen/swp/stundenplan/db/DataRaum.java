package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;

public class DataRaum {
	
	private static Statement stmt = null;
	private static String sql;
	
	private DataRaum() {}

	public static void addRaum(Room raum) {
		try {
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
			sql = "SELECT * FROM Raum WHERE name = " + pName;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int gebaeude = rs.getInt("gebaeude");
			return new Room(name, gebaeude);
		}catch (SQLException e){
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
				allRaum.add(getRaumByName(name));
			}
		} catch (SQLException e) {}
		return allRaum;
	}
	
	public static void deleteRaumByName(String pName) {
		try {
			sql = "DELETE FROM Raum WHERE name = " + pName;
			stmt.executeUpdate(sql);
		} catch (SQLException e) {}
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
			sql = "SELECT * FROM Raumfunktion WHERE name = " + pName;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			return new Raumfunktion(name);
		}catch (Exception e){
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
			
		}
		return null;
	}
	
	public static void deleteRaumfunktionByName(String name) {
		try {
			sql = "DELETE FROM Raumfunktion WHERE name = " + name;
			stmt.executeUpdate(sql);
		}catch(SQLException e) {
			
		}
	}
}
