package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextComponent;
import java.awt.TextField;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;


@SuppressWarnings("serial")
public final class WarningPanel extends JPanel{

	private static DefaultListModel<String> listModel = new DefaultListModel();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	
	public String message="No Warning";
	public TextField t=new TextField(300);
	
	public WarningPanel(){
		list.setCellRenderer(new SelectedListCellRenderer());
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(143,188,143));
		list.setPreferredSize(new Dimension(500,10));
		listScroller.setPreferredSize(new Dimension(250, 200));
		
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());
		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridwidth=2;
		add(listScroller);
	}
	
	public void setText(String pMessage) {
		listModel.addElement(pMessage);
	}
	
	@SuppressWarnings("serial")
	public class SelectedListCellRenderer extends DefaultListCellRenderer {
	     @Override
	     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	         
             c.setBackground(new Color(245,110,110));
	         return c;
	     }
	}
//	public static void updateList() {
//		listModel.clear();
//		for (Personal per : PersonalManager.getAllPersonalFromDB()){
//			 listModel.addElement(per);
//		 }
//	}
}
