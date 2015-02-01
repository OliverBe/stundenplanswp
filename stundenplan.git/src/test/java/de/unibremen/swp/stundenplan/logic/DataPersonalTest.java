///*
// * Copyright 2014 AG Softwaretechnik, University of Bremen, Germany
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//package de.unibremen.swp.stundenplan.logic;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import de.unibremen.swp.stundenplan.config.Weekday;
//import de.unibremen.swp.stundenplan.data.Personal;
//import de.unibremen.swp.stundenplan.db.Data;
//import de.unibremen.swp.stundenplan.db.DataPersonal;
//
//
//public class DataPersonalTest {
//
//    /**
//     * Der statische Initialisierer der Klasse {@link Data} benötigt eine gültige Konfiguration und daraus einen Wert.
//     * Wir mocken den entsprechenden Methodenaufruf.
//     */
//    @Before
//    public void setUp() {
//    	Data.start();
//    	Data.deleteAll();
////        PowerMockito.mockStatic(Config.class);
////        when(Config.getString("persistence_name", "stundenplan")).thenReturn("stundenplan");
//    }
//
//    @Test
//    public void testAddPersonalWithValidInput(){
//        Personal p1 = DataPersonal.getPersonalByKuerzel("No");
//        assertNull(p1);
//        ArrayList<String> array =  new ArrayList<String>();
//        HashMap<Weekday, int[]> map = new HashMap<Weekday, int[]>();
//        DataPersonal.addPersonal(new Personal("Nobody", "No", 10, 0, 0, false, false,array, map));
//        Personal p2 = DataPersonal.getPersonalByKuerzel("No");
//        assertEquals("Nobody", p2.getName());
//        assertEquals("No", p2.getKuerzel());
//        assertEquals(10, p2.getSollZeit());
//        assertEquals(0, p2.getIstZeit());
//        assertEquals(0, p2.getErsatzZeit());
//        assertEquals(false, p2.isGependelt());
//        assertEquals(false, p2.isLehrer());
//        assertEquals(array, p2.getMoeglicheStundeninhalte());
//        assertEquals(map, p2.getWunschzeiten());
//    }
//
//}
