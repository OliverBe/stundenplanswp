package de.unibremen.swp.stundenplan.data;

public class Raumfunktion {
	private String name;

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
