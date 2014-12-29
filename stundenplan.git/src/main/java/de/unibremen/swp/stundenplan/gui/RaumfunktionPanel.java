package de.unibremen.swp.stundenplan.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.naming.InvalidNameException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.Data;

public class RaumfunktionPanel extends JPanel {
		private Label lTime = new Label("Name der Funktion");
		private JTextField tf = new JTextField(20);
		private GridBagConstraints c = new GridBagConstraints();
		private GridBagConstraints c2 = new GridBagConstraints();
		private JButton button = new JButton("Funktion hinzufügen");


		private static DefaultListModel listModel = new DefaultListModel();
		private JList<String> list = new JList<String>(listModel);
		private JScrollPane listScroller = new JScrollPane(list);

		public RaumfunktionPanel() {
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
		
		private JPanel createAddPanel(final JPanel p){
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory
					.createTitledBorder("Funktionen von Räumen "));
			c.insets = new Insets(1, 1, 1, 1);
			c.gridx = 0;
			c.gridy = 0;
			p.add(lTime, c);
			c.gridx = 1;
			p.add(tf, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			p.add(button, c);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Raumfunktion rf;
					try {
						listModel.clear();
						if(textFieldsEmpty(p)) throw new InvalidNameException();
						rf = new Raumfunktion(tf.getText());
						Data.addRaumfunktion(rf);
						for (Raumfunktion r : Data.getAllRaumfunktion()){
							listModel.addElement(r);
						}
						
					} catch (InvalidNameException e) {
						System.out.println("ERROR beim Hinzufügen");
						e.printStackTrace();
					}
				}
			});		
			
			return p;
		}
		
		private JPanel createListPanel(final JPanel p){
			p.setLayout(new GridBagLayout());
			p.setBorder(BorderFactory.createTitledBorder("Existierende Raumfunktionen"));
			
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