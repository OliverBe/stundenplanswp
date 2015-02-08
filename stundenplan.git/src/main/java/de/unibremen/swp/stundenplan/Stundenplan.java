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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.Timer;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.gui.MainFrame;
/**
 * Hauptklasse fuer den Stundenplan mit main-Methode. Erzeugt eine Konfiguration und initialisiert die Logik-Komponenten
 * und die GUI und zeigt dann das Hauptfenster an.
 * 
 * @author D. Luedemann, K. Hoelscher
 * Erweitert von gesamter Gruppe Kulturmeister
 * @version 0.9
 */
public final class Stundenplan {
    /**
     * stellt das GUI-Hauptfenster dar 
     */
    public static MainFrame main;
    
    
    private static int time = 0;
    
    /**
     * Privater Konstruktor, der eine Instanziierung dieser Utility-Klasse verhindert.
     */
    private Stundenplan() {
    	
    }  
    
    /**
     * Startet die Anwendung. Erzeugt dazu einen neuen Stundenplaner und dann das Hauptanzeigefenster und macht dieses
     * sichtbar. Der Pfad zur Konfigurationsdatei kann als Parameter uebergeben werden.
     * 
     * Falls die Konfiguration nicht erzeugt werden kann, wird eine {@link IllegalStateException} ausgeloest.
     * 
     * @param args
     *            als erstes Argument kann der Pfad zur Konfigurationsdatei angegeben werden
     */
    public static void main(final String[] args) {
    	try {
			Config.init(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(Config.getInt(Config.PROGRAM_STARTED_STRING,Config.PROGRAM_STARTED)==0){
    	Config.setIntValue(Config.PROGRAM_STARTED_STRING, 1);
    	Data.start();
    	main = new MainFrame();
    	main.pack();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Timer timer = new Timer(60000, new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		time++;
        		if(time >= Config.getInt("backupintervall", Config.BACKUPINTERVALL)){
                    final Date date = new Date();
        			Data.backup("backup_" + dateFormat.format(date), true);
        			time = 0;
        		}
            }
        });
        timer.start();
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);}
    }
    
    /**
     * Gibt die Mainframe zurueck.
     * 
     * @return	die Mainframe
     */
    public static MainFrame getMain() {
    	return main;
    }
}
