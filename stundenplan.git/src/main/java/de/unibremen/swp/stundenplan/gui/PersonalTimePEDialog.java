package de.unibremen.swp.stundenplan.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JDialog;
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
		for (Personal p : pList) {
			if (PlanungseinheitManager.checkTimeInPE(pe,
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
			if (PlanungseinheitManager.checkTimeInPE(pe,
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
			if (((int) shspinner[i].getValue() == (int) ehspinner[i].getValue())
					&& ((int) smspinner[i].getValue() == (int) emspinner[i]
							.getValue())) {
				JOptionPane.showMessageDialog(null, "Das Personal (" + p.getName()
						+ ") darf nicht die gleiche Start- und Endzeit haben");
				return;
			} else if ((int) shspinner[i].getValue() > (int) ehspinner[i]
					.getValue()) {
				JOptionPane.showMessageDialog(null,
						"Die Startzeit von (" + p.getName()
								+ ") muss vir dessen Endzeit beginnen");
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
		if (!isonefulltime()) {
			JOptionPane
					.showMessageDialog(
							null,
							"Mindestens ein Personal muss in der gesamten Dauer der Planungseinheit sein.");
			return;
		}
		PlanungseinheitManager.addPlanungseinheitToDB(pe);
		saved = true;
		dispose();
	}

	public boolean getsaved() {
		return saved;
	}

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

	private String[] giveLabelforPersonal() {
		String[] labels = new String[pList.size()];
		int i = 0;
		for (Personal p : pList) {
			labels[i] = p.getName() + " : " + p.getKuerzel();
			i++;
		}
		return labels;
	}

	private JPanel[] givestartTimePanelforPersonal() {
		JPanel[] panels = new JPanel[pList.size()];
		int i = 0;
		for (Personal p : pList) {
			panels[i] = new JPanel();
			SpinnerModel hourmodel = new SpinnerNumberModel(pe.getStartHour(),
					pe.getStartHour(), pe.getEndhour(), 1);
			SpinnerModel minmodel = new SpinnerNumberModel(pe.getStartminute(), 0, 59,
					Timeslot.timeslotlength());
			shspinner[i] = new JSpinner(hourmodel);
			panels[i].add(shspinner[i]);
			smspinner[i] = new JSpinner(minmodel);
			panels[i].add(smspinner[i]);
			i++;
		}
		return panels;
	}

	private JPanel[] giveendTimePanelforPersonal() {
		JPanel[] panels = new JPanel[pList.size()];
		int i = 0;
		for (Personal p : pList) {
			panels[i] = new JPanel();
			SpinnerModel hourmodel = new SpinnerNumberModel(pe.getEndhour(),
					pe.getStartHour(), pe.getEndhour(), 1);
			SpinnerModel minmodel = new SpinnerNumberModel(pe.getEndminute(), 0, 59,
					Timeslot.timeslotlength());
			ehspinner[i] = new JSpinner(hourmodel);
			panels[i].add(ehspinner[i]);
			emspinner[i] = new JSpinner(minmodel);
			panels[i].add(emspinner[i]);
			i++;
		}
		return panels;
	}

}
