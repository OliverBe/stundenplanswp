package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class LehreransichtPanel extends JPanel {

	private JTable table;
	
	
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	
	
	public JLabel warning = new JLabel();
	
	
	public LehreransichtPanel(){
		init();
	}

	
public void init(){
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 4;
		
		String[] columnNames = {
				"", "", "Deu", "E", "MA", "SU/BGU", "KU"
		};
		String [][] rowData ={{"BER -4","28","3B 6","3B 2 \n 2C 1","-","-","1A/2C 2"}, {"BM -28","28","","","","",""}, {"DOR","10","2A 4","-","-","-","2B 6"}
		, {"VID","12","3B 6","-","-","-","4A 6"}
		, {"WAR","20","-","-","3A 10","-","1C 10"}
		, {"BER","8","-","2C 6","-","-","2C 2"}
		, {"HUE","20","1B 2","3B 8","2A 8","1D 2","-"}
		, {"NIE -28","28","","","","",""}
		, {"RUN -10","10","","","","",""}
		, {"ORD","-20","20","","","",""}};
		
		table = new JTable(rowData, columnNames);
		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		JScrollPane pane = new JScrollPane(table);
		add(pane,c);
		
		
		
}
	
}
