package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class RaumbelegungsplanPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201806456859333146L;
	public DataPanel data = new DataPanel();
	private JList<String> liste;
	
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;

	private static SchoolclassListModel schoolclassListModel = new SchoolclassListModel();
	private JTable table;
	
	public JLabel warning = new JLabel();
	
	public RaumbelegungsplanPanel(){
		init();
	}
	
	public void init(){
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx=0;
		c.gridy=0;
		
		JMenuBar menuBar;
		JMenu menu;
		
		menuBar = new JMenuBar();

		menu = new JMenu("Raeume");
		menuBar.add(menu);
		menuBar.setLayout(new GridLayout(0,1));
		add(menuBar, c);
		
		String[] columnNames = {
				"Uhrzeit", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"
		};
		String [][] rowData ={ {"8-9","1a VID","1a HUE","4b BEL","3b NGY","2c SCH"}, {"9-10","","","4b WAR","",""}, {"10-11","","","1b GOR","",""}, {"11-12","","3c SCH","","",""}, {"12-13","","","","",""}, {"13-14","","","","",""},{"14-15","","","","",""},{"16-17","","","","",""}, {"17-18","","","","",""}};
		table = new JTable(rowData, columnNames);
		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 4;
		JScrollPane pane = new JScrollPane(table);
		add(pane,c);
		
		JButton pdf = new JButton("PDF");
		JButton csv = new JButton("CSV");
		JButton text = new JButton("Text");
		
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
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
		c.gridy = 2;
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
