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

	private JMenuItem mT = new JMenuItem("Personal");
	private JMenuItem mS = new JMenuItem("Klassen");
	private JMenuItem mF = new JMenuItem("Stundeninhalte");
	private JMenuItem mR = new JMenuItem("Raeume");
	private JMenuItem mRF = new JMenuItem("Raumfunktionen");

	private RaumfunktionPanel raumfunktionPanel = new RaumfunktionPanel();
	private AddNewPersonal addNewPersonal = new AddNewPersonal();
	private AddNewStundeninhalt addNewStundeninhalt = new AddNewStundeninhalt();
	private AddNewSchoolclass addNewSchoolclass = new AddNewSchoolclass();
	private AddNewRoom addNewRoom = new AddNewRoom();

	private static PersonalListModel personalListModel = new PersonalListModel();
	private static SchoolclassListModel schoolclassListModel = new SchoolclassListModel();
	private static StundeninhaltListModel stundeninhaltListModel = new StundeninhaltListModel();
	private static RoomListModel roomListModel = new RoomListModel();
	
	private JList<String> personalList = new JList<String>(personalListModel);;
	private JList<String> schoolclassList = new JList<String>(schoolclassListModel);
	private JList<String> stundeninhaltList = new JList<String>(stundeninhaltListModel);
	private JList<String> roomList = new JList<String>(roomListModel);

	private JScrollPane personalListScroller = new JScrollPane(personalList);
	private JScrollPane schoolclassListScroller = new JScrollPane(schoolclassList);
	private JScrollPane stundeninhaltListScroller = new JScrollPane(stundeninhaltList);
	private JScrollPane roomListScroller = new JScrollPane(roomList);

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
		mT.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mS.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mF.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mR.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		mRF.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		
		menuBar.add(mT);
		menuBar.add(mS);
		menuBar.add(mF);
		menuBar.add(mR);
		menuBar.add(mRF);
		menuBar.setLayout(new GridLayout(0, 1));
		add(menuBar, c);

		// klick auf mT
		mT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(addNewPersonal, c);
				
				Personal p1 = new Personal();
				p1.setName("Mr. Boombastik");
				p1.setKuerzel("Boom");
			//	p1.setHoursPerWeek("20");
				personalListModel.addPersonal(p1);
				
				Personal p2 = new Personal();
				p2.setName("Fathan der Unglaubliche");
				p2.setKuerzel("Fath");
		//		p2.setHoursPerWeek("23");
				personalListModel.addPersonal(p2);
				
		   //     personalList = new JList<String>(personalListModel);
				personalList.setLayoutOrientation(JList.VERTICAL);
				personalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				personalListScroller.setPreferredSize(new Dimension(250, 200));
				// updatepersonalList();

				c.gridy = 2;
				personalListScroller.setBorder(BorderFactory.createTitledBorder("Existierendes Personal"));	
				add(personalListScroller, c);
				addPersonal(addNewPersonal.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewPersonal);
				SwingUtilities.updateComponentTreeUI(frame);
				
				personalList.addListSelectionListener(new ListSelectionListener() { 
					public void valueChanged(ListSelectionEvent event) {
						System.out.println("selected "+personalList.getSelectedValue()+" "+personalList.getSelectedIndex()+" "+personalList.getComponentCount());
						final DataPopup pop = new DataPopup(personalList, personalListModel);
						setComponentPopupMenu(pop);	 
						personalList.addMouseListener(new MouseAdapter() {
				            public void mouseClicked(MouseEvent e) {
				            	if ( e.isMetaDown() ) {  				            	
				            		pop.show(personalList,e.getX(),e.getY()); 
				            		System.out.println("YO");	
				                }  
				            }			      
				        });
						pop.edit.addActionListener(new ActionListener() {
	                        public void actionPerformed(ActionEvent ae) {
//	                       	 personal t = personalListModel.getpersonalAt(personalList.getSelectedIndex());
//	                       	 addNewpersonal.nameField.setText(t.getName());
//	                       	 addNewpersonal.acroField.setText(t.getAcronym());
//	                       	 addNewpersonal.timeField.setText((String)t.getHoursPerWeek().toString());
//	                       	 addNewpersonal.setVisible(true);
	                         EditPersonalFrame editPersonal = new EditPersonalFrame();
	                       	 editPersonal.setVisible(true);
	                    	 Personal p = personalListModel.getPersonalAt(personalList.getSelectedIndex());
	                       	 editPersonal.nameField.setText(p.getName());
	                       	 editPersonal.acroField.setText(p.getKuerzel());
	                    //   editPersonal.timeField.setText((String)p.getHoursPerWeek().toString());
	                       }
	                   });
						pop.delete.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								DeleteDialogue deleteD = new DeleteDialogue();
								deleteD.setVisible(true);
							}
						});
					}});
			}
		});	

		// klick auf mS
		mS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(addNewSchoolclass, c);

				schoolclassList.setLayoutOrientation(JList.VERTICAL);
				schoolclassList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				schoolclassListScroller.setPreferredSize(new Dimension(250, 200));
				// updatepersonalList();

				c.gridy = 2;
				schoolclassListScroller.setBorder(BorderFactory.createTitledBorder("Existierende Schulklassen"));
				add(schoolclassListScroller, c);
				addSchoolclass(addNewSchoolclass.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewSchoolclass);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mF
		mF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(addNewStundeninhalt, c);

				stundeninhaltList.setLayoutOrientation(JList.VERTICAL);
				stundeninhaltList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				stundeninhaltListScroller.setPreferredSize(new Dimension(250, 200));
				// updatepersonalList();

				c.gridy = 2;
				stundeninhaltListScroller.setBorder(BorderFactory.createTitledBorder("Existierende Faecher"));
				add(stundeninhaltListScroller, c);
				addStundeninhalt(addNewStundeninhalt.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewStundeninhalt);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		// klick auf mR
		mR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				c.fill = GridBagConstraints.BOTH;
				c.anchor = GridBagConstraints.EAST;
				c.gridwidth = 1;
				c.gridheight = 1;
				c.gridx = 1;
				c.gridy = 1;
				c.weightx = 1.8;
				c.weighty = 1.0;
				removeOld();
				add(addNewRoom, c);

				roomList.setLayoutOrientation(JList.VERTICAL);
				roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				roomListScroller.setPreferredSize(new Dimension(250, 200));
				// updatepersonalList();

				c.gridy = 2;
				roomListScroller.setBorder(BorderFactory.createTitledBorder("Existierende Räume"));
				add(roomListScroller, c);
				addRoom(addNewRoom.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewRoom);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});		
	
	
	// klick auf mRF
			mRF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
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

	private void addPersonal(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// updatepersonalList();
			}
		});
	}

	private void addSchoolclass(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//updateSchoolclassList();
			}
		});
	}

	private void addStundeninhalt(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//updatestundeninhaltList();
			}
		});
	}
	
	private void addRoom(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// updateRoomList();
			}
		});
	}

	private void removeOld() {
		remove(addNewPersonal);
		remove(addNewStundeninhalt);
		remove(addNewSchoolclass);
		remove(addNewRoom);
		remove(personalListScroller);
		remove(stundeninhaltListScroller);
		remove(schoolclassListScroller);
		remove(roomListScroller);
		remove(raumfunktionPanel);
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
