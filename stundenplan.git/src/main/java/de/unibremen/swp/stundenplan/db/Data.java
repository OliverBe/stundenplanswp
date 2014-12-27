package de.unibremen.swp.stundenplan.db;

import java.sql.*;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Personal;

public class Data {
	public final static int MAX_ACRONYM_LEN = 3;
	public final static int MAX_NORMAL_STRING_LEN = 20;
//	private Connection c = null;
//    private Statement stmt = null;
//    private String sql;
	
	public static void main(String[] args) {
		Connection c = null;
	    Statement stmt = null;
	    String sql;
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:" + Config.DATABASE_UNIT_NAME_DEFAULT + ".db");
		    System.out.println("Opened database successfully");
		    
	    	stmt = c.createStatement();
//	    	stmt.executeUpdate("DROP TABLE moegliche_Stundeninhalte_Raum");
	    	//Personal
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
	    	
	    	//Planungseinheit
	    	sql = "CREATE TABLE IF NOT EXISTS Planungseinheit "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startTimeslot INT NOT NULL, "
	    			+ "endTimeslot INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Schulklasse
	    	sql = "CREATE TABLE IF NOT EXISTS Schulklasse "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "jahrgang INT NOT NULL, "
	    			+ "klassenraumId INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Raum
	    	sql = "CREATE TABLE IF NOT EXISTS Raum "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "gebaeudennr INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Stundeninhalt
	    	sql = "CREATE TABLE IF NOT EXISTS Stundeninhalt "
	    			+ "(id INT PRIMARY KEY NOT NULL, "
	    			+ "name VARCHAR NOT NULL, "
	    			+ "kuerzel VARCHAR NOT NULL, "
	    			+ "regeldauer INT NOT NULL, "
	    			+ "rhythmustyp INT NOT NULL)";
	    	stmt.executeUpdate(sql);
	    	
	    	//Map von Personal
	    	sql = "CREATE TABLE IF NOT EXISTS Zeitwunsch "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "weekday INT NOT NULL, "
	    			+ "startZeit INT NOT NULL, "
	    			+ "endZeit INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_id, weekday)"
	    			+ "FOREIGN KEY (personal_id) REFERENCES Personal(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Personal(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS moegliche_Stundeninhalte_Personal "
	    			+ "(personal_id INT NOT NULL, "
	    			+ "stundeninhalt_id INT NOT NULL, "
	    			+ "PRIMARY KEY (personal_id, stundeninhalte_id), "
	    			+ "FOREIGN KEY (personal_id) REFERENCES Personal(id), "
	    			+ "FOREIGN KEY (stundeninhalte_id) REFERENCES Stundeninhalte(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Personal)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Personal "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "personal_id INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, personal_id), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Schulklasse(Personal)
	    	sql = "CREATE TABLE IF NOT EXISTS klassenlehrer "
	    			+ "(schulklasse_id INT NOT NULL, "
	    			+ "personal_id INT NOT NULL, "
	    			+ "PRIMARY KEY (schulklasse_id, personal_id), "
	    			+ "FOREIGN KEY (schulklasse_id) REFERENCES Schulklasse(id), "
	    			+ "FOREIGN KEY (personal_id) REFERENCES Personal(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Stundeninhalt "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "stundeninhalt_id INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, stundeninhalt_id), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (stundeninhalt_id) REFERENCES Stundeninhalt(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Raum(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS moegliche_Stundeninhalte_Raum "
	    			+ "(raum_id INT NOT NULL, "
	    			+ "stundeninhalt_id INT NOT NULL, "
	    			+ "PRIMARY KEY (raum_id, stundeninhalt_id), "
	    			+ "FOREIGN KEY (raum_id) REFERENCES Raum(id), "
	    			+ "FOREIGN KEY (stundeninhalt_id) REFERENCES Stundeninhalt(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Schulklasse(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS stundenbedarf "
	    			+ "(schulklasse_id INT NOT NULL, "
	    			+ "stundeninhalt_id INT NOT NULL, "
	    			+ "bedarf INT NOT NULL, "
	    			+ "PRIMARY KEY (schulklasse_id, stundeninhalt_id), "
	    			+ "FOREIGN KEY (schulklasse_id) REFERENCES Schulklasse(id), "
	    			+ "FOREIGN KEY (stundeninhalt_id) REFERENCES Stundeninhalt(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Jahrgang(Stundeninhalt)
	    	sql = "CREATE TABLE IF NOT EXISTS Jahrgang_Stundenbedarf "
	    			+ "(jahrgang_id INT NOT NULL, "
	    			+ "stundeninhalt_id INT NOT NULL, "
	    			+ "bedarf INT NOT NULL, "
	    			+ "PRIMARY KEY (jahrgang_id, stundeninhalt_id), "
	    			+ "FOREIGN KEY (stundeninhalt_id) REFERENCES Stundeninhalt(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Schulklasse)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Schulklasse "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "schulklasse_id INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, schulklasse_id), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (schulklasse_id) REFERENCES Schulklasse(id))";
	    	stmt.executeUpdate(sql);
	    	
	    	//ArrayList von Planungseinheit(Raum)
	    	sql = "CREATE TABLE IF NOT EXISTS planungseinheit_Raum "
	    			+ "(planungseinheit_id INT NOT NULL, "
	    			+ "raum_id INT NOT NULL, "
	    			+ "PRIMARY KEY (planungseinheit_id, raum_id), "
	    			+ "FOREIGN KEY (planungseinheit_id) REFERENCES Planungseinheit(id), "
	    			+ "FOREIGN KEY (raum_id) REFERENCES Raum(id))";
	    	stmt.executeUpdate(sql);
	    	System.out.println("Tables created.");
	    	stmt.close();
    		c.close();
	    }catch ( Exception e ) {
	    	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    	System.exit(0);
	    }
	}
	
