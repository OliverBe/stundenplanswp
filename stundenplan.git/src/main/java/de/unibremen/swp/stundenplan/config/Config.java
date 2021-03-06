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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Verwaltet die Konfiguration des Stundenplaners.
 * 
 * @author D. Luedemann, K. Hoelscher
 * @version 1.0
 * @editor Oliver, Fathan
 */
public final class Config {
    /**
     * Der Logger fuer diese Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    /**
     * Die Properties dieser Konfiguration.
     */
    private static PropertiesConfiguration propertiesConfig;
    /**
     * Zu verplanende Wochentage jeweils mit true und false angegeben
     */
    public static String MONDAY = "true";
    public final static String MONDAY_STRING = "Montag";
    public static String TUESDAY = "true";
    public final static String TUESDAY_STRING = "Dienstag";
    public static String WEDNESDAY = "true";
    public final static String WEDNESDAY_STRING = "Mittwoch";
    public static String THURSDAY = "true";
    public final static String THURSDAY_STRING = "Donnerstag";
    public static String FRIDAY = "true";
    public final static String FRIDAY_STRING = "Freitag";
    public static String SATURDAY = "false";
    public final static String SATURDAY_STRING = "Samstag";
    public static String SUNDAY = "false";
    public final static String SUNDAY_STRING = "Sonntag";
    /**
     * Default-Wert fuer die Stunde der Startzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * ueberschrieben werden.
     */
    public static int DAY_STARTTIME_HOUR = 07;
    /**
     * Der Schluessel fuer den Eintrag der Stunden-Startzeit in der Konfigurationsdatei.
     */
    public static final String DAY_STARTTIME_HOUR_STRING = "starthour";
    /**
     * Default-Wert fuer die Minuten der Startzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * ueberschrieben werden.
     */
    public static int DAY_STARTTIME_MINUTE = 00;
    /**
     * Der Schluessel fuer den Eintrag der Minuten-Startzeit in der Konfigurationsdatei.
     */
    public static final String DAY_STARTTIME_MINUTE_STRING = "startminute";
    /**
     * Default-Wert fuer die Stunde der Endzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * ueberschrieben werden.
     */
    public static int DAY_ENDTIME_HOUR = 15;
    /**
     * Der Schluessel fuer den Eintrag der Stunden-Endzeit in der Konfigurationsdatei.
     */
    public static final String DAY_ENDTIME_HOUR_STRING = "endhour";
    /**
     * Default-Wert fuer die Minuten der Endzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * ueberschrieben werden.
     */
    public static int DAY_ENDTIME_MINUTE = 00;
    /**
     * Der Schluessel fuer den Eintrag der Minuten-Endzeit in der Konfigurationsdatei.
     */
    public static final String DAY_ENDTIME_MINUTE_STRING = "endminute";
    /**
     * Default-Wert fuer die Laenge jeder Zeiteinheit eines Tagesplans in Minuten. Kann durch einen Eintrag in der
     * Konfigurationsdatei ueberschrieben werden.
     */
    public static int TIMESLOT_LENGTH = 10;
    /**
     * Der Schluessel fuer den Eintrag der Zeiteinheit-Laenge in der Konfigurationsdatei.
     */
    public static final String TIMESLOT_LENGTH_STRING = "timeslotlength";
    /**
     * Wert fuer die Dauer der Pendelzeit.
     */
    public static int PENDELTIME = 15;
    /**
     * Der Schluessel fuer den Eintrag der Pendelzeit in der Konfigurationsdatei.
     */
    public static String PENDELTIME_STRING = "pendeldauer";
    /**
     * Wert speichert, ob das Programm bereits gestartet ist.
     */
    public static int PROGRAM_STARTED = 0;
    /**
     * Der Schluessel fuer den Eintrag der "PROGRAM_STARTED"-Attribut in der Konfigurationsdatei.
     */
    public static final String PROGRAM_STARTED_STRING = "programstarted";
    /**
     * Die Standarddauer in Minuten bis wieder ein Backup erstellt wird.
     */
    public static int BACKUPINTERVALL = 60;
    /**
     * Der Schlussel fuer das Backupintervall in der Konfigurationsdatei.
     */
    public static final String BACKUPINTERVALL_STRING = "backupintervall";
    /**
     * Der Dateiname der Konfigurationsdatei.
     */
    private static final String PROPERTIES_FILE_NAME = "stundenplan.properties";
    /**
     * Privater Konstruktor, der die Instanziierung dieser Utility-Klasse verhindert.
     */
    private Config() {}

