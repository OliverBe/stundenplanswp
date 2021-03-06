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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.exceptions.TextException;
import de.unibremen.swp.stundenplan.logic.RaumManager;

/**
 * Repraesentiert das Panel zum Hinzufuegen, Bearbeiten, Loeschen und Anzeigen
 * von Raeumen
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class RoomPanel extends JPanel {

	/**
	 * Textfeld um den Namen des Raumes im addpanel anzugeben
	 */
	private JTextField nameField;

	/**
	 * Textfeld um den Namen des Raumes im editpanel anzugeben
	 */
	private JTextField nameField2;

	/**
	 * ComboBox fuer die Gebdaudeauswahl beim addpanel
	 */
	@SuppressWarnings("rawtypes")
	private JComboBox jcb;

	/**
	 * ComboBox fuer die Gebdaudeauswahl beim editpanel
	 */
	@SuppressWarnings({ "rawtypes" })
	private JComboBox jcb2;

	/**
	 * Gebaeudenummern fuer die comboboxen
	 */
	private Integer[] gebaeude = { 1, 2 };

	/**
	 * checklist fuer die Raumfunktionen beim addpanel
	 */
	private CheckBoxList checkList;

	/**
	 * checklist fuer die Raumfunktionen beim editpanel
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
	 * ListModel fuer Raumliste
	 */
	private static DefaultListModel<Room> listModel = new DefaultListModel<Room>();

	/**
	 * JList fuer Raeume
	 */
	private JList<Room> list = new JList<Room>(listModel);

	/**
	 * Scrollbar fuer Jlist von Raeumen
	 */
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * Konstruktor fuer Raumpanel
	 */
	public RoomPanel() {
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
	 * Erzeugt ein Panel auf dem man einen neuen Raum hinzufuegen kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return HinzuzufuegendesPanel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createAddPanel(final JPanel p) {
		c = new GridBagConstraints();
		nameField = new JTextField(5);
		JLabel lSp = new JLabel("Spezieller Raum:");
		JButton button = new JButton("Raum Hinzufuegen");
		checkList = new CheckBoxList();

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Neuen Raum hinzufuegen"));
		c.insets = new Insets(8, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name des Raumes:"), c);
		c.gridx = 1;
		p.add(nameField, c);
		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Gebaeude:"), c);
		c.gridx = 1;
		jcb = new JComboBox(gebaeude);
		p.add(jcb, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSp.setFont(new Font(nameField.getFont().getFontName(), Font.PLAIN,
				nameField.getFont().getSize()));
		p.add(lSp, c);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridy = 4;

		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Raumfunktion rf : RaumManager.getAllRaumfunktionFromDB()) {
			boxes.add(new JCheckBox(rf.getName()));
		}
		;
		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);

		c.gridy = 6;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!check(p))
					return;
				ArrayList<String> rf = new ArrayList<String>();
				for (int i = 0; i < checkList.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						rf.add(cb.getText());
				}

				RaumManager.addRaumToDB(new Room(nameField.getText(), (int) jcb
						.getSelectedItem(), rf));

				updateList();
			}
		});
		return p;
	}

	/**
	 * Erzeugt ein Panel auf dem man die Raumliste angezeigt bekommt.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @return Hinzufuegendes Panel
	 */
	private JPanel createListPanel(final JPanel p) {
		c = new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Existierende Raeume"));

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
						edit.add(createEditPanel(new JPanel(),
								list.getSelectedValue()));
						edit.setLocation((MouseInfo.getPointerInfo()
								.getLocation().x)-50,list.getParent().getY()+150);
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
	 * Erzeugt ein Panel auf dem man einen Raum editieren kann.
	 * 
	 * @param p
	 *            uebergebenes Panel
	 * @param ro
	 *            zu editierender Raum
	 * @return Hinzufuegendes Panel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createEditPanel(final JPanel p, final Room ro) {
		c = new GridBagConstraints();
		nameField2 = new JTextField(5);
		JLabel lSp = new JLabel("Spezieller Raum:");
		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");
		checkList2 = new CheckBoxList();

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Raum editieren"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(new Label("Name des Raumes:"), c);
		c.gridx = 1;
		p.add(nameField2, c);
		nameField2.setText(ro.getName());
		c.gridx = 0;
		c.gridy = 1;
		p.add(new Label("Gebaeude:"), c);
		c.gridx = 1;
		jcb2 = new JComboBox(gebaeude);
		p.add(jcb2, c);
		jcb2.setSelectedItem(ro.getGebaeude());
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL), c);

		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSp.setFont(new Font(nameField.getFont().getFontName(), Font.PLAIN,
				nameField.getFont().getSize()));
		p.add(lSp, c);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridy = 4;

		ArrayList<JCheckBox> boxes2 = new ArrayList<JCheckBox>();

		for (Raumfunktion rf : RaumManager.getAllRaumfunktionFromDB()) {
			boxes2.add(new JCheckBox(rf.getName()));
		}
		;

		for (JCheckBox jcb : boxes2) {
			for (String rf : ro.getMoeglicheFunktionen()) {
				if (jcb.getText().equals(rf))
					jcb.setSelected(true);
			}
			;
		}
		;
		checkList2.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		checkList2.setListData(boxes2.toArray());
		p.add(checkList2, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!check(p))
					return;
				ArrayList<String> rf2 = new ArrayList<String>();
				for (int i = 0; i < checkList2.getModel().getSize(); i++) {
					JCheckBox cb = (JCheckBox) checkList2.getModel()
							.getElementAt(i);
					if (cb.isSelected())
						rf2.add(cb.getText());
				}

				RaumManager.editRaum(
						ro.getName(),
						new Room(nameField2.getText(), (int) jcb2
								.getSelectedItem(), rf2));

				updateList();
				((JFrame) SwingUtilities.getWindowAncestor(p)).dispose();
			}
		});

		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
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
		boolean b=true;
		if (textFieldsEmpty(p)) {
			new TextException();
			b=false;
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
	 * leert die Liste des Panels und fuellt sie anschliessend wieder mit allen
	 * Daten der Datenbank
	 */
	public static void updateList() {
		listModel.clear();
		for (Room r : RaumManager.getAllRoomsFromDB()) {
			listModel.addElement(r);
		}
	}

}
