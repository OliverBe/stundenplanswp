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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;

/**
 * Repräsentiert einen Tagesplan eines Stundenplans an einem bestimmten Wochentag. Verwaltet eine Liste von
 * Zeiteinheiten. Jeder Tagesplan verwaltet innerhalb eines Stundenplans die gleiche Anzahl von Zeiteinheiten. Diese
 * Anzahl ist konfigurierbar und per Default auf {@linkplain Config#DAYTABLE_LENGTH_DEFAULT} festgelegt. Alle Tagespläne
 * beginnen zur gleichen konfigurierbaren Uhrzeit. Diese Startzeit ist per Default festgelegt auf die Stunde
 * {@linkplain Config#DAYTABLE_STARTTIME_HOUR_DEFAULT} und die Minute
 * {@linkplain Config#DAYTABLE_STARTTIME_MINUTE_DEFAULT}. Die Endzeit des Tagesplans ergibt sich dann aus der Startzeit
 * und der Anzahl der Zeiteinheiten.
 * 
 * @author K. Hölscher
 * @version 0.1
 */
@Entity
public final class DayTable {

    /**
     * Anzahl der Zeiteinheiten, die alle Tagespläne enthalten.
     */
    @Transient
    public static final int LENGTH = Config.getInt(Config.DAYTABLE_LENGTH_STRING,
            Config.DAYTABLE_LENGTH_DEFAULT);

    /**
     * Die Stunde, zu der ein Tagesplan beginnt.
     */
    @Transient
    public static final int STARTTIME_HOUR = Config.getInt(Config.DAYTABLE_STARTTIME_HOUR_STRING,
            Config.DAYTABLE_STARTTIME_HOUR_DEFAULT);

    /**
     * Die Minute, zu der ein Tagesplan beginnt.
     */
    @Transient
    public static final int STARTTIME_MINUTE = Config.getInt(Config.DAYTABLE_STARTTIME_MINUTE_STRING,
            Config.DAYTABLE_STARTTIME_MINUTE_DEFAULT);

    /**
     * Die ID dieses Tagesplans. Wird von der Persistenz automatisch generiert und dadurch die Eindeutigkeit
     * sichergestellt.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Die Liste von Zeiteinheiten, aufsteigend sortiert nach ihren Anfangszeiten.
     */
    @OneToMany(cascade = CascadeType.PERSIST)
    @OrderBy("startTime ASC")
    private List<Timeslot> timeslots;

    /**
     * Der Wochentag dieses Tagesplans.
     */
    @Enumerated(EnumType.STRING)
    private Weekday weekday;
    
    /**
     * 
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Schoolclass schoolclass;
    
    /**
     * 
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Teacher teacher;

    /**
     * Erzeugt einen neuen Tagesplan mit einer leeren Liste von Zeiteinheiten.
     */
    public DayTable() {
        timeslots = new ArrayList<>();
    }

    /**
     * Setzt den Wochentag dieses Tagesplans.
     * 
     * @param pWeekday
     *            der neue Wochentag für diesen Tagesplan
     */
    public void setWeekday(final Weekday pWeekday) {
        weekday = pWeekday;
    }

    /**
     * Setzt den Wochentag dieses Tagesplans.
     * 
     * @param pWeekday
     *            der neue Wochentag für diesen Tagesplan
     */
    public void setTeacher(final Teacher pTeacher) {
        teacher = pTeacher;
    }
    
    /**
     * Setzt den Wochentag dieses Tagesplans.
     * 
     * @param pWeekday
     *            der neue Wochentag für diesen Tagesplan
     */
    public void setSchoolclass(final Schoolclass pSchoolclass) {
        schoolclass = pSchoolclass;
    }
    
    public Teacher getTeacher() {
    	return teacher;
    }
    
    public Schoolclass getSchoolclass() {
    	return schoolclass;
    }
    
    /**
     * Fügt die übergebene Zeiteinheit zu diesem Tagesplan hinzu. Löst eine {@link IllegalArgumentException} aus, falls
     * die übergebene Zeiteinheit {@code null} ist
     * 
     * @param pTimeslot
     *            die hinzuzufügende Zeiteinheit
     */
    public void addTimeslot(final Timeslot pTimeslot) {
        if (pTimeslot == null) {
            throw new IllegalArgumentException("FIXME: configured exception string");
        }
        timeslots.add(pTimeslot);
    }

    /**
     * Gibt den Wochentag dieses Tagesplans zurück.
     * 
     * @return den Wochentag dieses Tagesplans
     */
    public Weekday getWeekday() {
        return weekday;
    }

    /**
     * Gibt die Zeiteinheit am gegebenen Positionsindex zurück oder {@code null} falls ein ungültiger Positionsindex
     * zurückgegeben wurde. Die erste Zeiteinheit des Tagesplans beginnt bei Index 0.
     * 
     * @param position
     *            die Position der gesuchten Zeiteinheit
     * @return die gesuchte Zeiteinheit oder {@code null}, falls eine ungültige Position übergeben wurde
     */
    public Timeslot getTimeslot(final int position) {
        if (position >= 0 && position < timeslots.size()) {
            return timeslots.get(position);
        } else {
            return null;
        }
    }

}
