package de.unibremen.swp.stundenplan.exceptions;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;

public class CommandHistoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
