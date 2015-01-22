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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.StundeninhaltException;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.logic.RaumManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

/**
 * Repräsentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * von Raumfunktionen
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class RaumfunktionPanel extends JPanel {

	/**
	 * Textfeld fuer den Namen der Raumfunktionen beim addpanel
	 */
	private JTextField tf;

	/**
	 * Textfeld fuer den Namen der Raumfunktionen beim editpanel
	 */
	private JTextField tf2;

	/**
	 * checklist fuer die Stundeninhalte die in diesem Raum moeglich sind beim
	 * addpanel
	 */
	private CheckBoxList checkList;

	/**
	 * checklist fuer die Stundeninhalte die in diesem Raum moeglich sind beim
	 * editpanel
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
	 * ListModel fuer die JList der Raumfunktionen
	 */
	private static DefaultListModel<Raumfunktion> listModel = new DefaultListModel<Raumfunktion>();

	/**
	 * JList der Raumfunktionen
	 */
	private JList<Raumfunktion> list = new JList<Raumfunktion>(listModel);

	/**
	 * Scroller fuer die JList der Raumfunktionen
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * Konstruktor des Raumfunktionpanels
	 */
	public RaumfunktionPanel() {
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
	 * Erzeugt ein Panel auf dem man eine neue Raumfunktion hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzufuegenPanel
	 */
	@SuppressWarnings("unchecked")
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		JButton button = new JButton("Funktion hinzufuegen");
		tf = new JTextField(20);
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Funktionen von Raeumen "));
		c.insets = new Insets(5, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name der Funktion:"), c);
		c.gridx = 1;
		p.add(tf, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		p.add(new Label("Moegliche Stundeninhalte"), c);

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		c.gridheight = 2;
		checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Stundeninhalt s : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		}
		;
		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);

		c.gridy = 5;
		p.add(button, c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!check(p))
					return;
				ArrayList<String> stdi = new ArrayList<String>();
				for (int i = 0; i < checkList.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						stdi.add(cb.getText());
				}
				RaumManager.addRaumfunktionToDB(new Raumfunktion(tf.getText(),
						stdi));
				updateList();
			}
		});

		return p;
	}

	/**
	 * Erzeugt ein Panel auf dem man die Raumfunktionliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzuzufuegendesPanel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Existierende Raumfunktionen"));

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
						JFrame edit = new JFrame("Raumfunktion editieren");
						edit.add(createEditPanel(new JPanel(),
								list.getSelectedValue()));
						edit.pack();
						edit.setLocation(MouseInfo.getPointerInfo()
								.getLocation().x, MouseInfo.getPointerInfo()
								.getLocation().y);
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
	 * Erzeugt ein Panel auf dem man eine Raumfunktion editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param rf
	 *            zu editierendes Element
	 * @return HinzuzufuegendesPanel
	 */
	@SuppressWarnings("unchecked")
	private JPanel createEditPanel(final JPanel p, final Raumfunktion rf) {
		c = new GridBagConstraints();
		tf2 = new JTextField(20);
		JButton button2 = new JButton("Funktion editieren");
		JButton button3 = new JButton("Abbrechen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Funktionen von Raeumen "));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name der Funktion:"), c);
		c.gridx = 1;
		p.add(tf2, c);
		tf2.setText(rf.getName());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		p.add(new Label("Moegliche Stundeninhalte"), c);

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		checkList2 = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		for (Stundeninhalt s : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		}
		for (JCheckBox jcb : boxes) {
			for (String s : rf.getStundeninhalte()) {
				if (jcb.getText().equals(s))
					jcb.setSelected(true);
			}
		}
		checkList2.setListData(boxes.toArray());
		checkList2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList2, c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		p.add(button2, c);

		// edit
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!check(p))
					return;
				ArrayList<String> stdi = new ArrayList<String>();
				for (int i = 0; i < checkList2.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList2.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						stdi.add(cb.getText());
				}
				RaumManager.editRaumfunktion(rf.getName(),
						new Raumfunktion(tf2.getText(), stdi));
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
		if (textFieldsEmpty(p)) {
			new TextException();
			return false;
		}
		boolean b = true;
		if (checkList != null) {
			for (int i = 0; i < checkList.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
				if (cb.isSelected())
					b = false;
			}
		}
		if (checkList2 != null) {
			for (int i = 0; i < checkList2.getModel().getSize(); i++) {
				JCheckBox cb = (JCheckBox) checkList2.getModel()
						.getElementAt(i);
				if (cb.isSelected())
					b = false;
			}
		}
		if (b) {
			new StundeninhaltException();
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
		for (Raumfunktion r : RaumManager.getAllRaumfunktionFromDB()) {
			listModel.addElement(r);
		}
	}
}