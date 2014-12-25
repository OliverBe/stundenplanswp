package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.unibremen.swp.stundenplan.exceptions.DatasetException;

public class AddNewSubject extends JPanel {

	private String name;
	private String acro;

	Label lName = new Label("Titel der Aktivitaet:");
	Label lAcro = new Label("Acronym:");
	Label ltime = new Label("Regeldauer in h:");
	Label lPause = new Label("rythmischer Typ:");

	private TextField nameField = new TextField(15);
	private TextField acroField = new TextField(5);
	private TextField timeField = new TextField(5);
	
	public JButton button = new JButton("Stundeninhalt hinzufügen");
	
	private GridBagConstraints c = new GridBagConstraints();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1219589162309740553L;

	public AddNewSubject() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Neuen Stundeninhalt hinzufügen"));
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		add(lName,c);
		c.gridx=1;
		add(nameField,c);

		c.gridx=0;
		c.gridy=1;
		add(lAcro,c);
		c.gridx=1;
		add(acroField,c);
		
		c.gridx=0;
		c.gridy=2;
		add(ltime,c);
		c.gridx=1;
		add(timeField,c);
			
		c.gridx=0;
		c.gridy=3;
		add(lPause,c);
		c.gridx=1;
		JRadioButton pauseB = new JRadioButton("Pause");
		pauseB.setSelected(true);
		JRadioButton leichtB = new JRadioButton("Leicht");
		JRadioButton schwerB = new JRadioButton("Schwer");
		ButtonGroup group = new ButtonGroup();
	    group.add(pauseB);
	    group.add(leichtB);
	    group.add(schwerB);
	    add(pauseB,c);
	    c.gridy=4;
	    add(leichtB,c);
	    c.gridy=5;
	    add(schwerB,c);
		
		c.gridx=0;
		c.gridy=6;
		c.gridwidth=2;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(button,c);
	}
}
