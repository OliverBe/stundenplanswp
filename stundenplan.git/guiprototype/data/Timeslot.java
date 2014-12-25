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
package data;

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

import config.Weekday;
import de.unibremen.swp.stundenplan.config.Config;

/**
 * Entspricht einer Zeiteinheit. Eine Zeiteinheit ist einem Tagesplan zugeordnet und hat eine startzeit. Die Dauer einer
 * solchen Zeiteinheit ist konfigurierbar und per Default auf {@linkplain Config#TIMESLOT_LENGTH_DEFAULT} festgelegt.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 * 
 */
@Entity
public final class Timeslot implements Serializable {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = 4249954963688259056L;

    /**
     * Die Dauer aller Zeiteinheiten in Minuten.
     */
    @Transient
    public static final int LENGTH = Config.getInt(Config.TIMESLOT_LENGTH_STRING,
            Config.TIMESLOT_LENGTH_DEFAULT);

    /**
     * Die eindeutige, von der unterliegenden Persistenzschicht automatisch erzeugte ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

 
    /**
     * Die startzeit dieses Timeslots. Die Einträge für {@linkplain Calendar#HOUR} und {@linkplain Calendar#MINUTE}
     * müssen entsprechend gesetzt sein.
     */
    @Temporal(TemporalType.TIME)
    private Calendar startzeit;

    private Weekday wochentag;
    
    /**
     * Erzeugt eine neue Zeiteinheit mit leeren Sammlungen für die zugeordneten LehrerInnen und Schulklassen.
     */
    public Timeslot() {
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
}
