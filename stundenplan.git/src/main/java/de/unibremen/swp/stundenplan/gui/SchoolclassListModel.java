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

import de.unibremen.swp.stundenplan.data.Schoolclass;

/**
 * Das ListModel für die Klassen.
 * 
 * @author F. Vidjaja,
 * @version 0.1
 * 
 */
public final class SchoolclassListModel extends DefaultListModel<String> {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = 5564982556376658908L;

    /**
     * Die Liste der Schulklassen dieses Modells.
     */
    private final List<Schoolclass> classes;

    /**
     * Der Konstruktor für des SchoolclassListModel. Hier wird nur die Liste der schoolclass initialisiert.
     */
    protected SchoolclassListModel() {
        super();
        classes = new ArrayList<>();
    }

    /**
     * Fügt eine Schulklasse hinzu.
     * 
     * @param schoolclass
     *            die hinzuzuf�gende schulklasse.
     */
    protected void addSchoolclass(final Schoolclass schoolclass) {
        classes.add(schoolclass);
        addElement(String.format("%s", schoolclass.getName()));
    }

    /**
     * Gibt die Schulklasse am gegebenen Index aus der Liste zurück.
     * 
     * @param index
     *            Der Index an dem die Schulklasse steht, der zurückgegeben werden soll.
     * @return die Schulklasse am gegebenen Index
     */
    protected Schoolclass getSchoolclassAt(final int index) {
        return classes.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.DefaultListModel#clear()
     */
    @Override
    public void clear() {
        super.clear();
        classes.clear();
    }

}

