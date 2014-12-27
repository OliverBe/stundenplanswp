package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;

public class AddNewPersonal extends JPanel {
	
	private String name;
	private String acro;
	private String time;

	private Label lName = new Label("Name des Personals:");
	private Label lAcro = new Label("Acronym:");
	private Label lPrefTime = new Label("Zeitwunsch:");
	private Label lBis = new Label("bis");
	private Label lMo = new Label("Montag:");
	private Label lDi = new Label("Dienstag:");
	private Label lMi = new Label("Mittwoch:");
	private Label lDo = new Label("Donnerstag:");
	private Label lTime = new Label("Zeitverpflichtung in h:");
	private Label lFr = new Label("Freitag:");

	public TextField nameField = new TextField(15);
	public TextField acroField = new TextField(5);
	public TextField timeField= new TextField(5);
	public TextField subjectsField= new TextField(5);
	public TextField prefTimeFieldMoFrom= new TextField(5);
	public TextField prefTimeFieldMoTo=new TextField(5);
	public TextField prefTimeFieldDiFrom= new TextField(5);
	public TextField prefTimeFieldDiTo=new TextField(5);
	public TextField prefTimeFieldMiFrom= new TextField(5);
	public TextField prefTimeFieldMiTo=new TextField(5);
	public TextField prefTimeFieldDoFrom= new TextField(5);
	public TextField prefTimeFieldDoTo=new TextField(5);
	public TextField prefTimeFieldFrFrom= new TextField(5);
	private Label lSubjects = new Label("Stundeninhalte:");
	public TextField prefTimeFieldFrTo=new TextField(5);
	
	public JButton button = new JButton("Personal hinzufügen");
	
	private GridBagConstraints c = new GridBagConstraints();	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1219589162309740553L;

	public AddNewPersonal(){
	    setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Neues Personal hinzufuegen"));
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		add(lName,c);
		c.gridx=1;
		add(nameField,c);
		c.gridx=2;
		JRadioButton lehrerB = new JRadioButton("Lehrer");
		lehrerB.setSelected(true);
		JRadioButton paedagogeB = new JRadioButton("Paedagoge");
		ButtonGroup group = new ButtonGroup();
	    group.add(lehrerB);
	    group.add(paedagogeB);
	    add(lehrerB,c);
	    c.gridx=3;
	    add(new Label("oder"));
	    c.gridx=4;
	    add(paedagogeB,c);

		c.gridx=0;
		c.gridy=1;
		add(lAcro,c);
		c.gridx=1;
		add(acroField,c);
			
		c.gridx=0;
		c.gridy=2;
		add(lTime,c);
		c.gridx=1;
		add(timeField,c);
		
		
		c.gridx=0;
		c.gridy=3;
		add(lPrefTime, c);
		c.gridx=1;
		add(lMo,c);
		c.gridx=2;
		add(prefTimeFieldMoFrom,c);
		c.gridx=3;
		add(lBis,c);
		c.gridx=4;
		add(prefTimeFieldMoTo,c);
		c.gridy=4;
		c.gridx=1;
		add(lDi,c);
		c.gridx=2;
		add(prefTimeFieldDiFrom,c);
		c.gridx=3;
	//	add(lBis,c);
		c.gridx=4;
		add(prefTimeFieldDiTo,c);
		c.gridy=5;
		c.gridx=1;
		add(lMi,c);
		c.gridx=2;
		add(prefTimeFieldMiFrom,c);
		c.gridx=3;
	//	add(lBis,c);
		c.gridx=4;
		add(prefTimeFieldMiTo,c);
		c.gridy=6;
		c.gridx=1;
		add(lDo,c);
		c.gridx=2;
		add(prefTimeFieldDoFrom,c);
		c.gridx=3;
	//	add(lBis,c);
		c.gridx=4;
		add(prefTimeFieldDoTo,c);
		c.gridy=7;
		c.gridx=1;
		add(lFr,c);
		c.gridx=2;
		add(prefTimeFieldFrFrom,c);
		c.gridx=3;
	//	add(lBis,c);
		c.gridx=4;
		add(prefTimeFieldFrTo,c);
		
		c.gridx=0;
		c.gridy=8;
		add(lSubjects, c);
		c.gridx=1;
		Label deutsch = new Label("Deutsch");
		add(deutsch,c);
		c.gridx=2;
		Label mathe = new Label("Mathe");
		add(mathe,c);
		c.gridx=3;
		Label musik=new Label("Musik");
		add(musik,c);
		
		c.gridx=1;
		c.gridy=9;
		JCheckBox box1 = new JCheckBox();
		add(box1,c);
		c.gridx=2;
		JCheckBox box2= new JCheckBox();
		add(box2,c);
		c.gridx=3;
		JCheckBox box3= new JCheckBox();
		add(box3,c);
		
		c.gridx=0;
		c.gridy=11;
		c.gridwidth=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(button,c);
		
		nameField.requestFocus();
	}
}
