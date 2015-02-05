package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;
import de.unibremen.swp.stundenplan.exceptions.DeleteException;
import de.unibremen.swp.stundenplan.gui.StundenplanPanel;

/**
 * Klasse stellt die Verbindung von Personal zur Datenbank dar.
 * 
 * @author Kim-Long
 *
 */
public class DataPersonal {
	/**
	 * Speichert das Statement, das in der Data Klasse erstellt wurde.
	 */
	private static Statement stmt = Data.stmt;
	/**
	 * Speichert den SQL-Befehl.
	 */
	private static String sql;
	
	/**
     * Privater Konstruktor, sodass kein Object dieser Klasse erstellt werden kann.
     */
	private DataPersonal() {}

	/**
	 * Fuegt das uebergebene Personal zur Datenbank hinzu.
	 * 
	 * @param personal
	 * 		das Personal, welches in die Datenbank gespeichert werden soll
	 */
	public static void addPersonal(Personal personal) {
		try {
			for(Personal pers : getAllPersonal()) {
				if(pers.getKuerzel().equals(personal.getKuerzel())){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "INSERT INTO Personal " + "VALUES ('" + personal.getName()
					+ "', '" + personal.getKuerzel() + "', "
					+ personal.getSollZeit() + ", 0, " + personal.getErsatzZeit() + ", "
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
		} catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode gibt das Personal aus der Datenbank zurueck, 
	 * das den uebergebenen Kuerzel besitzt.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel nach dem gesucht werden soll
	 * @return	das Personal mit dem uebergebenen Kuerzel, 
	 * 			wenn nicht vorhanden null
	 */
	public static Personal getPersonalByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM Personal WHERE kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				String name = rs.getString("name");
				int sollZeit = rs.getInt("sollZeit");
				int istZeitMin = rs.getInt("istZeit");
				int ersatzZeit = rs.getInt("ersatzZeit");
				boolean lehrer = rs.getBoolean("lehrer");
				int istZeit = istZeitMin/(lehrer ? 45:60);
				Personal p = new Personal(name, pKuerzel, sollZeit, istZeit, ersatzZeit, new HashMap<Weekday, Boolean>(), lehrer, new ArrayList<String>(), new HashMap<Weekday, int[]>());
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
	 * Gibt eine Liste aller Personal aus der Datenbank zurueck.
	 * 
	 * @return	Liste mit allem Personal, die sich in der Datenbank befindet.
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
				int ersatzZeit = rs.getInt("ersatzZeit");
				boolean lehrer = rs.getBoolean("lehrer");
				int istZeit = istZeitMin/(lehrer ? 45:60);
				allPersonal.add(new Personal(name, kuerzel, sollZeit, istZeit, ersatzZeit, new HashMap<Weekday, Boolean>(), lehrer, new ArrayList<String>(), new HashMap<Weekday, int[]>()));
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

	/**
	 * Methode gibt eine Liste mit allen Kuerzeln der Personal in der Datenbank zurueck.
	 * 
	 * @return	eine ArrayList mit allen Kuerzeln der Personal in der Datenbank
	 */
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
	
	/**
	 * Methode loescht das Personal mit dem uebergebenen Kuerzel.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Personals, das geloescht werden soll
	 */
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
	
	/**
	 * Private Hilfsmethode zum Loeschen eines Personals
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Personals, das geloescht werden soll
	 * @throws SQLException
	 */
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
	
	/**
	 * Methode bearbeitet ein Personal in der Datenbank.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Personals, das bearbeitet werden soll
	 * @param newPersonal
	 * 		das bearbeitete Personal
	 */
	public static void editPersonal(String pKuerzel, Personal newPersonal) {
		try {
			for(Personal pers : getAllPersonal()) {
				if(pers.getKuerzel().equals(newPersonal.getKuerzel()) && !pers.getKuerzel().equals(pKuerzel)){ 
					throw new BereitsVorhandenException();
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
		} catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	public static void setGependelt(String personal_kuerzel, Weekday day, boolean gependelt) {
		try {
			sql = "UPDATE gependelt_Personal SET gependelt = " + (gependelt ? 1:0) + " "
					+ "WHERE personal_kuerzel = '" + personal_kuerzel + "' "
					+ "AND weekday = " + day.getOrdinal() + ";";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean getGependelt(String personal_kuerzel, Weekday day) {
		try {
			sql = "SELECT gependelt FROM gependelt_Personal "
					+ "WHERE personal_kuerzel = '" + personal_kuerzel + "' "
					+ "AND weekday = " + day.getOrdinal() + ";";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			boolean gependelt = rs.getBoolean("gependelt");
			return gependelt;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
