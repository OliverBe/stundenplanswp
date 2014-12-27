package de.unibremen.swp.stundenplan.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.unibremen.swp.stundenplan.Stundenplan;
import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;

public class EditPersonalFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1579391984515127831L;

	private EditPersonal editPersonal = new EditPersonal(this);
	public TextField nameField = editPersonal.nameField;
	public TextField acroField = editPersonal.acroField;
	public TextField timeField = editPersonal.timeField;

	public EditPersonalFrame(){
		super("Lehrer bearbeiten");
		add(editPersonal);
		setSize(600,400);
		setVisible(true);	
	}
		
}
