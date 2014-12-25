package de.unibremen.swp.stundenplan.db;

import java.sql.*;

public class Data {
	public final static int MAX_ACRONYM_LEN = 3;
	public final static int MAX_NORMAL_STRING_LEN = 20;
	
	public static void main( String args[] ) {
	    Connection c = null;
	    Statement stmt = null;
	    String sql;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:sp.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
	    	stmt = c.createStatement();
	    	sql = "CREATE TABLE IF NOT EXISTS Personal "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "sollZeit INT NOT NULL, "
	    			+ "istZeit INT NOT NULL, "
	    			+ "ersatzZeit INT NOT NULL, "
	    			+ "schonGependelt BOOLEAN NOT NULL, "
	    			+ "lehrer BOOLEAN NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS Planungseinheit "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startTimeslot INT NOT NULL, "
	    			+ "endTimeslot INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS Schulklasse "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "jahrgang INT NOT NULL, "
	    			+ "klassenraumId INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS Raum "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "gebaeudennr INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS Stundeninhalt "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "regeldauer INT NOT NULL, "
	    			+ "rhythmustyp INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS Zeitwunsch "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startZeit INT NOT NULL, "
	    			+ "endZeit INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_id, weekday)"
	    			+ "FOREIGN KEY (personal_id) REFERENCES Personal(id))";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS moegliche_Stundeninhalte_Personal "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "stundeninhalte_id INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_id, stundeninhalte_id), "
	    			+ "FOREIGN KEY (personal_id) REFERENCES Personal(id), "
	    			+ "FOREIGN KEY (stundeninhalte_id) REFERENCES Stundeninhalte(id))";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Personal "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "personal_id INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, personal_id), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id))";
	    	stmt.executeUpdate(sql);
//	    	sql = "CREATE TABLE IF NOT EXISTS klassenlehrer "
//	    			+ "(schulklasse_id INT NOT NULL, "
//	    			+ "personal_id INT NOT NULL, "
//	    			+ "PRIMARY KEY (schulklasse_id, personal"
	    	stmt.close();
	    	c.commit();
	    	c.close();
	    }catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
}
