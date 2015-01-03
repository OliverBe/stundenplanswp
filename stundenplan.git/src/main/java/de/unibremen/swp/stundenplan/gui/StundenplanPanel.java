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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class StundenplanPanel extends JPanel {

	public DataPanel data = new DataPanel();

	private JTable table;
	private JFileChooser chooser = new JFileChooser();
	private JFrame f;
	private int eventX;
	private int eventY;

	private int eventXX;
	private int eventYY;
	
	public JLabel warning = new JLabel();
	
	JPopupMenu popmen = new JPopupMenu();
	
	public StundenplanPanel(){
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

		menu = new JMenu("Lehrer");
		menuBar.add(menu);
		
		menu = new JMenu("Klasse");
		menuBar.add(menu);
		
		menuBar.setLayout(new GridLayout(0,1));
		add(menuBar, c);

		String[] columnNames = {
				" Uhrzeiten", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"
		};
		String [][] rowData ={ {"8-9","","","","",""}, {"9-10","","","","",""}, {"10-11","","","","",""}, {"11-12","","","","",""}, {"12-13","","","","",""}, {"13-14","","","","",""},{"14-15","","","","",""},{"16-17","","","","",""}, {"17-18","","","","",""}};
		table = new JTable(rowData, columnNames);
		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 4;
		c.gridy = 0;
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
		

	
	
	
	private void buttonOkay(JButton b) {
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showSaveDialog(f);
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
	
	
}
