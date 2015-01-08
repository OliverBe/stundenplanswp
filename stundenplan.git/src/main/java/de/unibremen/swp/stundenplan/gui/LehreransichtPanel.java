package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class LehreransichtPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;

	private JFileChooser chooser = new JFileChooser();
	private JFrame f;

	GridBagConstraints c = new GridBagConstraints();

	public JLabel warning = new JLabel();

	public LehreransichtPanel() {
		init();
	}

	public void init() {

		setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;

		DefaultTableModel model = new DefaultTableModel();
		table = new JTable(model);
		model.addColumn("Kuerzel");
		model.addColumn("Wochenstunden");
		model.addColumn("Ersatzzeit");

		ArrayList<Stundeninhalt> si = DataStundeninhalt.getAllStundeninhalte();
		ArrayList<Planungseinheit> planungseinheiten = DataPlanungseinheit
				.getAllPlanungseinheit();
		ArrayList<Personal> allPersonal = PersonalManager
				.getAllPersonalFromDB();
		HashMap<String, HashMap<String, Integer>> inhaltKlasseStunden = new HashMap<>();

		for (Stundeninhalt s : si) {
			model.addColumn(s.getKuerzel());
			HashMap<String, Integer> hash = new HashMap<>();
			inhaltKlasseStunden.put(s.getKuerzel(), hash);
		}

		for (Personal p : allPersonal) {
			HashMap<String, HashMap<String, Integer>> inhaltKlasseStundenPerso = inhaltKlasseStunden;
			ArrayList<String> reihe = new ArrayList<>();
			reihe.add(p.getKuerzel());
			reihe.add(Integer.toString(p.getSollZeit()));
			reihe.add("- " + Integer.toString(p.getErsatzZeit()));
			for (Planungseinheit pe : planungseinheiten) {
				if (pe.getPersonalbyKuerzel(p.getKuerzel()) != null) {
					// TODO checken, ob die Reihenfolge bei der HashMap gleich
					// bleibt,
					// sonst stimmt die Anzahl der Stunden nicht mehr
					ArrayList<String> inhalteInPlanungseinheit = pe.getStundeninhalte();
					ArrayList<String> klassenInPlanungseinheit = pe.getSchoolclasses();

					for (String s : inhalteInPlanungseinheit) {

						for (String k : klassenInPlanungseinheit) {
							HashMap<String, Integer> neuHash = inhaltKlasseStundenPerso.get(s);
							neuHash.put(k, inhaltKlasseStundenPerso.get(s).get(k)+ pe.duration());
							inhaltKlasseStundenPerso.put(s, neuHash);
						}
					}
				}
			}
			for (Stundeninhalt s : si) {
				ArrayList<Schoolclass> klassen = DataSchulklasse
						.getAllSchulklasse();
				for (Schoolclass k : klassen) {

					if (inhaltKlasseStundenPerso.get(s.getKuerzel()).size() == 0) {
						reihe.add("-");
					} else {

						// Unterscheidung von Lehrer und Pädagoge
						if (p.isLehrer()) {
							reihe.add(k.getName()+ " "+ Integer.toString(inhaltKlasseStundenPerso.get(s.getKuerzel()).get(
													k.getName()) / 45));
						} else {
							reihe.add(k.getName()+ ":  "+ Integer.toString(inhaltKlasseStundenPerso.get(s.getKuerzel()).get(
													k.getName()) / 60));
						}
					}
					model.addRow(new Object[] { reihe.toArray() });
				}
			}
		}
		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
	}
}
