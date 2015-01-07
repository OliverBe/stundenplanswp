package de.unibremen.swp.stundenplan.data;

import java.util.HashMap;

public class Jahrgang {
	
	
    private int jahrgang;
    
    private HashMap<String, Integer> stundenbedarf;
    
    public Jahrgang(){
    	
    }
    
    public Jahrgang(int pJahrgang, HashMap<String, Integer> pStundenbedarf) {
    	setJahrgang(pJahrgang);
    	setStundenbedarf(pStundenbedarf);
    }

	public int getJahrgang() {
		return jahrgang;
	}

	public void setJahrgang(int jahrgang) {
		this.jahrgang = jahrgang;
	}

	public HashMap<String, Integer> getStundenbedarf() {
		return stundenbedarf;
	}

	public void setStundenbedarf(HashMap<String, Integer> stundenbedarf) {
		this.stundenbedarf = stundenbedarf;
	}
}
