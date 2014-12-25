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
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.gui.Timeslot;

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
public final class Teacher implements Serializable {

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
    /**
     * Die minimale Zahl an Unterrichts- bzw. Arbeitsstunden, die jede LehrerIn leisten muss. Kann nicht unterschritten
     * werden.
     */
    @Transient
    public static final BigDecimal MIN_HOURS_PER_WEEK = BigDecimal.ZERO;

    /**
     * Die maximale Zahl an Unterrichts- bzw. Arbeitsstunden, die jede LehrerIn leisten muss. Kann nicht überschritten
     * werden.
     */
    @Transient
    public static final BigDecimal MAX_HOURS_PER_WEEK = new BigDecimal(40);

    /**
     * Derzeitige Arbeitszeit, in der der Lehrer eingeteilt ist.
     */
    @Column
    public BigDecimal arbeitsZeit = BigDecimal.ZERO;
    
    /**
     * Der Name dieser LehrerIn.
     */
    private String name;

    /**
     * Das Kürzel dieser LehrerIn. Ein Kürzel muss systemweit eindeutig sein.
     */
    @Id
    private String acronym;

    /**
     * Die Anzahl an Stunden, die eine LehrerIn maximal unterrichtet bzw. arbeitet. Aktuell maximal zweistellig mit 2
     * Nachkommastellen.
     */
    @Column(precision = PRECISION, scale = SCALE)
    private BigDecimal hoursPerWeek;
    
    /**
     * 
     */
    private Collection<Timeslot> timeslots;

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

    /**
     * Setzt die Unterrichts- bzw. Arbeitsstunden dieser LehrerIn auf den übergebenen Wert. Löst eine
     * {@link IllegalArgumentException} aus, falls der Parameterwert kleiner als {@linkplain Teacher#MIN_HOURS_PER_WEEK}
     * oder größer als {@linkplain Teacher#MAX_HOURS_PER_WEEK} ist.
     * 
     * @param pHoursPerWeek
     *            die neue Anzahl der Unterrichts- bzw. Arbeitsstunden dieser LehrerIn
     */
    public void setHoursPerWeek(final String pHoursPerWeek) {
        final BigDecimal hpW = new BigDecimal(pHoursPerWeek, new MathContext(2));
        if (hpW.compareTo(MIN_HOURS_PER_WEEK) < 0 || hpW.compareTo(MAX_HOURS_PER_WEEK) > 0) {
            throw new IllegalArgumentException("Arbeitsstunden außerhalb der erlaubten Grenzen");
        }
        hoursPerWeek = hpW;
    }

    /**
     * Gibt den Namen dieses Lehrers zurück.
     * 
     * @return den Namen dieses Lehrers
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Anzahl an Unterrichts- bzw. Arbeitsstunden dieser LehrerIn zurück.
     * 
     * @return die Anzahl an Unterrichts- bzw. Arbeitsstunden dieser LehrerIn
     */
    public BigDecimal getHoursPerWeek() {
        return hoursPerWeek;
    }

    /**
     * Gibt die Anzahl an angeteilten Unterrichts- bzw. Arbeitsstunden dieser LehrerIn zur�ck.
     * 
     * @return die Anzahl an Unterrichts- bzw. Arbeitsstunden, f�r die dieserLehrerIn eingeteilt ist.
     */
    public BigDecimal getWorkingHours(){
    	return arbeitsZeit;
    }
    
    public void addWorkingHours(final int i){
    	BigDecimal b = new BigDecimal(i);
    	arbeitsZeit = arbeitsZeit.add(b);
    }
    
    public boolean arbeitetZuViel(){
    	int wochenStunden = hoursPerWeek.intValue();
    	int arbeitetStunden = arbeitsZeit.intValue();
    	if(wochenStunden < arbeitetStunden){
    		return true;
    	}
    	return false;
    }
    
    public int kannNochSoVielArbeiten(){
    	int i = hoursPerWeek.intValue() - arbeitsZeit.intValue();
    	return i;
    }
    
    @Override
    public String toString() {
        return String.format("Teacher [acronym=%s, name=%s, hpw=%.2f]", acronym, name, hoursPerWeek);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Teacher)) {
            return false;
        }
        final Teacher that = (Teacher) other;
        return acronym.equals(that.acronym);
    }

    @Override
    public int hashCode() {
        return acronym.hashCode();
    }
    
    public void addTimeslot(Timeslot timeslot) {
    	timeslots.add(timeslot);
    }
    
    public Collection<Timeslot> getTimeslots() {
    	return timeslots;
    }
}
