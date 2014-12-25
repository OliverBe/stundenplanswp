package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class WochenplanPanel extends JPanel {

	public JTabbedPane tabPane = new JTabbedPane();
	private WochenplanTag montag = new WochenplanTag();
	private WochenplanTag dienstag = new WochenplanTag();
	private WochenplanTag mittwoch = new WochenplanTag();
	private WochenplanTag donnerstag = new WochenplanTag();
	private WochenplanTag freitag = new WochenplanTag();
	private WochenplanTag samstag = new WochenplanTag();
	
	
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

}
