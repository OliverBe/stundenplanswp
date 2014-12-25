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
package de.unibremen.swp.stundenplan.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import de.unibremen.swp.stundenplan.data.Teacher;

/**
 * Das ListModel für die Lehrer.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 * 
 */
public final class TeacherListModel extends DefaultListModel<String> {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = 5554982556376658908L;

    /**
     * Die Liste der Lehrkräfte dieses Modells.
     */
    private final List<Teacher> teachers;

    /**
     * Der Konstruktor für des TecherListMode. Hier wird nur die Liste der teacher initialisiert.
     */
    protected TeacherListModel() {
        super();
        teachers = new ArrayList<>();
    }

    /**
     * Fügt einen Lehrer hinzu.
     * 
     * @param teacher
     *            Der hinzuzufügende Lehrer.
     */
    protected void addTeacher(final Teacher teacher) {
        teachers.add(teacher);
        addElement(String.format("%s (%s) %sh", teacher.getName(), teacher.getAcronym(), teacher.getHoursPerWeek().intValue()));
    }

    /**
     * Gibt die Lehrkraft am gegebenen Index aus der Liste zurück.
     * 
     * @param index
     *            Der Index an dem der Lehrer steht, der zurückgegeben werden soll.
     * @return die Lehrkraft am gegebenen Index
     */
    protected Teacher getTeacherAt(final int index) {
        return teachers.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultListModel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        teachers.clear();
    }

}
