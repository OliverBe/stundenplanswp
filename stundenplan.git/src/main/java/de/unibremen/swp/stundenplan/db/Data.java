package de.unibremen.swp.stundenplan.db;

import java.sql.*;
import java.util.Collection;

import javax.management.Query;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Personal;

public class Data {
	public final static int MAX_ACRONYM_LEN = 3;
	public final static int MAX_NORMAL_STRING_LEN = 20;
	
	public static void main( String args[] ) {
	    Connection c = null;
	    Statement stmt = null;
	    String sql;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
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
	
	/**
	 * Fügt Personal zu DB hinzu.
	 * @param person
	 * 		Person die hinzugefügt werden soll.
	 */
	public static void addPersonal(final Personal person){
		try {
		Connection c = null;
		Statement stmt = null;
	    String sql;
	    c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
	    
			stmt = c.createStatement();
			sql = "INSERT INTO Personal VALUES("
					+person.getId()+","
					+person.getName()+","
					+person.getAcronym()+","
					+person.getSollZeit()+","
					+person.getIstZeit()+","
					+person.getErsatzZeit()+","
					+person.isGependelt()+","
					+person.isLehrer();
				stmt.executeUpdate(sql);
    	
	    }catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
	
	/**
	 * Gibt Person mit gesuchtem Acronym zurück
	 * @param pAcro
	 * 			Acronym nach dem gesucht werden soll.
	 * @return
	 * 	Person, die Acronym besitzt oder {@code null}, wenn nicht gefunden
	 */
	public static Personal getPersonalByAcronym(final String pAcro){
		
		try {
			Connection c = null;
			Statement stmt = null;
		    String sql;
		    c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
		    
		    stmt = c.createStatement();
		    sql = "SELECT * FROM Personal where kuerzel= "+pAcro;
		    ResultSet result = stmt.executeQuery(sql);
		    
		    if(result.next()){
			    Personal personal = new Personal();
			    personal.setId(result.getInt(1));
			    personal.setName(result.getString(2));
			    personal.setAcronym(result.getString(3));
			    personal.setSollZeit(result.getInt(4));
			    personal.setIstZeit(result.getInt(5));
			    personal.setErsatzZeit(result.getInt(6));
			    personal.setGependelt(result.getBoolean(7));
			    personal.setLehrer(result.getBoolean(8));
			    
			    return personal;
		    }
		    
		}catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
		return null;
	}
}
