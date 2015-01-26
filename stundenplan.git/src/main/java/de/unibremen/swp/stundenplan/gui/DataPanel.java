package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * DatenPanel auf dem alle Editierungen des Datenbestandes vorgenommen werden
 * 
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class DataPanel extends JPanel {

	/**
	 * Menuebar auf der alle Panels anklickbar sind
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * MenuItem des Personals
	 */
	private JMenuItem mP;

	/**
	 * MenuItem der Schulklasse
	 */
	private JMenuItem mS;

	/**
	 * MenuItem des Stundeninhalts
	 */
	private JMenuItem mStdi;

	/**
	 * MenuItem des Raumes
	 */
	private JMenuItem mR;

	/**
	 * MenuItem der Raumfunktion
	 */
	private JMenuItem mRf;

	/**
	 * MenuItem Bedarfs der Jahrgaenge
	 */
	private JMenuItem mB;

	/**
	 * Raumfunktionspanel
	 */
	private RaumfunktionPanel raumfunktionPanel;

	/**
	 * Personalpanel
	 */
	private PersonalPanel personalPanel;

	/**
	 * Schulklassenpanel
	 */
	private SchoolclassPanel schoolclassPanel;

	/**
	 * Stundeninhaltpanel
	 */
	private StundeninhaltPanel stundeninhaltPanel;

	/**
	 * Raumpanel
	 */
	private RoomPanel roomPanel;

	/**
	 * Bedarfspanel
	 */
	private BedarfPanel bedarfPanel;
	
	/**
	 * Konstruktor des Datenpanel
	 */
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

	/**
	 * Initialisierungsauslagerung um Konstruktor nicht zu verstopfen
	 */
	private void initComponents() {
		mP = new JMenuItem("Personal");
		mS = new JMenuItem("Schulklassen");
		mStdi = new JMenuItem("Stundeninhalte");
		mR = new JMenuItem("Raeume");
		mRf = new JMenuItem("Raumfunktionen");
		mB = new JMenuItem("Jahrgangsbedarf");

		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.ipady = 80;
		c.gridx = 0;
		c.gridy = 0;
		// c.weightx = 0.05;
		// c.weighty = 0.5;
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
		c.gridy = 1;
		c.ipady = 0;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		add(new WarningPanel(), c);

		// klick auf mP
		mP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				personalPanel = new PersonalPanel();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(personalPanel, c);

				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(personalPanel));
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
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(schoolclassPanel, c);

				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(schoolclassPanel));
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
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(stundeninhaltPanel, c);

				if ((JFrame) SwingUtilities
						.getWindowAncestor(stundeninhaltPanel) != null)
					SwingUtilities
							.updateComponentTreeUI((JFrame) SwingUtilities
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
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(roomPanel, c);

				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(roomPanel));
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
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(raumfunktionPanel, c);
				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(raumfunktionPanel));
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
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(bedarfPanel, c);
				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(bedarfPanel));
			}
		});
	}

	/**
	 * loescht alle panel, damit sich nichts ueberlappt
	 */
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
}
