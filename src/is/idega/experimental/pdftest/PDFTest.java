package is.idega.experimental.pdftest;

import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.idega.block.messenger.data.Message;
import com.idega.core.data.ICFile;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.User;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Graphic;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

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
			fileOut = new FileOutputStream("c:\\test.pdf");
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
			textFont.setSize(10);

			HeaderFooter footer = new HeaderFooter(new Phrase("idegaWeb Commune", textFont), true);
			footer.setBorder(0);
			footer.setAlignment(Element.ALIGN_CENTER);
			outerDocument.setFooter(footer);
			String title = "Concerning your application for idegaWeb Commune account";
			Paragraph cTitle = new Paragraph(title, titleFont);
			// for each contract id
			int lettersProcessed=0;
			//outerDocument.setPageCount(messageIDs.length);
			for (int j = 0; j < 1; j++)
			{
				try{
					outerDocument.newPage();
					String sAddrString = "Þórhallur Helgason\nStafnaseli 5\n109 Reykjavík\nIceland";

					Paragraph P0, P1, P2;
					PdfPTable headerTable = new PdfPTable(3);
					
					float headerTableHeightMM = 36f;
					float headerTableHeightPoints = getPointsFromMM(headerTableHeightMM);
					
					headerTable.setWidths(new float[]{1,1,1});
					headerTable.getDefaultCell().setFixedHeight(headerTableHeightPoints);
					headerTable.getDefaultCell().setPadding(0);
					headerTable.setWidthPercentage(100f);

					PdfPCell cell1 = new PdfPCell(new Phrase(""));
					cell1.setBorder(0);
					cell1.setNoWrap(true);
					
					Phrase Ph0 = new Phrase(sAddrString, textFont);
					PdfPCell cell2 = new PdfPCell(new Phrase(""));
					cell2.setBorder(0);
					cell2.setNoWrap(true);
					
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
					
					Image image = Image.getInstance("porto_betalt.jpg");
					image.scaleAbsolute(148f, 60f);
					PdfPCell cell3 = new PdfPCell(image);
					cell3.setBorder(0);
					cell3.setNoWrap(true);
					cell3.setHorizontalAlignment(cell3.ALIGN_RIGHT);
					
					headerTable.addCell(cell1);
					headerTable.addCell(cell2);
					headerTable.addCell(cell3);
										
					P1 = new Paragraph(new Phrase("This is a message", paragraphFont));

					String sBodyText = "This is the message body and since it is a fairly long message the text should automatically break into several lines.  If it does not I will have to rethink this approach somewhat for future references...";
					Phrase phBodyText = new Phrase(sBodyText, textFont);
					P2 = new Paragraph(phBodyText);

					Phrase newlines = new Phrase("\n\n\n\n\n\n",textFont);

					ColumnText ct = new ColumnText(cb);
					ct.setSimpleColumn(Ph0,getPointsFromMM((30f+50f)), 755f, getPointsFromMM((100f+70f)), 820f, 15, Element.ALIGN_LEFT);
					ct.go();
					
					cb.addImage(image, 148f, 0, 0, 60f, getPointsFromMM((130f)),755f);
					
					cb.addTemplate(template, getPointsFromMM(30f), 761f);
					//outerDocument.add(headerTable);
					outerDocument.add(newlines);
					outerDocument.add(P1);
					outerDocument.add(new Phrase("\n"));
					outerDocument.add(P2);

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

	protected static Document getLetterDocumentTemplate(){
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

	protected static float getPointsFromMM(float millimeters){
		float pointPerMM=72/25.4f;
		return millimeters*pointPerMM;
	}
}