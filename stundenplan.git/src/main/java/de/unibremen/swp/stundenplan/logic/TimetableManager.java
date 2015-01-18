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
 * Diese Utility-Klasse verwaltet die Tagespläne.
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
    
    public static Weekday[] validdays(){
       Weekday[] w = new Weekday[givevaliddays().size()];
       return givevaliddays().toArray(w);
    }
    
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
        DayTable dayTable = createTimeslotsForPES(PlanungseinheitManager.demomethod(pWeekday), pWeekday);
        tscounter += 1 ;
        System.out.println("ts:"+tscounter);
        return dayTable;
   }
    
    private static boolean sametimeofday(Calendar cal1, Calendar cal2){
 	   boolean sametime = cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE);
 	   return sametime;
    } 

    /**
     * erzeugt eine Tagestabelle für eine Liste von Planungseinheiten
     * @param pPE Liste von Planungseinheiten
     * @param pWeekday den Tag für die Tabelle
     * @return gibt eine DayTable zurück
     */
    private static DayTable createTimeslotsForPES(ArrayList<Planungseinheit> pPE,Weekday pWeekday){
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR_OF_DAY, startTimeHour());
        cal.set(Calendar.MINUTE, startTimeMinute());
        DayTable dayTable = new DayTable(pWeekday);
        if(pPE.size() == 0 ){
        	dayTable.addTimeslot(filltoEnd(dayTable, cal));
        	System.out.println("nothing");
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
        System.out.println(dayTable.slotslength());
        System.out.println(daytablelength());
        if(dayTable.slotslength()<daytablelength()){
        	dayTable.addTimeslot(filltoEnd(dayTable, cal));
        }
        counter +=1;
        System.out.println("Zaehler"+counter);
        return dayTable;
    
    }
    /**
     * Erzeugt Zeiteinheiten für den gegebenen Tag und Person.
     * 
     * @param dayTable
     *            der Tagesplan, für den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForPersonal(Weekday pWeekday, Personal pPerson) {
        ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForPersonalbyWeekday(pWeekday, pPerson);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
    }
    
    /**
     * füll eine Tagesplan mit leeren Timeslots falls Tabelle noch nicht voll ist
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
        System.out.println("rest"+timeslot.size());
    	return timeslot;
	}

	/**
     * Erzeugt Zeiteinheiten für den gegebenen Tag und Schulklasse.
     * 
     * @param dayTable
     *            der Tagesplan, für den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForSchoolclass(Weekday pWeekday, Schoolclass pSC) {
    	ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForSchoolclassbyWeekday(pWeekday, pSC);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
        }
    
    /**
     * Erzeugt Zeiteinheiten für den gegebenen Tag und Raum.
     * 
     * @param dayTable
     *            der Tagesplan, für den die Zeiteinheiten erstellt werden sollen
     */
    private static DayTable createTimeslotsForRoom(Weekday pWeekday,Room pRoom) {
    	ArrayList<Planungseinheit> pE = PlanungseinheitManager.getPEForRoombyWeekday(pWeekday, pRoom);
        DayTable dayTable = createTimeslotsForPES(pE, pWeekday);
        return dayTable;
   }
    
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
    		t.setKlassentext(pPE);
    		t.setPersonaltext(pPE);
    		t.setRaumtext(pPE);
    		t.setStundeninhalttext(pPE);
    		t.setpe(pPE);
    		timeslots.add(t);
    		final Calendar newCal = Calendar.getInstance();
    		newCal.setTimeInMillis(cal.getTimeInMillis());
            t.setstartzeit(newCal);
            cal.add(Calendar.MINUTE, Timeslot.timeslotlength());
        }
        return timeslots;
    }
    
    
    
    public static void peTest(){
    	DayTable tp = createTimeslotsForPES(PlanungseinheitManager.demomethod(Weekday.values()[0]), Weekday.values()[0]);
    	System.out.println(tp.timeslots.size());
    	for(int i = 0; i<daytablelength();i++){
    		Timeslot t = tp.getTimeslot(i);
    		System.out.println("Test"+t.getTimeDisplay());
    	}
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
     * Gibt die Zeiteinheit an der gegebenen Position für den gegebenen Wochentag zurück. Falls die Index-Angaben
     * außerhalb der jeweils gültigen Bereiche liegen, wird {@code null} zurückgegeben.
     * 
     * @param weekday
     *            der Wochentag der gesuchten Zeiteinheit
     * @param position
     *            die Position der gesuchten Zeiteinheit am gegebenen Wochentag
     * @return die gesuchte Zeiteinheit oder {@code null}, falls unsinnige Parameterwerte übergeben wurden
     * @throws DatasetException
     *             falls es ein Problem bei der Abfrage des unterliegenden Datenbestandes gibt oder der Datenbestand
     *             inkonsistent ist
     */
    public static Timeslot getTimeslotAt(final Weekday weekday, final int position) throws DatasetException {
        DayTable dayTable;
        dayTable = createTimeslots(weekday);
        if (dayTable == null) {
            return null;
        }
        return dayTable.getTimeslot(position);
    }
    
    
    /**
     * Gibt die Zeiteinheit an der gegebenen Position für den gegebenen Wochentag zurück. Falls die Index-Angaben
     * außerhalb der jeweils gültigen Bereiche liegen, wird {@code null} zurückgegeben.
     * 
     * @param weekday
     *            der Wochentag der gesuchten Zeiteinheit
     * @param position
     *            die Position der gesuchten Zeiteinheit am gegebenen Wochentag
     * @return die gesuchte Zeiteinheit oder {@code null}, falls unsinnige Parameterwerte übergeben wurden
     * @throws DatasetException
     *             falls es ein Problem bei der Abfrage des unterliegenden Datenbestandes gibt oder der Datenbestand
     *             inkonsistent ist
     */
    public static Timeslot getTimeslotAt(final Weekday weekday, final int position, Object clazz) throws DatasetException {
        DayTable dayTable;
        if(clazz instanceof Personal){
        	System.out.println("Personal Time Table");
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
        time.add(Calendar.MINUTE, Timeslot.timeslotlength() * position);
        String display = String.format("%02d:%02d - ", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        time.add(Calendar.MINUTE, Timeslot.timeslotlength());
        display += String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
        return display;
    }

    public static int startTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_MINUTE_STRING,
	            Config.DAY_STARTTIME_MINUTE);
	}

	public static int startTimeHour() {
		// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_STARTTIME_HOUR_STRING,
	            Config.DAY_STARTTIME_HOUR);
	}

	public static int endTimeMinute() {
	// TODO Auto-generated method stub
		return Config.getInt(Config.DAY_ENDTIME_MINUTE_STRING,
	            Config.DAY_ENDTIME_MINUTE);
	}

	public static int endTimeHour() {
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
//        System.out.println(startTimeHour());
//        System.out.println(startTimeMinute());
//        System.out.println(endTimeHour());
//        System.out.println(endTimeMinute());
        return dur/Timeslot.timeslotlength();
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
//	     * Fügt die übergebene Zeiteinheit zu diesem Tagesplan hinzu. Löst eine {@link IllegalArgumentException} aus, falls
//	     * die übergebene Zeiteinheit {@code null} ist
//	     * 
//	     * @param pTimeslot
//	     *            die hinzuzufügende Zeiteinheit
//	     */
	    public void addTimeslot(final ArrayList<Timeslot> pTimeslots) {
	        if (pTimeslots == null || pTimeslots.size() == 0) {
	            throw new IllegalArgumentException("FIXME: configured exception string");
	        }
	        timeslots.addAll(pTimeslots);
	    }
	    
//	    /**
//	     * Gibt den Wochentag dieses Tagesplans zurück.
//	     * 
//	     * @return den Wochentag dieses Tagesplans
//	     */
	    public Weekday getWeekday() {
	        return weekday;
	    }
	    
	    public int slotslength(){
	    	return timeslots.size();
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