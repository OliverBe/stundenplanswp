package de.unibremen.swp.stundenplan.exceptions;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unibremen.swp.stundenplan.gui.WarningPanel;

/**
 * Komponententests fuer alle Exceptions
 * 
 * @author Oliver
 *
 */
public class Exceptiontest {

	@BeforeClass
	public static void before() {
		new WarningPanel();
	}

	@After
	public void afterEvery() {
		if (!WarningPanel.getListModel().isEmpty())
			WarningPanel.getListModel().clear();
	}

	@Test(expected = BereitsVorhandenException.class)
	public void testBereitsVorhanden() throws BereitsVorhandenException {
		throw new BereitsVorhandenException();
	}

	@Test
	public void testBereitsVorhandenOutprint() {
		new BereitsVorhandenException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Bereits vorhanden."))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}

	@Test(expected = BesetztException.class)
	public void testBesetztException() throws BesetztException {
		throw new BesetztException("Personal");
	}

	@Test
	public void testBesetztExceptionOutprint() {
		new BesetztException("Personal");
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Personal ist bereits Klassenlehrer."))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test
	public void testCommandHistoryException() {
		//wirft JOptionPane, deswegen nicht testen
	}

}
