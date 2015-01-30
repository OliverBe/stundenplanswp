package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Realisiert den Personaleinsatzplan als GUI-Element in der MainFrame. Heiﬂt 'Lehreransicht', da es anfangs
 * Verst‰ndnisprobleme in der Gruppe gab, was mit 'Personaleinsatzplan' gemeint war. Benennung wurde aufgrund
 * existierender Verbindungen beibehalten, never change a running system.
 * @author Roman
 *
 */
public class LehreransichtPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;

//	private JFileChooser chooser = new JFileChooser();
//	private JFrame f;

	GridBagConstraints c = new GridBagConstraints();

	private ArrayList<Stundeninhalt> si;
	private ArrayList<Planungseinheit> planungseinheiten;
	private ArrayList<String> allPersoKuerzel;
	
	
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
		model.addColumn("Ist-Zeit");

		planungseinheiten = DataPlanungseinheit.getAllPlanungseinheit();
		ArrayList<Personal> allPersonal = new ArrayList<Personal>();
		allPersoKuerzel = PersonalManager.getAllKuerzel();
		Collections.sort(allPersoKuerzel);
		for(int i = 0; i<allPersoKuerzel.size();i++){
			allPersonal.add(PersonalManager.getPersonalByKuerzel(allPersoKuerzel.get(i)));
		}
		HashMap<String, HashMap<String, Integer>> inhaltKlasseStunden = new HashMap<>();
		si = DataStundeninhalt.getAllStundeninhalte();
		
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
			reihe.add(Integer.toString(p.getIstZeit()));
//			if (planungseinheiten.size() == 0)
//				model.addRow(reihe.toArray());
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
								int[] zeitPersonInPE  = pe.getTimesofPersonal(p);
//								kleineHashMap.put(k, inhaltKlasseStundenPerso.get(s).get(k) + pe.duration());
								kleineHashMap.put(k, inhaltKlasseStundenPerso.get(s).get(k) + 
												TimetableManager.duration(zeitPersonInPE[0]
													, zeitPersonInPE[1]		
													, zeitPersonInPE[2]
													, zeitPersonInPE[3]));
							} else {
								int[] zeitPersonInPE  = pe.getTimesofPersonal(p);
								kleineHashMap.put(k, TimetableManager.duration(zeitPersonInPE[0]
										, zeitPersonInPE[1]		
										, zeitPersonInPE[2]
										, zeitPersonInPE[3]));
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
					System.out.println("Ein - eingf¸gt f¸r: "+s.getKuerzel());
				}
				for (Schoolclass k : klassen) {

					if (s.getKuerzel() != null && s.getRhythmustyp() != 0) {
						if (inhaltKlasseStundenPerso.get(s.getKuerzel()).containsKey(k.getName())) {
							anzahlKlassenInZelle++;
							System.out.println("Anzahl Klassen in Zelle: "+anzahlKlassenInZelle);

							// Unterscheidung von Lehrer und PÔøΩdagoge
							double ergebnisInStunden=0;
							double ergebnisInMinuten=0;
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
//								reihe.add(k.getName() + ":  "
//										+ Double.toString(ergebnisInStunden));
							}
							if (anzahlKlassenInZelle > 1) {
								String teilString = reihe.get(reihe.size()-1);
								reihe.remove(reihe.size()-1);
								reihe.add(teilString+ "<br>"+k.getName()+": "+Double.toString(ergebnisInStunden));
								System.out.println(reihe.toString());
							} else {
								reihe.add("<html><body><center>"+k.getName()
										+ ": "
										+ Double.toString(ergebnisInStunden));
								System.out.println("Reihe erster Durchlauf: "+reihe.toString());
							}
						}
					}
				}
				String substring = reihe.get(reihe.size()-1);
				if(!substring.equals("-")){
					reihe.remove(reihe.size()-1);
					reihe.add(substring+"</center></body></html>");
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
		table.setEnabled(false);
		table.setRowHeight(60);
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
	}
	
	/**
	 * Setzt die ¸bergebene HashMap auf den Anfangswert zur¸ck, der f¸r die init()-Methode pro Person benˆtigt wird.
	 * Da die HashMap 'inhaltKlasseStunden', in der zum Start der init()-Methode alle Stundeninhalte eingef¸gt werden, stets
	 * von allen Schleifen-Durchl‰ufen modifiziert wird, wurde diese Methode angelegt, damit die HashMap jeweils auf den
	 * benˆtigten Anfangsbestand zur¸ck gesetzt wird. Es werden nur die Stundeninhalte als Keys gebraucht und eine
	 * leere HashMap als Value 'hash', die anschlieﬂend von jeder Person mit den entsprechenden Klassen und Stunden bef¸llt wird.
	 * 
	 * @param pInhaltKlasseStunden Die HashMap, die auf den Anfangszustand gesetzt werden soll
	 * @return
	 * 	 Die HashMap in ihrem gewollten Anfangszustand
	 */
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
