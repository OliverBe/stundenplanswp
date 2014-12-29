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
package de.unibremen.swp.stundenplan.logic;

import java.util.Calendar;
import java.util.List;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.gui.Timeslot;
import de.unibremen.swp.stundenplan.config.Config;
import java.util.ArrayList;


/**
 * Diese Utility-Klasse verwaltet die Tagespläne.
 * 
 * @author Fathan Vidjaja
 * @version 0.1
 * 
 */
public final class TimetableManager {

    /**
     * Privater Konstruktor, der eine Instanziierung dieser Utility-Klasse verhindert.
     */
    private TimetableManager() {
    }

//    /**
//     * Prüft, ob es im unterliegenden Datenbestand schon Tagespläne gibt. Falls nicht, wird für jeden Wochentag ein
//     * Tagesplan erzeugt.
//     * 
//     * @throws DatasetException
//     *             falls es ein Problem beim Zugriff auf den Datenbestand gibt
//     */
//    public static void init() throws DatasetException {
//        List<DayTable> daytables = Data.getDayTables();
//        if (daytables.isEmpty()) {
//            fillDefaultData();
//        }
//    }
//
//    /**
//     * Prüft, ob es im unterliegenden Datenbestand schon Tagespläne gibt. Falls nicht, wird für jeden Wochentag ein
//     * Tagesplan erzeugt.
//     * 
//     * @throws DatasetException
//     *             falls es ein Problem beim Zugriff auf den Datenbestand gibt
//     */
//    public static void init(Teacher pTeacher) throws DatasetException {
//            fillDefaultDataForTeacher(pTeacher);
//        
//    }
//
//    /**
//     * Prüft, ob es im unterliegenden Datenbestand schon Tagespläne gibt. Falls nicht, wird für jeden Wochentag ein
//     * Tagesplan erzeugt.
//     * 
//     * @throws DatasetException
//     *             falls es ein Problem beim Zugriff auf den Datenbestand gibt
//     */
//    public static void init(Schoolclass pSchoolclass) throws DatasetException {
//           fillDefaultDataForSchoolclass(pSchoolclass);
//        
//    }
    
//    /**
//     * Erzeugt für jeden Wochentag einen Tagesplan und fügt sie diesem Manager hinzu.
//     * 
//     * @throws DatasetException
//     *             falls ein Problem beim Aktualisieren des Datenbestandes auftritt
//     */
//    private static void fillDefaultData() throws DatasetException {
//
//        for (final Weekday weekday : Weekday.values()) {
//            final DayTable dayTable = createDayTable(weekday);
//            Data.addDayTable(dayTable);
//        }
//    }

    private static ArrayList<Weekday> givevaliddays(){
    	final ArrayList<Weekday> schooldays = new ArrayList<Weekday>();
    	for(final Weekday weekday : Weekday.values()){
    		if(weekday.isSchoolday())schooldays.add(weekday);
    	}
    	return schooldays;
    }

//    /**
//     * Erzeugt einen neuen Tagesplan für den angegebenen Wochentag und gibt ihn zurück.
//     * 
//     * @param weekday
//     *            der Wochentag des neuen Tagesplans
//     * @return der neue Tagesplan
//     * @throws DatasetException
//     *             falls ein Problem beim Aktualisieren des Datenbestandes auftritt
//     */
    private static ArrayList<DayTable> createTimetable(){
        final ArrayList<DayTable> dayTables = new ArrayList<DayTable>();
        for(final Weekday weekday : givevaliddays()){
        	dayTables.add(createTimeslots(weekday));
        }
        return dayTables;
    }


