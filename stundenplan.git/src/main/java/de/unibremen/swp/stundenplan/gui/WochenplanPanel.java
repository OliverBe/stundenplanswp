package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.unibremen.swp.stundenplan.config.Weekday;

public class WochenplanPanel extends JPanel {

	private JFrame f;
	public static JTabbedPane tabPane = new JTabbedPane();
	private WochenplanTag montag;
	private WochenplanTag dienstag;
	private WochenplanTag mittwoch;
	private WochenplanTag donnerstag;
	private WochenplanTag freitag;
	private WochenplanTag samstag;
	
	
	public WochenplanPanel(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		init();
		
		
		
	}
	
	public void init(){
		
		montag = new WochenplanTag(Weekday.MONDAY);
		tabPane.add("Montag", montag);
		dienstag = new WochenplanTag(Weekday.TUESDAY);
		tabPane.add("Dienstag", dienstag);
		mittwoch = new WochenplanTag(Weekday.WEDNESDAY);
		tabPane.add("Mittwoch", mittwoch);
		donnerstag = new WochenplanTag(Weekday.THURSDAY);
		tabPane.add("Donnerstag", donnerstag);
		freitag = new WochenplanTag(Weekday.FRIDAY);
		tabPane.add("Freitag", freitag);
		samstag = new WochenplanTag(Weekday.SATURDAY);
		tabPane.add("Samstag",samstag);
		add(tabPane);
	}
	
	public void update(){
		tabPane.removeAll();  
		init();
		System.out.println("Personaliste aktualisiert");
	}

	public static JTabbedPane getTabPane() {
		return tabPane;
	}
}
