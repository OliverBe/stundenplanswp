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

	public void setStundeninhalte(ArrayList<String> pStundeninhalte) {
		stundeninhalte = pStundeninhalte;
	}
	
}
