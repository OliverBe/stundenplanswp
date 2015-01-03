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

import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Das TimetableModel für die Timetable. Es handelt sich hierbei um ein Model, dass bestimmt, welche Daten in der
 * Tabelle vorhanden sind und wie angezeigt werden. Dazu erbt TimetableModel von AbstractTableModel.
 * 
 * @author D. Lüdemann
 * @version 0.1
 * 
 */
public class TimetableModel extends AbstractTableModel {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = -903748217005098057L;
    
    private Object owner;
    
    public TimetableModel() {
    	super();
    	owner = null;
    	try {
 			Config.init(null);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }
    
    public TimetableModel(Object pOwner) {
	super();
	owner = pOwner;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()Für Jeden Wochentag eine Spalte und für Stundenanzeige ganz
     * links eine Spalte.
     */
    @Override
    public int getColumnCount() {
        return Weekday.values().length + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     * 
     * Hier werden die Wochentage als Spaltennamen benutzt.
     */
    @Override
    public String getColumnName(final int col) {
        if (col == 0) {
            return "";
        }
        final int index = col - 1;
        final Weekday[] weekdays = Weekday.values();
        if (index >= 0 && index < weekdays.length) {
            return weekdays[index].toString();
        }
        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return TimetableManager.daytablelength();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int) Hier darauf geachtet, dass wir ganz links die Uhrzeit der
     * Tabelle anzeigen. Die weiteren Spalten greifen auf die Timeslots des TimetableManagers zurück.
     */
    @Override
    public Object getValueAt(final int row, final int col) {
        if (col == 0) {
            return TimetableManager.getTimeframeDisplay(row);
        } else {
            try {
            	return TimetableManager.getTimeslotAt(Weekday.values()[col - 1], row);
                //return TimetableManager.getTimeslotAt(Weekday.values()[col - 1], row, owner);
            } catch (DatasetException e) {
                /*
                 * Exception wurde schon protokolliert.
                 */
                return null;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)Zellen sind nie editierbar.
     */
    @Override
    public boolean isCellEditable(final int row, final int col) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class getColumnClass(final int col) {
        return getValueAt(0, col).getClass();
    }

}