    /**
     * Erzeugt Zeiteinheiten für den gegebenen Tagesplan.
     * 
     * @param dayTable
     *            der Tagesplan, für den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslots(Weekday pWeekday) {
        final Calendar cal = Calendar.getInstance();
        DayTable dayTable = new DayTable(pWeekday);
        cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR, startTimeHour());
        cal.set(Calendar.MINUTE, startTimeMinute());
        for (int i = 0; i <= daytablelength(); i++) {
            final Timeslot timeslot = new Timeslot(cal, pWeekday);
            dayTable.addTimeslot(timeslot);
            final Calendar newCal = Calendar.getInstance();
            newCal.setTimeInMillis(cal.getTimeInMillis());
            timeslot.setstartzeit(newCal);
            cal.add(Calendar.MINUTE, Timeslot.LENGTH);
        }
        return dayTable;
   }
//
//    /**
//     * Gibt die Zeiteinheit an der gegebenen Position für den gegebenen Wochentag zurück. Falls die Index-Angaben
//     * außerhalb der jeweils gültigen Bereiche liegen, wird {@code null} zurückgegeben.
//     * 
//     * @param weekday
//     *            der Wochentag der gesuchten Zeiteinheit
//     * @param position
//     *            die Position der gesuchten Zeiteinheit am gegebenen Wochentag
//     * @return die gesuchte Zeiteinheit oder {@code null}, falls unsinnige Parameterwerte übergeben wurden
//     * @throws DatasetException
//     *             falls es ein Problem bei der Abfrage des unterliegenden Datenbestandes gibt oder der Datenbestand
//     *             inkonsistent ist
//     */
//    public static Timeslot getTimeslotAt(final Weekday weekday, final int position) throws DatasetException {
//        DayTable dayTable;
//        dayTable = Data.getDayTableForWeekday(weekday);
//        if (dayTable == null) {
//            return null;
//        }
//        return dayTable.getTimeslot(position);
//    }
    
    
//    /**
//     * Gibt die Zeiteinheit an der gegebenen Position für den gegebenen Wochentag zurück. Falls die Index-Angaben
//     * außerhalb der jeweils gültigen Bereiche liegen, wird {@code null} zurückgegeben.
//     * 
//     * @param weekday
//     *            der Wochentag der gesuchten Zeiteinheit
//     * @param position
//     *            die Position der gesuchten Zeiteinheit am gegebenen Wochentag
//     * @return die gesuchte Zeiteinheit oder {@code null}, falls unsinnige Parameterwerte übergeben wurden
//     * @throws DatasetException
//     *             falls es ein Problem bei der Abfrage des unterliegenden Datenbestandes gibt oder der Datenbestand
//     *             inkonsistent ist
//     */
//    public static Timeslot getTimeslotAt(final Weekday weekday, final int position, Object clazz) throws DatasetException {
//        DayTable dayTable;
//        if(clazz instanceof Teacher) {
//        	dayTable = Data.getDayTableForWeekday(weekday, (Teacher) clazz);
//        }else if(clazz instanceof Schoolclass) {
//        	dayTable = Data.getDayTableForWeekday(weekday, (Schoolclass) clazz);
//        }else {
//        	dayTable = null;
//        }
//        if (dayTable == null) {
//            return null;
//        }
//        return dayTable.getTimeslot(position);
//    }

    /**
     * Gibt eine Zeichenkette zur Anzeige in der UI für die Zeiteinheit an der gegebenen Position. Die erste Zeiteinheit
     * befindet sich an Position 0.
     * 
     * @param position
     *            die Position der Zeiteinheit
     * @return eine Zeichenkette, die die Start- und Endzeit einer Zeiteinheit darstellt
     */
    public static String getTimeframeDisplay(final int position) {
        if (position < 0 || position > daytablelength()) {
            return "";
        }
        final Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, startTimeHour());
        time.set(Calendar.MINUTE, startTimeMinute());
        time.add(Calendar.MINUTE, Timeslot.LENGTH * position);
        String display = String.format("%02d:%02d - ", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        time.add(Calendar.MINUTE, Timeslot.LENGTH);
        display += String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        return display;
    }

    private static int startTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_MINUTE_STRING,
	            Config.DAY_STARTTIME_MINUTE);
	}

	private static int startTimeHour() {
		// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_HOUR_STRING,
	            Config.DAY_STARTTIME_HOUR);
	}

	private static int endTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_ENDTIME_MINUTE_STRING,
	            Config.DAY_ENDTIME_MINUTE);
	}

	private static int endTimeHour() {
		// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_ENDTIME_HOUR_STRING,
	            Config.DAY_ENDTIME_HOUR);
	}
	
	public static int duration(final int pStarthour,final int pStartminute,final int pEndhour,final int pEndminute){
		final Calendar starttime = Calendar.getInstance();
		starttime.set(Calendar.HOUR_OF_DAY, pStarthour);
		starttime.set(Calendar.MINUTE, pStartminute);
		final Calendar endtime = Calendar.getInstance();
		endtime.set(Calendar.HOUR_OF_DAY, pEndhour);
		endtime.set(Calendar.MINUTE, pEndminute);
		long milliSec1 = starttime.getTimeInMillis();
        long milliSec2 = endtime.getTimeInMillis();
        long timeDifInMilliSec;
        if(milliSec1 >= milliSec2)
        {
            timeDifInMilliSec = milliSec1 - milliSec2;
        }
        else
        {
            timeDifInMilliSec = milliSec2 - milliSec1;
        }
        long timeDifMinutes = timeDifInMilliSec / (60 * 1000);
        return (int) timeDifMinutes;
	}
	/*
	 * errechnet die Anzahl der Timeslots die in dem Tag passt
	 */
	public static int daytablelength() {
		int dur = duration(startTimeHour(),startTimeMinute(),endTimeHour(),endTimeMinute());
        return dur/Timeslot.LENGTH;
	}

