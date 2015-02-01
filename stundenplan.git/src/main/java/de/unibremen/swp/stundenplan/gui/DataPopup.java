package de.unibremen.swp.stundenplan.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.unibremen.swp.stundenplan.data.Personal;

public class DataPopup extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<String> list = new JList<String>();
	
	private static DefaultListModel model;
	

	JMenuItem edit = new JMenuItem("Bearbeiten");
	JMenuItem delete = new JMenuItem("Loeschen");
	
	public DataPopup(final JList pList, final DefaultListModel pModel){
		list=pList;
		model=pModel;
		init();
	}
	
	private void init(){
		add(edit);
		add(delete);
	}	
}
