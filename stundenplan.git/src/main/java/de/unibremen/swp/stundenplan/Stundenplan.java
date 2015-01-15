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
package de.unibremen.swp.stundenplan;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;
import de.unibremen.swp.stundenplan.gui.MainFrame;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;
/**
 * Hauptklasse für den Stundenplan mit main-Methode. Erzeugt eine Konfiguration und initialisiert die Logik-Komponenten
 * und die GUI und zeigt dann das Hauptfenster an.
 * 
 * @author D. Lüdemann, K. Hölscher
 * @version 0.1
 */
public final class Stundenplan {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(Stundenplan.class.getName());
    
    private static MainFrame main;
    /**
     * Privater Konstruktor, der eine Instanziierung dieser Utility-Klasse verhindert.
     */
    private Stundenplan() {
    	Data.start();
    	final MainFrame mainFrame= new MainFrame();
    	 main = mainFrame;
    	try {
			Config.init(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//PEtest
    	if(PlanungseinheitManager.getAllPlanungseinheitFromDB().size() == 0){
    		PlanungseinheitManager.preparedemo();
    	}
    	}
    
    /**
     * Startet die Anwendung. Erzeugt dazu einen neuen Stundenplaner und dann das Hauptanzeigefenster und macht dieses
     * sichtbar. Der Pfad zur Konfigurationsdatei kann als Parameter übergeben werden.
     * 
     * Falls die Konfiguration nicht erzeugt werden kann, wird eine {@link IllegalStateException} ausgelöst.
     * 
     * @param args
     *            als erstes Argument kann der Pfad zur Konfigurationsdatei angegeben werden
     */
    public static void main(final String[] args) {
            final Stundenplan stundenplan= new Stundenplan();
    }
    
    public static MainFrame getMain() {
    	return main;
    }
}
