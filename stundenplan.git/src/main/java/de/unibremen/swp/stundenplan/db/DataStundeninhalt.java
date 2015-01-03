package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;

public class DataStundeninhalt {

	private static Statement stmt = Data.stmt;
	private static String sql;
	
	public static void addStundeninhalt(Stundeninhalt stundeninhalt) {
		try {
			sql = "INSERT INTO Stundeninhalt "
					+ "VALUES ('" + stundeninhalt.getName() + "','"
					+ stundeninhalt.getKuerzel() + "',"
					+ stundeninhalt.getRegeldauer() + ","
					+ stundeninhalt.getRhythmustyp() + ");";
			stmt.executeUpdate(sql);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Stundeninhalt getStundeninhaltByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int regeldauer = rs.getInt("regeldauer");
			int rhythmustyp = rs.getInt("rhythmustyp");
			return new Stundeninhalt(name, pKuerzel, regeldauer, rhythmustyp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Stundeninhalt> getAllStundeninhalte() {
		ArrayList<Stundeninhalt> allStundeninhalt = new ArrayList<Stundeninhalt>();
			try {
			sql = "SELECT * FROM Stundeninhalt";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String kuerzel = rs.getString("kuerzel");
				allStundeninhalt.add(getStundeninhaltByKuerzel(kuerzel));
			}
		} catch (SQLException e) {}
		return allStundeninhalt;
	}
	
	public static void deleteStundeninhaltByKuerzel(String pKuerzel) {
		try {
			sql = "DELETE FROM Schulklasse WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM moegliche_Stundeninhalte_Personal WHERE stundeninhalte_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM moegliche_Stundeninhalte_Raum WHERE kuerzel = '"
		} catch (SQLException e) {}
	}
}
