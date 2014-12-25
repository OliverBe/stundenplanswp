package de.unibremen.swp.stundenplan.db;

import java.sql.*;

public class Datenbank {
	public static void main( String args[] ) {
	    Connection c = null;
	    Statement stmt = null;
	    String sql;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    	c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
	    	stmt = c.createStatement();
	    	sql = "CREATE TABLE Personal IF NOT EXISTS "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "sollZeit INT NOT NULL, "
	    			+ "istZeit INT NOT NULL, "
	    			+ "ersatzZeit INT NOT NULL, "
	    			+ "schonGependelt BOOLEAN NOT NULL, "
	    			+ "lehrer BOOLEAN NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE Planungseinheit IF NOT EXISTS "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startTimeslot INT NOT NULL, "
	    			+ "endTimeslot INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE Schulklasse IF NOT EXISTS "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "jahrgang INT NOT NULL, "
	    			+ "klassenraumId INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE Raum IF NOT EXISTS "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "gebaeudennr INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE Stundeninhalt IF NOT EXISTS "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "regeldauer INT NOT NULL, "
	    			+ "rhythmustyp INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE Zeitwunsch IF NOT EXISTS "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startZeit INT NOT NULL, "
	    			+ "endZeit INT NOT NULL"
	    			+ "PRIMARY KEY (personal_id, weekday)"
	    			+ "FOREIGN KEY (persoanl_id) REFERENCES Personal(id))";
	    	stmt.executeUpdate(sql);
	    	sql = "CREATE TABLE moegliche_Stundeninhalte_Personal IF NOT EXISTS "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "stundeninhalte_id INT NOT NULL, "
	    			+ ""
	    	stmt.close();
	    	c.commit();
	    	c.close();
	    }catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
}
