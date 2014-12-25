package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeleteDialogue extends JFrame {
	
	private JPanel panel = new JPanel();
	public JButton okButton = new JButton("OK");
	public JButton noButton = new JButton("Abbrechen");	
	private GridBagConstraints c = new GridBagConstraints();	
	public DeleteDialogue(){
		
	super("Person löschen");	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	add(panel);
	setSize(400,100);
	setLocation(100,300);
	
	panel.setLayout(new GridBagLayout());
	c.insets=new Insets(1,1,1,1);
	c.anchor=GridBagConstraints.WEST;
	c.gridx=0;
	c.gridy=0;
	JLabel loeschen = new JLabel("Diese Person wirklich löschen?");
	panel.add(loeschen,c);
	
	c.gridx=0;
	c.gridy=1;
	c.gridwidth=2;
	c.fill=GridBagConstraints.HORIZONTAL;
	panel.add(okButton,c);
	
	c.gridx=2;
	c.gridy=1;
	c.gridwidth=2;
	c.fill=GridBagConstraints.HORIZONTAL;
	panel.add(noButton,c);
	buttonOkay(okButton);
	buttonOkay(noButton);


}

private void buttonOkay(JButton b) {
	b.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();	
		}
	});
}	
}