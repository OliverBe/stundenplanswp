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
package de.unibremen.swp.stundenplan.config;

import java.io.IOException;


/**
 * Eine Aufzaehlung von relevanten Wochentagen fuer einen Stundenplan.
 * 
 * @author D. Luedemann, K. Hoelscher
 * @version 0.1
 * @editor Oliver, Fathan
 */
public enum Weekday {

    /**
     * Der Wochentag Montag.
     */
    MONDAY("Montag", 0, Config.MONDAY_STRING ,Config.MONDAY),

    /**
     * Der Wochentag Dienstag.
     */
    TUESDAY("Dienstag", 1, Config.TUESDAY_STRING ,Config.TUESDAY),

    /**
     * Der Wochentag Mittwoch.
     */
    WEDNESDAY("Mittwoch", 2, Config.WEDNESDAY_STRING ,Config.WEDNESDAY),

    /**
     * Der Wochentag Donnerstag.
     */
    THURSDAY("Donnerstag", 3, Config.THURSDAY_STRING ,Config.THURSDAY),

    /**
     * Der Wochentag Freitag.
     */
    FRIDAY("Freitag", 4, Config.FRIDAY_STRING, Config.FRIDAY),
    
    /**
     * Der Wochentag Samstag.
     */
    SATURDAY("Samstag", 5, Config.SATURDAY_STRING, Config.SATURDAY),
    
    /**
     * Der Wochentag Sonntag.
     */
    SUNDAY("Sonntag", 6, Config.SUNDAY_STRING, Config.SUNDAY);

    /**
     * Der Anzeigename dieses Wochentages.
     */
    private final String displayName;

    /**
     * Die Ordinalzahl, die diesem Wochentag zugeordnet ist.
     */
    private final int ordinal;

    /**
     * Speichert ob an dem Tag unterricht stattfindet.
     */
    @SuppressWarnings("unused")
	private boolean isSchoolday;
    
    /**
     * 
     */
    private String daystring;
    
    private String dayvalue;
    
    /**
     * Erzeugt einen neuen Wochentag mit dem gegebenen Anzeigenamen und der gegebenen Ordinalzahl. Da es sich um einen
     * privaten Konstruktor handelt, werden die Parameterwerte nicht auf Plausibilituet geprueft.
     * 
     * @param pDisplayName
     *            der Anzeigename fuer den Wochentag
     * @param pOrdinal
     *            die Ordinalzahl fuer den neuen Wochentag
     */
    private Weekday(final String pDisplayName, final int pOrdinal, final String pDaystring, final String pDayvalue) {
    	try {
			Config.init(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
        displayName = pDisplayName;
        ordinal = pOrdinal;
        daystring = pDaystring;
        dayvalue = pDayvalue;
        isSchoolday = Boolean.parseBoolean(Config.getString(pDaystring,pDayvalue));
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Gibt die Ordinalzahl dieses Wochentages zurueck.
     * 
     * @return die Ordinalzahl dieses Wochentages
     */
    public int getOrdinal() {
        return ordinal;
    }
    
    /**
     *  Gibt zurueck ob Tag verplant.
     */
    public boolean isSchoolday() {
    	return Boolean.parseBoolean(Config.getString(daystring,dayvalue));
    }
    
    public void switchDay(final boolean pDay){
    	isSchoolday=pDay;
    }
    
    public static Weekday getDay(int ordinal) {
    	switch(ordinal) {
    	case 0:
    		return MONDAY;
    	case 1:
    		return TUESDAY;
    	case 2:
    		return WEDNESDAY;
    	case 3:
    		return THURSDAY;
    	case 4:
    		return FRIDAY;
    	case 5:
    		return SATURDAY;
    	case 6:
    		return SUNDAY;
    	default:
    		throw new IllegalArgumentException("Tag nicht vorhanden!");
    	}
    }
    
    public static Weekday getDay(String s) {
    	switch(s) {
    	case "Montag":
    		return MONDAY;
    	case "Dienstag":
    		return TUESDAY;
    	case "Mittwoch":
    		return WEDNESDAY;
    	case "Donnerstag":
    		return THURSDAY;
    	case "Freitag":
    		return FRIDAY;
    	case "Samstag":
    		return SATURDAY;
    	case "Sonntag":
    		return SUNDAY;
    	default:
    		throw new IllegalArgumentException("Tag nicht vorhanden!");
    	}
    }
}
