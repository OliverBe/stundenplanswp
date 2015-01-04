package de.unibremen.swp.stundenplan.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;

public final class PlanungseinheitManager {

	private PlanungseinheitManager() {
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Personal an einem Tag
	 * zurückgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pPerson
	 *            das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForPersonalbyWeekday(
			Weekday pWeekday, final Personal pPerson) {
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();
		// hier muss die Liste geholt werden
		order(pes);
		return pes;
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Personal an einem Tag
	 * zurückgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pPerson
	 *            das Personal die in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForSchoolclassbyWeekday(
			Weekday pWeekday, final Schoolclass pSchoolclass) {
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();

		// hier muss die Liste geholt werden
		order(pes);
		return pes;
	}

	/**
	 * Soll Planungseinheiten in einer Liste von einem Raum an einem Tag
	 * zurückgeben. Die Planungseinheiten sollen nach Zeiten geordnet sein.
	 * 
	 * @param pWeekday
	 *            der Tag der Planungeinheiten
	 * @param pRoom
	 *            Den Raum den in der Planungseinheiten geplant ist.
	 * @return Liste von Planungseinheiten nach geordnete Zeit.
	 */
	public static ArrayList<Planungseinheit> getPEForRoombyWeekday(
			Weekday pWeekday, final Room pRoom) {
		ArrayList<Planungseinheit> pes = new ArrayList<Planungseinheit>();

		// hier muss die Liste geholt werden
		order(pes);
		return pes;
	}

	public static void order(List<Planungseinheit> pPE) {
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
	 * Testet order()
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
		order(pes);
		for (Planungseinheit p : pes) {
			System.out.println(p.getStartHour());
			System.out.println(p.getStartminute());
		}
	}

	public static ArrayList<Planungseinheit> demomethod(Weekday pWeekday) {
		ArrayList<Planungseinheit> p = new ArrayList<Planungseinheit>();
		Planungseinheit p1 = new Planungseinheit();
		Planungseinheit p2 = new Planungseinheit();
		Personal per1 = new Personal();
		Personal per2 = new Personal();

		if (pWeekday.getOrdinal() == 0) {
			Stundeninhalt s1 = new Stundeninhalt("Mathe", "MAT", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Deutsch", "DEU", 45, 0);
			per1.setName("Max Mustermann");
			per1.setKuerzel("MM");
			per2.setName("Ned Stark");
			per2.setKuerzel("NS");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(9);
			p1.setStartminute(30);
			p1.setEndhour(10);
			p1.setEndminute(30);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(11);
			p2.setStartminute(30);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 1) {
			Stundeninhalt s1 = new Stundeninhalt("Westeroskunde", "WK", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Terrakunde", "TK", 45, 0);
			per1.setName("Rhaegar Bodoh");
			per1.setKuerzel("RB");
			per2.setName("Horus Rhet");
			per2.setKuerzel("HR");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(9);
			p1.setStartminute(0);
			p1.setEndhour(10);
			p1.setEndminute(45);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(10);
			p2.setStartminute(30);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 2) {
			Stundeninhalt s1 = new Stundeninhalt("Valyrian Steel", "VS", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Emperor", "EMP", 45, 0);
			per1.setName("Amuro Busk");
			per1.setKuerzel("AB");
			per2.setName("Grom Menethil");
			per2.setKuerzel("GM");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(8);
			p1.setStartminute(0);
			p1.setEndhour(11);
			p1.setEndminute(0);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(12);
			p2.setStartminute(00);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 3) {
			Stundeninhalt s1 = new Stundeninhalt("Winterfellkunde", "WF", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Padawan Sein", "PS", 45, 0);
			per1.setName("Ana Kind");
			per1.setKuerzel("AK");
			per2.setName("Luke Skywalker");
			per2.setKuerzel("LS");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(8);
			p1.setStartminute(0);
			p1.setEndhour(9);
			p1.setEndminute(0);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(12);
			p2.setStartminute(00);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 4) {
			Stundeninhalt s1 = new Stundeninhalt("TrollStudie", "TS", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Der Sith", "DS", 45, 0);
			per1.setName("Bill Gates");
			per1.setKuerzel("BG");
			per2.setName("Darth Serious");
			per2.setKuerzel("DS");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(9);
			p1.setStartminute(0);
			p1.setEndhour(9);
			p1.setEndminute(30);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(12);
			p2.setStartminute(00);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 5) {
			Stundeninhalt s1 = new Stundeninhalt("TrollStudie", "TS", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Der Sith", "DS", 45, 0);
			per1.setName("Bill Gates");
			per1.setKuerzel("BG");
			per2.setName("Darth Serious");
			per2.setKuerzel("DS");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(9);
			p1.setStartminute(0);
			p1.setEndhour(9);
			p1.setEndminute(30);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(12);
			p2.setStartminute(00);
			p2.setEndhour(12);
			p2.setEndminute(30);
		} else if (pWeekday.getOrdinal() == 6) {
			Stundeninhalt s1 = new Stundeninhalt("TrollStudie", "TS", 45, 0);
			Stundeninhalt s2 = new Stundeninhalt("Der Sith", "DS", 45, 0);
			per1.setName("Bill Gates");
			per1.setKuerzel("BG");
			per2.setName("Darth Serious");
			per2.setKuerzel("DS");
			p1.addPersonal(per1, new int[] { 7, 0, 15, 0 });
			p1.addStundeninhalt(s1);
			p1.setStarthour(9);
			p1.setStartminute(0);
			p1.setEndhour(9);
			p1.setEndminute(30);
			p2.addPersonal(per2, new int[] { 7, 0, 15, 0 });
			p2.addStundeninhalt(s2);
			p2.setStarthour(12);
			p2.setStartminute(00);
			p2.setEndhour(12);
			p2.setEndminute(30);
		}
		p.add(p1);
		p.add(p2);
		order(p);
		System.out.println(p.size());
		return p;
	}

	/**
	 * TO-DO prüft ob zwei Planungseinheiten sich überschneiden im selben Tag.
	 * 
	 * @return
	 */
	public boolean checktwoPEs(final Planungseinheit p1,
			final Planungseinheit p2) {
		return true;
	}

}
