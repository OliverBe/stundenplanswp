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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.exceptions.BereitsVorhandenException;

/**
 * Integrationstest fuer DataPersonal mit Personal
 * @author Oliver
 *
 */
public class DataPersonalTest {

	@Rule
	  public ExpectedException exception = ExpectedException.none();
	
    @Before
    public void setUp() {
    	Data.start();
    	Data.deleteAll();
    }

    @Test
    public void testAddPersonalWithValidInput(){
    	//Not in DB
        Personal p1 = DataPersonal.getPersonalByKuerzel("No");
        assertNull(p1);
        //Create in DB
        ArrayList<String> array =  new ArrayList<String>();
        HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
        DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false, false,array, map));
        Personal p2 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals("Nobody", p2.getName());
        assertEquals("No", p2.getKuerzel());
        assertEquals(10, p2.getSollZeit());
        assertEquals(0, p2.getIstZeit());
        assertEquals(0, p2.getErsatzZeit());
        assertEquals(false, p2.isGependelt());
        assertEquals(false, p2.isLehrer());
        assertEquals(array, p2.getMoeglicheStundeninhalte());
        assertEquals(map, p2.getWunschzeiten());
    }
    
    @Test(expected = NullPointerException.class)  
    public void testAddPersonalWithNull(){
        DataPersonal.addPersonal(null);
    }
    
    @Test
    public void testAddPersonalWithValidInputButAlreadyInDB(){
    	//Not in DB
        Personal p1 = DataPersonal.getPersonalByKuerzel("No");
        assertNull(p1);
        //Create in DB
        ArrayList<String> array =  new ArrayList<String>();
        HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
        DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false, false,array, map));
        Personal p2 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals("No", p2.getKuerzel());
        assertEquals("Nobody", p2.getName());
        DataPersonal.addPersonal(new Personal("Yobody", "No", 10, 0, 0, false, false,array, map));
        Personal p3 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals("No", p3.getKuerzel());
        //still Nobody and not Yobody!
        assertEquals("Nobody", p3.getName());
    }
    
    @Test
    public void testAddPersonalWithToLongKuerzel(){
        // intercepted by GUI-Check-Methode - so not relevant here
    }
    
    @Test
    public void testDeletePersonalByValidInput(){
    	//Not in DB
        Personal p1 = DataPersonal.getPersonalByKuerzel("No");
        assertNull(p1);
        //Create in DB
        ArrayList<String> array =  new ArrayList<String>();
        HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
        DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false, false,array, map));
        Personal p2 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals("Nobody", p2.getName());
        assertEquals("No", p2.getKuerzel());
        //Delete from DB
        DataPersonal.deletePersonalByKuerzel("No");
        Personal p3 = DataPersonal.getPersonalByKuerzel("No");
        assertNull(p3);
    }
    
    @Test
    public void testGetPersonalByValidInput(){
    	//Not in DB
        Personal p1 = DataPersonal.getPersonalByKuerzel("No");
        assertNull(p1);
        //Create in DB
        ArrayList<String> array =  new ArrayList<String>();
        HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
        DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false, false,array, map));
        Personal p2 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals("Nobody", p2.getName());
        assertEquals("No", p2.getKuerzel());
        //Get from DB
        Personal p3 = DataPersonal.getPersonalByKuerzel("No");
        assertEquals(p2,p3);
    }

}
