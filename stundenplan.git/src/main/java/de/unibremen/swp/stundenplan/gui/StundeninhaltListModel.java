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

import de.unibremen.swp.stundenplan.data.Subject;

/**
 * Das ListModel für die Faecher.
 * 
 * @author Belavic, Oliver
 * @version 0.1
 * 
 */
public final class StundeninhaltListModel extends DefaultListModel<String> {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = 5554982556376658908L;

    /**
     * Die Liste der Faecher dieses Modells.
     */
    private final List<Subject> subjects;

    /**
     * Der Konstruktor für des FaecherListMode. Hier wird nur die Liste der faecher initialisiert.
     */
    protected StundeninhaltListModel() {
        super();
        subjects = new ArrayList<>();
    }

    /**
     * Fügt ein Fach hinzu.
     * 
     * @param teacher
     *            Das hinzuzufügende Fach.
     */
    protected void addSubject(final Subject subject) {
        subjects.add(subject);
        addElement(String.format("%s (%s)", subject.getName(), subject.getAcronym()));
    }

    /**
     * Gibt das fach am gegebenen Index aus der Liste zurück.
     * 
     * @param index
     *            Das Fach an dem der Index steht, der zurückgegeben werden soll.
     * @return die Fach am gegebenen Index
     */
    protected Subject getSubjectAt(final int index) {
        return subjects.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultListModel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        subjects.clear();
    }

    public List<Subject> getSubjects(){
    	return subjects;
    }
}
