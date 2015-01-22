package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

/**
 * Warnungspanel, enthaelt alle Exceptions, die vom User gesehen werden sollen
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public final class WarningPanel extends JPanel{

	/**
	 * ListModel der JList im Warnungspanel
	 */
	private static DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	/**
	 * Jlist des Warnungspanels
	 */
	private JList<String> list = new JList<String>(listModel);
	
	/**
	 * Scrollbar der JList
	 */
	private JScrollPane listScroller = new JScrollPane(list);
	
	public static String message="No Warning";
	private JButton b1=new JButton("Warnung loeschen");
	private JButton b2=new JButton("Alle Warnungen loeschen");
	
	public WarningPanel(){
		list.setCellRenderer(new SelectedListCellRenderer());
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(143,188,143));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		list.setFocusable(false);
		listScroller.setPreferredSize(new Dimension(160,330));
		list.setSize(100,500);
		c.insets=new Insets(30,0,0,0);
		add(listScroller, c);
		c.gridy=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(5,0,0,0);
		add(b1, c);
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				listModel.removeElement(list.getSelectedValue());
			}
		});
		
		c.gridy=2;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(b2, c);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				listModel.clear();
				listModel.addElement(message);
			}
		});
		
		listModel.addElement(message);
	}
	
	public static void setText(String pMessage) {
	//	if(listModel.elementAt(0).equals(message)) listModel.removeElement(message);
		listModel.addElement(pMessage);
	}
	
	
	public class SelectedListCellRenderer extends DefaultListCellRenderer {
	     @Override
	     public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	         c.setBackground(new Color(245,110,110));
	         if(isSelected) c.setBackground(new Color(112,138,144));
	         if(value.equals(message)) c.setBackground(new Color(110,138,142));
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
