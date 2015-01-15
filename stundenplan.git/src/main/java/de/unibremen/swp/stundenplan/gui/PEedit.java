package de.unibremen.swp.stundenplan.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

public class PEedit extends JFrame {
	  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Config.init(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PEedit frame = new PEedit();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PEedit() {
		super("Planungseinheiten bearbeiten");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new SpringLayout());
		JLabel lLabel1 = new JLabel("Startzeit : Stunde - Minute");
		getContentPane().add(lLabel1);
		JLabel lLabel2 = new JLabel("Endzeit  : Stunde - Minute");
		getContentPane().add(lLabel2);
		JPanel starttime = new JPanel();
		JPanel endtime = new JPanel();
		SpinnerModel hourmodel = new SpinnerNumberModel(TimetableManager.startTimeHour() , TimetableManager.startTimeHour(),TimetableManager.endTimeHour(),1);
		SpinnerModel ehourmodel = new SpinnerNumberModel(TimetableManager.startTimeHour() , TimetableManager.startTimeHour(),TimetableManager.endTimeHour(),1);
		SpinnerModel minmodel = new SpinnerNumberModel(0 ,0,59,Timeslot.timeslotlength());
		SpinnerModel eminmodel = new SpinnerNumberModel(0 ,0,59,Timeslot.timeslotlength());
		JSpinner spinner1 = new JSpinner(hourmodel);
		starttime.add(spinner1);
		JSpinner spinner2 = new JSpinner(minmodel);
		starttime.add(spinner2);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spinner2.getEditor()).getTextField();
	    tf.setEditable(false);
		JSpinner spinner3 = new JSpinner(ehourmodel);
		endtime.add(spinner3);
		JSpinner spinner4 = new JSpinner(eminmodel);
		endtime.add(spinner4);
		tf = ((JSpinner.DefaultEditor) spinner4.getEditor()).getTextField();
	    tf.setEditable(false);
	    getContentPane().add(starttime);
	    getContentPane().add(endtime);
	    DualListBox pList = new DualListBox("Alle Lehrer", " Lehrer im Planungseinheit");
	    pList.addSourceElements(DataPersonal.getAllAcronymsFromPersonal().toArray());
	    getContentPane().add(pList);
	    DualListBox sIList = new DualListBox("Verfuegbare Stundeninhalte", " Stundeninhalte im Planungseinheit");
	    sIList.addSourceElements(DataStundeninhalt.getAllAcronymsFromStundeninhalt().toArray());
	    getContentPane().add(sIList);
	    DualListBox scList = new DualListBox("Alle Lehrer", " Lehrer im Planungseinheit");
	    scList.addSourceElements(DataSchulklasse.getAllNameFromSchulklasse().toArray());
	    getContentPane().add(scList);
	    DualListBox roomList = new DualListBox("Verfuegbare Stundeninhalte", " Stundeninhalte im Planungseinheit");
	    roomList.addSourceElements(DataRaum.getAllNameFromRaum().toArray());
	    getContentPane().add(roomList);
	    JLabel label = new JLabel("Hier koennen die Planungseinheiten bearbeitet werden");
		JButton button = new JButton("Planungseinheiten speichern");
	    getContentPane().add(label);
	    getContentPane().add(button);
	    SpringUtilities.makeCompactGrid(getContentPane(),
                5, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
	    setSize(1000, 700);
	}
}
