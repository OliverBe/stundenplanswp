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
public class ExceptionsTest {

	@BeforeClass
	public static void before() {
		System.out.println("TEST - Exception");
		new WarningPanel();
	}

	@After
	public void afterEvery() {
		System.out.println("... done");
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
					.equals("Bereits vorhanden"))) {
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
					.equals("Personal bereits in anderem Klassenteam"))) {
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
	
	@Test
	public void testDeleteException() {
		//wirft JOptionPane, deswegen nicht testen
	}
	
	@Test(expected = KuerzelException.class)
	public void testKuerzelException() throws KuerzelException {
		throw new KuerzelException();
	}
	
	@Test
	public void testKuerzelExceptionOutprint() {
		new KuerzelException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Kuerzel zu lang"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = MindestestensEinLehrerException.class)
	public void testMindestestensEinLehrerException() throws MindestestensEinLehrerException {
		throw new MindestestensEinLehrerException();
	}
	
	@Test
	public void testMindestestensEinLehrerExceptionOutprint() {
		new MindestestensEinLehrerException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Kein Lehrer im Team"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = NichtVorhandenException.class)
	public void testNichtVorhanden() throws NichtVorhandenException {
		throw new NichtVorhandenException();
	}
	
	@Test
	public void testNichtVorhandenExceptionOutprint() {
		new NichtVorhandenException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Nicht vorhanden"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = RaumException.class)
	public void testRaumException() throws RaumException {
		throw new RaumException();
	}
	
	@Test
	public void testRaumExceptionOutprint() {
		new RaumException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Kein Raum gewaehlt"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = StundeninhaltException.class)
	public void testStundeninhaltException() throws StundeninhaltException {
		throw new StundeninhaltException();
	}
	
	@Test
	public void testStundeninhaltExceptionOutprint() {
		new StundeninhaltException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Kein Stundeninhalt gewaehlt"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test
	public void testStundenplanException() {
		//JOptionDIalog, also nicht testen
	}
	
	@Test(expected = TextException.class)
	public void testTextException() throws TextException {
		throw new TextException();
	}
	
	@Test
	public void testTextExceptionOutprint() {
		new TextException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Feld ist leer"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = WochentagException.class)
	public void testWochentagException() throws WochentagException {
		throw new WochentagException();
	}
	
	@Test
	public void testWochentagExceptionOutprint() {
		new WochentagException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Kein Wochentag gewaehlt"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}
	
	@Test(expected = ZahlException.class)
	public void testZahlException() throws ZahlException {
		throw new ZahlException();
	}
	
	@Test
	public void testZahlExceptionOutprint() {
		new ZahlException();
		try {
			if ((WarningPanel.getListModel().elementAt(0)
					.equals("Feld benoetigt (gueltige, positive) Zahl"))) {
				assertTrue(true);
				return;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fail("should not reach here");
		}
		fail("should not reach here");
	}

}
