package de.unibremen.swp.stundenplan.db;

import java.sql.*;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.config.*;
import de.unibremen.swp.stundenplan.data.*;

public class Data {
	public final static int MAX_KUERZEL_LEN = 3;
	public final static int MAX_NORMAL_STRING_LEN = 20;
	private static Connection c = null;
    private static Statement stmt = null;
    private static String sql;
	
    private Data() {}
    
	public static void start() {
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
		    System.out.println("Opened database successfully");
		    
	    	stmt = c.createStatement();
	    	
	    	//Personal
	    	sql = "CREATE TABLE IF NOT EXISTS Personal "
	    			+ "(name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "sollZeit INT NOT NULL, "
	    			+ "istZeit INT NOT NULL, "
	    			+ "ersatzZeit INT NOT NULL, "
	    			+ "schonGependelt BOOLEAN NOT NULL, "
	    			+ "lehrer BOOLEAN NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Planungseinheit
	    	sql = "CREATE TABLE IF NOT EXISTS Planungseinheit "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startTimeslot INT NOT NULL, "
	    			+ "endTimeslot INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Schulklasse
	    	sql = "CREATE TABLE IF NOT EXISTS Schulklasse "
	    			+ "(name VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "jahrgang INT NOT NULL, "
	    			+ "klassenraumId INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Raum
	    	sql = "CREATE TABLE IF NOT EXISTS Raum "
	    			+ "(name VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "gebaeudennr INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Stundeninhalt
	    	sql = "CREATE TABLE IF NOT EXISTS Stundeninhalt "
	    			+ "(name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "regeldauer INT NOT NULL, "
	    			+ "rhythmustyp INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Map von Personal
	    	sql = "CREATE TABLE IF NOT EXISTS Zeitwunsch "
	    			+ "(personal_kuerzel VARCHAR NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startZeit INT NOT NULL, "
	    			+ "endZeit INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_kuerzel, weekday)"
	    			+ "FOREIGN KEY (personal_kuerzel) REFERENCES Personal(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Personal(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS moegliche_Stundeninhalte_Personal "
	    			+ "(personal_kuerzel VARCHAR NOT NULL, "
	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (personal_kuerzel, stundeninhalt_kuerzel), "
	    			+ "FOREIGN KEY (personal_kuerzel) REFERENCES Personal(kuerzel), "
	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Stundeninhalte(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Personal)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Personal "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "personal_kuerzel VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, personal_kuerzel), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id)"
	    			+ "FOREIGN KEY (personal_kuerzel) REFERENCES Personal(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Schulklasse(Personal)
	    	sql = "CREATE TABLE IF NOT EXISTS klassenlehrer "
	    			+ "(schulklasse_name VARCHAR NOT NULL, "
	    			+ "personal_kuerzel VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (schulklasse_name, personal_kuerzel), "
	    			+ "FOREIGN KEY (schulklasse_name) REFERENCES Schulklasse(name), "
	    			+ "FOREIGN KEY (personal_kuerzel) REFERENCES Personal(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Stundeninhalt "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, stundeninhalt_kuerzel), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Stundeninhalt(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Raumfunktion(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS Raumfunktion "
//	    			+ "(raum_name VARCHAR NOT NULL, "
//	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "(name VARCHAR NOT NULL)";
//	    			+ "PRIMARY KEY (raum_name, stundeninhalt_kuerzel), "
//	    			+ "FOREIGN KEY (raum_name) REFERENCES Raum(name), "
//	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Stundeninhalt(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Schulklasse(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS stundenbedarf "
	    			+ "(schulklasse_name VARCHAR NOT NULL, "
	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "bedarf INT NOT NULL, "
	    			+ "PRIMARY KEY (schulklasse_name, stundeninhalt_kuerzel), "
	    			+ "FOREIGN KEY (schulklasse_name) REFERENCES Schulklasse(name), "
	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Stundeninhalt(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Jahrgang(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS Jahrgang_Stundenbedarf "
	    			+ "(jahrgang INT NOT NULL, "
	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "bedarf INT NOT NULL, "
	    			+ "PRIMARY KEY (jahrgang, stundeninhalt_kuerzel), "
	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Stundeninhalt(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Schulklasse)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Schulklasse "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "schulklasse_name VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, schulklasse_name), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (schulklasse_name) REFERENCES Schulklasse(name))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Raum)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Raum "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "raum_name VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, raum_name), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (raum_name) REFERENCES Raum(name))";
	    	stmt.executeUpdate(sql);
	    	System.out.println("Tables created.");
	    }catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
	
	public static void addPersonal(Personal personal) {
		try {
			sql = "INSERT INTO Personal "
					+ "VALUES (" + personal.getName() + ","
					+ personal.getKuerzel() + ","
					+ personal.getSollZeit() + ","
					+ personal.getIstZeit() + ","
					+ personal.getErsatzZeit() + ","
					+ Boolean.toString(personal.isGependelt()) + ","
					+ Boolean.toString(personal.isLehrer()) + ");";
			stmt.executeUpdate(sql);
			for(String kuerzel : personal.getMoeglicheStundeninhalte()) {
				sql = "INSERT INTO moegliche_Stundeninhalte_Personal "
						+ "VALUES (" + personal.getKuerzel() + ","
						+ kuerzel + ");";
			}
//			for(Weekday weekday : personal.getWunschZeiten().keySet()) {
//				for()
//			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
//						+ "VAUES (" + planungseinheit.getId() + ","
//						+ kuerzel + ");";
//				stmt.executeUpdate(sql);
//			}
//			for(int name : planungseinheit.getKlassen()) {
//				sql = "INSERT INTO planungseinheit_Schulklasse "
//						+ "VAUES (" + planungseinheit.getId() + ","
//						+ name + ");";
//				stmt.executeUpdate(sql);
//			}
//			for(int name : planungseinheit.getRaeume()) {
//				sql = "INSERT INTO planungseinheit_Raum "
//						+ "VAUES (" + planungseinheit.getId() + ","
//						+ name + ");";
//				stmt.executeUpdate(sql);
//			}
//			for(int kuerzel : planungseinheit.getPersonal()) {
//				sql = "INSERT INTO planungseinheit_Personal "
//						+ "VAUES (" + planungseinheit.getId() + ","
//						+ kuerzel + ");";
//				stmt.executeUpdate(sql);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void addStundeninhalt(Stundeninhalt stundeninhalt) {
//		try {
//			sql = "INSERT INTO Stundeninhalt "
//					+ "VALUES (" + stundeninhalt.getName() + ","
//					+ stundeninhalt.getkuerzel() + ","
//					+ stundeninhalt.getRegeldauer() + ","
//					+ stundeninhalt.getRhythmustyp() + ");";
//			stmt.executeUpdate(sql);
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void addSchulklasse(Schoolclass schulklasse) {
//		try {
//			sql = "INSERT INTO Schulklasse "
//					+ "VALUES (" + schulklasse.getName() + ","
//					+ schulklasse.getJahrgang() + ","
//					+ schulklasse.getKlassenraum().getName() + ");";
//			stmt.executeUpdate(sql);
//			for(int kuerzel : schulklasse.getKlassenlehrer()) {
//				sql = "INSERT INTO klassenlehrer "
//						+ "VALUES (" + schulklasse.getName() + ","
//						+ kuerzel + ");";
//				stmt.executeUpdate(sql);
//			}
//		}catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void addRaum(Room raum) {
		try {
			sql = "INSERT INTO Raum "
					+ "VALUES ('" + raum.getName() + "',"
					+ raum.getGebaeude() + ");";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addRaumfunktion(Raumfunktion rf) {
		try {
			sql = "INSERT INTO Raumfunktion "
					+ "VALUES ('" + rf.getName() + "');";
			stmt.executeUpdate(sql);
			System.out.println(rf.getName()+"add");
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Personal getPersonalByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM Personal WHERE kuerzel = " + pKuerzel + ";";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int sollZeit = rs.getInt("sollZeit");
			int istZeit = rs.getInt("istZeit");
			int ersatzZeit = rs.getInt("ersatzZeit");
			boolean schonGependelt = rs.getBoolean("schonGependelt");
			boolean lehrer = rs.getBoolean("lehrer");
			sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE personal_kuerzel = " + pKuerzel + ";";
			rs = stmt.executeQuery(sql);
			ArrayList<String> moeglicheStundeninhalte = new ArrayList<String>();
			while(rs.next()) {
				moeglicheStundeninhalte.add(rs.getString("stundeninhalt_kuerzel"));
			}
			return new Personal(name, pKuerzel, sollZeit, istZeit, ersatzZeit, schonGependelt, lehrer, moeglicheStundeninhalte);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Raumfunktion> getAllRaumfunktion() {
		try {
			ArrayList<Raumfunktion> rfs = new ArrayList<Raumfunktion>();
			sql = "SELECT * FROM Raumfunktion;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString("name");
				System.out.println(name);
				rfs.add(new Raumfunktion(name));
			}
			return rfs;
		}catch(Exception e) {
			
		}
		return null;
	}
	
	public static void deleteRaumfunktionByName(String name) {
		try {
			sql = "DELETE FROM Raumfunktion WHERE name = " + name;
			stmt.executeUpdate(sql);
		}catch(SQLException e) {
			
		}
	}
	
	public static void close() {
    	try {
    		stmt.close();
    		c.close();
    	}catch (Exception e) {
    		System.out.println("Error on closing.");
    	}
	}
	
	public static void dbRaumLesen() {
		try {
			sql = "SELECT * FROM Raum;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("Name: " + rs.getString("name") + ", Gebäudennr: " + rs.getInt("gebaeudennr"));
			}
		}catch(SQLException e) {}
	}
}
