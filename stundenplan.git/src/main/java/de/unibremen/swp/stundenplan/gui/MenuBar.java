package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.command.CommandHistory;
import de.unibremen.swp.stundenplan.config.ExportPDF;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.db.DataPlanungseinheit;
import de.unibremen.swp.stundenplan.exceptions.StundenplanException;
import de.unibremen.swp.stundenplan.logic.PlanungseinheitManager;

public class MenuBar extends JMenuBar {

	// JFileChooser-Objekt erstellen
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenu data = new JMenu("Datei");
	private JMenuItem neww = new JMenuItem("Neue Datei");
	private JMenuItem open = new JMenuItem("Oeffnen");
	private JMenuItem save = new JMenuItem("Speichern");
	private JMenuItem saveAs = new JMenuItem("Speichern unter");
	private JMenu export = new JMenu("Export");
	private JMenuItem exportPDF = new JMenuItem("Exportieren als PDF");
	private JMenuItem exportCSV = new JMenuItem("Exportieren als CSV");
	private JMenuItem exportDOC = new JMenuItem("Exportieren als DOC");

	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);

	private JFrame f;

	private ImageIcon revert = new ImageIcon(getClass().getResource(
			"revert.png"));
	private ImageIcon revertG = new ImageIcon(getClass().getResource(
			"revertGRUEN.png"));

	private JButton button1 = new JButton(revert);

	public MenuBar(final JFrame frame) {
		f = frame;

		initComponents();
	}

	/**
	 * Open Source Logo fuer Revert Button, Quelle:
	 * https://openclipart.org/detail
	 * /181114/square-undo-or-back-button-by-barrettward-181114
	 */
	private void initComponents() {

		export.add(exportPDF);
		export.add(exportCSV);
		export.add(exportDOC);
		data.add(neww);
		data.add(open);
		data.add(save);
		data.add(saveAs);

		add(data);
		add(export);
		add(Box.createHorizontalGlue());

		button1.setBorderPainted(false);
		button1.setMargin(new Insets(0, 0, 0, 0));
		button1.setContentAreaFilled(false);
		button1.setPressedIcon(revertG);
		add(button1);
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					CommandHistory.getLast().undo();
					((MainFrame) f).checkSelectedTab();
					CommandHistory.deleteLast();
				} catch (StundenplanException e) {
					System.out
							.println("[COMMANDHISTORY]: Keine Befehle in History.");
				}
			}
		});

		newClick(neww);
		openClick(open);
		saveClick(save);
		saveAsClick(saveAs);
		exportPDFClick(exportPDF);
		exportCSVClick(exportCSV);
		exportDOCClick(exportDOC);

		// Shortcuts
		neww.setAccelerator(KeyStroke.getKeyStroke('N',
				InputEvent.CTRL_DOWN_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke('O',
				InputEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S',
				InputEvent.CTRL_DOWN_MASK));
		exportPDF.setAccelerator(KeyStroke.getKeyStroke('E',
				InputEvent.CTRL_DOWN_MASK));
	}

	private void newClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!Data.isSaved()) {
                	int result = JOptionPane.showConfirmDialog(Stundenplan.getMain(), "Es wurden Veraenderungen vorgenommen.\nSoll gespeichert werden?", "Warnung", JOptionPane.YES_NO_OPTION);
                	if(result==JOptionPane.YES_OPTION) {
                		if(Data.getLastRestoredFileName().equals("")) {
                			Data.backup(Data.getLastRestoredFileName(), true);
                		}else {
                			Data.setLastRestoredFileName("");
                			final JFrame backupFrame = new JFrame();
            				GridBagConstraints c = new GridBagConstraints();
            				JPanel backupChooser = new JPanel();
            				final JTextField tf = new JTextField();
            				JButton button = new JButton("Erstellen");
            				JButton button2 = new JButton("Abbrechen");
            				backupChooser.setLayout(new GridBagLayout());
            				backupChooser.setBorder(BorderFactory
            						.createTitledBorder("Backup erstellen"));
            				c.fill = GridBagConstraints.BOTH;
            				c.insets = new Insets(8, 5, 1, 1);
            				c.anchor = GridBagConstraints.CENTER;
            				c.gridx = 0;
            				c.gridy = 0;
            				c.gridwidth = 2;
            				backupChooser.add(tf, c);
            				c.gridy = 1;
            				c.gridwidth = 1;
            				c.weightx = 0.6;
            				backupChooser.add(button, c);
            				c.gridx = 1;
            				c.weightx = 0.4;
            				backupChooser.add(button2, c);
            				backupFrame.add(backupChooser);
            				backupFrame.setTitle("Backup");
            				backupFrame.setLocation(MouseInfo.getPointerInfo()
            						.getLocation().x, MouseInfo.getPointerInfo()
            						.getLocation().y);
            				backupFrame.pack();
            				backupFrame.setVisible(true);

            				button.addActionListener(new ActionListener() {
            					public void actionPerformed(ActionEvent ae) {
            						backupFrame.dispose();
            						Data.backup(tf.getText(), true);
            					}
            				});

            				button2.addActionListener(new ActionListener() {
            					public void actionPerformed(ActionEvent ae) {
            						backupFrame.dispose();
            					}
            				});
                		}
                	}
                }
                Data.deleteAll();
				((MainFrame) f).updateAll();
			}
		});
	}

	private void openClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				final JFrame backupFrame = new JFrame();
				GridBagConstraints c = new GridBagConstraints();
				JPanel backupChooser = new JPanel();
				JButton button = new JButton("Oeffnen");
				JButton button2 = new JButton("Abbrechen");
				backupChooser.setLayout(new GridBagLayout());
				backupChooser.setBorder(BorderFactory
						.createTitledBorder("Datei auswaehlen"));
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(8, 5, 1, 1);
				c.anchor = GridBagConstraints.CENTER;
				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth = 2;
				list.setLayoutOrientation(JList.VERTICAL);
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				list.setEnabled(true);
				listScroller.setPreferredSize(new Dimension(300, 200));
				backupChooser.add(listScroller, c);
				c.gridy = 1;
				c.gridwidth = 1;
				c.weightx = 0.6;
				backupChooser.add(button, c);
				c.gridx = 1;
				c.weightx = 0.4;
				backupChooser.add(button2, c);
				backupFrame.add(backupChooser);
				backupFrame.setTitle("Oeffnen");
				backupFrame.setLocation(MouseInfo.getPointerInfo()
						.getLocation().x, MouseInfo.getPointerInfo()
						.getLocation().y);
				backupFrame.pack();
				backupFrame.setVisible(true);

				File dir = new File(System.getProperty("user.dir"));
				File[] files = dir.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String filename) {
						return filename.endsWith(".db")
								&& !filename.equals("temp.db");
					}
				});
				listModel.clear();
				for (File file : files) {
					listModel.addElement(file.getName());
				}

				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						Data.restore(list.getSelectedValue());
						backupFrame.dispose();
						if(PlanungseinheitManager.consistencecheck()){ // prueft ob geladene PEs zum Zeitraster passt
							DataPlanungseinheit.deleteAll();		   // wenn nicht wird alle PEs geloescht
						}
						((MainFrame) f).updateAll();
					}
				});

				button2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						backupFrame.dispose();
					}
				});
			}
		});
	}

	private void saveClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!Data.getLastRestoredFileName().equals("")) {
					Data.backup(Data.getLastRestoredFileName(), true);
					Data.setSaved(true);
				}else saveFrame();
			}
		});
	}
	
	private void saveAsClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveFrame();
			}
		});
	}
	
	private void saveFrame() {
		final JFrame backupFrame = new JFrame();
		GridBagConstraints c = new GridBagConstraints();
		JPanel backupChooser = new JPanel();
		final JTextField tf = new JTextField();
		JButton button = new JButton("Erstellen");
		JButton button2 = new JButton("Abbrechen");
		backupChooser.setLayout(new GridBagLayout());
		backupChooser.setBorder(BorderFactory
				.createTitledBorder("Datei benennen"));
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(8, 5, 1, 1);
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		backupChooser.add(tf, c);
		c.gridy = 1;
		list.setLayoutOrientation(JList.VERTICAL);
		list.setEnabled(false);
		listScroller.setPreferredSize(new Dimension(300, 200));
		backupChooser.add(listScroller, c);
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0.6;
		backupChooser.add(button, c);
		c.gridx = 1;
		c.weightx = 0.4;
		backupChooser.add(button2, c);
		backupFrame.add(backupChooser);
		backupFrame.setTitle("Speichern");
		backupFrame.setLocation(MouseInfo.getPointerInfo()
				.getLocation().x, MouseInfo.getPointerInfo()
				.getLocation().y);
		backupFrame.pack();
		backupFrame.setVisible(true);
		
		File dir = new File(System.getProperty("user.dir"));
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".db")
						&& !filename.equals("temp.db");
			}
		});
		listModel.clear();
		for (File file : files) {
			listModel.addElement(file.getName());
		}
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Data.setLastRestoredFileName(tf.getText());
				backupFrame.dispose();
				Data.backup(tf.getText(), false);
			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				backupFrame.dispose();
			}
		});
		Data.setSaved(true);
	}

	private void exportPDFClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JTable eTable = new JTable();
				Object obj = MainFrame
						.getTabPane()
						.getComponentAt(
								MainFrame.getTabPane()
										.getSelectedIndex());

				if (obj instanceof StundenplanPanel) {
					StundenplanPanel panel = (StundenplanPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createPDF(eTable);
					}
				} else if (obj instanceof RaumbelegungsplanPanel) {
					eTable = RaumbelegungsplanPanel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createPDF(eTable);
					}
				} else if (obj instanceof LehreransichtPanel) {
					LehreransichtPanel panel = (LehreransichtPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {

						ExportPDF.setOwner("Personalplan");
						ExportPDF.createPDF(eTable);
						ExportPDF.setOwner("");
					}
				} else if (obj instanceof WochenplanPanel) {
					obj = WochenplanPanel.getTabPane().getSelectedComponent();
					if (obj instanceof WochenplanTag) {
						WochenplanTag wpT = (WochenplanTag) obj;
						if (wpT.getTable() == null) {
							JOptionPane.showMessageDialog(
									Stundenplan.getMain(),
									"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
						} else {

							ExportPDF.setOwner("Wochenplan-"
									+ wpT.day.toString());
							ExportPDF.createPDF(wpT.getTable());
							ExportPDF.setOwner("");
						}
					}

				} else
					JOptionPane.showMessageDialog(
							Stundenplan.getMain(),
							"Exportieren ist nur in Stundenplaenen, Raumbelegungsplaenen, Lehreransicht und Wochenplanansicht moeglich");

			}
		});
	}

	private void exportCSVClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JTable eTable = new JTable();
				Object obj = MainFrame
						.getTabPane()
						.getComponentAt(
								MainFrame.getTabPane()
										.getSelectedIndex());

				if (obj instanceof StundenplanPanel) {
					StundenplanPanel panel = (StundenplanPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createCSV(eTable);
					}
				} else if (obj instanceof RaumbelegungsplanPanel) {
					eTable = RaumbelegungsplanPanel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createCSV(eTable);
					}
				} else if (obj instanceof LehreransichtPanel) {
					LehreransichtPanel panel = (LehreransichtPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {

						ExportPDF.setOwner("Personalplan");
						ExportPDF.createCSV(eTable);
						ExportPDF.setOwner("");
					}

				} else if (obj instanceof WochenplanPanel) {
					obj = WochenplanPanel.getTabPane().getSelectedComponent();
					if (obj instanceof WochenplanTag) {
						WochenplanTag wpT = (WochenplanTag) obj;
						if (wpT.getTable() == null) {
							JOptionPane.showMessageDialog(
									Stundenplan.getMain(),
									"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
						} else {
							ExportPDF.setOwner("Wochenplan-"
									+ wpT.day.toString());
							ExportPDF.createCSV(wpT.getTable());
							ExportPDF.setOwner("");
						}
					}

				} else
					JOptionPane.showMessageDialog(
							Stundenplan.getMain(),
							"Exportieren ist nur in Stundenplaenen, Raumbelegungsplaenen, Lehreransicht und Wochenplanansicht moeglich");

			}
		});
	}

	private void exportDOCClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				JTable eTable = new JTable();
				Object obj = MainFrame
						.getTabPane()
						.getComponentAt(
								MainFrame.getTabPane()
										.getSelectedIndex());

				if (obj instanceof StundenplanPanel) {
					StundenplanPanel panel = (StundenplanPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createDOC(eTable);
					}
				} else if (obj instanceof RaumbelegungsplanPanel) {
					eTable = RaumbelegungsplanPanel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {
						ExportPDF.createDOC(eTable);
					}
				} else if (obj instanceof LehreransichtPanel) {
					LehreransichtPanel panel = (LehreransichtPanel) obj;
					eTable = panel.getTable();
					if (eTable == null) {
						JOptionPane.showMessageDialog(Stundenplan.getMain(),
								"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
					} else {

						ExportPDF.setOwner("Personalplan");
						ExportPDF.createDOC(eTable);
						ExportPDF.setOwner("");
					}
				} else if (obj instanceof WochenplanPanel) {
					obj = WochenplanPanel.getTabPane().getSelectedComponent();
					if (obj instanceof WochenplanTag) {
						WochenplanTag wpT = (WochenplanTag) obj;
						if (wpT.getTable() == null) {
							JOptionPane.showMessageDialog(
									Stundenplan.getMain(),
									"Waehlen Sie einen Stundenplan aus und lassen Sie diesen anzeigen");
						} else {
							ExportPDF.setOwner("Wochenplan-"
									+ wpT.day.toString());
							ExportPDF.createDOC(wpT.getTable());
							ExportPDF.setOwner("");
						}
					}

				} else
					JOptionPane.showMessageDialog(
							Stundenplan.getMain(),
							"Exportieren ist nur in Stundenplaenen, Raumbelegungsplaenen, Lehreransicht und Wochenplanansicht moeglich");

			}
		});
	}

	private void undoClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {
				// undo
			}
		});
	}

}
