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
package de.unibremen.swp.stundenplan.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Entspricht einer Zeiteinheit. Eine Zeiteinheit ist einem Tagesplan zugeordnet und hat eine startzeit. Die Dauer einer
 * solchen Zeiteinheit ist konfigurierbar und per Default auf {@linkplain Config#TIMESLOT_LENGTH_DEFAULT} festgelegt.
 * 
 * @author Fathan Vidjaja
 * @version 0.1
 * 
 */
public final class Timeslot implements Serializable {

    /**
     * Die Dauer aller Zeiteinheiten in Minuten.
     */
    public static final int LENGTH = Config.getInt(Config.TIMESLOT_LENGTH_STRING,
            Config.TIMESLOT_LENGTH);

 
    /**
     * Die startzeit dieses Timeslots. Die Einträge für {@linkplain Calendar#HOUR} und {@linkplain Calendar#MINUTE}
     * müssen entsprechend gesetzt sein.
     */
    private Calendar startzeit;

    private Weekday wochentag;
    
    private String stundeninhaltetext = "";
    private String personaltext = "";
    private String raumtext = "";
    private String klassetext = "";
    private int pe = -1; 
    private int rhytm = -1;
    
    /**
     * Erzeugt eine neue Zeiteinheit.
     */
    public Timeslot(Calendar pStart, Weekday pWeekday) {
    	startzeit = pStart;
    	wochentag = pWeekday;
    }
    
    /**
     * Erzeugt eine neue Zeiteinheit.
     */
    public Timeslot(Weekday pWeekday) {
    	wochentag = pWeekday;
    }

    /**
     * Setzt die startzeit dieser Zeiteineit auf den übergebenen Wert. Die Einträge für {@linkplain Calendar#HOUR} und
     * {@linkplain Calendar#MINUTE} müssen entsprechend gesetzt sein. Der Parameterwert wird nicht auf Plausibilität
     * geprüft - {@code null} wird allerdings ignoriert.
     * 
     * @param pstartzeit
     *            startzeit dieser Zeiteinheit (es sind nur die Einträge für {@linkplain Calendar#HOUR} und
     *            {@linkplain Calendar#MINUTE} relevant
     */
    public void setstartzeit(final Calendar pstartzeit) {
        if (pstartzeit != null) {
            startzeit = pstartzeit;
        }
    }
    
    public Weekday getDay(){
    	return wochentag;
    }
    
    public int getsHour(){
    	return startzeit.get(Calendar.HOUR_OF_DAY);
    }
    
    public int getsMinute(){
    	return startzeit.get(Calendar.MINUTE);
    }
    
    
    public int geteHour(){
    	final Calendar newCal = Calendar.getInstance();
    	newCal.setTimeInMillis(startzeit.getTimeInMillis());
        newCal.add(Calendar.MINUTE, timeslotlength());
        return newCal.get(Calendar.HOUR_OF_DAY);
    }
    
    public int geteMinute(){
    	final Calendar newCal = Calendar.getInstance();
    	newCal.setTimeInMillis(startzeit.getTimeInMillis());
    	newCal.add(Calendar.MINUTE, timeslotlength());
    	return newCal.get(Calendar.MINUTE);
    }
    
    /**
     * Gibt die startzeit dieser Zeiteinheit im Format <stunde>:<minute> mit evtl. führenden Nullen zurück oder einen
     * leeren String, falls die startzeit noch nicht initialisiert wurde.
     * 
     * @return die startzeit dieser Zeiteinheit im Format <stunde>:<minute> mit evtl. führenden Nullen
     */
    public String getTimeDisplay() {
        if (startzeit == null) {
            return "";
        }
        final int hour = startzeit.get(Calendar.HOUR);
        final int minute = startzeit.get(Calendar.MINUTE);
        return String.format("%02d:%02d", hour, minute);
    }
    
    public String getStundeninhalttext(){
    	return stundeninhaltetext;
    }
    
    public void setStundeninhalttext(final Planungseinheit pPE){
    	stundeninhaltetext = pPE.stundenInhaltetoString();
    }
    
    public String getPersonaltext(){
    	return personaltext;
    }

    public void setPersonaltext(final Planungseinheit pPE){
    	personaltext = pPE.personaltoString();
    }
    
    public String getRaeumetext(){
    	return raumtext;
    }
    
    public void setRaumtext(final Planungseinheit pPE){
    	raumtext = pPE.roomstoString();
    }
    
    public String getKlassentext(){
    	return klassetext;
    }
    
    public void setKlassentext(final Planungseinheit pPE){
    	klassetext = pPE.schoolclassestoString();
    }
    
    public void setpe(final Planungseinheit pPE){
    	if(pPE== null){throw new IllegalArgumentException("number must greater than 0");}
    	pe = pPE.getId();
    }
    
    public int getpe(){
    	return pe;
    }
    
    public void setrhytm(final Planungseinheit pPE){
    	if(pPE == null){throw new IllegalArgumentException("Argument must be not null");}
    	if(pPE.getStundeninhalte().size()==0){return;}
    	rhytm = DataStundeninhalt.getStundeninhaltByKuerzel(pPE.getStundeninhalte().get(0)).getRhythmustyp();
    }
    
    public int getrhytm(){
    	return rhytm;
    }
    
    public static int timeslotlength(){
    	return Config.getInt(Config.TIMESLOT_LENGTH_STRING,
                Config.TIMESLOT_LENGTH);
    }
    
}
