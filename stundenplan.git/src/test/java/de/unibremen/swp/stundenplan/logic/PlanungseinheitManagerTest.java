package de.unibremen.swp.stundenplan.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.gui.Timeslot;

/**
 * In diesem Test wird der Fall simuliert das es eine erste, zweiteund  dritte Klasse gibts mit den Faechern
 * Deutsch, Mathe, Kunst und Sport gibt, welche alle unterschiedlich Verteilt sind, und nun die vierte Klasse verplannt werden soll
 * und damit werden dann alle Klassen Grenz, Black und WhiteBox getestet.
 * @author Paddy
 *
 */

public class PlanungseinheitManagerTest {

	
	HashMap<Weekday, int[]> pWunschzeiten = new HashMap<Weekday,int[]>();
	HashMap<Weekday, int[]> pWunschzeitenP1 = new HashMap<Weekday,int[]>();
	HashMap<Weekday, int[]> pWunschzeitenP2 = new HashMap<Weekday,int[]>();
	HashMap<Weekday, int[]> pWunschzeitenP3 = new HashMap<Weekday,int[]>();
	HashMap<Weekday, int[]> sportLehrerWunschzeiten = new HashMap<Weekday,int[]>();
	HashMap<Weekday, Boolean> gependelt = new HashMap<Weekday, Boolean>();
	ArrayList<String> stundenInhalteP1 = new ArrayList<>();
	ArrayList<String> stundenInhalteP2 = new ArrayList<>();
	ArrayList<String> stundenInhalteP3 = new ArrayList<>();
	ArrayList<String> stundenInhalteP4 = new ArrayList<>();
	ArrayList<String> stundenInhalteP5 = new ArrayList<>();
	ArrayList<String> sportInhalte = new ArrayList<>();
	ArrayList<String> leereInhalte = new ArrayList<>();
	ArrayList<String> klassenlehrerKlasse1 = new ArrayList<>();
	ArrayList<String> klassenlehrerKlasse2 = new ArrayList<>();
	ArrayList<String> klassenlehrerKlasse3 = new ArrayList<>();
	ArrayList<String> klassenlehrerKlasse4 = new ArrayList<>();
	
	
	ArrayList<String> moeglicheFunktion = new ArrayList<>();
	ArrayList<String> moeglicheFunktionSport = new ArrayList<>();
	Personal p1,p2,p3,p4,p5;
	Personal test = new Personal("Test", "TT1", 35, 0, 30, true, stundenInhalteP1, pWunschzeiten);
	Planungseinheit klasse1Deutsch,klasse1Mathe, klasse1Kunst,klasse1Sport;
	Planungseinheit klasse2Deutsch, klasse2Mathe,klasse2Kunst, klasse2Sport;
	Planungseinheit klasse3Deutsch, klasse3Mathe,klasse3Kunst, klasse3Sport;
	Planungseinheit klasse4Deutsch, klasse4Mathe,klasse4Kunst, klasse4Sport;
	Planungseinheit klasse3und4Sport;
	Room r1,r2,r3,r4,r5,klassenZimmer1, klassenZimmer2,klassenZimmer3,klassenZimmer4,sportHalle;
	Schoolclass klasse1,klasse2,klasse3,klasse4;
	HashMap<String, Integer> stundenBedarf = new HashMap<String, Integer>();
	Stundeninhalt Deutsch, Mathe, Kunst, Sport;
	Raumfunktion Deu, rfMathe, rfKunst, rfSport;
	
	
	@BeforeClass
	public static void startDB() {
		System.out.println("TEST - PlanungseinheitManager");
		Data.start();
		Data.deleteAll();
	}
	
	
	@Before
	public void createDataForTest(){
		gependelt.put(Weekday.MONDAY, false);
		gependelt.put(Weekday.TUESDAY, false);
		gependelt.put(Weekday.WEDNESDAY, false);
		gependelt.put(Weekday.THURSDAY, false);
		gependelt.put(Weekday.FRIDAY, false);
		gependelt.put(Weekday.SATURDAY, false);
		gependelt.put(Weekday.SUNDAY, false);
		
		moeglicheFunktion.add("Deu");
		moeglicheFunktion.add("Mat");
		moeglicheFunktion.add("Kun");
		pWunschzeiten.put(Weekday.MONDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.TUESDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.WEDNESDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.THURSDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.FRIDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.SATURDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeiten.put(Weekday.SUNDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		
		pWunschzeitenP1.put(Weekday.MONDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),10,00});
		pWunschzeitenP1.put(Weekday.TUESDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		
		pWunschzeitenP2.put(Weekday.MONDAY,new int []{10,00,TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		pWunschzeitenP2.put(Weekday.TUESDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),10,00});
		
		sportLehrerWunschzeiten.put(Weekday.MONDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.TUESDAY,new int []{10,00,TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.WEDNESDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.THURSDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.FRIDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.SATURDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		sportLehrerWunschzeiten.put(Weekday.SUNDAY,new int []{TimetableManager.startTimeHour(),TimetableManager.startTimeMinute(),TimetableManager.endTimeHour(),TimetableManager.endTimeMinute()});
		
		stundenInhalteP1.add("Deu");
		stundenInhalteP1.add("Kun");
		stundenInhalteP2.add("Mat");
		stundenInhalteP2.add("Deu");
		stundenInhalteP3.add("Mat");
		stundenInhalteP3.add("Kun");
		stundenInhalteP4.add("Deu");
		stundenInhalteP4.add("Mat");
		stundenInhalteP4.add("Kun");
		stundenInhalteP4.add("Spo");
		sportInhalte.add("Spo");
		
		Deutsch = new Stundeninhalt("Deutsch", "Deu", 90, 2);
		Mathe = new Stundeninhalt("Mathe", "Mat", 90,2);
		Kunst = new Stundeninhalt("Kunst", "Kun",90,1);
		Sport = new Stundeninhalt("Sport","Spo", 90, 1);
		p1 = new Personal("Max Mustermann","MMM",35,0,30,false,stundenInhalteP1,pWunschzeitenP1);
		p2 = new Personal("Pierce Hawthrone","PCH", 35, 0,30, true,stundenInhalteP2,pWunschzeiten);
		p3 = new Personal("Patrick Warszewik","PWA",35,0,30 ,true,stundenInhalteP3, pWunschzeiten);
		p4 = new Personal("Roman Schmidt", "RS1",35,0,30,true, stundenInhalteP4,pWunschzeiten);
		p5 = new Personal("Sport Lehrer", "SL1",30,0,30,false,sportInhalte,sportLehrerWunschzeiten);
		PersonalManager.addPersonalToDb(p1);
		PersonalManager.addPersonalToDb(p2);
		PersonalManager.addPersonalToDb(p3);
		PersonalManager.addPersonalToDb(p5);
		klassenZimmer1 = new Room("KlassenZimmer1",1,moeglicheFunktion);
		klassenZimmer2 = new Room("KlassenZimmer2",2,moeglicheFunktion);
		klassenZimmer3 = new Room("KlassenZimmer3",2,moeglicheFunktion);
		klassenZimmer4 = new Room("KlassenZimmer4",2,moeglicheFunktion);
		DataRaum.addRaum(klassenZimmer1);
		DataRaum.addRaum(klassenZimmer2);
		DataRaum.addRaum(klassenZimmer3);
		DataRaum.addRaum(klassenZimmer4);
		r1 = new Room("MZH",1,leereInhalte);
		r2 = new Room("SFG",1,moeglicheFunktion);
		r3 = new Room("GW1",2,moeglicheFunktion);
		r4 = new Room("PC Pool",1,moeglicheFunktion);
		r5 = new Room("Tab",2,moeglicheFunktion);
		moeglicheFunktionSport.add("Sport");
		sportHalle = new Room("SportHalle",2,moeglicheFunktionSport);
		DataRaum.addRaum(sportHalle);
		DataRaum.addRaum(r1);
		DataRaum.addRaum(r2);
		DataRaum.addRaum(r3);
		DataRaum.addRaum(r4);
		DataRaum.addRaum(r5);
		
		
		Deu = new Raumfunktion("Deu",moeglicheFunktion);
		rfMathe = new Raumfunktion("Mat", moeglicheFunktion);
		rfKunst = new Raumfunktion("Kun", moeglicheFunktion);
		rfSport = new Raumfunktion("Spo", moeglicheFunktionSport);
		DataRaum.addRaumfunktion(Deu);
		DataRaum.addRaumfunktion(rfMathe);
		DataRaum.addRaumfunktion(rfKunst);
		DataRaum.addRaumfunktion(rfSport);
		klasse1 = new Schoolclass("1A",1,klassenZimmer1,klassenlehrerKlasse1,stundenBedarf);
		klasse2 = new Schoolclass("2A",2,klassenZimmer2,klassenlehrerKlasse2,stundenBedarf);
		klasse3 = new Schoolclass("3A",3,klassenZimmer3,klassenlehrerKlasse3,stundenBedarf);
		klasse4 = new Schoolclass("4A",4,klassenZimmer4,klassenlehrerKlasse4,stundenBedarf);
		DataSchulklasse.addSchulklasse(klasse1);
		DataSchulklasse.addSchulklasse(klasse2);
		DataSchulklasse.addSchulklasse(klasse3);
		DataSchulklasse.addSchulklasse(klasse4);
		klassenlehrerKlasse1.add(p1.getName());
		klassenlehrerKlasse2.add(p2.getName());
		klassenlehrerKlasse3.add(p3.getName());
		klassenlehrerKlasse4.add(p4.getName());
		klasse1Deutsch = new Planungseinheit(0,Weekday.MONDAY,10,Config.TIMESLOT_LENGTH,12,(Config.TIMESLOT_LENGTH+Config.TIMESLOT_LENGTH));
		klasse1Deutsch.addPersonal(p1, new int[] {10,Config.TIMESLOT_LENGTH,12,(Timeslot.LENGTH+Timeslot.LENGTH)});
		klasse1Deutsch.addRoom(klassenZimmer1);
		klasse1Deutsch.addSchulklassen(klasse1);
		klasse1Deutsch.addStundeninhalt(Deutsch);
		klasse1Mathe = new Planungseinheit(1,Weekday.TUESDAY,13,Config.TIMESLOT_LENGTH,14,Config.TIMESLOT_LENGTH);
		klasse1Mathe.addPersonal(p3,new int[]{13,Config.TIMESLOT_LENGTH,14,Config.TIMESLOT_LENGTH});
		klasse1Mathe.addRoom(klassenZimmer1);
		klasse1Mathe.addSchulklassen(klasse1);
		klasse1Mathe.addStundeninhalt(Mathe);
		klasse1Kunst = new Planungseinheit(2,Weekday.MONDAY,8,10,9,50);
		klasse1Kunst.addPersonal(p3, new int[]{8,10,9,50});
		klasse1Kunst.addRoom(klassenZimmer1);
		klasse1Kunst.addSchulklassen(klasse1);
		klasse1Kunst.addStundeninhalt(Kunst);
		klasse1Sport = new Planungseinheit(3,Weekday.TUESDAY,10,10,11,50);
		klasse1Sport.addPersonal(p5,new int[]{10,10,11,50});
		klasse1Sport.addRoom(sportHalle);
		klasse1Sport.addSchulklassen(klasse1);
		klasse1Sport.addStundeninhalt(Sport);
		klasse2Deutsch = new Planungseinheit(4,Weekday.TUESDAY, 8,10,9,50);
		klasse2Deutsch.addPersonal(p2, new int[]{8,10,9,50});
		klasse2Deutsch.addRoom(klassenZimmer2);
		klasse2Deutsch.addSchulklassen(klasse2);
		klasse2Deutsch.addStundeninhalt(Deutsch);
		klasse2Mathe = new Planungseinheit(5,Weekday.MONDAY,8,10,9,50);
		klasse2Mathe.addPersonal(p3, new int[]{8,10,9,50});
		klasse2Mathe.addRoom(klassenZimmer2);
		klasse2Mathe.addSchulklassen(klasse2);
		klasse2Mathe.addStundeninhalt(Mathe);
		klasse2Kunst = new Planungseinheit(6,Weekday.MONDAY,10,10,11,50);
		klasse2Kunst.addPersonal(p3, new int[]{10,10,11,50});
		klasse2Kunst.addRoom(klassenZimmer2);
		klasse2Kunst.addSchulklassen(klasse2);
		klasse2Kunst.addStundeninhalt(Kunst);
		klasse2Sport = new Planungseinheit(7,Weekday.TUESDAY,10,10,11,50);
		klasse2Sport.addPersonal(p5, new int[]{10,10,11,50});
		klasse2Sport.addRoom(sportHalle);
		klasse2Sport.addSchulklassen(klasse2);
		klasse2Sport.addStundeninhalt(Sport);
		klasse3Deutsch = new Planungseinheit(8,Weekday.TUESDAY, 10,10,11,50);
		klasse3Deutsch.addPersonal(p1, new int[]{10,10,11,50});
		klasse3Deutsch.addRoom(klassenZimmer3);
		klasse3Deutsch.addSchulklassen(klasse3);
		klasse3Deutsch.addStundeninhalt(Deutsch);
		klasse3Mathe = new Planungseinheit(9,Weekday.MONDAY, 10,10,11,50);
		klasse3Mathe.addPersonal(p2,new int[]{ 10,10,11,50});
		klasse3Mathe.addRoom(klassenZimmer3);
		klasse3Mathe.addSchulklassen(klasse3);
		klasse3Mathe.addStundeninhalt(Mathe);
		klasse3Kunst = new Planungseinheit(10,Weekday.TUESDAY, 8,10,9,50);
		klasse3Kunst.addPersonal(p1, new int[]{8,10,9,50});
		klasse3Kunst.addRoom(klassenZimmer3);
		klasse3Kunst.addSchulklassen(klasse3);
		klasse3Kunst.addStundeninhalt(Kunst);
		klasse3Sport = new Planungseinheit(11, Weekday.MONDAY, 8,10,9,50);
		klasse3Sport.addPersonal(p5, new int[]{8,10,9,50});
		klasse3Sport.addRoom(sportHalle);
		klasse3Sport.addSchulklassen(klasse3);
		klasse3Sport.addStundeninhalt(Sport);
		klasse4Deutsch = new Planungseinheit(12,Weekday.MONDAY,10,10,11,50);
		klasse4Deutsch.addSchulklassen(klasse4);
		klasse4Deutsch.addStundeninhalt(Deutsch);
		klasse4Sport = new Planungseinheit(13, Weekday.TUESDAY,8,10,9,50);
		klasse4Sport.addStundeninhalt(Sport);
		klasse3und4Sport = new Planungseinheit(14, Weekday.TUESDAY,8,10,9,10);
		klasse3und4Sport.addRoom(sportHalle);
		klasse3und4Sport.addRoom(klassenZimmer3);
		DataPlanungseinheit.addPlanungseinheit(klasse1Deutsch);
		DataPlanungseinheit.addPlanungseinheit(klasse1Mathe);
		DataPlanungseinheit.addPlanungseinheit(klasse1Kunst);
		DataPlanungseinheit.addPlanungseinheit(klasse1Sport);
		DataPlanungseinheit.addPlanungseinheit(klasse2Deutsch);
		DataPlanungseinheit.addPlanungseinheit(klasse2Mathe);
		DataPlanungseinheit.addPlanungseinheit(klasse2Kunst);
		DataPlanungseinheit.addPlanungseinheit(klasse2Sport);
		DataPlanungseinheit.addPlanungseinheit(klasse3Deutsch);
		DataPlanungseinheit.addPlanungseinheit(klasse3Mathe);
		DataPlanungseinheit.addPlanungseinheit(klasse3Kunst);
		DataPlanungseinheit.addPlanungseinheit(klasse3Sport);
		
		
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
	public void testPendelCounter() {
		assertTrue(PlanungseinheitManager.pendelCounter(p4, Weekday.MONDAY)==0);
		assertNotNull(PlanungseinheitManager.pendelCounter(p1, Weekday.MONDAY));
		assertTrue(PlanungseinheitManager.pendelCounter(p1, Weekday.MONDAY)==0);
		assertTrue(PlanungseinheitManager.pendelCounter(p1,Weekday.TUESDAY)==0);
		assertTrue(PlanungseinheitManager.pendelCounter(p2,Weekday.MONDAY)==0);
		assertFalse(PlanungseinheitManager.pendelCounter(p2,Weekday.TUESDAY)==3);
		assertTrue(PlanungseinheitManager.pendelCounter(p3, Weekday.MONDAY)==1);
		assertFalse(PlanungseinheitManager.pendelCounter(p3,Weekday.TUESDAY)==1);
		
	}
	
	@Test
	public void testPersonalWZCheck() {
		assertTrue(PlanungseinheitManager.personalWZCheck(p1, klasse4Deutsch));	
		assertTrue(PlanungseinheitManager.personalWZCheck(p5, klasse4Sport));
		assertFalse(PlanungseinheitManager.personalWZCheck(p1, klasse1Mathe));
		assertFalse(PlanungseinheitManager.personalWZCheck(p2, klasse1Mathe));
	}
	
	@Test
	public void testPersonalSiCheck(){
		Personal pTest1 = new Personal("Testi Test" , "TT1", 20, 0,20, true, leereInhalte,pWunschzeiten);
		assertFalse(PlanungseinheitManager.personalsiCheck(p1, klasse4Sport));
		assertFalse(PlanungseinheitManager.personalsiCheck(p5, klasse2Sport));	
		assertFalse(PlanungseinheitManager.personalsiCheck(p4, klasse4Deutsch));
		assertTrue(PlanungseinheitManager.personalsiCheck(p5, klasse4Deutsch));
		assertFalse(PlanungseinheitManager.personalsiCheck(pTest1, klasse2Sport));
	}
	
	@Test
	public void testRoomsiCheck(){
		assertTrue(PlanungseinheitManager.roomsiCheck(sportHalle, klasse4Deutsch));
		assertFalse(PlanungseinheitManager.roomsiCheck(klassenZimmer4, klasse4Deutsch));
		assertTrue(PlanungseinheitManager.roomsiCheck(sportHalle, klasse1Sport));
		assertFalse(PlanungseinheitManager.roomsiCheck(klassenZimmer3, klasse3und4Sport));
		assertFalse(PlanungseinheitManager.roomsiCheck(r1, klasse1Kunst));
	}
	
	@Test
	public void testOverTimePers(){
		assertFalse(PlanungseinheitManager.overtimePers(p1, 90));
		assertTrue(PlanungseinheitManager.overtimePers(p1, 90000));
	}
	
	@Test
	public void testCheckTwoPEs(){
		Planungseinheit test1 = new Planungseinheit(16,Weekday.MONDAY,9,30,14,00);
		assertTrue(PlanungseinheitManager.checktwoPEs(klasse3Sport, test1));
		assertTrue(PlanungseinheitManager.checktwoPEs(klasse2Sport, test1));
		assertFalse(PlanungseinheitManager.checktwoPEs(klasse1Deutsch, klasse1Mathe));
	}
	
	@Test
	public void testCheckTimeInPE(){
		assertFalse(PlanungseinheitManager.checkTimeInPE(klasse1Deutsch, 10, 30));
		assertTrue(PlanungseinheitManager.checkTimeInPE(klasse1Deutsch, 12, 80));
		assertFalse(PlanungseinheitManager.checkTimeInPE(klasse1Mathe, 13, 15));
		assertTrue(PlanungseinheitManager.checkTimeInPE(klasse1Mathe,8, 30));
		assertTrue(PlanungseinheitManager.checkTimeInPE(klasse4Deutsch, 12, 80));
		assertTrue(PlanungseinheitManager.checkTimeInPE(klasse4Sport, 10, 30));
		DataPlanungseinheit.addPlanungseinheit(klasse4Deutsch);
		assertFalse(PlanungseinheitManager.checkTimeInPE(klasse4Deutsch, 10, 33));
		DataPlanungseinheit.addPlanungseinheit(klasse4Sport);
		assertFalse(PlanungseinheitManager.checkTimeInPE(klasse4Sport, 8, 31));
		

		
	}
	
	
	

}
