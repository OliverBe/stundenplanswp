package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Map.Entry;

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
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Planungseinheit;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;
import de.unibremen.swp.stundenplan.logic.SchulklassenManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;

public class StundenplanPanel extends JPanel implements ActionListener,
		MouseListener {

	public JTable pendelTable;
	public JFrame pendel;
	
	public static JTable bedarfTable;
	public static JFrame bedarf; 
	/**
	 * gibt den optischen Punkt der x-Achse an an welchem ein Event ausgef���hrt
	 * wurde. Nicht jedes Event
	 */
	private int eventX;
	/**
	 * gibt den optischen Punkt der y-Achse an an welchem ein Event ausgef���hrt
	 * wurde. Nicht jedes Event
	 */
	private int eventY;
	
	private Object tableowner;

	/**
	 * gibt den Punkt der x-Achse an an welchem ein Event ausgef���hrt wurde.
	 * Nicht jedes Event
	 */
	private int eventXX;

	/**
	 * gibt den Punkt der y-Achse an an welchem ein Event ausgef���hrt wurde.
	 * Nicht jedes Event
	 */
	private int eventYY;

	/**
	 * repr���sentiert die Tabelle welche in dem Frame als Stundenplan angezeigt
	 * wird
	 */
	private static JTable table;
	
	
	

	/**
	 * die MenuBar ist die Men���Leiste im Pane welche die lehrer und Schulklassen
	 * listen enthalten
	 */
	private static JMenuBar menuBar = new JMenuBar();

	/**
	 * ist das Model der Liste f���r das Personal
	 */
	private static DefaultListModel pList;

	/**
	 * ist das Model der Liste f���r die Schulklassen
	 */
	private static DefaultListModel sList;

	/**
	 * ist die Liste, in dem das gesammte Personal erfasst wird
	 */
	private static JList personalList;

	/**
	 * ist die Liste in der alle Klassen erfasst werden
	 */
	private static JList schoolclassList;

	/**
	 * Label f���r die Men���bar
	 */
	private static JLabel label1 = new JLabel("Lehrer");

	/**
	 * Label f���r die Men���bar
	 */
	private static JLabel label2 = new JLabel("Klassen");

	/**
	 * dieses MouseAdapter erzeugt bei rechtsklick auf die Tabelle ein popup
	 * Men��� welches das Planungseinheiten hinzuf���gen, oder bearbeiten kann
	 */
	private MouseAdapter mousefunc = new MouseAdapter() {
		public void mousePressed(MouseEvent evt) {
			if (evt.getSource() == table) {
				eventX = evt.getXOnScreen();
				eventY = evt.getYOnScreen();
				eventXX = evt.getX();
				eventYY = evt.getY();

				if (SwingUtilities.isLeftMouseButton(evt)) {

					popmen.setVisible(false);
				}

				if (SwingUtilities.isRightMouseButton(evt)) {

					final int row = table.rowAtPoint(evt.getPoint());
					final int col = table.columnAtPoint(evt.getPoint());
					Timeslot t = null;
					if (table.getValueAt(row, col) instanceof Timeslot) {
						t = (Timeslot) table.getValueAt(row, col);
					}
					if (t != null){
					createPopup(t);
					}
				}
			}
		}
		
	};

	/**
	 * repr���sentiert den Button, welche beim dr���cken den ausgew���hlten
	 * Stundenplan anzeigt
	 */
	private static JButton show = new JButton("Anzeigen");

	/**
	 * WarningLabel
	 */
	public JLabel warning = new JLabel();

	/**
	 * ist das Popup Menue welches erscheint, wenn in dem Stundenplan rechtsklick
	 * ausgefuehrt wird
	 */
	JPopupMenu popmen = new JPopupMenu();

	/**
	 * der Konstrukter vom StundenplanPanel. die ActionListener werden den
	 * GUI-Elementen zugeordnet und die Tablle wird auf null gesetzt, sodass zu
	 * Beginn keine Tabelle ersichtlich ist.
	 */
	public StundenplanPanel() {
		table = null;
		init();
		show.addActionListener(this);
		personalList.addMouseListener(this);
		schoolclassList.addMouseListener(this);
	}

	/**
	 * wird beim Initialisieren dieses Panes ausgef���hrt und intialisiert die
	 * GUI-Elemente deses Panes.
	 */
	public void init() {
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;

		updateLists();

		add(menuBar, c);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 2;
		c.gridy = 0;
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);
		
		
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 2;
		c.weightx = 0.2;
		c.gridy = 0;
		drawPTable();
		drawBTable();
		
		
		if (table != null) {
			table.addMouseListener(mousefunc);
		}
		
	}
	private void drawBTable(){
		if(bedarfTable!= null) {
			bedarf = new JFrame("Bedarf der Klasse");
			JScrollPane pane2 = new JScrollPane(bedarfTable);
			bedarf.add(pane2);
			bedarf.setSize(250, 200);
			bedarf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			bedarf.setAlwaysOnTop(true);
			bedarf.setVisible(true);
		}
	}
	
	private void drawPTable(){
		if(pendelTable != null) {
			String ptitle = "";
			if(tableowner instanceof Personal){
				Personal p = (Personal)tableowner;
				ptitle = "Standortwechsel von Personal "+ p.getName();
			}
			if(tableowner instanceof Schoolclass){
				Schoolclass s = (Schoolclass)tableowner;
				ptitle = "Standortwechsel von Schulklasse "+ s.getName();
			}
			pendel = new JFrame(ptitle);
			JScrollPane pane2 = new JScrollPane(pendelTable);
			pendel.add(pane2);
			pendel.setSize(600, 300);
			pendel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			pendel.setLocation(0, 201);
			pendel.setAlwaysOnTop(true);
			pendel.setVisible(true);
		}
	}

	/**
	 * Erzeugt ein Popup zum hinzufuegen, editieren und loeschen von Lehrern
	 * Faechern und Klassen
	 * 
	 * @param row
	 *            Die Zeile.
	 * @param col
	 *            Die Spalte.
	 * @return das neue Popup-Menu
	 */
	protected JPopupMenu createPopup(final Timeslot t) {

		if (popmen.isVisible()) {
			popmen.setVisible(false);
		}
		popmen = new JPopupMenu();
		final JMenuItem menu1 = new JMenuItem("Neue Planungseinheit");
		final JMenuItem menu3 = new JMenuItem("Planungseinheit bearbeiten");
		final JMenuItem menu2 = new JMenuItem("Planungseinheit entfernen");

		menu1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
				PEedit frame = getpFrame(t, tableowner);
				frame.setVisible(true);
			}
		});
		menu2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
				PlanungseinheitManager.deletePlanungseinheitFromDB(t.getpe());
				updatetable();
			}
		});
		menu3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				popmen.setVisible(false);
				PEedit frame = getpFrame(t);
				frame.setVisible(true);
			}
		});
		
		if (t.getpe() != -1) {
			popmen.add(menu3);
			popmen.add(menu2);
		}else{
			popmen.add(menu1);
		}
		popmen.setLocation(eventX, eventY);
		popmen.setVisible(true);
		return popmen;

	}

	private PEedit getpFrame(Timeslot pt, Object owner) {
		return new PEedit(this, pt, owner);
	}
	
	private PEedit getpFrame(Timeslot pt) {
		return new PEedit(this, pt.getpe());
	}
	
	/**
	 * updated die GUI und die Listen. wird ausgefuehrt sobald eine relevante
	 * Aenderung in der Datenbank geschieht. Wird von anderen Klassen statisch
	 * ausgerufen.
	 */
	public static void updateLists() {
		Personal[] personalListe = new Personal[PersonalManager
				.getAllPersonalFromDB().size()];
		Schoolclass[] schoolclassListe = new Schoolclass[SchulklassenManager
				.getAllSchulklassenFromDB().size()];
		pList = new DefaultListModel();
		sList = new DefaultListModel();

		for (int i = 0; i < PersonalManager.getAllPersonalFromDB().size(); i++) {
			personalListe[i] = PersonalManager.getAllPersonalFromDB().get(i);

			pList.addElement(personalListe[i]);
		}

		for (int i = 0; i < SchulklassenManager.getAllSchulklassenFromDB()
				.size(); i++) {
			schoolclassListe[i] = SchulklassenManager
					.getAllSchulklassenFromDB().get(i);

			sList.addElement(schoolclassListe[i]);
		}

		personalList = new JList(pList);
		schoolclassList = new JList(sList);

		JScrollPane paneList1 = new JScrollPane(personalList);
		JScrollPane paneList2 = new JScrollPane(schoolclassList);

		menuBar.removeAll();
		GridBagLayout gbl = new GridBagLayout();
		menuBar.setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		gbl.setConstraints(label1, c);
		menuBar.add(label1, c);
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.2;
		gbl.setConstraints(paneList1, c);
		menuBar.add(paneList1, c);
		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0.1;
		gbl.setConstraints(label2, c);
		menuBar.add(label2, c);
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 0.2;
		gbl.setConstraints(paneList2, c);
		menuBar.add(paneList2, c);
		c.gridx = 0;
		c.gridy = 4;
		c.weighty = 0.1;
		gbl.setConstraints(show, c);
		menuBar.add(show, c);
		
		c.gridy = 6;
		c.weighty = 0.2;
		c.weightx = 1.0;
		menuBar.add(new WarningPanel(), c);

	}

	/**
	 * stellt eine Aktion da. Sobald der Anzeigen Button gedrueckt wird, wird von
	 * dem element in der JList der Stundenplan in Form eines JTables angezeigt
	 * Danach wird die GUI aktualisiert
	 * 
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == show && (!personalList.isSelectionEmpty())) {

			Personal p = (Personal) personalList.getSelectedValue();
			tableowner = p;
			System.out.println(p.getKuerzel());
			table = new StundenplanTable(p).getTable();
			if(pendelTable != null && pendel.isVisible()) {
				pendel.dispose();
			}
			if(bedarfTable != null && bedarf.isVisible()) {
				bedarf.dispose();
			}
			bedarfTable = null;
			//prueft ob personal ueberhaupt Standort wechseln muss
			if(PlanungseinheitManager.pendelTlength(p)!=0){
			pendelTable = creatependelTable(p);
			}else{
				pendelTable = null;
			}
			init();
		} else if (ae.getSource() == show
				&& (!schoolclassList.isSelectionEmpty())) {
			
			if(bedarfTable != null && bedarf.isVisible()) {
				bedarf.dispose();
			}
			if(pendelTable != null && pendel.isVisible()) {
				pendel.dispose();
			}
			
			pendelTable = null;
			Schoolclass s = (Schoolclass) schoolclassList.getSelectedValue();
			tableowner = s;
			if(PlanungseinheitManager.pendelTlength(s)!=0){
				pendelTable = creatependelTable(s);
				}else{
				pendelTable = null;
			}
			System.out.println(s.getName());
			table = new StundenplanTable(s).getTable();
			
			

			bedarfZeigen(s);

			
			
			init();
			


		}

		
		updatetable();
	}

	/**
	 * gibt den aktuellen Stundenplan (die Tabelle) zur���ck
	 * 
	 * @return
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * 
	 */
	public void updatetable() {
		if(table != null) {
		table.repaint();
		}
		if(bedarfTable != null) {
			bedarf.dispose();
			bedarfZeigen((Schoolclass)tableowner);
			drawBTable();
			bedarfTable.repaint();
		}
		
		if(PlanungseinheitManager.pendelTlength(tableowner)!=0){
			if(pendelTable==null){
				pendelTable=creatependelTable(tableowner);
				drawPTable();
			}
		}else{
			if(pendel!=null){
				pendel.setVisible(false);
				pendel = null;
			}
		}
		if(pendel != null &&!pendel.isVisible()){
			pendel.setVisible(true);
		}
		if(pendelTable!=null){
			pendelTable.repaint();
		}
		Stundenplan.getMain().validate();
		personalList.addMouseListener(this);
		schoolclassList.addMouseListener(this);
	}
	
	private JTable creatependelTable(final Object owner){
		JTable table = new JTable(new PendelTablemodel(owner));
		table.setDefaultRenderer(String.class, new LineWrapCellRenderer());
		table.setRowHeight(80);
		return table;
	}

	/**
	 * Sobald ein Element in den J-Listen ausgewaehlt
	 * wird, wird in der anderen JListe das ausgewaehlte Element "deselected".
	 * @param evt
	 */
	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getSource() == personalList) {
			schoolclassList.clearSelection();
		} else if (evt.getSource() == schoolclassList) {
			personalList.clearSelection();
		}
			popmen.setVisible(false);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static void bedarfZeigen(Schoolclass s) {
		DefaultTableModel modelSchoolclassBedarf = new DefaultTableModel();
		modelSchoolclassBedarf.addColumn("Stundeninhalt");
		modelSchoolclassBedarf.addColumn("Bedarf");
		modelSchoolclassBedarf.addColumn("Ist-Zeit");
		ArrayList<Planungseinheit> pEs = DataPlanungseinheit.getAllPlanungseinheitByObject(s);
		for (Stundeninhalt si : StundeninhaltManager
				.getAllStundeninhalteFromDB()) {
			modelSchoolclassBedarf.addRow(new String[] { si.getKuerzel(), "0", "0" });
		}
		for (Entry<String, Integer> entry : s.getStundenbedarf().entrySet()) {
			for (int i = 0; i < modelSchoolclassBedarf.getRowCount(); i++) {
				if (modelSchoolclassBedarf.getValueAt(i, 0).toString().equals(entry.getKey())) {
					modelSchoolclassBedarf.setValueAt(entry.getValue(), i, 1);
				}
			}
		}
		for (int i = 0; i < modelSchoolclassBedarf.getRowCount(); i++) {
			for(Planungseinheit p: pEs){
				if(p.getStundeninhalte().contains(modelSchoolclassBedarf.getValueAt(i, 0))){
					int dauer = Integer.parseInt(modelSchoolclassBedarf.getValueAt(i, 2).toString());
					dauer += p.duration()/45;
					modelSchoolclassBedarf.setValueAt(dauer, i, 2);
				}
			}
		}
		bedarfTable= new JTable(modelSchoolclassBedarf);
		bedarfTable.setColumnSelectionAllowed(false);
		bedarfTable.getTableHeader().setReorderingAllowed(false);
		bedarfTable.getTableHeader().setResizingAllowed(false);
	}


}
