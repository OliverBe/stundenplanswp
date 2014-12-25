package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class WarningPanel extends JPanel{

	
	public String message="No Warning";
	public TextField t=new TextField(300);
	
	public WarningPanel(){
		t.setText(message);
		t.setEditable(false);
		t.setBackground(new Color(150,205,150));
		setLayout(new GridBagLayout());
		add(t);
	}
}
