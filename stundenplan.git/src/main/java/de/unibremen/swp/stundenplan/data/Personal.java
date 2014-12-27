package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.Data;

public final class Personal {
    
    private int id;
    
    /**
     * Der Name dieser LehrerIn.
     */
    private String name;

    /**
     * Das Kürzel dieser LehrerIn. Ein Kürzel muss systemweit eindeutig sein.
     */
    private String acronym;

    //integer referenziert zu stundeninhalt
    private ArrayList<Integer> moeglicheStundeninhalte;
    
    private int sollZeit;
    
    // au die richtige berechnung achten!
    private int istZeit;
    
    private int ersatzZeit;
    
    //array of time[2] geht irgendwie nicht
    private HashMap<Weekday,Time[]> wunschZeiten;
    
    // schon einmal am tag gependelt true
    private boolean gependelt;
    
    //lehrer true, p�dagoge false
    private boolean lehrer;
 
    /**
     * Gibt den Namen dieses Lehrers zurück.
     * 
     * @return den Namen dieses Lehrers
     */
    public String getName() {
        return name;
    }

    /**
     * Konstruktor f�r Personal.
     *
     */
    public Personal(){
    }
    
    /**
     * Setzt den Namen dieser LehrerIn auf den übergebenen Namen. Falls der Name länger als
     * {@linkplain Data#MAX_NORMAL_STRING_LEN} Zeichen ist, wird er entsprechend gekürzt. Führende und folgende
     * Leerzeichen werden entfernt. Löst eine {@link IllegalArgumentException} aus, falls der Name leer ist.
     * 
     * @param pName
     *            der neue Name dieser LehrerIn
     */
    public void setName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name der LehrerIn ist leer");
        }
        name = pName.trim().substring(0, Math.min(Data.MAX_NORMAL_STRING_LEN, pName.length()));
    }

    /**
     * Gibt das Kürzel dieser LehrerIn zurück.
     * 
     * @return das Kürzel dieses LehrerIn
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * Setzt das Kürzel dieser LehrerIn auf das übergebene Kürzel. Falls das Kürzel länger als
     * {@linkplain Data#MAX_ACRONYM_LEN} Zeichen ist, wird es entsprechend gekürzt. Führende und folgende
     * Leerzeichen werden entfernt. Löst eine {@link IllegalArgumentException} aus, falls das Kürzel leer ist.
     * 
     * Die systemweite Eindeutigkeit des Kürzels wird hier NICHT geprüft!
     * 
     * @param pAcronym
     *            das neue Kürzel dieser LehrerIn
     */
    public void setAcronym(final String pAcronym) {
        if (pAcronym == null || pAcronym.trim().isEmpty()) {
            throw new IllegalArgumentException("Kürzel der LehrerIn ist leer");
        }
        acronym = pAcronym.trim().substring(0, Math.min(Data.MAX_ACRONYM_LEN, pAcronym.length()));
    }
    
//    public boolean arbeitetZuViel(){
//    	int wochenStunden = hoursPerWeek.intValue();
//    	int arbeitetStunden = arbeitsZeit.intValue();
//    	if(wochenStunden < arbeitetStunden){
//    		return true;
//    	}
//    	return false;
//    }
    
//    public int kannNochSoVielArbeiten(){
//    	int i = hoursPerWeek.intValue() - arbeitsZeit.intValue();
//    	return i;
//    }
    
    @Override
    public String toString() {
        return String.format("Teacher [acronym=%s, name=%s, hpw=%.2f]", acronym, name, istZeit);
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
        return acronym.equals(that.acronym);
    }

    @Override
    public int hashCode() {
        return acronym.hashCode();
    }

	public int getErsatzZeit() {
		return ersatzZeit;
	}

	public void setErsatzZeit(final int pErsatzZeit) {
		if (pErsatzZeit <0) {
            throw new IllegalArgumentException("Negativer Wert");
        }
		ersatzZeit = pErsatzZeit;
	}

	public int getSollZeit() {
		return sollZeit;
	}

	public void setSollZeit(final int pSollZeit) {
		if (pSollZeit <0) {
            throw new IllegalArgumentException("Negativer Wert");
        }
		sollZeit = pSollZeit;
	}

	public int getIstZeit(){
		return istZeit;
	}
	
	public void setIstZeit(final int i){
		istZeit = i;
	}
	
	public ArrayList<Integer> getMoeglicheStundeninhalte() {
		return moeglicheStundeninhalte;
	}

	public void setMoeglicheStundeninhalte(final ArrayList<Integer> pMoeglicheStundeninhalte) {
		moeglicheStundeninhalte = pMoeglicheStundeninhalte;
	}

	public HashMap<Weekday,Time[]> getWunschZeiten() {
		return wunschZeiten;
	}

	public void setWunschZeiten(final HashMap<Weekday,Time[]> pWunschZeiten) {
		wunschZeiten = pWunschZeiten;
	}

	public boolean isGependelt() {
		return gependelt;
	}

	public void setGependelt(final boolean pGependelt) {
		gependelt = pGependelt;
	}

	public boolean isLehrer() {
		return lehrer;
	}

	public void setLehrer(final boolean pLehrer) {
		lehrer = pLehrer;
	}

	public int getId() {
		return id;
	}

	public void setId(final int pId) {
		id = pId;
	}

}
