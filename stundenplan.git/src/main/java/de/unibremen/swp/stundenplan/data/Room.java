package de.unibremen.swp.stundenplan.data;

import java.util.ArrayList;
import de.unibremen.swp.stundenplan.logic.RaumManager;

public final class Room{

    private String name;
    
    private int gebaeude;
    
    //name der rf
    private ArrayList<String> moeglicheFunktionen; 

    public Room() {
    	
    }
    
    public Room(String pName, int pGebaeude, ArrayList<String> pMoeglicheFunktionen){
    	name = pName;
    	gebaeude = pGebaeude;
    	moeglicheFunktionen = pMoeglicheFunktionen;
    }
   
    public Room(String pName, int pGebaeude){
    	name = pName;
    	gebaeude = pGebaeude;
    }
    
    /**
     * Gibt den Namen dieses Raumes zur��ck.
     * 
     * @return den Namen dieses Raumes
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen dieses Raumes auf den gegebenen Wert. Ein Parameterwert von {@code null} wird ignoriert.
     * 
     * @param pName
     *            der neue Name dieses Raumes (falls nicht {@code null}
     */
    public void setName(final String pName) {
        name = pName;
    }

	public int getGebaeude() {
		return gebaeude;
	}

	public void setGebaeude(final int pGebaeude) {
		gebaeude = pGebaeude;
	}

	public ArrayList<String> getMoeglicheFunktionen() {
		return moeglicheFunktionen;
	}

	public void setMoeglicheFunktionen(final ArrayList<String> pMoeglicheFunktionen) {
		moeglicheFunktionen = pMoeglicheFunktionen;
	}
	
	public void addMoeglicheFunktionen(final String funktion){
		moeglicheFunktionen.add(funktion);
	}
	
	public String getmSI(){
		StringBuilder sb = new StringBuilder();
	    for (String st : moeglicheFunktionen) { 
	    	Raumfunktion rf = RaumManager.getRaumfunktionByName(st);
	    	sb.append(rf.getmSI());
	    	sb.append(',');
	    }
	    if (moeglicheFunktionen.size() != 0){ sb.deleteCharAt(sb.length()-1);}
	    return sb.toString();
	}
	
	@Override
    public boolean equals(Object pr){
    	if(pr instanceof Room){
    		Room r = (Room) pr;
    	return name.equals(r.getName());
    	}
    	return false;
    }
	
	@Override
    public String toString() {
        return String.format("Raum: "+name+" Gebaeude: "+ gebaeude);
    }
}
