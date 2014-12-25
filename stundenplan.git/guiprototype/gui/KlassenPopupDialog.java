package gui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KlassenPopupDialog extends JFrame{
	
	
	public KlassenPopupDialog room = this;
	private JTextField eingabe;
	private JButton ok;
	private JPanel panel;
	
	public boolean clicked = false;

	public KlassenPopupDialog()
	{
	super("Hinzufuegen");
	setLocation(300,300);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setLayout(new BorderLayout(5,5));

	panel = new JPanel(new GridLayout(1,1));
	eingabe = new JTextField("Input: ");
	ok = new JButton("Ok");
	
	
	
	ok.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            clicked = true;
            room.dispose();
           
            

        }
    });
	
	  

	panel.add(eingabe);
	getContentPane().add(new JScrollPane(eingabe), BorderLayout.CENTER);
	getContentPane().add(panel);

	pack();
	
	
	
	
	
	}
	
	public String sendString() {
		return eingabe.getText();
	}
	
	public void actionPerformed (ActionEvent ae){
		clicked = true;
		this.dispose();
	}
	
	 public String doEingabe(){
	        String eingabeS = "";
	        while(true) {
	            eingabeS = JOptionPane.showInputDialog("Eingabe");
	            eingabeS = eingabeS + " \n";
	            if(eingabeS == null) break;
	            return eingabeS;
	        }
	        return eingabeS;
	    }
	
}

	
