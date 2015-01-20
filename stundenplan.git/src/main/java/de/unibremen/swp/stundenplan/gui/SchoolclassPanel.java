package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.exceptions.MindestestensEinLehrerException;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.RaumManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class SchoolclassPanel extends JPanel {

	private Label jahr = new Label("Jahrgang: ");
	private Label bez = new Label("Zusatzbezeichner: ");
	private JLabel lKt = new JLabel("Klassenteam: ");

	public JTextField bezField = new JTextField(5);

	private JComboBox<Object> jcb;
	public Integer[] jahrgang = { 1, 2, 3, 4 };
	private JComboBox<Object> jg = new JComboBox<Object>(jahrgang);

	public JButton button = new JButton("Klasse hinzufuegen");
	public JButton bTeam = new JButton("+");

	private DefaultTableModel model;
	private DefaultTableModel model2;

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel<Schoolclass> listModel = new DefaultListModel<Schoolclass>();
	private JList<Schoolclass> list = new JList<Schoolclass>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	final CheckBoxList checkList = new CheckBoxList();
	final CheckBoxList checkList2 = new CheckBoxList();

	public SchoolclassPanel() {
		setLayout(new GridBagLayout());
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
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return false;
				} else {
					return true;
				}
			}
		};

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Neue Schulklasse hinzufuegen"));
		c.insets = new Insets(8, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(jahr, c);
		c.gridx = 1;
		p.add(jg, c);
		c.gridx = 0;
		c.gridy = 1;
		p.add(bez, c);
		c.gridx = 1;
		p.add(bezField, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lKt.setFont(new Font(bezField.getFont().getFontName(), Font.PLAIN,
				bezField.getFont().getSize()));
		p.add(lKt, c);
		c.gridy = 4;

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;

		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Personal per : PersonalManager.getAllPersonalFromDB()) {
			boxes.add(new JCheckBox(per.getKuerzel()));
		}
		;
		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);

		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 1;
		p.add(new Label("Klassenraum:"), c);
		c.gridx = 1;

		ArrayList<Room> ro = DataRaum.getAllRaum();
		jcb = new JComboBox<Object>(ro.toArray());
		p.add(jcb, c);

		model.addColumn("Stundeninhalt");
		model.addColumn("Bedarf");
		for (Stundeninhalt si : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			model.addRow(new String[] { si.getKuerzel(), "0" });
		}
		for (Entry<String, Integer> entry : DataSchulklasse
				.getJahrgangByJahrgang(jg.getSelectedIndex() + 1)
				.getStundenbedarf().entrySet()) {
			for (int i = 0; i < model.getRowCount(); i++) {
				if (model.getValueAt(i, 0).toString().equals(entry.getKey())) {
					model.setValueAt(entry.getValue() + "", i, 1);
				}
			}
		}

		JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		jg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < model.getRowCount(); i++) {
					model.setValueAt("0", i, 1);
				}
				for (Entry<String, Integer> entry : DataSchulklasse
						.getJahrgangByJahrgang((int) jg.getSelectedItem())
						.getStundenbedarf().entrySet()) {
					for (int i = 0; i < model.getRowCount(); i++) {
						if (model.getValueAt(i, 0).toString()
								.equals(entry.getKey())) {
							model.setValueAt(entry.getValue() + "", i, 1);
						}
					}
				}
			}
		});

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table.getTableHeader(), c);

		c.gridy = 8;
		c.gridx = 0;
		p.add(table, c);

		c.gridy = 9;
		c.gridx = 0;
		p.add(button, c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (!check(p))
						throw new WrongInputException();

					ArrayList<String> kt = new ArrayList<String>();
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel()
								.getElementAt(i);
						if (cb.isSelected())
							kt.add(cb.getText());
					}

					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					for (int i = 0; i < model.getRowCount(); i++) {
						hm.put((String) model.getValueAt(i, 0), Integer
								.parseInt((String) model.getValueAt(i, 1)));
					}

					DataSchulklasse.addSchulklasse(new Schoolclass((jg
							.getSelectedItem()) + bezField.getText(), (int) jg
							.getSelectedItem(), (Room) jcb.getSelectedItem(),
							kt, hm));
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
		p.setBorder(BorderFactory
				.createTitledBorder("Existierende Schulklassen"));

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

		updateList();

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
						edit.add(createEditPanel(new JPanel(),
								list.getSelectedValue()));
						edit.setLocation(MouseInfo.getPointerInfo()
								.getLocation().x, MouseInfo.getPointerInfo()
								.getLocation().y);
						edit.pack();
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(list
								.getSelectedValue());
						deleteD.setLocation(MouseInfo.getPointerInfo()
								.getLocation().x, MouseInfo.getPointerInfo()
								.getLocation().y);
						deleteD.setVisible(true);
					}
				});
			}
		});
		return p;
	}

	private JPanel createEditPanel(final JPanel p, final Schoolclass sc) {

		Label jahr2 = new Label("Jahrgang: ");
		Label bez2 = new Label("Zusatzbezeichner: ");
		JLabel lKt2 = new JLabel("Klassenteam: ");

		final JTextField bezField2 = new JTextField(5);

		final JComboBox<Object> jcb2;
		Integer[] jahrgang2 = { 1, 2, 3, 4 };
		final JComboBox<Object> jg2 = new JComboBox<Object>(jahrgang2);

		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");

		model2 = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return false;
				} else {
					return true;
				}
			}
		};

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Neue Schulklasse hinzufuegen"));
		c.insets = new Insets(8, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(jahr2, c);
		c.gridx = 1;
		p.add(jg2, c);
		
		c.gridx = 0;
		c.gridy = 1;
		p.add(bez2, c);
		c.gridx = 1;
		p.add(bezField2, c);
		bezField2.setText(sc.getName().substring(1, sc.getName().length()));
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lKt2.setFont(new Font(bezField2.getFont().getFontName(), Font.PLAIN, bezField2
				.getFont().getSize()));
		p.add(lKt2, c);
		c.gridy = 4;

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;

		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Personal per : PersonalManager.getAllPersonalFromDB()) {
			boxes.add(new JCheckBox(per.getKuerzel()));
		};
		
		for(JCheckBox jcb : boxes){
			for(String s : sc.getKlassenlehrer()){
				if(jcb.getText().equals(s)) jcb.setSelected(true);
			}	
		}	

		
		checkList2.setListData(boxes.toArray());
		checkList2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList2, c);

		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 1;
		p.add(new Label("Klassenraum:"), c);
		c.gridx = 1;

		ArrayList<Room> ro = RaumManager.getAllRoomsFromDB();
		jcb2 = new JComboBox<Object>(ro.toArray());
		
		for(Room r : ro){
			System.out.println("++ Klassenraum:  "+r.getName());
			System.out.println("++ Klassenraum: !"+sc.getKlassenraum().getName());
			if(r.getName().equals(sc.getKlassenraum().getName()))System.out.println("++ Yes"+sc.getKlassenraum().getName());
		}
		for(int i=0; i<ro.size(); i++){
			if(ro.get(i).getName().equals(sc.getKlassenraum().getName())) jcb2.setSelectedIndex(i);
		}
		p.add(jcb2, c);
		
		jg2.setSelectedItem(sc.getJahrgang());

		model2.addColumn("Stundeninhalt");
		model2.addColumn("Bedarf");
		for (Stundeninhalt si : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			model2.addRow(new String[] { si.getKuerzel(), "0" });
		}
		for (Entry<String, Integer> entry : sc.getStundenbedarf().entrySet()) {
			for (int i = 0; i < model2.getRowCount(); i++) {
				if (model2.getValueAt(i, 0).toString().equals(entry.getKey())) {
					model2.setValueAt(entry.getValue(), i, 1);
				}
			}
		}

		JTable table = new JTable(model2);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		jg2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < model2.getRowCount(); i++) {
					model2.setValueAt("0", i, 1);
				}
				if((int)jg2.getSelectedItem() == sc.getJahrgang()) {
					for (Entry<String, Integer> entry : sc.getStundenbedarf().entrySet()) {
						for (int i = 0; i < model2.getRowCount(); i++) {
							if (model2.getValueAt(i, 0).toString().equals(entry.getKey())) {
								model2.setValueAt(entry.getValue(), i, 1);
							}
						}
					}
				}else {
					for (Entry<String, Integer> entry : DataSchulklasse
							.getJahrgangByJahrgang((int)jg2.getSelectedItem())
							.getStundenbedarf().entrySet()) {
						for (int i = 0; i < model2.getRowCount(); i++) {
							if (model2.getValueAt(i, 0).toString()
									.equals(entry.getKey())) {
								model2.setValueAt(entry.getValue(), i, 1);
							}
						}
					}
				}
			}
		});

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table.getTableHeader(), c);

		c.gridy = 8;
		c.gridx = 0;
		p.add(table, c);

		c.gridwidth = 1;
		c.gridy = 9;
		c.gridx = 0;
		p.add(button2, c);

		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (!check(p))
						throw new WrongInputException();

					
					ArrayList<String> kt = new ArrayList<String>();
					for (int i = 0; i < checkList2.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList2.getModel()
								.getElementAt(i);
						if (cb.isSelected())
							kt.add(cb.getText());
					}

					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					for (int i = 0; i < model2.getRowCount(); i++) {
						hm.put((String) model2.getValueAt(i, 0), (Integer) model2.getValueAt(i, 1));
					}

					SchulklassenManager.editSchoolclass(sc.getName(), new Schoolclass((jg2
							.getSelectedItem()) + bezField2.getText(), (int) jg2
							.getSelectedItem(), (Room) jcb2.getSelectedItem(),
							kt, hm));
					
					
					updateList();
					JFrame topFrame = (JFrame) SwingUtilities
							.getWindowAncestor(p);
					topFrame.dispose();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});

		c.gridx = 1;
		p.add(button3, c);

		// abbruch Button
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
				topFrame.dispose();
			}
		});
		return p;
	}

	private boolean check(final JPanel p) {
		if (textFieldsEmpty(p)) return false;

		boolean b = false;
		if(checkList != null){
			ArrayList<String> kt = new ArrayList<String>();
			for (int i = 0; i < checkList.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
				if (cb.isSelected())
					kt.add(cb.getText());
			}
			for (String s : kt) {
				if (PersonalManager.getPersonalByKuerzel(s).isLehrer())
					b = true;
			}
		}
		if(checkList2 != null){
			ArrayList<String> kt = new ArrayList<String>();
			for (int i = 0; i < checkList2.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList2.getModel().getElementAt(i);
				if (cb.isSelected())
					kt.add(cb.getText());
			}
			for (String s : kt) {
				if (PersonalManager.getPersonalByKuerzel(s).isLehrer())
					b = true;
			}
		}
	
		try {
			if (!b)
				throw new MindestestensEinLehrerException();
		} catch (MindestestensEinLehrerException e) {
			e.printStackTrace();
			return false;
		}

		try {
			if (model != null) {
				for (int i = 0; i < model.getRowCount(); i++) {
					Integer.parseInt((String) model.getValueAt(i, 1));
				}
			}
			if (model2 != null) {
				for (int i = 0; i < model.getRowCount(); i++) {
					Integer.parseInt((String) model.getValueAt(i, 1));
				}
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean textFieldsEmpty(final JPanel p) {
		for (Component c : p.getComponents()) {
			if (c instanceof TextField) {
				TextField tf = (TextField) c;
				if (!tf.getText().isEmpty())
					return false;
			}
			if (c instanceof JTextField) {
				JTextField tf = (JTextField) c;
				if (!tf.getText().isEmpty())
					return false;
			}
		}
		return true;
	}

	public static void updateList() {
		listModel.clear();
		for (Schoolclass sc : DataSchulklasse.getAllSchulklasse()) {
			listModel.addElement(sc);
		}
	}

}
