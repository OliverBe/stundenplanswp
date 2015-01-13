package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class DeleteDialogue extends JFrame {
	
	private JPanel panel = new JPanel();
	public JButton okButton = new JButton("Ja");
	public JButton noButton = new JButton("Nein");	
	private GridBagConstraints c = new GridBagConstraints();	
	private Object o;
		
	public DeleteDialogue(final Object pO){
		super("Löschen");
		o=pO;
		init();
		pack();
	}
	private void init(){
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Loeschen"));
		c.insets=new Insets(20,5,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		JLabel loeschen = new JLabel("Möchten Sie wirklich loeschen?");
		panel.add(loeschen,c);
		
		c.gridx=0;
		c.gridy=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		panel.add(okButton,c);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if(o instanceof Raumfunktion) {
					DataRaum.deleteRaumfunktionByName(((Raumfunktion) o).getName());
					RaumfunktionPanel.updateList();
				}
				if(o instanceof Stundeninhalt) {
					StundeninhaltManager.deleteStundeninhaltFromDB(((Stundeninhalt) o).getKuerzel());
					StundeninhaltPanel.updateList();
				}
				if(o instanceof Schoolclass) {
					DataSchulklasse.deleteSchulklasseByName(((Schoolclass) o).getName());
					SchoolclassPanel.updateList();
				}
				if(o instanceof Room) {
					DataRaum.deleteRaumByName(((Room) o).getName());
					RoomPanel.updateList();
				}
				if(o instanceof Personal) {
					DataPersonal.deletePersonalByKuerzel(((Personal) o).getKuerzel());
					PersonalPanel.updateList();
				}
				if(o instanceof Jahrgang) {
					Entry<String, Integer> ent = ((Jahrgang) o).getStundenbedarf().entrySet().iterator()
							.next();
					DataSchulklasse.deleteJahrgangbedarfByJAndSkuerzel(((Jahrgang)o).getJahrgang(),ent.getKey());
					BedarfPanel.updateList();
				}
				dispose();	
			}
		});		
		c.gridx=1;
		panel.add(noButton,c);
		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});	
		
		add(panel);
	}		
}