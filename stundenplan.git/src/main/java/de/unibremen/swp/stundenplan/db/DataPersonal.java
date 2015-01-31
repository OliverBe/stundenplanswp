package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.exceptions.DeleteException;
import de.unibremen.swp.stundenplan.gui.StundenplanPanel;

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
					+ personal.getSollZeit() + ", 0, " + personal.getErsatzZeit() + ", "
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
			StundenplanPanel.updateLists(); 
			Data.setSaved(false);
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
				int istZeitMin = rs.getInt("istZeit");
				int istZeit = istZeitMin/60;
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
				sql = "SELECT * FROM Zeitwunsch WHERE personal_kuerzel = '" + pKuerzel + "';";
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
				p.setWunschZeiten(zeitwunsch);
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
				int istZeitMin = rs.getInt("istZeit");
				int istZeit = istZeitMin/60;
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
				p.setWunschZeiten(zeitwunsch);
			}
			return allPersonal;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String> getAllAcronymsFromPersonal(){
		try{ 
			sql = "SELECT kuerzel FROM Personal";
			ResultSet rs = stmt.executeQuery(sql);
			ArrayList<String> kuerzels = new ArrayList<>();
			while(rs.next()){
				kuerzels.add(rs.getString("kuerzel"));
			}
			
			return kuerzels;
			
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void deletePersonalByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM klassenlehrer WHERE personal_kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) throw new DeleteException("Das Personal ist als Klassenlehrer einer Klasse eingetragen und kann dadurch nicht gelöscht werden.");
			sql = "SELECT * FROM planungseinheit_Personal WHERE personal_kuerzel = '" + pKuerzel + "';";
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean yes = DeleteException.delete("Das Personal ist in einer Planungseinheit eingetragen.\nSoll das Personal trotzdem gelöscht werden?");
				if(yes) {
					ArrayList<Integer> pIds = new ArrayList<Integer>();
					do {
						int pId = rs.getInt("planungseinheit_id");
						pIds.add(pId);
					}while (rs.next());
					deleteSQL(pKuerzel);
					for(int pId : pIds) {
						DataPlanungseinheit.deleteIfEmpty(pId);
					}
				}
			}else deleteSQL(pKuerzel);
		} catch (SQLException | DeleteException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteSQL(String pKuerzel) throws SQLException {
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
	}
	
	public static void editPersonal(String pKuerzel, Personal newPersonal) {
		try {
			for(Personal pers : getAllPersonal()) {
				if(pers.getKuerzel().equals(newPersonal.getKuerzel())){ 
					throw new SQLException("DB - ERROR Personal already in Database");
				}
			}
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
