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
	
	public static void addRaumfunktion(Raumfunktion rf) {
		try {
			sql = "INSERT INTO Raumfunktion "
					+ "VALUES ('" + rf.getName() + "');";
			stmt.executeUpdate(sql);
			System.out.println(rf.getName()+"add");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Room getRoomByKuerzel(String pKuerzel){
		try	{
			sql = "SELECT * FROM Raum WHERE kuerzel = " +pKuerzel;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			String kuerzel = rs.getString("kuerzel");
			int gebaeude = rs.getInt("gebaeude");
			// TODO Liste mit Raumfunktionen per raum.addMoeglicheFunktion hinzuf�gen
			Room raum = new Room(name, kuerzel, gebaeude);
			return raum;
		}catch (SQLException e){
		}
		return null;
	}
	
	public static ArrayList<Raumfunktion> getAllRaumfunktion() {
		try{
			ArrayList<Raumfunktion> rfs = new ArrayList<Raumfunktion>();
			sql = "SELECT * FROM Raumfunktion;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString("name");
				System.out.println(name);
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
		
	public static void dbRaumLesen() {
		try {
			sql = "SELECT * FROM Raum;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("Name: " + rs.getString("name") + ", Gebäudennr: " + rs.getInt("gebaeudennr"));
			}
		}catch(SQLException e) {}
	}
}
