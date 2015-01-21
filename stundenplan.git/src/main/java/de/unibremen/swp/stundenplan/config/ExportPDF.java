package de.unibremen.swp.stundenplan.config;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.unibremen.swp.stundenplan.data.Personal;
import de.unibremen.swp.stundenplan.data.Room;
import de.unibremen.swp.stundenplan.data.Schoolclass;
import de.unibremen.swp.stundenplan.gui.Timeslot;
import de.unibremen.swp.stundenplan.gui.TimetableModel;

/**
 * Diese Klasse exportiert die Stundenpläne aus dem StundenplanPanel und dem
 * RaumansichtsPanel in eine .csv, .pdf oder .doc Datei.
 * 
 * Die Exportmethode für pdf basiert auf dem itext-Tutorial
 * http://itextpdf.com/examples/iia.php?id=12
 * 
 * Für das Exportieren wird die Itext API genutzt http://itextpdf.com/api
 * 
 * @author Fabian H.
 *
 */
public class ExportPDF {

	/**
	 * der Systempfad in dem sich die zu öffnenden Dateien befinden
	 */
	private static String path = System.getProperty("user.dir") + "/";

	/**
	 * Name der Datei + dessen Pfad. Die Dateiendung wird sich nicht in diesem
	 * String befinden
	 * 
	 */
	private static String FILE;

	/**
	 * planOwner ist der Name des Owners. Der Owner ist der Besitzer aus dem
	 * JTable.
	 */
	private static String planOwner;

