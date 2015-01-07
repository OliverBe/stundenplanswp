package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;

public class RaumfunktionPanel extends JPanel {
	private Label lName = new Label("Name der Funktion");
	private Label lStdi = new Label("Mögliche Stundeninhalte");
	private JTextField tf = new JTextField(20);
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	private JButton button = new JButton("Funktion hinzufuegen");

	private static DefaultListModel<Raumfunktion> listModel = new DefaultListModel<Raumfunktion>();
	private JList<Raumfunktion> list = new JList<Raumfunktion>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	public RaumfunktionPanel() {
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
		p.setBorder(BorderFactory.createTitledBorder("Funktionen von Raeumen "));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName, c);
		c.gridx = 1;
		p.add(tf, c);
		c.gridx = 0;
		c.gridy = 1;
		p.add(lStdi, c);

		c.gridx = 1;
		CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		for (Stundeninhalt s : DataStundeninhalt.getAllStundeninhalte()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		};
		checkList.setListData(boxes.toArray());
		p.add(checkList, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Raumfunktion rf;
				try {
					if (textFieldsEmpty(p))
						throw new WrongInputException();

					ArrayList<String> stdi = new ArrayList<String>();
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
						if(cb.isSelected()) stdi.add(cb.getText());
					}

					rf = new Raumfunktion(tf.getText(), stdi);
					DataRaum.addRaumfunktion(rf);

					updateList();

				} catch (WrongInputException e) {
					System.out.println("ERROR beim Hinzufuegen");
					e.printStackTrace();
				}
			}
		});

		return p;
	}

	private JPanel createListPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Existierende Raumfunktionen"));

		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroller.setPreferredSize(new Dimension(250, 200));

		updateList();

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.8;
		c.weighty = 1.0;
		p.add(listScroller, c);

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
						JFrame edit = new JFrame("Raumfunktion editieren");
						edit.add(createEditPanel(new JPanel(),
								list.getSelectedValue()));
						edit.pack();
						edit.setLocation(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y);
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(list.getSelectedValue());
						deleteD.setLocation(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y);
						deleteD.setVisible(true);	
					}
				});
			}
		});

		return p;
	}

	private JPanel createEditPanel(final JPanel p, final Raumfunktion rf) {
		Label lName2 = new Label("Name der Funktion");
		Label lStdi2 = new Label("Moegliche Stundeninhalte");
		JTextField tf2 = new JTextField(20);
		JButton button2 = new JButton("Funktion editieren");
		JButton button3 = new JButton("Abbrechen");

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Funktionen von Raeumen "));
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName2, c);
		c.gridx = 1;
		p.add(tf2, c);
		tf2.setText(rf.getName());
		c.gridx = 0;
		c.gridy = 1;
		p.add(lStdi2, c);

		c.gridx = 1;
		final CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		for (Stundeninhalt s : DataStundeninhalt.getAllStundeninhalte()) {
			boxes.add(new JCheckBox(s.getKuerzel()));
		};
		for(JCheckBox jcb : boxes){
			for(String s : rf.getStundeninhalte()){
				if(jcb.getText().equals(s)) jcb.setSelected(true);
			}	
		}	
		checkList.setListData(boxes.toArray());
		p.add(checkList, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(button2, c);
		
		// edit
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Raumfunktion rf2;
				try {
					if (textFieldsEmpty(p))
						throw new WrongInputException();

					ArrayList<String> stdi = new ArrayList<String>();
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
						if(cb.isSelected()) stdi.add(cb.getText());
					}

					rf2 = new Raumfunktion(tf2.getText(), stdi);
					DataRaum.editRaumfunktion(rf.getName(), rf2);
					updateList();				
					JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
					topFrame.dispose();

				} catch (WrongInputException e) {
					System.out.println("ERROR beim Editieren");
					e.printStackTrace();
				}
			}
		});
		c.gridx = 1;
		p.add(button3, c);

		// abbruch Button
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
				topFrame.dispose();
			}
		});
		return p;
	}

	private boolean textFieldsEmpty(final JPanel p) {
		boolean b = true;
		for (Component c : p.getComponents()) {
			if (c instanceof TextField) {
				TextField tf = (TextField) c;
				if (!tf.getText().isEmpty())
					b = false;
			}
			if (c instanceof JTextField) {
				JTextField tf = (JTextField) c;
				if (!tf.getText().isEmpty())
					b = false;
			}
		}
		return b;
	}
	
	public static void updateList(){
		listModel.clear();
		for (Raumfunktion r : DataRaum.getAllRaumfunktion()) {
			listModel.addElement(r);
		}
	}
}