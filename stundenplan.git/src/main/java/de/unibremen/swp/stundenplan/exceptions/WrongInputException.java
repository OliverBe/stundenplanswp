/*
 * Copyright 2014 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.unibremen.swp.stundenplan.exceptions;

import java.awt.Color;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.gui.MainFrame;

/**
 * Eine Ausnahme, die ein Problem mit der Persistierungskomponente anzeigt.
 * 
 * @author K. HÃ¶lscher
 * @version 0.1
 */
public class WrongInputException extends Exception {

    /**
     * Die eineindeutige ID fÃ¼r Serialisierung.
     */
    private static final long serialVersionUID = -7499912411622348365L;

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgelösten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public WrongInputException(final String message) {
        super(message);
        JOptionPane.showMessageDialog(Stundenplan.getMain(), message);
        
    }
    
    public WrongInputException() {
        super();
        JOptionPane.showMessageDialog(Stundenplan.getMain(), "Eine ihrer Angaben ist nicht zulässig. "
        		+ "Haben Sie vielleicht einen Buchstaben zu viel geschrieben "
        		+ "oder eine Zeile leer gelassen?");
        
    }

}
