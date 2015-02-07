package de.unibremen.swp.stundenplan.data;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.Data;

/**
 * Datenklasse des Personals
 * 
 * @author Oliver
 *
 */
public final class Personal {

	/**
	 * Der Name dieser Person.
	 */
	private String name;

	/**
	 * Kuerzel / primary key
	 */
	private String kuerzel;

	/**
	 * Zeit, die ein personal arbeiten soll
	 */
	private int sollZeit;

	/**
	 * Aktuelle Zeit die das Personal verbraucht hat
	 */
	private int istZeit;

	/**
	 * Zeit, die das Personal fuer externe Aktivitaeten verbraucht.
	 */
	private int ersatzZeit;

	/**
	 * Ist das Personal schon einmal an einem Tag gependelt, true, wenn nicht, dann false
	 */
	private HashMap<Weekday, Boolean> gependelt = new HashMap<Weekday, Boolean>();

	/**
	 * Ist das Personal ein Lehrer,true, ist es ein Paedagoge, false
	 */
	private boolean lehrer;

	/**
	 * Alle moeglichen Stundeninhalte eines Personals
	 */
	private ArrayList<String> moeglicheStundeninhalte = new ArrayList<String>();

	/**
	 * Die Wunschezeiten eines Personals
	 */
	private HashMap<Weekday, int[]> wunschzeiten = new HashMap<Weekday, int[]>();

	/**
	 * Konstruktor fuer das Personal
	 * 
	 * @param pName
	 *            Name
	 * @param pKuerzel
	 *            Kuerzel
	 * @param pSollZeit
	 *            so viel muss das Personal arbeiten
	 * @param pIstZeit
	 *            aktuell verbrauchte Zeit
	 * @param pErsatzZeit
	 *            andere aktivitaeten
	 * @param pGependelt
	 *            sagt ob ein Personal schon gependelt ist
	 * @param pLehrer
	 *            sagt ob ein Personal ein Lehrer ist
	 * @param pMoeglicheStundeninhalte
	 *            gibt die moeglichen STundeninhalte des Personals an
	 * @param pWunschzeiten
	 *            gibt die Wunschzeiten des Personals an
	 */
	public Personal(String pName, String pKuerzel, int pSollZeit, int pIstZeit,
			int pErsatzZeit, boolean pLehrer,
			ArrayList<String> pMoeglicheStundeninhalte,
			HashMap<Weekday, int[]> pWunschzeiten) {
		name = pName;
		kuerzel = pKuerzel;
		sollZeit = pSollZeit;
		istZeit = pIstZeit;
		ersatzZeit = pErsatzZeit;
		lehrer = pLehrer;
		moeglicheStundeninhalte = pMoeglicheStundeninhalte;
		wunschzeiten = pWunschzeiten;
		gependelt.put(Weekday.MONDAY, false);
		gependelt.put(Weekday.TUESDAY, false);
		gependelt.put(Weekday.WEDNESDAY, false);
		gependelt.put(Weekday.THURSDAY, false);
		gependelt.put(Weekday.FRIDAY, false);
		gependelt.put(Weekday.SATURDAY, false);
		gependelt.put(Weekday.SUNDAY, false);
	}

	/**
	 * Gibt den Namen dieses Lehrers zurueck.
	 * 
	 * @return den Namen dieses Lehrers
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen dieser LehrerIn auf den uebergebenen Namen. Falls der
	 * Name laenger als {@linkplain Data#MAX_NORMAL_STRING_LEN} Zeichen ist,
	 * wird er entsprechend gekuerzt. Fuehrende und folgende Leerzeichen werden
	 * entfernt. Liest eine {@link IllegalArgumentException} aus, falls der Name
	 * leer ist.
	 * 
	 * @param pName
	 *            der neue Name dieser LehrerIn
	 */
	public void setName(final String pName) {
		if (pName == null || pName.trim().isEmpty()) {
			throw new IllegalArgumentException("Der Name der LehrerIn ist leer");
		}
		name = pName.trim().substring(0,
				Math.min(Data.MAX_NORMAL_STRING_LEN, pName.length()));
	}

	/**
	 * Gibt das Kuerzel dieser LehrerIn zurueck.
	 * 
	 * @return das Kuerzel dieses LehrerIn
	 */
	public String getKuerzel() {
		return kuerzel;
	}

	/**
	 * Setzt das Kuerzel dieser LehrerIn auf das uebergebene Kuerzel. Falls
	 * das Kuerzel laenger als {@linkplain Data#MAX_kuerzel_LEN} Zeichen
	 * ist, wird es entsprechend gekuerzt. Fuehrende und folgende
	 * Leerzeichen werden entfernt. Loest eine
	 * {@link IllegalArgumentException} aus, falls das Kuerzel leer ist.
	 * 
	 * Die systemweite Eindeutigkeit des Kuerzels wird hier NICHT
	 * geprueft!
	 * 
	 * @param pkuerzel
	 *            das neue Kuerzel dieser LehrerIn
	 */
	public void setKuerzel(final String pKuerzel) {
		if (pKuerzel == null || pKuerzel.trim().isEmpty()) {
			throw new IllegalArgumentException("Kuerzel der LehrerIn ist leer");
		}
		kuerzel = pKuerzel.trim().substring(0,
				Math.min(Data.MAX_KUERZEL_LEN, pKuerzel.length()));
	}

	@Override
	public String toString() {
		return String.format("Kuerzel: %s, Name: %s", kuerzel, name);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Personal)) {
			return false;
		}
		final Personal that = (Personal) other;
		return kuerzel.equals(that.kuerzel);
	}

	@Override
	public int hashCode() {
		return kuerzel.hashCode();
	}

	public int getErsatzZeit() {
		return ersatzZeit;
	}

	public void setErsatzZeit(final int pErsatzZeit) {
		if (pErsatzZeit < 0) {
			throw new IllegalArgumentException("Negativer Wert");
		}
		ersatzZeit = pErsatzZeit;
	}

	public int getSollZeit() {
		return sollZeit;
	}

	public void setSollZeit(final int pSollZeit) {
		if (pSollZeit < 0) {
			throw new IllegalArgumentException("Negativer Wert");
		}
		sollZeit = pSollZeit;
	}

	public int getIstZeit() {
		return istZeit;
	}

	public void setIstZeit(final int i) {
		istZeit = i;
	}

	public ArrayList<String> getMoeglicheStundeninhalte() {
		return moeglicheStundeninhalte;
	}

	public String getmSI() {
		StringBuilder sb = new StringBuilder();
		for (String st : moeglicheStundeninhalte) {
			sb.append(st);
			sb.append(',');
		}
		if (moeglicheStundeninhalte.size() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public void setMoeglicheStundeninhalte(
			final ArrayList<String> pMoeglicheStundeninhalte) {
		moeglicheStundeninhalte = pMoeglicheStundeninhalte;
	}

	public HashMap<Weekday, int[]> getWunschzeiten() {
		return wunschzeiten;
	}

	public int[] getWunschzeitForWeekday(Weekday pWeekday) {
		return wunschzeiten.get(pWeekday);
	}

	public void setWunschZeiten(final HashMap<Weekday, int[]> pWunschzeiten) {
		wunschzeiten = pWunschzeiten;
	}

	public boolean isGependelt(Weekday weekday) {
		return gependelt.get(weekday);
	}

	public void setGependelt(Weekday weekday, boolean state) {
		gependelt.put(weekday, state);
	}

	public boolean isLehrer() {
		return lehrer;
	}

	public void setLehrer(final boolean pLehrer) {
		lehrer = pLehrer;
	}
}
