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

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.logic.JahrgangsManager;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.RaumManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

/**
 * Repräsentiert das Pop-Up-Fenster zum Loeschen von Daten
 * 
 * @author Oliver
 */
@SuppressWarnings("serial")
public class DeleteDialogue extends JFrame {
	
	/**
	 * Panel fuers Fenster
	 */
	private JPanel panel;
	
	/**
	 * Ja/Bestaetigungs Button
	 */
	public JButton okButton;
	
	/**
	 * Nein/Abbruchbutton
	 */
	public JButton noButton;	
	
	/**
	 * Uebergebenes Objekt, welches geloescht werden soll
	 */
	private Object o;
		
	/**
	 * Konstruktor des DeletePopUpFensters
	 * @param pO
	 */
	public DeleteDialogue(final Object pO){
		super("Löschen");
		o=pO;
		init();
		pack();
	}
	private void init(){
		panel=new JPanel();
		okButton = new JButton("Ja");
		noButton = new JButton("Nein");
		GridBagConstraints c = new GridBagConstraints();	
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Loeschen"));
		c.insets=new Insets(20,5,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		JLabel loeschen = new JLabel("Möchten Sie [ "+o.toString() +" ] wirklich loeschen?");
		panel.add(loeschen,c);
		
		c.gridx=0;
		c.gridy=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		panel.add(okButton,c);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if(o instanceof Raumfunktion) {
					RaumManager.deleteRaumfunktionFromDB(((Raumfunktion) o).getName());
					RaumfunktionPanel.updateList();
				}
				if(o instanceof Stundeninhalt) {
					StundeninhaltManager.deleteStundeninhaltFromDB(((Stundeninhalt) o).getKuerzel());
					StundeninhaltPanel.updateList();
				}
				if(o instanceof Schoolclass) {
					SchulklassenManager.deleteSchulklasseFromDB(((Schoolclass) o).getName());
					SchoolclassPanel.updateList();
				}
				if(o instanceof Room) {
					RaumManager.deleteRoomFromDB(((Room) o).getName());
					RoomPanel.updateList();
				}
				if(o instanceof Personal) {
					PersonalManager.deletePersonalFromDB(((Personal) o).getKuerzel());
					PersonalPanel.updateList();
				}
				if(o instanceof Jahrgang) {
					Entry<String, Integer> ent = ((Jahrgang) o).getStundenbedarf().entrySet().iterator()
							.next();
					JahrgangsManager.deleteBedarfFromJahrgang(((Jahrgang)o),ent.getKey());
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