package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class LehreransichtPanel extends JPanel {

	private JTable table;

	private JFileChooser chooser = new JFileChooser();
	private JFrame f;

	GridBagConstraints c = new GridBagConstraints();

	public JLabel warning = new JLabel();

	public LehreransichtPanel() {
		init();
	}

	public void init() {

		setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;

		// String[] columnNames = { "Kuerzel", "Wochenstunden", "Ersatzzeit",
		// "Deu", "E", "MA", "SU/BGU", "KU" };
		// String [][] rowData
		// ={{"BER -4","28","3B 6","3B 2 \n 2C 1","-","-","1A/2C 2",""}
		// , {"BM -28","28","","","","","",""}
		// };
		// ArrayList<Personal> allPerso =
		// PersonalManager.getAllPersonalFromDB();
		//
		// if (allPerso.size() > 0) {
		// ArrayList<ArrayList<String>> listOfLists = new ArrayList<>();
		//
		// for (int i = 0; i < allPerso.size(); i++) {
		//
		// }
		//
		// String[][] rowData = new String[0][];
		//
		// // table = new JTable(rowData, columnNames);
		DefaultTableModel model = new DefaultTableModel();
		table = new JTable(model);
		model.addColumn("Kuerzel");
		model.addColumn("Wochenstunden");
		model.addColumn("Ersatzzeit");
		ArrayList<Stundeninhalt> si = DataStundeninhalt.getAllStundeninhalte();
		int columnSize = 3 + si.size();

		for (Stundeninhalt s : si) {
			model.addColumn(s.getKuerzel());
		}

		ArrayList<String> kuerzel = PersonalManager.getAllKuerzel();
		for (String s : kuerzel) {
			model.addRow(new String[] { s });
		}
		
		ArrayList<Personal> perso = PersonalManager.getAllPersonalFromDB();
		for (Personal p : perso){
			//TODO hier müssen noch die Informationen zu Stundeninhalten rein
			model.addRow(new Object[] { p.getKuerzel(), p.getSollZeit(), "- "+p.getErsatzZeit()});
		}
		

		table.setRowSelectionAllowed(true);
		table.setRowHeight(40);
		JScrollPane pane = new JScrollPane(table);
		add(pane, c);

	}
	
	/**
	 * Parst einen int-Parameter in die gewünschte Form der Lehreransicht. Wenn eine 0 (Null) übergeben wird,
	 * soll im Lehrplan ein leerer String stehen.
	 * @param i int, das geparst werden soll
	 * @return
	 * 		das int als String oder leere Zeichenkette
	 */
	public String parseForPanel(int i){
		String s = "";
		StringBuilder sb = new StringBuilder(s);
		if(i == 0){
			return s;
		}else{
			sb.append(i);
			return sb.toString();
		}
	}
}
