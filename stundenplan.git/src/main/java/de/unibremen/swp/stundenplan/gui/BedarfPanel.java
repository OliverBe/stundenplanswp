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
import java.util.HashMap;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class BedarfPanel extends JPanel{
	
	private Label lName = new Label("Jahrgang: ");
	private Label lSti = new Label("Stundeninhalt: ");
	private Label lBed = new Label("Bedarf in Stunden: ");
	
	JComboBox cb1;
	JComboBox cb2;

	public TextField bedField = new TextField(3);
	public Integer[] jahrgaenge = {1,2,3,4};
	
	public JButton button = new JButton("Bedarf hinzufuegen");
	
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	public BedarfPanel() {
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
		p.setBorder(BorderFactory.createTitledBorder("Bedarf an Stundeninhalten pro Jahrgang"));
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		p.add(lName,c);
		c.gridx=1;
		cb1=new JComboBox(jahrgaenge);
		p.add(cb1,c);   
		c.gridx=2;
		c.weightx=0;
		p.add(lBed,c);
		c.gridx=3;
		p.add(bedField,c);
		c.weightx=0;
	    c.gridx=0;
		c.gridy=1;
	    p.add(lSti,c);
	    c.gridx=1;
	    c.gridwidth=3;
	    ArrayList<Stundeninhalt> si=DataStundeninhalt.getAllStundeninhalte();
	    cb2=new JComboBox(si.toArray());
	    p.add(cb2,c);
		c.gridx=0;
	    c.gridy=2;
	    c.gridwidth=4;
	    c.fill=GridBagConstraints.HORIZONTAL;
		p.add(button,c);   
		
		//add
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					try {
						if(textFieldsEmpty(p)) throw new WrongInputException();
						System.out.println("Jahrgang: "+cb1.getSelectedItem()+ " Stundeninhalt: "+((Stundeninhalt)cb2.getSelectedItem()).getKuerzel()+" Stunden: "+bedField.getText());
						Jahrgang j = DataSchulklasse.getAllJahrgang().get((int) cb1.getSelectedItem());
						HashMap<String, Integer> hm = new HashMap<String,Integer>();
						hm.put(cb2.getSelectedItem().toString(),Integer.parseInt(bedField.getText()));				
						j.setStundenbedarf(hm);
						DataSchulklasse.addJahrgang(j);
						
						listModel.clear();
						for(Jahrgang ja : DataSchulklasse.getAllJahrgang()){
							for(Entry<String, Integer> entry : ja.getStundenbedarf().entrySet()) {
								
							}
						};
						
					} catch (WrongInputException e) {
						e.printStackTrace();
					}
			}
		});		
		return p;
		
	}

	private JPanel createListPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Existierender Bedarf"));
		
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
