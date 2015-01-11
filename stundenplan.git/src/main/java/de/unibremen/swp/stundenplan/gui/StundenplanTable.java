package de.unibremen.swp.stundenplan.gui;


import javax.swing.JTable;

public class StundenplanTable {

	private JTable table;
	
	
	public StundenplanTable(Object obj) {
		table = new JTable (new TimetableModel(obj)); 
		table.setDefaultRenderer(Timeslot.class, new TimetableRenderer());
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(75);
	
	
	}
	
	public StundenplanTable() {
		table = new JTable (new TimetableModel()); 
		table.setDefaultRenderer(Timeslot.class, new TimetableRenderer());
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(75);
	
	
	}
	
	
	public JTable getTable() {
		return table;
	}
	
	
	
	
	
	
	
	
}
