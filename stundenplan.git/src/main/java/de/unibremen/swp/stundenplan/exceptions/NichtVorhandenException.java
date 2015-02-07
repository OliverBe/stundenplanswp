
package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;
/**
 *  Fuer Tests, beim Editieren von Daten, die nicht in der DB sind
 * @author Kim-Long
 *
 */
@SuppressWarnings("serial")
public class NichtVorhandenException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */  
    public NichtVorhandenException() {
    	WarningPanel.setText("Nicht vorhanden");
    }
}
