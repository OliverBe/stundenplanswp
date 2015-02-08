package de.unibremen.swp.stundenplan.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.xml.datatype.Duration;

import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * erzeugt eine Bearbeitungsfenster fuer Planungseinheit
 * @author Fathan
 *
 */
public class PEedit extends JFrame {
	private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
	private StundenplanPanel parentframe;
	private Object owner;
	private JSpinner spinner1;
	private JSpinner spinner2;
	private JSpinner spinner3;
	private JSpinner spinner4;
	private String[] options = new String[2];
	private JComboBox<Weekday> tag;
	private JCheckBox teamzeit;
	private JCheckBox bandselect;
	public static Comparator<Personal> PersonalComparator = new Comparator<Personal>() {
		public int compare(Personal p1, Personal p2) {
			if (p1.getKuerzel() == null) {
				return 1;
			}
			if (p2.getKuerzel() == null) {
				return -1;
			}
			return p1.getKuerzel().compareTo(p2.getKuerzel());
		}

	};
	public static Comparator<Room> RoomComparator = new Comparator<Room>() {
		public int compare(Room r1, Room r2) {
			if (r1.getName() == null) {
				return 1;
			}
			if (r2.getName() == null) {
				return -1;
			}
			return r1.getName().compareTo(r2.getName());
		}

	};
	public static Comparator<Stundeninhalt> SIComparator = new Comparator<Stundeninhalt>() {
		public int compare(Stundeninhalt s1, Stundeninhalt s2) {
			if (s1.getKuerzel() == null) {
				return 1;
			}
			if (s2.getKuerzel() == null) {
				return -1;
			}
			return s1.getKuerzel().compareTo(s2.getKuerzel());
		}

	};
	public static Comparator<Schoolclass> SCComparator = new Comparator<Schoolclass>() {
		public int compare(Schoolclass sc1, Schoolclass sc2) {
			if (sc1.getName() == null) {
				return 1;
			}
			if (sc2.getName() == null) {
				return -1;
			}
			return sc1.getName().compareTo(sc2.getName());
		}

	};

	private JLabel lLabel1 = new JLabel("Startzeit : Stunde - Minute");
	private JLabel lLabel2 = new JLabel("Endzeit  : Stunde - Minute");
	private JPanel starttime;
	private JPanel endtime;
	private DualListBox pList;
	private DualListBox sIList;
	private DualListBox scList;
	private DualListBox roomList;
	private JLabel label = new JLabel(
			"Hier kann die Planungseinheit bearbeitet werden");
	private JButton button;
	private Planungseinheit pe;


	/**
	 * erzeugt Editor fuer ERzeugung von einer Planungseinheit.
	 */
	public PEedit(final StundenplanPanel pParent, Timeslot pTimeslot,
			Object towner) {
		super("Planungseinheit bearbeiten");
		parentframe = pParent;
		owner = towner;
		init();
		spinner1.setValue(pTimeslot.getsHour());
		spinner2.setValue(pTimeslot.getsMinute());
		spinner3.setValue(pTimeslot.geteHour());
		spinner4.setValue(pTimeslot.geteMinute());
		tag.setSelectedItem(pTimeslot.getDay());
	}

	/**
	 * erzeugt Editor fuer Bearbeitung von einer existierender Planungseinheit.
	 */
	public PEedit(final StundenplanPanel pParent, final int pPeid) {
		super("Planungseinheit bearbeiten");
		parentframe = pParent;
		pe = PlanungseinheitManager.getPlanungseinheitById(pPeid);
		init();
		spinner1.setValue(pe.getStartHour());
		spinner2.setValue(pe.getStartminute());
		spinner3.setValue(pe.getEndhour());
		spinner4.setValue(pe.getEndminute());
		tag.setSelectedItem(pe.getWeekday());
	}

