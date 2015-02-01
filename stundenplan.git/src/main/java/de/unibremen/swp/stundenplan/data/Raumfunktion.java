package de.unibremen.swp.stundenplan.data;

import java.util.ArrayList;

public class Raumfunktion {
	
	private String name;
	
	//kuerzel von Stundeninhalten
	private ArrayList<String> stundeninhalte;

	public Raumfunktion(){
		name="Gamecuberaum";
	}
	
	public Raumfunktion(final String pName, final ArrayList<String> pStundeninhalte){
		name=pName;
		setStundeninhalte(pStundeninhalte);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name=pName;
	}
	
	@Override
    public String toString() {
        return String.format(name);
    }

	public ArrayList<String> getStundeninhalte() {
		return stundeninhalte;
	}
	
	public String getmSI(){
		StringBuilder sb = new StringBuilder();
	    for (String st : stundeninhalte) { 
	    	sb.append(st);
	        sb.append(',');
	    }
	    if (stundeninhalte.size() != 0){ sb.deleteCharAt(sb.length()-1);}
	    return sb.toString();
	}

	public void setStundeninhalte(ArrayList<String> pStundeninhalte) {
		stundeninhalte = pStundeninhalte;
	}
	
}
