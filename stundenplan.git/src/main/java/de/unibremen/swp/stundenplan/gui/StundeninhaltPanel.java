package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Jahrgang;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;

public class StundeninhaltPanel extends JPanel {

	Label lName = new Label("Titel der Aktivitaet:");
	Label lKuerzel = new Label("Kuerzel:");
	Label ltime = new Label("Regeldauer in min:");
	Label lPause = new Label("rythmischer Typ:");

	private TextField nameField = new TextField(15);
	private TextField kuerzField = new TextField(5);
	private TextField timeField = new TextField(5);

	public JButton button = new JButton("Stundeninhalt hinzufuegen");

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel<Stundeninhalt> listModel = new DefaultListModel<Stundeninhalt>();
	private JList<Stundeninhalt> list = new JList<Stundeninhalt>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	public StundeninhaltPanel() {
		setLayout(new GridBagLayout());
		setLayout(new GridBagLayout());
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()), c2);
		c2.gridy = 2;
		add(createListPanel(new JPanel()), c2);
	}

	private JPanel createAddPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Neuen Stundeninhalt hinzufuegen"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName, c);
		c.gridx = 1;
		p.add(nameField, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(lKuerzel, c);
		c.gridx = 1;
		p.add(kuerzField, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(ltime, c);
		c.gridx = 1;
		p.add(timeField, c);

		c.gridx = 0;
		c.gridy = 3;
		p.add(lPause, c);
		c.gridx = 1;
		final JRadioButton pauseB = new JRadioButton("Pause");
		pauseB.setSelected(true);
		final JRadioButton leichtB = new JRadioButton("Leicht");
		final JRadioButton schwerB = new JRadioButton("Schwer");
		ButtonGroup group = new ButtonGroup();
		group.add(pauseB);
		group.add(leichtB);
		group.add(schwerB);
		p.add(pauseB, c);
		c.gridy = 4;
		p.add(leichtB, c);
		c.gridy = 5;
		p.add(schwerB, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);
		
		// add Button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int rythm=0;
					if (!check(p))
						throw new WrongInputException();
					if(pauseB.isSelected()) rythm=0;
					if(leichtB.isSelected()) rythm=1;
					if(schwerB.isSelected()) rythm=2;
					
					Stundeninhalt si = new Stundeninhalt(nameField.getText(), 
							kuerzField.getText(), 
							Integer.parseInt(timeField.getText()),
							rythm);

					DataStundeninhalt.addStundeninhalt(si);

					updateList();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
		return p;

	}

	private JPanel createListPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Existierende Stundeninhalte"));

		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroller.setPreferredSize(new Dimension(250, 200));

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.8;
		c.weighty = 1.0;
		p.add(listScroller, c);	
		
		updateList();	
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				final DataPopup pop = new DataPopup(list, listModel);
				setComponentPopupMenu(pop);
				list.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.isMetaDown()) {
							pop.show(list, e.getX(), e.getY());
						}
					}
				});
				pop.edit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame edit = new JFrame("Bedarf editieren");
						edit.add(createEditPanel(new JPanel(),list.getSelectedValue()));
						edit.pack();
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(list.getSelectedValue());
						deleteD.setVisible(true);
					}
				});
			}
		});

		return p;
	}
	
	private JPanel createEditPanel(final JPanel p, final Stundeninhalt si){
		System.out.println("Rythm"+ si.getRhythmustyp());
		
		Label lName2 = new Label("Titel der Aktivitaet:");
		Label lKuerzel2 = new Label("Kuerzel:");
		Label ltime2 = new Label("Regeldauer in min:");
		Label lPause2 = new Label("rythmischer Typ:");

		TextField nameField2 = new TextField(15);
		TextField kuerzField2 = new TextField(5);
		TextField timeField2 = new TextField(5);

		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");
		
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Stundeninhalt editieren"));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName2, c);
		c.gridx = 1;
		p.add(nameField2, c);
		nameField2.setText(si.getName());

		c.gridx = 0;
		c.gridy = 1;
		p.add(lKuerzel2, c);
		c.gridx = 1;
		p.add(kuerzField2, c);
		kuerzField2.setText(si.getKuerzel());

		c.gridx = 0;
		c.gridy = 2;
		p.add(ltime2, c);
		c.gridx = 1;
		p.add(timeField2, c);
		timeField2.setText(si.getRegeldauer()+"");

		c.gridx = 0;
		c.gridy = 3;
		p.add(lPause2, c);
		c.gridx = 1;
		final JRadioButton pauseB2 = new JRadioButton("Pause");
		final JRadioButton leichtB2 = new JRadioButton("Leicht");
		final JRadioButton schwerB2 = new JRadioButton("Schwer");
		switch (si.getRhythmustyp()) {
        case 0:  pauseB2.setSelected(true);
                 break;
        case 1:  leichtB2.setSelected(true);
                 break;
        case 2:  schwerB2.setSelected(true);
                 break;
		}
		ButtonGroup group2 = new ButtonGroup();
		group2.add(pauseB2);
		group2.add(leichtB2);
		group2.add(schwerB2);
		p.add(pauseB2, c);
		c.gridy = 4;
		p.add(leichtB2, c);
		c.gridy = 5;
		p.add(schwerB2, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);
		
		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					int rythm=0;
					if (!check(p))
						throw new WrongInputException();
					if(pauseB2.isSelected()) rythm=0;
					if(leichtB2.isSelected()) rythm=1;
					if(schwerB2.isSelected()) rythm=2;
					
					Stundeninhalt si = new Stundeninhalt(nameField2.getText(), 
							kuerzField2.getText(), 
							Integer.parseInt(timeField2.getText()),
							rythm);

					DataStundeninhalt.addStundeninhalt(si);

					updateList();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
		
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
				topFrame.dispose();
			}
		});
		
		return p;
	}

	private boolean check(final JPanel p) {
		if (textFieldsEmpty(p))
			return false;
		try {
			Integer.parseInt(timeField.getText());
		} catch (NumberFormatException e) {
			return false;
		}

		return true;

	}

	private boolean textFieldsEmpty(final JPanel p){
		boolean b=true;
		for(Component c : p.getComponents()){
			if(c instanceof TextField){
				TextField tf = (TextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
			if(c instanceof JTextField ){
				JTextField tf = (JTextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
		}
		return b;
	}

	public static void updateList(){
		listModel.clear();
		for (Stundeninhalt sti : DataStundeninhalt.getAllStundeninhalte()) {
			listModel.addElement(sti);
		}
	}

}
