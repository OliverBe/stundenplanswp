package de.unibremen.swp.stundenplan.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import de.unibremen.swp.stundenplan.data.Teacher;

public class DataPopup extends JPopupMenu{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JList<String> list = new JList<String>();
	
	private static TeacherListModel model;
	
	private Teacher t = new Teacher();
	

	JMenuItem edit = new JMenuItem("Edit");
	JMenuItem delete = new JMenuItem("Delete");
	
	public DataPopup(final JList<String> pList, final TeacherListModel pModel){
		list=pList;
		model=pModel;
		init();
		
	}
	
	private void init(){
		add(edit);
		add(delete);
	}
	
	public Teacher getTeacher() {
		return t;	
	};
	
}
