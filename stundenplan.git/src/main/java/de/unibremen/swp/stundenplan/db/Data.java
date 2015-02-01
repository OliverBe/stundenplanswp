package de.unibremen.swp.stundenplan.db;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;

public class Data {
	public final static int MAX_KUERZEL_LEN = 4;
	public final static int MAX_NORMAL_STRING_LEN = 30;
	private static Connection c = null;
    protected static Statement stmt = null;
    private static String sql;
    private static boolean saved = true;
    private static String lastRestoredFileName = "";
    
	public static void start() {
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:temp.db");
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
	
	public static void backup(String backupName, boolean forceSave) {
		try {
			File dir = new File(System.getProperty("user.dir"));
			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".db");
				}
			});
			for (File file : files) {
				if((backupName + ".db").equals(file.getName())) {
					int result = JOptionPane.YES_OPTION;
					if(!forceSave) result = JOptionPane.showConfirmDialog(Stundenplan.getMain(), "Die Datei existiert bereits.\nSoll die Datei überschrieben werden?", "Warnung", JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						if(file.delete()) {
							stmt.executeUpdate("backup to " + backupName + ".db");
							System.out.println("DB - database replaced");
						}else System.out.println("DB - ERROR on deleting database");
					}
					return;
				}
			}
			stmt.executeUpdate("backup to " + backupName + ".db");
			System.out.println("DB - backup created");
		}catch (Exception e) {
			System.out.println("DB - ERROR on creating backup");
		}
	}
	
	public static void restore(String backupName) {
		try {
			stmt.executeUpdate("restore from " + backupName);
			setLastRestoredFileName(backupName.substring(0, backupName.length()-3));
			setSaved(true);
			System.out.println("DB - successful restored");
		}catch (Exception e) {
			System.out.println("DB - ERROR on restoring from backup");
		}
	}
	
	public static boolean isSaved() {
		return saved;
	}
	
	public static void setSaved(boolean pSaved) {
		saved = pSaved;
	}

	public static String getLastRestoredFileName() {
		return lastRestoredFileName;
	}

	public static void setLastRestoredFileName(String lastRestoredFileName) {
		Data.lastRestoredFileName = lastRestoredFileName;
	}
	
	public static void deleteAll() {
		try {
			sql = "DELETE FROM Personal;";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Planungseinheit;";
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
