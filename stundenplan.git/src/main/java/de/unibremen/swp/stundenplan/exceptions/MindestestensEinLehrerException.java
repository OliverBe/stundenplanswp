
package de.unibremen.swp.stundenplan.exceptions;

import java.awt.Color;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.gui.MainFrame;
import de.unibremen.swp.stundenplan.gui.WarningPanel;

public class MindestestensEinLehrerException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgelösten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public MindestestensEinLehrerException(final String message) {
        super(message);
        JOptionPane.showMessageDialog(Stundenplan.getMain(), message);
        
    }
    
    public MindestestensEinLehrerException() {
        super();       
        WarningPanel.setText("Sie müssen mindestens einen Lehrer im Team haben.");
    }

}
