package de.unibremen.swp.stundenplan.gui;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.unibremen.swp.stundenplan.config.Weekday;

@SuppressWarnings("serial")
public class WochenplanPanel extends JPanel {

	public static JTabbedPane tabPane = new JTabbedPane();
	private WochenplanTag montag;
	private WochenplanTag dienstag;
	private WochenplanTag mittwoch;
	private WochenplanTag donnerstag;
	private WochenplanTag freitag;
	private WochenplanTag samstag;
	private WochenplanTag sonntag;
	
	
	public WochenplanPanel(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		init();
		
		
		
	}
	
	public void init(){
		
		if(Weekday.MONDAY.isSchoolday()){
			montag = new WochenplanTag(Weekday.MONDAY);
			tabPane.add("Montag", montag);
		}
		
		
		if(Weekday.TUESDAY.isSchoolday()){
		dienstag = new WochenplanTag(Weekday.TUESDAY);
		tabPane.add("Dienstag", dienstag);
		}
		if(Weekday.WEDNESDAY.isSchoolday()){
		mittwoch = new WochenplanTag(Weekday.WEDNESDAY);
		tabPane.add("Mittwoch", mittwoch);
		}
		if(Weekday.THURSDAY.isSchoolday()){
		donnerstag = new WochenplanTag(Weekday.THURSDAY);
		tabPane.add("Donnerstag", donnerstag);
		}
		if(Weekday.FRIDAY.isSchoolday()){
		freitag = new WochenplanTag(Weekday.FRIDAY);
		tabPane.add("Freitag", freitag);
		}
		if(Weekday.SATURDAY.isSchoolday()){
		samstag = new WochenplanTag(Weekday.SATURDAY);
		tabPane.add("Samstag",samstag);
		}
		if(Weekday.SUNDAY.isSchoolday()){
		samstag = new WochenplanTag(Weekday.SATURDAY);
		tabPane.add("Sonntag",sonntag);	
		}
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
