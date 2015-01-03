package de.unibremen.swp.stundenplan.gui;

import de.unibremen.swp.stundenplan.*;
import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.db.Data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

//Diese Klasse repr�sentiert einen Plan und einem bestimmten Tag im Wochenplan.
public class WochenplanTag extends JPanel {

	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	String[] testeingaben = { "Name 1", "Name 2", "Name 3", "Name 4", "Name 5" };
	String[][] rowData = { { "", "", "", "", "", "AA", "BB", "", "", "", "" } };
	public JTable table;
	public JLabel warning = new JLabel();
	public DefaultTableModel model = new DefaultTableModel();
	List<Personal> personalliste = new ArrayList<>();
	public Personal p1 = new Personal("Testperson 1", "t1", 38, 38, 38, false,
			true, null);
	public Personal p2 = new Personal("Testperson 2", "t2", 38, 38, 38, false,
			true, null);
	public Personal p3 = new Personal("Testperson 3", "t3", 38, 38, 38, false,
			true, null);
	public Personal p4 = new Personal("Testperson 4", "t4", 38, 38, 38, false,
			true, null);

	public WochenplanTag() {
		init();
		addData();
	}

	public void init() {
		personalliste.add(p1);
		personalliste.add(p2);
		personalliste.add(p3);
		personalliste.add(p4);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;

		table = new JTable(model);

		model.addColumn("Personal");
		model.addColumn(Config.DAY_STARTTIME_HOUR + ":00");
		for (int i = (Config.DAY_STARTTIME_HOUR * 100); i <= (Config.DAY_ENDTIME_HOUR * 100); i += Config.TIMESLOT_LENGTH) {
			if (i == 60) {
				i = 0;
			}

			String text = "" + i;
			String text2 = text.substring(0, text.length() / 2);
			String text3 = text.substring((text.length() - 2), text.length());

			String ausgabe = text2 + ":" + text3;
			model.addColumn(ausgabe);

		}
		table.setRowSelectionAllowed(true);
		table.setRowHeight(50);
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane pane = new JScrollPane(table);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 4;
		c.gridy = 0;
		c.gridx = 1;

		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(1400, 700));
		add(pane, c);
		JButton pdf = new JButton("PDF");
		JButton csv = new JButton("CSV");
		JButton text = new JButton("Text");

		c.fill = GridBagConstraints.LAST_LINE_END;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Exportieren als:"), c);
		c.gridx = 2;
		add(pdf, c);
		c.gridx = 3;
		add(csv, c);
		c.gridx = 4;
		add(text, c);

		buttonOkay(pdf);
		buttonOkay(csv);
		buttonOkay(text);

		warning.setText("Warnungsfeld: Keine Probleme");
		warning.setBackground(Color.GREEN);
		warning.setOpaque(true);
		c.gridy = 3;
		c.gridx = 1;
		c.gridwidth = 4;
		add(warning, c);

	}

	public void addData() {
		for (Personal e : personalliste) {

			String nachname = e.getName();
			model.addRow(new Object[] { nachname });
		}
	}

	private void buttonOkay(JButton b) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showSaveDialog(f);
			}
		});
	}

}
