package gui;

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
		
		JButton pdf = new JButton("PDF");
		JButton csv = new JButton("CSV");
		JButton text = new JButton("Text");
		
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(new JLabel("Exportieren als:"),c);
		c.gridx = 1;
		add(pdf,c);
		c.gridx = 2;
		add(csv,c);
		c.gridx = 3;
		add(text,c);	
		
		buttonOkay(pdf);
		buttonOkay(csv);
		buttonOkay(text);
		
		warning.setText("Warnungsfeld: Keine Probleme");
		warning.setBackground(Color.GREEN);
		warning.setOpaque(true);
		c.gridy = 2;
		c.gridx = 0;
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
