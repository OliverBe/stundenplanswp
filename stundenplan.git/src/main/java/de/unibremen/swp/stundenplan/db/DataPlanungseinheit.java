package de.unibremen.swp.stundenplan.db;

import java.sql.Statement;

public class DataPlanungseinheit {

	private static Statement stmt = null;
	private static String sql;
	
	private DataPlanungseinheit() {}
	
//	public void addPlanungseinheit(Planungseinheit planungseinheit) {
//		try {
//			sql = "INSERT INTO Planungseinheit "
//					+ "VALUES (" + planungseinheit.getId() + ","
//					+ planungseinheit.getWeekday() + ","
//					+ planungseinheit.getStartTimeslot().getId() + ","
//					+ planungseinheit.getEndTimeslot().getId() + ");";
//			stmt.executeUpdate(sql);
//			for(int kuerzel : planungseinheit.getStundeninhalte()) {
//				sql = "INSERT INTO planungseinheit_Stundeninhalt "
//						+ "VAUES (" + planungseinheit.getId() + ",'"
//						+ kuerzel + "');";
//				stmt.executeUpdate(sql);
//			}
//			for(int name : planungseinheit.getKlassen()) {
//				sql = "INSERT INTO planungseinheit_Schulklasse "
//						+ "VAUES (" + planungseinheit.getId() + ",'"
//						+ name + "');";
//				stmt.executeUpdate(sql);
//			}
//			for(int name : planungseinheit.getRaeume()) {
//				sql = "INSERT INTO planungseinheit_Raum "
//						+ "VAUES (" + planungseinheit.getId() + ",'"
//						+ name + "');";
//				stmt.executeUpdate(sql);
//			}
//			for(int kuerzel : planungseinheit.getPersonal()) {
//				sql = "INSERT INTO planungseinheit_Personal "
//						+ "VAUES (" + planungseinheit.getId() + ",'"
//						+ kuerzel + "');";
//				stmt.executeUpdate(sql);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
