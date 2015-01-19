package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.unibremen.swp.stundenplan.config.Weekday;

public class WochenplanPanel extends JPanel {

	public JTabbedPane tabPane = new JTabbedPane();
	private WochenplanTag montag = new WochenplanTag(Weekday.MONDAY);
	private WochenplanTag dienstag = new WochenplanTag(Weekday.TUESDAY);
	private WochenplanTag mittwoch = new WochenplanTag(Weekday.WEDNESDAY);
	private WochenplanTag donnerstag = new WochenplanTag(Weekday.THURSDAY);
	private WochenplanTag freitag = new WochenplanTag(Weekday.FRIDAY);
	private WochenplanTag samstag = new WochenplanTag(Weekday.SATURDAY);
	
	
	public WochenplanPanel(){
		init();
		
	}
	
	public void init(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		tabPane.add("Montag", montag);
		tabPane.add("Dienstag", dienstag);
		tabPane.add("Mittwoch", mittwoch);
		tabPane.add("Donnerstag", donnerstag);
		tabPane.add("Freitag", freitag);
		tabPane.add("Samstag",samstag);
		add(tabPane);
		
	}
	
	public void update(){
		montag.refresh();
		dienstag.refresh();
		mittwoch.refresh();
		donnerstag.refresh();
		freitag.refresh();
		samstag.refresh();
		System.out.println("Personaliste aktualisiert");
	}

}
