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
//import static org.powermock.api.mockito.PowerMockito.when;
//
//import java.math.BigDecimal;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.experimental.categories.Category;
//import org.junit.runner.RunWith;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import de.unibremen.swp.stundenplan.IntegrationTest;
//import de.unibremen.swp.stundenplan.config.Config;
//import de.unibremen.swp.stundenplan.data.Teacher;
//import de.unibremen.swp.stundenplan.db.Data;
//import de.unibremen.swp.stundenplan.exceptions.DatasetException;
//import de.unibremen.swp.stundenplan.gui.Timeslot;
//
///**
// * Integrationstest für die Klassen {@link TeacherManager}, {@link Data} und {@link Teacher}. Die Methode
// * {@link TeacherManager#init()} wird nicht getestet, daher wird die private Methode {@code fillDefaultData()} nicht
// * aufgerufen. Daher sind keine Stubs für die Klasse {@link Timeslot} nötig.
// * 
// * @author K. Hölscher
// * @version 0.1
// * 
// */
//@PowerMockIgnore("javax.management.*")
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Config.class)
//@Category(IntegrationTest.class)
//public class TeacherManagerDataIntTest {
//
//    /**
//     * Der statische Initialisierer der Klasse {@link Data} benötigt eine gültige Konfiguration und daraus einen Wert.
//     * Wir mocken den entsprechenden Methodenaufruf.
//     */
//    @Before
//    public void setUp() {
//        PowerMockito.mockStatic(Config.class);
//        when(Config.getString("persistence_name", "stundenplan")).thenReturn("stundenplan");
//    }
//
//    /**
//     * Testet die Methode {@link TeacherManager#addTeacher(String, String, String)} mit gültigen Werten.
//     * 
//     * @throws DatasetException
//     *             falls es ein Problem mit der unterliegenden Datenbank gibt
//     */
//    @Test
//    public void testAddTeacherWithValidInput() throws DatasetException {
//        /*
//         * Wir stellen sicher, dass es keinen Lehrer mit dem Kürzel Tes gibt.
//         */
//        Teacher t = Data.getTeacherByAcronym("Tes");
//        assertNull(t);
//        TeacherManager.addTeacher("Tes", "Test Tester", "23.25");
//        t = Data.getTeacherByAcronym("Tes");
//        assertEquals("Tes", t.getAcronym());
//        assertEquals("Test Tester", t.getName());
//        assertEquals(new BigDecimal("23.25"), t.getHoursPerWeek());
//    }
//
//}
