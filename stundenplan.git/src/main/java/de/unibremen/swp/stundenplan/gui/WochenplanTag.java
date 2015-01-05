package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.unibremen.swp.stundenplan.*;
import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;

//Diese Klasse reprï¿½sentiert einen Plan und einem bestimmten Tag im Wochenplan.
public class WochenplanTag extends JPanel {

	Weekday day;
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	public JTable table;
	public JLabel warning = new JLabel();
	public DefaultTableModel model = new DefaultTableModel();
	public Personal p1 = new Personal("Testperson 1", "TP1", 35, 35, 35, false,
			true, null);
	public Personal p2 = new Personal("Testperson 2", "t2", 38, 38, 38, false,
			true, null);
	public Personal p3 = new Personal("Testperson 3", "t3", 38, 38, 38, false,
			true, null);
	public Personal p4 = new Personal("Testperson 4", "t4", 38, 38, 38, false,
			true, null);

	public Planungseinheit e1 = new Planungseinheit();
	public Planungseinheit e2 = new Planungseinheit();
	public Room r1 = new Room("MZH 1100", 1);
	public Schoolclass s1 = new Schoolclass("K1", 5, r1);
	public Stundeninhalt fach = new Stundeninhalt("Mathe", "Ma", 90, 0);

	public List<Planungseinheit> einheitsliste = new ArrayList<>();

	public WochenplanTag(final Weekday pDay) {
		day = pDay;
		init();
		setTestPlanungs();
		addData();
		generateTime();
	}

	public void init() {
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
		for (int i = (Config.DAY_STARTTIME_HOUR * 100); i <= (Config.DAY_ENDTIME_HOUR * 100); i += 10) {

			String text = "" + i;
			String c1 = "" + text.charAt(text.length() - 2);
			int zahl = Integer.parseInt(c1);
			if (zahl == 5) {
				i += 40;
			}
			String text2 = text.substring(0, text.length() / 2);
			String text3 = text.substring((text.length() - 2), text.length());

			String ausgabe = text2 + ":" + text3;
			model.addColumn(ausgabe);

		}

		table.getTableHeader().setReorderingAllowed(false);
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

	}

	/**
	 * Diese Methode füllt die Reihen der ersten Spalte der Tabelle mit den Vor und Nachnamen 
	 * des gesamten Personals.
	 */
	public void addData() {
		try {
			for (Personal e : DataPersonal.getAllPersonal()) {

				String nachname = e.getName();
				model.addRow(new Object[] { nachname });

			}
		} catch (Exception e) {
			System.out.println("Kein Personal in der Datenbank vorhanden");
		}

	}

	public void generateTime() {
		for (Planungseinheit p : einheitsliste) {
			List<Personal> ppliste = new ArrayList<>();
			ppliste = p.getPersonal();
			int starthour;
			int startminute;
			int endminute;
			int endhour;
			for (int i = 0; i < model.getRowCount(); i++) {
				String tablePersoName = (String) model.getValueAt(i, 0);
				String personalName = ppliste.get(0).getName();
				if (tablePersoName.equals(personalName)
						&& p.getWeekday().getOrdinal() == day.getOrdinal()) {

					starthour = p.getStartHour();
					startminute = p.getStartminute();
					endminute = p.getEndminute();
					endhour = p.getEndhour();
					String ausgabe = "<html><body><center>"
							+ p.getStundeninhalte().get(0) + " "
							+ p.getSchoolclasses().get(0) + "<br>"
							+ p.getRooms().get(0) + "<br>" + starthour + ":"
							+ startminute + "-" + endhour + ":" + endminute
							+ "</center></body></html>";

					for (int j = 1; j < model.getColumnCount(); j++) {
						String zeit = model.getColumnName(j);
						String[] zeiten = zeit.split(":");
						int stunde = Integer.parseInt(zeiten[0]);
						int minute = Integer.parseInt(zeiten[1]);
						if (starthour <= stunde && minute >= startminute) {
							//Endzeit noch implementieren.
							model.setValueAt(ausgabe, i, j);
						}
					}

				}

			}

		}
	}

	public void deleteAllRows() {

		for (int i = 0; i < model.getRowCount(); i++) {
			model.removeRow(i);
		}
	}

	public void refresh() {
		deleteAllRows();
		addData();
	}

	public void setTestPlanungs() {
		e1.addPersonal(DataPersonal.getPersonalByKuerzel("TP1"), new int[] { 1,
				2, 3 });
		e1.addPersonal(p2, new int[] { 1, 2, 3 });
		e1.setStarthour(8);
		e1.setEndhour(10);
		e1.setWeekday(Weekday.MONDAY);
		e1.addRoom(r1);
		e1.addSchulklassen(s1);
		e1.addStundeninhalt(fach);
		einheitsliste.add(e1);
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
