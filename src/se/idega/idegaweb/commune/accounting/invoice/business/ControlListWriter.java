/* $Id: ControlListWriter.java,v 1.9 2004/05/05 10:00:05 sigtryggur Exp $
*
* Copyright (C) 2003 Agura IT. All Rights Reserved.
*
* This software is the proprietary information of Agura IT AB.
* Use is subject to license terms.
*
*/
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.accounting.invoice.presentation.ControlList;
import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;

import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/** 
 * PDF and XLS Writer for the Control List
 * <p>
 * $Id: ControlListWriter.java,v 1.9 2004/05/05 10:00:05 sigtryggur Exp $
 *
 * @author Kelly
 */
public class ControlListWriter extends AccountingBlock implements MediaWritable  {

	private MemoryFileBuffer buffer = null;
	private Locale locale;
	private IWResourceBundle iwrb;

	public final static String prmPrintType = "print_type";
	public final static String compareDate = "compare_date";
	public final static String withDate = "with_date";
	public final static String opField = "op_field";
	public final static String XLS = "xls";
	public final static String PDF = "pdf";
	
	public ControlListWriter() {
	}

	public void init(IWContext iwc) {
	
	}
	
	public void init(HttpServletRequest req, IWMainApplication iwma) {
		try {
			locale = iwma.getIWApplicationContext().getApplicationSettings().getApplicationLocale();
			//TODO The correnct bundle_ident variable is called IW_ACCOUNTING_BUNDLE_IDENTIFER and this
			//     one points to CommuneBlock.IW_BUNDLE_IDENTIFIER and therefore this class is using the
			//     wrong resourceBundle. Remember that, when fixing this, whoever you are ....
			iwrb = iwma.getBundle(AccountingBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			ControlListBusiness business = getControlListBusiness(iwma.getIWApplicationContext());
			
			String type = req.getParameter(prmPrintType);
			Date cDate = parseDate(req.getParameter(compareDate));
			Date wDate = parseDate(req.getParameter(withDate));
			String of = req.getParameter(opField);
			
			if (type.equals(PDF)) {
				buffer = writePDF(business.getControlListValues(cDate, wDate, of));
			} else if (type.equals(XLS)) {
				buffer = writeXLS(business.getControlListValues(cDate, wDate, of));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return "application/pdf";
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}
	
	public MemoryFileBuffer writeXLS(Collection data) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		if (!data.isEmpty()) {			
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Excel");
			sheet.setColumnWidth((short)0, (short) (30 * 256));
			sheet.setColumnWidth((short)1, (short) (20 * 256));
			sheet.setColumnWidth((short)2, (short) (20 * 256));
			sheet.setColumnWidth((short)3, (short) (20 * 256));
			sheet.setColumnWidth((short)4, (short) (20 * 256));
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font);
	
	
			HSSFRow row = sheet.createRow((short)0);
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue(iwrb.getLocalizedString(ControlList.KEY_PROVIDER, "Provider"));
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			cell.setCellValue(iwrb.getLocalizedString(ControlList.KEY_NUM_INDIVIDUALS_PREL, "No of individuals, prel."));
			cell.setCellStyle(style);
			cell = row.createCell((short)2);
			cell.setCellValue(iwrb.getLocalizedString(ControlList.KEY_NUM_INDIVIDUALS_COMPARE_MONTH, "No of individuals, compare"));
			cell.setCellStyle(style);
			cell = row.createCell((short)3);
			cell.setCellValue(iwrb.getLocalizedString(ControlList.KEY_TOTAL_AMOUNT_PREL, "Total amount, prel."));
			cell.setCellStyle(style);
			cell = row.createCell((short)4);
			cell.setCellValue(iwrb.getLocalizedString(ControlList.KEY_TOTAL_AMOUNT_COMPARE_MONTH, "Total amount, compare"));
			cell.setCellStyle(style);

			
			int cellRow = 1;
			Iterator iter = data.iterator();

			// render header
			Object[] header = (Object[]) iter.next();
			row = sheet.createRow((short)cellRow);
			row.createCell((short)0).setCellValue((String)(header[1]));
			row.createCell((short)1).setCellValue((String)(header[2]));
			row.createCell((short)2).setCellValue((String)(header[3]));
			row.createCell((short)3).setCellValue((String)(header[4]));
			row.createCell((short)4).setCellValue((String)(header[5]));
			cellRow++;

			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			int sum4 = 0;

			while (iter.hasNext()) {
				row = sheet.createRow((short)cellRow);
				Object[] obj = (Object[]) iter.next();

				row.createCell((short)0).setCellValue((String)(obj[1]));

				final long currentMonthIndividualsCount = ((Long) obj[2]).longValue ();
				final long compareMonthIndividualsCount = ((Long) obj[3]).longValue ();
				final long currentMonthTotalAmount = ((Long) obj[4]).longValue ();
				final long compareMonthTotalAmount = ((Long) obj[5]).longValue ();

				row.createCell((short)1).setCellValue(currentMonthIndividualsCount);
				row.createCell((short)2).setCellValue(compareMonthIndividualsCount);
				row.createCell((short)3).setCellValue(currentMonthTotalAmount);
				row.createCell((short)4).setCellValue(compareMonthTotalAmount);

				sum1 += currentMonthIndividualsCount;
				sum2 += compareMonthIndividualsCount;
				sum3 += currentMonthTotalAmount;
				sum4 += compareMonthTotalAmount;
				
				cellRow++;
			}

			row = sheet.createRow((short)cellRow);
			row.createCell((short)0).setCellValue(" ");
			row.createCell((short)1).setCellValue(" ");
			row.createCell((short)2).setCellValue(" ");
			row.createCell((short)3).setCellValue(" ");
			row.createCell((short)4).setCellValue(" ");

			row = sheet.createRow((short)cellRow);
			row.createCell((short)0).setCellValue((localize(ControlList.KEY_TALLY, "Total")));
			row.createCell((short)1).setCellValue((""+sum1));
			row.createCell((short)2).setCellValue((""+sum2));
			row.createCell((short)3).setCellValue((""+sum3));
			row.createCell((short)4).setCellValue((""+sum4));

			wb.write(mos);
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	public MemoryFileBuffer writePDF(Collection data) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		if (!data.isEmpty()) {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(document, mos);
			document.addTitle("Title");
			document.addAuthor("Idega Reports");
			document.addSubject("Subject");
			document.open();
			
			String[] headers = {
				iwrb.getLocalizedString(ControlList.KEY_PROVIDER, "Provider"), 
				iwrb.getLocalizedString(ControlList.KEY_NUM_INDIVIDUALS_PREL, "No of individuals, prel."),
				iwrb.getLocalizedString(ControlList.KEY_NUM_INDIVIDUALS_COMPARE_MONTH, "No of individuals, compare"),
				iwrb.getLocalizedString(ControlList.KEY_TOTAL_AMOUNT_PREL, "Total amount, prel."),
				iwrb.getLocalizedString(ControlList.KEY_TOTAL_AMOUNT_COMPARE_MONTH, "Total amount, compare")};
				
			int[] sizes = { 20, 20, 20, 20, 20 };
			Cell cell;

			Table datatable = getTable(headers, sizes);
			Iterator iter = data.iterator();
			while (iter.hasNext()) {

				Object[] obj = (Object[]) iter.next();
				
				cell = new Cell(new Phrase((String)obj[1], new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase((String)obj[2], new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase((String)obj[3], new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase((String)obj[4], new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase((String)obj[5], new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				if (!writer.fitsPage(datatable)) {
					datatable.deleteLastRow();
					document.add(datatable);
					document.newPage();
					datatable = getTable(headers, sizes);
				}
			}
			document.add(datatable);
			document.close();
			writer.setPdfVersion(PdfWriter.VERSION_1_2);
		}
		
		buffer.setMimeType("application/pdf");
		return buffer;
	}
	
	private Table getTable(String[] headers, int[] sizes) throws BadElementException, DocumentException {
		Table datatable = new Table(headers.length);
		datatable.setPadding(0.0f);
		datatable.setSpacing(0.0f);
		datatable.setBorder(Rectangle.NO_BORDER);
		datatable.setWidth(100);
		if (sizes != null)
			datatable.setWidths(sizes);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = new Cell(new Phrase(headers[i], new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setBorder(Rectangle.BOTTOM);
			datatable.addCell(cell);
		}
		datatable.setDefaultCellBorderWidth(0);
		datatable.setDefaultCellBorder(Rectangle.NO_BORDER);
		datatable.setDefaultRowspan(1);
		return datatable;
	}

	protected ControlListBusiness getControlListBusiness(IWApplicationContext iwc) throws RemoteException {
		return (ControlListBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ControlListBusiness.class);
	}	

}
