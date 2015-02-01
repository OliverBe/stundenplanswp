package de.unibremen.swp.stundenplan.exceptions;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

@SuppressWarnings("serial")
public class BesetztException extends Exception {
	/**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */  
    public BesetztException(String besetztesObjekt) {
    	if(besetztesObjekt.equals("Personal")) WarningPanel.setText(besetztesObjekt + " ist bereits Klassenlehrer.");
    	if(besetztesObjekt.equals("Raum")) WarningPanel.setText(besetztesObjekt + " ist bereits Klassenraum.");
    }
}
