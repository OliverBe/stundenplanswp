
package de.unibremen.swp.stundenplan.db;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;

/**
 * Leistungstests fuer das Erstellen von einer enormen Anzahl von Daten sowie für das Hinzufügen in die DB
 * 
 * @author Oliver
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataLeistungsTest {

	private long tStart;
	private ArrayList<String> array;
	private HashMap<Weekday, int[]> map;
	private HashMap<String, Integer> map2;
	private Room r;
	private Jahrgang j;
	private Schoolclass sc;
	private Raumfunktion rf;
	private Stundeninhalt si;
	private Personal p;

	@BeforeClass
	public static void startDB() {
		System.out.println("TEST - Leistung");
		Data.start();
		Data.deleteAll();
	}

	@Before
	public void setUp() {
		tStart = System.currentTimeMillis();
		array = new ArrayList<String>();
		map = new HashMap<Weekday, int[]>();
		map2 = new HashMap<String, Integer>();
		j = new Jahrgang(1, new HashMap<String, Integer>());
		si = new Stundeninhalt("Si", "Si", 1, 1);
		rf = new Raumfunktion("Rf", new ArrayList<String>());
		r = new Room("R", 1, new ArrayList<String>());
		p = new Personal("P", "P", 0, 0, 0, false, new ArrayList<String>(),
				new HashMap<Weekday, int[]>());
		sc = new Schoolclass("Sc", 1, r, new ArrayList<String>(),
				new HashMap<String, Integer>());
		DataStundeninhalt.addStundeninhalt(si);
		DataSchulklasse.addJahrgang(j);
		DataRaum.addRaumfunktion(rf);
		DataRaum.addRaum(r);
		DataPersonal.addPersonal(p);
		DataSchulklasse.addSchulklasse(sc);
	}

	@After
	public void tearDown() {
		System.out.println("... done");
		Data.deleteAll();
	}

	@AfterClass
	public static void endDB() {
		File dir = new File(System.getProperty("user.dir"));
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".db") && filename.equals("temp.db");
			}
		});
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].toString());
			if (files[i].equals("temp.db")) {
				Data.close();
			}
		}
	}

	@Test
	public void massiveCreatePersonal() {
		int[] arr = { 1, 2, 3, 4 };
		map.put(Weekday.MONDAY, arr);
		map.put(Weekday.TUESDAY, arr);
		map.put(Weekday.WEDNESDAY, arr);
		map.put(Weekday.THURSDAY, arr);
		map.put(Weekday.FRIDAY, arr);
		map.put(Weekday.SATURDAY, arr);
		map.put(Weekday.SUNDAY, arr);
		// hundert Stdi
		for (int i = 0; i < 100; i++) {
			array.add("Si" + i);
		}
		ArrayList<Personal> list = new ArrayList<Personal>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler < 10000) {
			list.add(new Personal("P", "" + zaehler, 0, 0, 0, false, array, map));
			zaehler++;
		}
		// 10.000 Personal in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveCreateJahrgang() {
		// hundert Bedarfe
		for (int i = 0; i < 100; i++) {
			map2.put("B" + i, i);
		}
		ArrayList<Jahrgang> list = new ArrayList<Jahrgang>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Jahrgang(zaehler, map2));
			zaehler++;
		}
		// 10.000 Jahrgaenge in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveCreatePlanungseinheit() {
		ArrayList<Planungseinheit> list = new ArrayList<Planungseinheit>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Planungseinheit(zaehler, Weekday.MONDAY, zaehler, 0,
					zaehler, 1));
			zaehler++;
		}
		// 10.000 Jahrgaenge in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveCreateRaumfunktion() {
		// hundert Stdi
		for (int i = 0; i < 100; i++) {
			array.add("Si" + i);
		}
		ArrayList<Raumfunktion> list = new ArrayList<Raumfunktion>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Raumfunktion("Rf" + zaehler, array));
			zaehler++;
		}
		// 10.000 Raumfunktionen in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveCreateStundeninhalt() {
		ArrayList<Stundeninhalt> list = new ArrayList<Stundeninhalt>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Stundeninhalt("Si", "si" + zaehler, 1, 1));
			zaehler++;
		}
		// 10.000 Stundeninhalte in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveCreateSchoolclass() {
		ArrayList<String> rfs = new ArrayList<String>();
		// hundert Raumfunktionen fuer Raum
		for (int i = 0; i < 100; i++) {
			array.add("Rf" + i);
		}
		// hundert Personal
		for (int i = 0; i < 100; i++) {
			array.add("P" + i);
		}
		// hundert Bedarfe
		for (int i = 0; i < 100; i++) {
			map2.put("B" + i, i);
		}
		ArrayList<Schoolclass> list = new ArrayList<Schoolclass>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Schoolclass("Sc", 1, new Room("R", 1, rfs), array,
					map2));
			zaehler++;
		}
		// 10.000 Schulklassen in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

	@Test
	public void massiveAddPersonalToDB() {
		int[] arr = { 1, 2, 3, 4 };
		map.put(Weekday.MONDAY, arr);
		map.put(Weekday.TUESDAY, arr);
		map.put(Weekday.WEDNESDAY, arr);
		map.put(Weekday.THURSDAY, arr);
		map.put(Weekday.FRIDAY, arr);
		map.put(Weekday.SATURDAY, arr);
		map.put(Weekday.SUNDAY, arr);
		// zehn Stdi
		for (int i = 0; i < 10; i++) {
			array.add("Si" + i);
		}
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 30) {
			DataPersonal.addPersonal(new Personal("P", "P" + zaehler, 0, 0, 0,
					false, array, map));
			zaehler++;
		}
		// mind 30 Personal in höchstens 30 Sekunden zu DB hinzugefuegt
		assertTrue(zaehler == 30);
	}

	@Test
	public void massiveAddJahrgangToDB() {
		// hundert Bedarfe
		for (int i = 0; i < 10; i++) {
			map2.put("B" + i, i);
		}
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 50) {
			DataSchulklasse.addJahrgang(new Jahrgang(zaehler, map2));
			zaehler++;
		}
		// 50 Jahrgaenge in höchstens 30 Sekunden zu DB hinzugefuegt
		assertTrue(zaehler == 50);
	}

	@Test
	public void massiveAddSchoolclassToDB() {
		ArrayList<String> rfs = new ArrayList<String>();
		// 10 Raumfunktionen fuer Raum
		for (int i = 0; i < 10; i++) {
			array.add("Rf" + i);
		}
		// 20 Personal
		for (int i = 0; i < 20; i++) {
			array.add("P" + i);
		}
		// 20 Bedarfe
		for (int i = 0; i < 20; i++) {
			map2.put("B" + i, i);
		}
		int zaehler = 0;
		Room r1 = new Room("R", 1, rfs);
		DataRaum.addRaum(new Room("R1", 1, rfs));
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 50) {
			DataSchulklasse.addSchulklasse(new Schoolclass("Sc" + zaehler, 1,
					r1, array, map2));
			zaehler++;
		}
		// 50 Schulklassen in höchstens 30 Sekunden erstellt
		assertTrue(zaehler == 50);
	}

	@Test
	public void massiveAddStundeninhaltToDB() {
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 100) {
			DataStundeninhalt.addStundeninhalt(new Stundeninhalt("Si", "si"
					+ zaehler, 1, 1));
			zaehler++;
		}
		// 1000 Schulklassen in höchstens 30 Sekunden erstellt
		assertTrue(zaehler == 100);
	}

	@Test
	public void massiveAddPlanungseinheitToDB() {
		int[] ia = { 1, 2, 3, 4 };
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 100) {
			Planungseinheit Pe = new Planungseinheit(zaehler, Weekday.MONDAY,
					zaehler, 0, zaehler, 1);
			// Personal fuer PE
			for (int i = 0; i < 2; i++) {
				p.setName("p"+zaehler+""+i);
				Pe.addPersonal(p, ia);
			}
			// Room fuer PE
			for (int i = 0; i < 2; i++) {
				r.setName("r"+zaehler+""+i);
				Pe.addRoom(r);
			}
			// Schulklassen fuer PE
			for (int i = 0; i < 2; i++) {
				sc.setName("sc"+zaehler+""+i);
				Pe.addSchulklassen(sc);
			}
			// fuer Stundeninhalte
			for (int i = 0; i < 2; i++) {
				si.setKuerzel("sip"+zaehler+""+i);
				Pe.addStundeninhalt(si);
			}
			DataPlanungseinheit.addPlanungseinheit(Pe);
			zaehler++;
		}
		// 100 Planungseinheiten in höchstens 20 Sekunden erstellt
		assertTrue(zaehler == 100);
	}

	@Test
	public void massiveAddRaumfunktionToDB() {
		// fuenf Stdi
		for (int i = 0; i < 5; i++) {
			array.add("Si" + i);
		}
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 120
				&& zaehler < 50) {
			DataRaum.addRaumfunktion(new Raumfunktion("Rf" + zaehler, array));
			zaehler++;
		}
		// 100 Raumfunktionen in höchstens 30 Sekunden erstellt
		assertTrue(zaehler == 50);
	}
}
