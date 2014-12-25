package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListModel;

//Diese Klasse repr�sentiert einen Plan und einem bestimmten Tag im Wochenplan.
public class WochenplanTag extends JPanel {
	
	
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	String[] columnNames = { "8:00 - 9:00", "9:00 - 10:00", "10:00- 11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"};
	String[][] rowData = { {"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""},{"","","","","","","","","",""}};
	public JTable table;
	public JLabel warning = new JLabel();

	private DefaultListModel<String> pListe = new DefaultListModel<String>();
	public WochenplanTag() {
		init();
	}
	
	public void init(){
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		JMenuBar menuBar;
		JMenu menu;
		menuBar = new JMenuBar();
		table = new JTable(rowData, columnNames);
		table.setRowSelectionAllowed(true);
		table.setRowHeight(50);
		JScrollPane pane = new JScrollPane(table);

		
		menu = new JMenu("Personal");
		menuBar.add(menu);
		add(menuBar, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 4;
		c.gridy = 0;
		c.gridx = 1;
		pane.setPreferredSize(new Dimension(500, 350));
		add(pane,c);
		JButton pdf = new JButton("PDF");
		JButton csv = new JButton("CSV");
		JButton text = new JButton("Text");

		c.fill = GridBagConstraints.LAST_LINE_END;
		c.insets = new Insets(5,5,5,5);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Exportieren als:"),c);
		c.gridx = 2;
		add(pdf,c);
		c.gridx = 3;
		add(csv,c);
		c.gridx = 4;
		add(text,c);	
		
		buttonOkay(pdf);
		buttonOkay(csv);
		buttonOkay(text);
		
		warning.setText("Warnungsfeld: Keine Probleme");
		warning.setBackground(Color.GREEN);
		warning.setOpaque(true);
		c.gridy = 3;
		c.gridx = 1;
		c.gridwidth = 4;
		add(warning, c);
		
		
	}

	
	private void buttonOkay(JButton b) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showSaveDialog(f);
			}
		});
	}	
	
}
