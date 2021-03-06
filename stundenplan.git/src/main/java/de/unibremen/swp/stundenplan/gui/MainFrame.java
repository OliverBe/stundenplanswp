package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPersonal;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

/**
 * Hauptfenster des Stundenplans
 * 
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
	 * Panel zum Hinzuf���gen, Editieren, Loeschen von Daten aus der DB
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

	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	/**
	 * Konstruktor fuers Hauptfenster
	 */
	public MainFrame() {
		super("Stundenplan Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setMinimumSize(new Dimension(1280, 1024));
		setMaximumSize(new Dimension(1280, 1024));
		// setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}

	/**
	 * Auslagerung der hinzuzuf���genden Panels in diese Methode
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

		tabpane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				checkSelectedTab();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				if (!Data.isSaved()) {
					int result = JOptionPane
							.showConfirmDialog(
									Stundenplan.getMain(),
									"Es wurden Veraenderungen vorgenommen.\nSoll gespeichert werden?",
									"Warnung", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						if (!Data.getLastRestoredFileName().equals("")) {
							Data.backup(Data.getLastRestoredFileName(), true);
							closeAll();
						} else {
							final JFrame backupFrame = new JFrame();
							GridBagConstraints c = new GridBagConstraints();
							JPanel backupChooser = new JPanel();
							final JTextField tf = new JTextField();
							JButton button = new JButton("Erstellen");
							JButton button2 = new JButton("Abbrechen");
							backupChooser.setLayout(new GridBagLayout());
							backupChooser.setBorder(BorderFactory
									.createTitledBorder("Datei benennen"));
							c.fill = GridBagConstraints.BOTH;
							c.insets = new Insets(8, 5, 1, 1);
							c.anchor = GridBagConstraints.CENTER;
							c.gridx = 0;
							c.gridy = 0;
							c.gridwidth = 2;
							backupChooser.add(tf, c);
							c.gridy = 1;
							list.setLayoutOrientation(JList.VERTICAL);
							list.setEnabled(false);
							listScroller.setPreferredSize(new Dimension(300,
									200));
							backupChooser.add(listScroller, c);
							c.gridy = 2;
							c.gridwidth = 1;
							c.weightx = 0.6;
							backupChooser.add(button, c);
							c.gridx = 1;
							c.weightx = 0.4;
							backupChooser.add(button2, c);
							backupFrame.add(backupChooser);
							backupFrame.setTitle("Speichern");
							backupFrame.setLocation(MouseInfo.getPointerInfo()
									.getLocation().x, MouseInfo
									.getPointerInfo().getLocation().y);
							backupFrame.pack();
							backupFrame.setVisible(true);

							File dir = new File(System.getProperty("user.dir"));
							File[] files = dir.listFiles(new FilenameFilter() {
								public boolean accept(File dir, String filename) {
									return filename.endsWith(".db")
											&& !filename.equals("temp.db");
								}
							});
							listModel.clear();
							for (File file : files) {
								listModel.addElement(file.getName());
							}

							button.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									backupFrame.dispose();
									Data.backup(tf.getText(), false);
									closeAll();
								}
							});

							button2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									backupFrame.dispose();
								}
							});
						}
					}else closeAll();
				}else closeAll();
				Config.setIntValue(Config.PROGRAM_STARTED_STRING, 0);
			}
		});

		addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				setExtendedState(getExtendedState() | JFrame.NORMAL);
			}
		});

	};
	
	private void closeAll() {
		Stundenplan.getTimer().stop();
		Data.close();
		Stundenplan.getMain().dispose();
	}

	/**
	 * Checkt das ausgewaehlte Tab. Je nach Klasse des Tabs, fuehrt es
	 * Updatemethoden o.Ae. aus.
	 * 
	 * Im Falle des LehreransichtsPanels, prueft es zunaechst, ob relevante
	 * Zielgroessen veraendert wurden. Sind diese nicht veraendert, ist kein
	 * Update noetig. Ausserdem wird ueberprueft, ob das letzte Command in der
	 * History ein Edit-Command ist, da diese Commands keine unmittelbaren
	 * Aenderungen an den Zielgrossen bewirken, aber dennoch Unterschiede
	 * ausmachen koennen (bsplw. Aenderung der Sollzeiten von Lehrerinnen etc.)
	 * So wird verhindert, dass der Personaleinsatzplan immer weiter
	 * aktualisiert wird, obwohl der Plan z.B. fertig ist und nicht mehr
	 * bearbeitet wird. Spart so dauerhaft Laufzeit, wenn Bearbeitung bereits
	 * abgeschlossen.
	 */
	public void checkSelectedTab() {
		Component c = tabpane.getSelectedComponent();

		if (!(c instanceof StundenplanPanel)) {
			paneStundenplan.popmen.setVisible(false);
			if (paneStundenplan.bedarf != null) {
					paneStundenplan.bedarf.dispose();
					paneStundenplan.bedarfTable = null;
			}
			if (paneStundenplan.pendel != null) {
					paneStundenplan.pendel.dispose();
					paneStundenplan.pendelTable = null;
				
			}
		}

		if (c instanceof LehreransichtPanel) {
			if (paneLehrer.getSi().size() != DataStundeninhalt
					.getAllStundeninhalte().size()
					|| paneLehrer.getAllPersoKuerzel().size() != DataPersonal
							.getAllAcronymsFromPersonal().size()
					|| paneLehrer.getPlanungseinheiten().size() != DataPlanungseinheit
							.getAllPlanungseinheit().size()
					|| CommandHistory.isLastEditCommand()) {

				paneLehrer.removeAll();
				paneLehrer.init();
				System.out.println("[DEBUG]: Lehreransicht aktualisiert.");
			}
			pack();
		}
		;
		if (c instanceof DataPanel) {
			SchoolclassPanel.updateList();
			PersonalPanel.updateList();
			RoomPanel.updateList();
			RaumfunktionPanel.updateList();
			StundeninhaltPanel.updateList();
			BedarfPanel.updateList();
			pack();
		}
		if (c instanceof WochenplanPanel) {
			paneWochen.update();
			pack();
		}
		;
		if (c instanceof RaumbelegungsplanPanel) {
			RaumbelegungsplanPanel.updateLists();
			pack();
		}
		;
		if (c instanceof StundenplanPanel) {
			StundenplanPanel.updateLists();
			pack();
		}
		;
	}

	public void updateAll() {
		paneStundenplan.popmen.setVisible(false);
		if (paneLehrer.getSi().size() != DataStundeninhalt
				.getAllStundeninhalte().size()
				|| paneLehrer.getAllPersoKuerzel().size() != DataPersonal
						.getAllAcronymsFromPersonal().size()
				|| paneLehrer.getPlanungseinheiten().size() != DataPlanungseinheit
						.getAllPlanungseinheit().size()
				|| CommandHistory.isLastEditCommand()) {

			paneLehrer.removeAll();
			paneLehrer.init();
			System.out.println("[DEBUG]: Lehreransicht aktualisiert.");
		}
		SchoolclassPanel.updateList();
		PersonalPanel.updateList();
		RoomPanel.updateList();
		RaumfunktionPanel.updateList();
		StundeninhaltPanel.updateList();
		BedarfPanel.updateList();
		paneWochen.update();
		RaumbelegungsplanPanel.updateLists();
		StundenplanPanel.updateLists();
		pack();
	}

	public static JTabbedPane getTabPane() {
		return tabpane;
	}
}
