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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class BedarfPanel extends JPanel {

	private Label lName = new Label("Jahrgang: ");
	private Label lSti = new Label("Stundeninhalt: ");
	private Label lBed = new Label("Bedarf in Stunden: ");

	private JComboBox cb1;
	private JComboBox cb2;

	public TextField bedField = new TextField(3);
	public TextField bedField2 = new TextField(3);
	public Integer[] jahrgaenge = { 1, 2, 3, 4 };

	public JButton button = new JButton("Bedarf hinzufuegen");

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	public BedarfPanel() {
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
		p.setBorder(BorderFactory
				.createTitledBorder("Bedarf an Stundeninhalten pro Jahrgang"));
		c.insets = new Insets(10, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName, c);
		c.gridx = 1;
		cb1 = new JComboBox(jahrgaenge);
		p.add(cb1, c);
		c.gridx = 2;
		c.weightx = 0;
		p.add(lBed, c);
		c.gridx = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(bedField, c);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 1;
		p.add(lSti, c);
		c.gridx = 1;
		c.gridwidth = 3;
		ArrayList<Stundeninhalt> si = DataStundeninhalt.getAllStundeninhalte();
		cb2 = new JComboBox(si.toArray());
		p.add(cb2, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (!check(p))
						throw new WrongInputException();
					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					hm.put(((Stundeninhalt) cb2.getSelectedItem()).getKuerzel(),
							Integer.parseInt(bedField.getText()));
					DataSchulklasse.addJahrgang(new Jahrgang((int) cb1
							.getSelectedItem(), hm));

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
		p.setBorder(BorderFactory.createTitledBorder("Existierender Bedarf"));

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
				Matcher matcher = Pattern.compile("'(.*?)'").matcher(
						list.getSelectedValue());
				ArrayList<String> arr = new ArrayList<String>();
				while (matcher.find()) {
					System.out.println(matcher.group(1));
					arr.add(matcher.group(1));
				}
				pop.edit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame edit = new JFrame("Bedarf editieren");
						edit.add(createEditPanel(new JPanel(), DataSchulklasse
								.getJahrgangByJundSkuerzel(
										Integer.parseInt(arr.get(0)),
										arr.get(1))));
						edit.pack();
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(
								DataSchulklasse.getJahrgangByJundSkuerzel(
										Integer.parseInt(arr.get(0)),
										arr.get(1)));
						deleteD.setVisible(true);
					}
				});
			}
		});

		return p;
	}

	private JPanel createEditPanel(final JPanel p, final Jahrgang j) {
		c = new GridBagConstraints();
		Label lBed2 = new Label("Bedarf in Stunden: ");
		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Editieren des Bedarfs"));
		c.insets = new Insets(10, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Jahrgang: " + j.getJahrgang()), c);
		c.gridy = 1;
		Entry<String, Integer> ent=j.getStundenbedarf().entrySet().iterator().next();
		String stdi = ent.getKey();
		int stdb =ent.getValue();
		
		p.add(new Label("Stundeninhalt: " + stdi),c);
		c.gridy = 2;
		p.add(lBed2, c);
		c.gridx = 1;
		bedField2.setText("" + stdb);
		p.add(bedField2, c);
		c.gridx = 0;
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (!check(p)) throw new WrongInputException();
					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					hm.put(stdi, Integer.parseInt(bedField2.getText()));
					j.setStundenbedarf(hm);
					DataSchulklasse.editJahrgang(j);

					updateList();
					JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
					topFrame.dispose();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
	
		c.gridx=1;
		p.add(button3, c);
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
				topFrame.dispose();
			}
		});

		return p;
	}

	private boolean check(final JPanel p) {
		if (textFieldsEmpty(p))
			return false;
		try {
			Integer.parseInt(bedField2.getText());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean textFieldsEmpty(final JPanel p) {
		boolean b = true;
		for (Component c : p.getComponents()) {
			if (c instanceof TextField) {
				TextField tf = (TextField) c;
				if (!tf.getText().isEmpty())
					b = false;
			}
			if (c instanceof JTextField) {
				JTextField tf = (JTextField) c;
				if (!tf.getText().isEmpty())
					b = false;
			}
		}
		return b;
	}

	public static void updateList() {
		listModel.clear();
		for (Jahrgang ja : DataSchulklasse.getAllJahrgang()) {
			for (Entry<String, Integer> entry : ja.getStundenbedarf()
					.entrySet()) {
				listModel.addElement("Jahrgang: '" + ja.getJahrgang()
						+ "', Stundeninhalt: '" + entry.getKey()
						+ "', Bedarf: " + entry.getValue() + " Stunden");
			}
		}
		;
	}
}
