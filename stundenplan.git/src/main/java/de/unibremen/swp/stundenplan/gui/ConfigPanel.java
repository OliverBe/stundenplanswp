package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.exceptions.WochentagException;
import de.unibremen.swp.stundenplan.exceptions.ZahlException;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

/**
 * Konfigurationspanel zur Einstellung von Wochenplantage, BackupInvertallen etc
 * 
 * @author Oliver
 *
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel {

	/**
	 * Menuebar mit einzelnen Einstellungspunkten
	 */
	private JMenuBar menuBar = new JMenuBar();

	/**
	 * Menuitem fuer die Dauer eines Timeslots
	 */
	private JMenuItem mT;

	/**
	 * Menuitem fuer das Backupintervall
	 */
	private JMenuItem mBI;

	/**
	 * Menuitem fuer die zu verplanenenden Wochentage
	 */
	private JMenuItem mWD;

	/**
	 * Menuitem fuer die Tagesdauer
	 */
	private JMenuItem mDL;

	/**
	 * Timeslotpanel
	 */
	private JPanel tsConfig;

	/**
	 * Backuppanel
	 */
	private JPanel bkpConfig;

	/**
	 * Wochentagpanel
	 */
	private JPanel wdConfig;

	/**
	 * Tageslaengepanel
	 */
	private JPanel dlConfig;

	/**
	 * Konstruktor des Einstellungspanels
	 */
	public ConfigPanel() {
		init();
		mT.doClick();
	}

	/**
	 * Auslagerung der Initialisierung
	 */
	public void init() {
		try {
			Config.init(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mT = new JMenuItem("Timeslotdauer");
		mBI = new JMenuItem("Back-Up-Intervall");
		mWD = new JMenuItem("Zu verplanende Wochentage");
		mDL = new JMenuItem("Dauer eines Wochentages");

		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 80;
		c.gridx = 0;
		c.gridy = 0;

		mT.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mBI.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mWD.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mDL.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		menuBar.add(mT);
		menuBar.add(mBI);
		menuBar.add(mWD);
		menuBar.add(mDL);
		menuBar.setLayout(new GridLayout(0, 1));
		add(menuBar, c);

		c.gridy = 1;
		c.ipady = 0;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		add(new WarningPanel(), c);

		// klick auf mT
		mT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				tsConfig = new TimeslotConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(tsConfig, c);
				if ((JFrame) SwingUtilities.getWindowAncestor(tsConfig) != null)
					SwingUtilities
							.updateComponentTreeUI((JFrame) SwingUtilities
									.getWindowAncestor(tsConfig));

			}
		});

		// klick auf mBI
		mBI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				bkpConfig = new BackUpConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(bkpConfig, c);
				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(bkpConfig));
			}
		});

		// klick auf mWD
		mWD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				wdConfig = new WeekdayConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(wdConfig, c);
				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(wdConfig));
			}
		});

		// klick auf mDL
		mDL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				dlConfig = new DaylengthConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridx = 1;
				c.gridy = 0;
				c.gridheight = 10;
				c.weightx = 0.95;
				c.weighty = 1;
				add(dlConfig, c);
				SwingUtilities.updateComponentTreeUI((JFrame) SwingUtilities
						.getWindowAncestor(dlConfig));
			}
		});
	}

	/**
	 * Loeschte alle panel um Ueberlappung zu verhindern
	 */
	private void removeOld() {
		if (tsConfig != null)
			remove(tsConfig);
		if (bkpConfig != null)
			remove(bkpConfig);
		if (wdConfig != null)
			remove(wdConfig);
		if (dlConfig != null)
			remove(dlConfig);
	}

	/**
	 * Panel fuer die Timeslotdauereinstellung
	 * 
	 * @author Oliver
	 *
	 */
	public class TimeslotConfig extends JPanel {
		private Label lTime = new Label("Dauer eines Timeslots:");
		private JTextField tf = new JTextField(2);
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");

		public TimeslotConfig() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory
					.createTitledBorder("Dauer einer Planungseinheit"));
			c.insets = new Insets(1, 1, 1, 1);
			c.gridx = 0;
			c.gridy = 0;
			add(lTime, c);
			c.gridx = 1;
			add(tf, c);
			c.gridx = 2;
			add(new Label("Minuten"), c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			add(button, c);
			tf.setText("" + Timeslot.timeslotlength());
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (!DataPlanungseinheit.isEmpty()) {
						Object[] options = { "Planungseinheiten loeschen und speichern!", "Nichts loeschen und abbrechen!"};
						 int selected=JOptionPane.showOptionDialog(null,
								"Wenn Sie die Timeslotlaenge aendern, werden alle Planungseinheiten aus Ueberschneidungsgruenden geloescht!", "WARNUNG!",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, options,
								options[1]);
						if(selected==0){
							DataPlanungseinheit.deleteAll();
						}else{
							return;
						}
					}
					try {
						if (Integer.parseInt(tf.getText()) > 0) {
							Config.TIMESLOT_LENGTH = Integer.parseInt(tf
									.getText());
							Config.setIntValue(Config.TIMESLOT_LENGTH_STRING,
									Integer.parseInt(tf.getText()));
						} else {
							new ZahlException();
						}
					} catch (NumberFormatException e) {
						new ZahlException();
					}
				}
			});
		}
	}

	/**
	 * Panel fuer das Backupintervall
	 * 
	 * @author Oliver
	 *
	 */
	public class BackUpConfig extends JPanel {
		private Label lTime = new Label("Backup alle");
		private JTextField tf = new JTextField(2);
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");

		public BackUpConfig() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createTitledBorder("Backupintervall "));
			c.insets = new Insets(1, 1, 1, 1);
			c.gridx = 0;
			c.gridy = 0;
			add(lTime, c);
			c.gridx = 1;
			add(tf, c);
			c.gridx = 2;
			add(new Label("Minuten"), c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 1;
			add(button, c);
			tf.setText("" + Config.BACKUPINTERVALL);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						if (Integer.parseInt(tf.getText()) > 0) {
							Config.BACKUPINTERVALL = Integer.parseInt(tf
									.getText());
							Config.setIntValue(Config.BACKUPINTERVALL_STRING,
									Integer.parseInt(tf.getText()));
						} else {
							new ZahlException();
						}
					} catch (NumberFormatException e) {
						new ZahlException();
					}
				}
			});
		}
	}

	/**
	 * Panel die Wochentagverplanungseinstellung
	 * 
	 * @author Oliver
	 *
	 */
	public class WeekdayConfig extends JPanel {
		private Label lTime = new Label(
				"Waehlen sie die Wochentage des Stundenplans");
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");

		JCheckBox mo = new JCheckBox("Montag");
		JCheckBox di = new JCheckBox("Dienstag");
		JCheckBox mi = new JCheckBox("Mittwoch");
		JCheckBox don = new JCheckBox("Donnerstag");
		JCheckBox fr = new JCheckBox("Freitag");
		JCheckBox sa = new JCheckBox("Samstag");
		JCheckBox so = new JCheckBox("Sonntag");

		@SuppressWarnings("unchecked")
		public WeekdayConfig() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory
					.createTitledBorder("Wochentage des Stundenplanes "));
			c.insets = new Insets(1, 1, 1, 1);
			c.gridx = 0;
			c.gridy = 0;
			add(lTime, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 1;
			final CheckBoxList checkList = new CheckBoxList();
			checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			JCheckBox[] boxes = { mo, di, mi, don, fr, sa, so };
			if (Weekday.MONDAY.isSchoolday() == true)
				mo.setSelected(true);
			if (Weekday.TUESDAY.isSchoolday() == true)
				di.setSelected(true);
			if (Weekday.WEDNESDAY.isSchoolday() == true)
				mi.setSelected(true);
			if (Weekday.THURSDAY.isSchoolday() == true)
				don.setSelected(true);
			if (Weekday.FRIDAY.isSchoolday() == true)
				fr.setSelected(true);
			if (Weekday.SATURDAY.isSchoolday() == true)
				sa.setSelected(true);
			if (Weekday.SUNDAY.isSchoolday() == true)
				so.setSelected(true);
			checkList.setListData(boxes);
			add(checkList, c);
			c.gridy = 2;
			add(button, c);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (!DataPlanungseinheit.isEmpty()) {
						Object[] options = { "Planungseinheiten loeschen und speichern!", "Nichts loeschen und abbrechen!"};
						 int selected=JOptionPane.showOptionDialog(null,
								"Wenn Sie die Tage aendern, werden alle Planungseinheiten aus Ueberschneidungsgruenden geloescht!", "WARNUNG!",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, options,
								options[1]);
						if(selected==0){
							DataPlanungseinheit.deleteAll();
						}else{
							return;
						}
					}
					boolean b = false;
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel()
								.getElementAt(i);
						if (cb.isSelected())
							b = true;
					}
					if (!b) {
						new WochentagException();
					} else {
						String monday = mo.isSelected() ? "true" : "false";
						String tuesday = di.isSelected() ? "true" : "false";
						String wednesday = mi.isSelected() ? "true" : "false";
						String thursday = don.isSelected() ? "true" : "false";
						String friday = fr.isSelected() ? "true" : "false";
						String saturday = sa.isSelected() ? "true" : "false";
						String sunday = so.isSelected() ? "true" : "false";
						Config.setStringValue(Config.MONDAY_STRING, monday);
						Config.setStringValue(Config.TUESDAY_STRING, tuesday);
						Config.setStringValue(Config.WEDNESDAY_STRING,
								wednesday);
						Config.setStringValue(Config.THURSDAY_STRING, thursday);
						Config.setStringValue(Config.FRIDAY_STRING, friday);
						Config.setStringValue(Config.SATURDAY_STRING, saturday);
						Config.setStringValue(Config.SUNDAY_STRING, sunday);
					}
				}
			});
		}
	}

	/**
	 * Panel fuer die Tageslaengeneinstellung
	 * 
	 * @author Oliver
	 *
	 */
	public class DaylengthConfig extends JPanel {
		private Label lTime = new Label(
				"Waehlen sie die Laenge eines Wochentages");
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");
		private JTextField start = new JTextField(5);
		private JTextField end = new JTextField(5);

		public DaylengthConfig() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory
					.createTitledBorder("Laenge der Wochentages"));
			c.insets = new Insets(1, 1, 1, 1);
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 4;
			add(lTime, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 1;
			c.gridwidth = 1;
			add(new Label("Beginn :"), c);
			c.gridx = 1;
			add(start, c);
			c.gridx = 2;
			add(new Label("Ende :"), c);
			c.gridx = 3;
			add(end, c);
			c.gridy = 2;
			c.gridwidth = 4;
			c.gridx = 0;
			add(button, c);
			String sh;
			String sm;
			String eh;
			String em;
			if (TimetableManager.startTimeHour() < 10) {
				sh = "0" + TimetableManager.startTimeHour();
			} else {
				sh = "" + TimetableManager.startTimeHour();
			}

			if (TimetableManager.startTimeMinute() < 10) {
				sm = "0" + TimetableManager.startTimeMinute();
			} else {
				sm = "" + TimetableManager.startTimeMinute();
			}

			if (TimetableManager.endTimeHour() < 10) {
				eh = "0" + TimetableManager.endTimeHour();
			} else {
				eh = "" + TimetableManager.endTimeHour();
			}

			if (TimetableManager.endTimeMinute() < 10) {
				em = "0" + TimetableManager.endTimeMinute();
			} else {
				em = "" + TimetableManager.endTimeMinute();
			}

			start.setText(sh + ":" + sm);
			end.setText(eh + ":" + em);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (!DataPlanungseinheit.isEmpty()) {
						Object[] options = { "Planungseinheiten loeschen und speichern!", "Nichts loeschen und abbrechen!"};
						 int selected=JOptionPane.showOptionDialog(null,
								"Wenn Sie die Tageslaenge aendern, werden alle Planungseinheiten aus Ueberschneidungsgruenden geloescht!", "WARNUNG!",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, options,
								options[1]);
						if(selected==0){
							DataPlanungseinheit.deleteAll();
						}else{
							return;
						}
					}
					try {
						if (Integer.parseInt(start.getText(0, 2)) >= 0
								&& Integer.parseInt(start.getText(0, 2)) <= 23
								&& Integer.parseInt(start.getText(3, 2)) >= 0
								&& Integer.parseInt(start.getText(3, 2)) <= 59
								&& Integer.parseInt(end.getText(0, 2)) >= 0
								&& Integer.parseInt(end.getText(0, 2)) <= 23
								&& Integer.parseInt(end.getText(3, 2)) >= 0
								&& Integer.parseInt(end.getText(3, 2)) <= 59) {
							Config.DAY_STARTTIME_HOUR = Integer.parseInt(start
									.getText(0, 2));
							Config.setIntValue(
									Config.DAY_STARTTIME_HOUR_STRING,
									Integer.parseInt(start.getText(0, 2)));
							Config.DAY_STARTTIME_MINUTE = Integer
									.parseInt(start.getText(3, 2));
							Config.setIntValue(
									Config.DAY_STARTTIME_MINUTE_STRING,
									Integer.parseInt(start.getText(3, 2)));
							Config.DAY_ENDTIME_HOUR = Integer.parseInt(start
									.getText(0, 2));
							Config.setIntValue(Config.DAY_ENDTIME_HOUR_STRING,
									Integer.parseInt(end.getText(0, 2)));
							Config.DAY_ENDTIME_MINUTE = Integer.parseInt(start
									.getText(3, 2));
							Config.setIntValue(
									Config.DAY_ENDTIME_MINUTE_STRING,
									Integer.parseInt(end.getText(3, 2)));
						} else {
							new ZahlException();
						}
						;
					} catch (NumberFormatException | BadLocationException e) {
						new ZahlException();
					}
				}
			});
		}
	}
}
