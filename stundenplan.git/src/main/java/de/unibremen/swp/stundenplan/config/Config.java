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
 * @author D. Lüdemann, K. Hölscher
 * @version 1.0
 * @editor Oliver, Fathan
 */
public final class Config {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    /**
     * Die Properties dieser Konfiguration.
     */
    private static PropertiesConfiguration propertiesConfig;

    /**
     * Zu verplanende Wochentage jeweils mit true,false angegeben
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
    
//    /**
//     * Die Anzahl an Zeiteinheiten, die alle Tagespläne per Default haben. Kann durch einen Eintrag in der
//     * Konfigurationsdatei überschrieben werden.
//     */
//    public static final int DAYTABLE_LENGTH_DEFAULT = 8;
//
//    /**
//     * Der Schlüssel für den Eintrag der Tagesplan-Anzahl in der Konfigurationsdatei.
//     */
//    public static final String DAYTABLE_LENGTH_STRING = "daytablelength";

    /**
     * Default-Wert für die Stunde der Startzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * überschrieben werden.
     */
    public static int DAY_STARTTIME_HOUR = 07;

    /**
     * Der Schlüssel für den Eintrag der Stunden-Startzeit in der Konfigurationsdatei.
     */
    public static final String DAY_STARTTIME_HOUR_STRING = "starthour";

    /**
     * Default-Wert für die Minute der Startzeit jedes Tagesplans. Kann durch einen Eintrag in der Konfigurationsdatei
     * überschrieben werden.
     */
    public static int DAY_STARTTIME_MINUTE = 00;

    /**
     * Der Schlüssel für den Eintrag der Stunden-Startzeit in der Konfigurationsdatei.
     */
    public static final String DAY_STARTTIME_MINUTE_STRING = "startminute";
    
    public static int DAY_ENDTIME_HOUR = 15;
    public static final String DAY_ENDTIME_HOUR_STRING = "endhour";
    
    public static int DAY_ENDTIME_MINUTE = 00;
    public static final String DAY_ENDTIME_MINUTE_STRING = "endminute";

    /**
     * Default-Wert für die Länge jeder Zeiteinheit eines Tagesplans in Minuten. Kann durch einen Eintrag in der
     * Konfigurationsdatei überschrieben werden.
     */
    public static int TIMESLOT_LENGTH = 10;
    
    /**
     * Wert f�r die Dauer der Pendelzeit.
     */
    public static int PENDELTIME = 15;
    
    /**
     * Wert f�r die Dauer der Pendelzeit.
     */
    public static String PENDELTIME_STRING = "pendeldauer";

    /**
     * Der Schlüssel für den Eintrag der Zeiteinheit-Länge in der Konfigurationsdatei.
     */
    public static final String TIMESLOT_LENGTH_STRING = "timeslotlength";
    
    /**
     * in minuten
     */
    public static int BACKUPINTERVALL = 60;

    public static final String BACKUPINTERVALL_STRING = "backupintervall";

    /**
     * Der Dateiname der Konfigurationsdatei.
     */
    private static final String PROPERTIES_FILE_NAME = "stundenplan.properties";

    /**
     * Privater Konstruktor, der die Instanziierung dieser Utility-Klasse verhindert.
     */
    private Config() {
    }

    /**
     * Erzeugt eine neue Konfiguration für den Stundenplan, indem die Konfigurationsdatei mit dem gegebenen Pfad
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
     * Erzeugt eine neue, leere Konfigurationsdatei für den Stundenplan und speichert diese unter
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
     * Gibt den Wert zum gegebenen Schlüssel der Konfiguration zurück. Falls es in der Konfiguration keinen
     * entsprechenden Schlüssel gibt, wird der gegebene Standardwert zurückgegeben.
     * 
     * @param pKey
     *            der Schlüssel des gesuchten Wertes
     * @param pDefaultValue
     *            der Standardwert, falls es den Schlüssel in der Konfiguration nicht gibt
     * @return den Wert zum gegebenen Schlüssel
     */
    public static String getString(final String pKey, final String pDefaultValue) {
        return propertiesConfig.getString(pKey, pDefaultValue);
    }

    /**
     * Gibt den Wert zum gegebenen Schlüssel zurück. Falls es in der Konfiguration keinen entsprechenden Schlüssel gibt,
     * wird der gegebene Standardwert zurückgegeben.
     * 
     * @param pKey
     *            der Schlüssel des gesuchten Wertes
     * @param pDefaultValue
     *            der Standardwert, falls es den Schlüssel in der Konfiguration nicht gibt
     * @return den Wert zum gegebenen Schlüssel
     */
    public static int getInt(final String pKey, final int pDefaultValue) {
        return propertiesConfig.getInt(pKey, pDefaultValue);
    }
    
    public static void setIntValue(final String pKey, final int pValue){
    	if((pKey == null || pKey.length() == 0))throw new IllegalArgumentException("There must be a key");
    	if(pKey.equals(TIMESLOT_LENGTH_STRING) && pValue <= 0)throw new IllegalArgumentException("Value of timeslot must be greater than zero");
    	propertiesConfig.setProperty(pKey, pValue);
    	try {
			propertiesConfig.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("saved");
    }
    
    public static void setStringValue(final String pKey, final String pValue){
    	if((pKey == null || pKey.length() == 0))throw new IllegalArgumentException("There must be a key");
    	if((pValue == null || pValue.length() == 0))throw new IllegalArgumentException("There must be a key");
    	propertiesConfig.setProperty(pKey, pValue);
    	try {
			propertiesConfig.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("saved");
    }
}
