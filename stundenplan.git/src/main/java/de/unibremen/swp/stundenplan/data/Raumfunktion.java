package de.unibremen.swp.stundenplan.data;

import javax.naming.InvalidNameException;

public class Raumfunktion {
	private String name;

	public Raumfunktion(final String pName) throws InvalidNameException{
		if(pName==null) throw new InvalidNameException();
		name=pName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = pName;
	}
	
	@Override
    public String toString() {
        return String.format(name);
    }
	
}
