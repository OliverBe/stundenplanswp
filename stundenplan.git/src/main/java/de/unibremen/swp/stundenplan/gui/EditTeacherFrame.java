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
import de.unibremen.swp.stundenplan.data.Teacher;
import de.unibremen.swp.stundenplan.exceptions.DatasetException;

public class EditTeacherFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1579391984515127831L;

	private EditTeacher editTeacher = new EditTeacher(this);
	public TextField nameField = editTeacher.nameField;
	public TextField acroField = editTeacher.acroField;
	public TextField timeField = editTeacher.timeField;

	public EditTeacherFrame(){
		super("Lehrer bearbeiten");
		add(editTeacher);
		setSize(600,400);
		setVisible(true);	
	}
		
}