/*	
	public void addPersonal(Personal personal) {
		sql = "INSERT INTO Personal "
				+ "VALUES (" + personal.getId() + ","
				+ personal.getName() + ","
				+ personal.getAcronym() + ","
				+ personal.getSollZeit() + ","
				+ personal.getIstZeit() + ","
				+ personal.getErsatzZeit() + ","
				+ toString(personal.getGependelt()) + ","
				+ toString(personal.getLehrer()) + ");";
		stmt.executeUpdate(sql);
		for(Int id : personal.getMoeglicheStundeninhalte()) {
			sql = "INSERT INTO moegliche_Stundeninhalte_Personal "
					+ "VALUES (" + personal.getId() + ","
					+ id + ");";
		}
	}
	
	public void addPlanungseinheit(Planungseinheit planungseinheit) {
		sql = "INSERT INTO Planungseinheit "
				+ "VALUES (" + planungseinheit.getId() + ","
				+ planungseinheit.getWeekday() + ","
				+ planungseinheit.getStartTimeslot().getId() + ","
				+ planungseinheit.getEndTimeslot().getId() + ");";
		stmt.executeUpdate(sql);
		for(Int id : planungseinheit.getStundeninhalte()) {
			sql = "INSERT INTO planungseinheit_Stundeninhalt "
					+ "VAUES (" + planungseinheit.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
		for(Int id : planungseinheit.getKlassen()) {
			sql = "INSERT INTO planungseinheit_Schulklasse "
					+ "VAUES (" + planungseinheit.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
		for(Int id : planungseinheit.getRaeume()) {
			sql = "INSERT INTO planungseinheit_Raum "
					+ "VAUES (" + planungseinheit.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
		for(Int id : planungseinheit.getPersonal()) {
			sql = "INSERT INTO planungseinheit_Personal "
					+ "VAUES (" + planungseinheit.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
	}
	
	public void addStundeninhalt(Stundeninhalt stundeninhalt) {
		sql = "INSERT INTO Stundeninhalt "
				+ "VALUES (" + stundeninhalt.getId() + ","
				+ stundeninhalt.getName() + ","
				+ stundeninhalt.getKuerzel() + ","
				+ stundeninhalt.getRegeldauer() + ","
				+ stundeninhalt.getRhythmustyp() + ");";
		stmt.executeUpdate(sql);
	}
	
	public void addSchulklasse(Schulklasse schulklasse) {
		sql = "INSERT INTO Schulklasse "
				+ "VALUES (" + schulklasse.getId() + ","
				+ schulklasse.getJahrgang() + ","
				+ schulklasse.getKlassenraum().getId() + ");";
		stmt.executeUpdate(sql);
		for(Int id : schulklasse.getKlassenlehrer()) {
			sql = "INSERT INTO klassenlehrer "
					+ "VALUES (" + schulklasse.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
	}
	
	public void addRaum(Raum raum) {
		sql = "INSERT INTO Raum "
				+ "VALUES (" + raum.getId() + ","
				+ raum.getName() + ","
				+ raum.getKuerzel() + ","
				+ raum.getGebaeudennr() + ");";
		stmt.executeUpdate();
		for(Int id : raum.getMoeglicheStundeninhalte()) {
			sql = "INSERT INTO moegliche_Stundeninhalte_Raum "
					+ "VALUES (" + raum.getId() + ","
					+ id + ");";
			stmt.executeUpdate(sql);
		}
	}
	
	public Personal getPersonalById(int pId) {
		sql = "SELECT * Personal WHERE id = " + pId;
		ResultSet rs = stmt.executeQuery(sql);
		try {
			rs.next();
			int id = rs.getInt("id");
			String name = rs.getString("name");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Personal(id, name, kuerzel, sollZeit, istZeit, ersatzZeit, )
	}
	
	public void close() {
    	try {
    		stmt.close();
    		c.close();
    	}catch (Exception e) {
    		System.out.println("Error on closing.");
    	}
	}
*/
}
