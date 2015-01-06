package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;

public class DataPersonal {

	private static Statement stmt = Data.stmt;
	private static String sql;

	private DataPersonal() {}

	public static void addPersonal(Personal personal) {
		try {
			for(Personal pers : getAllPersonal()) {
				if(pers.getKuerzel().equals(personal.getKuerzel())){ 
					throw new SQLException("DB - ERROR Personal already in Database");
				}
			}
			sql = "INSERT INTO Personal " + "VALUES ('" + personal.getName()
					+ "', '" + personal.getKuerzel() + "', "
					+ personal.getSollZeit() + ", 0, 0, "
					+ (personal.isGependelt() ? 1:0) + ", "
					+ (personal.isLehrer() ? 1:0) + ");";
			stmt.executeUpdate(sql);
			for (String kuerzel : personal.getMoeglicheStundeninhalte()) {
				sql = "INSERT INTO moegliche_Stundeninhalte_Personal "
						+ "VALUES ('" + personal.getKuerzel() + "','" + kuerzel
						+ "');";
				stmt.executeUpdate(sql);
			}
			for (Entry<Weekday, int[]> entry : personal.getWunschzeiten().entrySet()) {
				sql = "INSERT INTO Zeitwunsch " + "VALUES ('" 
						+ personal.getKuerzel() + "',"
						+ entry.getKey().getOrdinal() + ","
						+ entry.getValue()[0] + ","
						+ entry.getValue()[1] + ","
						+ entry.getValue()[2] + ","
						+ entry.getValue()[3] + ");";
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Personal getPersonalByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM Personal WHERE kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String name = rs.getString("name");
				int sollZeit = rs.getInt("sollZeit");
				int istZeit = rs.getInt("istZeit");
				int ersatzZeit = rs.getInt("ersatzZeit");
				boolean schonGependelt = rs.getBoolean("schonGependelt");
				boolean lehrer = rs.getBoolean("lehrer");
				Personal p = new Personal(name, pKuerzel, sollZeit, istZeit, ersatzZeit,schonGependelt, lehrer);
				sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '"
						+ pKuerzel + "';";
				rs = stmt.executeQuery(sql);
				ArrayList<String> moeglicheStundeninhalte = new ArrayList<String>();
				while (rs.next()) {
					moeglicheStundeninhalte.add(rs.getString("stundeninhalt_kuerzel"));
				}
				p.setMoeglicheStundeninhalte(moeglicheStundeninhalte);
				return p;
			}else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Fathanisiert
	 * 
	 * @return Liste mit allem Personal das sich in der DB befindet.
	 */
	public static ArrayList<Personal> getAllPersonal() {
		try {
			ArrayList<Personal> allPersonal = new ArrayList<Personal>();
			sql = "SELECT * FROM Personal";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				String kuerzel = rs.getString("kuerzel");
				int sollZeit = rs.getInt("sollZeit");
				int istZeit = rs.getInt("istZeit");
				int ersatzZeit = rs.getInt("ersatzZeit");
				boolean schonGependelt = rs.getBoolean("schonGependelt");
				boolean lehrer = rs.getBoolean("lehrer");
				allPersonal.add(new Personal(name, kuerzel, sollZeit, istZeit, ersatzZeit, schonGependelt, lehrer));
			}
			for(Personal p : allPersonal) {
				sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '"
						+ p.getKuerzel() + "';";
				rs = stmt.executeQuery(sql);
				ArrayList<String> sk = new ArrayList<String>();
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					sk.add(stundeninhalt_kuerzel);
				}
				p.setMoeglicheStundeninhalte(sk);
			}
			for(Personal p : allPersonal) {
				sql = "SELECT * FROM Zeitwunsch WHERE personal_kuerzel = '"
						+ p.getKuerzel() + "';";
				rs = stmt.executeQuery(sql);
				HashMap<Weekday, int[]> zeitwunsch = new HashMap<Weekday, int[]>();
				while(rs.next()) {
					int weekday = rs.getInt("weekday");
					int[] zeiten = new int[4];
					zeiten[0] = rs.getInt("startHour");
					zeiten[1] = rs.getInt("startMin");
					zeiten[2] = rs.getInt("endHour");
					zeiten[3] = rs.getInt("endMin");
					zeitwunsch.put(Weekday.getDay(weekday), zeiten);
				}
			}
			return allPersonal;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void deletePersonalByKuerzel(String pKuerzel) {
		try {
			sql = "DELETE FROM Personal WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Zeitwunsch WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Personal WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM klassenlehrer WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editPersonal(String pKuerzel, Personal newPersonal) {
		try {
			sql = "DELETE FROM Personal WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Zeitwunsch WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Personal SET personal_kuerzel = '" + newPersonal.getKuerzel() + "' WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE klassenlehrer SET personal_kuerzel = '" + newPersonal.getKuerzel() + "' WHERE personal_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			addPersonal(newPersonal);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
