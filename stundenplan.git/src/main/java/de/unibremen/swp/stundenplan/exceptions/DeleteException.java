package de.unibremen.swp.stundenplan.exceptions;

import javax.swing.JOptionPane;

import de.unibremen.swp.stundenplan.Stundenplan;

@SuppressWarnings("serial")
public class DeleteException extends Exception {

	public DeleteException(final String message) {
        super(message);
        JOptionPane.showMessageDialog(Stundenplan.getMain(), message);
    }
	
	public static boolean delete(final String message) {
		int result = JOptionPane.showConfirmDialog(Stundenplan.getMain(), message, "Warnung", JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.YES_OPTION;
	}
}
