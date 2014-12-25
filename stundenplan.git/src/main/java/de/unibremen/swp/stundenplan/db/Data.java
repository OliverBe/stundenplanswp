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
package de.unibremen.swp.stundenplan.db;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Subject;
import de.unibremen.swp.stundenplan.data.Teacher;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.gui.DayTable;
import de.unibremen.swp.stundenplan.gui.Timeslot;

/**
 * Utility-Klasse als konkrete Persistenz-Implementierung. Verwendet JPA 2.0 zum Verwalten des Datenbestandes. Der
 * statische Initialisierer erzeugt einen Entity-Manager für die weitere Verwendung. Falls das fehlschlägt, wird eine
 * {@link IllegalStateException} ausgelöst.
 * 
 * @author K. Hölscher
 * @editor Belavic, Oliver
 */
public final class Data {

    /**
     * Logger dieser Klasse zum Protokollieren von Ereignissen und Informationen.
     */
    private static final Logger LOGGER = Logger.getLogger(Data.class.getName());

    /*
     * Statischer Initialisierer. Wird vor der ersten Verwendung dieser Klasse ausgeführt.
     */
    static {
        try {
            LOGGER.debug("Creating new persistence manager");
            EntityManagerFactory factory;
            factory = Persistence.createEntityManagerFactory(Config.getString("persistence_name", "stundenplan"));
            entityManager = factory.createEntityManager();
        } catch (Exception e) {
            LOGGER.error("Exception while creating new data object!", e);
            throw new PersistenceException("Could not initialize persistence component: " + e.getMessage());
        }

    }

    /**
     * Maximale Anzahl von Zeichen innerhalb eines normalen Strings. Definiert die entsprechende Spaltenbreite in der
     * unterliegenden Datenbank-Tabelle.
     */
    public static final int MAX_NORMAL_STRING_LEN = 255;

    /**
     * Maximale Anzahl von Zeichen, die ein Akronym haben darf. Definiert die entsprechende Spaltenbreite in der
     * unterliegenden Datenbank-Tabelle.
     */
    public static final int MAX_ACRONYM_LEN = 5;

    /**
     * Der Entity-Manager, der die eigentliche Persistierung und Transaktionsverwaltung übernimmt. Wird von der
     * JPA-Implementierung bereitgestellt und über die {@code persistence.xml} konfiguriert.
     */
    private static EntityManager entityManager;

    /**
     * Statischer Initialisierer. Wird unmittelbar vor der ersten Verwendung dieser Hilfsklasse aufgerufen und
     * initialisiert den Entity-Manager.
     */

    /**
     * Privater Konstruktor um eine unnötige Instanziierung dieser Utility-Klasse zu verhindern.
     */
    private Data() {
    }

    /**
     * Fügt die gegebene LehrerIn dem Datenbestand hinzu. Löst eine {@link IllegalArgumentException} aus, falls der
     * Parameterwert {@code null} ist.
     * 
     * @param teacher
     *            die hinzuzufügende LehrerIn
     * 
     * @throws DatasetException
     *             falls beim Hinzufügen in der unterliegenden Persistenzkomponente ein Fehler auftritt
     */
    public static void addTeacher(final Teacher teacher) throws DatasetException {
        if (teacher == null) {
            throw new IllegalArgumentException("Value of teacher parameter is null");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(teacher);
            entityManager.getTransaction().commit();
            LOGGER.debug(String.format("Teacher %s persisted.", teacher));
        } catch (Exception e) {
            LOGGER.error("Error adding teacher: ", e);
            throw new DatasetException("Error while adding a teacher: " + e.getMessage());
        }
    }

    /**
     * Gibt die LehrerIn zu dem gegebenen Kürzel zurück oder {@code null} falls es keine solche LehrerIn gibt oder das
     * gegebene Kürzel {@code null} oder leer ist.
     * 
     * @param acronym
     *            das Kürzel der gesuchten LehrerIn
     * @return die LehrerIn zu dem gegebenen Kürzel oder {@code null} falls es keine LehrerIn mit dem Kürzel im
     *         Datenbestand gibt oder der Parameterwert ungültig war
     * @throws DatasetException
     *             falls ein Fehler bei der Abfrage des Datenbestandes in der unterliegenden Persistenzkomponente
     *             auftritt
     * 
     */
    public static Teacher getTeacherByAcronym(final String acronym) throws DatasetException {
        if (acronym == null || acronym.trim().isEmpty()) {
            return null;
        }
        try {
            return entityManager.find(Teacher.class, acronym);
        } catch (Exception e) {
            LOGGER.error("Exception while getting teacher by acronym " + acronym, e);
            throw new DatasetException("Error while searching a teacher for acronym " + acronym + ": " + e.getMessage());
        }
    }

