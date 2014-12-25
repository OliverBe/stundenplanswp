package data;

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
	
	//lehrerid, time[2] (anfangs,endzeit)
	private HashMap<Integer,Time[]> personal; 
	
	private ArrayList<Stundeninhalt> stundeninhalte;
	
	private ArrayList<Room> raeume;
	
	private ArrayList<Schoolclass> schulklassen;
	
	private Calendar startzeit;
	
	private Calendar endzeit;	
	
}
