package is.idega.experimental.pdftest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.XmlParser;

/**
 * @author laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PDFTest {
	
	public static void main(String[] args) {
    FileOutputStream fileOut = null;
		try {
			//File file = new File("test.pdf");
			fileOut = new FileOutputStream("test.pdf");
		}
		catch (FileNotFoundException e) {
		}

		try {
			Document outerDocument = getLetterDocumentTemplate();
			PdfWriter writer = PdfWriter.getInstance(outerDocument, fileOut);
			outerDocument.open();

			Font titleFont = new Font(Font.HELVETICA);
			titleFont.setSize(12);
			Font paragraphFont = new Font(Font.HELVETICA, 13, Font.BOLD);
			Font tagFont = new Font(Font.HELVETICA);
			tagFont.setSize(12);
			Font textFont = new Font(Font.HELVETICA);
			textFont.setSize(9);

			/*HeaderFooter footer = new HeaderFooter(new Phrase("idegaWeb Commune", textFont), true);
			footer.setBorder(0);
			footer.setAlignment(Element.ALIGN_CENTER);
			outerDocument.setFooter(footer);*/
			String title = "Concerning your application for idegaWeb Commune account";
			Paragraph cTitle = new Paragraph(title, titleFont);
			// for each contract id
			int lettersProcessed=0;
			Image image = Image.getInstance("porto_betalt.jpg");
			image.scaleAbsolute(60f, 60f);
			//outerDocument.setPageCount(messageIDs.length);
			for (int j = 0; j < 10; j++)
			{
				try{
					outerDocument.newPage();
					String sAddrString = "��rhallur B�ring Sveinn Hreinn Helgason\nStafnaseli 5\n109 Reykjav�k\nIceland";

					Paragraph P1, P2, P3, P4, P5, P6, P7, P8, P9;
					
					Phrase Ph0 = new Phrase(sAddrString, textFont);
					BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
					PdfContentByte cb = writer.getDirectContent();
					PdfTemplate template = cb.createTemplate(124f, 54f);
					template.rectangle(0f, 0f, 124f, 54f);
					template.moveTo(0f, 0f);
					template.lineTo(124f, 54f);
					template.moveTo(124f, 0f);
					template.lineTo(0f, 54f);
					template.stroke();
					template.beginText();
					template.setFontAndSize(bf, 11f);
					template.setTextMatrix(5f, 40f);
					template.showText("Nacka komun");
					template.endText();
					template.beginText();
					template.setFontAndSize(bf, 11f);
					template.setTextMatrix(5f, 25f);
					template.showText("131 81 NACKA");
					template.endText();
					
					P1 = new Paragraph(new Phrase("Hej,"));

					String sBodyText = "Din ans�kan om medborgarkonto f�r Nacka24 har blivit godk�nd. Du har d�rmed f�tt tillg�ng till din personliga sida p� Nacka24.";
					P2 = new Paragraph(new Phrase(sBodyText));
					
					sBodyText = "Anv�ndarnamn: ";
					P3 = new Paragraph();
					P3.add(new Phrase(sBodyText));
					P3.add(new Phrase("Laddi\n", paragraphFont));
					P3.add(new Phrase("L�senord: "));
					P3.add(new Phrase("Rapperson", paragraphFont));
					
					P4 = new Paragraph(new Phrase("Webbadressen till Nacka24 �r: http://www.nacka24.nacka.se. Skriv in ditt anv�ndarnamn och l�senord �verst till h�ger i f�nstret. Klicka p� Logga in. Min sida, ditt personliga medborgarkonto �ppnas."));
					P5 = new Paragraph(new Phrase("F�r att du ska f� tillg�ng till din personliga sida kr�vs att du anv�nder en webbl�sare med tillr�ckligt h�g s�kerhet. Webbl�saren m�ste ocks� ha st�d f�r cookies och i de flesta webbl�sare fungerar det automatiskt. Du ska allts� inte inaktivera funktionen med cookies."));
					P6 = new Paragraph(new Phrase("Du b�r anv�nda n�gon av f�ljande webbl�sare:"));
					P7 = new Paragraph(new Phrase("\t-\tInternet Explorer 5.5 f�r Windows eller senare versioner "));
					P8 = new Paragraph(new Phrase("\t-\tInternet Explorer 5.1 f�r Mac eller senare versioner"));
					P9 = new Paragraph(new Phrase("\t-\tNetscape 6.0 eller senare versioner"));

					ColumnText ct = new ColumnText(cb);
					ct.setSimpleColumn(Ph0,getPointsFromMM((30f+48f)), 755f, getPointsFromMM((200f)), 820f, 15, Element.ALIGN_LEFT);
					ct.go();
					
					/*ColumnText subjectText = new ColumnText(cb);
					subjectText.setSimpleColumn(P1, getPointsFromMM(30f), 640f, getPointsFromMM((216f-30f)), 670f, 15, Element.ALIGN_LEFT);
					subjectText.go();
					
					ColumnText bodyText = new ColumnText(cb);
					bodyText.setSimpleColumn(P2, getPointsFromMM(30f), 100f, getPointsFromMM((216f-30f)), 635f, 15, Element.ALIGN_LEFT);
					bodyText.setSimpleColumn(P3, getPointsFromMM(30f), 100f, getPointsFromMM((216f-30f)), 635f, 15, Element.ALIGN_LEFT);
					bodyText.go();*/
					
					cb.addImage(image, 60f, 0, 0, 60f, getPointsFromMM((160f)),755f);
					cb.addTemplate(template, getPointsFromMM(30f), 761f);
					
					Paragraph emptyLine = new Paragraph("\n");
					
					outerDocument.add(new Paragraph("\n\n\n\n\n\n"));
					outerDocument.add(P1);
					outerDocument.add(emptyLine);
					outerDocument.add(P2);
					outerDocument.add(emptyLine);
					outerDocument.add(P3);
					outerDocument.add(emptyLine);
					outerDocument.add(P4);
					outerDocument.add(emptyLine);
					outerDocument.add(P5);
					outerDocument.add(emptyLine);
					outerDocument.add(P6);
					//outerDocument.add(emptyLine);
					outerDocument.add(P7);
					//outerDocument.add(emptyLine);
					outerDocument.add(P8);
					//outerDocument.add(emptyLine);
					outerDocument.add(P9);
					outerDocument.add(emptyLine);
					/*outerDocument.add(new Paragraph("Du kan ladda ner n�gon av webbl�sarna fr�n f�ljande webbsidor."));
					outerDocument.add(emptyLine);
					outerDocument.add(new Paragraph("Internet Explorer 5.5 f�r Windows och Internet Explorer 5.1 f�r Mac"));
					outerDocument.add(new Paragraph("http://www.microsoft.com/downloads"));
					outerDocument.add(emptyLine);
					outerDocument.add(new Paragraph("Netscape 6.0"));
					outerDocument.add(new Paragraph("http://www.netscape.com"));
					outerDocument.add(emptyLine);*/
					outerDocument.add(new Paragraph("P� Nacka24 under Fr�gor & Svar finns mer hj�lp om hur du ser vilken version av webbl�sare du har och hur du laddar ner nya versioner. Om du har fr�gor �r du ocks� v�lkommen att kontakta kundvalsgruppen i Nacka kommun p� telefon 08-718 80 00 eller via e-post kundvalsgruppen.bun@nacka.se."));

				}
				catch(Exception e){
          			e.printStackTrace();
				}
			}
			outerDocument.close();
	    fileOut.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Document getLetterDocumentTemplate(){
			  //Margins defined in millimeters:
		float headFootMarginsMM = 9.0f;
		float leftRightMarginsMM = 30.0f;
		
		//Margins defined in points:
		float headFootMargins = getPointsFromMM(headFootMarginsMM);
		float leftRightMargins = getPointsFromMM(leftRightMarginsMM);
		Document document = new Document(PageSize.A4, leftRightMargins, leftRightMargins, headFootMargins, headFootMargins);
		//document.setMargins(leftRightMargins,leftRightMargins,headFootMargins,headFootMargins);
		document.addAuthor("IdegaWeb");
		document.addSubject("PrintedLetter");
		//document.open();
		return document;
	}

	public static float getPointsFromMM(float millimeters){
		float pointPerMM=72/25.4f;
		return millimeters*pointPerMM;
	}
	
	// Example of XML template usage, creating a PDF
	public MemoryFileBuffer createPDFFromXML(String  xmlFileURL)throws Exception{
		Document  document =new Document(PageSize.A4);
		MemoryFileBuffer buf = new MemoryFileBuffer();
		OutputStream DocOS = new MemoryOutputStream(buf);
	
		PdfWriter writer = PdfWriter.getInstance(document, DocOS);
		
		XmlParser.parse(document,xmlFileURL);
		// see http://www.lowagie.com/iText/tutorial/ch07.html for examples
		// and http://www.lowagie.com/iText/examples/Chap0703.xml for a XML file
		//
		return buf;
	}
	
	
}