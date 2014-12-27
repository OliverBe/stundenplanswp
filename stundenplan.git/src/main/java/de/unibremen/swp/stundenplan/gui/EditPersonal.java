package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class EditPersonal extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1579191984515127831L;

	private String name;
	private String acro;
	private String time;

	private Label lName = new Label("Name des Personals:");
	private Label lLehrer = new Label("Ist es ein Lehrer?");
	private Label lAcro = new Label("Acronym:");
	private Label lTime = new Label("Zeitverpflichtung");
	private Label lPrefTime = new Label("Zeitwunsch:");
	private Label lBis = new Label("bis");
	private Label lMo = new Label("Montag:");
	private Label lDi = new Label("Dienstag:");
	private Label lMi = new Label("Mittwoch:");
	private Label lDo = new Label("Donnerstag:");
	private Label lFr = new Label("Freitag:");
	private Label lSubjects = new Label("Stundeninhalte:");

	public TextField nameField = new TextField(20);
	public TextField acroField = new TextField(20);
	public TextField timeField= new TextField(20);
	public TextField subjectsField= new TextField(20);
	public TextField prefTimeFieldMoFrom= new TextField(10);
	public TextField prefTimeFieldMoTo=new TextField(10);
	public TextField prefTimeFieldDiFrom= new TextField(10);
	public TextField prefTimeFieldDiTo=new TextField(10);
	public TextField prefTimeFieldMiFrom= new TextField(10);
	public TextField prefTimeFieldMiTo=new TextField(10);
	public TextField prefTimeFieldDoFrom= new TextField(10);
	public TextField prefTimeFieldDoTo=new TextField(10);
	public TextField prefTimeFieldFrFrom= new TextField(10);
	public TextField prefTimeFieldFrTo=new TextField(10);
	
	public JButton okButton = new JButton("OK");
	public JButton noButton = new JButton("Abbrechen");

	private GridBagConstraints c = new GridBagConstraints();
	
	private JFrame mainFrame;
	
	public EditPersonal(JFrame j){
	mainFrame= j;
	
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
	add(lLehrer,c);
	c.gridx=3;
	JCheckBox personalBox= new JCheckBox();
	add(personalBox,c);

	c.gridx=0;
	c.gridy=1;
	add(lAcro,c);
	c.gridx=1;
	c.ipadx=-115;
	add(acroField,c);
	c.ipadx=0;
		
	c.gridx=0;
	c.gridy=2;
	add(lTime,c);
	c.gridx=1;
	c.ipadx=-115;
	add(timeField,c);
	c.ipadx=0;
	
	
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
	c.gridy=10;
	c.gridwidth=3;
	c.fill=GridBagConstraints.HORIZONTAL;
	add(okButton,c);
	c.gridx=3;
	c.gridwidth=2;
	c.fill=GridBagConstraints.HORIZONTAL;
	add(noButton,c);
	

	buttonOkay(okButton);
	buttonOkay(noButton);
	nameField.requestFocus();

}

private void buttonOkay(JButton b) {
	b.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainFrame.dispose();	
		}
	});
	
}	
}
