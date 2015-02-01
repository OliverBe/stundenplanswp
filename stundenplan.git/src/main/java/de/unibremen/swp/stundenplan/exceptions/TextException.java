
package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

/**
 * Exception wird geworfen, sobald ein Textfeld leer ist
 * @author Oliver
 */
@SuppressWarnings("serial")
public class TextException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */  
    public TextException() {
    	WarningPanel.setText("Feld ist leer");
    }

}
