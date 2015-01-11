package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;

public class DataStundeninhalt {

	private static Statement stmt = Data.stmt;
	private static String sql;
	
	private DataStundeninhalt() {}
	
	public static void addStundeninhalt(Stundeninhalt stundeninhalt) {
		try {
			for(Stundeninhalt si : getAllStundeninhalte()) {
				if(si.getKuerzel().equals(stundeninhalt.getKuerzel())){ 
					throw new SQLException("DB - ERROR Stundeninhalt already in Database");
				}
			}
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
				String name = rs.getString("name");
				String kuerzel = rs.getString("kuerzel");
				int regeldauer = rs.getInt("regeldauer");
				int rhythmustyp = rs.getInt("rhythmustyp");
				allStundeninhalt.add(new Stundeninhalt(name, kuerzel, regeldauer, rhythmustyp));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allStundeninhalt;
	}
	
	public static void deleteStundeninhaltByKuerzel(String pKuerzel) {
		try {
			sql = "DELETE FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM moegliche_Stundeninhalte_Personal WHERE stundeninhalte_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Raumfunktion WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM stundenbedarf WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Stundeninhalt WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Jahrgang_Stundenbedarf WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editStundeninhalt(String pKuerzel, Stundeninhalt newStundeninhalt) {
		try {
			sql = "DELETE FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE moegliche_Stundeninhalte_Personal SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalte_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE Raumfunktion SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE stundenbedarf SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Stundeninhalt SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE Jahrgang_Stundenbedarf SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			addStundeninhalt(newStundeninhalt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
