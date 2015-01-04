package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Schoolclass;

public class DataSchulklasse {

	private static Statement stmt = Data.stmt;
	private static String sql;

	private DataSchulklasse() {}

	public static void addSchulklasse(Schoolclass schulklasse) {
		try {
			for(Schoolclass sc : getAllSchulklasse()) {
				if(sc.getName().equals(schulklasse.getName())){ 
					throw new SQLException("DB - ERROR Schulklasse already in Database");
				}
			}
			sql = "INSERT INTO Schulklasse "
					+ "VALUES ('" + schulklasse.getName() + "',"
					+ schulklasse.getJahrgang() + ",'"
					+ schulklasse.getKlassenraum().getName() + "');";
			stmt.executeUpdate(sql);
			for(String klassenlehrer : schulklasse.getKlassenlehrer()) {
				sql = "INSERT INTO klassenlehrer "
						+ "VALUES ('" + schulklasse.getName() + "','"
						+ klassenlehrer + "');";
				stmt.executeUpdate(sql);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Schoolclass getSchulklasseByName(String pName) {
		try {
			sql = "SELECT * FROM Schulklasse WHERE name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int jahrgang = rs.getInt("jahrgang");
			String klassenraumName = rs.getString("klassenraumName");
			return new Schoolclass(name, jahrgang, DataRaum.getRaumByName(klassenraumName));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Schoolclass> getAllSchulklasse() {
		ArrayList<Schoolclass> allSchulklasse = new ArrayList<Schoolclass>();
		try {
			ArrayList<String> klassenraumNamen = new ArrayList<String>();
			sql = "SELECT * FROM Schulklasse";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				int jahrgang = rs.getInt("jahrgang");
				String klassenraumName = rs.getString("klassenraumName");
				klassenraumNamen.add(klassenraumName);
				allSchulklasse.add(new Schoolclass(name, jahrgang, null));
			}
			for(int i=0;i<allSchulklasse.size();i++) {
				allSchulklasse.get(i).setKlassenraum(DataRaum.getRaumByName(klassenraumNamen.get(i)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allSchulklasse;
	}
	
	public static void deleteSchulklasseByName(String pName) {
		try {
			sql = "DELETE FROM Schulklasse WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM klassenlehrer WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM stundenbedarf WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editSchulklasse(String pName, Schoolclass newSchulklasse) {
		
	}
	
	public static void addStundenbedarf() {
		
	}
	
	public static void addJahrgangStundenbedarf(Jahrgang jahrgang) {
		try {
			boolean inDB;
			for(Entry<String, Integer> entry : jahrgang.getStundenbedarf().entrySet()) {
				inDB = false;
				for(Jahrgang j : getAllJahrgang()) {
					if(jahrgang.getJahrgang() == j.getJahrgang()) {
						for(Entry<String, Integer> entryDB : j.getStundenbedarf().entrySet()) {
							if(entry.getKey().equals(entryDB.getKey())) {
								inDB = true;
								System.out.println("DB - JahrgangStundenbedarf already in Database");
							}
						}
					}
				}
				if(!inDB) {
					sql = "INSERT INTO Jahrgang_Stundenbedarf "
							+ "VALUES (" + jahrgang.getJahrgang() + ",'"
							+ entry.getKey() + "',"
							+ entry.getValue() + ");";
					stmt.executeUpdate(sql);
				}
			}
			System.out.println("DB - Jahrgang " + jahrgang.getJahrgang() + " added");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Jahrgang> getAllJahrgang() {
		ArrayList<Jahrgang> allJahrgangbedarf = new ArrayList<Jahrgang>();
		try {
			sql = "SELECT * FROM Jahrgang_Stundenbedarf ORDER BY jahrgang ASC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int jahrgang = rs.getInt("jahrgang");
				String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
				int bedarf = rs.getInt("bedarf");
				boolean put = false;
				for(int i=0;i<allJahrgangbedarf.size();i++) {
					if(allJahrgangbedarf.get(i).getJahrgang() == jahrgang && !put) {
						allJahrgangbedarf.get(i).getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
						put = true;
					}
				}
				if(!put) {
					HashMap<String, Integer> bedarfMap = new HashMap<String, Integer>();
					bedarfMap.put(stundeninhalt_kuerzel, bedarf);
					allJahrgangbedarf.add(new Jahrgang(jahrgang, bedarfMap));
				}
			}
			return allJahrgangbedarf;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deleteJahrgangbedarfByJAndSkuerzel(int pJahrgang, String pStundeninhalt_kuerzel) {
		try {
			sql = "DELETE FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + pJahrgang + " AND stundeninhalt_kuerzel = '" + pStundeninhalt_kuerzel + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
