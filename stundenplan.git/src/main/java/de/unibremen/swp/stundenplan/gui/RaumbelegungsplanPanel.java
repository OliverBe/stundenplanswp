package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.logic.RaumManager;

public class RaumbelegungsplanPanel extends JPanel implements ActionListener {

	private JList<String> liste;

	private JFileChooser chooser = new JFileChooser();
	private JFrame f;

	private static JTable table = null;

	private static JMenuBar menuBar = new JMenuBar();
	private static DefaultListModel rList;
	private static JList roomList;
	private static JLabel label1 = new JLabel("Raeume");

	private static JButton show = new JButton("Anzeigen");

	public JLabel warning = new JLabel();

	public RaumbelegungsplanPanel() {
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

		add(menuBar, c);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 4;
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);

	}

	public static void updateLists() {
		Room[] raumListe = new Room[RaumManager.getAllRoomsFromDB().size()];
		rList = new DefaultListModel();

		for (int i = 0; i < RaumManager.getAllRoomsFromDB().size(); i++) {
			raumListe[i] = RaumManager.getAllRoomsFromDB().get(i);

			rList.addElement(raumListe[i]);
		}

		roomList = new JList(rList);
		JScrollPane list1 = new JScrollPane(roomList);
		menuBar.removeAll();
		GridBagLayout gbl = new GridBagLayout();
		menuBar.setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill =GridBagConstraints.REMAINDER;
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.1;
		menuBar.add(label1, c);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 0.3;
		menuBar.add(list1, c);
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 0.2;
		menuBar.add(show,c);
		
		c.gridx = 0;
		c.gridy = 3;
		menuBar.add(new WarningPanel(), c);

	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == show) {
			if (!roomList.isSelectionEmpty()) {
				Room r = (Room) roomList.getSelectedValue();
				System.out.println(r.getName());
				table = new StundenplanTable().getTable();
				table.setModel(new TimetableModel(r));
				init();
				updatetable();

			}

		}
	}

	public void updatetable() {

		table.repaint();
		Stundenplan.getMain().validate();
	}

	public static JTable getTable() {
		return table;
	}

}
