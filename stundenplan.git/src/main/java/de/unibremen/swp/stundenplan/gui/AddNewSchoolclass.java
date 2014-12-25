package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;


public class AddNewSchoolclass extends JPanel {
	
	
	private Label jahr = new Label("Jahrgang: ");
	private Label bez = new Label("Zusatzbezeichner: ");

	
	public TextField bezField = new TextField(5);
	
	public String name;
	public Integer[] jahrgang = {1,2,3,4};
	
	public JButton button = new JButton("Klasse hinzufuegen");
	public JButton bTeam =new JButton("+");
	
	private GridBagConstraints c = new GridBagConstraints();
	
	private int x=1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1219589162309740553L;

	public AddNewSchoolclass(){
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Neue Schulklasse hinzufuegen"));
		c.insets=new Insets(1,5,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		add(jahr,c);
		c.gridx=1;
	    add(new JComboBox(jahrgang),c);   
		c.gridx=0;
		c.gridy=1;
		add(bez,c);
		c.gridx=1;
	    add(bezField,c);
	    c.gridx=0;
	    c.gridy=2;
		add(new Label("Klassenraum:"),c);
		c.gridx=1;
		String[] raeume = {"32","24","23423"};
		add(new JComboBox(raeume),c);
		c.gridx=0;
		c.gridy=3;
		add(new Label("Klassenteam:"),c);
		c.gridx=1;
	    CheckBoxList checkList = new CheckBoxList();
	    checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    JCheckBox[] boxes = {new JCheckBox("VID"), new JCheckBox("KND")};
	    checkList.setListData(boxes);
		add(checkList,c);
	    c.gridx=0;
	    c.gridy=4;
	    c.gridwidth=2;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    add(new Label("Stunden pro Woche"),c);
	    DefaultTableModel model = new DefaultTableModel();
	    String[] array={"English 5h","Mathe 5h"};
	    model.addColumn("MyColumnHeader",array);
	    JTable table = new JTable(model);
//	    final DefaultListModel<String> dummyList = new DefaultListModel<String>();
//	    for ( String ss : ("English		5h,Mathe		5h,		," +
//	                      "			, 		,		,").split(",") )
//	      dummyList.addElement( ss );
//	    JList<String> list = new JList<String>( dummyList );    
	    c.gridy=5;
	    c.gridx=0; 
	    add(table,c);
	    
	    c.gridy=6;
	    c.gridx=0;
		add(button,c);    
	}
}
