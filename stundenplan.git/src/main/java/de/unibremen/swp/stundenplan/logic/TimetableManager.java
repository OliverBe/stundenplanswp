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

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.unibremen.swp.stundenplan.Stundenplan;
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
 * Diese Utility-Klasse verwaltet die Tagespl��ne.
 * 
 * @author Fathan Vidjaja
 * @version 0.1
 * 
 */
public final class TimetableManager {
	
	private static int counter = 0;
	private static int tscounter = 0;

    /**
     * Privater Konstruktor, der eine Instanziierung dieser Utility-Klasse verhindert.
     */
    private TimetableManager() {
    }
    
    /**
     * gibt eine Array der geplanten Wochentagen zurück, wird in Timetablemodel benutzt um 
     * die Berrechnung der Anzahl und die Beschriftung der Spalten benutzt 
     * @return
     */
    public static Weekday[] validdays(){
       Weekday[] w = new Weekday[givevaliddays().size()];
       return givevaliddays().toArray(w);
    }
    
    /**
     * gibt eine Liste der geplanten Wochentagen zurueck.
     * @return
     */
    private static ArrayList<Weekday> givevaliddays(){
    	final ArrayList<Weekday> schooldays = new ArrayList<Weekday>();
    	for(final Weekday weekday : Weekday.values()){
    		if(weekday.isSchoolday())schooldays.add(weekday);
    	}
    	return schooldays;
    }


