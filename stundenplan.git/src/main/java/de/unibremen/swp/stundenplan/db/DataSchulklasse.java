package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;
import de.unibremen.swp.stundenplan.exceptions.BesetztException;
import de.unibremen.swp.stundenplan.exceptions.DeleteException;
import de.unibremen.swp.stundenplan.gui.StundenplanPanel;

public class DataSchulklasse {

	private static Statement stmt = Data.stmt;
	private static String sql;

	private DataSchulklasse() {}

	public static void addSchulklasse(Schoolclass schulklasse) {
		boolean error = false;
		try {
			for(Schoolclass sc : getAllSchulklasse()) {
				if(sc.getName().equals(schulklasse.getName())){ 
					new BereitsVorhandenException();
					error = true;
				}
			}
			for(String klassenlehrer : schulklasse.getKlassenlehrer()) {
				sql = "SELECT * FROM klassenlehrer WHERE personal_kuerzel = '" + klassenlehrer + "';";
				ResultSet rs = stmt.executeQuery(sql);
				if(rs.next()) {
					new BesetztException("Personal");
					error = true;
				}
			}
			sql = "SELECT * FROM Schulklasse WHERE klassenraumName = '" + schulklasse.getKlassenraum().getName() + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				new BesetztException("Raum");
				error = true;
			}
			if(error) return;
			sql = "INSERT INTO Schulklasse "
					+ "VALUES ('" + schulklasse.getName() + "',"
					+ schulklasse.getJahrgang() + ",'"
					+ schulklasse.getKlassenraum().getName() + "');";
			stmt.executeUpdate(sql);
			for(String klassenlehrer : schulklasse.getKlassenlehrer()) {
				sql = "INSERT INTO klassenlehrer "
						+ "VALUES ('" + schulklasse.getName() + "','"
						+ klassenlehrer + "');";
				stmt.executeUpdate(sql);
			}
			for (Entry<String, Integer> entry : schulklasse.getStundenbedarf().entrySet()) {
				sql = "INSERT INTO stundenbedarf " + "VALUES ('" 
						+ schulklasse.getName() + "', '"
						+ entry.getKey() + "', "
						+ entry.getValue() + ");";
				stmt.executeUpdate(sql);
			}
			StundenplanPanel.updateLists(); 
			Data.setSaved(false);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Schoolclass getSchulklasseByName(String pName) {
		try {
			sql = "SELECT * FROM Schulklasse WHERE name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int jahrgang = rs.getInt("jahrgang");
			String klassenraumName = rs.getString("klassenraumName");
			Schoolclass sc = new Schoolclass(name, jahrgang, DataRaum.getRaumByName(klassenraumName), new ArrayList<String>(), new HashMap<String, Integer>());
			sql = "SELECT * FROM klassenlehrer WHERE schulklasse_name = '" + sc.getName() + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String personal_kuerzel = rs.getString("personal_kuerzel");
				sc.getKlassenlehrer().add(personal_kuerzel);
			}
			sql = "SELECT * FROM stundenbedarf WHERE schulklasse_name = '" + sc.getName() + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
				int bedarf = rs.getInt("bedarf");
				sc.getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
			}
			return sc;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Schoolclass> getAllSchulklasse() {
		ArrayList<Schoolclass> allSchulklasse = new ArrayList<Schoolclass>();
		try {
			ArrayList<String> klassenraumNamen = new ArrayList<String>();
			sql = "SELECT * FROM Schulklasse";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				int jahrgang = rs.getInt("jahrgang");
				String klassenraumName = rs.getString("klassenraumName");
				klassenraumNamen.add(klassenraumName);
				allSchulklasse.add(new Schoolclass(name, jahrgang, new Room(), new ArrayList<String>(), new HashMap<String, Integer>()));
			}
			for(int i=0;i<allSchulklasse.size();i++) {
				allSchulklasse.get(i).setKlassenraum(DataRaum.getRaumByName(klassenraumNamen.get(i)));
			}
			for(Schoolclass sc : allSchulklasse) {
				sql = "SELECT * FROM klassenlehrer WHERE schulklasse_name = '" + sc.getName() + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String personal_kuerzel = rs.getString("personal_kuerzel");
					sc.getKlassenlehrer().add(personal_kuerzel);
				}
				sql = "SELECT * FROM stundenbedarf WHERE schulklasse_name = '" + sc.getName() + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					int bedarf = rs.getInt("bedarf");
					sc.getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allSchulklasse;
	}
	
	public static ArrayList<String> getAllNameFromSchulklasse(){
				try{ 
					sql = "SELECT name FROM Schulklasse";
					ResultSet rs = stmt.executeQuery(sql);
					ArrayList<String> names = new ArrayList<>();
					while(rs.next()){
						names.add(rs.getString("name"));
					}
					
					return names;
					
				}catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
	
	public static void deleteSchulklasseByName(String pName) {
		try {
			sql = "SELECT * FROM planungseinheit_Schulklasse WHERE schulklasse_name = '" + pName + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean yes = DeleteException.delete("Die Schulklasse ist in einer Planungseinheit eingetragen.\nSoll die Schulklasse trotzdem gelöscht werden?");
				if(yes) {
					ArrayList<Integer> pIds = new ArrayList<Integer>();
					do {
						int pId = rs.getInt("planungseinheit_id");
						pIds.add(pId);
					}while (rs.next());
					deleteSQL(pName);
					for(int pId : pIds) {
						DataPlanungseinheit.deleteIfEmpty(pId);
					}
				}
			}else deleteSQL(pName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteSQL(String pName) throws SQLException {
		sql = "DELETE FROM Schulklasse WHERE name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM klassenlehrer WHERE schulklasse_name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM stundenbedarf WHERE schulklasse_name = '" + pName + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM planungseinheit_Schulklasse WHERE schulklasse_name = '" + pName + "';";
		stmt.executeUpdate(sql);
	}
	
	public static void editSchulklasse(String pName, Schoolclass newSchulklasse) {
		try {
			for(Schoolclass sc : getAllSchulklasse()) {
				if(sc.getName().equals(newSchulklasse.getName()) && !sc.getName().equals(pName)){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "DELETE FROM Schulklasse WHERE name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM klassenlehrer WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM stundenbedarf WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Schulklasse SET schulklasse_name = '" + newSchulklasse.getName() + "' WHERE schulklasse_name = '" + pName + "';";
			stmt.executeUpdate(sql);
			addSchulklasse(newSchulklasse);
		} catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	public static void addJahrgang(Jahrgang jahrgang) {
		try {
			boolean inDB;
			for(Entry<String, Integer> entry : jahrgang.getStundenbedarf().entrySet()) {
				inDB = false;
				for(Jahrgang j : getAllJahrgang()) {
					if(jahrgang.getJahrgang() == j.getJahrgang()) {
						for(Entry<String, Integer> entryDB : j.getStundenbedarf().entrySet()) {
							if(entry.getKey().equals(entryDB.getKey())) {
								inDB = true;
								throw new BereitsVorhandenException();
							}
						}
					}
				}
				if(!inDB) {
					sql = "INSERT INTO Jahrgang_Stundenbedarf "
							+ "VALUES (" + jahrgang.getJahrgang() + ",'"
							+ entry.getKey() + "',"
							+ entry.getValue() + ");";
					stmt.executeUpdate(sql);
				}
			}
			Data.setSaved(false);
		}catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	public static Jahrgang getJahrgangByJahrgang(int jahrgang) {
		try {
			sql = "SELECT * FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + jahrgang + ";";
			ResultSet rs = stmt.executeQuery(sql);
			Jahrgang jg = new Jahrgang(jahrgang, new HashMap<String, Integer>());
			while (rs.next()) {
				String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
				int bedarf = rs.getInt("bedarf");
				jg.getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
			}
			return jg;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Jahrgang getJahrgangByJundSkuerzel(int jahrgang, String stundeninhalt_kuerzel) {
		try {
			sql = "SELECT * FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + jahrgang + " AND stundeninhalt_kuerzel = '" + stundeninhalt_kuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			Jahrgang jg = new Jahrgang(jahrgang, new HashMap<String, Integer>());
			rs.next();
			int bedarf = rs.getInt("bedarf");
			jg.getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
			return jg;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Jahrgang> getAllJahrgang() {
		ArrayList<Jahrgang> allJahrgangbedarf = new ArrayList<Jahrgang>();
		try {
			sql = "SELECT DISTINCT jahrgang FROM Jahrgang_Stundenbedarf ORDER BY jahrgang ASC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int jahrgang = rs.getInt("jahrgang");
				allJahrgangbedarf.add(new Jahrgang(jahrgang, new HashMap<String, Integer>()));
			}
			for(int i=0;i<allJahrgangbedarf.size();i++) {
				sql = "SELECT * FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + allJahrgangbedarf.get(i).getJahrgang() + ";";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String stundeninhalt_kuerzel = rs.getString("stundeninhalt_kuerzel");
					int bedarf = rs.getInt("bedarf");
					allJahrgangbedarf.get(i).getStundenbedarf().put(stundeninhalt_kuerzel, bedarf);
				}
			}
			return allJahrgangbedarf;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void deleteJahrgangbedarfByJahrgang(int pJahrgang) {
		try {
			sql = "DELETE FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + pJahrgang + ";";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteJahrgangbedarfByJAndSkuerzel(int pJahrgang, String pStundeninhalt_kuerzel) {
		try {
			sql = "DELETE FROM Jahrgang_Stundenbedarf WHERE jahrgang = " + pJahrgang + " AND stundeninhalt_kuerzel = '" + pStundeninhalt_kuerzel + "';";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editJahrgang(Jahrgang jahrgang) {
		try {
			for(Entry<String, Integer> entry : jahrgang.getStundenbedarf().entrySet()) {
				sql = "UPDATE Jahrgang_Stundenbedarf SET bedarf = " + entry.getValue() + " WHERE jahrgang = " + jahrgang.getJahrgang() + " AND stundeninhalt_kuerzel = '" + entry.getKey() + "';";
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
