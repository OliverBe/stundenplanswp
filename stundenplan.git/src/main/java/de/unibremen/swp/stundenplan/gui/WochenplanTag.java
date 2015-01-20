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
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;

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
	public Room r2 = new Room("SGF 0140", 2);
//	public Schoolclass s1 = new Schoolclass("K1", 5, r1);
	public Stundeninhalt fach = new Stundeninhalt("Mathe", "Ma", 90, 0);

	public List<Planungseinheit> einheitsliste = new ArrayList<>();

	public WochenplanTag(final Weekday pDay) {
		day = pDay;
		init();
	//	setTestPlanungs();
		addData();
		calculateTime();
	}

	/**
	 * Erstellt das Layout und die Tabelle des Wochenplanes.
	 */
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
	 * Diese Methode füllt die Reihen der ersten Spalte der Tabelle mit den Vor
	 * und Nachnamen des gesamten Personals.
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

	/**
	 * Diese Methode durchläuft alle Planungseinheiten und versucht diese den
	 * entsprechenden Lehrern zuzuordnen, mit Infos des Faches, der Klasse und
	 * des Raumes an der besagten Zeit der Planungseinheit.
	 */
	public void calculateTime() {
	
		if(DataPlanungseinheit.getAllPlanungseinheit().size()<=0){
			System.out.println("Keine Planungseinheiten zum Verplanen vorhanden");
			return;
		}
		for (Planungseinheit p : DataPlanungseinheit.getAllPlanungseinheit() ) {
			List<Personal> ppliste = new ArrayList<>();
			ppliste = p.getPersonal();
			int starthour = p.getStartHour();
			int startminute = p.getStartminute();
			int endminute = p.getEndminute();
			int endhour = p.getEndhour();
			StringBuilder raeume = new StringBuilder();

			for (int i = 0; i < model.getRowCount(); i++) {
				String tablePersoName = (String) model.getValueAt(i, 0);
				String personalName ="";
				if(ppliste.get(0)!=null){
				for(int k = 0; k < ppliste.size(); k++){
					 personalName = ppliste.get(k).getName();
				if (tablePersoName.equals(personalName)
						&& p.getWeekday().getOrdinal() == day.getOrdinal()) {

					String ausgabe = createOutput(p);

					for (int j = 1; j < model.getColumnCount(); j++) {
						String zeit = model.getColumnName(j);
						String[] zeiten = zeit.split(":");
						int stunde = Integer.parseInt(zeiten[0]);
						int minute = Integer.parseInt(zeiten[1]);
						if (stunde >= starthour && minute >= startminute) {
							if (ausgabe.length() >= 70) {
								TableColumn spalte = table.getColumnModel()
										.getColumn(j);
								spalte.setPreferredWidth(ausgabe.length() + 30);
							}
							if (stunde >= endhour && minute >= endminute) {
								System.out.println("Ausgabe läenge"
										+ ausgabe.length());

								model.setValueAt(ausgabe, i, j);

								return;

							} else {
								model.setValueAt(ausgabe, i, j);
							}

						}
					}

				}

			}
		}
	}

		}
	}

	/**
	 * Generiert den Output eine Zelle im Wochenplaner
	 * 
	 * @param pEinheit
	 *            Die entsprechende Planungseinheit
	 * @return Ein String mit Infos des Faches/Fächer, der Klasse/Klassen und
	 *         anschließend des Raumes/der Raeume
	 */
	public String createOutput(Planungseinheit pEinheit) {
		final String outputAnfang = "<html><body><center>";
		final String outputEnde = "</center></body></html>";
		final String br = "<br>";
		String raeume = holeRaume(pEinheit);
		String stundenInhalte = holeStundeninhalte(pEinheit);
		String schulklassen = holeSchulklassen(pEinheit);
		String completeOutput = outputAnfang + schulklassen + br
				+ stundenInhalte + br + raeume + outputEnde;
		return completeOutput;

	}

	/**
	 * Durchquert alle Raume in der entsprechenden Planungseinheit und gibt
	 * diese aus.
	 * 
	 * @param pEinheit
	 *            Die entsprechende Planungseinheit
	 * @return Ein String mit allen Raeumen in der Planungseinheit
	 */
	public String holeRaume(Planungseinheit pEinheit) {
		StringBuilder raeumeOutput = new StringBuilder();
		for (int i = 0; i < pEinheit.getRooms().size(); i++) {
			if (raeumeOutput.length() > 0) {
				raeumeOutput.append(" / ");

			}
			raeumeOutput.append(pEinheit.getRooms().get(i));
		}
		return raeumeOutput.toString();
	}

	/**
	 * Durchquert alle Stundeninhalte in der entsprechenden Planungseinheit und
	 * gibt diese aus.
	 * 
	 * @param pEinheit
	 * 
	 * @return
	 */
	public String holeStundeninhalte(Planungseinheit pEinheit) {
		StringBuilder stundeninhaltOutput = new StringBuilder();
		for (int i = 0; i < pEinheit.getStundeninhalte().size(); i++) {
			if (stundeninhaltOutput.length() > 0) {
				stundeninhaltOutput.append(" / ");

			}
			stundeninhaltOutput.append(pEinheit.getStundeninhalte().get(i));
		}
		return stundeninhaltOutput.toString();

	}

	/**
	 * Durchquert alle Schulklassen in der entsprechenden Planungseinheit und
	 * gibt diese aus.
	 * 
	 * @param pEinheit
	 *            Die entsprechende Planungseinheit
	 * @return
	 */
	public String holeSchulklassen(Planungseinheit pEinheit) {
		StringBuilder schulklassenOutput = new StringBuilder();
		for (int i = 0; i < pEinheit.getSchoolclasses().size(); i++) {
			if (schulklassenOutput.length() > 0) {
				schulklassenOutput.append("/ ");
			}
			schulklassenOutput.append(pEinheit.getSchoolclasses().get(i));
		}
		return schulklassenOutput.toString();
	}

	/**
	 * Löscht das gesamte Personal aus der Tabelle.
	 */
	public void deleteAllPersonal() {

		for (int i = 0; i <= model.getRowCount(); i++) {
			System.out.println("Länge des TableModelLänge: "+model.getRowCount());
			model.removeRow(i);
		}
	}

	/**
	 * Löscht einmal das gesamte Personal fügt dann wieder das gesamte Personal
	 * ein.
	 */
	public void refresh() {
		deleteAllPersonal();
		
		
	}

	/**
	 * Erstellt hier lokale Testdaten um die Funktionalität des Wochenplans zu
	 * testen.
	 *
	 */
	public void setTestPlanungs() {
		e1.addPersonal(DataPersonal.getPersonalByKuerzel("TP1"), new int[] { 1,
				2, 3 });
		e1.setStarthour(8);
		e1.setEndhour(10);
		e1.setWeekday(Weekday.MONDAY);
		e1.addRoom(r1);
		e1.addRoom(r2);
	//	e1.addSchulklassen(s1);
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
