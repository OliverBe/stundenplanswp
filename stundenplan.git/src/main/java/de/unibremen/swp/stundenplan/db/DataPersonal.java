package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;

public class DataPersonal {

	private static Statement stmt = Data.stmt;
	private static String sql;

	private DataPersonal() {
	}

	public static void addPersonal(Personal personal) {
		try {
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
			for (Entry<Weekday, int[]> entry : personal.getWunschzeiten()
					.entrySet()) {
				sql = "INSERT INTO Zeitwunsch " + "VALUES ('" 
						+ personal.getKuerzel() + "',"
						+ entry.getKey().getOrdinal() + ","
						+ entry.getValue()[0] + "," + entry.getValue()[1]
						+ ");";
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
			rs.next();
			String name = rs.getString("name");
			int sollZeit = rs.getInt("sollZeit");
			int istZeit = rs.getInt("istZeit");
			int ersatzZeit = rs.getInt("ersatzZeit");
			boolean schonGependelt = rs.getBoolean("schonGependelt");
			boolean lehrer = rs.getBoolean("lehrer");
			sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '"
					+ pKuerzel + "';";
			rs = stmt.executeQuery(sql);
			ArrayList<String> moeglicheStundeninhalte = new ArrayList<String>();
			while (rs.next()) {
				moeglicheStundeninhalte.add(rs
						.getString("stundeninhalt_kuerzel"));
			}
			return new Personal(name, pKuerzel, sollZeit, istZeit, ersatzZeit,
					schonGependelt, lehrer, moeglicheStundeninhalte);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fathanisiert
	 * 
	 * @return Liste mit allem Personal das sich in der DB befindet.
	 */
	public static ArrayList<Personal> getAllPersonal() {
		ArrayList<Personal> allPersonal = new ArrayList<Personal>();
		try {
			sql = "SELECT * FROM Personal";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String kuerzel = rs.getString("kuerzel");
				allPersonal.add(getPersonalByKuerzel(kuerzel));
			}
		} catch (SQLException e) {}
		return allPersonal;
	}

	public static void deletePersonalByKuerzel(String pKuerzel) {
		try {
			sql = "DELETE FROM Personal WHERE kuerzel = '" + pKuerzel + "'";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {}
	}
}
