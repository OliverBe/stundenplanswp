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

public class ExportPDF {
	private static String path = System.getProperty("user.dir") + "/";

	private static String FILE;
	
	private static String planOwner;

	public static void createPDF(JTable jTable) {
		try {
			Document document = new Document(PageSize.A4.rotate());

			setOwnerAndFile(jTable);

			PdfWriter.getInstance(document, new FileOutputStream(FILE + ".pdf"));
			document.open();


			PdfPTable table = new PdfPTable(jTable.getModel().getColumnCount());

			for (int i = 0; i < jTable.getModel().getRowCount(); i++) {
				for (int e = 0; e < jTable.getModel().getColumnCount(); e++) {
					Object obj = jTable.getModel().getValueAt(i, e);
					if (obj instanceof Timeslot) {
						Timeslot ts = (Timeslot) obj;
						String text = "";
						if (!ts.getKlassentext().isEmpty()) {
							text = text + ts.getKlassentext() + "; \r\n";
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + ts.getPersonaltext() + "; \r\n";
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + ts.getRaeumetext() + "; \r\n";
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + ts.getStundeninhalttext() + ";";
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

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

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
							text = text + ts.getKlassentext() + "; \r\n";
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + ts.getPersonaltext() + "; \r\n";
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + ts.getRaeumetext() + "; \r\n";
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + ts.getStundeninhalttext() + ";";
						}
						writer.append(text);
						writer.append(";");
					} else {
						writer.append(obj.toString());
						writer.append(";");
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
						text  = text  +jTable.getModel().getColumnName(e)+ "\n";
						if (!ts.getKlassentext().isEmpty()) {
							text = text + "\n" +  ts.getKlassentext() + ";";
						} else {
							text = text + "\n" + "Keine Klasse ;"; 
						}
						if (!ts.getPersonaltext().isEmpty()) {
							text = text + "\r" + "Personal: \t" + ts.getPersonaltext() + ";";
						}else {
							text = text + "\r" + "Kein Personal ;"; 
						}
						if (!ts.getRaeumetext().isEmpty()) {
							text = text + "\r" + "Raum: \t" + ts.getRaeumetext() + ";";
						}else {
							text = text + "\r" + "Kein Raum ;"; 
						}
						if (!ts.getStundeninhalttext().isEmpty()) {
							text = text + "\r" + "Inhalt: \t" + ts.getStundeninhalttext() + ";";
						}else {
							text = text + "\r" + "Keine Inhalte ;"; 
						}
						writer.append(text);
						writer.append(";");
					} else {
						writer.append(obj.toString());
						writer.append("\n");
					}

				}
				writer.append(" \r\n");
			}
			writer.flush();
			writer.close();

			Runtime.getRuntime().exec("cmd.exe /c " + FILE + ".doc");

		} catch (IOException e1) {
			System.out.println("ExportFehler");
			e1.printStackTrace();
		}

	}
	
	

	private static void setOwnerAndFile(JTable jTable) {
		Object owner = jTable.getModel();
		if (owner instanceof TimetableModel) {
			TimetableModel tm = (TimetableModel) owner;
			owner = tm.getOwner();
			if (owner instanceof Personal) {
				Personal p = (Personal) owner;
				owner = p.getName();
			} else if (owner instanceof Schoolclass) {
				Schoolclass sc = (Schoolclass) owner;
				owner = sc.getJahrgang() + sc.getName();
			} else if (owner instanceof Room) {
				Room r = (Room) owner;
				owner = r.getName();

			}
			FILE = path + "Stundenplan-" + owner.toString() ;
			planOwner = owner.toString();
		} else {
			FILE = path + "Stundenplan";
			planOwner = "";
		}
	}
}