//    /**
//     * Aktualisiert die Werte für die gegebene Zeiteinheit im Datenbestand.
//     * 
//     * @param pTimeslot
//     *            die zu aktualisierende Zeiteinheit
//     * @throws DatasetException
//     *             falls bei der Aktualisierung ein Fehler in der unterliegenden Persistenzkomponente auftritt oder das
//     *             gegebene Objekt noch nicht im Datenbestand existiert
//     */
//    public static void updateTimeslot(final Timeslot pTimeslot) throws DatasetException {
//        Data.updateTimeslot(pTimeslot);
//    }
	
	///**
	// * Repräsentiert einen Tagesplan eines Stundenplans an einem bestimmten Wochentag. Verwaltet eine Liste von
	// * Zeiteinheiten. Jeder Tagesplan verwaltet innerhalb eines Stundenplans die gleiche Anzahl von Zeiteinheiten. Diese
	// * Anzahl ist konfigurierbar und per Default auf {@linkplain Config#DAYTABLE_LENGTH_DEFAULT} festgelegt. Alle Tagespläne
	// * beginnen zur gleichen konfigurierbaren Uhrzeit. Diese Startzeit ist per Default festgelegt auf die Stunde
	// * {@linkplain Config#DAYTABLE_STARTTIME_HOUR_DEFAULT} und die Minute
	// * {@linkplain Config#DAYTABLE_STARTTIME_MINUTE_DEFAULT}. Die Endzeit des Tagesplans ergibt sich dann aus der Startzeit
	// * und der Anzahl der Zeiteinheiten.
	// * 
	// * @author F.Vidjaja
	// * @version 0.1
	// */
	private static class DayTable {
		
	
//	    /**
//	     * Die Liste von Zeiteinheiten, aufsteigend sortiert nach ihren Anfangszeiten.
//	     */
	    private List<Timeslot> timeslots;
	
//	    /**
//	     * Der Wochentag dieses Tagesplans.
//	     */
	    private Weekday weekday;
	    
	    
	
//	    /**
//	     * Erzeugt einen neuen Tagesplan mit einer leeren Liste von Zeiteinheiten.
//	     */
	    public DayTable(final Weekday pWeekday) {
	        timeslots = new ArrayList<>();
	        weekday = pWeekday;
	    }
		    
//	    /**
//	     * Fügt die übergebene Zeiteinheit zu diesem Tagesplan hinzu. Löst eine {@link IllegalArgumentException} aus, falls
//	     * die übergebene Zeiteinheit {@code null} ist
//	     * 
//	     * @param pTimeslot
//	     *            die hinzuzufügende Zeiteinheit
//	     */
	    public void addTimeslot(final Timeslot pTimeslot) {
	        if (pTimeslot == null) {
	            throw new IllegalArgumentException("FIXME: configured exception string");
	        }
	        timeslots.add(pTimeslot);
	    }
	
//	    /**
//	     * Gibt den Wochentag dieses Tagesplans zurück.
//	     * 
//	     * @return den Wochentag dieses Tagesplans
//	     */
	    public Weekday getWeekday() {
	        return weekday;
	    }
	
//	    /**
//	     * Gibt die Zeiteinheit am gegebenen Positionsindex zurück oder {@code null} falls ein ungültiger Positionsindex
//	     * zurückgegeben wurde. Die erste Zeiteinheit des Tagesplans beginnt bei Index 0.
//	     * 
//	     * @param position
//	     *            die Position der gesuchten Zeiteinheit
//	     * @return die gesuchte Zeiteinheit oder {@code null}, falls eine ungültige Position übergeben wurde
//	     */
	    public Timeslot getTimeslot(final int position) {
	        if (position >= 0 && position < timeslots.size()) {
	            return timeslots.get(position);
	        } else {
	            return null;
	        }
	    }
	}
	
}