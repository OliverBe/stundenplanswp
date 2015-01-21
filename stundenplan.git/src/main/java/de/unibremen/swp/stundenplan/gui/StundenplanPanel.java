package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;

public class StundenplanPanel extends JPanel implements ActionListener {

	public DataPanel data = new DataPanel();

	private JFrame f;
	private int eventX;
	private int eventY;

	private int eventXX;
	private int eventYY;

	private static JTable table;

	private static JMenuBar menuBar = new JMenuBar();
	private static DefaultListModel pList;
	private static DefaultListModel sList;
	private static JList personalList;
	private static JList schoolclassList;
	private static JLabel label1 = new JLabel("Lehrer");
	private static JLabel label2 = new JLabel("Klassen");
	private MouseAdapter mousefunc = new MouseAdapter() {
		public void mouseClicked(MouseEvent evt) {
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
	};

	private static JButton show = new JButton("Anzeigen");

	public JLabel warning = new JLabel();

	JPopupMenu popmen = new JPopupMenu();

	public StundenplanPanel() {
		table = null;
		init();
		show.addActionListener(this);
	}

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

		menuBar.setLayout(new GridLayout(0, 1));
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
	 * Erzeugt ein Popup zum hinzuf�gen, editieren und l�schen von Lehrern
	 * F�chern und Klassen
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

		menuBar.removeAll();
		menuBar.add(label1);
		menuBar.add(personalList);
		menuBar.add(label2);
		menuBar.add(schoolclassList);
		menuBar.add(show);

	}

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

	public JTable getTable() {
		return table;
	}

	public void updatetable() {
		table.repaint();
	}

}
