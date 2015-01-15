package de.unibremen.swp.stundenplan.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.unibremen.swp.stundenplan.config.ExportPDF;

public class MenuBar extends JMenuBar{

	 // JFileChooser-Objekt erstellen
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenu data = new JMenu("Datei");
	private JMenu edit = new JMenu("Edit");
	private JMenuItem undo = new JMenuItem("R�ckg�ngig");
	private JMenuItem neww = new JMenuItem("Neue Datei");
	private JMenuItem open = new JMenuItem("�ffnen");
	private JMenuItem save = new JMenuItem("Speichern");
	private JMenuItem export = new JMenuItem("Exportieren");
	
	private JFileChooser chooser = new JFileChooser();
	
	private JFrame f;
	
	public MenuBar(final JFrame frame) {
		f=frame;

		initComponents();
	}
	
	private void initComponents() {
		
		data.add(neww);
		data.add(open);
		data.add(save);
		data.add(export);
		add(data);
		edit.add(undo);
		add(edit);
		
		ImageIcon revert = new ImageIcon("revert.png"); 
		JButton button2 = new JButton("UndoButton", revert);
		add(button2);
		
		newClick(neww);
		openClick(open);
		saveClick(save);
		exportClick(export);
		undoClick(undo);
		
		
		//Shortcuts
		neww.setAccelerator(KeyStroke.getKeyStroke( 'N', InputEvent.CTRL_DOWN_MASK ));
		open.setAccelerator(KeyStroke.getKeyStroke( 'O', InputEvent.CTRL_DOWN_MASK ));
		save.setAccelerator(KeyStroke.getKeyStroke( 'S', InputEvent.CTRL_DOWN_MASK ));
		export.setAccelerator(KeyStroke.getKeyStroke( 'E', InputEvent.CTRL_DOWN_MASK ));
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
	}
	
	private void newClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				//neue Datei anlegen
			}
		});
	}
	
	private void openClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				chooser.showOpenDialog(f);
			}
		});
	}
	
	private void saveClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				
			}
		});
	}
	
	private void exportClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				ExportPDF.createPDF();
			}
		});
	}
	
	private void undoClick(JMenuItem item) {
		item.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ae) {
				//undo
			}
		});
	}
	
}
