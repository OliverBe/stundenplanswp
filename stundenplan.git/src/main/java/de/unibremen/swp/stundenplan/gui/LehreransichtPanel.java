package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Realisiert den Personaleinsatzplan als GUI-Element in der MainFrame. Heisst
 * 'Lehreransicht', da es anfangs Verstaendnisprobleme in der Gruppe gab, was
 * mit 'Personaleinsatzplan' gemeint war. Benennung wurde aufgrund existierender
 * Verbindungen beibehalten, never change a running system.
 * 
 * @author Roman
 *
 */
public class LehreransichtPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Respraesentiert das Tabllenelement.
	 */
	private JTable table;

	/**
	 * Constraints zum festlegen, wo im Panel Elemente hinzugefuegt werden
	 */
	private GridBagConstraints c = new GridBagConstraints();

	/**
	 * Liste, die im Verlaufe der init Methode die existierenden Stundeninhalte
	 * fuehrt
	 */
	private ArrayList<Stundeninhalt> si;

	/**
	 * Liste, die im Verlaufe der init Methode die existierenden PEs fuehrt.
	 */
	private ArrayList<Planungseinheit> planungseinheiten;

	/**
	 * Liste, die im Verlaufe der init Methode die existierenden Kuerzel aller
	 * Personen fuehrt.
	 */
	private ArrayList<String> allPersoKuerzel;

	/**
	 * Konstruktor, ruft lediglich die init Methode auf.
	 */
	public LehreransichtPanel() {
		init();
	}

	/**
	 * Initialisiert den Personaleinsatzplan. In den einzelnen Schritten wird 1.
	 * Standard Columns erstellen 2. Stundeninhalte als Columns hinzufuegen 3.
	 * Alle Personen durchgehen 4. Standard Columns der Person einfuegen 5.
	 * Planungseinheiten der Personen durchgehen, Laenge und Klasse in HashMap
	 * speichern 6. HashMap mit Stundeninhalten abgleichen und neu mappen 7.
	 * neue HashMap mit Stundeninhalten und dazugehoerigen Klassen + Zeiten als
	 * Array in die Reihen der entsprechenden Personen eingeben
	 * 
	 * Genauere Kommentare finden sich im Code
	 */
	public void init() {

		setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;

		// Hier werden zunaechst die Standard Columns gesetzt und Vorbereitungen
		// getroffen.
		DefaultTableModel model = new DefaultTableModel();
		table = new JTable(model);
		table.setDefaultRenderer(String.class, new LineWrapCellRenderer());
		model.addColumn("Kuerzel");
		model.addColumn("Arbeitszeit");
		model.addColumn("Ersatzzeit");
		model.addColumn("Ist-Zeit");

		// Benoetigte Listen werden mit Daten befuellt.
		planungseinheiten = DataPlanungseinheit.getAllPlanungseinheit();
		ArrayList<Personal> allPersonal = new ArrayList<Personal>();
		allPersoKuerzel = PersonalManager.getAllKuerzel();
		Collections.sort(allPersoKuerzel);

		// Da urspruenglicher Einsatzplan alphabetisch sortiert war, habe ich
		// diesen
		// die Personalliste ebenfalls alphabetisch sortiert.
		for (int i = 0; i < allPersoKuerzel.size(); i++) {
			allPersonal.add(PersonalManager
					.getPersonalByKuerzel(allPersoKuerzel.get(i)));
		}
		// HashMap<String, HashMap<String, Integer>> inhaltKlasseStunden = new
		// HashMap<>();
		si = DataStundeninhalt.getAllStundeninhalte();

		// Hier werden die Columns mit allen Stundeninhaltenkuerzeln besetzt.
		// Pausen (Rhythmustyp 0) werden nicht beruecksichtigt.
		for (Stundeninhalt s : si) {
			if (s.getRhythmustyp() == 0)
				continue;
			model.addColumn(s.getKuerzel());
		}

		for (Personal p : allPersonal) {
			// Diese HashMap soll spaeter die Kuerzel aller Stundeninhalte als
			// Key haben und die Value pro
			// Stundeninhalt ist eine weitere HashMap, die Klassennamen auf die
			// Dauer, die die Person diese Klasse
			// im Stundeninhalt unterrichtet speichert.
			HashMap<String, HashMap<String, Integer>> inhaltKlasseStundenPerso = hashMapReset(new HashMap<String, HashMap<String, Integer>>());
			// Die Reihe im Table braucht spaeter ein Array und da man die
			// ArrayList leicht in ein Array umwandeln kann,
			// und sie benutzerfreundlicher ist, werden die Reihenelemente nach
			// und nach in die List eingefuegt und danach
			// in die eigentliche Tabellenreihe eingefuegt.
			ArrayList<String> reihe = new ArrayList<>();
			reihe.add(" "+p.getKuerzel());
			reihe.add(" "+Integer.toString(p.getSollZeit()));
			if (p.getErsatzZeit() == 0) {
				reihe.add(" -");
			} else {
				reihe.add(" - " + Integer.toString(p.getErsatzZeit()));
			}
			reihe.add(" "+Integer.toString(p.getIstZeit()));

			for (Planungseinheit pe : planungseinheiten) {
				if (pe.getPersonalbyKuerzel(p.getKuerzel()) != null) {
					// Dies ist eine der HashMaps, die spaeter in den Values der
					// einzelnen Stundeninhalt Keys gespeichert sind.
					HashMap<String, Integer> kleineHashMap = new HashMap<>();
					ArrayList<String> inhalteInPlanungseinheit = pe
							.getStundeninhalte();

					ArrayList<String> klassenInPlanungseinheit = pe
							.getSchoolclasses();

					// Pro Stundeninhalt in der Planungseinheit wird nun
					// ueberprueft, ob die Schulklasse bereits gemaped ist
					// Wenn ja, das heisst, die Klasse wird in diesem Inhalt
					// bereits von der Person unterrichtet, muss man
					// die bereits vorhandene Zeit mit der neuen addieren.
					//
					// Wenn nein, das heisst, die Klasse wird zu diesem
					// Zeitpunkt der Iteration noch nicht unterrichtet,
					// muss ein neuer Eintrag fuer die Klasse in der kleinen
					// HashMap angelegt werden mit der Dauer
					// der Planungseinheit.
					for (String s : inhalteInPlanungseinheit) {
						for (String k : klassenInPlanungseinheit) {
							if (inhaltKlasseStundenPerso != null && inhaltKlasseStundenPerso.get(s) != null) {
								if (inhaltKlasseStundenPerso.get(s).get(k) != null) {
									kleineHashMap = inhaltKlasseStundenPerso
											.get(s);
									int[] zeitPersonInPE = pe
											.getTimesofPersonal(p);
									kleineHashMap
											.put(k,
													inhaltKlasseStundenPerso
															.get(s).get(k)
															+ TimetableManager
																	.duration(
																			zeitPersonInPE[0],
																			zeitPersonInPE[1],
																			zeitPersonInPE[2],
																			zeitPersonInPE[3]));
								} else {
									int[] zeitPersonInPE = pe
											.getTimesofPersonal(p);
									kleineHashMap.put(k, TimetableManager
											.duration(zeitPersonInPE[0],
													zeitPersonInPE[1],
													zeitPersonInPE[2],
													zeitPersonInPE[3]));
								}
							}
							// Falls der Stundeninhalt noch ueberhaupt nicht mit
							// dieser Klasse in Verbindung gebracht wurde
							// waehrend der Iteration muss ein ganz neuer
							// Eintrag in die HashMap erfolgen
							if (inhaltKlasseStundenPerso.get(s) == null) {
								inhaltKlasseStundenPerso.put(s, kleineHashMap);
							} else {
								HashMap<String, Integer> zwischenHash = inhaltKlasseStundenPerso
										.get(s);
								zwischenHash.putAll(kleineHashMap);
								inhaltKlasseStundenPerso.put(s, zwischenHash);
							}

						}
					}
				}
			}
			// Die HashMaps sind nun befuellt, es bedarf jedoch noch Anpassung
			// mit Hinblick auf Stundeninhalte,
			// die die Person in mehreren Klassen unterrichtet, diese
			// verschiedenen Klassen muessen nun in einen String
			// zusammengebracht werden, damit sie zusammen in der richtigen
			// Zelle erscheinen.
			for (Stundeninhalt s : si) {
				ArrayList<Schoolclass> klassen = DataSchulklasse
						.getAllSchulklasse();
				// Iteratin benoetigt Zaehler, damit man weiss, wann der Fall
				// von mehreren Klassen in einer Zelle auftritt.
				int anzahlKlassenInZelle = 0;
				// Fuer nicht belegte Stundeninhalte wird der Uebersicht halber
				// ein " - " hinzugefuegt.
				if (s.getKuerzel() != null
						&& s.getRhythmustyp() != 0
						&& inhaltKlasseStundenPerso.get(s.getKuerzel()).size() == 0) {
					reihe.add("-");
				}
				for (Schoolclass k : klassen) {

					if (s.getKuerzel() != null && s.getRhythmustyp() != 0) {
						if (inhaltKlasseStundenPerso.get(s.getKuerzel())
								.containsKey(k.getName())) {
							anzahlKlassenInZelle++;

							// Unterscheidung von Lehrer und Paedagoge, die
							// Stunden zaehlen ein Mal
							// 45 fuer Lehrer oder 60 fuer Paedagogen
							// Hier wird zunaechst nur die Unterrichtsdauer der
							// iterierten Schulklasse errechnet.
							double ergebnisInStunden = 0;
							double ergebnisInMinuten = 0;
							if (p.isLehrer()) {
								ergebnisInMinuten = (inhaltKlasseStundenPerso
										.get(s.getKuerzel()).get(k.getName()));
								ergebnisInStunden = Math
										.round(((ergebnisInMinuten / 45) * 100));
								ergebnisInStunden = ergebnisInStunden / 100;

							} else {
								ergebnisInMinuten = (inhaltKlasseStundenPerso
										.get(s.getKuerzel()).get(k.getName()));
								ergebnisInStunden = Math
										.round(((ergebnisInMinuten / 60) * 100));
								ergebnisInStunden = ergebnisInStunden / 100;
							}
							// Ist der Zaehler groesser als 1, heisst das, es
							// sind mehr als 1 Klasse in dieser Zelle
							// und die Strings werden zusammengesetzt. Letzter
							// Eintrag in der Reihe wird geloescht und
							// mit neuer Klasse in Zelle + Dauer
							// zusammengesetzt. 
							if (anzahlKlassenInZelle > 1) {
								String teilString = reihe.get(reihe.size() - 1);
								reihe.remove(reihe.size() - 1);
								reihe.add(teilString + "\n "+ k.getName()
										+ ": "
										+ Double.toString(ergebnisInStunden));
							} else {
//								reihe.add("<html><body><center>" + k.getName()
//										+ ": "
//										+ Double.toString(ergebnisInStunden)
//										+ "\n"+"Test test"+"\n"+"test2");
								reihe.add(" "+k.getName()
										+ ": "
										+ Double.toString(ergebnisInStunden));
							}
						}
					}
				}
			}
			// Hier wird die nun fertige Reihe als ArrayList in ein normales
			// Aray das model uebergeben
			// und die Eintraege der Person stehen in der Reihe.
			// Schleife der Person endet hier.
			model.addRow(reihe.toArray());
		}

		// Falls Lehrer noch keinen Planungseinheiten zugeteilt sind, tauchen
		// sie noch nicht
		// im Plan auf. Hier werden alle vorhandenen Kuerzel im Plan gsammelt.
		ArrayList<String> kuerzelInAllenReihen = new ArrayList<String>();
		for (int i = 0; i < model.getRowCount(); i++) {
			kuerzelInAllenReihen.add(model.getValueAt(i, 0).toString());
		}

		// Und hier werden die fehlenden Personen hinzugefuegt, wenn sie noch
		// nicht im Plan auftauchen.
		for (Personal p : allPersonal) {
			ArrayList<String> reihe = new ArrayList<String>();
			if (!kuerzelInAllenReihen.contains(" "+p.getKuerzel())) {
				reihe.add(p.getKuerzel());
				for (int i = 0; i < model.getColumnCount(); i++) {
					reihe.add("-");
				}
				model.addRow(reihe.toArray());

			}
		}

//		table.setRowSelectionAllowed(true);
		table.setEnabled(false);
		table.setRowHeight(55);
		for(int a=4; a<table.getColumnCount(); a++){
			table.getColumnModel().getColumn(a).setResizable(true);
		}
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
	}

	/**
	 * Setzt die uebergebene HashMap auf den Anfangswert zurueck, der fuer die
	 * init()-Methode pro Person benoetigt wird. Da die HashMap
	 * 'inhaltKlasseStunden', in der zum Start der init()-Methode alle
	 * Stundeninhalte eingefuegt werden, stets von allen Schleifen-Durchlaufen
	 * modifiziert wird, wurde diese Methode angelegt, damit die HashMap jeweils
	 * auf den benoetigten Anfangsbestand zurueck gesetzt wird. Es werden nur
	 * die Stundeninhalte als Keys gebraucht und eine leere HashMap als Value
	 * 'hash', die anschliessend von jeder Person mit den entsprechenden Klassen
	 * und Stunden befaellt wird.
	 * 
	 * @param pInhaltKlasseStunden
	 *            Die HashMap, die auf den Anfangszustand gesetzt werden soll
	 * @return Die HashMap in ihrem gewollten Anfangszustand
	 */
	private HashMap<String, HashMap<String, Integer>> hashMapReset(
			final HashMap<String, HashMap<String, Integer>> pInhaltKlasseStunden) {
		pInhaltKlasseStunden.clear();
		for (Stundeninhalt s : si) {
			if (s.getRhythmustyp() == 0)
				continue;
			HashMap<String, Integer> hash = new HashMap<>();
			pInhaltKlasseStunden.put(s.getKuerzel(), hash);
		}
		return pInhaltKlasseStunden;
	}

	public JTable getTable() {
		return table;
	}

	public ArrayList<Stundeninhalt> getSi() {
		return si;
	}

	public ArrayList<Planungseinheit> getPlanungseinheiten() {
		return planungseinheiten;
	}

	public ArrayList<String> getAllPersoKuerzel() {
		return allPersoKuerzel;
	}
}
