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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;

/**
 * Integrationstest fuer DataPersonal mit Personal Bitte beachten, dass
 * Wertabfragen (negative Werte, leere �bergaben etc.) in den GUI-Panels
 * vorgenommen werden und somit hier r�berfl�ssig sind
 * 
 * @author Oliver
 *
 */
public class DataPersonalTest {

	@Before
	public void setUp() {
		Data.start();
		Data.deleteAll();
	}

	@Test
	public void testAddPersonalWithValidInput() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false,
				false, array, map));
		Personal p2 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals("Nobody", p2.getName());
		assertEquals("No", p2.getKuerzel());
		assertEquals(10, p2.getSollZeit());
		assertEquals(0, p2.getIstZeit());
		assertEquals(0, p2.getErsatzZeit());
		assertEquals(false, p2.isGependelt());
		assertEquals(false, p2.isLehrer());
		assertEquals(array, p2.getMoeglicheStundeninhalte());
		assertEquals(map, p2.getWunschzeiten());
	}

	@Test(expected = NullPointerException.class)
	public void testAddPersonalWithNull() {
		DataPersonal.addPersonal(null);
	}

	@Test
	public void testAddPersonalWithValidInputButAlreadyInDB() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false,
				false, array, map));
		Personal p2 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals("No", p2.getKuerzel());
		assertEquals("Nobody", p2.getName());
		DataPersonal.addPersonal(new Personal("Yobody", "No", 10, 0, 0, false,
				false, array, map));
		Personal p3 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals("No", p3.getKuerzel());
		// still Nobody and not Yobody!
		assertEquals("Nobody", p3.getName());
	}

	@Test
	public void testAddPersonalWithToLongKuerzel() {
		// intercepted by GUI-Check-Methode - so not relevant here
	}

	@Test
	public void testDeletePersonalByValidInput() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false,
				false, array, map));
		Personal p2 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals("Nobody", p2.getName());
		assertEquals("No", p2.getKuerzel());
		// Delete from DB
		DataPersonal.deletePersonalByKuerzel("No");
		Personal p3 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p3);
	}

	@Test
	public void testDeletePersonalByNotExistingInput() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
		// Delete from DB
		DataPersonal.deletePersonalByKuerzel("No");
		// already intercepted in DataPersonal
		Personal p3 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p3);
	}

	@Test
	public void testDeletePersonalByNull() {
		// Delete from DB
		DataPersonal.deletePersonalByKuerzel(null);
		// already intercepted in DataPersonal
	}

	@Test
	public void testGetPersonalByValidInput() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false,
				false, array, map));
		Personal p2 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals("Nobody", p2.getName());
		assertEquals("No", p2.getKuerzel());
		// Get from DB
		Personal p3 = DataPersonal.getPersonalByKuerzel("No");
		assertEquals(p2, p3);
	}

	@Test
	public void testGetPersonalByInalidInput() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No");
		assertNull(p1);
	}

	@Test
	public void testGetPersonalByNull() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel(null);
		assertNull(p1);
	}

	@Test
	public void testEditPersonalToOtherPersonalWhichAlreadyHasKuerzel() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No1");
		Personal p2 = DataPersonal.getPersonalByKuerzel("No2");
		Personal p3 = DataPersonal.getPersonalByKuerzel("No3");
		Personal p4 = DataPersonal.getPersonalByKuerzel("No4");
		Personal p5 = DataPersonal.getPersonalByKuerzel("No5");
		assertNull(p1);
		assertNull(p2);
		assertNull(p3);
		assertNull(p4);
		assertNull(p5);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody1", "No1", 1, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody2", "No2", 2, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody3", "No3", 3, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody4", "No4", 4, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody5", "No5", 5, 0, 0, false,
				false, array, map));
		p1 = DataPersonal.getPersonalByKuerzel("No1");
		p2 = DataPersonal.getPersonalByKuerzel("No2");
		p3 = DataPersonal.getPersonalByKuerzel("No3");
		p4 = DataPersonal.getPersonalByKuerzel("No4");
		p5 = DataPersonal.getPersonalByKuerzel("No5");
		assertEquals("Nobody1", p1.getName());
		assertEquals("No1", p1.getKuerzel());
		assertEquals(1, p1.getSollZeit());
		assertEquals(0, p1.getIstZeit());
		assertEquals(0, p1.getErsatzZeit());
		assertEquals(false, p1.isGependelt());
		assertEquals(false, p1.isLehrer());
		assertEquals(array, p1.getMoeglicheStundeninhalte());
		assertEquals(map, p1.getWunschzeiten());
		assertEquals("Nobody2", p2.getName());
		assertEquals("No2", p2.getKuerzel());
		assertEquals(2, p2.getSollZeit());
		assertEquals(0, p2.getIstZeit());
		assertEquals(0, p2.getErsatzZeit());
		assertEquals(false, p2.isGependelt());
		assertEquals(false, p2.isLehrer());
		assertEquals(array, p2.getMoeglicheStundeninhalte());
		assertEquals(map, p2.getWunschzeiten());
		assertEquals("Nobody3", p3.getName());
		assertEquals("No3", p3.getKuerzel());
		assertEquals(3, p3.getSollZeit());
		assertEquals(0, p3.getIstZeit());
		assertEquals(0, p3.getErsatzZeit());
		assertEquals(false, p3.isGependelt());
		assertEquals(false, p3.isLehrer());
		assertEquals(array, p3.getMoeglicheStundeninhalte());
		assertEquals(map, p3.getWunschzeiten());
		assertEquals("Nobody4", p4.getName());
		assertEquals("No4", p4.getKuerzel());
		assertEquals(4, p4.getSollZeit());
		assertEquals(0, p4.getIstZeit());
		assertEquals(0, p4.getErsatzZeit());
		assertEquals(false, p4.isGependelt());
		assertEquals(false, p4.isLehrer());
		assertEquals(array, p4.getMoeglicheStundeninhalte());
		assertEquals(map, p4.getWunschzeiten());
		assertEquals("Nobody5", p5.getName());
		assertEquals("No5", p5.getKuerzel());
		assertEquals(5, p5.getSollZeit());
		assertEquals(0, p5.getIstZeit());
		assertEquals(0, p5.getErsatzZeit());
		assertEquals(false, p5.isGependelt());
		assertEquals(false, p5.isLehrer());
		assertEquals(array, p5.getMoeglicheStundeninhalte());
		assertEquals(map, p5.getWunschzeiten());
		// Edit in DB
		DataPersonal.editPersonal("No1", new Personal("Nobody1", "No1", 1337,
				0, 0, false, false, array, map));
		p1 = DataPersonal.getPersonalByKuerzel("No1");
		assertEquals("Nobody1", p1.getName());
		assertEquals("No1", p1.getKuerzel());
		assertEquals(1337, p1.getSollZeit());
		assertEquals(0, p1.getIstZeit());
		assertEquals(0, p1.getErsatzZeit());
		assertEquals(false, p1.isGependelt());
		assertEquals(false, p1.isLehrer());
		assertEquals(array, p1.getMoeglicheStundeninhalte());
		assertEquals(map, p1.getWunschzeiten());
	}

	@Test
	public void testEditPersonalWithValidChanges() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No1");
		Personal p2 = DataPersonal.getPersonalByKuerzel("No2");
		Personal p3 = DataPersonal.getPersonalByKuerzel("No3");
		Personal p4 = DataPersonal.getPersonalByKuerzel("No4");
		Personal p5 = DataPersonal.getPersonalByKuerzel("No5");
		assertNull(p1);
		assertNull(p2);
		assertNull(p3);
		assertNull(p4);
		assertNull(p5);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody1", "No1", 1, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody2", "No2", 2, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody3", "No3", 3, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody4", "No4", 4, 0, 0, false,
				false, array, map));
		DataPersonal.addPersonal(new Personal("Nobody5", "No5", 5, 0, 0, false,
				false, array, map));
		p1 = DataPersonal.getPersonalByKuerzel("No1");
		p2 = DataPersonal.getPersonalByKuerzel("No2");
		p3 = DataPersonal.getPersonalByKuerzel("No3");
		p4 = DataPersonal.getPersonalByKuerzel("No4");
		p5 = DataPersonal.getPersonalByKuerzel("No5");
		assertEquals("Nobody1", p1.getName());
		assertEquals("No1", p1.getKuerzel());
		assertEquals(1, p1.getSollZeit());
		assertEquals(0, p1.getIstZeit());
		assertEquals(0, p1.getErsatzZeit());
		assertEquals(false, p1.isGependelt());
		assertEquals(false, p1.isLehrer());
		assertEquals(array, p1.getMoeglicheStundeninhalte());
		assertEquals(map, p1.getWunschzeiten());
		assertEquals("Nobody2", p2.getName());
		assertEquals("No2", p2.getKuerzel());
		assertEquals(2, p2.getSollZeit());
		assertEquals(0, p2.getIstZeit());
		assertEquals(0, p2.getErsatzZeit());
		assertEquals(false, p2.isGependelt());
		assertEquals(false, p2.isLehrer());
		assertEquals(array, p2.getMoeglicheStundeninhalte());
		assertEquals(map, p2.getWunschzeiten());
		assertEquals("Nobody3", p3.getName());
		assertEquals("No3", p3.getKuerzel());
		assertEquals(3, p3.getSollZeit());
		assertEquals(0, p3.getIstZeit());
		assertEquals(0, p3.getErsatzZeit());
		assertEquals(false, p3.isGependelt());
		assertEquals(false, p3.isLehrer());
		assertEquals(array, p3.getMoeglicheStundeninhalte());
		assertEquals(map, p3.getWunschzeiten());
		assertEquals("Nobody4", p4.getName());
		assertEquals("No4", p4.getKuerzel());
		assertEquals(4, p4.getSollZeit());
		assertEquals(0, p4.getIstZeit());
		assertEquals(0, p4.getErsatzZeit());
		assertEquals(false, p4.isGependelt());
		assertEquals(false, p4.isLehrer());
		assertEquals(array, p4.getMoeglicheStundeninhalte());
		assertEquals(map, p4.getWunschzeiten());
		assertEquals("Nobody5", p5.getName());
		assertEquals("No5", p5.getKuerzel());
		assertEquals(5, p5.getSollZeit());
		assertEquals(0, p5.getIstZeit());
		assertEquals(0, p5.getErsatzZeit());
		assertEquals(false, p5.isGependelt());
		assertEquals(false, p5.isLehrer());
		assertEquals(array, p5.getMoeglicheStundeninhalte());
		assertEquals(map, p5.getWunschzeiten());
		// Edit in DB
		DataPersonal.editPersonal("No1", new Personal("Nobody1", "No2", 1, 0,
				0, false, false, array, map));
		// still the same, intercepted in DataPersonal
		p1 = DataPersonal.getPersonalByKuerzel("No1");
		assertEquals("Nobody1", p1.getName());
		assertEquals("No1", p1.getKuerzel());
		assertEquals(1, p1.getSollZeit());
		assertEquals(0, p1.getIstZeit());
		assertEquals(0, p1.getErsatzZeit());
		assertEquals(false, p1.isGependelt());
		assertEquals(false, p1.isLehrer());
		assertEquals(array, p1.getMoeglicheStundeninhalte());
		assertEquals(map, p1.getWunschzeiten());
		assertEquals("Nobody2", p2.getName());
		assertEquals("No2", p2.getKuerzel());
		assertEquals(2, p2.getSollZeit());
		assertEquals(0, p2.getIstZeit());
		assertEquals(0, p2.getErsatzZeit());
		assertEquals(false, p2.isGependelt());
		assertEquals(false, p2.isLehrer());
		assertEquals(array, p2.getMoeglicheStundeninhalte());
		assertEquals(map, p2.getWunschzeiten());
	}

	@Test
	public void testEditPersonalToNewKuerzel() {
		// Not in DB
		Personal p1 = DataPersonal.getPersonalByKuerzel("No1");
		Personal p2 = DataPersonal.getPersonalByKuerzel("No2");
		assertNull(p1);
		assertNull(p2);
		// Create in DB
		ArrayList<String> array = new ArrayList<String>();
		HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
		DataPersonal.addPersonal(new Personal("Nobody1", "No1", 1, 0, 0, false,
				false, array, map));
		p1 = DataPersonal.getPersonalByKuerzel("No1");
		assertEquals("Nobody1", p1.getName());
		assertEquals("No1", p1.getKuerzel());
		assertEquals(1, p1.getSollZeit());
		assertEquals(0, p1.getIstZeit());
		assertEquals(0, p1.getErsatzZeit());
		assertEquals(false, p1.isGependelt());
		assertEquals(false, p1.isLehrer());
		assertEquals(array, p1.getMoeglicheStundeninhalte());
		assertEquals(map, p1.getWunschzeiten());
		// Edit in DB
		DataPersonal.editPersonal("No1", new Personal("Nobody2", "No2", 2, 0,
				0, false, false, array, map));
		// still the same, intercepted in DataPersonal
		p2 = DataPersonal.getPersonalByKuerzel("No2");
		assertEquals("Nobody2", p2.getName());
		assertEquals("No2", p2.getKuerzel());
		assertEquals(2, p2.getSollZeit());
		assertEquals(0, p2.getIstZeit());
		assertEquals(0, p2.getErsatzZeit());
		assertEquals(false, p2.isGependelt());
		assertEquals(false, p2.isLehrer());
		assertEquals(array, p2.getMoeglicheStundeninhalte());
		assertEquals(map, p2.getWunschzeiten());
	}
}