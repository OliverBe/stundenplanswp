package de.unibremen.swp.stundenplan.db;

import java.sql.Statement;

public class DataStundeninhalt {

	private static Statement stmt = null;
	private static String sql;
	
// public void addStundeninhalt(Stundeninhalt stundeninhalt) {
//		try {
//			sql = "INSERT INTO Stundeninhalt "
//					+ "VALUES ('" + stundeninhalt.getName() + "','"
//					+ stundeninhalt.getkuerzel() + "',"
//					+ stundeninhalt.getRegeldauer() + ","
//					+ stundeninhalt.getRhythmustyp() + ");";
//			stmt.executeUpdate(sql);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
