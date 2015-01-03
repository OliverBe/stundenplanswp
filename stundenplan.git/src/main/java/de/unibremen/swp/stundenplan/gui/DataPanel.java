package de.unibremen.swp.stundenplan.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;

public class DataPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar = new JMenuBar();

	private JMenuItem mP = new JMenuItem("Personal");
	private JMenuItem mS = new JMenuItem("Klassen");
	private JMenuItem mF = new JMenuItem("Stundeninhalte");
	private JMenuItem mR = new JMenuItem("Raeume");
	private JMenuItem mRF = new JMenuItem("Raumfunktionen");

	private RaumfunktionPanel raumfunktionPanel;
	private PersonalPanel personalPanel;
	private SchoolclassPanel schoolclassPanel;
	private StundeninhaltPanel stundeninhaltPanel;
	private RoomPanel roomPanel;


	public DataPanel() {
		initComponents();
	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		 c.insets=new Insets(5,5,5,5);

		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.weightx = 0.2;
		c.weighty = 1.8;
		c.gridx = 0;
		c.gridy = 1;
		mP.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mS.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mF.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mR.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mRF.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		menuBar.add(mP);
		menuBar.add(mS);
		menuBar.add(mF);
		menuBar.add(mR);
		menuBar.add(mRF);
		menuBar.setLayout(new GridLayout(0, 1));
		add(menuBar, c);

		// klick auf mP
		mP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				personalPanel = new PersonalPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(personalPanel, c);
				
				personalPanel.nameField.requestFocus();
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(personalPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});	

		// klick auf mS
		mS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				schoolclassPanel= new SchoolclassPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(schoolclassPanel, c);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(schoolclassPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mF
		mF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				stundeninhaltPanel= new StundeninhaltPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(stundeninhaltPanel, c);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(stundeninhaltPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mR
		mR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				roomPanel= new RoomPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(roomPanel, c);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(roomPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});		
	
	
	// klick auf mRF
			mRF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					raumfunktionPanel= new RaumfunktionPanel();
					c.fill = GridBagConstraints.BOTH;
					c.anchor = GridBagConstraints.EAST;
					c.gridwidth = 1;
					c.gridheight = 1;
					c.gridx = 1;
					c.gridy = 1;
					c.weightx = 1.8;
					c.weighty = 1.0;
					removeOld();
					add(raumfunktionPanel, c);
					JFrame frame = (JFrame) SwingUtilities
							.getWindowAncestor(raumfunktionPanel);
					SwingUtilities.updateComponentTreeUI(frame);
				}
			});
	}

	private void removeOld() {
		if(personalPanel !=null) remove(personalPanel);
		if(stundeninhaltPanel !=null) remove(stundeninhaltPanel);
		if(schoolclassPanel !=null) remove(schoolclassPanel);
		if(roomPanel !=null) remove(roomPanel);
		if(raumfunktionPanel !=null) remove(raumfunktionPanel);
	}
//
//	public static void updatepersonalList() {
//        try {
//			Collection<personal> personals = personalManager.getAllpersonals();
//			personalListModel.clear();
//            for (final personal personal : personals) {
//            	personalListModel.addpersonal(personal);
//            }
//		} catch (DatasetException e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	public static void updateSchoolclassList() {
//        try {
//			Collection<Schoolclass> schoolclasses = SchoolclassManager.getAllSchoolclasses();
//			schoolclassListModel.clear();
//            for (final Schoolclass schoolclass : schoolclasses) {
//            	schoolclassListModel.addSchoolclass(schoolclass);
//            }
//		} catch (DatasetException e1) {
//			e1.printStackTrace();
//		}
//	}
//
//	public static void updatestundeninhaltList() {
//        try {
//		Collection<stundeninhalt> stundeninhalts = stundeninhaltManager.getAllstundeninhalts();
//			stundeninhaltListModel.clear();
//            for (final stundeninhalt stundeninhalt : stundeninhalts) {
//            	stundeninhaltListModel.addstundeninhalt(stundeninhalt);
//            }
//		} catch (DatasetException e1) {
//			e1.printStackTrace();
//		}
//	}
}
