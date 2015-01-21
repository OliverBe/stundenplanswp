 package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Hauptfenster des Stundenplans
 * @author Che
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * Menubar des Hauptfensters
	 */
	private MenuBar menu = new MenuBar(this);

	/**
	 * Tabpane des Hauptfensters
	 */
	private static JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP);

	/**
	 * Panel zum Hinzufügen, Editieren, Loeschen von Daten aus der DB
	 */
	private DataPanel paneData = new DataPanel();
	
	/**
	 * Panel zur Anzeige des Stundenplans, PE koennen hier hinzugefuegt werden.
	 */
	private StundenplanPanel paneStundenplan = new StundenplanPanel();
	
	/**
	 * Panel zur Anzeige des Personaleinsatzplans
	 */
	private LehreransichtPanel paneLehrer = new LehreransichtPanel();
	
	/**
	 * Panel zur Anzeige des Raumbelegungsplans
	 */
	private JPanel paneRaeume = new RaumbelegungsplanPanel();
	
	/**
	 * Panel der Einstellungen
	 */
	private JPanel paneConfig = new ConfigPanel();
	
	/**
	 * Panel zur Anzeige des Wochenplans
	 */
	private WochenplanPanel paneWochen = new WochenplanPanel();
	
	/**
	 * Panel fuer die Warnungen
	 */
	private static WarningPanel paneWarning = new WarningPanel();

	/**
	 * Konstruktor fuers Hauptfenster
	 */
	public MainFrame() {
		super("StundenplanTool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		pack();
		setMinimumSize(new Dimension(1280,1024));
	//	setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);	
		setVisible(true);
	}
	
	/**
	 * Auslagerung der hinzuzufügenden Panels in diese Methode
	 */
	private void init() {
		tabpane.addTab("Daten", paneData);
		tabpane.addTab("Stundenplaene", paneStundenplan);
		tabpane.addTab("Personaleinsatzplan", paneLehrer);
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
	 * 
	 * Im Falle des LehreransichtsPanels, prüft es zunächst, ob relevante Zielgroeßen verändert
	 * wurden. Sind diese nicht verändert, ist kein Update noetig.
	 * Außerdem wird überprüft, ob das letzte Command in der History ein Edit-Command ist, da
	 * diese Commands keine unmittelbaren Änderungen an den Zielgroeßen bewirken, aber dennoch
	 * Unterschiede ausmachen können (bsplw. Aenderung der Sollzeiten von Lehrerinnen etc.)
	 * So wird verhindert, dass der Personaleinsatzplan immer weiter aktualisiert wird, obwohl der Plan
	 * z.B. fertig ist und nicht mehr bearbeitet wird. Spart so dauerhaft Laufzeit, wenn Bearbeitung bereits abgeschlossen.
	 */
	public void checkSelectedTab(){
		Component c = tabpane.getSelectedComponent();
		if(c instanceof LehreransichtPanel){
			if(   paneLehrer.getSi().size() != DataStundeninhalt.getAllStundeninhalte().size()
			   || paneLehrer.getAllPersoKuerzel().size() != DataPersonal.getAllAcronymsFromPersonal().size()
			   || paneLehrer.getPlanungseinheiten().size() != DataPlanungseinheit.getAllPlanungseinheit().size()
			   || CommandHistory.isLastEditCommand()) {
				
					paneLehrer.removeAll();
					paneLehrer.init();
					System.out.println("[DEBUG]: Lehreransicht aktualisiert.");
			}
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
