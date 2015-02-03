package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unibremen.swp.stundenplan.command.AddPlanungseinheitToDB;
import de.unibremen.swp.stundenplan.command.DeletePlanungseinheitFromDB;
import de.unibremen.swp.stundenplan.command.EditPlanungseinheit;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.gui.Timeslot;

public final class PlanungseinheitManager {

	private static int peindex = 0;

	private PlanungseinheitManager() {
	}

	public static void addPlanungseinheitToDB(Planungseinheit pl) {
		System.out.println("Adding Planungseinheit [" + pl.getId()
				+ "] to DB...");
		pl.setId(createId());
		AddPlanungseinheitToDB addPl = new AddPlanungseinheitToDB();
		addPl.execute(pl);
		System.out.println("Planungseinheit [" + pl.getId() + "] added.");
	}

	/**
	 * Bearbeitet eine Planungseinheit aus der DB. Bearbeiten findet im
	 * woertlichen Sinne nicht statt, das ausgewaehlte Objekt wird mit einem neuen
	 * ueberschrieben.
	 * 
	 * @param pId
	 *            Die ID der Planungseinheit, die bearbeitet werden soll.
	 * @param pl
	 *            Die Planungseinheit, mit der die alte Planungseinheit
	 *            ueberschrieben wird.
	 */
	public static void editPlanungseinheit(int pId, Planungseinheit pl) {
		System.out.println("Editing Planungseinheit [" + pId + "].");
		EditPlanungseinheit editPl = new EditPlanungseinheit();
		editPl.execute(pId, pl);
		System.out.println("Planungseinheit [" + pl.getId() + "] edited.");
	}

	public static Planungseinheit getPlanungseinheitById(final int pId) {
		return DataPlanungseinheit.getPlanungseinheitById(pId);
	}

