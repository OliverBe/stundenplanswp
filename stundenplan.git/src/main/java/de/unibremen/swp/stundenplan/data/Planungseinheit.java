package de.unibremen.swp.stundenplan.data;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Planungseinheit implements Serializable{
	
	@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	//lehrer, time[2] (anfangs,endzeit)
	private HashMap<Personal,Time[]> personal; 
	
	private ArrayList<Stundeninhalt> stundeninhalte = new ArrayList<Stundeninhalt>();
	
	private ArrayList<Room> raeume = new ArrayList<Room>();
	
	private ArrayList<Schoolclass> schulklassen = new ArrayList<Schoolclass>();

	private int starthour;
	private int startminute;
	private int endhour;
	private int endminute;
	
	public Planungseinheit(){
		
	}
	
	public void addStundeninhalt (final Stundeninhalt pSI){
		if(pSI== null){new IllegalArgumentException("Argument must be not null");}
		stundeninhalte.add(pSI);
	}
	
	public void addRoom (final Room pRoom){
		if(pRoom== null){new IllegalArgumentException("Argument must be not null");}
		raeume.add(pRoom);
	}
	
	public void addSchulklassen (final Schoolclass pSchoolclass){
		if(pSchoolclass== null){new IllegalArgumentException("Argument must be not null");}
		schulklassen.add(pSchoolclass);
	}
	
	public void addPersonal (final Personal pPerson, final Time[] pZeiten){
		if(pZeiten== null){new IllegalArgumentException("Argument must be not null");}
		personal.put(pPerson, pZeiten);
	}
	
	public ArrayList<Stundeninhalt> getStundeninhalte(){
		return stundeninhalte;
	}
	
	public ArrayList<Schoolclass> getSchoolclasses(){
	 return schulklassen;	
	} 
	
	public Schoolclass getSchoolclassByName(final String pName){
		if(pName == null || pName.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
		for(Schoolclass s : schulklassen){
			if(s.getName()==pName)return s;
		}
		return null;
	}
	
	public ArrayList<Room> getRooms(){
		return raeume;
	}
	
	public Room getRoomByName(final String pName){
		if(pName == null || pName.length()<= 0){new IllegalArgumentException("Argument must not be null or empty String");}
		for(Room r : raeume){
			if(r.getName()==pName)return r;
		}
		return null;
	}
	
	public HashMap<Personal, Time[]> getPersonal(){
		return personal;
	}
	
	public 
}
