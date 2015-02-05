/*
 * Copyright 2014 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.unibremen.swp.stundenplan.db;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;

/**
 * Integrationstest fuer DataPersonal mit Personal
 * 
 * @author Oliver
 *
 */
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

	@Before
	public void setUp() {
		Data.start();
		tStart = System.currentTimeMillis();		
		array = new ArrayList<String>();
		map = new HashMap<Weekday, int[]>();
		map2 = new HashMap<String, Integer>();
		r = new Room("R",1,new ArrayList<String>());
		j = new Jahrgang(1,new HashMap<String,Integer>());
		sc = new Schoolclass("Sc", 1, r, new ArrayList<String>(), new HashMap<String,Integer>(), new HashMap<Weekday,Boolean>());
		rf = new Raumfunktion("Rf", new ArrayList<String>());
		si = new Stundeninhalt("Si", "Si", 1, 1);
		p = new Personal("P", "P", 0, 0, 0,new HashMap<Weekday,Boolean>(), false, new ArrayList<String>(), new HashMap<Weekday,int[]>());
		System.out.println("--START--");
		DataStundeninhalt.addStundeninhalt(si);
		DataSchulklasse.addJahrgang(j);
		DataRaum.addRaumfunktion(rf);	
		DataRaum.addRaum(r);
		DataPersonal.addPersonal(p);	
		DataSchulklasse.addSchulklasse(sc);
	}
	
	@After
    public void tearDown() {
		Data.close();
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
			list.add(new Personal("P", "" + zaehler, 0, 0, 0, new HashMap<Weekday,Boolean>(), false,
					array, map));
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
	public void massiveCreateRaumfunktion() {
		// hundert Stdi
		for (int i = 0; i < 100; i++) {
			array.add("Si" + i);
		}
		ArrayList<Raumfunktion> list = new ArrayList<Raumfunktion>();
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 20
				&& zaehler <= 10000) {
			list.add(new Raumfunktion("Rf"+zaehler, array));
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
			list.add(new Stundeninhalt("Si", "si"+zaehler,1,1));
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
					map2,new HashMap<Weekday,Boolean>()));
			zaehler++;
		}
		// 10.000 Schulklassen in höchstens 20 Sekunden erstellt
		assertTrue(zaehler > 9999);
	}

//	@Test
//	public void massiveAddPersonalToDB() {
//		int[] arr = { 1, 2, 3, 4 };
//		map.put(Weekday.MONDAY, arr);
//		map.put(Weekday.TUESDAY, arr);
//		map.put(Weekday.WEDNESDAY, arr);
//		map.put(Weekday.THURSDAY, arr);
//		map.put(Weekday.FRIDAY, arr);
//		map.put(Weekday.SATURDAY, arr);
//		map.put(Weekday.SUNDAY, arr);
//		// zehn Stdi
//		for (int i = 0; i < 10; i++) {
//			array.add("Si" + i);
//		}	
//		int zaehler = 0;
//		DataPersonal.addPersonal(new Personal("P", "P" + zaehler, 0, 0, 0,
//				false, false, array, map));
////		while ((System.currentTimeMillis() - tStart) / 1000 < 30
////				&& zaehler < 50) {
////			DataPersonal.addPersonal(new Personal("P", "P" + zaehler, 0, 0, 0,
////					false, false, array, map));
////			zaehler++;
////		}
//		// mind 50 Personal in höchstens 30 Sekunden zu DB hinzugefuegt
//		assertTrue(zaehler == 50);
//	}
	
	@Test
	public void massiveAddJahrgangToDB() {
		// hundert Bedarfe
		for (int i = 0; i < 10; i++) {
			map2.put("B" + i, i);
		}
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 30
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
		DataSchulklasse.addSchulklasse(new Schoolclass("Sc"+zaehler, 1, r1, array,
				map2,new HashMap<Weekday,Boolean>()));
		while ((System.currentTimeMillis() - tStart) / 1000 < 30
				&& zaehler < 50) {
			DataSchulklasse.addSchulklasse(new Schoolclass("Sc"+zaehler, 1, r, array,
					map2,new HashMap<Weekday,Boolean>()));
			zaehler++;
		}
		// 50 Schulklassen in höchstens 30 Sekunden erstellt
		assertTrue(zaehler == 50);
	}
	
	@Test
	public void massiveAddStundeninhaltToDB() {
		int zaehler = 0;
		while ((System.currentTimeMillis() - tStart) / 1000 < 30
				&& zaehler < 100) {
			DataStundeninhalt.addStundeninhalt(new Stundeninhalt("Si", "si"+zaehler,1,1));
			zaehler++;
		}
		// 1000 Schulklassen in höchstens 30 Sekunden erstellt
		assertTrue(zaehler == 100);
//		Data.close();
	}
}
