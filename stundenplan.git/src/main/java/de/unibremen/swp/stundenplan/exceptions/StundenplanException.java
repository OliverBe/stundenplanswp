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

/**
 * Eine Ausnahme, die ein generelles Problem mit der Applikation anzeigt.
 * 
 * @author K. Hölscher, D. Lüdemann
 * @version 0.1
 */
public class StundenplanException extends Exception {

    /**
     * Die eineindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = -1537026985521086798L;

    /**
     * Erzeugt eine neue Ausnahme mit der gegebenen Nachricht.
     * 
     * @param message
     *            die Nachricht der neuen Ausnahme
     */
    public StundenplanException(final String message) {
        super(message);
    }
}
