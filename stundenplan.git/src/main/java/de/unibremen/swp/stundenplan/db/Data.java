package de.unibremen.swp.stundenplan.db;

import java.sql.*;

import de.unibremen.swp.stundenplan.config.*;
public class Data {
	public final static int MAX_KUERZEL_LEN = 3;
	public final static int MAX_NORMAL_STRING_LEN = 20;
	private static Connection c = null;
    protected static Statement stmt = null;
    private static String sql;
    
	public static void start() {
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
		    System.out.println("DB - Opened database successfully");
		    
	    	stmt = c.createStatement();
	    	
	    	//Personal
	    	sql = "CREATE TABLE IF NOT EXISTS Personal "
	    			+ "(name VARCHAR(" + MAX_NORMAL_STRING_LEN + ") NOT NULL, "
	    			+ "kuerzel VARCHAR(" + MAX_KUERZEL_LEN + ") NOT NULL, "
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
	    			+ "startHour INT NOT NULL, "
	    			+ "startMin INT NOT NULL, "
	    			+ "endHour INT NOT NULL, "
	    			+ "endMin INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Schulklasse
	    	sql = "CREATE TABLE IF NOT EXISTS Schulklasse "
	    			+ "(name VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "jahrgang INT NOT NULL, "
	    			+ "klassenraumName INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Raum
	    	sql = "CREATE TABLE IF NOT EXISTS Raum "
	    			+ "(name VARCHAR PRIMARY KEY NOT NULL,"
	    			+ "gebaeudennr INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Stundeninhalt
	    	sql = "CREATE TABLE IF NOT EXISTS Stundeninhalt "
	    			+ "(name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR PRIMARY KEY NOT NULL, "
	    			+ "regeldauer INT NOT NULL, "
	    			+ "rhythmustyp INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Raumfunktion
	    	sql = "CREATE TABLE IF NOT EXISTS Raumfunktion "
	    			+ "(name VARCHAR NOT NULL, "
	    			+ "stundeninhalt_kuerzel VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (name, stundeninhalt_kuerzel), "
	    			+ "FOREIGN KEY (stundeninhalt_kuerzel) REFERENCES Personal(kuerzel))";
	    	stmt.executeUpdate(sql);
	    	
	    	//Map von Personal
	    	sql = "CREATE TABLE IF NOT EXISTS Zeitwunsch "
	    			+ "(personal_kuerzel VARCHAR NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startHour INT NOT NULL, "
	    			+ "startMin INT NOT NULL, "
	    			+ "endHour INT NOT NULL, "
	    			+ "endMin INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_kuerzel, weekday), "
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
	    			+ "startZeitHour INT NOT NULL, "
	    			+ "startZeitMin INT NOT NULL, "
	    			+ "endZeitHour INT NOT NULL, "
	    			+ "endZeitMin INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, personal_kuerzel), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
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
	    	
	    	//ArrayList von Raum(Raumfunktion)
	    	sql = "CREATE TABLE IF NOT EXISTS raum_Raumfunktion "
	    			+ "(raum_name VARCHAR NOT NULL, "
	    			+ "raumfunktion_name VARCHAR NOT NULL, "
	    			+ "PRIMARY KEY (raum_name, raumfunktion_name), "
	    			+ "FOREIGN KEY (raum_name) REFERENCES Raum(name), "
	    			+ "FOREIGN KEY (raumfunktion_name) REFERENCES Raumfunktion(name))";
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
	    	System.out.println("DB - Tables created");
	    }catch ( Exception e ) {
	    	System.out.println("DB - ERROR on creating tables");
	    }
	}
		
	public static void close() {
		try {
			stmt.close();
			c.close();
		}catch (Exception e) {
			System.out.println("DB - ERROR on closing Database");
		}
	}
	
	public static void backup(String backupDate) {
		try {
			System.out.println(backupDate);
			stmt.executeUpdate("backup to backup_" + backupDate + ".db");
			System.out.println("DB - backup created");
		}catch (Exception e) {
			System.out.println("DB - ERROR on creating backup");
		}
	}
	
	public static void restore(String backupName) {
		try {
			stmt.executeUpdate("restore from " + backupName);
			System.out.println("DB - successful restored");
		}catch (Exception e) {
			System.out.println("DB - ERROR on restoring from backup");
		}
	}
}
