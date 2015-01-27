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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import javax.swing.table.TableColumn;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.exceptions.KuerzelException;
import de.unibremen.swp.stundenplan.exceptions.StundeninhaltException;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.exceptions.ZahlException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Repraesentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * von Personal
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class PersonalPanel extends JPanel {

	/**
	 * Eingabefeld fuer das Kuerzel beim Addpanel
	 */
	private JTextField kuerzField;

	/**
	 * Eingabefeld fuer das Kuerzel beim Editpanel
	 */
	private JTextField kuerzField2;

	/**
	 * Eingabefeld fuer die Zeitverpflichtung beim Addpanel
	 */
	private JTextField pflichtField;

	/**
	 * Eingabefeld fuer die Zeitverpflichtung beim Editpanel
	 */
	private JTextField pflichtField2;

	/**
	 * Eingabefeld fuer die Ersatzzeit beim Addpanel
	 */
	private JTextField ersatzField;

	/**
	 * Eingabefeld fuer die Ersatzzeit beim Editpanel
	 */
	private JTextField ersatzField2;

	/**
	 * checklist fuer die Stundeninhalte beim addpanel
	 */
	private CheckBoxList checkList;

	/**
	 * checklist fuer die Stundeninhalte beim editpanel
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
	 * ListModel fuer Personalliste
	 */
	private static DefaultListModel<Personal> listModel = new DefaultListModel<Personal>();

	/**
	 * JList fuer Personal
	 */
	private JList<Personal> list = new JList<Personal>(listModel);

	/**
	 * Scrollbar fuer Jlist von Personal
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * TableModel fuer die wunschzeiten beim addpanel
	 */
	private DefaultTableModel model;

	/**
	 * TableModel fuer die wunschzeiten beim editpanel
	 */
	private DefaultTableModel model2;

	/**
	 * Konstruktor vom PersonalPanel
	 */
	public PersonalPanel() {
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
	 * Erzeugt ein Panel auf dem man neues Personal hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return Hinzuzufuegendes Panel
	 */
	@SuppressWarnings({ "unchecked" })
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		final JTextField nameField = new JTextField(15);
		kuerzField = new JTextField(5);
		pflichtField = new JTextField(5);
		ersatzField = new JTextField(5);
		JLabel lSubjects = new JLabel("Moegliche Stundeninhalte :");
		JButton button = new JButton("Personal hinzufuegen");
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
				.createTitledBorder("Neues Personal hinzufuegen"));
		c.insets = new Insets(10, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name:"), c);

		c.gridx = 1;
		p.add(nameField, c);

		c.gridx = 2;
		final JRadioButton lehrerB = new JRadioButton("Lehrer");
		lehrerB.setSelected(true);
		JRadioButton paedagogeB = new JRadioButton("Paedagoge");
		ButtonGroup group = new ButtonGroup();
		group.add(lehrerB);
		group.add(paedagogeB);
		p.add(lehrerB, c);

		c.gridx = 3;
		p.add(paedagogeB, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Kuerzel:"), c);

		c.gridx = 1;
		p.add(kuerzField, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Zeitverpflichtung (Std):"), c);

		c.gridx = 1;
		p.add(pflichtField, c);

		c.gridx = 2;
		p.add(new Label("Ersatzzeit (Std):"), c);

		c.gridx = 3;
		p.add(ersatzField, c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		c.gridy = 4;
		p.add(new Label("Wunschzeiten:"), c);

		final JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		model.addColumn("Wochentag");
		model.addColumn("Von (h)");
		model.addColumn("Von (min)");
		model.addColumn("Bis (h)");
		model.addColumn("Bis (min)");
		TableColumn column = null;
		for (int i = 0; i < model.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(100);
			} else {
				column.setPreferredWidth(20);
			}
		}
		String sh;
		String sm;
		String eh;
		String em;
		if (TimetableManager.startTimeHour() < 10) {
			sh = "0" + TimetableManager.startTimeHour();
		} else {
			sh = "" + TimetableManager.startTimeHour();
		}
		if (TimetableManager.startTimeMinute() < 10) {
			sm = "0" + TimetableManager.startTimeMinute();
		} else {
			sm = "" + TimetableManager.startTimeMinute();
		}
		if (TimetableManager.endTimeHour() < 10) {
			eh = "0" + TimetableManager.endTimeHour();
		} else {
			eh = "" + TimetableManager.endTimeHour();
		}
		if (TimetableManager.endTimeMinute() < 10) {
			em = "0" + TimetableManager.endTimeMinute();
		} else {
			em = "" + TimetableManager.endTimeMinute();
		}
		final ArrayList<Weekday> wds = new ArrayList<Weekday>();
		if (Weekday.MONDAY.isSchoolday()) {
			model.addRow(new String[] { Config.MONDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.MONDAY);
		}
		if (Weekday.TUESDAY.isSchoolday()) {
			model.addRow(new String[] { Config.TUESDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.TUESDAY);
		}
		if (Weekday.WEDNESDAY.isSchoolday()) {
			model.addRow(new String[] { Config.WEDNESDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.WEDNESDAY);
		}
		if (Weekday.THURSDAY.isSchoolday()) {
			model.addRow(new String[] { Config.THURSDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.THURSDAY);
		}
		if (Weekday.FRIDAY.isSchoolday()) {
			model.addRow(new String[] { Config.FRIDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.FRIDAY);
		}
		if (Weekday.SATURDAY.isSchoolday()) {
			model.addRow(new String[] { Config.SATURDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.SATURDAY);
		}
		if (Weekday.SUNDAY.isSchoolday()) {
			model.addRow(new String[] { Config.SUNDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.SUNDAY);
		}
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table.getTableHeader(), c);

		c.gridy = 6;
		p.add(table, c);

		c.gridy = 0;
		c.gridx = 5;
		c.gridwidth = 1;
		c.gridheight = 8;
		c.fill = GridBagConstraints.VERTICAL;
		p.add(new JSeparator(SwingConstants.VERTICAL), c);

		c.gridx = 6;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSubjects.setFont(new Font(nameField.getFont().getFontName(),
				Font.PLAIN, nameField.getFont().getSize()));
		p.add(lSubjects, c);

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 7;
		c.gridy = 1;
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		for (Stundeninhalt s : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		}

		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 9;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add Button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (table.getCellEditor() != null)
					table.getCellEditor().stopCellEditing();
				if (!check(p))
					return;
				HashMap<Weekday, int[]> wunsch = new HashMap<Weekday, int[]>();

				for (int i = 0; i < wds.size(); i++) {
					int[] arr = {
							Integer.parseInt((String) model.getValueAt(i, 1)),
							Integer.parseInt((String) model.getValueAt(i, 2)),
							Integer.parseInt((String) model.getValueAt(i, 3)),
							Integer.parseInt((String) model.getValueAt(i, 4)) };

					wunsch.put(wds.get(i), arr);
				}

				ArrayList<String> stdi = new ArrayList<String>();
				for (int i = 0; i < checkList.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList.getModel()
							.getElementAt(i);
					if (cb.isSelected()) {
						stdi.add(cb.getText());
					}
				}

				Personal pe = new Personal(nameField.getText(), kuerzField
						.getText(), Integer.parseInt(pflichtField.getText()),
						Integer.parseInt(ersatzField.getText()), lehrerB
								.isSelected(), stdi, wunsch);
				PersonalManager.addPersonalToDb(pe);
				updateList();

			}
		});
		return p;
	}

	/**
	 * Erzeugt ein Panel auf dem man die Personalliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzuzufuegendesPanel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Existierendes Personal"));

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
						JFrame edit = new JFrame("Personal editieren");
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

	/**
	 * Erzeugt ein Panel auf dem man Personal editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param pe
	 *            zu editierendes Personal
	 * @return Hinzuzufuegendes Panel
	 */
	@SuppressWarnings({ "unchecked" })
	private JPanel createEditPanel(final JPanel p, final Personal pe) {
		c = new GridBagConstraints();
		final JTextField nameField2 = new JTextField(15);
		kuerzField2 = new JTextField(5);
		JLabel lSubjects2 = new JLabel(
				"<html><body>Moegliche<br>Stundeninhalte :</body></html>");
		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");
		checkList2 = new CheckBoxList();

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

		c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name des Personals:"), c);
		c.gridx = 1;
		p.add(nameField2, c);
		nameField2.setText(pe.getName());

		c.gridx = 2;
		final JRadioButton lehrerB2 = new JRadioButton("Lehrer");
		JRadioButton paedagogeB2 = new JRadioButton("Paedagoge");
		if (pe.isLehrer()) {
			lehrerB2.setSelected(true);
		} else {
			paedagogeB2.setSelected(true);
		}
		ButtonGroup group2 = new ButtonGroup();
		group2.add(lehrerB2);
		group2.add(paedagogeB2);
		p.add(lehrerB2, c);

		c.gridx = 3;
		p.add(paedagogeB2, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Kuerzel:"), c);
		c.gridx = 1;
		p.add(kuerzField2, c);
		kuerzField2.setText(pe.getKuerzel());

		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Zeitverpflichtung in h:"), c);
		c.gridx = 1;
		p.add(pflichtField2, c);
		pflichtField2.setText(pe.getSollZeit() + "");

		c.gridx = 2;
		p.add(new Label("Ersatzzeit in h:"), c);
		c.gridx = 3;
		p.add(ersatzField2, c);
		ersatzField2.setText(pe.getErsatzZeit() + "");

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		c.gridy = 4;
		p.add(new Label("Wunschzeiten:"), c);

		final JTable table2 = new JTable(model2);
		table2.setColumnSelectionAllowed(false);

		model2.addColumn("Wochentag");
		model2.addColumn("Von (h)");
		model2.addColumn("Von (min)");
		model2.addColumn("Bis (h)");
		model2.addColumn("Bis (min)");

		String sh;
		String sm;
		String eh;
		String em;
		Map<Weekday, int[]> map = new TreeMap<Weekday, int[]>(
				pe.getWunschzeiten());
		for (Entry<Weekday, int[]> entry : map.entrySet()) {
			int[] arr = entry.getValue();
			if (arr[0] < 10) {
				sh = "0" + arr[0];
			} else {
				sh = "" + arr[0];
			}
			if (arr[1] < 10) {
				sm = "0" + arr[1];
			} else {
				sm = "" + arr[1];
			}
			if (arr[2] < 10) {
				eh = "0" + arr[2];
			} else {
				eh = "" + arr[2];
			}
			if (arr[3] < 10) {
				em = "0" + arr[3];
			} else {
				em = "" + arr[3];
			}
			model2.addRow(new String[] { entry.getKey().toString(), sh, sm, eh,
					em });
		}
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table2.getTableHeader(), c);
		c.gridy = 6;
		p.add(table2, c);
		c.gridy = 7;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 8;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSubjects2.setFont(new Font(nameField2.getFont().getFontName(),
				Font.PLAIN, nameField2.getFont().getSize()));
		p.add(lSubjects2, c);

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridy = 9;
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		for (Stundeninhalt s : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		}

		for (JCheckBox jcb : boxes) {
			for (String s : pe.getMoeglicheStundeninhalte()) {
				if (jcb.getText().equals(s))
					jcb.setSelected(true);
			}
		}

		checkList2.setListData(boxes.toArray());
		checkList2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList2, c);

		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (table2.getCellEditor() != null)
					table2.getCellEditor().stopCellEditing();
				if (!check(p))
					return;
				HashMap<Weekday, int[]> wunsch = new HashMap<Weekday, int[]>();
				for (int i = 0; i < model2.getRowCount(); i++) {
					int[] arr = {
							Integer.parseInt((String) model2.getValueAt(i, 1)),
							Integer.parseInt((String) model2.getValueAt(i, 2)),
							Integer.parseInt((String) model2.getValueAt(i, 3)),
							Integer.parseInt((String) model2.getValueAt(i, 4)) };
					wunsch.put(
							Weekday.getDay((String) model2.getValueAt(i, 0)),
							arr);
				}
				ArrayList<String> stunden = new ArrayList<String>();
				for (int i = 0; i < checkList2.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList2.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						stunden.add(cb.getText());
				}
				PersonalManager.editPersonal(
						pe.getKuerzel(),
						new Personal(nameField2.getText(), kuerzField2
								.getText(), Integer.parseInt(pflichtField2
								.getText()), Integer.parseInt(ersatzField2
								.getText()), lehrerB2.isSelected(), stunden,
								wunsch));

				updateList();
				((JFrame) SwingUtilities.getWindowAncestor(p)).dispose();

			}
		});

		c.gridx = 3;
		c.gridwidth = 2;
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
	 * Ueberprueft, ob es irgendwelche falsche Eingaben gibt. Zb Leere Felder,
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
		if ((kuerzField != null && kuerzField.getText().length() > Data.MAX_KUERZEL_LEN)) {
			new KuerzelException();
			b = false;
		}
		if (kuerzField2 != null
				&& kuerzField2.getText().length() > Data.MAX_KUERZEL_LEN) {
			new KuerzelException();
			b = false;
		}
		try {
			if (model != null) {
				for (int i = 0; i < model.getRowCount(); i++) {
					if ((Integer.parseInt((String) model.getValueAt(i, 1)) > 23)
							|| (Integer.parseInt((String) model
									.getValueAt(i, 1)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model.getValueAt(i, 2)) > 59)
							|| (Integer.parseInt((String) model
									.getValueAt(i, 2)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model.getValueAt(i, 3)) > 23)
							|| (Integer.parseInt((String) model
									.getValueAt(i, 3)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model.getValueAt(i, 4)) > 59)
							|| (Integer.parseInt((String) model
									.getValueAt(i, 4)) < 0)) {
						new ZahlException();
						b = false;
					}
				}
			}
			if (model2 != null) {
				for (int i = 0; i < model2.getRowCount(); i++) {
					if ((Integer.parseInt((String) model2.getValueAt(i, 1)) > 23)
							|| (Integer.parseInt((String) model2.getValueAt(i,
									1)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model2.getValueAt(i, 2)) > 59)
							|| (Integer.parseInt((String) model2.getValueAt(i,
									2)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model2.getValueAt(i, 3)) > 23)
							|| (Integer.parseInt((String) model2.getValueAt(i,
									3)) < 0)) {
						new ZahlException();
						b = false;
					}
					if ((Integer.parseInt((String) model2.getValueAt(i, 4)) > 59)
							|| (Integer.parseInt((String) model2.getValueAt(i,
									4)) < 0)) {
						new ZahlException();
						b = false;
					}
				}
			}
			for (Component c : p.getComponents()) {
				if (c == pflichtField) {
					if (Integer.parseInt(pflichtField.getText()) < 1) {
						new ZahlException();
						b = false;
					}
				}
				if (c == pflichtField2) {
					if (Integer.parseInt(pflichtField2.getText()) < 1) {
						new ZahlException();
						b = false;
					}
				}
				if (c == ersatzField) {
					if (Integer.parseInt(ersatzField.getText()) < 1) {
						new ZahlException();
						b = false;
					}
				}
				if (c == ersatzField2) {
					if (Integer.parseInt(ersatzField2.getText()) < 1) {
						new ZahlException();
						b = false;
					}
				}
			}
		} catch (NumberFormatException e) {
			new ZahlException();
			b = false;
		}
		boolean b2 = true;
		if (checkList != null) {
			for (int i = 0; i < checkList.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
				if (cb.isSelected())
					b2 = false;
			}
		}
		if (checkList2 != null) {
			for (int i = 0; i < checkList2.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList2.getModel()
						.getElementAt(i);
				if (cb.isSelected())
					b2 = false;
			}
		}
		if (b2) {
			new StundeninhaltException();
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
	 * leert die Liste des Panels und fuellt sie anschließend wieder mit allen
	 * Daten der Datenbank
	 */
	public static void updateList() {
		listModel.clear();
		for (Personal per : PersonalManager.getAllPersonalFromDB()) {
			listModel.addElement(per);
		}
	}
}
