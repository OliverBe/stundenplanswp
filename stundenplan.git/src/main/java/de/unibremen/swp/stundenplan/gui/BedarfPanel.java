package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.DataStundeninhalt;

public class BedarfPanel extends JPanel{
	
	private Label lName = new Label("Name des Raumes: ");
	private Label lPos = new Label("In welchem Gebäude: ");

	public TextField nameField = new TextField(5);
	
	public String name;
	
	public JButton button = new JButton("Raum Hinzufuegen");
	
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	
	private int x=1;
	
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	public BedarfPanel() {
		setLayout(new GridBagLayout());
		c2.fill=GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()),c2);
	}
	
	private JPanel createAddPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Neuen Raum hinzufuegen"));
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		c.fill = GridBagConstraints.BOTH;
		
		
		final DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);
		
		model.addColumn("Jahrgang");
		model.addColumn("Stundeninhalt");
		model.addColumn("Stunden (h)");
		
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		table.getTableHeader().setReorderingAllowed(false);
		
		for(Stundeninhalt s : DataStundeninhalt.getAllStundeninhalte()){
			model.addRow(new String[] {""+1, s+"", ""+0});
		}
		
		c.gridy=1;
		p.add(table.getTableHeader(), c);
		c.gridy=2;
		p.add(table,c);
	    c.gridy=3;
		p.add(button,c);    
		return p;
		
	}
	
	private boolean textFieldsEmpty(final JPanel p){
		boolean b=true;
		for(Component c : p.getComponents()){
			if(c instanceof TextField){
				TextField tf = (TextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
		}
		return b;
	}

}
