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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entspricht einer Schulklasse. Eine Schulklasse hat einen Namen.
 * 
 * @author D. Lüdemann
 * @version 0.1
 */
public final class Schoolclass extends Jahrgang{

    /**
     * Der Name dieser Schulklasse.
     */
    //primary
    private String name;
    
    private Room klassenraum;
    
    private ArrayList<String> klassenlehrer = new ArrayList<String>();
    
    //falls die klasse einen speziellen bedarf hat
//    private HashMap<String, Integer> stundenbedarf = new HashMap<String, Integer>();
    
    private ArrayList<Planungseinheit> planungseinheiten;
    
    public Schoolclass() {
    	super();
    }

    public Schoolclass(String pName, int pJahrgang, Room pKlassenraum, ArrayList<String> pKlassenlehrer, HashMap<String,Integer> pStundenbedarf) {
		super(pJahrgang, pStundenbedarf);
    	name = pName;
		klassenraum = pKlassenraum;
		klassenlehrer=pKlassenlehrer;
	}

	/**
     * Gibt den Namen dieser Schulklasse zurück.
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
    public String toString() {
    	return ("Name=" + name + ", Jahrgang=" + getJahrgang() + ", Klassenraum=(" + klassenraum + ")");
    }
}
