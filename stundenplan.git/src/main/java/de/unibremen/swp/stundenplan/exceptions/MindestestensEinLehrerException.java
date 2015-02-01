
package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

/**
 * Exception wird geworfen, sobald kein Lehrer im Lehrerteam ist
 * @author Oliver
 */
@SuppressWarnings("serial")
public class MindestestensEinLehrerException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public MindestestensEinLehrerException() {     
    	WarningPanel.setText("Kein Lehrer im Team");
    }

}