    /**
     * Gibt die Sammlung aller im Datenbestand befindlichen LehrerInnen zurück.
     * 
     * @return die Sammlung aller LehrerInnen
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static Collection<Teacher> getAllTeachers() throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT t FROM Teacher t");
            return (Collection<Teacher>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all teachers!", e);
            throw new DatasetException("Error while getting all teachers: " + e.getMessage());
        }
    }
    
    /**
     * Fügt das gegebene Fach dem Datenbestand hinzu. Löst eine {@link IllegalArgumentException} aus, falls der
     * Parameterwert {@code null} ist.
     * 
     * @param subject
     *            die hinzuzufügende Fach
     * 
     * @throws DatasetException
     *             falls beim Hinzufügen in der unterliegenden Persistenzkomponente ein Fehler auftritt
     */
    public static void addSubject(final Subject subject) throws DatasetException {
        if (subject == null) {
            throw new IllegalArgumentException("Value of teacher parameter is null");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(subject);
            entityManager.getTransaction().commit();
            LOGGER.debug(String.format("Subject %s persisted.", subject));
        } catch (Exception e) {
            LOGGER.error("Error adding subject: ", e);
            throw new DatasetException("Error while adding a subject: " + e.getMessage());
        }
    }

    /**
     * Gibt die F�cher zu dem gegebenen Kürzel zurück oder {@code null} falls es keine solches Fach gibt oder das
     * gegebene Kürzel {@code null} oder leer ist.
     * 
     * @param pID
     *            das Kürzel der gesuchtes Fach
     * @return das Fach zu dem gegebenen Kürzel oder {@code null} falls es kein fach mit dem Kürzel im
     *         Datenbestand gibt oder der Parameterwert ungültig war
     * @throws DatasetException
     *             falls ein Fehler bei der Abfrage des Datenbestandes in der unterliegenden Persistenzkomponente
     *             auftritt
     * 
     */
    public static Subject getSubjectByID(final long pID) throws DatasetException {
        
        try {
            return entityManager.find(Subject.class, pID);
        } catch (Exception e) {
            LOGGER.error("Exception while getting subject by acronym " + pID, e);
            throw new DatasetException("Error while searching a subject for acronym " + pID + ": " + e.getMessage());
        }
    }

    public static void editSubject(final Subject subject) throws DatasetException {
    	if (subject == null){
    		throw new IllegalArgumentException("Value of teacher parameter is null");
    	}
    	try{
    		entityManager.getTransaction().begin();
    		entityManager.merge(subject);
    		entityManager.getTransaction().commit();
    		LOGGER.debug(String.format("Subject %s edited. ", subject));
    	}catch (Exception e){
    		LOGGER.error("Error editing subject: ", e);
    		throw new DatasetException("Error while editing a subject: " + e.getMessage());
    	}
    }

    
    /**
     * Gibt die Sammlung aller im Datenbestand befindlichen F�cher zurück.
     * 
     * @return die Sammlung aller F�cher
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static Collection<Subject> getAllSubjects() throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT s FROM Subject s");
            return (Collection<Subject>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all subjects!", e);
            throw new DatasetException("Error while getting all subjects: " + e.getMessage());
        }
    }

    /**
     * Fügt die gegebene Schulklasse dem Datenbestand hinzu. Löst eine {@link IllegalArgumentException} aus, falls der
     * Parameterwert {@code null} ist.
     * 
     * @param subject
     *            die hinzuzufügende Schulklasse
     * 
     * @throws DatasetException
     *             falls beim Hinzufügen in der unterliegenden Persistenzkomponente ein Fehler auftritt
     */
    public static void addSchoolclass(final Schoolclass schoolclass) throws DatasetException {
        if (schoolclass == null) {
            throw new IllegalArgumentException("Value of schoolclass parameter is null");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(schoolclass);
            entityManager.getTransaction().commit();
            LOGGER.debug(String.format("Schoolclass %s persisted.", schoolclass.getName()));
        } catch (Exception e) {
            LOGGER.error("Error adding schoolclass: ", e);
            throw new DatasetException("Error while adding a schoolclass: " + e.getMessage());
        }
    }
    
    /**
     * Gibt die Schulklasse zu dem gegebenen Namen zurück oder {@code null} falls es keine solche Schulklasse gibt oder der
     * gegebene Name {@code null} oder leer ist.
     * 
     * @param name
     *            der Name der gesuchten Schulklasse
     * @return die Schulklasse zu dem gegebenen Namen oder {@code null} falls es keine Schulklasse mit dem Namen im
     *         Datenbestand gibt oder der Parameterwert ungültig war
     * @throws DatasetException
     *             falls ein Fehler bei der Abfrage des Datenbestandes in der unterliegenden Persistenzkomponente
     *             auftritt
     * 
     */
    public static Schoolclass getSchoolclassByName(final String name) throws DatasetException {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        try {
            return entityManager.find(Schoolclass.class, name);
        } catch (Exception e) {
            LOGGER.error("Exception while getting schoolclass by name " + name, e);
            throw new DatasetException("Error while searching a schoolclass for name " + name + ": " + e.getMessage());
        }
    }
    
