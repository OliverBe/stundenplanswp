package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map.Entry;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;

public class SchoolclassPanel extends JPanel{
	
	private Label jahr = new Label("Jahrgang: ");
	private Label bez = new Label("Zusatzbezeichner: ");

	public JTextField bezField = new JTextField(5);
	
	private JComboBox<Object> jcb;
	public Integer[] jahrgang = {1,2,3,4};
	private JComboBox<Object> jg = new JComboBox<Object>(jahrgang);
	
	public JButton button = new JButton("Klasse hinzufuegen");
	public JButton bTeam =new JButton("+");
	
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	
	private static DefaultListModel<Schoolclass> listModel = new DefaultListModel<Schoolclass>();
	private JList<Schoolclass> list = new JList<Schoolclass>(listModel);
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
	    p.add(jg,c);
		c.gridx=0;
		c.gridy=1;
		p.add(bez,c);
		c.gridx=1;
	    p.add(bezField,c);
		c.gridx=0;
		c.gridy=2;
		p.add(new Label("Klassenteam:"),c);
		c.gridx=1;
		final CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		 for(Personal per : DataPersonal.getAllPersonal()){
			 boxes.add(new JCheckBox(per.getKuerzel()));
		 };
		checkList.setListData(boxes.toArray());
		p.add(checkList, c);
		c.gridx=0;
	    c.gridy=3;
		p.add(new Label("Klassenraum:"),c);
		c.gridx=1;
		
		ArrayList<Room> ro=DataRaum.getAllRaum();
	    jcb=new JComboBox<Object>(ro.toArray());
		p.add(jcb,c);
	    
	    final DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("Stundeninhalt");
	    model.addColumn("Bedarf");
	    for(Stundeninhalt si : DataStundeninhalt.getAllStundeninhalte()) {
	    	model.addRow(new String[] {si.getKuerzel(), "0"});
	    }
	    for(Entry<String, Integer> entry : DataSchulklasse.getJahrgangByJahrgang(jg.getSelectedIndex()+1).getStundenbedarf().entrySet()) {
	    	for(int i=0;i<model.getRowCount();i++) {
	    		if(model.getValueAt(i, 0).toString().equals(entry.getKey())) {
	    			model.setValueAt(entry.getValue(), i, 1);
	    		}
	    	}
		}
	    
	    JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		jg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for(int i=0;i<model.getRowCount();i++) {
					model.setValueAt("0", i, 1);
				}
				for(Entry<String, Integer> entry : DataSchulklasse.getJahrgangByJahrgang(jg.getSelectedIndex()+1).getStundenbedarf().entrySet()) {
			    	for(int i=0;i<model.getRowCount();i++) {
			    		if(model.getValueAt(i, 0).toString().equals(entry.getKey())) {
			    			model.setValueAt(entry.getValue(), i, 1);
			    		}
			    	}
				}
			}
		});
		
//	    final DefaultListModel<String> dummyList = new DefaultListModel<String>();
//	    for ( String ss : ("English		5h,Mathe		5h,		," +
//	                      "			, 		,		,").split(",") )
//	      dummyList.addElement( ss );
//	    JList<String> list = new JList<String>( dummyList );
		
	    c.gridx=0;
	    c.gridy=4;
	    c.gridwidth=2;
	    c.fill=GridBagConstraints.HORIZONTAL;
	    p.add(table.getTableHeader(),c);
	    
	    c.gridy=5;
	    c.gridx=0; 
	    p.add(table,c);
	    
	    c.gridy=6;
	    c.gridx=0;
		p.add(button,c);   
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (textFieldsEmpty(p))
						throw new WrongInputException();
					
					
					
				//	DataSchulklasse.addSchulklasse(schulklasse);
					
					updateList();				

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
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
			if(c instanceof JTextField ){
				JTextField tf = (JTextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
		}
		return b;
	}

	public static void updateList() {
		listModel.clear();
		for (Schoolclass sc : DataSchulklasse.getAllSchulklasse()){
			listModel.addElement(sc);
		}	
	}

}
