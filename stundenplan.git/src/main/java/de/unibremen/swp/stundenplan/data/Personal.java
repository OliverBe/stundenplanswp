package de.unibremen.swp.stundenplan.data;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.Data;

public final class Personal {
    /**
     * Der Name dieser Person.
     */
    private String name;

    //primary
    private String kuerzel;
    
    private int sollZeit;
    
    // auf die richtige berechnung achten!
    private int istZeit;
    
    private int ersatzZeit;
    
    // schon einmal am tag gependelt true
    private boolean gependelt;
    
    //lehrer true, p���dagoge false
    private boolean lehrer;
    
    //integer referenziert zu stundeninhalt
    private ArrayList<String> moeglicheStundeninhalte = new ArrayList<String>();
    
    //array of time[2] geht irgendwie nicht
    private HashMap<Weekday, int[]> wunschzeiten = new HashMap<Weekday,int[]>();
 
    /**
     * Gibt den Namen dieses Lehrers zur��ck.
     * 
     * @return den Namen dieses Lehrers
     */
    public String getName() {
        return name;
    }

    /**
     * Konstruktor f���r Personal.
     *
     */
    public Personal(){
    }
    
    /**
     * Konstruktor f���r Personal.
     *
     */
    public Personal(String pName, String pKuerzel, int pSollZeit, int pIstZeit, int pErsatzZeit, boolean pGependelt, boolean pLehrer, ArrayList<String> pMoeglicheStundeninhalte) {
    	name = pName;
    	kuerzel = pKuerzel;
    	sollZeit = pSollZeit;
    	istZeit = pIstZeit;
    	ersatzZeit = pErsatzZeit;
    	gependelt = pGependelt;
    	lehrer = pLehrer;
    	moeglicheStundeninhalte = pMoeglicheStundeninhalte;
    }
    
    public Personal(String pName, String pKuerzel, int pSollZeit, int pIstZeit, int pErsatzZeit, boolean pGependelt, boolean pLehrer) {
    	name = pName;
    	kuerzel = pKuerzel;
    	sollZeit = pSollZeit;
    	istZeit = pIstZeit;
    	ersatzZeit = pErsatzZeit;
    	gependelt = pGependelt;
    	lehrer = pLehrer;
    }
    
    public Personal(String pName, String pKuerzel, int pSollZeit, int pErsatzzeit, boolean pLehrer, ArrayList<String> pMoeglicheStundeninhalte, HashMap<Weekday,int[]> pWunschzeiten) {
    	name = pName;
    	kuerzel = pKuerzel;
    	sollZeit = pSollZeit;
    	istZeit=0;
    	ersatzZeit=pErsatzzeit;
    	gependelt=false;
    	lehrer = pLehrer;
    	wunschzeiten=pWunschzeiten;
    	moeglicheStundeninhalte = pMoeglicheStundeninhalte;
    }
    
    /**
     * Setzt den Namen dieser LehrerIn auf den ��bergebenen Namen. Falls der Name l��nger als
     * {@linkplain Data#MAX_NORMAL_STRING_LEN} Zeichen ist, wird er entsprechend gek��rzt. F��hrende und folgende
     * Leerzeichen werden entfernt. L��st eine {@link IllegalArgumentException} aus, falls der Name leer ist.
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
     * Gibt das K��rzel dieser LehrerIn zur��ck.
     * 
     * @return das K��rzel dieses LehrerIn
     */
    public String getKuerzel() {
        return kuerzel;
    }

    /**
     * Setzt das K��rzel dieser LehrerIn auf das ��bergebene K��rzel. Falls das K��rzel l��nger als
     * {@linkplain Data#MAX_kuerzel_LEN} Zeichen ist, wird es entsprechend gek��rzt. F��hrende und folgende
     * Leerzeichen werden entfernt. L��st eine {@link IllegalArgumentException} aus, falls das K��rzel leer ist.
     * 
     * Die systemweite Eindeutigkeit des K��rzels wird hier NICHT gepr��ft!
     * 
     * @param pkuerzel
     *            das neue K��rzel dieser LehrerIn
     */
    public void setKuerzel(final String pKuerzel) {
        if (pKuerzel == null || pKuerzel.trim().isEmpty()) {
            throw new IllegalArgumentException("K��rzel der LehrerIn ist leer");
        }
        kuerzel = pKuerzel.trim().substring(0, Math.min(Data.MAX_KUERZEL_LEN, pKuerzel.length()));
    }
    
    @Override
    public String toString() {
        return String.format("Kuerzel=%s, Name=%s, SollZeit=%d", kuerzel, name, sollZeit);
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
	
	public ArrayList<String> getMoeglicheStundeninhalte() {
		return moeglicheStundeninhalte;
	}
	
	public String getmSI(){
		StringBuilder sb = new StringBuilder();
	    for (String st : moeglicheStundeninhalte) { 
	        sb.append('\'').append(st).append('\'').append(',');
	    }
	    if (moeglicheStundeninhalte.size() != 0){ sb.deleteCharAt(sb.length()-1);}
	    return sb.toString();
	}

	public void setMoeglicheStundeninhalte(final ArrayList<String> pMoeglicheStundeninhalte) {
		moeglicheStundeninhalte = pMoeglicheStundeninhalte;
	}

	public HashMap<Weekday,int[]> getWunschzeiten() {
		return wunschzeiten;
	}

	public void setWunschZeiten(final HashMap<Weekday, int[]> pWunschzeiten) {
		wunschzeiten = pWunschzeiten;
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
}