    /**
     * prueft ob zwei Calendareinheiten die gleiche Zeitpunkt  am Tag besitzt
     * @param cal1 erste Calendarobjekt
     * @param cal2 zweite Calendarobjekt
     * @return gibt true zurueck falls die Stunden und Minuten gleich sind
     */
    private static boolean sametimeofday(Calendar cal1, Calendar cal2){
 	   boolean sametime = cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE);
 	   return sametime;
    } 

    /**
     * erzeugt eine Tagestabelle fuer eine Liste von Planungseinheiten
     * @param pPE Liste von Planungseinheiten
     * @param pWeekday den Tag fuer die Tabelle
     * @return gibt eine DayTable zurueck
     */
    private static DayTable createTimeslotsForPES(ArrayList<Planungseinheit> pPE,Weekday pWeekday){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR_OF_DAY, startTimeHour());
        cal.set(Calendar.MINUTE, startTimeMinute());
        DayTable dayTable = new DayTable(pWeekday);
        if(pPE.size() == 0 ){
        	dayTable.addTimeslot(filltoEnd(dayTable, cal));
        	return dayTable;
        }
        ArrayList<Planungseinheit> pE = pPE;
        for(Planungseinheit p: pE){
        	final Calendar pecal = Calendar.getInstance();
            pecal.setTimeInMillis(0);
            pecal.set(Calendar.HOUR_OF_DAY, p.getStartHour());
            pecal.set(Calendar.MINUTE, p.getStartminute());
        	while(!sametimeofday(cal, pecal)){
        		Timeslot t = new Timeslot(pWeekday);
        		dayTable.addTimeslot(t);
        		final Calendar newCal = Calendar.getInstance();
        		newCal.setTimeInMillis(cal.getTimeInMillis());
                t.setstartzeit(newCal);
                cal.add(Calendar.MINUTE, Timeslot.timeslotlength());
            }
        	ArrayList<Timeslot> ts = planungsEinheitToTimeslot(p);
        	dayTable.addTimeslot(ts);
            cal.add(Calendar.MINUTE, Timeslot.timeslotlength()*ts.size());
        }
        if(dayTable.slotslength()<daytablelength()){
        	dayTable.addTimeslot(filltoEnd(dayTable, cal));
        }
        return dayTable;
    
    }
    /**
     * Erzeugt Zeiteinheiten fuer den gegebenen Tag und Person.
     * 
     * @param dayTable
     *            der Tagesplan, fuer den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForPersonal(Weekday pWeekday, Personal pPerson) {
        ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForPersonalbyWeekday(pWeekday, pPerson);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
    }
    
    /**
     * fuellt eine Tagesplan mit leeren Timeslots falls Tabelle noch nicht voll ist
     * @param pdt Tagesplan
     * @return Liste mit restlichen leeren Timeslots
     */
    private static ArrayList<Timeslot> filltoEnd(DayTable pdt, Calendar cal) {
		ArrayList<Timeslot> timeslot = new ArrayList<Timeslot>();
		int index = pdt.slotslength();
		final Calendar time = Calendar.getInstance();
		time.setTimeInMillis(cal.getTimeInMillis());
        for(;index< daytablelength(); index++){
			Timeslot t = new Timeslot(pdt.getWeekday());
			final Calendar newtime = Calendar.getInstance();
			newtime.setTimeInMillis(time.getTimeInMillis());
			t.setstartzeit(newtime);
			timeslot.add(t);
			time.add(Calendar.MINUTE, Timeslot.timeslotlength());			
		}
        return timeslot;
	}

	/**
     * Erzeugt Zeiteinheiten fuer den gegebenen Tag und Schulklasse.
     * 
     * @param dayTable
     *            der Tagesplan, fuer den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForSchoolclass(Weekday pWeekday, Schoolclass pSC) {
    	ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForSchoolclassbyWeekday(pWeekday, pSC);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
        }
    
    /**
     * Erzeugt Zeiteinheiten f��r den gegebenen Tag und Raum.
     * 
     * @param dayTable
     *            der Tagesplan, f��r den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForRoom(Weekday pWeekday,Room pRoom) {
    	ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForRoombyWeekday(pWeekday, pRoom);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
   }
    
    /**
     * Berechnet die Anzahl der Timeslots die in einem Planungseinheiten passen,(es muss genau passen)
     * und beschriftet die Timeslots mit den notwendigen Informationen
     * @param pPE
     * @return gibt eine Liste von Timeslots zurueck
     */
    private static ArrayList<Timeslot> planungsEinheitToTimeslot(final Planungseinheit pPE){
    	ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
    	int timeslotcount = duration(pPE.getStartHour(), pPE.getStartminute(), pPE.getEndhour(), pPE.getEndminute());
    	timeslotcount = timeslotcount/Timeslot.timeslotlength();
    	final Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR_OF_DAY, pPE.getStartHour());
        cal.set(Calendar.MINUTE, pPE.getStartminute());
        for(int i = 0 ; i<timeslotcount; i++){
    		Timeslot t = new Timeslot(pPE.getWeekday());
    		final Calendar newCal = Calendar.getInstance();
    		newCal.setTimeInMillis(cal.getTimeInMillis());
            t.setstartzeit(newCal);
    		t.setKlassentext(pPE);
    		t.setPersonaltext(pPE);
    		t.setRaumtext(pPE);
    		t.setStundeninhalttext(pPE);
    		t.setpe(pPE);
    		t.setrhytm(pPE);
    		if(pPE.getStundeninhalte().size()==0){
    			t.setrpause();
    		}
    		timeslots.add(t);
            cal.add(Calendar.MINUTE, Timeslot.timeslotlength());
        }
        return timeslots;
    }
    
    
    
    /**
     * testet Planungseinheit in timeslot Konversion
     */
    public static void peConversiontest(){
    	Planungseinheit pe = new Planungseinheit(); 
    	pe.setStarthour(8);
    	pe.setStartminute(0);
    	pe.setEndhour(8);
    	pe.setEndminute(30);
    	ArrayList<Timeslot> tp = planungsEinheitToTimeslot(pe);
    	System.out.println(tp.size());
    	System.out.println(tp.get(0).getTimeDisplay());
    	System.out.println(tp.get(1).getTimeDisplay());
    	System.out.println(tp.get(2).getTimeDisplay());
     }
    
    
    
    /**
     * Gibt die Zeiteinheit an der gegebenen Position fuer den gegebenen Wochentag zurueck. Falls die Index-Angaben
     * ausserhalb der jeweils gueltigen Bereiche liegen, wird {@code null} zuruwckgegeben.
     * 
     * @param weekday
     *            der Wochentag der gesuchten Zeiteinheit
     * @param position
     *            die Position der gesuchten Zeiteinheit am gegebenen Wochentag
     * @return die gesuchte Zeiteinheit oder {@code null}, falls unsinnige Parameterwerte ��bergeben wurden
     * @throws DatasetException
     *             falls es ein Problem bei der Abfrage des unterliegenden Datenbestandes gibt oder der Datenbestand
     *             inkonsistent ist
     */
    public static Timeslot getTimeslotAt(final Weekday weekday, final int position, Object clazz) throws DatasetException {
        DayTable dayTable;
        if(clazz instanceof Personal){
        	dayTable = createTimeslotsForPersonal(weekday, (Personal)clazz);
        }else if(clazz instanceof Schoolclass){
        	dayTable = createTimeslotsForSchoolclass(weekday, (Schoolclass)clazz);
        }else if(clazz instanceof Room){
        	dayTable = createTimeslotsForRoom(weekday, (Room)clazz);
        }else{ 	
        	return null;
        }
       	
        return dayTable.getTimeslot(position);
    }

    /**
     * Gibt eine Zeichenkette zur Anzeige in der GUI fuer die Zeiteinheit an der gegebenen Position. Die erste Zeiteinheit
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
        time.add(Calendar.MINUTE, Timeslot.timeslotlength() * position);
        String display = String.format("%02d:%02d - ", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        time.add(Calendar.MINUTE, Timeslot.timeslotlength());
        display += String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        return display;
    }
    
    /**
     * gibt die sespeicherte Startminute von Wochentagen zurueck
     * @return
     */
    public static int startTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_MINUTE_STRING,
	            Config.DAY_STARTTIME_MINUTE);
	}
    
    /**
     * gibt die sespeicherte Startstunde von Wochentagen zurueck
     * @return
     */
	public static int startTimeHour() {
		// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_HOUR_STRING,
	            Config.DAY_STARTTIME_HOUR);
	}
	
	/**
     * gibt die sespeicherte Endminute von Wochentagen zurueck
     * @return
     */
	public static int endTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_ENDTIME_MINUTE_STRING,
	            Config.DAY_ENDTIME_MINUTE);
	}
	
	/**
     * gibt die sespeicherte Endstunde von Wochentagen zurueck
     * @return
     */
	public static int endTimeHour() {
		// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_ENDTIME_HOUR_STRING,
	            Config.DAY_ENDTIME_HOUR);
	}
	
	/**
	 * berechnet die Dauer von Startzeitpunkt zum Endzeitpunkt
	 * @param pStarthour Stundeziffer der Startzeitpunkt
	 * @param pStartminute	Minutenziffer der Startzeitpunkt
	 * @param pEndhour	Stundenziffer der Endzeitpunkt
	 * @param pEndminute	Minutenziffer der Endzeitpunkt
	 * @return Dauer in Minuten
	 */
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
	
	/**
	 * errechnet die Anzahl der Timeslots die in dem Tag passt
	 * @return gibt die Anzahl der Timeslot in einem Tag zurueck
	 */
	public static int daytablelength() {
		int dur = duration(startTimeHour(),startTimeMinute(),endTimeHour(),endTimeMinute());
//        System.out.println(startTimeHour());
//        System.out.println(startTimeMinute());
//        System.out.println(endTimeHour());
//        System.out.println(endTimeMinute());
        return dur/Timeslot.timeslotlength();
	}


	
	/**
	 * eine private Hilfsklasse die, die Timeslots an die Tabelle verknuepft
	 * @author F.Vidjaja
	 * @version 0.1
	 */
	private static class DayTable {
		
	
	    /**
	     * Die Liste von Zeiteinheiten, aufsteigend sortiert nach ihren Anfangszeiten.
	     */
	    private List<Timeslot> timeslots;
	
	    /**
	     * Der Wochentag dieses Tagesplans.
	     */
	    private Weekday weekday;
	    
	    
	
	    /**
	     * Erzeugt einen neuen Tagesplan mit einer leeren Liste von Zeiteinheiten.
	     */
	    public DayTable(final Weekday pWeekday) {
	        timeslots = new ArrayList<>();
	        weekday = pWeekday;
	    }
		    
	    /**
	     * Fuegt die uebergebene Zeiteinheit zu diesem Tagesplan hinzu. Loest eine {@link IllegalArgumentException} aus, falls
	     * die uebergebene Zeiteinheit {@code null} ist
	     * 
	     * @param pTimeslot
	     *            die hinzuzufuegende Zeiteinheit
	     */
	    public void addTimeslot(final Timeslot pTimeslot) {
	        if (pTimeslot == null) {
	            throw new IllegalArgumentException("FIXME: configured exception string");
	        }
	        timeslots.add(pTimeslot);
	    }

	    /**
	     * Fuegt die uebergebene Zeiteinheit zu diesem Tagesplan hinzu. Loest eine {@link IllegalArgumentException} aus, falls
	     * die uebergebene Zeiteinheit {@code null} ist
	     * 
	     * @param pTimeslot
	     *            die hinzuzufuegende Zeiteinheit
	     */
	    public void addTimeslot(final ArrayList<Timeslot> pTimeslots) {
	        if (pTimeslots == null || pTimeslots.size() == 0) {
	            throw new IllegalArgumentException("FIXME: configured exception string");
	        }
	        timeslots.addAll(pTimeslots);
	    }
	    
	    /**
	     * Gibt den Wochentag dieses Tagesplans zurueck.
	     * 
	     * @return den Wochentag dieses Tagesplans
	     */
	    public Weekday getWeekday() {
	        return weekday;
	    }
	    
	    /**
	     * Gibt die Laenge der Liste von Timeslots zurueck
	     * @return
	     */
	    public int slotslength(){
	    	return timeslots.size();
	    }
	
	    /**
	     * Gibt die Zeiteinheit am gegebenen Positionsindex zurueck oder {@code null} falls ein ungueltiger Positionsindex
	     * zurueckgegeben wurde. Die erste Zeiteinheit des Tagesplans beginnt bei Index 0.
	     * 
	     * @param position
	     *            die Position der gesuchten Zeiteinheit
	     * @return die gesuchte Zeiteinheit oder {@code null}, falls eine ungueltige Position uebergeben wurde
	     */
	    public Timeslot getTimeslot(final int position) {
	        if (position >= 0 && position < timeslots.size()) {
	            return timeslots.get(position);
	        } else {
	            return null;
	        }
	    }
	}
	
}