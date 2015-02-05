package de.unibremen.swp.stundenplan.data;

/*
 * Copyright 2014 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;

import de.unibremen.swp.stundenplan.config.Weekday;

/**
 * Entspricht einer Schulklasse. Eine Schulklasse hat einen Namen.
 * 
 */
public final class Schoolclass extends Jahrgang{

    /**
     * Der Name dieser Schulklasse.
     */
    //primary
    private String name;
    
    private Room klassenraum;
    
    private ArrayList<String> klassenlehrer = new ArrayList<String>(); 
    
    /**
	 * Ist die Schulklasse schon einmal an einem Tag gependelt, true, wenn nicht, dann false
	 */
	private HashMap<Weekday, Boolean> gependelt = new HashMap<Weekday, Boolean>();

    public Schoolclass(String pName, int pJahrgang, Room pKlassenraum, ArrayList<String> pKlassenlehrer, HashMap<String,Integer> pStundenbedarf, HashMap<Weekday, Boolean> pGependelt) {
		super(pJahrgang, pStundenbedarf);
    	name = pName;
		klassenraum = pKlassenraum;
		klassenlehrer = pKlassenlehrer;
		setGependelt(pGependelt);
	}

	/**
     * Gibt den Namen dieser Schulklasse zur��ck.
     * 
     * @return den Namen dieser Schulklasse
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen dieser Schulklasse auf den gegebenen Wert. Ein Parameterwert von {@code null} wird ignoriert.
     * 
     * @param pName
     *            der neue Name dieser Schulklasse (falls nicht {@code null}
     */
    public void setName(final String pName) {
        name = pName;
    }
    
    public Room getKlassenraum() {
    	return klassenraum;
    }
    
    public void setKlassenraum(Room pKlassenraum) {
    	klassenraum = pKlassenraum;
    }
    
    public ArrayList<String> getKlassenlehrer() {
    	return klassenlehrer;
    }
    
    public void setKlassenlehrer(ArrayList<String> pKlassenlehrer) {
    	klassenlehrer = pKlassenlehrer;
    }
    
    @Override
    public boolean equals(Object psc){
    	if(psc instanceof Schoolclass){
    		Schoolclass sc = (Schoolclass) psc;
    	return name.equals(sc.getName());
    	}
    	return false;
    }
    
    @Override
    public String toString() {
    	return ("Name=" + name + ", Jahrgang=" + getJahrgang() + ", Klassenraum=(" + klassenraum + ")");
    }

	public HashMap<Weekday, Boolean> getGependelt() {
		return gependelt;
	}

	public void setGependelt(HashMap<Weekday, Boolean> gependelt) {
		this.gependelt = gependelt;
	}
}
