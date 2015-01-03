package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;

public class DataPlanungseinheit {

	private static Statement stmt = Data.stmt;
	private static String sql;
	
	private DataPlanungseinheit() {}
	
	public void addPlanungseinheit(Planungseinheit planungseinheit) {
		try {
			sql = "INSERT INTO Planungseinheit(id, weekday) "
					+ "VALUES (" + planungseinheit.getId() + ","
					+ planungseinheit.getWeekday().getOrdinal() + ");";
//					+ planungseinheit.getStartTimeslot().getId() + ","
//					+ planungseinheit.getEndTimeslot().getId() + ");";
			stmt.executeUpdate(sql);
			for(String kuerzel : planungseinheit.getStundeninhalte()) {
				sql = "INSERT INTO planungseinheit_Stundeninhalt "
						+ "VALUES (" + planungseinheit.getId() + ",'"
						+ kuerzel + "');";
				stmt.executeUpdate(sql);
			}
			for(String name : planungseinheit.getSchoolclasses()) {
				sql = "INSERT INTO planungseinheit_Schulklasse "
						+ "VALUES (" + planungseinheit.getId() + ",'"
						+ name + "');";
				stmt.executeUpdate(sql);
			}
			for(String name : planungseinheit.getRooms()) {
				sql = "INSERT INTO planungseinheit_Raum "
						+ "VALUES (" + planungseinheit.getId() + ",'"
						+ name + "');";
				stmt.executeUpdate(sql);
			}
			for (Entry<Personal, int[]> entry : planungseinheit.getPersonalMap().entrySet()) {
				sql = "INSERT INTO planungseinheit_Personal " + "VALUES (" 
						+ planungseinheit.getId() + ",'"
						+ entry.getKey().getKuerzel() + "',"
						+ entry.getValue()[0] + "," 
						+ entry.getValue()[1] + ","
						+ entry.getValue()[2] + ","
						+ entry.getValue()[3] + ");";
				stmt.executeUpdate(sql);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Planungseinheit> getAllPlanungseinheit() {
		try {
			ArrayList<Planungseinheit> allPlanungseinheit = new ArrayList<Planungseinheit>();
			sql = "SELECT * FROM Planungseinheit";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				int weekday = rs.getInt("weekday");
				int startHour = rs.getInt("startHour");
				int startMin = rs.getInt("startMin");
				int endHour = rs.getInt("endHour");
				int endMin = rs.getInt("endMin");
				allPlanungseinheit.add(new Planungseinheit(id, Weekday.MONDAY, startHour, startMin, endHour, endMin));
			}
//			for(Personal p : allPersonal) {
//				sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = '"
//						+ p.getKuerzel() + "';";
//				rs = stmt.executeQuery(sql);
//				while(rs.next()) {
//					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
//					p.getMoeglicheStundeninhalte().add(stundeninhalt_kuerzel);
//				}
//			}
			return allPlanungseinheit;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void deletePlanungseinheitById(int id) {
		try {
			sql = "DELETE FROM Planungseinheit WHERE id = " + id + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Personal WHERE planungseinheit_id = " + id + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = " + id + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Schulklasse WHERE planungseinheit_id = " + id + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM planungseinheit_Raum WHERE planungseinheit_id = " + id + ";";
			stmt.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
