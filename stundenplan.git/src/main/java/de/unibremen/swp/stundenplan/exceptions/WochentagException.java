
package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

/**
 * Exception wird geworfen, sobald Kuerzel zu lang ist
 * @author Oliver
 */
@SuppressWarnings("serial")
public class WochentagException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgelösten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */  
    public WochentagException() {
        WarningPanel.setText("Kein Wochentag gewaehlt");
    }

}
