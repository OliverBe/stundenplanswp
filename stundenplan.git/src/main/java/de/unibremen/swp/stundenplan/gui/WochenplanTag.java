package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import de.unibremen.swp.stundenplan.logic.TimetableManager;

//Diese Klasse reprï¿½sentiert einen Plan und einem bestimmten Tag im Wochenplan.
public class WochenplanTag extends JPanel {

	Weekday day;
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	public static JTable table;
	public JLabel warning = new JLabel();
	public DefaultTableModel model = new DefaultTableModel();
	
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
		for(int i= 1; i<TimetableManager.daytablelength(); i++){
		
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
		pane.setPreferredSize(new Dimension(1400,700));
		add(pane);

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
		
			
			for (int i = 0; i < model.getRowCount(); i++) {
				String tablePersoName = (String) model.getValueAt(i, 0);
				String personalName ="";
				if(ppliste.get(0)!=null){
				for(int k = 0; k < ppliste.size(); k++){
					 personalName = ppliste.get(k).getName();
						System.out.println("personalName: "+personalName);
						int starthour = p.getStartHour();
						int startminute = p.getStartminute();
						int endminute = p.getEndminute();
						int endhour = p.getEndhour();
					 
					 
					 
					 if (tablePersoName.equals(personalName)
						&& p.getWeekday().getOrdinal() == day.getOrdinal()) {

					String ausgabe = createOutput(p);

					for (int j = 1; j < model.getColumnCount(); j++) {
						String zeit = model.getColumnName(j);
						String[] zeiten = zeit.split(" - ");
						String[] ersteZeit = zeiten[0].split(":");
						String[] zweiteZeit = zeiten[1].split(":");
						int ersteZeitTabStunde = Integer.parseInt(ersteZeit[0]);
						int ersteZeitTabMinute = Integer.parseInt(ersteZeit[1]);
						int zweiteZeitTabStunde = Integer.parseInt(zweiteZeit[0]);
						int zweiteZeitTabMinute = Integer.parseInt(zweiteZeit[1]);
						
						if(ersteZeitTabStunde >= starthour && ersteZeitTabMinute >= startminute|| ersteZeitTabStunde>starthour && ersteZeitTabMinute<=startminute) {
							
							if (ausgabe.length() >= 70) {
								TableColumn spalte = table.getColumnModel()
										.getColumn(j);
								spalte.setPreferredWidth(ausgabe.length() + 30);
							}
							if (zweiteZeitTabStunde == endhour && zweiteZeitTabMinute == endminute) {
								System.out.println("Ausgabe läenge "
										+ ausgabe.length());
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

	private void buttonOkay(JButton b) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showSaveDialog(f);
			}
		});
	}
	
	public static JTable getTable() {
		return table;
	}
}
