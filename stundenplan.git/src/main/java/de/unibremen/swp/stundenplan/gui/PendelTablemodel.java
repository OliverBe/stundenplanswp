package de.unibremen.swp.stundenplan.gui;

import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Das PendeltableModel fuer die Pendeltabelle. Es handelt sich hierbei um ein Model, dass bestimmt, welche Daten in der
 * Tabelle vorhanden sind und wie angezeigt werden. Dazu erbt PendeltableModel von AbstractTableModel.
 * 
 * @author Fathan Vidjaja
 * @version alpha
 * 
 */
public class PendelTablemodel extends AbstractTableModel {

    /**
     * Die generierte serialVersionUID.
     */
    private static final long serialVersionUID = -903748217005098057L;
    
    private Personal owner;
    
    public PendelTablemodel(Personal pOwner) {
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
        		return "";
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
        return PlanungseinheitManager.pendelTlength(owner);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int) Hier darauf geachtet, dass wir ganz links die 
     * die Erklaerunf fuer die Zellen angeben. Die weiteren Spalten greifen auf die Pendeltag.
     */
    @Override
    public Object getValueAt(final int row, final int col) {
        if (col == 0) {
            return "Zeitraum\nZielraum \nZielgeb.";
        } else {
            	return PlanungseinheitManager.getPendelString(TimetableManager.validdays()[col - 1], row, owner);
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
