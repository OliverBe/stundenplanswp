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
import de.unibremen.swp.stundenplan.exceptions.CommandHistoryException;
import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

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
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		
		setVisible(true);
	}

	private void initComponents() {

		// TODO bei click sollen sich die anderen closen, bzw der eine öffnen
		tabpane.addTab("Daten", dataPanel);
		tabpane.addTab("StundenplÃ¤ne", paneStundenplan);
		tabpane.addTab("Lehreransicht", paneLehrer);
		tabpane.addTab("Raumbelegungsplan", paneRaeume);
		tabpane.addTab("Wochenplan", paneWochen);
		tabpane.addTab("Einstellungen", paneConfig);

		// Der Undo-Button und Funktionen etc.
		ImageIcon revert = new ImageIcon(getClass().getResource("revert.png"));
		JButton undo = new JButton(revert);
		add(undo, BorderLayout.EAST);
		undo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					System.out.println("[Undone]: "+CommandHistory.getLast().toString());
					CommandHistory.getLast().undo();
					CommandHistory.deleteLast();
					checkSelectedTab();
				}catch (CommandHistoryException e){
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
	 * Checkt das ausgewählte Tab. Je nach Klasse des Tabs, führt es Updatemethoden
	 * o.Ä. aus.
	 */
	public void checkSelectedTab(){
		Component c = tabpane.getSelectedComponent();
		
		if(c instanceof LehreransichtPanel){
			paneLehrer.removeAll();
			paneLehrer.init();
			System.out.println("[DEBUG]: Lehreransicht aktualisiert.");
		}else if(c instanceof DataPanel){
			dataPanel.getPersonalPanel().updateList();
			dataPanel.getRoomPanel().updateList();
			dataPanel.getStundeninhaltPanel().updateList();
			dataPanel.getSchoolclassPanel().updateList();
			dataPanel.getRaumfunktionPanel().updateList();
		}else if(c instanceof WochenplanPanel){
			paneWochen.removeAll();
			paneWochen.init();
			System.out.println("[DEBUG]: Wochenplan aktualisiert.");
		}
		// TODO restliche Ansichten/LIsten Update Funktionen implementieren
	}

	public static WarningPanel getWarning() {
		return paneWarning;
	}
}
