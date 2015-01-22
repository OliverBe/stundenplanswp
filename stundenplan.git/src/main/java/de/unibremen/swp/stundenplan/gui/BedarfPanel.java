package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.StundeninhaltException;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.exceptions.ZahlException;
import de.unibremen.swp.stundenplan.logic.JahrgangsManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

/**
 * Repräsentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * vom Jahrgangsbedarf
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class BedarfPanel extends JPanel {

	/**
	 * JComboBox fuer die Jahrgangsstufen beim add panel
	 */
	private JComboBox<Integer> cb1;

	/**
	 * JComboBox fuer die Jahrgangsstufen beim edit panel
	 */
	private JComboBox<Integer> cb2;

	/**
	 * Eingabefeld fuer die Stundenzahl des Bedarfs beim addpanel
	 */
	private JTextField bedField;

	/**
	 * Eingabefeld fuer die Stundenzahl des Bedarfs beim editpanel
	 */
	private JTextField bedField2;

	/**
	 * Jahrgaenge in der JComboBox
	 */
	private static Integer[] jahrgaenge = { 1, 2, 3, 4 };

	/**
	 * GridBagConsraint fuer die add,edit,listpanel
	 */
	private GridBagConstraints c;

	/**
	 * GridBagConsraint fuer das gesamte Panel
	 */
	private GridBagConstraints c2;

	/**
	 * ListModel fuer die JList des Bedarfs
	 */
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();

	/**
	 * JList des Bedarfs
	 */
	private JList<String> list = new JList<String>(listModel);

	/**
	 * Scroller fuer die JList des Bedarfs
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * Konstruktor des Bedarfpanels
	 */
	public BedarfPanel() {
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
	 * Erzeugt ein Panel auf dem man einen neuen Bedarf hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzufuegenPanel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		bedField = new JTextField(3);
		JButton button = new JButton("Bedarf hinzufuegen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Bedarf an Stundeninhalten pro Jahrgang"));
		c.insets = new Insets(10, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Jahrgang: "), c);
		c.gridx = 1;
		cb1 = new JComboBox<Integer>(jahrgaenge);
		p.add(cb1, c);
		c.gridx = 2;
		c.weightx = 0;
		p.add(new Label("Bedarf (Std):"), c);
		c.gridx = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(bedField, c);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Stundeninhalt: "), c);
		c.gridx = 1;
		c.gridwidth = 3;
		ArrayList<Stundeninhalt> si = StundeninhaltManager.getAllStundeninhalteFromDB();
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
				if (!check(p))
					return;
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				hm.put(((Stundeninhalt) cb2.getSelectedItem()).getKuerzel(),
						Integer.parseInt(bedField.getText()));
				JahrgangsManager.addBedarfToJahrgang(new Jahrgang((int) cb1
						.getSelectedItem(), hm));
				updateList();
			}
		});
		return p;

	}

	/**
	 * Erzeugt ein Panel auf dem man die Bedarfliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzufuegenPanel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
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
				if (event.getValueIsAdjusting()) {
					final DataPopup pop = new DataPopup(list, listModel);
					setComponentPopupMenu(pop);
					list.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (e.isMetaDown()) {
								pop.show(list, e.getX(), e.getY());
							}
						}
					});
					final ArrayList<String> arr = new ArrayList<String>();
					try {
						Matcher matcher = Pattern.compile("'(.*?)'").matcher(
								list.getSelectedValue());
						while (matcher.find() && arr.size() < 2) {
							arr.add(matcher.group(1));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
					pop.edit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							JFrame edit = new JFrame("Bedarf editieren");
							edit.add(createEditPanel(new JPanel(),
									JahrgangsManager.getJahrgangByJundSkuerzelFromDB(
											Integer.parseInt(arr.get(0)),
											arr.get(1))));
							edit.setLocation(MouseInfo.getPointerInfo()
									.getLocation().x, MouseInfo
									.getPointerInfo().getLocation().y);
							edit.pack();
							edit.setVisible(true);
						}
					});
					pop.delete.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							DeleteDialogue deleteD = new DeleteDialogue(
									JahrgangsManager.getJahrgangByJundSkuerzelFromDB(
											Integer.parseInt(arr.get(0)),
											arr.get(1)));
							deleteD.setLocation(MouseInfo.getPointerInfo()
									.getLocation().x, MouseInfo
									.getPointerInfo().getLocation().y);
							deleteD.setVisible(true);
						}
					});
				}
			}
		});

		return p;
	}

	/**
	 * Erzeugt ein Panel auf dem man einen Bedarf editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param j
	 *            zu editierender Jahrgang
	 * @return HinzufuegenPanel
	 */
	private JPanel createEditPanel(final JPanel p, final Jahrgang j) {
		c = new GridBagConstraints();
		bedField2 = new JTextField(3);
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
		Entry<String, Integer> ent = j.getStundenbedarf().entrySet().iterator()
				.next();
		final String stdi = ent.getKey();
		int stdb = ent.getValue();

		p.add(new Label("Stundeninhalt: " + stdi), c);
		c.gridy = 2;
		p.add(new Label("Bedarf (Std):"), c);
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
				if (!check(p))
					return;
				HashMap<String, Integer> hm = new HashMap<String, Integer>();
				hm.put(stdi, Integer.parseInt(bedField2.getText()));
				j.setStundenbedarf(hm);
				JahrgangsManager.editBedarfFromJahrgang(j);

				updateList();
				((JFrame) SwingUtilities.getWindowAncestor(p)).dispose();
			}
		});

		c.gridx = 1;
		p.add(button3, c);
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
		if (textFieldsEmpty(p)) {
			new TextException();
			return false;
		}
		if (cb1 != null && cb1.getSelectedItem() == null) {
			new StundeninhaltException();
			return false;
		}
		if (cb2 != null && cb2.getSelectedItem() == null) {
			new StundeninhaltException();
			return false;
		}
		try {
			for (Component c : p.getComponents()) {
				if (c == bedField) {
					Integer.parseInt(bedField.getText());
					if (Integer.parseInt(bedField.getText()) < 1) {
						new ZahlException();
						return false;
					}
				}
				if (c == bedField2) {
					Integer.parseInt(bedField2.getText());
					if (Integer.parseInt(bedField2.getText()) < 1) {
						new ZahlException();
						return false;
					}
				}
			}
		} catch (NumberFormatException e) {
			new ZahlException();
			return false;
		}
		return true;
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

	/**
	 * leert die Liste des Panels und fuellt sie anschließend wieder mit allen
	 * Daten der Datenbank
	 */
	public static void updateList() {
		listModel.clear();
		for (Jahrgang ja : JahrgangsManager.getAllJahrgangFromDB()) {
			for (Entry<String, Integer> entry : ja.getStundenbedarf()
					.entrySet()) {
				listModel.addElement("Jahrgang: '" + ja.getJahrgang()
						+ "', Stundeninhalt: '" + entry.getKey()
						+ "', Bedarf: " + entry.getValue() + " Stunden");
			}
		}
	}
}