    /**
     * Gibt die Sammlung aller im Datenbestand befindlichen Schulklassen zurück.
     * 
     * @return die Sammlung aller Schulklassen
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static Collection<Schoolclass> getAllSchoolclasses() throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT c FROM Schoolclass c");
            return (Collection<Schoolclass>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all schoolclasse!", e);
            throw new DatasetException("Error while getting all schoolclasses: " + e.getMessage());
        }
    }
    
    /**
     * Fügt den gegebenen Tagesplan zum Datenbestand hinzu. Falls der Parameterwert {@code null} ist, wird eine
     * {@link IllegalArgumentException} ausgelöst.
     * 
     * @param dayTable
     *            der hinzuzufügende Tagesplan
     * @throws DatasetException
     *             falls beim Hinzufügen ein Fehler in der unterliegenden Persistenzkomponente auftritt
     */
    public static void addDayTable(final DayTable dayTable) throws DatasetException {
        if (dayTable == null) {
            throw new IllegalArgumentException("Value of daytable parameter is null!");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(dayTable);
            entityManager.getTransaction().commit();
            LOGGER.debug("Tagesplan für " + dayTable.getWeekday() + " persistiert.");
        } catch (Exception e) {
            LOGGER.error("Exception while adding day table " + dayTable, e);
            throw new DatasetException("Error while adding day table: " + e.getMessage());
        }
    }

    /**
     * Gibt die Liste aller Tagespläne des Datenbestandes zurück.
     * 
     * @return die Liste aller Tagespläne des Datenbestandes
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static List<DayTable> getDayTables() throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT d FROM DayTable d");
            return (List<DayTable>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all day tables!", e);
            throw new DatasetException("Error while getting all day tables: " + e.getMessage());
        }
    }

    /**
     * Gibt die Liste aller Tagespläne des Datenbestandes zurück.
     * 
     * @return die Liste aller Tagespläne des Datenbestandes
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static List<DayTable> getDayTablesForTeacher(final Teacher pTeacher) throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT d FROM DayTable d WHERE d.Teacher = ?1");
            query.setParameter(1, pTeacher);
            return (List<DayTable>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all day tables!", e);
            throw new DatasetException("Error while getting all day tables: " + e.getMessage());
        }
    }
    
    /**
     * Gibt die Liste aller Tagespläne des Datenbestandes zurück.
     * 
     * @return die Liste aller Tagespläne des Datenbestandes
     * @throws DatasetException
     *             falls bei der Abfrage des Datenbestandes ein Fehler in der unterliegenden Persistenzkomponente
     *             auftritt
     */
    @SuppressWarnings("unchecked")
    public static List<DayTable> getDayTablesForSchoolclass(final Schoolclass pSchoolclas) throws DatasetException {
        try {
            final Query query = entityManager.createQuery("SELECT d FROM DayTable d WHERE d.Schoolclass = ?1");
            query.setParameter(1, pSchoolclas);
            return (List<DayTable>) query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Exception while getting all day tables!", e);
            throw new DatasetException("Error while getting all day tables: " + e.getMessage());
        }
    }
    
    /**
     * Gibt den Tagesplan des gegebenen Wochentages zurück oder {@code null}, falls kein Tagesplan für den Wochentag im
     * Datenbestand existiert. Falls der gegebene Wochentag den Wert {@code null} hat, wird ebenfalls {@code null}
     * zurück gegeben.
     * 
     * @param pWeekday
     *            der Wochentag des gesuchten Tagesplans
     * @return den Tagesplan des gegebenen Wochentages oder {@code null}
     * @throws DatasetException
     *             falls es mehr als einen Tagesplan zu dem gegebenen Wochentag gibt oder es ein Problem bei der Abfrage
     *             des unterliegenden Datenbestandes gibt
     */
    public static DayTable getDayTableForWeekday(final Weekday pWeekday) throws DatasetException {
        if (pWeekday == null) {
            return null;
        }
        final String weekday = pWeekday.name();
        try {

            final Query query = entityManager.createQuery("SELECT d FROM DayTable d WHERE d.weekday=?1");
            query.setParameter(1, pWeekday);
            @SuppressWarnings("unchecked")
            List<DayTable> dayTables = query.getResultList();
            if (dayTables.isEmpty()) {
                return null;
            }
//            if (dayTables.size() > 1) {
//                LOGGER.error("There is more than one daytable for day " + weekday);
//                throw new DatasetException("Daytable not unique for " + weekday);
//            }
            return dayTables.get(0);
        } catch (Exception e) {
            LOGGER.error("Exception while getting day tables for day " + weekday, e);
            throw new DatasetException(String.format("Error while getting day table for day %s: %s", weekday,
                    e.getMessage()));
        }
    }
    
