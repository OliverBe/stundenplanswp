package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class SchoolclassPanel extends JPanel{
	
	private Label jahr = new Label("Jahrgang: ");
	private Label bez = new Label("Zusatzbezeichner: ");

	public TextField bezField = new TextField(5);
	
	public String name;
	public Integer[] jahrgang = {1,2,3,4};
	
	public JButton button = new JButton("Klasse hinzufuegen");
	public JButton bTeam =new JButton("+");
	
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	
	private int x=1;
	
	private static DefaultListModel listModel = new DefaultListModel();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	public SchoolclassPanel() {
		setLayout(new GridBagLayout());
		setLayout(new GridBagLayout());
		c2.fill=GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()),c2);
		c2.gridy=2;
		add(createListPanel(new JPanel()),c2);
	}
	
	private JPanel createAddPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Neue Schulklasse hinzufuegen"));
		c.insets=new Insets(1,5,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		p.add(jahr,c);
		c.gridx=1;
	    p.add(new JComboBox(jahrgang),c);   
		c.gridx=0;
		c.gridy=1;
		p.add(bez,c);
		c.gridx=1;
	    p.add(bezField,c);
	    c.gridx=0;
	    c.gridy=2;
		p.add(new Label("Klassenraum:"),c);
		c.gridx=1;
		String[] raeume = {"32","24","23423"};
		p.add(new JComboBox(raeume),c);
		c.gridx=0;
		c.gridy=3;
		p.add(new Label("Klassenteam:"),c);
		c.gridx=1;
	    CheckBoxList checkList = new CheckBoxList();
	    checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    JCheckBox[] boxes = {new JCheckBox("VID"), new JCheckBox("KND")};
	    checkList.setListData(boxes);
		p.add(checkList,c);
	    c.gridx=0;
	    c.gridy=4;
	    c.gridwidth=2;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    p.add(new Label("Stunden pro Woche"),c);
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
	    p.add(table,c);
	    
	    c.gridy=6;
	    c.gridx=0;
		p.add(button,c);   
		return p;
		
	}

	private JPanel createListPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Existierende Schulklassen"));
		
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroller.setPreferredSize(new Dimension(250, 200));

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.8;
		c.weighty = 1.0;
		p.add(listScroller, c);
		
		return p;
	}
	
	private boolean textFieldsEmpty(final JPanel p){
		boolean b=true;
		for(Component c : p.getComponents()){
			if(c instanceof TextField){
				TextField tf = (TextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
		}
		return b;
	}

}
