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
	private WochenplanTag montag = new WochenplanTag("Montag");
	private WochenplanTag dienstag = new WochenplanTag("Dienstag");
	private WochenplanTag mittwoch = new WochenplanTag("Mittwoch");
	private WochenplanTag donnerstag = new WochenplanTag("Donnerstag");
	private WochenplanTag freitag = new WochenplanTag("Freitag");
	private WochenplanTag samstag = new WochenplanTag("Samstag");
	
	
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
