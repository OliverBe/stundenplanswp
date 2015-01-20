package de.unibremen.swp.stundenplan.config;


import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JTable;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.unibremen.swp.stundenplan.gui.Timeslot;


public class ExportPDF {
	private static String path = System.getProperty("user.dir") + "/";
	
  private static String FILE = path + "Stundenplan.pdf";
  private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
      Font.BOLD);
  private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.NORMAL, BaseColor.RED);
  private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
      Font.BOLD);
  private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
      Font.BOLD);

  public static void createPDF(JTable jTable) {
    try {
      Document document = new Document(PageSize.A4.rotate());
      
      PdfWriter.getInstance(document,  new FileOutputStream(FILE));
      document.open();
      addMetaData(document);
      addTitleBar(document);
      
	  PdfPTable table = new PdfPTable(jTable.getModel().getColumnCount());

      
      for(int i = 0; i < jTable.getModel().getRowCount(); i++) {
    	  for(int e = 0; e < jTable.getModel().getColumnCount(); e++) {
    		  Object obj = jTable.getModel().getValueAt(i, e);
    		  if(obj instanceof Timeslot) {
    			Timeslot ts = (Timeslot) obj;
    			String text ="";
    			if(!ts.getKlassentext().isEmpty()) {
    				text = text + ts.getKlassentext() + "; \r\n";
    			}
    			if(!ts.getPersonaltext().isEmpty()) {
    				text = text + ts.getPersonaltext() + "; \r\n";
    			}
    			if(!ts.getRaeumetext().isEmpty()) {
    				text = text + ts.getRaeumetext() + "; \r\n";
    			}
    			if(!ts.getStundeninhalttext().isEmpty()) {
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
      
      document.add(table);
      
      document.close();
     

   
      
      
    //öffnet die PDF
		Runtime.getRuntime().exec(
				"cmd.exe /c " + path 
						+ "Stundenplan.pdf");
      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // iText allows to add metadata to the PDF which can be viewed in your Adobe
  // Reader
  // under File -> Properties
  private static void addMetaData(Document document) {
    document.addTitle("Stundenplan von YYY");
  }

  private static void addTitleBar(Document document)
      throws DocumentException {
    Paragraph preface = new Paragraph();
    // We add one empty line
    addEmptyLine(preface, 1);
    // Lets write a big header
    preface.add(new Paragraph("Stundenplan von YYY", catFont));

   
  }

  

  private static void addEmptyLine(Paragraph paragraph, int number) {
    for (int i = 0; i < number; i++) {
      paragraph.add(new Paragraph(" "));
    }
  }
  
  public static void createCSV(JTable jTable) {
	  
	  
	  
	  
      
	try {
		FileWriter writer = new FileWriter(path + "Stundenplan.csv");
		   for(int i = 0; i < jTable.getModel().getRowCount(); i++) {
		    	  for(int e = 0; e < jTable.getModel().getColumnCount(); e++) {
		    		  Object obj = jTable.getModel().getValueAt(i, e);
		    		  if(obj instanceof Timeslot) {
		      			Timeslot ts = (Timeslot) obj;
		      			writer.append(ts.getKlassentext() + ", \r" + ts.getPersonaltext() + ", \r" + ts.getRaeumetext() + ", \r" + ts.getStundeninhalttext());
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
		   
		   Runtime.getRuntime().exec(
					"cmd.exe /c " + path 
							+ "Stundenplan.csv");
		  
	} catch (IOException e1) {
		System.out.println("ExportFehler");
		e1.printStackTrace();
	}
      
   
   
     
  }
} 