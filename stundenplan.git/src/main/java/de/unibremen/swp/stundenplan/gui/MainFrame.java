package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuBar menu = new MenuBar(this);

	private static JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP);

	private DataPanel dataPanel = new DataPanel();
	private StundenplanPanel paneStundenplan = new StundenplanPanel();
	private LehreransichtPanel paneLehrer = new LehreransichtPanel();
	private JPanel paneRaeume = new RaumbelegungsplanPanel();
	private JPanel paneConfig = new ConfigPanel();
	private WochenplanPanel paneWochen = new WochenplanPanel();
	private static WarningPanel paneWarning = new WarningPanel();

	public MainFrame() {
		super("StundenplanTool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents();
		pack();
		setSize(1280, 1024);
	//	setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		
		setVisible(true);
	}

	private void initComponents() {

		// TODO bei click sollen sich die anderen closen, bzw der eine �ffnen
		tabpane.addTab("Daten", dataPanel);
		tabpane.addTab("Stundenplaene", paneStundenplan);
		tabpane.addTab("Lehreransicht", paneLehrer);
		tabpane.addTab("Raumbelegungsplan", paneRaeume);
		tabpane.addTab("Wochenplan", paneWochen);
		tabpane.addTab("Einstellungen", paneConfig);

		ImageIcon revert = new ImageIcon(getClass().getResource("revert.png"));
		JButton button1 = new JButton(revert);
		add(button1, BorderLayout.EAST);
		button1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					CommandHistory.getLast().undo();
					CommandHistory.deleteLast();
					checkSelectedTab();
				}catch (StundenplanException e){
					System.out.println("[COMMANDHISTORY]: Keine Befehle in History.");
				}
			}});
		
		setJMenuBar(menu);
		add(tabpane);
		add(paneWarning, BorderLayout.SOUTH);
		
		tabpane.addChangeListener(new ChangeListener()
	    {
	      public void stateChanged(ChangeEvent e)
	      {
	        checkSelectedTab();
	      }
	    });
	};
	
	/**
	 * Checkt das ausgew�hlte Tab. Je nach Klasse des Tabs, f�hrt es Updatemethoden
	 * o.�. aus.
	 */
	public void checkSelectedTab(){
		Component c = tabpane.getSelectedComponent();
		if(c instanceof LehreransichtPanel){
			paneLehrer.removeAll();
			paneLehrer.init();
			System.out.println("[DEBUG]: Lehreransicht aktualisiert.");
		};
		if(c instanceof DataPanel){
			SchoolclassPanel.updateList();
			PersonalPanel.updateList();
			RoomPanel.updateList();
			RaumfunktionPanel.updateList();
			StundeninhaltPanel.updateList();
			BedarfPanel.updateList();
		}
		if(c instanceof WochenplanPanel){
			paneWochen.update();
		};
	}

	public static WarningPanel getWarning() {
		return paneWarning;
	}
	
	public static JTabbedPane getTabPane() {
		return tabpane;
	}
}
