package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

public class Planungseinheit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private int id;
	
	//lehrer, time[2] (anfangs,endzeit)
	private HashMap<Personal, int[]> personal = new HashMap<Personal, int[]>(); 
	
	private ArrayList<String> stundeninhalte = new ArrayList<String>();
	
	private ArrayList<String> raeume = new ArrayList<String>();
	
	private ArrayList<String> schulklassen = new ArrayList<String>();

	private int startHour;
	private int startMin;
	private int endHour;
	private int endMin;
	
	private Weekday day;
	
	public Planungseinheit(){
	}
	
	public Planungseinheit(int pId, Weekday pDay, int pStartHour, int pStartMin, int pEndHour, int pEndMin) {
		id = pId;
		day = pDay;
		startHour = pStartHour;
		startMin = pStartMin;
		endHour = pEndHour;
		endMin = pEndMin;
	}
	
	public void setWeekday(Weekday pWeekday){
		if(pWeekday== null){new IllegalArgumentException("Argument must be not null");}
		day = pWeekday;
	}
	
	public Weekday getWeekday(){
		return day;
	}
	
	public void addStundeninhalt (final Stundeninhalt pSI){
		if(pSI== null){new IllegalArgumentException("Argument must be not null");}
		stundeninhalte.add(pSI.getKuerzel());
	}
	
	public void addRoom (final Room pRoom){
		if(pRoom== null){new IllegalArgumentException("Argument must be not null");}
		raeume.add(pRoom.getName());
	}
	
	public void addSchulklassen (final Schoolclass pSchoolclass){
		if(pSchoolclass== null){new IllegalArgumentException("Argument must be not null");}
		schulklassen.add(pSchoolclass.getName());
	}
	
	public void addPersonal (final Personal pPerson, final int[] pZeiten){
		if(pZeiten== null){new IllegalArgumentException("Argument must be not null");}
		personal.put(pPerson, pZeiten);
	}
	
	public ArrayList<String> getStundeninhalte(){
		return stundeninhalte;
	}
	
	public String schoolclassestoString(){
		StringBuilder sb = new StringBuilder();
		for(String sc : schulklassen){
			sb.append(sc);
			sb.append(" ,");
		}
		return sb.toString();
	}
	
	public String roomstoString(){
		StringBuilder sb = new StringBuilder();
		for(String r : raeume){
			sb.append(r);
			sb.append(" ,");
		}
		return sb.toString();
	}
	
	public String personaltoString(){
		StringBuilder sb = new StringBuilder();
		for(Personal p : personal.keySet()){
			sb.append(p.getKuerzel());
			sb.append(" ,");
		}
		return sb.toString();
	}
	
	public String stundenInhaltetoString(){
		StringBuilder sb = new StringBuilder();
		for(String si : stundeninhalte){
			sb.append(si);
			sb.append(" ,");
		}
		return sb.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<String> getSchoolclasses(){
	 return schulklassen;	
	} 
	
//	public Schoolclass getSchoolclassByName(final String pName){
//		if(pName == null || pName.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
//		for(String s : schulklassen){
//			if(s.getName().equals(pName))return s;
//		}
//		return null;
//	}
	
	public ArrayList<String> getRooms(){
		return raeume;
	}
	
//	public Room getRoomByName(final String pName){
//		if(pName == null || pName.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
//		for(Room r : raeume){
//			if(r.getName().equals(pName))return r;
//		}
//		return null;
//	}
	
	public HashMap<Personal, int[]> getPersonalMap(){
		return personal;
	}
	
	public ArrayList<Personal> getPersonal(){
		return new ArrayList<Personal>(personal.keySet());
	}
	
	/*
	 * gibt Personal fuer eine Name oder Kuerzel zurück.
	 */
	public Personal getPersonalbyName(final String pName){
		if(pName == null || pName.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
		for(Personal p: personal.keySet()){
			if(p.getKuerzel().equals(pName) || p.getName().equals(pName)){
				return p;
			}
		}
		return null;
	}
	
	public Personal getPersonalbyIndex(final int pIndex){
		if(pIndex < 0){new IllegalArgumentException("Argument must not be less than zero");}
		ArrayList<Personal> pl = new ArrayList<Personal>(personal.keySet());
		return pl.get(pIndex);
	}
	
	public int[] getTimesofPersonal(final Personal pPerson){
		if(pPerson == null ){new IllegalArgumentException("Argument must not be null");}
		return personal.get(pPerson);
	}
	
	public boolean containsPersonal(final Personal pPerson){
		if(pPerson == null ){new IllegalArgumentException("Argument must not be null");}
		if(personal.get(pPerson) != null){return true;}
		return false;
	}
	
	public int getStartHour(){
		return startHour;
	}
	
	public void setStarthour(final int pStarthour){
		if(pStarthour < 0){throw new IllegalArgumentException("Argument must not be less than 0");}
		startHour = pStarthour;
	}
	
	public int getStartminute(){
		return startMin;
	}
	
	public void setStartminute(final int pStartminute){
		if(pStartminute < 0){throw new IllegalArgumentException("Argument must not be less than 0");}
		startMin = pStartminute;
	}
	
	public int getEndhour(){
		return endHour;
	}
	
	public void setEndhour(final int pEndhour){
		if(pEndhour < 0){throw new IllegalArgumentException("Argument must not be less than 0");}
		endHour = pEndhour;
	}
	
	public int getEndminute(){
		return endMin;
	}
	
	public void setEndminute(final int pEndminute){
		if(pEndminute < 0){throw new IllegalArgumentException("Argument must not be less than 0");}
		endMin = pEndminute;
	}
	
	/**
	 *  Rechnet die Dauer des Planungseinheit aus
	 * @return gib die Dauer der Planungseinheit in Minuten zurück
	 */
	public int duration(){
		int dur = TimetableManager.duration(startHour, startMin, endHour, endMin);
	    return dur;
	}
}
