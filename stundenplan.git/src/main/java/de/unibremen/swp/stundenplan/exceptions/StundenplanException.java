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

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;

/**
 * Eine Ausnahme, die ein generelles Problem mit der Applikation anzeigt.
 * 
 * @author K. Hölscher, D. Lüdemann
 * @version 1.0
 * @editor Kulturmeister
 */
@SuppressWarnings("serial")
public class StundenplanException extends Exception {

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * 
     * Bei einer ausgeloesten Exception wird im Warningpanel eine Fehlermeldung ausgegeben
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public StundenplanException(final String message) {
        super(message);
        JOptionPane.showMessageDialog(Stundenplan.getMain(), message);
    }
}
