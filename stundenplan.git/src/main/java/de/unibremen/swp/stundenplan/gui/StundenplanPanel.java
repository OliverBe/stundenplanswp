package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataSchulklasse;

public class StundenplanPanel extends JPanel implements ActionListener {

	public DataPanel data = new DataPanel();

	private JTable table;
	private JFrame f;
	private int eventX;
	private int eventY;

	private int eventXX;
	private int eventYY;
	
	private static JMenuBar menuBar = new JMenuBar();
	private static DefaultListModel pList;
	private static DefaultListModel sList;
	private static JList personalList;
	private static JList schoolclassList;
	private static JLabel label1 =new JLabel("Lehrer");
	private static JLabel label2 =new JLabel("Klassen");
	
	private static JButton show = new JButton("Anzeigen");
	
	public JLabel warning = new JLabel();
	
	JPopupMenu popmen = new JPopupMenu();
	
	public StundenplanPanel(){
		init();
		show.addActionListener(this);
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
		
		
	
		updateLists();
		
		menuBar.setLayout(new GridLayout(0,1));
		add(menuBar, c);

		table = new JTable (new TimetableModel()); 
		//table = new JTable(rowData, columnNames);
		table.setDefaultRenderer(Timeslot.class, new TimetableRenderer());
		table.setRowSelectionAllowed(true);
		table.setRowHeight(75);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 4;
		c.gridy = 0;
		JScrollPane pane = new JScrollPane(table);
		add(pane,c);
		
		
		
		
		table.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent evt) {
	        	eventX = evt.getXOnScreen();
	        	eventY = evt.getYOnScreen();
	        	eventXX = evt.getX();
	        	eventYY = evt.getY();
	           
	        	if (SwingUtilities.isRightMouseButton(evt)) {
	            	final int row = table.rowAtPoint(evt.getPoint());
	                final int col = table.columnAtPoint(evt.getPoint());
	              createPopup(row, col);
	            }
	        }
	    });
		
        
    }
		

	
	
	
	
	 /**
     * Erzeugt ein Popup zum hinzuf�gen, editieren und l�schen von Lehrern F�chern und Klassen
     * 
     * @param row
     *            Die Zeile.
     * @param col
     *            Die Spalte.
     * @return das neue Popup-Menu
     */
    protected JPopupMenu createPopup(final int row, final int col) {
    	
    	if( popmen.isVisible()) {
    		return popmen;
    	} else {
    	popmen = new JPopupMenu();
        final JMenuItem menu1 = new JMenuItem("Fach hinzuf�gen");
        final JMenuItem menu2 = new JMenuItem("Fach editieren");
        final JMenuItem menu3 = new JMenuItem("Fach entfernen");
        final JMenuItem menu4 = new JMenuItem("Lehrer hinzuf�gen");
        final JMenuItem menu5 = new JMenuItem("Lehrer editieren");
        final JMenuItem menu6 = new JMenuItem("Lehrer entfernen");
        final JMenuItem menu7 = new JMenuItem("Klasse hinzuf�gen");
        final JMenuItem menu8 = new JMenuItem("Klasse editieren");
        final JMenuItem menu9 = new JMenuItem("Klasse entfernen");
        
        
        menu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            KlassenPopupDialog r = new KlassenPopupDialog();
            
            String msg = (String) table.getValueAt(row, col);
            msg = msg + r.doEingabe();
            table.setValueAt(msg, row, col);
            popmen.setVisible(false);
            }
        });
        menu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	
            }
        });
        menu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	String msg = "";
                table.setValueAt(msg, row, col);
                popmen.setVisible(false);
            }
        });
        menu4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
            	KlassenPopupDialog r = new KlassenPopupDialog();
                
                String msg = (String) table.getValueAt(row, col);
                msg = msg + r.doEingabe();
                table.setValueAt(msg, row, col);
                popmen.setVisible(false);
            }
        });
        menu5.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		
        	}          
        });
        
        menu6.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		String msg = "";
                table.setValueAt(msg, row, col);
                popmen.setVisible(false);
        	}          
        });
        
        menu7.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		KlassenPopupDialog r = new KlassenPopupDialog();
                
                String msg = (String) table.getValueAt(row, col);
                msg = msg + r.doEingabe();
                table.setValueAt(msg, row, col);
                popmen.setVisible(false);
        	}          
        });
        
        menu8.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		
        	}          
        });
        
        menu9.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(final ActionEvent event) {
        		String msg = "";
                table.setValueAt(msg, row, col);
                popmen.setVisible(false);
        	}          
        });
        popmen.add(menu1);
        popmen.add(menu2);
        popmen.add(menu3);
        popmen.add(menu4);
        popmen.add(menu5);
        popmen.add(menu6);
        popmen.add(menu7);
        popmen.add(menu8);
        popmen.add(menu9);
        popmen.setLocation(eventX, eventY);
        popmen.setVisible(true);
        return popmen;
    	}
    }
	
	public static void updateLists() {
		Personal[] personalListe = new Personal[DataPersonal.getAllPersonal().size()];
		Schoolclass[] schoolclassListe = new Schoolclass[DataSchulklasse.getAllSchulklasse().size()];
		pList = new DefaultListModel();
		sList = new DefaultListModel();
		
		for(int i=0; i < DataPersonal.getAllPersonal().size(); i++) {
			personalListe[i] = DataPersonal.getAllPersonal().get(i);

			pList.addElement(personalListe[i]);
		}
		
		for(int i=0; i < DataSchulklasse.getAllSchulklasse().size(); i++) {
			schoolclassListe[i] = DataSchulklasse.getAllSchulklasse().get(i);

			sList.addElement(schoolclassListe[i]);
		}
		
		
		personalList = new JList(pList);
		schoolclassList = new JList(sList);
		
		menuBar.removeAll();
		menuBar.add(label1);
		menuBar.add(personalList);
		menuBar.add(label2);
		menuBar.add(schoolclassList);
		menuBar.add(show);
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == show){
			if(!personalList.isSelectionEmpty()) {
				Personal p = (Personal) personalList.getSelectedValue();
				System.out.println(p.getKuerzel());
				
				
			} else if (!schoolclassList.isSelectionEmpty()) {             
				Schoolclass s = (Schoolclass) schoolclassList.getSelectedValue();
				System.out.println(s.getName());
				
			}
			
        }
	}
}
