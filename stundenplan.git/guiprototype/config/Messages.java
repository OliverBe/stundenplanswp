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
package config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Verwaltet die Nachrichtentexte für Lokalisierung. Liest die in der Konstanten {@linkplain Messages#BUNDLE_NAME}
 * festgelegte Properties-Datei ein und gibt bei Bedarf die dort abgelegten Texte zu den entsprechenden Schlüssel
 * zurück.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 */
public final class Messages {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

    /**
     * Der Basisname der Datei, in der die Nachrichtentexte abgelegt sind.
     */
    private static final String BUNDLE_NAME = "de.unibremen.swp.stundenplan.config.messages";

    /**
     * Das ResourceBundle zur Datei mit den Nachrichtentexten.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Privater Konstruktor, der die Instanziierung dieser Utility-Klasse verhindert.
     */
    private Messages() {
    }

    /**
     * Gibt den in der Nachrichtentextdatei definierten Text für den gegebenen Schlüssel zurück. Falls es für den
     * gegebenen Schlüssel keinen Text gibt, wird der Schlüssel selbst, eingerahmt von Ausrufungszeichen, zurückgegeben.
     * 
     * @param key
     *            der Schlüssel des gesuchten Textes
     * @return den Text zum gegebenen Schlüssel oder der Schlüssel eingerahmt von Ausrufungszeichen falls es keinen
     *         solchen Text gibt
     */
    public static String getString(final String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.debug("Exception while getting string for key " + key, e);
            return '!' + key + '!';
        }
    }
}
