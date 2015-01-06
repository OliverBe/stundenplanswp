package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class Room implements Serializable {

    private static final long serialVersionUID = 3137139574206115533L;

    private int id;

    private String name;
    
    private int gebaeude;
    
    //name der rf
    private ArrayList<String> moeglicheFunktionen; 

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
     * Gibt den Namen dieses Raumes zurück.
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

	public int getId() {
		return id;
	}

	public void setId(final int pId) {
		id = pId;
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
	
	@Override
    public String toString() {
        return String.format("Name=%s, Gebaeude=%d", name, gebaeude);
    }
}
