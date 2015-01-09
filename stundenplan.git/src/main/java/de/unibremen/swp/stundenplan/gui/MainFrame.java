package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuBar menu = new MenuBar(this);

	private JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP);

	private DataPanel dataPanel = new DataPanel();
	private StundenplanPanel paneStundenplan = new StundenplanPanel();
	private LehreransichtPanel paneLehrer = new LehreransichtPanel();
	private JPanel paneRaeume = new RaumbelegungsplanPanel();
	private JPanel paneConfig = new ConfigPanel();
	private WochenplanPanel paneWochen = new WochenplanPanel();
	private static WarningPanel paneWarning = new WarningPanel();

	public MainFrame() {
		super("GUI-Prototype");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents();
		pack();
		setVisible(true);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	private void initComponents() {

		// TODO bei click sollen sich die anderen closen, bzw der eine �ffnen
		tabpane.addTab("Daten", dataPanel);
		tabpane.addTab("Stundenpläne", paneStundenplan);
		tabpane.addTab("Lehreransicht", paneLehrer);
		tabpane.addTab("Raumbelegungsplan", paneRaeume);
		tabpane.addTab("Wochenplan", paneWochen);
		tabpane.addTab("Einstellungen", paneConfig);

		setJMenuBar(menu);
		add(tabpane);
		add(paneWarning, BorderLayout.SOUTH);
	};

	public static WarningPanel getWarning() {
		return paneWarning;
	}
	
	public void updateLehrerPanel(){
		JButton btnExample = new JButton("press me");
        btnExample.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                paneLehrer.removeAll();
                paneLehrer.init();
            }
           
        });
        tabpane.setComponentAt(2, btnExample);
	}
}
