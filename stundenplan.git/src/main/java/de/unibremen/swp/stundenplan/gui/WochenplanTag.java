package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;

import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

//Diese Klasse repraesentiert einen Plan und einem bestimmten Tag im Wochenplan.
public class WochenplanTag extends JPanel {

	Weekday day;
	public static JTable table;
	public JLabel warning = new JLabel();
	public DefaultTableModel model = new DefaultTableModel();

	/**
	 * Konstruktor der Klasse WochenplanTag
	 * @param pDay
	 * 			Der Name des Tages.
	 */
	public WochenplanTag(final Weekday pDay) {
		day = pDay;
		init();
		addData();
		calculateTime();
	}

	/**
	 * Erstellt das Layout und die Tabelle des Wochenplanes.
	 */
	public void init() {
		setLayout(new FlowLayout(FlowLayout.LEFT));

		table = new JTable(model);
		model.addColumn("Personal");
		for (int i = 0; i < TimetableManager.daytablelength(); i++) {

			model.addColumn(TimetableManager.getTimeframeDisplay(i));

		}

		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setRowHeight(50);

		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(100);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane pane = new JScrollPane(table);

		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setPreferredSize(new Dimension(1400, 700));
		add(pane);

	}

	/**
	 * Diese Methode faellt die Reihen der ersten Spalte der Tabelle mit den Vor
	 * und Nachnamen des gesamten Personals. Vorher wird der Inhalt der PersonalDatenBank lokal Alphabetisch sortiert.
	 */
	public void addData() {
		try {
			ArrayList<Personal> pliste = DataPersonal.getAllPersonal();
			ArrayList<String> ppliste = new ArrayList<>();
			for (int i = 0; i < pliste.size(); i++) {
				ppliste.add(pliste.get(i).getKuerzel());

			}

			Collections.sort(ppliste);
			pliste.clear();
			for (int j = 0; j < ppliste.size(); j++) {
				pliste.add(DataPersonal.getPersonalByKuerzel(ppliste.get(j)));
			}

			for (Personal e : pliste) {

				String nachname = e.getName();
				model.addRow(new Object[] { nachname });
			}

		} catch (Exception e) {
			System.out.println("Kein Personal in der Datenbank vorhanden");
		}
	}

	/**
	 * Diese Methode durchlaeuft alle Planungseinheiten und versucht diese den
	 * entsprechenden Lehrern zuzuordnen, mit Infos des Faches, der Klasse und
	 * des Raumes an der besagten Zeit der Planungseinheit.
	 */
	public void calculateTime() {
		if (DataPlanungseinheit.getAllPlanungseinheit().size() <= 0) {
			System.out
					.println("Keine Planungseinheiten zum Verplanen vorhanden");

			return;
		}
		for (Planungseinheit p : DataPlanungseinheit.getAllPlanungseinheit()) {
			List<Personal> ppliste = new ArrayList<>();
			ppliste = p.getPersonal();

			for (int i = 0; i < model.getRowCount(); i++) {
				String tablePersoName = (String) model.getValueAt(i, 0);
				String personalName = "";
				if (ppliste.size() == 0 || ppliste == null) {
					break;
				}
				for (int k = 0; k < ppliste.size(); k++) {
					personalName = ppliste.get(k).getName();
					int[] zeite = p.getTimesofPersonal(ppliste.get(k));
					int starthour = zeite[0];
					int startminute = zeite[1];

					int endhour = zeite[2];
					int endminute = zeite[3];

					if (tablePersoName.equals(personalName)
							&& p.getWeekday().getOrdinal() == day.getOrdinal()) {

						String ausgabe = createOutput(p);

						for (int j = 1; j < model.getColumnCount(); j++) {
							String zeit = model.getColumnName(j);
							String[] zeiten = zeit.split(" - ");
							String[] ersteZeit = zeiten[0].split(":");
							String[] zweiteZeit = zeiten[1].split(":");
							int ersteZeitTabStunde = Integer
									.parseInt(ersteZeit[0]);
							int ersteZeitTabMinute = Integer
									.parseInt(ersteZeit[1]);
							int zweiteZeitTabStunde = Integer
									.parseInt(zweiteZeit[0]);
							int zweiteZeitTabMinute = Integer
									.parseInt(zweiteZeit[1]);

							if (ersteZeitTabStunde >= starthour
									&& ersteZeitTabMinute >= startminute
									|| ersteZeitTabStunde > starthour
									&& ersteZeitTabMinute <= startminute) {

								if (ausgabe.length() >= 70) {
									TableColumn spalte = table.getColumnModel()
											.getColumn(j);
									spalte.setPreferredWidth(ausgabe.length() + 30);

								}
								if (zweiteZeitTabStunde == endhour
										&& zweiteZeitTabMinute >= endminute) {

									model.setValueAt(ausgabe, i, j);
									break;

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

	/**
	 * Generiert den Output eine Zelle im Wochenplaner
	 * 
	 * @param pEinheit
	 *            Die entsprechende Planungseinheit
	 * @return Ein String mit Infos des Faches/Faecher, der Klasse/Klassen und
	 *         anschliessend des Raumes/der Raeume
	 */
	public String createOutput(Planungseinheit pEinheit) {
		final String outputAnfang = "<html><body><center>";
		final String outputEnde = "</center></body></html>";
		final String br = "<br>";
		String raeume = holeRaume(pEinheit);
		String stundenInhalte = holeStundeninhalte(pEinheit);
		
		String schulklassen = holeSchulklassen(pEinheit);
		if(schulklassen.equals("")&& stundenInhalte.equals("")){
			stundenInhalte = ("Team Zeit");
			
		}
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
	 * Loescht das gesamte Personal aus der Tabelle.
	 */
	public void deleteAllPersonal() {

		for (int i = 0; i <= model.getRowCount(); i++) {
			System.out.println("Laenge des TableModelLaenge: "
					+ model.getRowCount());
			model.removeRow(i);
		}
	}

	/**
	 * Loescht einmal das gesamte Personal fuegt dann wieder das gesamte Personal
	 * ein.
	 */
	public void refresh() {
		deleteAllPersonal();

	}

	public static JTable getTable() {
		return table;
	}

}