    /**
     * Gibt den Tagesplan des gegebenen Wochentages zurück oder {@code null}, falls kein Tagesplan für den Wochentag im
     * Datenbestand existiert. Falls der gegebene Wochentag den Wert {@code null} hat, wird ebenfalls {@code null}
     * zurück gegeben.
     * 
     * @param pWeekday
     *            der Wochentag des gesuchten Tagesplans
     * @return den Tagesplan des gegebenen Wochentages oder {@code null}
     * @throws DatasetException
     *             falls es mehr als einen Tagesplan zu dem gegebenen Wochentag gibt oder es ein Problem bei der Abfrage
     *             des unterliegenden Datenbestandes gibt
     */
    public static DayTable getDayTableForWeekday(final Weekday pWeekday, final Teacher pTeacher) throws DatasetException {
        if (pWeekday == null) {
            return null;
        }
        final String weekday = pWeekday.name();
        try {

            final Query query = entityManager.createQuery("SELECT d FROM DayTable d WHERE d.weekday=?1 AND d.teacher=?2");
            query.setParameter(1, pWeekday);
            query.setParameter(2, pTeacher);
            @SuppressWarnings("unchecked")
            List<DayTable> dayTables = query.getResultList();
            if (dayTables.isEmpty()) {
                return null;
            }
            if (dayTables.size() > 1) {
                LOGGER.error("There is more than one daytable for day " + weekday);
                throw new DatasetException("Daytable not unique for " + weekday);
            }
            return dayTables.get(0);
        } catch (Exception e) {
            LOGGER.error("Exception while getting day tables for day " + weekday, e);
            throw new DatasetException(String.format("Error while getting day table for day %s: %s", weekday,
                    e.getMessage()));
        }
    }
    
    /**
     * Gibt den Tagesplan des gegebenen Wochentages zurück oder {@code null}, falls kein Tagesplan für den Wochentag im
     * Datenbestand existiert. Falls der gegebene Wochentag den Wert {@code null} hat, wird ebenfalls {@code null}
     * zurück gegeben.
     * 
     * @param pWeekday
     *            der Wochentag des gesuchten Tagesplans
     * @return den Tagesplan des gegebenen Wochentages oder {@code null}
     * @throws DatasetException
     *             falls es mehr als einen Tagesplan zu dem gegebenen Wochentag gibt oder es ein Problem bei der Abfrage
     *             des unterliegenden Datenbestandes gibt
     */
    public static DayTable getDayTableForWeekday(final Weekday pWeekday, final Schoolclass pSchoolclass) throws DatasetException {
        if (pWeekday == null) {
            return null;
        }
        final String weekday = pWeekday.name();
        try {

            final Query query = entityManager.createQuery("SELECT d FROM DayTable d WHERE d.weekday=?1 AND d.schoolclass=?2");
            query.setParameter(1, pWeekday);
            query.setParameter(2, pSchoolclass);
            @SuppressWarnings("unchecked")
            List<DayTable> dayTables = query.getResultList();
            if (dayTables.isEmpty()) {
                return null;
            }
            if (dayTables.size() > 1) {
                LOGGER.error("There is more than one daytable for day " + weekday);
                throw new DatasetException("Daytable not unique for " + weekday);
            }
            return dayTables.get(0);
        } catch (Exception e) {
            LOGGER.error("Exception while getting day tables for day " + weekday, e);
            throw new DatasetException(String.format("Error while getting day table for day %s: %s", weekday,
                    e.getMessage()));
        }
    }

    /**
     * Aktualisiert die Werte für die gegebene Zeiteinheit im Datenbestand.
     * 
     * @param timeslot
     *            die zu aktualisierende Zeiteinheit
     * @throws DatasetException
     *             falls bei der Aktualisierung ein Fehler in der unterliegenden Persistenzkomponente auftritt oder das
     *             gegebene Objekt noch nicht im Datenbestand existiert
     */
    public static void updateTimeslot(final Timeslot timeslot) throws DatasetException {

        if (timeslot == null) {
            throw new IllegalArgumentException("Value of timeslot parameter is null!");
        }
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(timeslot);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Exception while updating the timeslot " + timeslot, e);
            throw new DatasetException("Error while updating timeslot: " + e.getMessage());
        }
    }

}
