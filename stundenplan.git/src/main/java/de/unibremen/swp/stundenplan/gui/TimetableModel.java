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
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Das TimetableModel fuer die Timetable. Es handelt sich hierbei um ein Model, dass bestimmt, welche Daten in der
 * Tabelle vorhanden sind und wie angezeigt werden. Dazu erbt TimetableModel von AbstractTableModel.
 * 
 * @author D. Luedemann
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
	try {
			Config.init(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()Fuer Jeden Wochentag eine Spalte und fuer Stundenanzeige ganz
     * links eine Spalte.
     */
    @Override
    public int getColumnCount() {
        return TimetableManager.validdays().length+1;
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
        	if(owner!=null){
            if(owner instanceof Personal){
            	Personal p = (Personal)owner; 
            	return p.getName();
            }else if(owner instanceof Room){
            	Room r = (Room)owner;
            	return r.getName();
            }else if(owner instanceof Schoolclass){
            	Schoolclass sc = (Schoolclass)owner;
            	return sc.getName();
        	}else{
        	return "";
        		}
            }
        	}
        final int index = col - 1;
        final Weekday[] weekdays = TimetableManager.validdays();
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
     * Tabelle anzeigen. Die weiteren Spalten greifen auf die Timeslots des TimetableManagers zurueck.
     */
    @Override
    public Object getValueAt(final int row, final int col) {
        if (col == 0) {
            return TimetableManager.getTimeframeDisplay(row);
        } else {
            if(owner==null){
            	return new Timeslot(TimetableManager.validdays()[col-1]);
            }
			return TimetableManager.getTimeslotAt(TimetableManager.validdays()[col - 1], row, owner);      
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
    
    public Object getOwner() {
    	return owner;
    }

}
