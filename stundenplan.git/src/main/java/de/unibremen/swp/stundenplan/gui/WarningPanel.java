package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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


@SuppressWarnings("serial")
public final class WarningPanel extends JPanel{

	private static DefaultListModel<String> listModel = new DefaultListModel();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	
	public String message="No Warning";
	private JButton b=new JButton("Warnung loeschen");
	
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
		add(listScroller, c);
		c.gridy=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		add(b, c);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				listModel.removeElement(list.getSelectedValue());
			}
		});
	}
	
	public static void setText(String pMessage) {
		listModel.addElement(pMessage);
	}
	
	
	@SuppressWarnings("serial")
	public class SelectedListCellRenderer extends DefaultListCellRenderer {
	     @Override
	     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	         c.setBackground(new Color(245,110,110));
	         if(isSelected) c.setBackground(new Color(112,138,144));
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
