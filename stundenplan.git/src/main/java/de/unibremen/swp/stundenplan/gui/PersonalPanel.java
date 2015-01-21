package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.config.Weekday;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;
import de.unibremen.swp.stundenplan.logic.StundeninhaltManager;
import de.unibremen.swp.stundenplan.logic.TimetableManager;

public class PersonalPanel extends JPanel {

	private Label lName = new Label("Name des Personals:");
	private Label lKuerz = new Label("Kuerzel:");
	private Label lPrefTime = new Label("Zeitwunsch:");

	private Label lTime = new Label("Zeitverpflichtung in h:");
	private Label lErsatz = new Label("Ersatzzeit in h:");

	public TextField nameField = new TextField(15);
	public TextField kuerzField = new TextField(5);
	private TextField timeField = new TextField(5);
	private TextField timeField2 = new TextField(5);
	private TextField ersatzField = new TextField(5);
	private TextField ersatzField2 = new TextField(5);
	
	private JLabel lSubjects = new JLabel("Moegliche Stundeninhalte :");

	public JButton button = new JButton("Personal hinzufuegen");

	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();

	private static DefaultListModel<Personal> listModel = new DefaultListModel();
	private JList<Personal> list = new JList<Personal>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);
	
	private DefaultTableModel model;
	
	private DefaultTableModel model2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1219589162309740553L;

	public PersonalPanel() {
		setLayout(new GridBagLayout());
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()), c2);
		c2.gridy = 2;
		add(createListPanel(new JPanel()), c2);
	}

	@SuppressWarnings("serial")
	private JPanel createAddPanel(final JPanel p) {		
		model = new DefaultTableModel(){
		    @Override
			public boolean isCellEditable(int row, int column)
		    {
		        if(column == 0){ 
		        	return false;
		        }else{
		        	return true;
		        }
		    }
		};
		

		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory
				.createTitledBorder("Neues Personal hinzufuegen"));
		c.insets = new Insets(10, 5, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName, c);
		c.gridx = 1;
		p.add(nameField, c);
		c.gridx = 2;
		final JRadioButton lehrerB = new JRadioButton("Lehrer");
		lehrerB.setSelected(true);
		JRadioButton paedagogeB = new JRadioButton("Paedagoge");
		ButtonGroup group = new ButtonGroup();
		group.add(lehrerB);
		group.add(paedagogeB);
		p.add(lehrerB, c);
		c.gridx = 3;
		p.add(paedagogeB, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(lKuerz, c);
		c.gridx = 1;
		p.add(kuerzField, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(lTime, c);
		c.gridx = 1;
		p.add(timeField, c);
		c.gridx = 2;
		p.add(lErsatz, c);
		c.gridx = 3;
		p.add(ersatzField, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL),c);
		c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		c.gridy=4;
		p.add(lPrefTime, c);

		JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);

		model.addColumn("Wochentag");
		model.addColumn("Von (h)");
		model.addColumn("Von (min)");
		model.addColumn("Bis (h)");
		model.addColumn("Bis (min)");
		
		TableColumn column = null;
		for (int i = 0; i < model.getColumnCount(); i++) {
	        column = table.getColumnModel().getColumn(i);
	        if (i == 0) {
	            column.setPreferredWidth(100); 
	        } else {
	            column.setPreferredWidth(20);
	        }
	    } 

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
		

		final ArrayList<Weekday> wds = new ArrayList<Weekday>();

		if (Weekday.MONDAY.isSchoolday()) {
			model.addRow(new String[] { Config.MONDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.MONDAY);
		}
		if (Weekday.TUESDAY.isSchoolday()) {
			model.addRow(new String[] { Config.TUESDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.TUESDAY);
		}
		if (Weekday.WEDNESDAY.isSchoolday()) {
			model.addRow(new String[] { Config.WEDNESDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.WEDNESDAY);
		}
		if (Weekday.THURSDAY.isSchoolday()) {
			model.addRow(new String[] { Config.THURSDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.THURSDAY);
		}
		if (Weekday.FRIDAY.isSchoolday()) {
			model.addRow(new String[] { Config.FRIDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.FRIDAY);
		}
		if (Weekday.SATURDAY.isSchoolday()) {
			model.addRow(new String[] { Config.SATURDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.SATURDAY);
		}
		if (Weekday.SUNDAY.isSchoolday()) {
			model.addRow(new String[] { Config.SUNDAY_STRING, sh, sm, eh, em });
			wds.add(Weekday.SUNDAY);
		}

		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table.getTableHeader(), c);
		c.gridy = 6;
		p.add(table, c);
		c.gridy=0;
		c.gridx=5;
		c.gridwidth=1;
		c.gridheight=8;
		c.fill = GridBagConstraints.VERTICAL;
		p.add(new JSeparator(SwingConstants.VERTICAL),c);
		
		c.gridx= 6; 
		c.gridwidth=3;
		c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSubjects.setFont(new Font(nameField.getFont().getFontName(),
				Font.PLAIN, nameField.getFont().getSize()));
		p.add(lSubjects, c);
		
		c.anchor = GridBagConstraints.WEST;
		c.fill=GridBagConstraints.BOTH;
		c.gridheight = 7;
		c.gridy=1;
		final CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		 for(Stundeninhalt s : StundeninhaltManager.getAllStundeninhalteFromDB()){
			 boxes.add(new JCheckBox(s.getKuerzel()));
		 };

		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);
		
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 9;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button, c);

		// add Button
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					
					if (!check(p))
						throw new WrongInputException();
					HashMap<Weekday, int[]> wunsch = new HashMap<Weekday, int[]>();

					for (int i = 0; i < wds.size(); i++) {
						int[] arr = {Integer.parseInt((String) model.getValueAt(i,1)),
								Integer.parseInt((String) model.getValueAt(i,2)),
								Integer.parseInt((String) model.getValueAt(i,3)),
								Integer.parseInt((String) model.getValueAt(i,4))};
					
						wunsch.put(wds.get(i), arr);
					};

					ArrayList<String> stdi = new ArrayList<String>();
					int zaehler=0;
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
						if(cb.isSelected()) {
							zaehler++;
							stdi.add(cb.getText());
						}
					}
					
					Personal pe = new Personal(nameField.getText(), kuerzField
							.getText(), Integer.parseInt(timeField.getText()), Integer.parseInt(ersatzField.getText()),
							lehrerB.isSelected(), stdi, wunsch);
					
					PersonalManager.addPersonalToDb(pe);
					
					updateList();					

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
		return p;
	}

	private JPanel createListPanel(final JPanel p) {
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Existierendes Personal"));

		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroller.setPreferredSize(new Dimension(250, 200));

		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1.8;
		c.weighty = 1.0;
		p.add(listScroller, c);

		updateList();
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				final DataPopup pop = new DataPopup(list, listModel);
				setComponentPopupMenu(pop);
				list.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.isMetaDown()) {
							pop.show(list, e.getX(), e.getY());
						}
					}
				});
				pop.edit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame edit = new JFrame("Personal editieren");
						edit.add(createEditPanel(new JPanel(),list.getSelectedValue()));
						edit.setLocation(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y);
						edit.pack();
						edit.setVisible(true);
					}
				});
				pop.delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						DeleteDialogue deleteD = new DeleteDialogue(list.getSelectedValue());
						deleteD.setLocation(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y);
						deleteD.setVisible(true);
					}
				});
			}
		});
		return p;
	}
	
	@SuppressWarnings("serial")
	private JPanel createEditPanel(final JPanel p, final Personal pe) {
		
		Label lName2 = new Label("Name des Personals:");
		Label lKuerz2 = new Label("Kuerzel:");
		Label lPrefTime2 = new Label("Zeitwunsch:");
		Label lTime2 = new Label("Zeitverpflichtung in h:");
		Label lErsatz2 = new Label("Ersatzzeit in h:");
		final TextField nameField2 = new TextField(15);
		final TextField kuerzField2 = new TextField(5);
		JLabel lSubjects2 = new JLabel(
				"<html><body>Moegliche<br>Stundeninhalte :</body></html>");
		JButton button2 = new JButton("Speichern");
		JButton button3 = new JButton("Abbrechen");
		
		model2 = new DefaultTableModel(){
		    @Override
			public boolean isCellEditable(int row, int column)
		    {
		        if(column == 0){ 
		        	return false;
		        }else{
		        	return true;
		        }
		    }
		};
		

		c=new GridBagConstraints();
		p.setLayout(new GridBagLayout());
		c.insets = new Insets(1, 1, 1, 1);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		p.add(lName2, c);
		c.gridx = 1;
		p.add(nameField2, c);
		nameField2.setText(pe.getName());
		
		c.gridx = 2;
		final JRadioButton lehrerB2 = new JRadioButton("Lehrer");
		JRadioButton paedagogeB2 = new JRadioButton("Paedagoge");
		if(pe.isLehrer()){
			lehrerB2.setSelected(true);
		}else{
			paedagogeB2.setSelected(true);
		}
		ButtonGroup group2 = new ButtonGroup();
		group2.add(lehrerB2);
		group2.add(paedagogeB2);
		p.add(lehrerB2, c);
		c.gridx = 3;
		p.add(paedagogeB2, c);

		c.gridx = 0;
		c.gridy = 1;
		p.add(lKuerz2, c);
		c.gridx = 1;
		p.add(kuerzField2, c);
		kuerzField2.setText(pe.getKuerzel());

		c.gridx = 0;
		c.gridy = 2;
		p.add(lTime2, c);
		c.gridx = 1;
		p.add(timeField2, c);
		timeField2.setText(pe.getSollZeit()+"");
		c.gridx = 2;
		p.add(lErsatz2, c);
		c.gridx = 3;
		p.add(ersatzField2, c);
		ersatzField2.setText(pe.getErsatzZeit()+"");
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JSeparator(SwingConstants.HORIZONTAL),c);
		c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		c.gridy=4;
		p.add(lPrefTime2, c);

		JTable table2 = new JTable(model2);
		table2.setColumnSelectionAllowed(false);

		model2.addColumn("Wochentag");
		model2.addColumn("Von (h)");
		model2.addColumn("Von (min)");
		model2.addColumn("Bis (h)");
		model2.addColumn("Bis (min)");

		String sh;
		String sm;
		String eh;
		String em;
		
		Map<Weekday, int[]> map =new TreeMap<Weekday, int[]>(pe.getWunschzeiten());		
		for(Entry<Weekday, int[]> entry : map.entrySet()) {
			int[] arr = entry.getValue();
			if(arr[0] < 10) {
				sh="0"+arr[0];
			}else{
				sh=""+arr[0];
			}
			if(arr[1] < 10) {
				sm="0"+arr[1];
			}else{
				sm=""+arr[1];
			}
			if(arr[2] < 10) {
				eh="0"+arr[2];
			}else{
				eh=""+arr[2];
			}
			if(arr[3] < 10) {
				em="0"+arr[3];
			}else{
				em=""+arr[3];
			}
			model2.addRow(new String[] { entry.getKey().toString(), sh, sm, eh, em});
		}
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(table2.getTableHeader(), c);
		c.gridy = 6;
		p.add(table2, c);
		c.gridy=7;
		p.add(new JSeparator(SwingConstants.HORIZONTAL),c);

		c.gridy = 8;
		c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTH;
		lSubjects2.setFont(new Font(nameField2.getFont().getFontName(),
				Font.PLAIN, nameField2.getFont().getSize()));
		p.add(lSubjects2, c);
		
		c.anchor = GridBagConstraints.WEST;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.gridheight = 2;
		c.gridy=9;
		final CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();

		for(Stundeninhalt s : StundeninhaltManager.getAllStundeninhalteFromDB()){
			 boxes.add(new JCheckBox(s.getKuerzel()));
		}
		 
		for(JCheckBox jcb : boxes){
			for(String s : pe.getMoeglicheStundeninhalte()){
				if(jcb.getText().equals(s)) jcb.setSelected(true);
			}	
		}	

		checkList.setListData(boxes.toArray());
		checkList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		p.add(checkList, c);
		
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(button2, c);

		// edit Button
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					if (!check(p))
						throw new WrongInputException();
					
					HashMap<Weekday, int[]> wunsch = new HashMap<Weekday, int[]>();

					for (int i = 0; i < model2.getRowCount(); i++) {
						int[] arr = {Integer.parseInt((String) model2.getValueAt(i,1)),
								Integer.parseInt((String) model2.getValueAt(i,2)),
								Integer.parseInt((String) model2.getValueAt(i,3)),
								Integer.parseInt((String) model2.getValueAt(i,4))};			
						wunsch.put(Weekday.getDay((String) model2.getValueAt(i, 0)), arr);
					};

					ArrayList<String> stunden = new ArrayList<String>();
					for (int i = 0; i < checkList.getModel().getSize(); i++) {
						JCheckBox cb = (JCheckBox) checkList.getModel().getElementAt(i);
						if(cb.isSelected()) stunden.add(cb.getText());
					}
					
					PersonalManager.editPersonal(pe.getKuerzel(),new Personal(nameField2.getText(), kuerzField2
							.getText(), Integer.parseInt(timeField2.getText()), Integer.parseInt(ersatzField2.getText()),
							lehrerB2.isSelected(), stunden, wunsch));
	
					updateList();
					JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
					topFrame.dispose();

				} catch (WrongInputException e) {
					e.printStackTrace();
				}
			}
		});
		
		c.gridx = 3;
		c.gridwidth = 2;
		p.add(button3,c);
		
		// abbruch Button
				button3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(p);
						topFrame.dispose();
					}
				});
		return p;
	}

	private boolean check(final JPanel p) {
		if (textFieldsEmpty(p))
			return false;
		if (kuerzField.getText().length()>Data.MAX_KUERZEL_LEN) return false;
		try {
			if(model!=null){
				for (int i = 0; i < model.getRowCount(); i++) {
					Integer.parseInt((String) model.getValueAt(i, 1));
					Integer.parseInt((String) model.getValueAt(i, 2));
					Integer.parseInt((String) model.getValueAt(i, 3));
					Integer.parseInt((String) model.getValueAt(i, 4));
					if((Integer.parseInt((String) model.getValueAt(i, 1))>23)) return false;
					if((Integer.parseInt((String) model.getValueAt(i, 2))>59)) return false;
					if((Integer.parseInt((String) model.getValueAt(i, 3))>23)) return false;
					if((Integer.parseInt((String) model.getValueAt(i, 4))>59)) return false;
				}
			}
			if(model2!=null){
				for (int i = 0; i < model.getRowCount(); i++) {
					Integer.parseInt((String) model.getValueAt(i, 1));
					Integer.parseInt((String) model.getValueAt(i, 2));
					Integer.parseInt((String) model.getValueAt(i, 3));
					Integer.parseInt((String) model.getValueAt(i, 4));
					if((Integer.parseInt((String) model2.getValueAt(i, 1))>23)) return false;
					if((Integer.parseInt((String) model2.getValueAt(i, 2))>59)) return false;
					if((Integer.parseInt((String) model2.getValueAt(i, 3))>23)) return false;
					if((Integer.parseInt((String) model2.getValueAt(i, 4))>59)) return false;
				}
			}
			for (Component c : p.getComponents()) {
				if (c == timeField)
					Integer.parseInt(timeField.getText());
				if (c == timeField2)
					Integer.parseInt(timeField2.getText());
				if (c == ersatzField)
					Integer.parseInt(ersatzField.getText());
				if (c == ersatzField2)
					Integer.parseInt(ersatzField2.getText());
			}
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	private boolean textFieldsEmpty(final JPanel p){
		boolean b=true;
		for(Component c : p.getComponents()){
			if(c instanceof TextField){
				TextField tf = (TextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
			if(c instanceof JTextField ){
				JTextField tf = (JTextField) c;
				if(!tf.getText().isEmpty()) b=false;
			}
		}
		return b;
	}

	public static void updateList() {
		listModel.clear();
		for (Personal per : PersonalManager.getAllPersonalFromDB()){
			 listModel.addElement(per);
		 }
	}
}
