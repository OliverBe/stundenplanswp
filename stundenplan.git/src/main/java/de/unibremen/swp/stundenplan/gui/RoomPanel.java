package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class RoomPanel extends JPanel {

	private Label lName = new Label("Name des Raumes: ");
	private Label lPos = new Label("In welchem Gebaeude: ");

	private TextField nameField = new TextField(5);
	private JComboBox jcb;

	public JButton button = new JButton("Raum Hinzufuegen");

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel<Room> listModel = new DefaultListModel<Room>();
	private JList<Room> list = new JList<Room>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	public RoomPanel() {
		setLayout(new GridBagLayout());
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()), c2);
		c2.gridy = 2;
		add(createListPanel(new JPanel()), c2);
	}

	private JPanel createAddPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Neuen Raum hinzufuegen"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName, c);
		c.gridx = 1;
		p.add(nameField, c);
		c.gridx = 0;
		c.gridy = 1;
		p.add(lPos, c);
		c.gridx = 1;
		Integer[] gebaeude = { 1, 2 };
		jcb = new JComboBox(gebaeude);
		p.add(jcb, c);
		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Spezieller Raum:"), c);
		c.gridx = 1;
		final CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Raumfunktion rf : DataRaum.getAllRaumfunktion()) {
			boxes.add(new JCheckBox(rf.getName()));
		}
		;
		checkList.setListData(boxes.toArray());
		p.add(checkList, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (textFieldsEmpty(p))
						throw new WrongInputException();

					ArrayList<String> rf = new ArrayList<String>();
					for (int i = 0; i < checkList.getSelectedValuesList()
							.size(); i++) {
						JCheckBox cb = (JCheckBox) checkList
								.getSelectedValuesList().get(i);
						rf.add(cb.getText());
					}

					DataRaum.addRaum(new Room(nameField.getText(), (int) jcb
							.getSelectedItem(), rf));

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
		p.setBorder(BorderFactory.createTitledBorder("Existierende Räume"));

		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroller.setPreferredSize(new Dimension(250, 200));

		updateList();

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.8;
		c.weighty = 1.0;
		p.add(listScroller, c);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				final DataPopup pop = new DataPopup(list, listModel);
				setComponentPopupMenu(pop);
				list.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.isMetaDown()) {
							pop.show(list, e.getX(), e.getY());
						}
					}
				});
				pop.edit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame edit = new JFrame("Raum editieren");
						edit.add(createEditPanel(new JPanel(),list.getSelectedValue()));
						edit.pack();
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(list.getModel());
						deleteD.setVisible(true);
					}
				});
			}
		});

		return p;
	}

	private JPanel createEditPanel(final JPanel p, final Room ro) {
		Label lName2 = new Label("Name des Raumes: ");
		Label lPos2 = new Label("In welchem Gebaeude: ");

		TextField nameField2 = new TextField(5);
		JComboBox jcb2;

		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");
		
		
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Raum editieren"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName2, c);
		c.gridx = 1;
		p.add(nameField2, c);
		nameField2.setText(ro.getName());
		c.gridx = 0;
		c.gridy = 1;
		p.add(lPos2, c);
		c.gridx = 1;
		Integer[] gebaeude = { 1, 2 };
		jcb2 = new JComboBox(gebaeude);
		p.add(jcb2, c);
		jcb2.setSelectedItem(ro.getGebaeude());
		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Spezieller Raum:"), c);
		c.gridx = 1;
		CheckBoxList checkList2 = new CheckBoxList();
		ArrayList<JCheckBox> boxes2 = new ArrayList<JCheckBox>();
//		for (Raumfunktion rf : ro.getMoeglicheFunktionen()) {
//			boxes2.add(new JCheckBox(rf.getName()));
//		};
//		System.out.println(ro.getMoeglicheFunktionen().toString());
		checkList2.setListData(boxes2.toArray());
		p.add(checkList2, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (textFieldsEmpty(p))
						throw new WrongInputException();

					ArrayList<String> rf = new ArrayList<String>();
					for (int i = 0; i < checkList2.getSelectedValuesList()
							.size(); i++) {
						JCheckBox cb = (JCheckBox) checkList2
								.getSelectedValuesList().get(i);
						rf.add(cb.getText());
					}

					DataRaum.addRaum(new Room(nameField.getText(), (int) jcb
							.getSelectedItem(), rf));

					updateList();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});

		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button3, c);

		// abbruch Button
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				((JFrame) p.getParent()).dispose();
			}
		});
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
		for (Room r : DataRaum.getAllRaum()) {
			listModel.addElement(r);
		}
	}

}