	/**
	 * Diese methode erzeugt eine neue PDF-Datei. In der Datei wird sich der
	 * Inhalt des Parameters jTable befinden. Es wird ein document erstellt
	 * dieses erhält zwei Paragraphen. Zum einen den String mit dem Namen des
	 * Owners und zum anderen den Inhalt des JTables. Dieser Inhalt wird aus der
	 * doppelten for-Schleife in ein PdfPTable gebracht.
	 * 
	 * erstellt mit itext API
	 * 
	 * @param jTable
	 */
	public static void createPDF(JTable jTable) {
		try {
			Document document = new Document(PageSize.A4.rotate());

			setOwnerAndFile(jTable);

			PdfWriter
					.getInstance(document, new FileOutputStream(FILE + ".pdf"));
			document.open();

			PdfPTable table = new PdfPTable(jTable.getModel().getColumnCount());

			for (int i = 0; i < jTable.getModel().getRowCount(); i++) {
				for (int e = 0; e < jTable.getModel().getColumnCount(); e++) {
					Object obj = jTable.getModel().getValueAt(i, e);
					if (obj instanceof Timeslot) {
						Timeslot ts = (Timeslot) obj;
						String text = "";
						if (!ts.getKlassentext().isEmpty()) {
							text = text + ts.getKlassentext() + " \r\n";
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + ts.getPersonaltext() + " \r\n";
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + ts.getRaeumetext() + " \r\n";
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + ts.getStundeninhalttext();
						}
						PdfPCell c1 = new PdfPCell(new Phrase(text));
						c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(c1);
					} else {
						PdfPCell c1 = new PdfPCell(new Phrase(obj.toString()));
						c1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(c1);
					}
				}
			}
			Paragraph pot = new Paragraph("Stundenplan von " + planOwner);
			addEmptyLine(pot, 2);
			document.add(pot);
			document.add(table);

			document.close();

			// öffnet die PDF
			Runtime.getRuntime().exec("cmd.exe /c " + FILE + ".pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * fügt Leere Zeilen ein wobei number die Anzahl der leeren Zeilen angibt
	 * und Paragraph den Paragraphen nennt in dem die Zeilen eingefügt werden.
	 * 
	 * @param paragraph
	 * @param number
	 */
	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	/**
	 * erstellt eine comma-seperated-values Datei. Diese beinhaltet die Strings
	 * der Objekte welche sich in dem JTable befinden. Das JTable ist der
	 * Stundenplan.
	 * 
	 * @param jTable
	 */
	public static void createCSV(JTable jTable) {

		try {

			setOwnerAndFile(jTable);

			FileWriter writer = new FileWriter(FILE + ".csv");
			for (int i = 0; i < jTable.getModel().getRowCount(); i++) {
				for (int e = 0; e < jTable.getModel().getColumnCount(); e++) {
					Object obj = jTable.getModel().getValueAt(i, e);

					if (obj instanceof Timeslot) {
						Timeslot ts = (Timeslot) obj;
						String text = "";
						if (!ts.getKlassentext().isEmpty()) {
							text = text + ts.getKlassentext() + ",";
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + ts.getPersonaltext() + ",";
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + ts.getRaeumetext() + ",";
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + ts.getStundeninhalttext() + ",";
						}
						writer.append(text);
					} else {
						writer.append(obj.toString());
						writer.append(",");
					}

				}
				writer.append("\r\n");

			}
			writer.flush();
			writer.close();

			Runtime.getRuntime().exec("cmd.exe /c " + FILE + ".csv");

		} catch (IOException e1) {
			System.out.println("ExportFehler");
			e1.printStackTrace();
		}

	}

	/**
	 * erstellt eine .doc-Datei die den Inhalt des Stundenplan wiedergibt
	 * 
	 * @param jTable
	 */
	public static void createDOC(JTable jTable) {

		try {

			setOwnerAndFile(jTable);

			FileWriter writer = new FileWriter(FILE + ".doc");
			for (int i = 0; i < jTable.getModel().getRowCount(); i++) {
				for (int e = 0; e < jTable.getModel().getColumnCount(); e++) {
					Object obj = jTable.getModel().getValueAt(i, e);
					if (obj instanceof Timeslot) {
						Timeslot ts = (Timeslot) obj;
						String text = "";
						text = text + jTable.getModel().getColumnName(e);
						if (!ts.getKlassentext().isEmpty()) {
							text = text + "\t" + "Klasse/n: "
									+ ts.getKlassentext();
						} else {
							text = text + "\t" + "Keine Klasse ";
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + " Personal: " + ts.getPersonaltext();
						} else {
							text = text + " Kein Personal ";
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + " Raum: " + ts.getRaeumetext();
						} else {
							text = text + " Kein Raum ";
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + " Inhalt: "
									+ ts.getStundeninhalttext();
						} else {
							text = text + " Keine Inhalte";
						}
						writer.append(text);
					} else {
						writer.append("\n");
						writer.append(obj.toString());
						writer.append("\n");
					}
					writer.append("\r\n");
				}
			}
			writer.flush();
			writer.close();

			Runtime.getRuntime().exec("cmd.exe /c " + FILE + ".doc");

		} catch (IOException e1) {
			System.out.println("ExportFehler");
			e1.printStackTrace();
		}

	}

	/**
	 * setzt die Strings FILE und planOwner fest, indem auf das jTable
	 * zugegriffen wird und der Owner des JTables genutzt wird. Somit wird für
	 * jede einzellene Schulklasse, Raum, Personal ein eigener Stundenplan
	 * erstellt mit verschiedenen Namen.
	 * 
	 * @param jTable
	 */
	public static void setOwnerAndFile(JTable jTable) {
		if (planOwner != null && planOwner.equals("Personalplan")) {
			FILE = path + "Personalplan";
		} else {
		Object owner = jTable.getModel();
		if (owner instanceof TimetableModel) {
			TimetableModel tm = (TimetableModel) owner;
			owner = tm.getOwner();
			if (owner instanceof Personal) {
				Personal p = (Personal) owner;
				owner = p.getName();
			} else if (owner instanceof Schoolclass) {
				Schoolclass sc = (Schoolclass) owner;
				owner = sc.getName();
			} else if (owner instanceof Room) {
				Room r = (Room) owner;
				owner = r.getName();

			}
			FILE = path + "Stundenplan-" + owner.toString();
			planOwner = owner.toString();
		
		} else {
			FILE = path + "Stundenplan";
			planOwner = "";
		}
		}
	}
	
	public static void setOwner(String name) {
		planOwner = name;
	}
}