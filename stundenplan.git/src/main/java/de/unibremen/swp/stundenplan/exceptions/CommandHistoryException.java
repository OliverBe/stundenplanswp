package de.unibremen.swp.stundenplan.exceptions;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;

@SuppressWarnings("serial")
public class CommandHistoryException extends Exception {
	
	/**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public CommandHistoryException(final String message) {
        super(message);
        JOptionPane.showMessageDialog(Stundenplan.getMain(), message);
    }
}
