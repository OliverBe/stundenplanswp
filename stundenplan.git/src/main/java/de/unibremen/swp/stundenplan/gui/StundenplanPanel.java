package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;

public class StundenplanPanel extends JPanel implements ActionListener,
		MouseListener {

	/**
	 * gibt den optischen Punkt der x-Achse an an welchem ein Event ausgeführt
	 * wurde. Nicht jedes Event
	 */
	private int eventX;
	/**
	 * gibt den optischen Punkt der y-Achse an an welchem ein Event ausgeführt
	 * wurde. Nicht jedes Event
	 */
	private int eventY;

	/**
	 * gibt den Punkt der x-Achse an an welchem ein Event ausgeführt wurde.
	 * Nicht jedes Event
	 */
	private int eventXX;

	/**
	 * gibt den Punkt der y-Achse an an welchem ein Event ausgeführt wurde.
	 * Nicht jedes Event
	 */
	private int eventYY;

	/**
	 * repräsentiert die Tabelle welche in dem Frame als Stundenplan angezeigt
	 * wird
	 */
	private static JTable table;

	/**
	 * die MenuBar ist die MenüLeiste im Pane welche die lehrer und Schulklassen
	 * listen enthalten
	 */
	private static JMenuBar menuBar = new JMenuBar();

	/**
	 * ist das Model der Liste für das Personal
	 */
	private static DefaultListModel pList;

	/**
	 * ist das Model der Liste für die Schulklassen
	 */
	private static DefaultListModel sList;

	/**
	 * ist die Liste, in dem das gesammte Personal erfasst wird
	 */
	private static JList personalList;

	/**
	 * ist die Liste in der alle Klassen erfasst werden
	 */
	private static JList schoolclassList;

	/**
	 * Label für die Menübar
	 */
	private static JLabel label1 = new JLabel("Lehrer");

	/**
	 * Label für die Menübar
	 */
	private static JLabel label2 = new JLabel("Klassen");

	/**
	 * dieses MouseAdapter erzeugt bei rechtsklick auf die Tabelle ein popup
	 * Menü welches das Planungseinheiten hinzufügen, oder bearbeiten kann
	 */
	private MouseAdapter mousefunc = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			if (evt.getSource() == table) {
				eventX = evt.getXOnScreen();
				eventY = evt.getYOnScreen();
				eventXX = evt.getX();
				eventYY = evt.getY();

				if (SwingUtilities.isLeftMouseButton(evt)) {

					popmen.setVisible(false);
				}

				if (SwingUtilities.isRightMouseButton(evt)) {

					final int row = table.rowAtPoint(evt.getPoint());
					final int col = table.columnAtPoint(evt.getPoint());
					Timeslot t = null;
					if (table.getValueAt(row, col) instanceof Timeslot) {
						t = (Timeslot) table.getValueAt(row, col);
					}
					if (t != null)
						;
					createPopup(t.getpe());
				}
			}
		}
	};

	/**
	 * repräsentiert den Button, welche beim drücken den ausgewählten
	 * Stundenplan anzeigt
	 */
	private static JButton show = new JButton("Anzeigen");

	/**
	 * WarningLabel
	 */
	public JLabel warning = new JLabel();

	/**
	 * ist das Popup Menü welches erscheint, wenn in dem Stundenplan rechtsklick
	 * ausgeführt wird
	 */
	JPopupMenu popmen = new JPopupMenu();

	/**
	 * der Konstrukter vom StundenplanPanel. die ActionListener werden den
	 * GUI-Elementen zugeordnet und die Tablle wird auf null gesetzt, sodass zu
	 * Beginn keine Tabelle ersichtlich ist.
	 */
	public StundenplanPanel() {
		table = null;
		init();
		show.addActionListener(this);
		personalList.addMouseListener(this);
		schoolclassList.addMouseListener(this);
	}

	/**
	 * wird beim Initialisieren dieses Panes ausgeführt und intialisiert die
	 * GUI-Elemente deses Panes.
	 */
	public void init() {
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;

		updateLists();

		add(menuBar, c);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 4;
		c.gridy = 0;
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
		if (table != null) {
			table.addMouseListener(mousefunc);
		}
	}

	/**
	 * Erzeugt ein Popup zum hinzufï¿½gen, editieren und lï¿½schen von Lehrern
	 * Fï¿½chern und Klassen
	 * 
	 * @param row
	 *            Die Zeile.
	 * @param col
	 *            Die Spalte.
	 * @return das neue Popup-Menu
	 */
	protected JPopupMenu createPopup(final int peid) {

		if (popmen.isVisible()) {
			popmen.setVisible(false);
		}
		popmen = new JPopupMenu();
		final JMenuItem menu1 = new JMenuItem("Neue Planungseinheit");
		final JMenuItem menu2 = new JMenuItem("Planungseinheit entfernen");

		menu1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
				PEedit frame = getpFrame();
				frame.setVisible(true);
			}
		});
		menu2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
				PlanungseinheitManager.deletePlanungseinheitFromDB(peid);
				updatetable();
			}
		});
		popmen.add(menu1);
		if (peid != -1) {
			popmen.add(menu2);
		}
		popmen.setLocation(eventX, eventY);
		popmen.setVisible(true);
		return popmen;

	}

	private PEedit getpFrame() {
		return new PEedit(this);
	}

	/**
	 * updated die GUI und die Listen. wird ausgeführt sobald eine relevante
	 * Änderung in der Datenbank geschieht. Wird von anderen Klassen statisch
	 * ausgerufen.
	 */
	public static void updateLists() {
		Personal[] personalListe = new Personal[PersonalManager
				.getAllPersonalFromDB().size()];
		Schoolclass[] schoolclassListe = new Schoolclass[SchulklassenManager
				.getAllSchulklassenFromDB().size()];
		pList = new DefaultListModel();
		sList = new DefaultListModel();

		for (int i = 0; i < PersonalManager.getAllPersonalFromDB().size(); i++) {
			personalListe[i] = PersonalManager.getAllPersonalFromDB().get(i);

			pList.addElement(personalListe[i]);
		}

		for (int i = 0; i < SchulklassenManager.getAllSchulklassenFromDB()
				.size(); i++) {
			schoolclassListe[i] = SchulklassenManager
					.getAllSchulklassenFromDB().get(i);

			sList.addElement(schoolclassListe[i]);
		}

		personalList = new JList(pList);
		schoolclassList = new JList(sList);

		JScrollPane paneList1 = new JScrollPane(personalList);
		JScrollPane paneList2 = new JScrollPane(schoolclassList);

		menuBar.removeAll();
		GridBagLayout gbl = new GridBagLayout();
		menuBar.setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.REMAINDER;

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.1;
		gbl.setConstraints(label1, c);
		menuBar.add(label1, c);
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.3;
		c.weightx = 1.0;
		gbl.setConstraints(paneList1, c);
		menuBar.add(paneList1, c);
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 0.1;
		gbl.setConstraints(label2, c);
		menuBar.add(label2, c);
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 0.3;
		c.weightx = 1.0;
		gbl.setConstraints(paneList2, c);
		menuBar.add(paneList2, c);
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 1.0;
		c.weighty = 0.2;
		gbl.setConstraints(show, c);
		menuBar.add(show, c);

	}

	/**
	 * stellt eine Aktion da. Sobald der Anzeigen Button gedrückt wird, wird von
	 * dem element in der JList der Stundenplan in Form eines JTables angezeigt
	 * Danach wird die GUI aktualisiert
	 * 
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == show && (!personalList.isSelectionEmpty())) {

			Personal p = (Personal) personalList.getSelectedValue();
			System.out.println(p.getKuerzel());
			table = new StundenplanTable(p).getTable();

		} else if (ae.getSource() == show
				&& (!schoolclassList.isSelectionEmpty())) {
			Schoolclass s = (Schoolclass) schoolclassList.getSelectedValue();
			System.out.println(s.getName());
			table = new StundenplanTable(s).getTable();

		}

		init();
		updatetable();
	}

	/**
	 * gibt den aktuellen Stundenplan (die Tabelle) zurück
	 * 
	 * @return
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * 
	 */
	public void updatetable() {

		table.repaint();
		Stundenplan.getMain().validate();
		personalList.addMouseListener(this);
		schoolclassList.addMouseListener(this);
	}

	/**
	 * Sobald ein Element in den J-Listen ausgewählt
	 * wird, wird in der anderen JListe das ausgewählte Element "deselected".
	 * @param evt
	 */
	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getSource() == personalList) {
			schoolclassList.clearSelection();
		} else if (evt.getSource() == schoolclassList) {
			personalList.clearSelection();
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
