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
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.exceptions.KuerzelException;
import de.unibremen.swp.stundenplan.exceptions.MindestestensEinLehrerException;
import de.unibremen.swp.stundenplan.exceptions.RaumException;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.exceptions.ZahlException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.RaumManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

/**
 * Repraesentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * von Schulklassen
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class SchoolclassPanel extends JPanel {

	/**
	 * Jahrgangsbezeichnerfeld im Addpanel
	 */
	private JTextField bezField;
	
	/**
	 * Jahrgangsbezeichnerfeld im Editpanel
	 */
	private JTextField bezField2;

	/**
	 * Moegliche Jahrgaenge
	 */
	private Integer[] jahrgang = { 1, 2, 3, 4 };
	
	/**
	 * Klassenraumcombobox beim Addpanel
	 */
	private JComboBox<Object> cb1;
	
	/**
	 * Klassenraumcombobox beim Editpanel
	 */
	private JComboBox<Object> cb2;
	
	/**
	 * Jahrgangscombobox beim Addpanel
	 */
	private JComboBox<Object> jg;
	
	/**
	 * Jahrgangscombobox beim Editpanel
	 */
	private JComboBox<Object> jg2;

	/**
	 * checklist fuer die Personalteams beim addpanel
	 */
	private CheckBoxList checkList;

	/**
	 * checklist fuer die Personalteams beim editpanel
	 */
	private CheckBoxList checkList2;

	/**
	 * GridBagConsraint fuer die add,edit,listpanel
	 */
	private GridBagConstraints c;

	/**
	 * GridBagConsraint fuer das gesamte Panel
	 */
	private GridBagConstraints c2;

	/**
	 * ListModel fuer Schulklassenliste
	 */
	private static DefaultListModel<Schoolclass> listModel = new DefaultListModel<Schoolclass>();

	/**
	 * JList fuer Schulklassen
	 */
	private JList<Schoolclass> list = new JList<Schoolclass>(listModel);

	/**
	 * Scrollbar fuer Jlist von Schulklassen
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * TableModel fuer den Bedarf beim addpanel
	 */
	private DefaultTableModel model;

	/**
	 * TableModel fuer den Bedarf beim editpanel
	 */
	private DefaultTableModel model2;

	/**
	 * Konstruktor vom SchoolclassPanel
	 */
	public SchoolclassPanel() {
		c2 = new GridBagConstraints();
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

	/**
	 * Erzeugt ein Panel auf dem man einee neue Schulklasse hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return Hinzuzufuegendes Panel
	 */
	@SuppressWarnings("unchecked")
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		JLabel lKt = new JLabel("Klassenteam:");
		bezField = new JTextField(5);
		jg = new JComboBox<Object>(jahrgang);
		JButton button = new JButton("Klasse hinzufuegen");
		checkList = new CheckBoxList();

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
		p.add(new Label("Jahrgang:"), c);
		c.gridx = 1;
		p.add(jg, c);
		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Zusatzbezeichner:"), c);
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
		cb1 = new JComboBox<Object>(ro.toArray());
		p.add(cb1, c);

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

		final JTable table = new JTable();
		table.setModel(model);
		final JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(100,100));

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

		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 7;
		c.gridx = 0;
		p.add(scroll, c);

		c.gridy = 8;
		c.gridx = 0;
		p.add(button, c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (table.getCellEditor() != null)
					table.getCellEditor().stopCellEditing();
				if (!check(p))
					return;
				ArrayList<String> kt = new ArrayList<String>();
				for (int i = 0; i < checkList.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						kt.add(cb.getText());
				}

				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				for (int i = 0; i < model.getRowCount(); i++) {
					hm.put((String) model.getValueAt(i, 0),
							Integer.parseInt((String) model.getValueAt(i, 1)));
				}

				DataSchulklasse.addSchulklasse(new Schoolclass((jg
						.getSelectedItem()) + bezField.getText(), (int) jg
						.getSelectedItem(), (Room) cb1.getSelectedItem(), kt,
						hm));
				updateList();
			}
		});
		return p;

	}

	/**
	 * Erzeugt ein Panel auf dem man die Schulklassenliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzuzufuegendesPanel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
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
						edit.setLocation((MouseInfo.getPointerInfo()
								.getLocation().x)-20, (MouseInfo.getPointerInfo()
								.getLocation().y)-300);
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

	/**
	 * Erzeugt ein Panel auf dem man eine Schulklasse editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param sc
	 *            zu editierende Schulklasse
	 * @return Hinzuzufuegendes Panel
	 */
	@SuppressWarnings("unchecked")
	private JPanel createEditPanel(final JPanel p, final Schoolclass sc) {
		c = new GridBagConstraints();
		JLabel lKt2 = new JLabel("Klassenteam:");
		checkList2 = new CheckBoxList();
		bezField2 = new JTextField(5);
		jg2 = new JComboBox<Object>(jahrgang);

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
		p.add(new Label("Jahrgang:"), c);
		c.gridx = 1;
		p.add(jg2, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Zusatzbezeichner:"), c);
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
		lKt2.setFont(new Font(bezField2.getFont().getFontName(), Font.PLAIN,
				bezField2.getFont().getSize()));
		p.add(lKt2, c);
		c.gridy = 4;

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;

		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Personal per : PersonalManager.getAllPersonalFromDB()) {
			boxes.add(new JCheckBox(per.getKuerzel()));
		}

		for (JCheckBox jcb : boxes) {
			for (String s : sc.getKlassenlehrer()) {
				if (jcb.getText().equals(s))
					jcb.setSelected(true);
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
		cb2 = new JComboBox<Object>(ro.toArray());

		for (int i = 0; i < ro.size(); i++) {
			if (ro.get(i).getName().equals(sc.getKlassenraum().getName()))
				cb2.setSelectedIndex(i);
		}
		p.add(cb2, c);

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
					model2.setValueAt(entry.getValue()+"", i, 1);
				}
			}
		}

		final JTable table2 = new JTable();
		table2.setModel(model2);
		final JScrollPane scroll2 = new JScrollPane(table2);
		scroll2.setPreferredSize(new Dimension(100,100));
		
		table2.setColumnSelectionAllowed(false);
		table2.getTableHeader().setReorderingAllowed(false);
		table2.getTableHeader().setResizingAllowed(false);

		jg2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < model2.getRowCount(); i++) {
					model2.setValueAt("0", i, 1);
				}
				if ((int) jg2.getSelectedItem() == sc.getJahrgang()) {
					for (Entry<String, Integer> entry : sc.getStundenbedarf()
							.entrySet()) {
						for (int i = 0; i < model2.getRowCount(); i++) {
							if (model2.getValueAt(i, 0).toString()
									.equals(entry.getKey())) {
								model2.setValueAt(entry.getValue()+"", i, 1);
							}
						}
					}
				} else {
					for (Entry<String, Integer> entry : DataSchulklasse
							.getJahrgangByJahrgang((int) jg2.getSelectedItem())
							.getStundenbedarf().entrySet()) {
						for (int i = 0; i < model2.getRowCount(); i++) {
							if (model2.getValueAt(i, 0).toString()
									.equals(entry.getKey())) {
								model2.setValueAt(entry.getValue()+"", i, 1);
							}
						}
					}
				}
			}
		});

		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 7;
		c.gridx = 0;
		p.add(scroll2, c);

		c.gridy = 8;
		c.gridx = 0;
		c.gridwidth = 1;
		p.add(button2, c);

		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (table2.getCellEditor() != null)
					table2.getCellEditor().stopCellEditing();
				if (!check(p))
					return;
				ArrayList<String> kt = new ArrayList<String>();
				for (int i = 0; i < checkList2.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList2.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						kt.add(cb.getText());
				}

				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				for (int i = 0; i < model2.getRowCount(); i++) {
					hm.put((String) model2.getValueAt(i, 0),
							Integer.parseInt((String)model2.getValueAt(i, 1)));
				}

				SchulklassenManager.editSchoolclass(
						sc.getName(),
						new Schoolclass((jg2.getSelectedItem())
								+ bezField2.getText(), (int) jg2
								.getSelectedItem(), (Room) cb2
								.getSelectedItem(), kt, hm));

				updateList();
				((JFrame) SwingUtilities.getWindowAncestor(p)).dispose();

			}
		});

		c.gridx = 1;
		p.add(button3, c);

		// abbruch Button
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				((JFrame) SwingUtilities.getWindowAncestor(p)).dispose();
			}
		});
		return p;
	}

	/**
	 * Ueberprueft, ob es irgendwelche falsche EIngaben gibt. Zb Leere Felder,
	 * Zahlen in Textfeldern, zu lange Kuerzel etc.
	 * 
	 * @param p
	 *            uebergebenes panel
	 * @return true, wenn alles ok ist, false, wenn eine Eingabe falsch ist
	 */
	private boolean check(final JPanel p) {
		boolean b = true;
		if (textFieldsEmpty(p)) {
			new TextException();
			b = false;
		}
		if (bezField != null
				&& bezField.getText().length() > Data.MAX_KUERZEL_LEN) {
			new KuerzelException();
			b = false;
		}
		if (bezField2 != null
				&& bezField2.getText().length() > Data.MAX_KUERZEL_LEN) {
			new KuerzelException();
			b = false;
		}

		boolean b2 = false;
		if (checkList != null) {
			ArrayList<String> kt = new ArrayList<String>();
			for (int i = 0; i < checkList.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
				if (cb.isSelected())
					kt.add(cb.getText());
			}
			for (String s : kt) {
				if (PersonalManager.getPersonalByKuerzel(s).isLehrer())
					b2 = true;
			}
		}
		if (checkList2 != null) {
			ArrayList<String> kt = new ArrayList<String>();
			for (int i = 0; i < checkList2.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList2.getModel()
						.getElementAt(i);
				if (cb.isSelected())
					kt.add(cb.getText());
			}
			for (String s : kt) {
				if (PersonalManager.getPersonalByKuerzel(s).isLehrer())
					b2 = true;
			}
		}
		if (!b2) {
			new MindestestensEinLehrerException();
			b = false;
		}
		if (cb1 != null && cb1.getSelectedItem() == null) {
			new RaumException();
			b = false;
		}
		if (cb2 != null && cb2.getSelectedItem() == null) {
			new RaumException();
			b = false;
		}
		try {
			if (model != null) {
				for (int i = 0; i < model.getRowCount(); i++) {
					if (Integer.parseInt(((String)model.getValueAt(i, 1))) < 0) {
						new ZahlException();
						b = false;
					}
				}
			}
			if (model2 != null) {
				for (int i = 0; i < model2.getRowCount(); i++) {
					if (Integer.parseInt(((String)model2.getValueAt(i, 1))) < 0) {
						new ZahlException();
						b = false;
					}
				}
			}
		} catch (NumberFormatException e) {
			new ZahlException();
			b = false;
		}
		return b;
	}

	/**
	 * Ueberprueft ob ein Textfeld leer ist
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return true, wenn ein Textfeld leer ist, false, wenn ein Textfeld nicht
	 *         leer ist
	 */
	private boolean textFieldsEmpty(final JPanel p) {
		for (Component c : p.getComponents()) {
			if (c instanceof TextField) {
				if (((TextField) c).getText().isEmpty())
					return true;
			}
			if (c instanceof JTextField) {
				if (((JTextField) c).getText().isEmpty())
					return true;
			}
		}
		return false;
	}

	/**
	 * leert die Liste des Panels und fuellt sie anschlie�end wieder mit allen
	 * Daten der Datenbank
	 */
	public static void updateList() {
		listModel.clear();
		for (Schoolclass sc : DataSchulklasse.getAllSchulklasse()) {
			listModel.addElement(sc);
		}
	}

}
