
package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

/**
 * Exception wird geworfen, sobald keine Zahl oder eine Zahl < 0 oder 1 angegeben wird
 * @author Oliver
 */
@SuppressWarnings("serial")
public class ZahlException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */  
    public ZahlException() {
    	WarningPanel.setText("Feld benoetigt (gueltige, positive) Zahl");
    }

}
