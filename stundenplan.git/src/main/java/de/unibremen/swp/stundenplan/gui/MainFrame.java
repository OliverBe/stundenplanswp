 package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.exceptions.StundenplanException;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

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

		// TODO bei click sollen sich die anderen closen, bzw der eine öffnen
		tabpane.addTab("Daten", dataPanel);
		tabpane.addTab("Stundenplaene", paneStundenplan);
		tabpane.addTab("Lehreransicht", paneLehrer);
		tabpane.addTab("Raumbelegungsplan", paneRaeume);
		tabpane.addTab("Wochenplan", paneWochen);
		tabpane.addTab("Einstellungen", paneConfig);
		
		setJMenuBar(menu);
		add(tabpane);
		add(paneWarning, BorderLayout.PAGE_END);
		
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
