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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.exceptions.KuerzelException;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.exceptions.ZahlException;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

/**
 * Repräsentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * von Stundeninhalten
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class StundeninhaltPanel extends JPanel {

	/**
	 * Textfeld fuer das Kerzel des Stundeninhalts beim adden
	 */
	private JTextField kuerzField;

	/**
	 * Textfeld fuer das Kerzel des Stundeninhalts beim editen
	 */
	private JTextField kuerzField2;

	/**
	 * Textfeld fuer die Regeldauer des Stundeninhalts beim adden
	 */
	private JTextField dauerField;

	/**
	 * Textfeld fuer die Regeldauer des Stundeninhalts beim editen
	 */
	private JTextField dauerField2;
	
	/**
	 * GridBagConsraint fuer die add,edit,listpanel
	 */
	private GridBagConstraints c;
	
	/**
	 * GridBagConsraint fuer das gesamte Panel
	 */
	private GridBagConstraints c2;

	/**
	 * ListModel fuer die JList der Stundeninhalte
	 */
	private static DefaultListModel<Stundeninhalt> listModel = new DefaultListModel<Stundeninhalt>();
	
	/**
	 * JList der Stundeninhalte
	 */
	private JList<Stundeninhalt> list = new JList<Stundeninhalt>(listModel);
	
	/**
	 * Scroller fuer die JList der Stundeninhalte
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * Konstruktor des Stundeninhaltpanels
	 */
	public StundeninhaltPanel() {
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
	 * Erzeugt ein Panel auf dem man einen neuen Stundeninhalt hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzufuegenPanel
	 */
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		dauerField = new JTextField(5);
		kuerzField = new JTextField(5);
		JTextField titelField = new JTextField(15);
		JButton button = new JButton("Stundeninhalt hinzufuegen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Neuen Stundeninhalt hinzufuegen"));
		c.insets = new Insets(5, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Titel:"), c);
		c.gridx = 1;
		p.add(titelField, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Kuerzel:"), c);
		c.gridx = 1;
		p.add(kuerzField, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Regeldauer (Min):"), c);
		c.gridx = 1;
		p.add(dauerField, c);

		c.gridx = 0;
		c.gridy = 3;
		p.add(new Label("Rythmustyp:"), c);
		c.gridx = 1;
		final JRadioButton pauseB = new JRadioButton("Pause");
		pauseB.setSelected(true);
		final JRadioButton leichtB = new JRadioButton("Leicht");
		final JRadioButton schwerB = new JRadioButton("Schwer");
		ButtonGroup group = new ButtonGroup();
		group.add(pauseB);
		group.add(leichtB);
		group.add(schwerB);
		p.add(pauseB, c);
		c.gridy = 4;
		p.add(leichtB, c);
		c.gridy = 5;
		p.add(schwerB, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add Button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int rythm = 0;
				if (!check(p))
					return;
				if (pauseB.isSelected())
					rythm = 0;
				if (leichtB.isSelected())
					rythm = 1;
				if (schwerB.isSelected())
					rythm = 2;

				Stundeninhalt si = new Stundeninhalt(titelField.getText(),
						kuerzField.getText(), Integer.parseInt(dauerField
								.getText()), rythm);

				StundeninhaltManager.addStundeninhaltToDb(si);

				updateList();
			}
		});
		return p;
	}

	/**
	 * Erzeugt ein Panel auf dem man die Stundeninhaltliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzuzufuegendesPanel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Existierende Stundeninhalte"));

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
						JFrame edit = new JFrame("Bedarf editieren");
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
	 * Erzeugt ein Panel auf dem man einen Stundeninhalt editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param si
	 *            zu editierendes Element
	 * @return HinzuzufuegendesPanel
	 */
	private JPanel createEditPanel(final JPanel p, final Stundeninhalt si) {
		c = new GridBagConstraints();
		JTextField titelField2 = new JTextField(15);
		dauerField2 = new JTextField(5);
		kuerzField2 = new JTextField(5);
		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Stundeninhalt editieren"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Titel:"), c);
		c.gridx = 1;
		p.add(titelField2, c);
		titelField2.setText(si.getName());

		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Kuerzel:"), c);
		c.gridx = 1;
		p.add(kuerzField2, c);
		kuerzField2.setText(si.getKuerzel());

		c.gridx = 0;
		c.gridy = 2;
		p.add(new Label("Regeldauer (Min):"), c);
		c.gridx = 1;
		p.add(dauerField2, c);
		dauerField2.setText(si.getRegeldauer() + "");

		c.gridx = 0;
		c.gridy = 3;
		p.add(new Label("Rythmustyp:"), c);
		c.gridx = 1;
		final JRadioButton pauseB2 = new JRadioButton("Pause");
		final JRadioButton leichtB2 = new JRadioButton("Leicht");
		final JRadioButton schwerB2 = new JRadioButton("Schwer");
		switch (si.getRhythmustyp()) {
		case 0:
			pauseB2.setSelected(true);
			break;
		case 1:
			leichtB2.setSelected(true);
			break;
		case 2:
			schwerB2.setSelected(true);
			break;
		}
		ButtonGroup group2 = new ButtonGroup();
		group2.add(pauseB2);
		group2.add(leichtB2);
		group2.add(schwerB2);
		p.add(pauseB2, c);
		c.gridy = 4;
		p.add(leichtB2, c);
		c.gridy = 5;
		p.add(schwerB2, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					int rythm = 0;
					if (!check(p))
						return;
					if (pauseB2.isSelected())
						rythm = 0;
					if (leichtB2.isSelected())
						rythm = 1;
					if (schwerB2.isSelected())
						rythm = 2;
					Stundeninhalt si2 = new Stundeninhalt(titelField2.getText(),
							kuerzField2.getText(), Integer.parseInt(dauerField2
									.getText()), rythm);
					StundeninhaltManager.editStundeninhalt(si.getKuerzel(), si2);

					updateList();
					((JFrame)SwingUtilities
					.getWindowAncestor(p)).dispose();
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
		if (kuerzField != null
				&& kuerzField.getText().length() > Data.MAX_KUERZEL_LEN) {
			new KuerzelException();
			return false;
		}
		if (kuerzField2 != null
				&& kuerzField2.getText().length() > Data.MAX_KUERZEL_LEN) {
			new KuerzelException();
			return false;
		}
		try {
			for (Component c : p.getComponents()) {
				if (c == dauerField) {
					Integer.parseInt(dauerField.getText());
					if (Integer.parseInt(dauerField.getText()) < 1) {
						new ZahlException();
						return false;
					}
				}
				if (c == dauerField2) {
					Integer.parseInt(dauerField2.getText());
					if (Integer.parseInt(dauerField2.getText()) < 1) {
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
	 * leert die Liste des Panels und fuellt sie anschließend wieder mit allen Daten der Datenbank
	 */
	public static void updateList() {
		listModel.clear();
		for (Stundeninhalt sti : StundeninhaltManager.getAllStundeninhalteFromDB()) {
			listModel.addElement(sti);
		}
	}

}
