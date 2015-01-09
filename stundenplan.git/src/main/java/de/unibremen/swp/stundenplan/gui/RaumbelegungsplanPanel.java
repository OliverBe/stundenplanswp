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
import javax.swing.DefaultListModel;
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

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataRaum;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class RaumbelegungsplanPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 201806456859333146L;
	public DataPanel data = new DataPanel();
	private JList<String> liste;
	
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;

	private static JTable table  = null;
	
	private static JMenuBar menuBar = new JMenuBar();
	private static DefaultListModel rList;
	private static JList roomList;
	private static JLabel label1 =new JLabel("Räume");
	
	private static JButton show = new JButton("Anzeigen");
	
	
	public JLabel warning = new JLabel();
	
	public RaumbelegungsplanPanel(){
		init();

		show.addActionListener(this);
	}
	
	public void init(){
		
		removeAll();
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx=0;
		c.gridy=0;
		
		updateLists();
		
		menuBar.setLayout(new GridLayout(0,1));
		add(menuBar, c);
		
		
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 4;
		JScrollPane pane = new JScrollPane(table);
		add(pane,c);
		
		
	}
	
	public static void updateLists() {
		Room[] raumListe = new Room[DataRaum.getAllRaum().size()];
		rList = new DefaultListModel();
		
		
		for(int i=0; i < DataRaum.getAllRaum().size(); i++) {
			raumListe[i] = DataRaum.getAllRaum().get(i);

			rList.addElement(raumListe[i]);
		}
		
		
		
		
		roomList = new JList(rList);
		
		menuBar.removeAll();
		menuBar.add(label1);
		menuBar.add(roomList);
		
		menuBar.add(show);
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == show){
			if(!roomList.isSelectionEmpty()) {
				Room r = (Room) roomList.getSelectedValue();
				System.out.println(r.getName());
				table = new StundenplanTable(r).getTable();
				init();
				
				
			} 
			
        }
	}
	

	
	
}