    /**
     * Erzeugt eine neue Konfiguration fuer den Stundenplan, indem die Konfigurationsdatei mit dem gegebenen Pfad
     * eingelesen wird. Falls dieser Pfad {@code null} ist, wird die Konfigurationsdatei mit dem Pfad
     * {@linkplain Config#PROPERTIES_FILE_NAME} eingelesen. Falls die Konfigurationsdatei nicht gefunden werden kann
     * oder fehlerhaft ist, wird eine neue, leere Konfigurationsdatei erzeugt.
     * 
     * @param pPath
     *            Pfad zur Konfigurationsdatei
     * @throws IOException
     *             falls eine neue leere Konfigurationsdatei nicht erzeugt werden kann
     */
    public static void init(final String pPath) throws IOException {
        String configPath;
        if (pPath == null) {
            configPath = PROPERTIES_FILE_NAME;
        } else {
            configPath = pPath;
        }
        try {
            propertiesConfig = new PropertiesConfiguration(configPath);
            
        } catch (ConfigurationException e) {
            createNewConfig();
            LOGGER.warn("Exception while creating new configuration object!", e);
            LOGGER.warn("creating new config file");
        }
    }

    /**
     * Erzeugt eine neue, leere Konfigurationsdatei fuer den Stundenplan und speichert diese unter
     * {@linkplain Config#PROPERTIES_FILE_NAME}.
     * 
     * @throws IOException
     *             falls die neue leere Konfigurationsdatei nicht erzeugt werden kann
     */
    private static void createNewConfig() throws IOException {
        propertiesConfig = new PropertiesConfiguration();
        try {
            propertiesConfig.save(PROPERTIES_FILE_NAME);
        } catch (ConfigurationException e) {
            LOGGER.error("Exception while creating new configuration: ", e);
            throw new IOException("Could not save new configuration in " + PROPERTIES_FILE_NAME, e);
        }
    }

    /**
     * Gibt den Wert zum gegebenen Schluessel der Konfiguration zurueck. Falls es in der Konfiguration keinen
     * entsprechenden Schluessel gibt, wird der gegebene Standardwert zurueckgegeben.
     * 
     * @param pKey
     *            der Schluessel des gesuchten Wertes
     * @param pDefaultValue
     *            der Standardwert, falls es den Schluessel in der Konfiguration nicht gibt
     * @return den Wert zum gegebenen Schluessel
     */
    public static String getString(final String pKey, final String pDefaultValue) {
        return propertiesConfig.getString(pKey, pDefaultValue);
    }

    /**
     * Gibt den Wert zum gegebenen Schluessel zurueck. Falls es in der Konfiguration keinen entsprechenden Schluessel gibt,
     * wird der gegebene Standardwert zurueckgegeben.
     * 
     * @param pKey
     *            der Schluessel des gesuchten Wertes
     * @param pDefaultValue
     *            der Standardwert, falls es den Schluessel in der Konfiguration nicht gibt
     * @return den Wert zum gegebenen Schluessel
     */
    public static int getInt(final String pKey, final int pDefaultValue) {
        return propertiesConfig.getInt(pKey, pDefaultValue);
    }
    
    /**
     * Methode setzt den Integer fuer den gegebenen Key auf den gegebenen Integer.
     * 
     * @param pKey
     * 		der Key bei dem der Wert gesetzt werden soll
     * @param pValue
     * 		der Integer-Wert, der den alten ersetzen soll
     */
    public static void setIntValue(final String pKey, final int pValue){
    	if((pKey == null || pKey.length() == 0))throw new IllegalArgumentException("There must be a key");
    	if(pKey.equals(TIMESLOT_LENGTH_STRING) && pValue <= 0)throw new IllegalArgumentException("Value of timeslot must be greater than zero");
    	propertiesConfig.setProperty(pKey, pValue);
    	try {
			propertiesConfig.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Methode setzt den String fuer den gegebenen Key auf den gegebenen String.
     * 
     * @param pKey
     * 		der Key bei dem der Wert gesetzt werden soll
     * @param pValue
     * 		der String-Wert, der den alten ersetzen soll
     */
    public static void setStringValue(final String pKey, final String pValue){
    	if((pKey == null || pKey.length() == 0))throw new IllegalArgumentException("There must be a key");
    	if((pValue == null || pValue.length() == 0))throw new IllegalArgumentException("There must be a key");
    	propertiesConfig.setProperty(pKey, pValue);
    	try {
			propertiesConfig.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
    }
}