	public static void deletePlanungseinheitFromDB(int planungseinheitId) {
		System.out.println("Deleting Planungseinheit [" + planungseinheitId
				+ "] from DB...");
		DeletePlanungseinheitFromDB deletePl = new DeletePlanungseinheitFromDB();
		deletePl.execute(planungseinheitId);
		System.out.println("Planungseinheit [" + planungseinheitId
				+ "]) deleted.");
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Personal an einem Tag
	 * zurueckgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pPerson
	 *            das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForWeekdayDemo(
			Weekday pWeekday) {
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		for (Planungseinheit p : DataPlanungseinheit.getAllPlanungseinheit()) {
			if (p.isWeekday(pWeekday)) {
				pes.add(p);
			}
		}
		orderByTime(pes);
		return pes;
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Personal an einem Tag
	 * zurueckgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pPerson
	 *            das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForPersonalbyWeekday(
			Weekday pWeekday, final Personal pPerson) {
		ArrayList<Planungseinheit> pes = DataPlanungseinheit.getAllPlanungseinheitByWeekdayAndObject(pWeekday, pPerson);
		orderByTime(pes);
		return pes;
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einer Schulklasse an einem Tag
	 * zurueckgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pPerson
	 *            das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForSchoolclassbyWeekday(
			Weekday pWeekday, final Schoolclass pSchoolclass) {
		ArrayList<Planungseinheit> pes = DataPlanungseinheit.getAllPlanungseinheitByWeekdayAndObject(pWeekday, pSchoolclass);
		orderByTime(pes);
		return pes;
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Raum an einem Tag
	 * zurueckgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pRoom
	 *            Den Raum den in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForRoombyWeekday(
			Weekday pWeekday, final Room pRoom) {
		ArrayList<Planungseinheit> pes = DataPlanungseinheit.getAllPlanungseinheitByWeekdayAndObject(pWeekday, pRoom);
		orderByTime(pes);
		return pes;
	}
	
	/**
	 * ueberprueft ob Planungseinheiten mit Zeitraster(Timeslotlength) passt.
	 * @return true wenn Planungseinheiten nicht mit Zeitraster passt sonst false.
	 */
	public static boolean consistencecheck(){
		if(getAllPlanungseinheitFromDB().size()!=0){
			for(Planungseinheit p : getAllPlanungseinheitFromDB()){
				if(p.duration()%Timeslot.timeslotlength()!= 0){
					return true;
				}
			}
		}
		return false;
		
	}

	
	/**
	 * ordnet eine Liste von Planungseinheiten nach Zeit, die Liste soll nur Planungseinheiten
	 * von einem Tag beinhalten
	 * @param pPE die Liste von Planungseinheiten
	 */
	public static void orderByTime(List<Planungseinheit> pPE) {
		Collections.sort(pPE, new Comparator<Planungseinheit>() {
			@Override
			public int compare(Planungseinheit p1, Planungseinheit p2) {
				if (p1.getStartHour() != p2.getStartHour()) {
					return p1.getStartHour() - p2.getStartHour(); // Ascending
				} else {
					return p1.getStartminute() - p2.getStartminute();
				}
			}
		});
	}

	/**
	 * ordnet eine Liste von Planungseinheiten nach id-Nummer
	 * @param pPE die Liste von Planungseinheiten
	 */
	public static void orderByID(List<Planungseinheit> pPE) {
		Collections.sort(pPE, new Comparator<Planungseinheit>() {
			@Override
			public int compare(Planungseinheit p1, Planungseinheit p2) {
				return p1.getId() - p2.getId(); // Ascending
			}
		});
	}

	/**
	 * Testet orderByTime()
	 */
	public static void ordertest() {
		Planungseinheit pe1 = new Planungseinheit();
		pe1.setStarthour(8);
		pe1.setStartminute(0);
		Planungseinheit pe2 = new Planungseinheit();
		pe2.setStarthour(8);
		pe2.setStartminute(30);
		Planungseinheit pe3 = new Planungseinheit();
		pe3.setStarthour(9);
		pe3.setStartminute(30);
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		pes.add(pe2);
		pes.add(pe3);
		pes.add(pe1);
		for (Planungseinheit p : pes) {
			System.out.println(p.getStartHour());
			System.out.println(p.getStartminute());
		}
		orderByTime(pes);
		for (Planungseinheit p : pes) {
			System.out.println(p.getStartHour());
			System.out.println(p.getStartminute());
		}
		orderByID(pes);
		for (Planungseinheit p : pes) {
			System.out.println(p.getId());
		}
	}

	public static ArrayList<Planungseinheit> demomethod(Weekday pWeekday) {
		ArrayList<Planungseinheit> p = getPEForWeekdayDemo(pWeekday);
		orderByTime(p);
		return p;
	}
	
	/**
	 * Zaehlt wiviel mal eine Person am Tag die Gebaeude wechselt
	 * @param pers Personal
	 * @param pDay Tag
	 * @return gibt die Anzahl von Wechsel
	 */
	public static int pendelCounter( final Personal pers, final Weekday pDay){
		int counter = 0;
		int gebnr = -1;
		for(Planungseinheit p : getPEForPersonalbyWeekday(pDay, pers)){
			if(p.getRooms().size() != 0){
			Room r = DataRaum.getRaumByName(p.getRooms().get(0));
			if(gebnr != -1 && gebnr != r.getGebaeude()){
				counter ++;
			}
			gebnr = r.getGebaeude();
			}
		}
		return counter;
	}
	 /**
	  * prueft ob die geplante Planungseinheit mit dem Wunschzeiten der Personal
	  * uebereinstimmen
	  * @param pPer Personal die in die Planungseinheit hinzugefuegt werden soll
	  * @param pPe	neue Planungseinheit die gprueft wird
	  * @return	gibt true zurueck, falls die Zeiten der Planungseinheiten ausserhalb der Wunschzeiten sind
	  */
	public static boolean personalWZCheck(final Personal pPer,
			final Planungseinheit pPe) {
		int[] wunschzeiten = pPer.getWunschzeitForWeekday(pPe.getWeekday());
		Planungseinheit p = new Planungseinheit();
		p.setStarthour(wunschzeiten[0]);
		p.setStartminute(wunschzeiten[1]);
		p.setEndhour(wunschzeiten[2]);
		p.setEndminute(wunschzeiten[3]);
		p.setId(0);
		if (!checktwoPEs(pPe, p)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Prueft ob Personal fuer die geplanten Stundeninhalte im Planungseinheit
	 * eingetragen ist, falls Personal fuer keine SI eingetragen ist, wird die Pruefung beendet.
	 * @param pPer Personal die in die Planung eingetragen werden soll
	 * @param pPe	Planungseinheit die geprueft wird
	 * @return gibt true zurueck, falls Stundeninhalte im PE nicht fuer die Personal eingetragen ist.
	 */
	public static boolean personalsiCheck(final Personal pPer, final Planungseinheit pPe){
		if(pPe.getStundeninhalte().size() == 0){return false;}
		if(pPe.getSchoolclasses().size() == 0){return false;}
		if(pPer.getMoeglicheStundeninhalte().size() == 0){return true;}
			for(String si : pPe.getStundeninhalte()){
				if(!pPer.getMoeglicheStundeninhalte().contains(si)){
				return true;
			}	
		
		}
		return false;
	}
	
	/**
	 * Prueft ob Raum fuer die geplanten Stundeninhalte im Planungseinheit
	 * eingetragen ist, falls Raum fuer keine SI eingetragen ist, wird die Pruefung beendet.
	 * @param pr   Raum die in der Planung eingetragen werden soll
	 * @param pPe	Planungseinheit die geprueft wird
	 * @return gibt true zurueck, falls Stundeninhalte im PE nicht fuer die Raum eingetragen ist.
	 */
	public static boolean roomsiCheck(final Room pr, final Planungseinheit pPe){
		if(pr.getMoeglicheFunktionen().size()==0){return true;}
		if(pPe.getSchoolclasses().size() == 0){return false;}
		if(pPe.getStundeninhalte().size() == 0){return false;}
		for(String s : pr.getMoeglicheFunktionen()){
		Raumfunktion rf = DataRaum.getRaumfunktionByName(s);
			for(String si : pPe.getStundeninhalte()){
				if(!rf.getStundeninhalte().contains(si)){
				return true;
				}	
			}
		}
		return false;
	}
	
	public static int newTimeforPers(final int oldtime, final int newtimemin){
		return oldtime + (newtimemin/60);
	}
	
	/**
	 * prueft ob die neue Planung die Istzeit den Sollzeit uebersteigt
	 * @param pPers
	 * @param newtimemin
	 * @return
	 */
	public static boolean overtimePers(final Personal pPers, final int newtimemin){
		int newizeit = newTimeforPers(pPers.getIstZeit(), newtimemin);
		System.out.println("oldistZeit="+pPers.getIstZeit());
		System.out.println("newistZeit="+newizeit);
		System.out.println("Sollzeit"+pPers.getSollZeit());
		if(newizeit>pPers.getSollZeit()){
			return true;
		}
		return false;
	}
	
	/**
	 * prueft ob zwei Planungseinheiten sich ueberschneiden im selben Tag.
	 * 
	 * @return gibt true zurueck wenn die PEs sich ueberlappen
	 */
	public static boolean checktwoPEs(final Planungseinheit p1,
			final Planungseinheit p2) {
		if(p1 == null || p2 == null){throw new IllegalArgumentException("Parameters should be not null");}
		if(p1.getId() == p2.getId()){return false;}
		if (checkPEandStartTime(p1, p2.getStartHour(), p2.getStartminute())) {
			return true;
		} else if (checkPEandEndTime(p1, p2.getEndhour(), p2.getEndminute())) {
			return true;
		} else if (checkPEandStartTime(p2, p1.getStartHour(),
				p1.getStartminute())) {
			return true;
		} else if (checkPEandEndTime(p2, p1.getEndhour(), p1.getEndminute())) {
			return true;
		}
		return false;
	}
	
	/**
	 * prueft ob Zeit zwischen die Zeiten des PE ist, War prototyp der Methoden
	 * @param p1 PE
	 * @param hour Stunde der Zeit
	 * @param minute Minute der Zeit
	 * @return gibt true zurueck falls Zeit im PE befindet
	 */
	private static boolean checkPEandTime(final Planungseinheit p1,
			final int hour, final int minute) {
		if (hour > p1.getStartHour() && hour < p1.getEndhour()) {
			return true;
		} else if (hour == p1.getStartHour() && minute > p1.getStartminute()) {
			return true;
		} else if (hour == p1.getEndhour() && minute < p1.getEndminute()) {
			return true;
		}
		return false;

	}

	/**
	 * prueft auf ueberschneidungen von PE fuer Personal
	 * 
	 * @return gibt true zurueck wenn die PEs sich ueberlappen
	 */
	public static boolean checkPersonPE(final Personal p,
			final Planungseinheit pe, final Weekday day) {
		ArrayList<Planungseinheit> pes = getPEForPersonalbyWeekday(day, p);
		for (Planungseinheit pl : pes) {
			if (checktwoPEs(pl, pe))
				return true;
		}
		return false;
	}

	/**
	 * prueft auf ueberschneidungen von PE fuer Personal
	 * 
	 * @return gibt true zurueck wenn die PEs sich ueberlappen
	 */
	public static boolean checkRoomPE(final Room r, final Planungseinheit pe,
			final Weekday day) {
		ArrayList<Planungseinheit> pes = getPEForRoombyWeekday(day, r);
		for (Planungseinheit pl : pes) {
			if (checktwoPEs(pl, pe))
				return true;
		}
		return false;
	}

	/**
	 * prueft auf ueberschneidungen von PE fuer Personal
	 * 
	 * @return gibt true zurueck wenn die PEs sich ueberlappen
	 */
	public static boolean checkScPE(final Schoolclass sc,
			final Planungseinheit pe, final Weekday day) {
		ArrayList<Planungseinheit> pes = getPEForSchoolclassbyWeekday(day, sc);
		for (Planungseinheit pl : pes) {
			if (checktwoPEs(pl, pe))
				return true;
		}
		return false;
	}

	/**
	 * prueft ob Startzeitpunkt sich mit PE ueberschneidet im selben Tag.
	 * prueft bis auf die letzte Minute der PE, da Startzeit und Endzeit zwei 
	 * folgenden PE gleich sein kann
	 * @return gibt true zurueck falls es sich ueberscheiden,
	 */
	public static boolean checkPEandStartTime(final Planungseinheit p1,
			final int hour, final int minute) {
		if (hour > p1.getStartHour() && hour < p1.getEndhour()) {
			return true;
		} else if ((hour == p1.getStartHour() && hour == p1.getEndhour())) {
			if (minute >= p1.getStartminute() && minute < p1.getEndminute()) {
				return true;
			} else {
				return false;
			}
		} else if (hour == p1.getStartHour() && minute >= p1.getStartminute()) {
			return true;
		} else if (hour == p1.getEndhour() && minute < p1.getEndminute()) {
			return true;
		}
		return false;
	}

	/**
	 *prueft ob Endzeitpunkt sich mit PE ueberschneidet im selben Tag.
	 * prueft bis auf die erste Minute der PE, da Endzeit und Startzeit zwei 
	 * folgenden PE gleich sein kann
	 * @return gibt true zurueck falls die Endzeit mit dem PE ausser der ersten Minute ueberlappt
	 */
	public static boolean checkPEandEndTime(final Planungseinheit p1,
			final int hour, final int minute) {
		if (hour > p1.getStartHour() && hour < p1.getEndhour()) {
			return true;
		} else if ((hour == p1.getStartHour() && hour == p1.getEndhour())) {
			if (minute > p1.getStartminute() && minute <= p1.getEndminute()) {
				return true;
			} else {
				return false;
			}
		} else if (hour == p1.getStartHour() && minute > p1.getStartminute()) {

			return true;
		} else if (hour == p1.getEndhour() && minute <= p1.getEndminute()) {

			return true;
		}
		return false;
	}

	/**
	 * prueft ob Zeitpunkt nicht im PE befindet, im selben Tag.
	 * 
	 * @return gibt true zurueck wenn Zeit nicht im 
	 */
	public static boolean checkTimeInPE(final Planungseinheit p1,
			final int hour, final int minute) {
		if (hour < p1.getStartHour() && hour > p1.getEndhour()) {
			return true;
		}  else if ((hour == p1.getStartHour() && hour == p1.getEndhour())) {
			if (minute < p1.getStartminute() && minute > p1.getEndminute()) {
				return true;
			} else {
				return false;
			}
		}  else if (hour == p1.getStartHour() && minute < p1.getStartminute()) {
			return true;
		} else if (hour == p1.getEndhour() && minute > p1.getEndminute()) {
			return true;
		}
		return false;
	}

	/**
	 * teste die Zeitueberlappungpruefung
	 */
	public static void checkPetimetest() {
		Planungseinheit p1 = new Planungseinheit();
		p1.setStarthour(9);
		p1.setStartminute(30);
		p1.setEndhour(10);
		p1.setEndminute(30);
		Planungseinheit p2 = new Planungseinheit();
		p2.setStarthour(11);
		p2.setStartminute(30);
		p2.setEndhour(12);
		p2.setEndminute(30);
		System.out.println("true " + checkPEandTime(p1, 9, 30));
		System.out.println("true " + checkPEandTime(p1, 10, 0));
		System.out.println("true " + checkPEandTime(p1, 10, 30));
		System.out.println("false " + checkPEandTime(p1, 10, 45));
		System.out.println("false " + checkPEandTime(p1, 9, 15));
		System.out.println("false " + checkPEandTime(p1, 8, 15));
		System.out.println("false " + checkPEandTime(p1, 11, 15));
		System.out.println("2pecomparison : false " + checktwoPEs(p1, p2));
		p2.setStarthour(10);
		p2.setStartminute(15);
		System.out.println("2pecomparison : true " + checktwoPEs(p1, p2));
		p2.setStarthour(9);
		p2.setStartminute(15);
		System.out.println("2pecomparison : true " + checktwoPEs(p1, p2));
	}
	
	public static ArrayList<Schoolclass> getSCforPE(Planungseinheit pPE){
		ArrayList<Schoolclass> scList = new ArrayList<Schoolclass>();
		for(String s: pPE.getSchoolclasses()){
			scList.add(DataSchulklasse.getSchulklasseByName(s));
		}
		return scList;
	}
	
	public static ArrayList<Room> getRforPE(Planungseinheit pPE){
		ArrayList<Room> rList = new ArrayList<Room>();
		for(String s: pPE.getRooms()){
			rList.add(DataRaum.getRaumByName(s));
		}
		return rList;
	}
	
	public static ArrayList<Stundeninhalt> getSIforPE(Planungseinheit pPE){
		ArrayList<Stundeninhalt> siList = new ArrayList<Stundeninhalt>();
		for(String s: pPE.getStundeninhalte()){
			siList.add(DataStundeninhalt.getStundeninhaltByKuerzel(s));
		}
		return siList;
	}
	
	/**
	 * Gibt alle Planungseinheiten in der DB als ArrayList zurueck
	 */
	public static ArrayList<Planungseinheit> getAllPlanungseinheitFromDB() {
		System.out.println("Getting all Planungseinheiten from DB...");
		return DataPlanungseinheit.getAllPlanungseinheit();
	}

	public static void dbPETest() {
		Planungseinheit p1 = new Planungseinheit();
		Planungseinheit p2 = new Planungseinheit();
		p1.setStarthour(9);
		p1.setStartminute(30);
		p1.setEndhour(10);
		p1.setEndminute(30);
		p2.setStarthour(11);
		p2.setStartminute(30);
		p2.setEndhour(12);
		p2.setEndminute(30);
		p1.setWeekday(Weekday.MONDAY);
		p2.setWeekday(Weekday.SATURDAY);
		addPlanungseinheitToDB(p1);
		addPlanungseinheitToDB(p2);
	}

	/**
	 * hier wird ein Id fuer eine PE generiert
	 * 
	 * @return einzigartige Id des PE
	 */
	public static int createId() {
		ArrayList<Planungseinheit> pes = getAllPlanungseinheitFromDB();
		orderByID(pes);
		System.out.println("size:" + getAllPlanungseinheitFromDB().size());
		for (Planungseinheit p : pes) {
			System.out.println("iddb:" + p.getId());
		}
		if (pes.size() == 0) {
			return 1;
		} else {
			return pes.get(pes.size() - 1).getId() + 1;
		}
	}
}