package de.unibremen.swp.stundenplan.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class StundeninhaltManagerTest {
	Stundeninhalt Deutsch, Sport;
	ArrayList<Stundeninhalt> allStundeninhalt = new ArrayList<Stundeninhalt>();
	
	@BeforeClass
	public static void startDB() {
		System.out.println("TEST - StundeninhaltManager");
		Data.start();
		Data.deleteAll();
	}
	
	@Before
	public void createDataForTest(){
		Deutsch = new Stundeninhalt("Deutsch", "Deu", 90, 2);
		Sport = new Stundeninhalt("Sport","Spo", 90, 1);
		allStundeninhalt.add(Deutsch);
		allStundeninhalt.add(Sport);
	}
	
	@After
	public void tearDown() {
		System.out.println("... done");
		Data.deleteAll();
		DataStundeninhalt.deleteStundeninhaltByKuerzel("Deu");
		DataStundeninhalt.deleteStundeninhaltByKuerzel("Spo");
		DataStundeninhalt.deleteStundeninhaltByKuerzel("Mat");
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
	public void testAddStundeninhaltToDb() {
		StundeninhaltManager.addStundeninhaltToDb(Deutsch);
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Deutsch.getKuerzel()), Deutsch);
		StundeninhaltManager.addStundeninhaltToDb(Sport);
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Sport.getKuerzel()), Sport);
	}
	
	@Test
	public void testEditStundeninhalt() {
		StundeninhaltManager.addStundeninhaltToDb(Deutsch);
		Deutsch.setKuerzel("Mat");
		Deutsch.setName("Mathematik");
		StundeninhaltManager.editStundeninhalt("Deu", Deutsch);
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Deutsch.getKuerzel()), Deutsch);
	}
	
	@Test
	public void testGetStundeninhaltByKuerzel() {
		StundeninhaltManager.addStundeninhaltToDb(Deutsch);
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Deutsch.getKuerzel()), Deutsch);
		StundeninhaltManager.addStundeninhaltToDb(Sport);
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Sport.getKuerzel()), Sport);
	}
	
	@Test
	public void testDeleteStundeinhaltFromDb() {
		StundeninhaltManager.addStundeninhaltToDb(Deutsch);
		StundeninhaltManager.deleteStundeninhaltFromDB("Deu");
		assertEquals(StundeninhaltManager.getStundeninhaltByKuerzel(Deutsch.getKuerzel()), null);
	}
	
	@Test
	public void testGetAllStundeninhalteFromDB() {
		StundeninhaltManager.addStundeninhaltToDb(Deutsch);
		StundeninhaltManager.addStundeninhaltToDb(Sport);
		assertEquals(StundeninhaltManager.getAllStundeninhalteFromDB(), allStundeninhalt);
	}
}