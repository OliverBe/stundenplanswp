package de.unibremen.swp.stundenplan.gui;

import java.awt.BorderLayout;
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

@SuppressWarnings("serial")
public class DataPanel extends JPanel {

	private JMenuBar menuBar = new JMenuBar();

	private JMenuItem mP = new JMenuItem("Personal");
	private JMenuItem mS = new JMenuItem("Schulklassen");
	private JMenuItem mStdi = new JMenuItem("Stundeninhalte");
	private JMenuItem mR = new JMenuItem("Raeume");
	private JMenuItem mRf = new JMenuItem("Raumfunktionen");
	private JMenuItem mB = new JMenuItem("Bedarf an Stundeninhalten");

	private RaumfunktionPanel raumfunktionPanel;

	private PersonalPanel personalPanel;
	private SchoolclassPanel schoolclassPanel;
	private StundeninhaltPanel stundeninhaltPanel;
	private RoomPanel roomPanel;
	private BedarfPanel bedarfPanel;
	private WarningPanel warningPanel = new WarningPanel();

	public DataPanel() {
		initComponents();
		mStdi.doClick();
	}
	
	public RaumfunktionPanel getRaumfunktionPanel() {
		return raumfunktionPanel;
	}
	
	public PersonalPanel getPersonalPanel() {
		return personalPanel;
	}
	
	public SchoolclassPanel getSchoolclassPanel() {
		return schoolclassPanel;
	}
	
	public StundeninhaltPanel getStundeninhaltPanel() {
		return stundeninhaltPanel;
	}
	
	public RoomPanel getRoomPanel() {
		return roomPanel;
	}
	
	public BedarfPanel getBedarfPanel() {
		return bedarfPanel;
	}

	private void initComponents() {

		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

//		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.ipady=80;
		c.gridx = 0;
		c.gridy = 0;
//		c.weightx = 0.05;
//		c.weighty = 0.5;
		mP.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mS.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mStdi.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mR.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mRf.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mB.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		menuBar.add(mStdi);
		menuBar.add(mB);
		menuBar.add(mRf);
		menuBar.add(mR);
		menuBar.add(mP);
		menuBar.add(mS);
		menuBar.setLayout(new GridLayout(0, 1));
		add(menuBar, c);
		c.gridy=1;
		c.ipady=0;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		add(warningPanel, c);

		// klick auf mP
		mP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				personalPanel = new PersonalPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(personalPanel, c);

				personalPanel.nameField.requestFocus();

				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(personalPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mS
		mS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				schoolclassPanel = new SchoolclassPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(schoolclassPanel, c);

				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(schoolclassPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mStdi
		mStdi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				stundeninhaltPanel = new StundeninhaltPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(stundeninhaltPanel, c);

				if((JFrame) SwingUtilities
						.getWindowAncestor(stundeninhaltPanel) != null) SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(stundeninhaltPanel));
			}
		});

		// klick auf mR
		mR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				roomPanel = new RoomPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(roomPanel, c);

				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(roomPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mRF
		mRf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				raumfunktionPanel = new RaumfunktionPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(raumfunktionPanel, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(raumfunktionPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
		
		// klick auf mB
		mB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				bedarfPanel = new BedarfPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight=10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(bedarfPanel, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(bedarfPanel);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
	}

	private void removeOld() {
		if (personalPanel != null)
			remove(personalPanel);
		if (stundeninhaltPanel != null)
			remove(stundeninhaltPanel);
		if (schoolclassPanel != null)
			remove(schoolclassPanel);
		if (roomPanel != null)
			remove(roomPanel);
		if (raumfunktionPanel != null)
			remove(raumfunktionPanel);
		if (bedarfPanel != null)
			remove(bedarfPanel);
	}
	//
	// public static void updatepersonalList() {
	// try {
	// Collection<personal> personals = personalManager.getAllpersonals();
	// personalListModel.clear();
	// for (final personal personal : personals) {
	// personalListModel.addpersonal(personal);
	// }
	// } catch (DatasetException e1) {
	// e1.printStackTrace();
	// }
	// }
	//
	// public static void updateSchoolclassList() {
	// try {
	// Collection<Schoolclass> schoolclasses =
	// SchoolclassManager.getAllSchoolclasses();
	// schoolclassListModel.clear();
	// for (final Schoolclass schoolclass : schoolclasses) {
	// schoolclassListModel.addSchoolclass(schoolclass);
	// }
	// } catch (DatasetException e1) {
	// e1.printStackTrace();
	// }
	// }
	//
	// public static void updatestundeninhaltList() {
	// try {
	// Collection<stundeninhalt> stundeninhalts =
	// stundeninhaltManager.getAllstundeninhalts();
	// stundeninhaltListModel.clear();
	// for (final stundeninhalt stundeninhalt : stundeninhalts) {
	// stundeninhaltListModel.addstundeninhalt(stundeninhalt);
	// }
	// } catch (DatasetException e1) {
	// e1.printStackTrace();
	// }
	// }
}
