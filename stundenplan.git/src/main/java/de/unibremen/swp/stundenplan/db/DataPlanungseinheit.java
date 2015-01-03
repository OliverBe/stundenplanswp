package de.unibremen.swp.stundenplan.db;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
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
			for (Entry<Personal, int[]> entry : planungseinheit.getPersonal().entrySet()) {
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
	
	
}
