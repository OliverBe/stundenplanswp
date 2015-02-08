package de.unibremen.swp.stundenplan.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;

/**
 * Integrationstest fuer DataStundeninhalt mit Personal Bitte beachten, dass
 * Wertabfragen (negative Werte, leere Übergaben etc.) in den GUI-Panels
 * vorgenommen werden und somit hier überflüssig sind
 * 
 * @author Roman
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataStundeninhaltTest {

	@BeforeClass
	public static void startDB() {
		System.out.println("TEST - DataStundeninhalt");
		Data.start();
		Data.deleteAll();
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
	public void testAddStundeninhaltWithValidInput() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals("Philosophie", s2.getName());
		assertEquals("Phil", s2.getKuerzel());
		assertEquals(0, s2.getRegeldauer());
		assertEquals(1, s2.getRhythmustyp());
	}

	@Test(expected = NullPointerException.class)
	public void testAddStundeninhaltWithNull() {
		DataStundeninhalt.addStundeninhalt(null);
	}

	@Test
	public void testAddPersonalWithValidInputButAlreadyInDB() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		s2.setName("Mathematik");
		// Kuerzel bereits vergeben
		DataStundeninhalt.addStundeninhalt(s2);
		Stundeninhalt s3 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals("Phil", s3.getKuerzel());
		// still Nobody and not Yobody!
		assertEquals("Philosophie", s3.getName());
	}

	@Test
	public void testAddStundeninhaltWithToLongKuerzel() {
		// intercepted by GUI-Check-Methode - so not relevant here
	}

	@Test
	public void testDeleteStundeninhaltByValidInput() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		// Delete from DB
		DataStundeninhalt.deleteStundeninhaltByKuerzel("Phil");
		Personal p3 = DataPersonal.getPersonalByKuerzel("Phil");
		assertNull(p3);
	}

	@Test
	public void testDeleteStundeninhaltByNotExistingInput() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
		// Delete from DB
		DataStundeninhalt.deleteStundeninhaltByKuerzel("Phil");
		// already intercepted in DataPersonal
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s2);
	}

	@Test
	public void testDeleteStundeninhaltByNull() {
		DataStundeninhalt.deleteStundeninhaltByKuerzel(null);
		// already intercepted in DataStundeninhalt
	}

	@Test
	public void testGetStundeninhaltByValidInput() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals("Philosophie", s2.getName());
		assertEquals("Phil", s2.getKuerzel());
		Stundeninhalt s3 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals(s2, s3);
	}

	@Test
	public void testGetStundeninhaltByInvalidInput() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertNull(s1);
	}

	@Test
	public void testGetStundeninhaltByNull() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel(null);
		assertNull(s1);
	}

	@Test
	public void testEditPersonalWithValidChange() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Si1");
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Si2");
		assertNull(s1);
		assertNull(s2);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		s2 = new Stundeninhalt("Mathematik", "Ma", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		DataStundeninhalt.addStundeninhalt(s2);
		Stundeninhalt s3 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		Stundeninhalt s4 = DataStundeninhalt.getStundeninhaltByKuerzel("Ma");
		assertEquals("Phil", s3.getKuerzel());
		assertEquals("Ma", s4.getKuerzel());
		// Edit in DB
		DataStundeninhalt.editStundeninhalt("Phil", new Stundeninhalt("Neu",
				"Phil", 0, 1));
		Stundeninhalt s5 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals("Neu", s5.getName());
	}

	@Test
	public void testEditStundeninhaltToOtherStundeninhaltWhichAlreadyHasKuerzel() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Si1");
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Si2");
		assertNull(s1);
		assertNull(s2);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		s2 = new Stundeninhalt("Mathematik", "Ma", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		DataStundeninhalt.addStundeninhalt(s2);
		Stundeninhalt s3 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		Stundeninhalt s4 = DataStundeninhalt.getStundeninhaltByKuerzel("Ma");
		assertEquals("Phil", s3.getKuerzel());
		assertEquals("Ma", s4.getKuerzel());
		// Edit in DB
		DataStundeninhalt.editStundeninhalt("Phil", new Stundeninhalt("Neu",
				"Ma", 0, 1));
		// still the same, intercepted in DataStundeninhalt
		Stundeninhalt s5 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		assertEquals("Phil", s5.getKuerzel());
	}

	@Test
	public void testEditStundeninhaltToNewKuerzel() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Si1");
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Si2");
		assertNull(s1);
		assertNull(s2);
		// Create in DB
		s1 = new Stundeninhalt("Philosophie", "Phil", 0, 1);
		s2 = new Stundeninhalt("Mathematik", "Ma", 0, 1);
		DataStundeninhalt.addStundeninhalt(s1);
		DataStundeninhalt.addStundeninhalt(s2);
		Stundeninhalt s3 = DataStundeninhalt.getStundeninhaltByKuerzel("Phil");
		Stundeninhalt s4 = DataStundeninhalt.getStundeninhaltByKuerzel("Ma");
		assertEquals("Phil", s3.getKuerzel());
		assertEquals("Ma", s4.getKuerzel());
		// Edit in DB
		DataStundeninhalt.editStundeninhalt("Phil", new Stundeninhalt("Neu",
				"Kill", 0, 1));
		Stundeninhalt s5 = DataStundeninhalt.getStundeninhaltByKuerzel("Kill");
		assertEquals("Kill", s5.getKuerzel());
	}

	@Test
	public void testEditStundeninhaltThatDoesNotExist() {
		// Not in DB
		Stundeninhalt s1 = DataStundeninhalt.getStundeninhaltByKuerzel("Si1");
		assertNull(s1);
		// Edit in DB
		DataStundeninhalt.editStundeninhalt("Phil", new Stundeninhalt("Neu",
				"Kill", 0, 1));
		Stundeninhalt s2 = DataStundeninhalt.getStundeninhaltByKuerzel("Kill");
		assertNull(s2);
	}
}
