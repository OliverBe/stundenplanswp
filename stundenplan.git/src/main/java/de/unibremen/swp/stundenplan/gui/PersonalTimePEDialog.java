package de.unibremen.swp.stundenplan.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;

/**
 * Dialog um die Zeiten der Personal einzustellen
 * 
 * @author Fathan
 *
 */
public class PersonalTimePEDialog extends JDialog implements ActionListener {

	private Planungseinheit pe;
	private ArrayList<Personal> pList;
	private JSpinner[] shspinner;
	private JSpinner[] ehspinner;
	private JSpinner[] smspinner;
	private JSpinner[] emspinner;
	private boolean saved = false;

	public PersonalTimePEDialog(final JFrame parent,
			final ArrayList<Personal> perList, final Planungseinheit pPE) {
		super(parent, "Zeiten des Personals in der Planungseinheit", true);
		pList = perList;
		int lsize = perList.size();
		pe = pPE;
		shspinner = new JSpinner[lsize];
		ehspinner = new JSpinner[lsize];
		smspinner = new JSpinner[lsize];
		emspinner = new JSpinner[lsize];
		setLayout(new SpringLayout());
		getContentPane().add(new JLabel("Personal in der Planungseinheit"));
		getContentPane().add(new JLabel("Startzeit in der Planungseinheit"));
		getContentPane().add(new JLabel("Endzeit in der Planungseinheit"));
		String[] labels = giveLabelforPersonal();
		JPanel[] startpanels = givestartTimePanelforPersonal();
		JPanel[] endpanels = giveendTimePanelforPersonal();
		int i = 0;
		for (String s : labels) {
			getContentPane().add(new JLabel(labels[i]));
			getContentPane().add(startpanels[i]);
			getContentPane().add(endpanels[i]);
			i++;
		}
		JLabel explanation = new JLabel("Hier legen sie die Zeiten des ");
		JLabel explanation2 = new JLabel("Personals in der Planungseinheit fest");
		JButton button = new JButton("Speichern");
		button.addActionListener(this);
		getContentPane().add(explanation);
		getContentPane().add(explanation2);
		getContentPane().add(button);
		SpringUtilities.makeCompactGrid(getContentPane(), perList.size() + 2,
				3, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int i = 0;
		pe.getPersonalMap().clear();
		for (Personal p : pList) {
			if (PlanungseinheitManager.checkTimeInPE(pe, //prueft ob indiv. Zeit mit PE passt
					(int) shspinner[i].getValue(),
					(int) smspinner[i].getValue())) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die Startzeit von "
										+ p.getName()
										+ " befindet sich nicht in der Startzeit der Planungseinheit");
				return;
			}
			if (PlanungseinheitManager.checkTimeInPE(pe,//prueft ob indiv. Zeit mit PE passt
					(int) ehspinner[i].getValue(),
					(int) emspinner[i].getValue())) {
				JOptionPane
						.showMessageDialog(
								null,
								"Die Endzeit von "
										+ p.getName()
										+ " befindet sich nicht in der Endzeit der Planungseinheit");
				return;
			}
			if (((int) shspinner[i].getValue() == (int) ehspinner[i].getValue())//prueft ob indiv. Zeit 
					&& ((int) smspinner[i].getValue() == (int) emspinner[i]		//nicht selbe Start und Endzeit haben
							.getValue())) {
				JOptionPane.showMessageDialog(null, "Das Personal (" + p.getName()
						+ ") darf nicht die gleiche Start- und Endzeit haben");
				return;
			} else if ((int) shspinner[i].getValue() > (int) ehspinner[i]//Startzeit soll vor Endzeit sein
					.getValue()) {
				JOptionPane.showMessageDialog(null,
						"Die Startzeit von (" + p.getName()
								+ ") muss vor dessen Endzeit beginnen");
				return;
			}
			pe.addPersonal(
					p,
					new int[] { (int) shspinner[i].getValue(),
							(int) smspinner[i].getValue(),
							(int) ehspinner[i].getValue(),
							(int) emspinner[i].getValue() });
			i++;
		}
		if (!isonefulltime()) {//prueft ob mind ein Personal da ist
			JOptionPane
					.showMessageDialog(
							null,
							"Mindestens ein Personal muss in der gesamten Dauer der Planungseinheit sein.");
			return;
		}
		System.out.print("pID"+pe.getId());
		if(pe.getId()!= -1){
			PlanungseinheitManager.editPlanungseinheit(pe.getId(), pe);
		}else{
		   PlanungseinheitManager.addPlanungseinheitToDB(pe);
		}
		saved = true;
		dispose();
	}

	public boolean getsaved() {
		return saved;
	}
    
	/**
	 * prueft ob mind eine Person im gesamten PE da ist
	 * @return true falls einer durchgehend da ist
	 */
	private boolean isonefulltime() {
		Iterator it = pe.getPersonalMap().values().iterator();
		while (it.hasNext()) {
			int[] time = (int[]) it.next();
			if ((time[0] == pe.getStartHour() && time[1] == pe.getStartminute())
					&& (time[2] == pe.getEndhour()
					&& time[3] == pe.getEndminute())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * gibt eine Liste alle Personal
	 * @return gibt eine Array zurueck mit Strings fuer alle Personal im PE
	 */
	private String[] giveLabelforPersonal() {
		String[] labels = new String[pList.size()];
		int i = 0;
		for (Personal p : pList) {
			labels[i] = p.getName() + " : " + p.getKuerzel();
			i++;
		}
		return labels;
	}
    
	/**
	 * erzeugt startzeitspinner fuer alle PErsonal im PE 
	 * @return Array von Panel fuer Startzeit
	 */
	private JPanel[] givestartTimePanelforPersonal() {
		JPanel[] panels = new JPanel[pList.size()];
		int i = 0;
		int sh;
		int sm;
		JFormattedTextField tf; 
		for (Personal p : pList) {
			int[] times = pe.getTimesofPersonal(p);
			if(pe.getTimesofPersonal(p)!= null && times[0] <=pe.getStartHour() && times[0] >=pe.getEndhour()){
				sh = times[0];
				sm = times[1];
			}else{
				sh = pe.getStartHour();
				sm = pe.getStartminute();
			}
			panels[i] = new JPanel();
			SpinnerModel hourmodel = new SpinnerNumberModel(sh,
					pe.getStartHour(), pe.getEndhour(), 1);
			SpinnerModel minmodel = new SpinnerNumberModel(sm, 0, 59,
					Timeslot.timeslotlength());
			shspinner[i] = new JSpinner(hourmodel);
			panels[i].add(shspinner[i]);
			smspinner[i] = new JSpinner(minmodel);
			tf = ((JSpinner.DefaultEditor) smspinner[i].getEditor())
					.getTextField();
			tf.setEditable(false);
			panels[i].add(smspinner[i]);
			i++;
		}
		return panels;
	}
	
	/**
	 * erzeugt endzeitspinner fuer alle PErsonal im PE 
	 * @return Array von Panel fuer Endzeit
	 */
	private JPanel[] giveendTimePanelforPersonal() {
		JPanel[] panels = new JPanel[pList.size()];
		int i = 0;
		int eh;
		int em;
		JFormattedTextField tf;
		for (Personal p : pList) {
			int[] times = pe.getTimesofPersonal(p);
			if(pe.getTimesofPersonal(p)!= null && times[2] >=pe.getStartHour() &&times[2] <=pe.getEndhour()){
				eh = times[2];
				em = times[3];
			}else{
				eh = pe.getEndhour();
				em = pe.getEndminute();
			}
			panels[i] = new JPanel();
			SpinnerModel hourmodel = new SpinnerNumberModel(eh,
					pe.getStartHour(), pe.getEndhour(), 1);
			SpinnerModel minmodel = new SpinnerNumberModel(em, 0, 59,
					Timeslot.timeslotlength());
			ehspinner[i] = new JSpinner(hourmodel);
			panels[i].add(ehspinner[i]);
			emspinner[i] = new JSpinner(minmodel);
			tf = ((JSpinner.DefaultEditor) emspinner[i].getEditor())
					.getTextField();
			tf.setEditable(false);
			panels[i].add(emspinner[i]);
			i++;
		}
		return panels;
	}

}
