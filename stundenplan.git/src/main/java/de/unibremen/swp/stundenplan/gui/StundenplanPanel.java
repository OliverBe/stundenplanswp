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

	private static JButton show = new JButton("Anzeigen");

	public JLabel warning = new JLabel();

	JPopupMenu popmen = new JPopupMenu();

	public StundenplanPanel() {
		init();
		show.addActionListener(this);
	}

	public void init() {

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

		table = new StundenplanTable().getTable();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 4;
		c.gridy = 0;
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
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
					createPopup(row, col);
				}
			}
		});

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
	protected JPopupMenu createPopup(final int row, final int col) {

		if (popmen.isVisible()) {
			popmen.setVisible(false);
		}
		popmen = new JPopupMenu();
		final JMenuItem menu1 = new JMenuItem("Neue Planungseinheit");
		final JMenuItem menu2 = new JMenuItem("Planungseinheit bearbeiten");

		menu1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
			}
		});
		menu2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
			}
		});
		popmen.add(menu1);
		popmen.add(menu2);

		popmen.setLocation(eventX, eventY);
		popmen.setVisible(true);
		return popmen;

	}

	public static void updateLists() {
		Personal[] personalListe = new Personal[PersonalManager.getAllPersonalFromDB().size()];
		Schoolclass[] schoolclassListe = new Schoolclass[DataSchulklasse
				.getAllSchulklasse().size()];
		pList = new DefaultListModel();
		sList = new DefaultListModel();

		for (int i = 0; i < DataPersonal.getAllPersonal().size(); i++) {
			personalListe[i] = DataPersonal.getAllPersonal().get(i);

			pList.addElement(personalListe[i]);
		}

		for (int i = 0; i < DataSchulklasse.getAllSchulklasse().size(); i++) {
			schoolclassListe[i] = DataSchulklasse.getAllSchulklasse().get(i);

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
		if (ae.getSource() == show) {
			if (!personalList.isSelectionEmpty()) {
				Personal p = (Personal) personalList.getSelectedValue();
				System.out.println(p.getKuerzel());
				table.setModel(new TimetableModel(p));

			} else if (!schoolclassList.isSelectionEmpty()) {
				Schoolclass s = (Schoolclass) schoolclassList
						.getSelectedValue();
				System.out.println(s.getName());
				table.setModel(new TimetableModel(s));

			}

		}

	}
}
