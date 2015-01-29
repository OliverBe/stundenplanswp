package de.unibremen.swp.stundenplan.data;

import java.util.ArrayList;
import java.util.HashMap;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

public class Planungseinheit{

    private int id;
	
	//lehrerkuerzel, time[2] (anfangs,endzeit)
	private HashMap<String, int[]> personal = new HashMap<String, int[]>(); 
	
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
		personal.put(pPerson.getKuerzel(), pZeiten);
	}
	
	public ArrayList<String> getStundeninhalte(){
		return stundeninhalte;
	}
	
	public String schoolclassestoString(){
		StringBuilder sb = new StringBuilder();
		for(String sc : schulklassen){
			sb.append(sc);
			sb.append(",");
		}
		if (schulklassen.size() != 0){ sb.deleteCharAt(sb.length()-1);}
		return sb.toString();
	}
	
	public String roomstoString(){
		StringBuilder sb = new StringBuilder();
		if(raeume.size() > 1){sb.append("Band:");}
		for(String r : raeume){
			sb.append(r);
			sb.append(",");
		}
		if (raeume.size() != 0){ sb.deleteCharAt(sb.length()-1);}
		return sb.toString();
	}
	
	public String personaltoString(){
		StringBuilder sb = new StringBuilder();
		for(String kuerzel : personal.keySet()){
			sb.append(kuerzel);
			sb.append(",");
		}
		if (personal.keySet().size() != 0){ sb.deleteCharAt(sb.length()-1);}
		return sb.toString();
	}
	
	public String stundenInhaltetoString(){
		if(stundeninhalte.size() == 0){return "Teamzeit";}
		StringBuilder sb = new StringBuilder();
		for(String si : stundeninhalte){
			sb.append(si);
			sb.append(",");
		}
		if (stundeninhalte.size() != 0){ sb.deleteCharAt(sb.length()-1);}
		return sb.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<String> getSchoolclasses(){
	 return schulklassen;	
	} 
	
	public Schoolclass getSchoolclassByName(final String pName){
		if(pName == null || pName.length()<= 0){throw new IllegalArgumentException("Argument must not be null or empty String");}
		for(String name : schulklassen){
			if(name.equals(pName)) return DataSchulklasse.getSchulklasseByName(name);
		}
		return null;
	}
	
	public boolean containsClass(Schoolclass pSC){
		if(pSC == null){throw new IllegalArgumentException("Argument must not be null");}
		return schulklassen.contains(pSC.getName());
	}
	
	public ArrayList<String> getRooms(){
		return raeume;
	}
	
	public Room getRoomByName(final String pName){
		if(pName == null || pName.length()<= 0){throw new IllegalArgumentException("Argument must not be null or empty String");}
		for(String name : raeume){
			if(name.equals(pName)) return DataRaum.getRaumByName(name);
		}
		return null;
	}
	
	public boolean containsRoom(Room pRoom){
		if(pRoom == null){throw new IllegalArgumentException("Argument must not be null or empty String");}
		return raeume.contains(pRoom.getName());
	}
	
	public HashMap<String, int[]> getPersonalMap(){
		return personal;
	}
	
	public ArrayList<Personal> getPersonal(){
		ArrayList<Personal> personalArray = new ArrayList<Personal>();
		for(String kuerzel : personal.keySet()) {
			personalArray.add(DataPersonal.getPersonalByKuerzel(kuerzel));
		}
		return personalArray;
	}
	
	/*
	 * gibt Personal fuer eine Name oder Kuerzel zur��ck.
	 */
	public Personal getPersonalbyKuerzel(final String pKuerzel){
		if(pKuerzel == null || pKuerzel.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
		for(String kuerzel : personal.keySet()){
			if(kuerzel.equals(pKuerzel)){
				return DataPersonal.getPersonalByKuerzel(kuerzel);
			}
		}
		return null;
	}
	
	public Personal getPersonalbyIndex(final int pIndex){
		if(pIndex < 0){new IllegalArgumentException("Argument must not be less than zero");}
		ArrayList<Personal> pl = new ArrayList<Personal>();
		for(String kuerzel : personal.keySet()){
			pl.add(DataPersonal.getPersonalByKuerzel(kuerzel));
		}
		return pl.get(pIndex);
	}
	
	public int[] getTimesofPersonal(final Personal pPerson){
		if(pPerson == null ){new IllegalArgumentException("Argument must not be null");}
		return personal.get(pPerson.getKuerzel());
	}
	
	public boolean containsPersonal(final Personal pPerson){
		if(pPerson == null ){new IllegalArgumentException("Argument must not be null");}
		if(personal.get(pPerson.getKuerzel()) != null){return true;}
		return false;
	}
	
	public boolean isWeekday(Weekday pWeekday){
		if(pWeekday == null){new IllegalArgumentException("Argument must not be null");}
		if(day.getOrdinal()== pWeekday.getOrdinal()){return true;}
		return false;
	}
	
	/**
	 * Methode nur von Planungseinheitmanager.getPEforPersonalAndWeekday zu nutzen
	 * setzt die Zeiten der PE mit der Eingabe eines 4-elementigen Array.
	 * @param time
	 */
	public void setTime(final int[] time){
		if(time.length != 4){return;}
		setStarthour(time[0]);
		setStartminute(time[1]);
		setEndhour(time[2]);
		setEndminute(time[3]);
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
	 * @return gib die Dauer der Planungseinheit in Minuten zur��ck
	 */
	public int duration(){
		int dur = TimetableManager.duration(startHour, startMin, endHour, endMin);
	    return dur;
	}
	
	public void setId(int pId){
	 id = pId;	
	}
}
