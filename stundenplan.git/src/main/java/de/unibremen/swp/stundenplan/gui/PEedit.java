package de.unibremen.swp.stundenplan.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
			if (p1.getName() == null) {
				return 1;
			}
			if (p2.getName() == null) {
				return -1;
			}
			return p1.getName().compareTo(p2.getName());
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
			if (s1.getName() == null) {
				return 1;
			}
			if (s2.getName() == null) {
				return -1;
			}
			return s1.getName().compareTo(s2.getName());
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
			"Hier koennen die Planungseinheiten bearbeitet werden");
	private JButton button;
	private Planungseinheit pe;

	// /**
	// * Launch the application.
	// */
	// public static void main(String[] args) {
	// try {
	// Config.init(null);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// PEedit frame = new PEedit();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * erzeugt Editor fuer ERzeugung von einer Planungseinheit.
	 */
	public PEedit(final StundenplanPanel pParent, Timeslot pTimeslot, Object towner) {
		super("Planungseinheiten bearbeiten");
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
	 * erzeugt Editor fuer Erzeugung von einer Planungseinheit.
	 */
	public PEedit(final StundenplanPanel pParent, final int pPeid) {
		super("Planungseinheiten bearbeiten");
		parentframe = pParent;
		pe = PlanungseinheitManager.getPlanungseinheitById(pPeid);
		init();
		spinner1.setValue(pe.getStartHour());
		spinner2.setValue(pe.getStartminute());
		spinner3.setValue(pe.getEndhour());
		spinner4.setValue(pe.getEndminute());
		tag.setSelectedItem(pe.getWeekday());
		if(pe.getSchoolclasses().size() == 0){
			teamzeit.setSelected(true);
		}
		if(pe.getRooms().size()>1){
			bandselect.setSelected(true);
		}
		pList.clearSourceListModel();
		pList.clearDestinationListModel();
		sIList.clearSourceListModel();
		sIList.clearDestinationListModel();
		scList.clearSourceListModel();
		scList.clearDestinationListModel();
		roomList.clearSourceListModel();
		roomList.clearDestinationListModel();
	}

	private void init() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new SpringLayout());
		getContentPane().add(lLabel1);
		getContentPane().add(lLabel2);
		final String[] options = new String[2];
		options[0] = new String("fortfahren");
		options[1] = new String("abbrechen");
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
		tf = ((JSpinner.DefaultEditor) spinner4.getEditor()).getTextField();
		tf.setEditable(false);
		getContentPane().add(starttime);
		getContentPane().add(endtime);
		pList = new DualListBox("Alle Lehrer", " Lehrer im Planungseinheit",
				PersonalComparator);
		ArrayList<Personal> plist = DataPersonal.getAllPersonal();
		
		if(owner instanceof Personal){
			if (plist.contains(owner)){
				plist.remove(owner);
				pList.addDestinationElements(getList(owner));
			}
		}
		pList.addSourceElements(plist.toArray());
		getContentPane().add(pList);
		sIList = new DualListBox("Verfuegbare Stundeninhalte",
				" Stundeninhalte im Planungseinheit", SIComparator);
		sIList.addSourceElements(DataStundeninhalt.getAllStundeninhalte()
				.toArray());
		getContentPane().add(sIList);
		scList = new DualListBox("Alle Klassen", " Klassen im Planungseinheit",
				SCComparator);
		ArrayList<Schoolclass> sclist = DataSchulklasse.getAllSchulklasse();
		if(owner instanceof Schoolclass){
			System.out.println(owner);
			if (sclist.contains(owner)){
				sclist.remove(owner);
				scList.addDestinationElements(getList(owner));
			}
		}
		scList.addSourceElements(sclist.toArray());
		getContentPane().add(scList);
		roomList = new DualListBox("Alle Raeume", " Raeume im Planungseinheit",
				RoomComparator);
		roomList.addSourceElements(DataRaum.getAllRaum().toArray());
		getContentPane().add(roomList);
		button = new JButton("Planungseinheiten speichern");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				if (pList.getDestsize() == 0 || roomList.getDestsize() == 0) {
					JOptionPane.showMessageDialog(null,
							"Es sind keine Personal oder Raeume eingeplant");
					return;
				} else if ((sIList.getDestsize() > 1
						|| scList.getDestsize() > 1 || roomList.getDestsize() > 1)
						&& !bandselect.isSelected()) {
					JOptionPane
							.showMessageDialog(null,
									"Nur Band-Unterricht kann mehrere Stundeninhalte, Klassen und Raeume haben");
					return;
				}
				Planungseinheit p = new Planungseinheit();
				p.setStarthour((int) spinner1.getValue());
				p.setStartminute((int) spinner2.getValue());
				p.setEndhour((int) spinner3.getValue());
				p.setEndminute((int) spinner4.getValue());
				if ((p.getStartHour() == p.getEndhour())
						&& (p.getStartminute() == p.getEndminute())) {
					JOptionPane
							.showMessageDialog(null,
									"Planungseinheit darf nicht gleiche Startzeit und Endzeit haben");
					return;
				} else if ((p.getStartHour() > p.getEndhour())) {
					JOptionPane.showMessageDialog(null,
							"Startzeit muss frueher als Endzeit sein");
					return;
				}
				if (p.getEndhour() == TimetableManager.endTimeHour()
						&& p.getEndminute() > TimetableManager.endTimeMinute()) {
					JOptionPane.showMessageDialog(
							null,
							"Der Tag ist leider um " + p.getEndhour() + ":"
									+ p.getEndminute() + " schon zu Ende.");
					return;
				}
				p.setWeekday((Weekday) tag.getSelectedItem());
				Iterator it = pList.destinationIterator();
				ArrayList<Personal> listp = new ArrayList<Personal>();
				while (it.hasNext()) {
					Personal pr = (Personal) it.next();
					if (PlanungseinheitManager.checkPersonPE(pr, p,
							p.getWeekday())) {
						JOptionPane.showMessageDialog(null,
								"Personal " + pr.getName()
										+ " ist schon zu dieser Zeit gebucht");
						return;
					} else {
						listp.add(pr);
					}
				}
				
				it = sIList.destinationIterator();
				while (it.hasNext()) {
					Stundeninhalt si = (Stundeninhalt) it.next();
					if (si.getRegeldauer() != p.duration()) {
						int result = JOptionPane
								.showOptionDialog(
										null,
										"Dauer von Planungseinheit ist anders als die Regeldauer von "
												+ si.getName()
												+ "\n Dauer von Planungseinheit in min:"
												+ p.duration()
												+ "\n Regeldauer von "
												+ si.getName() + " :"
												+ si.getRegeldauer(),
										"Warnung", 0,
										JOptionPane.YES_NO_OPTION, null,
										options, null);
						if (result == 0) {
							WarningPanel
									.setText("Konflikt : Regeldauer mit Planung von "
											+ si.getName());
						} else {
							return;
						}
					}
					p.addStundeninhalt(si);
				}

				if (scList.getDestsize() == 0 && !teamzeit.isSelected()) {
					JOptionPane.showMessageDialog(null,
							"Nur Teamzeit kann keine Klassen haben");
					return;
				}

				it = scList.destinationIterator();
				while (it.hasNext()) {
					Schoolclass sc = (Schoolclass) it.next();
					if (PlanungseinheitManager.checkScPE(sc, p, p.getWeekday())) {
						JOptionPane.showMessageDialog(null,
								"Klasse " + sc.getName()
										+ " ist schon zu dieser Zeit gebucht");
						return;
					} else {
						p.addSchulklassen(sc);
					}
				}
				it = roomList.destinationIterator();
				while (it.hasNext()) {
					Room r = (Room) it.next();
					if (PlanungseinheitManager.checkRoomPE(r, p, p.getWeekday())) {
						JOptionPane.showMessageDialog(null,
								"Raum " + r.getName()
										+ " ist schon zu dieser Zeit gebucht");
						return;
					} else {
						p.addRoom(r);
					}
				}
				
				

				for(int i = 0; i < listp.size() ; i++) {
					StundenplanTable table = new StundenplanTable(listp.get(i));
					int row;
					int column;
					for(int e = 0; e < table.getTable().getRowCount(); e++) {
						String s ="";
						if(p.getStartHour()< 10) {
							s = s + "0";
						}
						s = s + p.getStartHour() + ":";
						if(p.getStartminute()< 10) {
							s = s + "0";
						}
						s = s + p.getStartminute();
						s = s + " - ";
						
						if(p.getEndhour()< 10) {
							s = s + "0";
							
							
						}
						s = s + p.getEndhour() + ":";
						if(p.getEndminute()< 10) {
							s = s + "0";
						}
						s = s + p.getEndminute();
						
						if(table.getTable().getValueAt(e, 0).toString().equals(s)) {
							//TODO
							
						}
						
						
							
					}
				}
				
				
				
				
				
				
				
				
				
				
				for (Personal pers : listp) {
					if (PlanungseinheitManager.personalsiCheck(pers, p)) {
						int result = JOptionPane.showOptionDialog(
								null,
								"Personal "
										+ pers.getName()
										+ " ist nicht fuer die geplante Stundeninhalte eingetragen. \n Stundeninhalte von Personal : "
										+ pers.getmSI()
										+ " \n Stundeninhalte im Planung "
										+ p.stundenInhaltetoString(),
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							WarningPanel.setText("Konflikt : inkompatibles SI "
									+ pers.getName());
						} else {
							return;
						}
					}

					if (PlanungseinheitManager.personalWZCheck(pers, p)) {
						int result = JOptionPane.showOptionDialog(
								null,
								"Personal "
										+ pers.getName()
										+ " ist zum geplanten Zeitspanne nicht verfügbar",
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							// add Warningpanel
							WarningPanel.setText("Konflikt : Wunschzeit "
									+ pers.getName());
						} else {
							return;
						}
					}

					if (PlanungseinheitManager.overtimePers(pers, p.duration())) {
						int result = JOptionPane.showOptionDialog(
								null,
								"Istzeit von Personal "
										+ pers.getName()
										+ " wird mit der Planung die Sollzeit übersteigen \n Sollzeit :"
										+ pers.getSollZeit()
										+ "\n neue Istzeit :"
										+ PlanungseinheitManager
												.newTimeforPers(
														pers.getIstZeit(),
														p.duration()),
								"Warnung", 0, JOptionPane.YES_NO_OPTION, null,
								options, null);
						if (result == 0) {
							// add Warningpanel
							WarningPanel.setText("Konflikt : Ueberstunden "
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

	private JFrame getmyFrame() {
		return this;
	}
}
