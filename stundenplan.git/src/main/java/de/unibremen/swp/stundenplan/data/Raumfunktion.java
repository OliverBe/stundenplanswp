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
		stundeninhalte=pStundeninhalte;
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
	
}