	private void init() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new SpringLayout());
		getContentPane().add(lLabel1);
		getContentPane().add(lLabel2);
		final String[] options = new String[2];
		options[0] = new String("Fortfahren");
		options[1] = new String("Abbrechen");
		starttime = new JPanel();
		endtime = new JPanel();
		SpinnerModel hourmodel = new SpinnerNumberModel(
				TimetableManager.startTimeHour(),
				TimetableManager.startTimeHour(),
				TimetableManager.endTimeHour(), 1);
		SpinnerModel ehourmodel = new SpinnerNumberModel(
				TimetableManager.startTimeHour(),
				TimetableManager.startTimeHour(),
				TimetableManager.endTimeHour(), 1);
		SpinnerModel minmodel = new SpinnerNumberModel(0, 0, 59,
				Timeslot.timeslotlength());
		SpinnerModel eminmodel = new SpinnerNumberModel(0, 0, 59,
				Timeslot.timeslotlength());
		tag = new JComboBox<Weekday>(TimetableManager.validdays());
		starttime.add(tag);
		spinner1 = new JSpinner(hourmodel);
		starttime.add(spinner1);
		spinner2 = new JSpinner(minmodel);
		starttime.add(spinner2);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner2.getEditor())
				.getTextField();
		tf.setEditable(false);
		spinner3 = new JSpinner(ehourmodel);
		endtime.add(spinner3);
		spinner4 = new JSpinner(eminmodel);
		endtime.add(spinner4);
		bandselect = new JCheckBox("Band-Unterricht");
		teamzeit = new JCheckBox("Teamzeit");
		endtime.add(bandselect);
		endtime.add(teamzeit);
		if (pe != null && pe.getSchoolclasses().size() == 0) {
			teamzeit.setSelected(true);
		}
		if (pe != null && pe.getRooms().size() > 1) {
			bandselect.setSelected(true);
		}
		tf = ((JSpinner.DefaultEditor) spinner4.getEditor()).getTextField();
		tf.setEditable(false);
		getContentPane().add(starttime);
		getContentPane().add(endtime);
		pList = new DualListBox("Alle Lehrer",
				" Lehrer in der Planungseinheit", PersonalComparator);
		ArrayList<Personal> plist = DataPersonal.getAllPersonal();
		if (pe != null) {
			plist.removeAll(pe.getPersonal());
			pList.addDestinationElements(pe.getPersonal().toArray());
		}
		if (owner instanceof Personal) {
			if (plist.contains(owner)) {
				plist.remove(owner);
				pList.addDestinationElements(getList(owner));
			}
		}
		pList.addSourceElements(plist.toArray());
		getContentPane().add(pList);
		sIList = new DualListBox("Verfuegbare Stundeninhalte",
				" Stundeninhalte in der Planungseinheit", SIComparator);
		ArrayList<Stundeninhalt> silist = DataStundeninhalt
				.getAllStundeninhalte();
		if (pe != null) {
			silist.removeAll(PlanungseinheitManager.getSIforPE(pe));
			sIList.addDestinationElements(PlanungseinheitManager.getSIforPE(pe)
					.toArray());
		}
		sIList.addSourceElements(silist.toArray());
		getContentPane().add(sIList);
		scList = new DualListBox("Alle Klassen",
				" Klassen in der Planungseinheit", SCComparator);
		ArrayList<Schoolclass> sclist = DataSchulklasse.getAllSchulklasse();
		if (pe != null) {
			sclist.removeAll(PlanungseinheitManager.getSCforPE(pe));
			scList.addDestinationElements(PlanungseinheitManager.getSCforPE(pe)
					.toArray());
		}
		if (owner instanceof Schoolclass) {
			System.out.println(owner);
			if (sclist.contains(owner)) {
				sclist.remove(owner);
				scList.addDestinationElements(getList(owner));
			}
		}
		scList.addSourceElements(sclist.toArray());
		getContentPane().add(scList);
		roomList = new DualListBox("Alle Raeume",
				" Raeume in der Planungseinheit", RoomComparator);
		ArrayList<Room> rlist = DataRaum.getAllRaum();
		if (pe != null) {
			rlist.removeAll(PlanungseinheitManager.getRforPE(pe));
			roomList.addDestinationElements(PlanungseinheitManager
					.getRforPE(pe).toArray());
		}
		roomList.addSourceElements(rlist.toArray());
		getContentPane().add(roomList);
		if (pe != null) {
			pe.getSchoolclasses().clear();
			pe.getRooms().clear();
			pe.getStundeninhalte().clear();
		}
		button = new JButton("Planungseinheit speichern");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if (pList.getDestsize() == 0 ) { // Prueft ob mindestens einen Personal in der PE eingeplant ist
					JOptionPane
							.showMessageDialog(null,
									"Es sind kein Personal eingeplant");
					return;
				} else if (roomList.getDestsize() > 1 //prueft ob Bandunterricht gewaehlt wurde,
																				   //und ob mehr als ein Raum , 
																				   //Stundeninhalt oder Klassen haben
						&& !bandselect.isSelected()) {
					JOptionPane
							.showMessageDialog(null,
									"Nur ein Bandunterricht kann mehrere Raeume haben");
					return;
				}
				
				if (scList.getDestsize() == 0 && !teamzeit.isSelected()) {// prueft ob PE als Teamzeit angewaehlt wurde und
																		  // ob keine Klassen in der PE ist
					JOptionPane
							.showMessageDialog(null,
									"Nur in der Teamzeit kann keine Klasse eingetragen werden");
					return;
				}else if(scList.getDestsize() > 0 && teamzeit.isSelected()){
					JOptionPane
					.showMessageDialog(null,
							"In der Teamzeit soll keine Klasse eingetragen werden");
					return;
				}else if(sIList.getDestsize() == 0 && !teamzeit.isSelected()){
					JOptionPane
					.showMessageDialog(null,
							"Es sind kein Stundeninhalt eingeplant");
					return;
					
				}
				
				Planungseinheit p;
				if (pe != null) {
					p = pe;
				} else {
					p = new Planungseinheit();
				}
				p.setStarthour((int) spinner1.getValue());
				p.setStartminute((int) spinner2.getValue());
				p.setEndhour((int) spinner3.getValue());
				p.setEndminute((int) spinner4.getValue());
				if ((p.getStartHour() == p.getEndhour())   //prueft ob Startzeit und Endzeit gleich ist
						&& (p.getStartminute() == p.getEndminute())) {
					JOptionPane
							.showMessageDialog(null,
									"Planungseinheit darf nicht gleiche Startzeit und Endzeit haben");
					return;
				} else if (p.getStartHour() > p.getEndhour() //prueft ob Startzeit vor Endzeit ist
						|| (p.getStartHour() == p.getEndhour() && p
								.getStartminute() > p.getEndminute())) {
					JOptionPane.showMessageDialog(null,
							"Die Startzeit muss vor der Endzeit beginnen");
					return;
				}
				if ((p.getEndhour() == TimetableManager.endTimeHour() && p //prueft ob zeiten in geplante
						.getEndminute() > TimetableManager.endTimeMinute())//Zeiten der Wochentage sind
						|| p.getEndhour() > TimetableManager.endTimeHour()) {//hier Endzeit
					JOptionPane.showMessageDialog(null,
							"Der Tag ist leider bereits um " + p.getEndhour()
									+ ":" + p.getEndminute() + " zu Ende");
					return;
				}
				if ((p.getStartHour() == TimetableManager.startTimeHour() && p
						.getStartminute() < TimetableManager.startTimeMinute())//Hier Startzeit
						|| p.getStartHour() < TimetableManager.startTimeHour()) {
					JOptionPane.showMessageDialog(null,
							"Der Tag hat leider  um " + p.getStartHour() + ":"
									+ p.getStartminute()
									+ " noch nicht angefangen");
					return;
				}
				p.setWeekday((Weekday) tag.getSelectedItem());
				

				Iterator it = sIList.destinationIterator();
				while (it.hasNext()) {
					Stundeninhalt si = (Stundeninhalt) it.next();
					if (si.getRegeldauer() != p.duration()) {//prueft ob PE Dauer mit Regeldauer von SI uebereinstimmt
						int result = JOptionPane
								.showOptionDialog(
										null,
										"Die Dauer der Planungseinheit ist anders als die Regeldauer des Stundeninhaltes ("
												+ si.getKuerzel()
												+ ")"
												+ "\n"
												+ "Dauer der Planungseinheit in min: "
												+ p.duration()
												+ "\n"
												+ "Regeldauer von "
												+ si.getKuerzel()
												+ " in min: "
												+ si.getRegeldauer(),
										"Warnung", 0,
										JOptionPane.YES_NO_OPTION, null,
										options, null);
						if (result == 0) {
							WarningPanel.setText("Regeldauer bei Planung von "
									+ si.getKuerzel());
						} else {
							return;
						}
					}
					p.addStundeninhalt(si);
				}
				if(roomList.getDestsize() == 0 && PlanungseinheitManager.istKeinPause(p)){ //prueft ob SI Pause ist
					JOptionPane
					.showMessageDialog(null,
							"Nur Pausen haben keine Raeume");
					return;
				}
				
				it = roomList.destinationIterator();
				while (it.hasNext()) {
					Room r = (Room) it.next();
					if (PlanungseinheitManager.roomsiCheck(r, p)) { // prueft ob Raum mit SI im PE uebereinstimmen
						int result = JOptionPane.showOptionDialog(
								null,
								"Der Raum ("
										+ r.getName()
										+ ") ist nicht fuer die geplanten Stundeninhalte vorhergesehen."
										+ "\n" + "Stundeninhalte vom Raum: "
										+ r.getmSI() + "\n"
										+ "Stundeninhalte in der Planung: "
										+ p.stundenInhaltetoString(),
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							WarningPanel
									.setText("Stundeninhalt nicht fuer Raum vorhergesehen"
											+ r.getName());
						} else {
							return;
						}
					}
					if (PlanungseinheitManager.checkRoomPE(r, p)) { // prueft ob Raum zur PE ZEit besetzt ist
						JOptionPane.showMessageDialog(null,
								"Der Raum (" + r.getName()
										+ ") ist schon zu dieser Zeit gebucht");
						return;
					} else {
						p.addRoom(r);
					}
				}
				
				it = scList.destinationIterator();
				while (it.hasNext()) {
					Schoolclass sc = (Schoolclass) it.next();
					if (PlanungseinheitManager.checkScPE(sc, p)) { //Prueft ob Klasse schon verplant ist
						JOptionPane.showMessageDialog(null,
								"Die Klasse (" + sc.getName()
										+ ") ist schon zu dieser Zeit gebucht");
						return;
					} else {
						if(checkPendelbeforeSC(sc, p) != null){//prueft ob Klasse genug Zeit zu pendeln hat von PE bevor
							if (getSpacebetweenPEs(checkPendelbeforeSC(sc, p),
									p) < getPZeit()) {
								int result = JOptionPane
										.showOptionDialog(
												null,
												"Vor der geplante Unterricht ist Klasse "
														+ sc.getName()
														+ " in einer anderen Gebaeude und hat nicht genug Zeit zu pendeln.",
												"Warnung", 0,
												JOptionPane.YES_NO_OPTION,
												null, options, null);
								if (result == 0) {
									WarningPanel.setText("Klasse "
											+ sc.getName()
											+ " hat nicht genug Zeit zu pendeln");
								} else {
									return;
								}
							}
						}
						if(checkPendelafterSC(sc, p) != null){//prueft ob Klasse genug Zeit zu pendeln hat zu PE danach
							if (getSpacebetweenPEs(
									p,checkPendelafterSC(sc, p)) < getPZeit()) {
								int result = JOptionPane
										.showOptionDialog(
												null,
												"Nach der geplante Unterricht ist Klasse "
														+ sc.getName()
														+ " in einer anderen Gebaeude und hat nicht genug Zeit zu pendeln.",
												"Warnung", 0,
												JOptionPane.YES_NO_OPTION,
												null, options, null);
								if (result == 0) {
									WarningPanel.setText("Klasse "
											+ sc.getName()
											+ " hat nicht genug Zeit zu pendeln");
								} else {
									return;
								}
							}
						}
						p.addSchulklassen(sc);
					}
				}
				

				if (PlanungseinheitManager.peRoomGcheck(p)) { // prueft ob Raeum in PE im selben Gebaude befindet
					JOptionPane.showMessageDialog(null,
							"Raeume sind nicht im selben Gebaeude");
					return;
				}
				
				it = pList.destinationIterator();
				ArrayList<Personal> listp = new ArrayList<Personal>();
				while (it.hasNext()) {
					Personal pr = (Personal) it.next();
					if (PlanungseinheitManager.checkPersonPE(pr, p)) { //prueft ob Personal verplant ist
						JOptionPane.showMessageDialog(null,
								"Personal (" + pr.getName()
										+ ") ist schon zu dieser Zeit gebucht");
						return;
					} else {
						if(checkPendelbeforePers(pr, p) != null){//prueft ob Personal genug Zeit zu pendeln hat von PE bevor
							if (getSpacebetweenPEs(checkPendelbeforePers(pr, p),
									p) < getPZeit()) {
								int result = JOptionPane
										.showOptionDialog(
												null,
												"Vor der geplante Unterricht ist Personal "
														+ pr.getName()
														+ " in einer anderen Gebaeude und hat nicht genug Zeit zu pendeln.",
												"Warnung", 0,
												JOptionPane.YES_NO_OPTION,
												null, options, null);
								if (result == 0) {
									WarningPanel.setText("Personal "
											+ pr.getName()
											+ " hat nicht genug Zeit zu pendeln");
								} else {
									return;
								}
							}
						}
						if(checkPendelafterPers(pr, p) != null){//prueft ob personal genug Zeit zu pendeln hat von PE danach
							if (getSpacebetweenPEs(
									p,checkPendelafterPers(pr, p)) < getPZeit()) {
								int result = JOptionPane
										.showOptionDialog(
												null,
												"Nach der geplante Unterricht ist Personal "
														+ pr.getName()
														+ " in einer anderen Gebaeude und hat nicht genug Zeit zu pendeln.",
												"Warnung", 0,
												JOptionPane.YES_NO_OPTION,
												null, options, null);
								if (result == 0) {
									WarningPanel.setText("Personal "
											+ pr.getName()
											+ " hat nicht genug Zeit zu pendeln");
								} else {
									return;
								}
							}
						}
						listp.add(pr);
					}
				}
				
				for (Personal pers : listp) {
					if (PlanungseinheitManager.personalsiCheck(pers, p)) {//prueft ob Personal fuer SI im PE vorgesehen ist
						int result = JOptionPane.showOptionDialog(
								null,
								"Das Personal ("
										+ pers.getName()
										+ ") ist nicht fuer die geplanten Stundeninhalte vorhergesehen."
										+ "\n"
										+ "Stundeninhalte vom Personal: "
										+ pers.getmSI() + " \n"
										+ "Stundeninhalte in der Planung: "
										+ p.stundenInhaltetoString(),
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							WarningPanel
									.setText("Stundeninhalt nicht fuer Personal vorhergesehen"
											+ pers.getName());
						} else {
							return;
						}
					}

					if (PlanungseinheitManager.personalWZCheck(pers, p)) { // prueft ob WZ  mit PE-Zeit uebereinstimmt
						int result = JOptionPane.showOptionDialog(
								null,
								"Das Personal ("
										+ pers.getName()
										+ ") ist zu der geplanten Zeitspanne nicht verfuegbar"
										+ "\n"
										+ "Anwesenheit des Personals: "
										+ printWZ(pers
												.getWunschzeitForWeekday(p
														.getWeekday())),
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							// add Warningpanel
							WarningPanel
									.setText("Wunschzeit stimmt nicht mit geplanter Zeit ueberein"
											+ pers.getName());
						} else {
							return;
						}
					}

					if (PlanungseinheitManager.overtimePers(pers, p.duration())) {//prueft ob istzeit sollzeit uebersteigt
						int result = JOptionPane
								.showOptionDialog(
										null,
										"Die Istzeit vom Personal ("
												+ pers.getName()
												+ ") uebersteigt mit der Eintragung die "
												+ "\n"
												+ "Sollzeit: "
												+ pers.getSollZeit()
												+ "\n"
												+ "neue Istzeit: "
												+ PlanungseinheitManager
														.newTimeforPers(pers,
																p.duration()),
										"Warnung", 0,
										JOptionPane.YES_NO_OPTION, null,
										options, null);
						if (result == 0) {
							// add Warningpanel
							WarningPanel.setText("Personal macht Ueberstunden"
									+ pers.getName());
						} else {
							return;
						}
					}

				}
				PersonalTimePEDialog pdialog = new PersonalTimePEDialog(
						getmyFrame(), listp, p);
				if (pdialog.getsaved()) {
					parentframe.updatetable();
					parentframe.updateLists();
					dispose();
				} else {
					return;
				}
			}
		});
		getContentPane().add(label);
		getContentPane().add(button);
		SpringUtilities.makeCompactGrid(getContentPane(), 5, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad
		setSize(1000, 700);
	}

	private Object[] getList(Object powner) {
		// TODO Auto-generated method stub
		Object[] nlist = new Object[1];
		nlist[0] = powner;
		return nlist;
	}
	
	private Planungseinheit checkPendelbeforePers(final Personal pers, final Planungseinheit pe){
		ArrayList<Planungseinheit> pes = PlanungseinheitManager.getPEForPersonalbyWeekday(pe.getWeekday(),pers);
		return PlanungseinheitManager.checkbeforePendelPES(pes, pe);
	}
	
	private Planungseinheit checkPendelafterPers(final Personal pers, final Planungseinheit pe){
		ArrayList<Planungseinheit> pes = PlanungseinheitManager.getPEForPersonalbyWeekday(pe.getWeekday(),pers);
		return PlanungseinheitManager.checkafterPendelPES(pes, pe);
	}
	
	private Planungseinheit checkPendelbeforeSC(final Schoolclass pSC, final Planungseinheit pe){
		ArrayList<Planungseinheit> pes = PlanungseinheitManager.getPEForSchoolclassbyWeekday(pe.getWeekday(),pSC);
		return PlanungseinheitManager.checkbeforePendelPES(pes, pe);
	}
	
	private Planungseinheit checkPendelafterSC(final Schoolclass pSC, final Planungseinheit pe){
		ArrayList<Planungseinheit> pes = PlanungseinheitManager.getPEForSchoolclassbyWeekday(pe.getWeekday(),pSC);
		return PlanungseinheitManager.checkafterPendelPES(pes, pe);
	}
	
	/**
	 * rechnet die Differenz zwischen zwei Planungseinheiten.
	 * @param p1
	 * @param p2
	 * @return gibt differenz in minuten zurueck
	 */
	private int getSpacebetweenPEs(Planungseinheit p1,Planungseinheit p2){
		return TimetableManager.duration(p1.getEndhour(),p1.getEndminute(), p2.getStartHour(), p2.getStartminute());
	}
	
	/**
	 * holt die eingestellte mindestpendelzeit im Konfigdatei
	 * @return mindestpendelzeit in Minuten
	 */
	private int getPZeit(){
		return Config.getInt(Config.PENDELTIME_STRING, Config.PENDELTIME);
	}
	/**
	 * Formatiert Anfangszeit und Endzeit in Format (SH:SM - EH:EM)
	 * @param wZ die Stundenzahl und Minutenzahl in 4elementige Array
	 * @return gibt Zeichenkette zurueck, falls Eingabe richtig ist sonst, leere String
	 */
	private String printWZ(int[] wZ) {
		if (wZ.length != 4) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d:%02d", wZ[0], wZ[1]));
		sb.append(" - ");
		sb.append(String.format("%02d:%02d", wZ[2], wZ[3]));
		return sb.toString();
	}

	private JFrame getmyFrame() {
		return this;
	}
}
