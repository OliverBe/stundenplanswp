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
import de.unibremen.swp.stundenplan.data.Subject;
import de.unibremen.swp.stundenplan.data.Teacher;
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

	private AddNewTeacher addNewTeacher = new AddNewTeacher();
	private AddNewStundeninhalt addNewSubject = new AddNewStundeninhalt();
	private AddNewSchoolclass addNewSchoolclass = new AddNewSchoolclass();
	private AddNewRoom addNewRoom = new AddNewRoom();

	private static TeacherListModel teacherListModel = new TeacherListModel();
	private static SchoolclassListModel schoolclassListModel = new SchoolclassListModel();
	private static StundeninhaltListModel subjectListModel = new StundeninhaltListModel();
	private static RoomListModel roomListModel = new RoomListModel();
	
	private JList<String> teacherList = new JList<String>(teacherListModel);;
	private JList<String> schoolclassList = new JList<String>(schoolclassListModel);
	private JList<String> subjectList = new JList<String>(subjectListModel);
	private JList<String> roomList = new JList<String>(roomListModel);

	private JScrollPane teacherListScroller = new JScrollPane(teacherList);
	private JScrollPane schoolclassListScroller = new JScrollPane(schoolclassList);
	private JScrollPane subjectListScroller = new JScrollPane(subjectList);
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
		
		menuBar.add(mT);
		menuBar.add(mS);
		menuBar.add(mF);
		menuBar.add(mR);
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
				add(addNewTeacher, c);
				
				Teacher t1 = new Teacher();
				t1.setName("Mr. Boombastik");
				t1.setAcronym("Boom");
				t1.setHoursPerWeek("20");
				teacherListModel.addTeacher(t1);
				
				Teacher t2 = new Teacher();
				t2.setName("Fathan der Unglaubliche");
				t2.setAcronym("Fath");
				t2.setHoursPerWeek("23");
				teacherListModel.addTeacher(t2);
				
		   //     teacherList = new JList<String>(teacherListModel);
				teacherList.setLayoutOrientation(JList.VERTICAL);
				teacherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				teacherListScroller.setPreferredSize(new Dimension(250, 200));
				// updateTeacherList();

				c.gridy = 2;
				teacherListScroller.setBorder(BorderFactory.createTitledBorder("Existierendes Personal"));	
				add(teacherListScroller, c);
				addTeacher(addNewTeacher.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewTeacher);
				SwingUtilities.updateComponentTreeUI(frame);
				
				teacherList.addListSelectionListener(new ListSelectionListener() { 
					public void valueChanged(ListSelectionEvent event) {
						System.out.println("selected "+teacherList.getSelectedValue()+" "+teacherList.getSelectedIndex()+" "+teacherList.getComponentCount());
						final DataPopup pop = new DataPopup(teacherList, teacherListModel);
						setComponentPopupMenu(pop);	 
						teacherList.addMouseListener(new MouseAdapter() {
				            public void mouseClicked(MouseEvent e) {
				            	if ( e.isMetaDown() ) {  				            	
				            		pop.show(teacherList,e.getX(),e.getY()); 
				            		System.out.println("YO");	
				                }  
				            }			      
				        });
						pop.edit.addActionListener(new ActionListener() {
	                        public void actionPerformed(ActionEvent ae) {
//	                       	 Teacher t = teacherListModel.getTeacherAt(teacherList.getSelectedIndex());
//	                       	 addNewTeacher.nameField.setText(t.getName());
//	                       	 addNewTeacher.acroField.setText(t.getAcronym());
//	                       	 addNewTeacher.timeField.setText((String)t.getHoursPerWeek().toString());
//	                       	 addNewTeacher.setVisible(true);
	                         EditTeacherFrame editTeacher = new EditTeacherFrame();
	                       	 editTeacher.setVisible(true);
	                    	 Teacher t = teacherListModel.getTeacherAt(teacherList.getSelectedIndex());
	                       	 editTeacher.nameField.setText(t.getName());
	                       	 editTeacher.acroField.setText(t.getAcronym());
	                       	 editTeacher.timeField.setText((String)t.getHoursPerWeek().toString());
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
				// updateTeacherList();

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
				add(addNewSubject, c);

				subjectList.setLayoutOrientation(JList.VERTICAL);
				subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				subjectListScroller.setPreferredSize(new Dimension(250, 200));
				// updateTeacherList();

				c.gridy = 2;
				subjectListScroller.setBorder(BorderFactory.createTitledBorder("Existierende Faecher"));
				add(subjectListScroller, c);
				addSubject(addNewSubject.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewSubject);
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
				// updateTeacherList();

				c.gridy = 2;
				roomListScroller.setBorder(BorderFactory.createTitledBorder("Existierende Räume"));
				add(roomListScroller, c);
				addRoom(addNewRoom.button);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(addNewRoom);
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});		
	}

	private void addTeacher(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// updateTeacherList();
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

	private void addSubject(JButton b) {
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				//updateSubjectList();
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
		remove(addNewTeacher);
		remove(addNewSubject);
		remove(addNewSchoolclass);
		remove(addNewRoom);
		remove(teacherListScroller);
		remove(subjectListScroller);
		remove(schoolclassListScroller);
		remove(roomListScroller);
	}
//
//	public static void updateTeacherList() {
//        try {
//			Collection<Teacher> teachers = TeacherManager.getAllTeachers();
//			teacherListModel.clear();
//            for (final Teacher teacher : teachers) {
//            	teacherListModel.addTeacher(teacher);
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
//	public static void updateSubjectList() {
//        try {
//		Collection<Subject> subjects = SubjectManager.getAllSubjects();
//			subjectListModel.clear();
//            for (final Subject subject : subjects) {
//            	subjectListModel.addSubject(subject);
//            }
//		} catch (DatasetException e1) {
//			e1.printStackTrace();
//		}
//	}
}
