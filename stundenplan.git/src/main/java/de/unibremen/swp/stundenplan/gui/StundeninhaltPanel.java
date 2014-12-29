package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StundeninhaltPanel extends JPanel {

	private String name;
	private String Kuerzel;

	Label lName = new Label("Titel der Aktivitaet:");
	Label lKuerzel = new Label("Kuerzel:");
	Label ltime = new Label("Regeldauer in h:");
	Label lPause = new Label("rythmischer Typ:");

	private TextField nameField = new TextField(15);
	private TextField kuerzelField = new TextField(5);
	private TextField timeField = new TextField(5);

	public JButton button = new JButton("Stundeninhalt hinzufuegen");

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel listModel = new DefaultListModel();
	private JList<String> list = new JList<String>(listModel);
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
		p.add(kuerzelField, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(ltime, c);
		c.gridx = 1;
		p.add(timeField, c);

		c.gridx = 0;
		c.gridy = 3;
		p.add(lPause, c);
		c.gridx = 1;
		JRadioButton pauseB = new JRadioButton("Pause");
		pauseB.setSelected(true);
		JRadioButton leichtB = new JRadioButton("Leicht");
		JRadioButton schwerB = new JRadioButton("Schwer");
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
		}
		return b;
	}

}
