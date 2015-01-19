package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.naming.InvalidNameException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.Data;

public class ConfigPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3384783527067309086L;
	private JLabel label = new JLabel("Einstellungen");
	private JMenuBar menuBar = new JMenuBar();

	private JMenuItem mP = new JMenuItem("Dauer Planungseinheit");
	private JMenuItem mBI = new JMenuItem("Back-Up Intervall");
	private JMenuItem mWD = new JMenuItem("Zu verplanende Wochentage");
	private JMenuItem mDL = new JMenuItem("Dauer eines Wochentags");
	private JPanel plnConfig;
	private JPanel bkpConfig;
	private JPanel wdConfig;
	private JPanel dlConfig;

	public ConfigPanel() {
		init();
	}

	public void init() {
		try {
			Config.init(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 0.05;
		c.gridx = 0;
		c.gridy = 0;
		label.setFont(new Font("Arial", Font.BOLD, 15));
		add(label, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.weightx = 0.2;
		c.weighty = 1.8;
		c.gridx = 0;
		c.gridy = 1;

		mP.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mBI.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mWD.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mDL.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		menuBar.add(mP);
		menuBar.add(mBI);
		menuBar.add(mWD);
		menuBar.add(mDL);
		menuBar.setLayout(new GridLayout(0, 1));
		add(menuBar, c);

		// klick auf mP
		mP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				plnConfig = new PlanungsEinheitConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				add(plnConfig, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(plnConfig);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mBI
		mBI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				bkpConfig = new BackUpConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				add(bkpConfig, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(bkpConfig);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mWD
		mWD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				wdConfig = new WeekdayConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				add(wdConfig, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(wdConfig);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mDL
		mDL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				removeOld();
				dlConfig = new DaylengthConfig();
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				add(dlConfig, c);
				JFrame frame = (JFrame) SwingUtilities
						.getWindowAncestor(dlConfig);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});
	}

	private void removeOld() {
		if (plnConfig != null) remove(plnConfig);
		if (bkpConfig != null) remove(bkpConfig);
		if (wdConfig != null) remove(wdConfig);
		if (dlConfig != null) remove(dlConfig);
	}

	public class PlanungsEinheitConfig extends JPanel {
		private Label lTime = new Label("Dauer einer Planungseinheit:");
		private JTextField tf = new JTextField(2);
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");

		public PlanungsEinheitConfig() {
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
					Config.TIMESLOT_LENGTH = Integer.parseInt(tf.getText());
					Config.setIntValue(Config.TIMESLOT_LENGTH_STRING, Integer.parseInt(tf.getText())) ;
					System.out.println(Config.TIMESLOT_LENGTH);
				}
			});
		}
	}

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
					Config.BACKUPINTERVALL = Integer.parseInt(tf.getText());
					System.out.println(Config.BACKUPINTERVALL);
					Config.setIntValue(Config.BACKUPINTERVALL_STRING, Integer.parseInt(tf.getText()));
				}
			});
		}
	}

	public class WeekdayConfig extends JPanel {
		private Label lTime = new Label(
				"Waehlen sie die Wochentage des Stundenplans");
		private GridBagConstraints c = new GridBagConstraints();
		private JButton button = new JButton("Einstellungen speichern");
		private JComboBox jcb = new JComboBox();

		JCheckBox mo = new JCheckBox("Montag");
		JCheckBox di = new JCheckBox("Dienstag");
		JCheckBox mi = new JCheckBox("Mittwoch");
		JCheckBox don = new JCheckBox("Donnerstag");
		JCheckBox fr = new JCheckBox("Freitag");
		JCheckBox sa = new JCheckBox("Samstag");
		JCheckBox so = new JCheckBox("Sonntag");

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
			CheckBoxList checkList = new CheckBoxList();
			checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			JCheckBox[] boxes = { mo, di, mi, don, fr, sa, so };
			if (Weekday.MONDAY.isSchoolday()==true)
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
			add(jcb, c);
			c.gridy = 2;
			add(button, c);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String monday = mo.isSelected() ? "true" : "false";
					String tuesday = di.isSelected() ? "true" : "false";
					String wednesday = mi.isSelected() ? "true" : "false";
					String thursday = don.isSelected() ? "true" : "false";
					String friday = fr.isSelected() ? "true" : "false";
					String saturday = sa.isSelected() ? "true" : "false";
					String sunday = so.isSelected() ? "true" : "false";
					Config.setStringValue(Config.MONDAY_STRING, monday);
					Config.setStringValue(Config.TUESDAY_STRING, tuesday);
					Config.setStringValue(Config.WEDNESDAY_STRING, wednesday);
					Config.setStringValue(Config.THURSDAY_STRING, thursday);
					Config.setStringValue(Config.FRIDAY_STRING, friday);
					Config.setStringValue(Config.SATURDAY_STRING, saturday);
					Config.setStringValue(Config.SUNDAY_STRING, sunday);
				}
			});
		}
	}

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
			c.gridwidth=4;
			add(lTime, c);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 1;
			c.gridwidth=1;
			add(new Label(
					"Beginn :"),c);
			c.gridx = 1;
			add(start,c);
			c.gridx= 2;
			add(new Label(
					"Ende :"),c);
			c.gridx = 3;
			add(end,c);
			c.gridy = 2;
			c.gridwidth=4;
			c.gridx=0;
			add(button, c);
			String sh;
			String sm;
			String eh;
			String em;
			if(Config.DAY_STARTTIME_HOUR<10){ 
				sh="0"+Config.DAY_STARTTIME_HOUR;
			}else{
				sh=""+Config.DAY_STARTTIME_HOUR;
			};		
			if(Config.DAY_STARTTIME_MINUTE<10){ 
				sm="0"+Config.DAY_STARTTIME_MINUTE;
			}else{
				sm=""+Config.DAY_STARTTIME_MINUTE;
			};		
			if(Config.DAY_ENDTIME_HOUR<10){ 
				eh="0"+Config.DAY_ENDTIME_HOUR;
			}else{
				eh=""+Config.DAY_ENDTIME_HOUR;
			};		
			if(Config.DAY_ENDTIME_MINUTE<10){ 
				em="0"+Config.DAY_ENDTIME_MINUTE;
			}else{
				em=""+Config.DAY_ENDTIME_MINUTE;
			};		
			start.setText(sh+":"+sm);
			end.setText(eh+":"+em);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					try {
						Config.DAY_STARTTIME_HOUR=Integer.parseInt(start.getText(0,2));
						Config.setIntValue(Config.DAY_STARTTIME_HOUR_STRING, Integer.parseInt(start.getText(0,2)));
						Config.DAY_STARTTIME_MINUTE=Integer.parseInt(start.getText(3,2));
						Config.setIntValue(Config.DAY_STARTTIME_MINUTE_STRING, Integer.parseInt(start.getText(3,2)));
						Config.DAY_ENDTIME_HOUR=Integer.parseInt(start.getText(0,2));
						Config.setIntValue(Config.DAY_ENDTIME_HOUR_STRING, Integer.parseInt(end.getText(0,2)));
						Config.DAY_ENDTIME_MINUTE=Integer.parseInt(start.getText(3,2));
						Config.setIntValue(Config.DAY_ENDTIME_MINUTE_STRING, Integer.parseInt(end.getText(3,2)));
					} catch (NumberFormatException | BadLocationException e) {
						System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					}
				}
			});
		}
	}
}
