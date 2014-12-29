package de.unibremen.swp.stundenplan.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Stundeninhalt {
    /**
     * Die eineindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = 3137139574206115533L;

    /**
     * Die eindeutige, von der unterliegenden Persistenzschicht automatisch erzeugte ID.
     */
    
    private String name;
    
    private String kuerzel;
    
    private int regeldauer;
    
    // 0 ist pause, 1 ist leicht, 2 ist schwer
    private int rythmusTyp;

    public Stundeninhalt(final String pName, final String pKuerzel, final int pRegeldauer, final int pRythmusTyp){
    	name=pName;
    	kuerzel=pKuerzel;
    	regeldauer=pRegeldauer;
    	rythmusTyp=pRythmusTyp;
    }
	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = pName;
	}

	public String getKuerzel() {
		return kuerzel;
	}

	public void setKuerzel(String pKuerzel) {
		kuerzel = pKuerzel;
	}
}
