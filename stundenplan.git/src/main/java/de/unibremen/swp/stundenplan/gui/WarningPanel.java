package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Warnungspanel, enthaelt alle Exceptions, die vom User gesehen werden sollen
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class WarningPanel extends JPanel{

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
	
	/**
	 * Standartanzeige : No Warning
	 */
	public static String message="No Warning";
	
	/**
	 * Button um eine Warnung zu loeschen
	 */
	private JButton b1=new JButton("Warnung loeschen");
	
	/**
	 * Button um alle Warnungen zu loeschen
	 */
	private JButton b2=new JButton("Alle Warnungen loeschen");
	
	/**
	 * Konstruktor des Warnungspanels
	 */
	public WarningPanel(){
		System.out.println("--------- WARN -------");
		list.setCellRenderer(new SelectedListCellRenderer());
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBackground(new Color(143,188,143));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		list.setFocusable(false);
		listScroller.setPreferredSize(new Dimension(160,330));
		//list.setSize(100,500);
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
		try{
			if((listModel.elementAt(0).equals(message))) listModel.removeElement(message);
		}catch(ArrayIndexOutOfBoundsException e){
		}
		listModel.addElement(message);
	}
	
	public static void setText(String pMessage) {
		if(listModel.elementAt(0).equals(message)) listModel.removeElement(message);
		listModel.addElement(pMessage);
	}
	
	/**
	 * Anpassung des ListCellRenderers auf unser Konzept
	 * @author Oliver
	 *
	 */
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
}
