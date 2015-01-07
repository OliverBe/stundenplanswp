package de.unibremen.swp.stundenplan.data;

public class Stundeninhalt {
    /**
     * Die eindeutige, von der unterliegenden Persistenzschicht automatisch erzeugte ID.
     */
    
    private String name;
    
    private String kuerzel;
    
    private int regeldauer;
    
    // 0 ist pause, 1 ist leicht, 2 ist schwer
    private int rhythmusTyp;

    public Stundeninhalt(final String pName, final String pKuerzel, final int pRegeldauer, final int pRhythmusTyp){
    	name=pName;
    	kuerzel=pKuerzel;
    	regeldauer=pRegeldauer;
    	rhythmusTyp=pRhythmusTyp;
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
	
	public int getRegeldauer() {
		return regeldauer;
	}
	
	public int getRhythmustyp() {
		return rhythmusTyp;
	}
	@Override
    public String toString() {
        return String.format("%s, %s, %d", name, kuerzel, regeldauer);
    }
}
