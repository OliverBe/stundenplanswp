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
package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.Data;

/**
 * Repräsentiert eine LehrerIn. Eine LehrerIn hat einen Namen, ein systemweit eindeutiges Kürzel und eine Anzahl von
 * Stunden, die sie unterrichtet bzw. arbeitet.
 * 
 * Zwei LehrerInnen werden als gleich angesehen, wenn sie das gleiche Kürzel haben.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 */
@Entity
public final class Personal implements Serializable {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = -2391687797016927732L;

    /**
     * Rechengenauigkeit der verwendeten Kommazahlen.
     */
    private static final int PRECISION = 4;

    /**
     * Anzahl der Nachkommstellen bei Kommazahlen.
     */
    private static final int SCALE = 2;
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    /**
     * Der Name dieser LehrerIn.
     */
    private String name;

    /**
     * Das Kürzel dieser LehrerIn. Ein Kürzel muss systemweit eindeutig sein.
     */
    @Id
    @Column(length = Data.MAX_ACRONYM_LEN)
    private String acronym;

    //integer referenziert zu stundeninhalt
    private ArrayList<Integer> moeglicheStundeninhalte;
    
    private int sollZeit;
    
    // au die richtige berechnung achten!
    private int istZeit;
    
    private int ersatzZeit;
    
    //array of time[2] geht irgendwie nicht
    private HashMap<Weekday,Time[]> wunschZeiten;
    
    private boolean gependelt;
    
    private boolean lehrer;
 
    /**
     * Gibt den Namen dieses Lehrers zurück.
     * 
     * @return den Namen dieses Lehrers
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen dieser LehrerIn auf den übergebenen Namen. Falls der Name länger als
     * {@linkplain Data#MAX_NORMAL_STRING_LEN} Zeichen ist, wird er entsprechend gekürzt. Führende und folgende
     * Leerzeichen werden entfernt. Löst eine {@link IllegalArgumentException} aus, falls der Name leer ist.
     * 
     * @param pName
     *            der neue Name dieser LehrerIn
     */
    public void setName(final String pName) {
        if (pName == null || pName.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name der LehrerIn ist leer");
        }
        name = pName.trim().substring(0, Math.min(Data.MAX_NORMAL_STRING_LEN, pName.length()));
    }

    /**
     * Gibt das Kürzel dieser LehrerIn zurück.
     * 
     * @return das Kürzel dieses LehrerIn
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * Setzt das Kürzel dieser LehrerIn auf das übergebene Kürzel. Falls das Kürzel länger als
     * {@linkplain Data#MAX_ACRONYM_LEN} Zeichen ist, wird es entsprechend gekürzt. Führende und folgende
     * Leerzeichen werden entfernt. Löst eine {@link IllegalArgumentException} aus, falls das Kürzel leer ist.
     * 
     * Die systemweite Eindeutigkeit des Kürzels wird hier NICHT geprüft!
     * 
     * @param pAcronym
     *            das neue Kürzel dieser LehrerIn
     */
    public void setAcronym(final String pAcronym) {
        if (pAcronym == null || pAcronym.trim().isEmpty()) {
            throw new IllegalArgumentException("Kürzel der LehrerIn ist leer");
        }
        acronym = pAcronym.trim().substring(0, Math.min(Data.MAX_ACRONYM_LEN, pAcronym.length()));
    }
    
//    public boolean arbeitetZuViel(){
//    	int wochenStunden = hoursPerWeek.intValue();
//    	int arbeitetStunden = arbeitsZeit.intValue();
//    	if(wochenStunden < arbeitetStunden){
//    		return true;
//    	}
//    	return false;
//    }
    
//    public int kannNochSoVielArbeiten(){
//    	int i = hoursPerWeek.intValue() - arbeitsZeit.intValue();
//    	return i;
//    }
    
    @Override
    public String toString() {
        return String.format("Teacher [acronym=%s, name=%s, hpw=%.2f]", acronym, name, istZeit);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Personal)) {
            return false;
        }
        final Personal that = (Personal) other;
        return acronym.equals(that.acronym);
    }

    @Override
    public int hashCode() {
        return acronym.hashCode();
    }

}
