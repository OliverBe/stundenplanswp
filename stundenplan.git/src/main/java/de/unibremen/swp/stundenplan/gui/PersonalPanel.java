package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.naming.InvalidNameException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import de.unibremen.swp.stundenplan.config.Config;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.data.Stundeninhalt;
import de.unibremen.swp.stundenplan.db.Data;
import de.unibremen.swp.stundenplan.exceptions.WrongInputException;
import de.unibremen.swp.stundenplan.logic.PersonalManager;

public class PersonalPanel extends JPanel{

	private Label lName = new Label("Name des Personals:");
	private Label lKuerz = new Label("Kuerzel:");
	private Label lPrefTime = new Label("Zeitwunsch:");

	private Label lTime = new Label("Zeitverpflichtung in h:");

	public TextField nameField = new TextField(15);
	public TextField kuerzField = new TextField(5);
	public TextField timeField= new TextField(5);
	private JLabel lSubjects = new JLabel("<html><body>Mögliche<br>Stundeninhalte :</body></html>");
	
	public JButton button = new JButton("Personal hinzufuegen");
	
	private GridBagConstraints c = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	
	private static DefaultListModel listModel = new DefaultListModel();
	private JList<String> list = new JList<String>(listModel);
	private JScrollPane listScroller = new JScrollPane(list);


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1219589162309740553L;

	public PersonalPanel(){
		setLayout(new GridBagLayout());
		setLayout(new GridBagLayout());
		c2.fill=GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(createAddPanel(new JPanel()),c2);
		c2.gridy=2;
		add(createListPanel(new JPanel()),c2);
	}

	private JPanel createAddPanel(final JPanel p) {
		
		p.setLayout(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Neues Personal hinzufuegen"));
		c.insets=new Insets(1,1,1,1);
		c.anchor=GridBagConstraints.WEST;
		c.gridx=0;
		c.gridy=0;
		p.add(lName,c);
		c.gridx=1;
		p.add(nameField,c);
		c.gridx=2;
		JRadioButton lehrerB = new JRadioButton("Lehrer");
		lehrerB.setSelected(true);
		JRadioButton paedagogeB = new JRadioButton("Paedagoge");
		ButtonGroup group = new ButtonGroup();
	    group.add(lehrerB);
	    group.add(paedagogeB);
	    p.add(lehrerB,c);
	    c.gridx=3;
	    p.add(new Label("oder"));
	    c.gridx=4;
	    p.add(paedagogeB,c);

		c.gridx=0;
		c.gridy=1;
		p.add(lKuerz,c);
		c.gridx=1;
		p.add(kuerzField,c);
			
		c.gridx=0;
		c.gridy=2;
		p.add(lTime,c);
		c.gridx=1;
		p.add(timeField,c);
		c.gridx=0;
		c.gridy=3;
		p.add(lPrefTime,c);
		
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		table.setColumnSelectionAllowed(false);

		model.addColumn("Wochentag");
		model.addColumn("Von (h)");
		model.addColumn("Von (min)");
		model.addColumn("Bis (h)");
		model.addColumn("Bis (min)");
		
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
		
		if(Config.MONDAY)model.addRow(new String[] {Config.MONDAY_STRING,sh,sm,eh,em});
		if(Config.TUESDAY)model.addRow(new String[] {Config.TUESDAY_STRING,sh,sm,eh,em});
		if(Config.WEDNESDAY)model.addRow(new String[] {Config.WEDNESDAY_STRING,sh,sm,eh,em});
		if(Config.THURSDAY)model.addRow(new String[] {Config.THURSDAY_STRING,sh,sm,eh,em});
		if(Config.FRIDAY)model.addRow(new String[] {Config.FRIDAY_STRING,sh,sm,eh,em});
		if(Config.SATURDAY)model.addRow(new String[] {Config.SATURDAY_STRING,sh,sm,eh,em});
		if(Config.SUNDAY)model.addRow(new String[] {Config.SUNDAY_STRING,sh,sm,eh,em});							
				
		c.gridy=4;
		c.gridwidth=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		p.add(table.getTableHeader(),c);
		c.gridy=5;
		p.add(table,c);
		
		c.gridx=0;
		c.gridy=6;
		c.gridheight=2;
		c.gridwidth=1;
		lSubjects.setFont(new Font(nameField.getFont().getFontName(), Font.PLAIN, nameField.getFont().getSize()));
		p.add(lSubjects, c);
		c.gridheight=1;
		c.gridx=1;
		CheckBoxList checkList = new CheckBoxList();
		ArrayList<JCheckBox> boxes = new ArrayList<JCheckBox>();
		
//		for(Stundeninhalt s : StundeninhaltManager.getAllStundeninhalte()){
//			boxes.add(new JCheckBox(s.getKuerzel()));
//		};
		boxes.add(new JCheckBox("Ma"));
	
	    checkList.setListData(boxes.toArray());
		p.add(checkList,c);
		
		c.gridx=0;
		c.gridy=8;
		c.gridwidth=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		p.add(button,c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					listModel.clear();
					if(!check(p)) throw new WrongInputException();
					Personal pe = new Personal(
							nameField.getText(),
							kuerzField.getText(),
							Integer.parseInt(timeField.getText()),
							lehrerB.isSelected(),
							new ArrayList<String>(checkList.getSelectedValuesList()));
					PersonalManager.addPersonalToDb(pe);
//					for (Personal pe : Data.getAllPersonal()){
//						listModel.addElement(pe);
//					}
					
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
		
		list.addListSelectionListener(new ListSelectionListener() { 
			public void valueChanged(ListSelectionEvent event) {
				System.out.println("selected "+list.getSelectedValue()+" "+list.getSelectedIndex()+" "+list.getComponentCount());
				final DataPopup pop = new DataPopup(list, listModel);
				setComponentPopupMenu(pop);	 
				list.addMouseListener(new MouseAdapter() {
		            public void mouseClicked(MouseEvent e) {
		            	if ( e.isMetaDown() ) {  				            	
		            		pop.show(list,e.getX(),e.getY()); 
		                }  
		            }			      
		        });
				pop.edit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
//                   	 personal t = personalListModel.getpersonalAt(personalList.getSelectedIndex());
//                   	 addNewpersonal.nameField.setText(t.getName());
//                   	 addNewpersonal.acroField.setText(t.getAcronym());
//                   	 addNewpersonal.timeField.setText((String)t.getHoursPerWeek().toString());
//                   	 addNewpersonal.setVisible(true);
                     EditPersonalFrame editPersonal = new EditPersonalFrame();
                   	 editPersonal.setVisible(true);
      //          	 Personal p = listModel.getPersonalAt(list.getSelectedIndex());
      //             	 editPersonal.nameField.setText(p.getName());
      //             	 editPersonal.acroField.setText(p.getKuerzel());
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
		return p;
	}
	
	private boolean check(final JPanel p){
		if(textFieldsEmpty(p)) return false;
		try{
			Integer.parseInt(timeField.getText());
		}catch(NumberFormatException e){
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
		}
		return b;
	}
}
