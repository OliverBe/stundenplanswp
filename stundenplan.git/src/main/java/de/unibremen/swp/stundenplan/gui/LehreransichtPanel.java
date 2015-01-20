package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

	ArrayList<Stundeninhalt> si = DataStundeninhalt.getAllStundeninhalte();
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

		ArrayList<Planungseinheit> planungseinheiten = DataPlanungseinheit
				.getAllPlanungseinheit();
		ArrayList<Personal> allPersonal = PersonalManager
				.getAllPersonalFromDB();
		HashMap<String, HashMap<String, Integer>> inhaltKlasseStunden = new HashMap<>();

		for (Stundeninhalt s : si) {
			if (s.getRhythmustyp() == 0)
				continue;
			model.addColumn(s.getKuerzel());
			HashMap<String, Integer> hash = new HashMap<>();
			inhaltKlasseStunden.put(s.getKuerzel(), hash);
		}

		for (Personal p : allPersonal) {
			System.out.println("PERSONAL WIRD BEARBEITET: "+ p.getName());
			HashMap<String, HashMap<String, Integer>> inhaltKlasseStundenPerso = hashMapReset(inhaltKlasseStunden);
			ArrayList<String> reihe = new ArrayList<>();
			reihe.add(p.getKuerzel());
			reihe.add(Integer.toString(p.getSollZeit()));
			if (p.getErsatzZeit() == 0) {
				reihe.add("-");
			} else {
				reihe.add("- " + Integer.toString(p.getErsatzZeit()));
			}
			if (planungseinheiten.size() == 0)
				model.addRow(reihe.toArray());
			for (Planungseinheit pe : planungseinheiten) {
				if (pe.getPersonalbyKuerzel(p.getKuerzel()) != null) {
					HashMap<String, Integer> kleineHashMap = new HashMap<>();
					// TODO checken, ob die Reihenfolge bei der HashMap gleich
					// bleibt,
					// sonst stimmt die Anzahl der Stunden nicht mehr
					ArrayList<String> inhalteInPlanungseinheit = pe
							.getStundeninhalte();

					ArrayList<String> klassenInPlanungseinheit = pe
							.getSchoolclasses();

					for (String s : inhalteInPlanungseinheit) {
						for (String k : klassenInPlanungseinheit) {
							System.out.println(inhaltKlasseStundenPerso.get(s).toString());
							if (inhaltKlasseStundenPerso.get(s).get(k) != null) {
								kleineHashMap = inhaltKlasseStundenPerso.get(s);
								kleineHashMap.put(k, inhaltKlasseStundenPerso
										.get(s).get(k) + pe.duration());
							} else {
								kleineHashMap.put(k, pe.duration());
							}
							if(inhaltKlasseStundenPerso.get(s) == null){
								inhaltKlasseStundenPerso.put(s, kleineHashMap);
							}else{
								HashMap<String,Integer> zwischenHash = inhaltKlasseStundenPerso.get(s);
								zwischenHash.putAll(kleineHashMap);
								inhaltKlasseStundenPerso.put(s, zwischenHash);
							}
							System.out.println("inhaltKlasseStundenPerso ist: "+inhaltKlasseStundenPerso.toString());
						}
					}
				}
			}
			for (Stundeninhalt s : si) {
				ArrayList<Schoolclass> klassen = DataSchulklasse
						.getAllSchulklasse();
				int anzahlKlassenInZelle = 0;
				if (s.getKuerzel() != null && s.getRhythmustyp() != 0&&inhaltKlasseStundenPerso.get(s.getKuerzel()).size() == 0) {
					reihe.add("-");
					System.out.println("Ein - eingfügt für: "+s.getKuerzel());
				}
				for (Schoolclass k : klassen) {

					if (s.getKuerzel() != null && s.getRhythmustyp() != 0) {
						if (inhaltKlasseStundenPerso.get(s.getKuerzel()).containsKey(k.getName())) {
							anzahlKlassenInZelle++;
							System.out.println("Anzahl Klassen in Zelle: "+anzahlKlassenInZelle);

							// Unterscheidung von Lehrer und Pï¿½dagoge
							if (p.isLehrer()) {
								System.out.println("s ist: " + s.getKuerzel()
										+ "\nin inhaltKlasseStundenPerso ist: "
										+ inhaltKlasseStundenPerso.toString());
								System.out.println("Stundeninhalt ist: "
										+ inhaltKlasseStundenPerso.get(s
												.getKuerzel()));
								System.out.println("Gefundener Wert für "
										+ k.getName()
										+ ": "
										+ inhaltKlasseStundenPerso.get(
												s.getKuerzel())
												.get(k.getName()));
								double ergebnisInMinuten = (inhaltKlasseStundenPerso
										.get(s.getKuerzel()).get(k.getName()));
								double ergebnisInStunden = Math
										.round(((ergebnisInMinuten / 45) * 100));
								ergebnisInStunden = ergebnisInStunden / 100;
								if (anzahlKlassenInZelle > 1) {
									String teilString = reihe.get(reihe.size()-1);
									System.out.println("StringBuilder nimmt: "+reihe.get(reihe.size()-1));
									StringBuilder builder = new StringBuilder(
											teilString);
									builder.append("  ||  " +k.getName()
											+ ": "
											+ Double.toString(ergebnisInStunden));
									reihe.remove(reihe.size()-1);
									reihe.add(builder.toString());
									System.out.println(reihe.toString());
								} else {
									reihe.add(k.getName()
											+ ": "
											+ Double.toString(ergebnisInStunden));
									System.out.println("Reihe erster Durchlauf: "+reihe.toString());
								}
							} else {
								double ergebnisInMinuten = (inhaltKlasseStundenPerso
										.get(s.getKuerzel()).get(k.getName()));
								double ergebnisInStunden = Math
										.round(((ergebnisInMinuten / 60) * 100));
								ergebnisInStunden = ergebnisInStunden / 100;
								reihe.add(k.getName() + ":  "
										+ Double.toString(ergebnisInStunden));
							}
						}
					}
				}
			}
			System.out.println("REIHE VOR EINFUEGEN: "+reihe.toString());
			System.out.println("InhaltKlasseStunden ist: "+inhaltKlasseStunden.toString()
					+"\n InhaltKlasseStundenPerso von "+ p.getName()+" ist: "+inhaltKlasseStundenPerso.toString());
			model.addRow(reihe.toArray());
		}

		ArrayList<String> kuerzelInAllenReihen = new ArrayList<String>();
		for (int i = 0; i < model.getRowCount(); i++) {
			kuerzelInAllenReihen.add(model.getValueAt(i, 0).toString());
		}

		for (Personal p : allPersonal) {
			ArrayList<String> reihe = new ArrayList<String>();
			if (!kuerzelInAllenReihen.contains(p.getKuerzel())) {
				reihe.add(p.getKuerzel());
				for (int i = 0; i < model.getColumnCount(); i++) {
					reihe.add("-");
				}
				model.addRow(reihe.toArray());
			}
		}

		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
	}
	
	private HashMap<String, HashMap<String,Integer>> hashMapReset(final HashMap<String, HashMap<String,Integer>> pInhaltKlasseStunden){
		pInhaltKlasseStunden.clear();
		for (Stundeninhalt s : si) {
			if (s.getRhythmustyp() == 0)
				continue;
			HashMap<String, Integer> hash = new HashMap<>();
			pInhaltKlasseStunden.put(s.getKuerzel(), hash);
		}
		return pInhaltKlasseStunden;
	}
}
