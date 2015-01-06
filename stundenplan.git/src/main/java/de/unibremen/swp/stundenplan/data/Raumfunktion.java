package de.unibremen.swp.stundenplan.data;

import javax.naming.InvalidNameException;

public class Raumfunktion {
	private String name;

	public Raumfunktion(){
		name="Gamecuberaum";
	}
	
	public Raumfunktion(final String pName){
		if(pName==null || pName=="" )
			try {
				if(pName==null || pName=="" ) throw new InvalidNameException();
			} catch (InvalidNameException e) {
				e.printStackTrace();
			}
		name=pName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String pName) {
		if(pName==null)
			try {
				if(pName==null) throw new InvalidNameException();
			} catch (InvalidNameException e) {
				e.printStackTrace();
			}
	}
	
	@Override
    public String toString() {
        return String.format(name);
    }
	
}
