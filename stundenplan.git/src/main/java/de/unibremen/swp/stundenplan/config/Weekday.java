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


/**
 * Eine Aufzählung von relevanten Wochentagen für einen Stundenplan.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 */
public enum Weekday {

    /**
     * Der Wochentag Montag.
     */
    MONDAY("Montag", 0),

    /**
     * Der Wochentag Dienstag.
     */
    TUESDAY("Dienstag", 1),

    /**
     * Der Wochentag Mittwoch.
     */
    WEDNESDAY("Mittwoch", 2),

    /**
     * Der Wochentag Donnerstag.
     */
    THURSDAY("Donnerstag", 3),

    /**
     * Der Wochentag Freitag.
     */
    FRIDAY("Freitag", 4),
    
    /**
     * Der Wochentag Samstag.
     */
    SATURDAY("Samstag", 5),
    
    /**
     * Der Wochentag Sonntag.
     */
    SUNDAY("Sonntag", 6);

    /**
     * Der Anzeigename dieses Wochentages.
     */
    private final String displayName;

    /**
     * Die Ordinalzahl, die diesem Wochentag zugeordnet ist.
     */
    private final int ordinal;

    /**
     * Erzeugt einen neuen Wochentag mit dem gegebenen Anzeigenamen und der gegebenen Ordinalzahl. Da es sich um einen
     * privaten Konstruktor handelt, werden die Parameterwerte nicht auf Plausibilität geprüft.
     * 
     * @param pDisplayName
     *            der Anzeigename für den Wochentag
     * @param pOrdinal
     *            die Ordinalzahl für den neuen Wochentag
     */
    private Weekday(final String pDisplayName, final int pOrdinal) {
        displayName = pDisplayName;
        ordinal = pOrdinal;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Gibt die Ordinalzahl dieses Wochentages zurück.
     * 
     * @return die Ordinalzahl dieses Wochentages
     */
    public int getOrdinal() {
        return ordinal;
    }
}
