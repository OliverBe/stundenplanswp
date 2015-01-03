package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Schoolclass;

public class DataSchulklasse {

	private static Statement stmt = Data.stmt;
	private static String sql;

	private DataSchulklasse() {
	}

	public static void addSchulklasse(Schoolclass schulklasse) {
		try {
			sql = "INSERT INTO Schulklasse "
					+ "VALUES ('" + schulklasse.getName() + "',"
					+ schulklasse.getJahrgang() + ",'"
					+ schulklasse.getKlassenraum().getName() + "');";
			stmt.executeUpdate(sql);
			for(Personal klassenlehrer : schulklasse.getKlassenlehrer()) {
				sql = "INSERT INTO klassenlehrer "
						+ "VALUES ('" + schulklasse.getName() + "','"
						+ klassenlehrer.getKuerzel() + "');";
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
			return new Schoolclass(name, jahrgang, 
					DataRaum.getRaumByName(klassenraumName));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Schoolclass> getAllSchulklasse() {
		ArrayList<Schoolclass> allSchulklasse = new ArrayList<Schoolclass>();
		try {
			sql = "SELECT * FROM Schulklasse";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				allSchulklasse.add(getSchulklasseByName(name));
			}
		} catch (SQLException e) {}
		return allSchulklasse;
	}
	
	public static void deleteSchulklasseByName(String pName) {
		try {
			sql = "DELETE FROM Schulklasse WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM klassenlehrer WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {}
	}
}
