package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

public class DataPlanungseinheit {

	private static Statement stmt = Data.stmt;
	private static String sql;
	
	private DataPlanungseinheit() {}
	
	public static void addPlanungseinheit(Planungseinheit planungseinheit) {
		try {
			sql = "INSERT INTO Planungseinheit "
					+ "VALUES (" + planungseinheit.getId() + ","
					+ planungseinheit.getWeekday().getOrdinal() + ","
					+ planungseinheit.getStartHour() + ","
					+ planungseinheit.getStartminute() + ","
					+ planungseinheit.getEndhour() + ","
					+ planungseinheit.getEndminute() + ");";
			stmt.executeUpdate(sql);
			for (Entry<String, int[]> entry : planungseinheit.getPersonalMap().entrySet()) {
				sql = "INSERT INTO planungseinheit_Personal " + "VALUES (" 
						+ planungseinheit.getId() + ",'"
						+ entry.getKey() + "',"
						+ entry.getValue()[0] + "," 
						+ entry.getValue()[1] + ","
						+ entry.getValue()[2] + ","
						+ entry.getValue()[3] + ");";
				stmt.executeUpdate(sql);
				sql = "UPDATE Personal SET istZeit = istZeit + " 
						+ planungseinheit.duration() 
						+ " WHERE kuerzel = '" + entry.getKey() + "';";
				stmt.executeUpdate(sql);
			}
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
			Data.setSaved(false);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Planungseinheit> getAllPlanungseinheit() {
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
				allPlanungseinheit.add(new Planungseinheit(id, Weekday.getDay(weekday), startHour, startMin, endHour, endMin));
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String personal_kuerzel = rs.getString("personal_kuerzel");
					int zeiten[] = new int[4];
					zeiten[0] = rs.getInt("startZeitHour");
					zeiten[1] = rs.getInt("startZeitMin");
					zeiten[2] = rs.getInt("endZeitHour");
					zeiten[3] = rs.getInt("endZeitMin");
					p.getPersonalMap().put(personal_kuerzel, zeiten);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					p.getStundeninhalte().add(stundeninhalt_kuerzel);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Schulklasse WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String schulklasse_name = rs.getString("schulklasse_name");
					p.getSchoolclasses().add(schulklasse_name);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Raum WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String raum_name = rs.getString("raum_name");
					p.getRooms().add(raum_name);
				}
			}
			return allPlanungseinheit;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Planungseinheit> getAllPlanungseinheitByWeekday(final Weekday pWeekday) {
		try {
			ArrayList<Planungseinheit> allPlanungseinheit = new ArrayList<Planungseinheit>();
			sql = "SELECT * FROM Planungseinheit WHERE weekday = "+ pWeekday.getOrdinal();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				int weekday = rs.getInt("weekday");
				int startHour = rs.getInt("startHour");
				int startMin = rs.getInt("startMin");
				int endHour = rs.getInt("endHour");
				int endMin = rs.getInt("endMin");
				allPlanungseinheit.add(new Planungseinheit(id, Weekday.getDay(weekday), startHour, startMin, endHour, endMin));
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String personal_kuerzel = rs.getString("personal_kuerzel");
					int zeiten[] = new int[4];
					zeiten[0] = rs.getInt("startZeitHour");
					zeiten[1] = rs.getInt("startZeitMin");
					zeiten[2] = rs.getInt("endZeitHour");
					zeiten[3] = rs.getInt("endZeitMin");
					p.getPersonalMap().put(personal_kuerzel, zeiten);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					p.getStundeninhalte().add(stundeninhalt_kuerzel);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Schulklasse WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String schulklasse_name = rs.getString("schulklasse_name");
					p.getSchoolclasses().add(schulklasse_name);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Raum WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String raum_name = rs.getString("raum_name");
					p.getRooms().add(raum_name);
				}
			}
			return allPlanungseinheit;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Planungseinheit getPlanungseinheitById(int pId){
		Planungseinheit pl = new Planungseinheit();
		try{
			sql = "SELECT * FROM Planungseinheit WHERE id = " + pId + ";";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				int id = rs.getInt("id");
				int weekday = rs.getInt("weekday");
				int startHour = rs.getInt("startHour");
				int startMin = rs.getInt("startMin");
				int endHour = rs.getInt("endHour");
				int endMin = rs.getInt("endMin");
				pl = new Planungseinheit(id, Weekday.getDay(weekday), startHour, startMin, endHour, endMin);
				sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = "
						+ pId + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String personal_kuerzel = rs.getString("personal_kuerzel");
					int zeiten[] = new int[4];
					zeiten[0] = rs.getInt("startZeitHour");
					zeiten[1] = rs.getInt("startZeitMin");
					zeiten[2] = rs.getInt("endZeitHour");
					zeiten[3] = rs.getInt("endZeitMin");
					pl.getPersonalMap().put(personal_kuerzel, zeiten);
				}
				sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = "
						+ pId + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					pl.getStundeninhalte().add(stundeninhalt_kuerzel);
				}
				sql = "SELECT * FROM planungseinheit_Schulklasse WHERE planungseinheit_id = "
						+ pId + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String schulklasse_name = rs.getString("schulklasse_name");
					pl.getSchoolclasses().add(schulklasse_name);
				}
				sql = "SELECT * FROM planungseinheit_Raum WHERE planungseinheit_id = "
						+ pId + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String raum_name = rs.getString("raum_name");
					pl.getRooms().add(raum_name);
				}
				return pl;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Planungseinheit> getAllPlanungseinheitByWeekdayAndObject(Weekday day, Object object) {
		try {
			ArrayList<Planungseinheit> allPlanungseinheit = new ArrayList<Planungseinheit>();
			if(object instanceof Room) {
				sql = "SELECT * FROM Planungseinheit, planungseinheit_Raum "
						+ "WHERE id = planungseinheit_id "
						+ "AND weekday = " + day.getOrdinal() + " "
						+ "AND raum_name = '" + ((Room)object).getName() + "';";
			}else if(object instanceof Personal) {
				sql = "SELECT * FROM Planungseinheit, planungseinheit_Personal "
						+ "WHERE id = planungseinheit_id "
						+ "AND weekday = " + day.getOrdinal() + " "
						+ "AND personal_kuerzel = '" + ((Personal)object).getKuerzel() + "';";
			}else if(object instanceof Schoolclass) {
				sql = "SELECT * FROM Planungseinheit, planungseinheit_Schulklasse "
						+ "WHERE id = planungseinheit_id "
						+ "AND weekday = " + day.getOrdinal() + " "
						+ "AND schulklasse_name = '" + ((Schoolclass)object).getName() + "';";
			}else return null;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				int weekday = rs.getInt("weekday");
				int startHour = rs.getInt("startHour");
				int startMin = rs.getInt("startMin");
				int endHour = rs.getInt("endHour");
				int endMin = rs.getInt("endMin");
				allPlanungseinheit.add(new Planungseinheit(id, Weekday.getDay(weekday), startHour, startMin, endHour, endMin));
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String personal_kuerzel = rs.getString("personal_kuerzel");
					int zeiten[] = new int[4];
					zeiten[0] = rs.getInt("startZeitHour");
					zeiten[1] = rs.getInt("startZeitMin");
					zeiten[2] = rs.getInt("endZeitHour");
					zeiten[3] = rs.getInt("endZeitMin");
					p.getPersonalMap().put(personal_kuerzel, zeiten);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					p.getStundeninhalte().add(stundeninhalt_kuerzel);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Schulklasse WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String schulklasse_name = rs.getString("schulklasse_name");
					p.getSchoolclasses().add(schulklasse_name);
				}
			}
			for(Planungseinheit p : allPlanungseinheit) {
				sql = "SELECT * FROM planungseinheit_Raum WHERE planungseinheit_id = "
						+ p.getId() + ";";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String raum_name = rs.getString("raum_name");
					p.getRooms().add(raum_name);
				}
			}
			return allPlanungseinheit;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void deletePlanungseinheitById(int id) {
		try {
			sql = "SELECT * FROM Planungseinheit WHERE id = " + id + ";";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			int startHour = rs.getInt("startHour");
			int startMin = rs.getInt("startMin");
			int endHour = rs.getInt("endHour");
			int endMin = rs.getInt("endMin");
			int dif = TimetableManager.duration(startHour, startMin, endHour, endMin);
			sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = " + id + ";";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String personal_kuerzel = rs.getString("personal_kuerzel");
				sql = "UPDATE Personal SET istZeit = istZeit - " + dif + " WHERE kuerzel = '" + personal_kuerzel + "';";
				stmt.executeUpdate(sql);
			}
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
	
	public static void editPlanungseinheit(int iD, Planungseinheit planungseinheit) {
			deletePlanungseinheitById(iD);
			addPlanungseinheit(planungseinheit);
	}
	
	protected static void deleteIfEmpty(int id) {
		try {
			sql = "SELECT * FROM planungseinheit_Personal WHERE planungseinheit_id = " + id + ";";
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.next()) {
				sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE planungseinheit_id = " + id + ";";
				rs = stmt.executeQuery(sql);
				if(!rs.next()) {
					sql = "SELECT * FROM planungseinheit_Schulklasse WHERE planungseinheit_id = " + id + ";";
					rs = stmt.executeQuery(sql);
					if(!rs.next()) {
						sql = "SELECT * FROM planungseinheit_Raum WHERE planungseinheit_id = " + id + ";";
						rs = stmt.executeQuery(sql);
						if(!rs.next()) deletePlanungseinheitById(id);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmpty() {
		return getAllPlanungseinheit().isEmpty();
	}

	public static void deleteAll() {
		ArrayList<Planungseinheit> allPlanungseinheit = getAllPlanungseinheit();
		for(Planungseinheit p : allPlanungseinheit) {
			deletePlanungseinheitById(p.getId());
		}
	}
}
