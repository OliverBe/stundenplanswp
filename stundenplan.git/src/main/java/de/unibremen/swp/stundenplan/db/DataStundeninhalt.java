package de.unibremen.swp.stundenplan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;
import de.unibremen.swp.stundenplan.exceptions.DeleteException;
import de.unibremen.swp.stundenplan.exceptions.NichtVorhandenException;

/**
 * Klasse stellt die Verbindung vom Stundeninhalt zur Datenbank dar.
 * 
 * @author Kim-Long
 *
 */
public class DataStundeninhalt {
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
	private DataStundeninhalt() {}
	
	/**
	 * Fuegt den uebergebenen Stundeninhalt zur Datenbank hinzu.
	 * 
	 * @param stundeninhalt
	 * 		der Stundeninhalt, der hinzugefuegt werden soll
	 */
	public static void addStundeninhalt(Stundeninhalt stundeninhalt) {
		try {
			for(Stundeninhalt si : getAllStundeninhalte()) {
				if(si.getKuerzel().equals(stundeninhalt.getKuerzel())){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "INSERT INTO Stundeninhalt "
					+ "VALUES ('" + stundeninhalt.getName() + "','"
					+ stundeninhalt.getKuerzel() + "',"
					+ stundeninhalt.getRegeldauer() + ","
					+ stundeninhalt.getRhythmustyp() + ");";
			stmt.executeUpdate(sql);
			Data.setSaved(false);
		}catch (SQLException | BereitsVorhandenException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt den Stundeninhalt mit dem gegebenen Kuerzel zurueck.
	 * 
	 * @param pKuerzel
	 * 		der Kuerzel des Stundeninhalts
	 * @return	der Stundeninhalt mit dem gegebenen Kuerzel, falls nicht vorhanden null
	 */
	public static Stundeninhalt getStundeninhaltByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String name = rs.getString("name");
			int regeldauer = rs.getInt("regeldauer");
			int rhythmustyp = rs.getInt("rhythmustyp");
			return new Stundeninhalt(name, pKuerzel, regeldauer, rhythmustyp);
		} catch (SQLException e) {
			return null;
		}
	}
	
	/**
	 * Methode gibt eine Liste aller Stundeninhalte zurueck.
	 * 
	 * @return	eine ArrayList aller Stundeninhalte
	 */
	public static ArrayList<Stundeninhalt> getAllStundeninhalte() {
		ArrayList<Stundeninhalt> allStundeninhalt = new ArrayList<Stundeninhalt>();
		try {
			sql = "SELECT * FROM Stundeninhalt";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				String kuerzel = rs.getString("kuerzel");
				int regeldauer = rs.getInt("regeldauer");
				int rhythmustyp = rs.getInt("rhythmustyp");
				allStundeninhalt.add(new Stundeninhalt(name, kuerzel, regeldauer, rhythmustyp));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allStundeninhalt;
	}
	
	/**
	 * Gibt eine Liste von Kuerzel aller Stundeninhalten zurueck.
	 * 
	 * @return	eine ArrayList von Kuerzeln aller Stundeninhalten
	 */
	public static ArrayList<String> getAllAcronymsFromStundeninhalt(){
		try{ 
			sql = "SELECT kuerzel FROM Stundeninhalt";
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
	 * Methode loescht den Stundeninhalt mit dem gegebenen Kuerzel. 
	 * 
	 * @param pKuerzel
	 * 		der Kuerzel vom Stundeninhalt, der geloescht werden soll
	 */
	public static void deleteStundeninhaltByKuerzel(String pKuerzel) {
		try {
			sql = "SELECT * FROM moegliche_Stundeninhalte_Personal WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				boolean yes = DeleteException.delete("Der Stundeninhalt kann von einem oder mehreren Lehrern ausgeführt werden.\nSoll der Stundeninhalt trotzdem gelöscht werden?");
				if(yes) delete0(pKuerzel);
			}else delete0(pKuerzel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Hilfsmethode fuer das Loeschen eines Stundeninhaltes.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Stundeninhaltes
	 * @throws SQLException
	 */
	private static void delete0(String pKuerzel) throws SQLException {
		sql = "SELECT * FROM Raumfunktion WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			boolean yes = DeleteException.delete("Der Stundeninhalt ist in einer Raumfunktion eingetragen.\nSoll der Stundeninhalt trotzdem gelöscht werden?");
			if (yes) {
				ArrayList<String> rfNames = new ArrayList<String>();
				do {
					String rfName = rs.getString("name");
					rfNames.add(rfName);
				}while (rs.next());
				delete1(pKuerzel, rfNames);
			}
		}else delete1(pKuerzel, new ArrayList<String>());
	}
	
	/**
	 * Hilfsmethode fuer das Loeschen eines Stundeninhaltes.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Stundeninhaltes
	 * @param rfNames
	 * 		ArrayList mit Namen von Raumfunktionen
	 * @throws SQLException
	 */
	private static void delete1(String pKuerzel, ArrayList<String> rfNames) throws SQLException {
		sql = "SELECT * FROM planungseinheit_Stundeninhalt WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			boolean yes = DeleteException.delete("Der Stundeninhalt ist in einer Planungseinheit eingetragen.\nSoll der Stundeninhalt trotzdem gelöscht werden?");
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
		}else {
			deleteSQL(pKuerzel);
		}
	}
	
	/**
	 * Hilfsmethode fuer das Loeschen eines Stundeninhaltes.
	 * 
	 * @param pKuerzel
	 * 		das Kuerzel des Stundeninhaltes
	 * @throws SQLException
	 */
	private static void deleteSQL(String pKuerzel) throws SQLException {
		sql = "DELETE FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM moegliche_Stundeninhalte_Personal WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM Raumfunktion WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM stundenbedarf WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM planungseinheit_Stundeninhalt WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
		sql = "DELETE FROM Jahrgang_Stundenbedarf WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
		stmt.executeUpdate(sql);
	}
	
	/**
	 * Methode bearbeitet den Stundeninhalt mit dem gegebenen Kuerzel.
	 * 
	 * @param pKuerzel
	 * 		der Kuerzel des zu bearbeitenden Stundeninhaltes
	 * @param newStundeninhalt
	 * 		der bearbeitete Stundeninhalt
	 */
	public static void editStundeninhalt(String pKuerzel, Stundeninhalt newStundeninhalt) {
		try {
			if(getStundeninhaltByKuerzel(pKuerzel) == null) throw new NichtVorhandenException();
			for(Stundeninhalt si : getAllStundeninhalte()) {
				if(si.getKuerzel().equals(newStundeninhalt.getKuerzel()) && !si.getKuerzel().equals(pKuerzel)){ 
					throw new BereitsVorhandenException();
				}
			}
			sql = "DELETE FROM Stundeninhalt WHERE kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE moegliche_Stundeninhalte_Personal SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE Raumfunktion SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE stundenbedarf SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE planungseinheit_Stundeninhalt SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			sql = "UPDATE Jahrgang_Stundenbedarf SET stundeninhalt_kuerzel = '" + newStundeninhalt.getKuerzel() + "' WHERE stundeninhalt_kuerzel = '" + pKuerzel + "';";
			stmt.executeUpdate(sql);
			addStundeninhalt(newStundeninhalt);
		} catch (SQLException | BereitsVorhandenException | NichtVorhandenException e) {
			e.printStackTrace();
		}
	}
}
