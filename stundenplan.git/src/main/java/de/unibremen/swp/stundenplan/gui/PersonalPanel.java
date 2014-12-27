package de.unibremen.swp.stundenplan.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.naming.InvalidNameException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Raumfunktion;
import de.unibremen.swp.stundenplan.db.Data;

public class PersonalPanel extends JPanel{
	private String name;
	private String acro;
	private String time;

	private Label lName = new Label("Name des Personals:");
	private Label lAcro = new Label("Acronym:");
	private Label lPrefTime = new Label("Zeitwunsch:");
	private Label lBis = new Label("bis");
	private Label lMo = new Label("Montag:");
	private Label lDi = new Label("Dienstag:");
	private Label lMi = new Label("Mittwoch:");
	private Label lDo = new Label("Donnerstag:");
	private Label lTime = new Label("Zeitverpflichtung in h:");
	private Label lFr = new Label("Freitag:");

	public TextField nameField = new TextField(15);
	public TextField acroField = new TextField(5);
	public TextField timeField= new TextField(5);
	public TextField subjectsField= new TextField(5);
	public TextField prefTimeFieldMoFrom= new TextField(5);
	public TextField prefTimeFieldMoTo=new TextField(5);
	public TextField prefTimeFieldDiFrom= new TextField(5);
	public TextField prefTimeFieldDiTo=new TextField(5);
	public TextField prefTimeFieldMiFrom= new TextField(5);
	public TextField prefTimeFieldMiTo=new TextField(5);
	public TextField prefTimeFieldDoFrom= new TextField(5);
	public TextField prefTimeFieldDoTo=new TextField(5);
	public TextField prefTimeFieldFrFrom= new TextField(5);
	private Label lSubjects = new Label("Stundeninhalte:");
	public TextField prefTimeFieldFrTo=new TextField(5);
	
	public JButton button = new JButton("Personal hinzufÃ¼gen");
	
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
		createAddPanel(new JPanel());
		createListPanel(new JPanel());
	}

	private void createAddPanel(final JPanel p) {
		
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
		p.add(lAcro,c);
		c.gridx=1;
		p.add(acroField,c);
			
		c.gridx=0;
		c.gridy=2;
		p.add(lTime,c);
		c.gridx=1;
		p.add(timeField,c);
		
		
		c.gridx=0;
		c.gridy=3;
		p.add(lPrefTime, c);
		c.gridx=1;
		p.add(lMo,c);
		c.gridx=2;
		p.add(prefTimeFieldMoFrom,c);
		c.gridx=3;
		p.add(lBis,c);
		c.gridx=4;
		p.add(prefTimeFieldMoTo,c);
		c.gridy=4;
		c.gridx=1;
		p.add(lDi,c);
		c.gridx=2;
		p.add(prefTimeFieldDiFrom,c);
		c.gridx=3;
	//	p.add(lBis,c);
		c.gridx=4;
		p.add(prefTimeFieldDiTo,c);
		c.gridy=5;
		c.gridx=1;
		p.add(lMi,c);
		c.gridx=2;
		p.add(prefTimeFieldMiFrom,c);
		c.gridx=3;
	//	p.add(lBis,c);
		c.gridx=4;
		p.add(prefTimeFieldMiTo,c);
		c.gridy=6;
		c.gridx=1;
		p.add(lDo,c);
		c.gridx=2;
		p.add(prefTimeFieldDoFrom,c);
		c.gridx=3;
	//	p.add(lBis,c);
		c.gridx=4;
		p.add(prefTimeFieldDoTo,c);
		c.gridy=7;
		c.gridx=1;
		p.add(lFr,c);
		c.gridx=2;
		p.add(prefTimeFieldFrFrom,c);
		c.gridx=3;
	//	p.add(lBis,c);
		c.gridx=4;
		p.add(prefTimeFieldFrTo,c);
		
		c.gridx=0;
		c.gridy=8;
		p.add(lSubjects, c);
		c.gridx=1;
		Label deutsch = new Label("Deutsch");
		p.add(deutsch,c);
		c.gridx=2;
		Label mathe = new Label("Mathe");
		p.add(mathe,c);
		c.gridx=3;
		Label musik=new Label("Musik");
		p.add(musik,c);
		
		c.gridx=1;
		c.gridy=9;
		JCheckBox box1 = new JCheckBox();
		p.add(box1,c);
		c.gridx=2;
		JCheckBox box2= new JCheckBox();
		p.add(box2,c);
		c.gridx=3;
		JCheckBox box3= new JCheckBox();
		p.add(box3,c);
		
		c.gridx=0;
		c.gridy=11;
		c.gridwidth=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		p.add(button,c);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Raumfunktion rf;
				try {
					listModel.clear();
					if(textFieldsEmpty(p)) throw new InvalidNameException();
					Personal pe = new Personal();
//					Data.addPersonal(rf);
//					for (Personal pe : Data.getAllPersonal()){
//						listModel.addElement(pe);
//					}
					
				} catch (InvalidNameException e) {
					System.out.println("Error beim Hinzufügen");
					e.printStackTrace();
				}
			}
		});
		
		nameField.requestFocus();
		
		c2.fill=GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.EAST;
		c2.gridwidth = 1;
		c2.gridheight = 1;
		c2.gridx = 1;
		c2.gridy = 1;
		c2.weightx = 1.8;
		c2.weighty = 1.0;
		add(p,c2);
	}
	
	private void createListPanel(final JPanel p) {
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
		
		c2.gridy=2;
		add(p,c2);
		
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
