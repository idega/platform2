/*
 * $Id: IFSCreateExcelFileUtil.java,v 1.1 2005/01/28 11:36:24 palli Exp $ Created on Jan
 * 21, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.accounting.export.ifs.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import javax.ejb.FinderException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.business.PaymentComparator;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * 
 * Last modified: $Date: 2005/01/28 11:36:24 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli </a>
 * @version $Revision: 1.1 $
 */
public class IFSCreateExcelFileUtil {
	
	protected final static int FILE_TYPE_OWN_POSTING = 1;

	protected final static int FILE_TYPE_DOUBLE_POSTING = 2;

	protected final static int FILE_TYPE_KOMMUN = 3;	

	private HSSFWorkbook wb = null;

	private HSSFRow row = null;

	private HSSFCell cell = null;

	private HSSFCellStyle styleAlignRight = null;

	private HSSFCellStyle styleBold = null;

	private HSSFCellStyle styleBoldAlignRight = null;

	private HSSFCellStyle styleBoldUnderline = null;

	private HSSFCellStyle styleBoldUnderlineAlignRight = null;

	private HSSFCellStyle styleItalicUnderlineAlignRight = null;

	private long inCommuneSum = 0;

	private NumberFormat numberFormat = null;

	private String deviationString = "";
	
	protected IWApplicationContext iwac = null;
	
	protected IWTimestamp paymentDate = null;

	public IFSCreateExcelFileUtil(IWApplicationContext iwac, IWTimestamp paymentDate) {
		this.iwac = iwac;
		this.paymentDate = paymentDate;
		createNumberFormat();
	}

	protected void createDeviationFileExcel(Collection data, String fileName, String headerText) throws IOException,
			FinderException {
		if (data != null && !data.isEmpty()) {
			int[] columnWidths = { 15, 20, 12, 35 };
			String[] columnNames = { "Fakturaperiod", "Fakturmottagars pnr", "Belopp", "Avvikelse orsak" };
			createExcelWorkBook(columnWidths, columnNames, headerText);
			HSSFSheet sheet = wb.getSheet("Excel");
			short rowNumber = (short) (sheet.getLastRowNum() + 1);
			short cellNumber = 0;
			long totalAmount = 0;
			long recordAmount;
			boolean invoiceHeaderDeviations;
			Iterator it = data.iterator();
			createStyleAlignRight();
			createStyleBold();
			createStyleBoldAlignRight();
			while (it.hasNext()) {
				InvoiceHeader iHead = (InvoiceHeader) it.next();
				ArrayList iRecs = new ArrayList(
						((InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class)).findByInvoiceHeader(iHead));
				if (!iRecs.isEmpty()) {
					long headerSum = 0;
					invoiceHeaderDeviations = false;
					for (int i = 0; i < iRecs.size(); i++)
						headerSum += AccountingUtil.roundAmount(((InvoiceRecord) iRecs.get(i)).getAmount());
					if (headerSum < 0) {
						setDeviationString("Total belopp från faktura huvud är negativt");
						invoiceHeaderDeviations = true;
					}
					else if (iHead.getCustodian() == null) {
						setDeviationString("Saknas fakturamottagare");
						invoiceHeaderDeviations = true;
					}
					else if (iHead.getCustodian().getAddresses().size() == 0) {
						setDeviationString("Saknas faktura adress");
						invoiceHeaderDeviations = true;
					}
					Iterator irIt = iRecs.iterator();
					while (irIt.hasNext()) {
						InvoiceRecord iRec = (InvoiceRecord) irIt.next();
						recordAmount = AccountingUtil.roundAmount(iRec.getAmount());
						if (recordAmount >= 0 || headerSum < 0) {
							if (invoiceHeaderDeviations || hasInvoiceRecordDeviations(iRec)) {
								totalAmount += recordAmount;
								row = sheet.createRow(rowNumber++);
								row.createCell(cellNumber++).setCellValue(iHead.getPeriod().toString());
								if (iHead.getCustodian() != null)
									row.createCell(cellNumber++).setCellValue(iHead.getCustodian().getPersonalID());
								else
									cellNumber++;
								cell = row.createCell(cellNumber++);
								cell.setCellValue(getNumberFormat().format(recordAmount));
								cell.setCellStyle(getStyleAlignRight());
								row.createCell(cellNumber++).setCellValue(getDeviationString());
								cellNumber = 0;
								if (!invoiceHeaderDeviations)
									setDeviationString("");
							}
						}
					}
				}
			}
			setDeviationString("");
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(cellNumber);
			cell.setCellValue("Summa");
			cell.setCellStyle(getStyleBold());
			cell = row.createCell(cellNumber += 2);
			cell.setCellValue(getNumberFormat().format(totalAmount));
			cell.setCellStyle(getStyleBoldAlignRight());
			saveExcelWorkBook(fileName, wb);
		}
	}

	private boolean hasInvoiceRecordDeviations(InvoiceRecord iRec) {
		if (hasNoCheck(iRec)) {
			setDeviationString("Saknas check");
			return true;
		}
		try {
			PostingBusiness pb = getIFSBusiness().getPostingBusiness();
			IWTimestamp now = IWTimestamp.RightNow();
			pb.validateString(iRec.getOwnPosting(), now.getDate());
		}
		catch (PostingException e) {
			e.printStackTrace();
			setDeviationString("Posting failed");
			return true;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean hasNoCheck(InvoiceRecord iRec) {
		ChildCareContract contract = iRec.getChildCareContract();
		if (contract == null) {
			return true;
		}
		if (contract.getApplication() == null) {
			return true;
		}
		try {
			CareBusiness business = (CareBusiness) IBOLookup.getServiceInstance(iwac, CareBusiness.class);
			return !business.hasGrantedCheck(contract.getChild());
		}
		catch (RemoteException re) {
			return true;
		}
	}

	protected void createPaymentFilesExcel(Collection data, String fileName, String headerText, int fileType)
			throws IOException {
		if (data != null && !data.isEmpty()) {
			int[] columnWidths = { 11, 7, 6, 7, 10, 8, 7, 7, 7, 10, 35, 25 };
			String[] columnNames = { "Bokf datum", "Ansvar", "Konto", "Resurs", "Verksamhet", "Aktivitet", "Projekt",
					"Objekt", "Motpart", "Belopp", "Text", "Anordnare" };
			int[] kommunColumnWidths = { 11, 7, 6, 7, 10, 8, 7, 7, 7, 10, 10, 35, 25 };
			String[] kommunColumnNames = { "Bokf datum", "Ansvar", "Konto", "Resurs", "Verksamhet", "Aktivitet",
					"Projekt", "Objekt", "Motpart", "Placeringar", "Belopp", "Text", "Anordnare" };
			if (fileType == FILE_TYPE_KOMMUN) {
				columnWidths = kommunColumnWidths;
				columnNames = kommunColumnNames;
			}
			createExcelWorkBook(columnWidths, columnNames, headerText);
			HSSFSheet sheet = wb.getSheet("Excel");
			short rowNumber = (short) (sheet.getLastRowNum() + 1);
			// HSSFHeader header = sheet.getHeader();
			// header.setLeft(headerText);
			// header.setRight("Sida "+HSSFHeader.page());
			// sheet.getPrintSetup().setLandscape(true);
			long totalAmount = 0;
			long amount;
			PostingBusiness pb = getIFSBusiness().getPostingBusiness();
			Iterator it = data.iterator();
			int numberOfRecords = 0;
			createStyleAlignRight();
			while (it.hasNext()) {
				PaymentRecord pRec = (PaymentRecord) it.next();
				School school = pRec.getPaymentHeader().getSchool();
				if (pRec.getTotalAmount() != 0.0f) {
					amount = AccountingUtil.roundAmount(pRec.getTotalAmount());
					if (fileType == FILE_TYPE_OWN_POSTING || fileType == FILE_TYPE_DOUBLE_POSTING) {
						numberOfRecords++;
						rowNumber = createPaymentLine(columnNames, sheet, rowNumber, amount, pb, pRec, school,
								pRec.getOwnPosting(), fileType);
					}
					if (fileType == FILE_TYPE_DOUBLE_POSTING || fileType == FILE_TYPE_KOMMUN) {
						numberOfRecords++;
						rowNumber = createPaymentLine(columnNames, sheet, rowNumber, -1 * amount, pb, pRec, school,
								pRec.getDoublePosting(), fileType);
					}
					totalAmount += amount;
				}
			}
			if (fileType == FILE_TYPE_KOMMUN)
				setInCommuneSum(totalAmount);
			sheet.createRow(rowNumber += 2).createCell(row.getFirstCellNum()).setCellValue(
					numberOfRecords + " bokföringsposter,   Kreditbelopp totalt:  - "
							+ getNumberFormat().format(totalAmount) + ",   Debetbelopp totalt: "
							+ getNumberFormat().format(totalAmount));
			saveExcelWorkBook(fileName, wb);
		}
	}

	private short createPaymentLine(String[] columnNames, HSSFSheet sheet, short rowNumber, long amount,
			PostingBusiness pb, PaymentRecord pRec, School school, String postingString, int fileType)
			throws RemoteException {
		short cellNumber = 0;
		row = sheet.createRow(rowNumber++);
		row.createCell(cellNumber++).setCellValue(paymentDate.getDateString("yyyy-MM-dd"));
		short loopTillEndOfPostingFields = (short) (cellNumber + 8);
		for (short i = cellNumber; i < loopTillEndOfPostingFields; i++)
			row.createCell(cellNumber++).setCellValue(pb.findFieldInStringByName(postingString, columnNames[i]));
		if (fileType == FILE_TYPE_KOMMUN) {
			cell = row.createCell(cellNumber++);
			cell.setCellValue(pRec.getPlacements());
		}
		cell = row.createCell(cellNumber++);
		cell.setCellValue(getNumberFormat().format(amount));
		cell.setCellStyle(getStyleAlignRight());
		row.createCell(cellNumber++).setCellValue(pRec.getPaymentText());
		row.createCell(cellNumber++).setCellValue(school.getName());
		return rowNumber;
	}

	protected void createInvoiceSigningFilesExcel(String fileName, String headerText, boolean signingFooter)
			throws IOException, IDOException {
		wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Excel");
		sheet.setColumnWidth((short) 0, (short) (30 * 256));
		sheet.setColumnWidth((short) 1, (short) (20 * 256));
		sheet.setColumnWidth((short) 2, (short) (20 * 256));
		short rowNumber = 0;
		short cellNumber = 0;
		row = sheet.createRow(rowNumber++);
		if (!headerText.equals("")) {
			row.createCell(cellNumber++).setCellValue(headerText);
			rowNumber++;
			row = sheet.createRow(rowNumber += 4);
		}
		CalendarMonth currentMonth = new CalendarMonth();
		CalendarMonth previousMonth = currentMonth.getPreviousCalendarMonth();
		int numberOfInvoicesForCurrentMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getNumberOfInvoicesForCurrentMonth();
		int numberOfInvoicesForPreviousMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getNumberOfInvoicesForMonth(previousMonth);
		int numberOfChildrenForCurrentMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getNumberOfChildrenForCurrentMonth();
		int numberOfChildrenForPreviousMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getNumberOfChildrenForMonth(previousMonth);
		int totalInvoiceRecordAmountForCurrentMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getTotalInvoiceRecordAmountForCurrentMonth();
		int totalInvoiceRecordAmountFoPreviousMonth = ((InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class)).getTotalInvoiceRecordAmountForMonth(previousMonth);
		row = sheet.createRow(rowNumber++);
		row.createCell(cellNumber++).setCellValue("Innevarande månad");
		row.createCell(cellNumber).setCellValue("Föregående månad");
		row = sheet.createRow(rowNumber++);
		row.createCell(cellNumber--).setCellValue(numberOfInvoicesForPreviousMonth);
		row.createCell(cellNumber--).setCellValue(numberOfInvoicesForCurrentMonth);
		row.createCell(cellNumber).setCellValue("Total antal generade fakturor");
		row = sheet.createRow(rowNumber++);
		row.createCell(cellNumber++).setCellValue("Total antal behandlade indvider");
		row.createCell(cellNumber++).setCellValue(numberOfChildrenForCurrentMonth);
		row.createCell(cellNumber).setCellValue(numberOfChildrenForPreviousMonth);
		row = sheet.createRow(rowNumber++);
		row.createCell(cellNumber--).setCellValue(totalInvoiceRecordAmountFoPreviousMonth);
		row.createCell(cellNumber--).setCellValue(totalInvoiceRecordAmountForCurrentMonth);
		row.createCell(cellNumber).setCellValue("Totalt fakturerat belopp");
		if (signingFooter) {
			createSigningFooter(sheet, rowNumber);
		}
		saveExcelWorkBook(fileName, wb);
	}

	protected void createPaymentSigningFilesExcel(Collection data, String fileName, String headerText)
			throws IOException, FinderException {
		if (data != null && !data.isEmpty()) {
			int[] columnWidths = { 25, 35, 12, 12 };
			String[] columnNames = { "Anordnare", "Text", "Placeringar", "Belopp" };
			createExcelWorkBook(columnWidths, columnNames, headerText);
			HSSFSheet sheet = wb.getSheet("Excel");
			short rowNumber = (short) (sheet.getLastRowNum() + 1);
			short cellNumber = 0;
			ArrayList paymentHeaders = new ArrayList(data);
			Collections.sort(paymentHeaders, new PaymentComparator());
			Iterator it = paymentHeaders.iterator();
			boolean firstRecord;
			long recordAmount;
			long totalHeaderAmount = 0;
			long totalAmount = 0;
			int totalHeaderStudents = 0;
			int totalStudents = 0;
			School school = null;
			createStyleAlignRight();
			createStyleBold();
			createStyleBoldAlignRight();
			createStyleItalicUnderlineAlignRight();
			while (it.hasNext()) {
				PaymentHeader pHead = (PaymentHeader) it.next();
				Collection pRecs = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).findByPaymentHeader(pHead);
				if (!pRecs.isEmpty()) {
					Iterator prIt = pRecs.iterator();
					firstRecord = true;
					school = pHead.getSchool();
					row = sheet.createRow(rowNumber++);
					row.createCell(cellNumber++).setCellValue(school.getName());
					while (prIt.hasNext()) {
						PaymentRecord pRec = (PaymentRecord) prIt.next();
						if (!firstRecord)
							row = sheet.createRow(rowNumber++);
						row.createCell(cellNumber++).setCellValue(pRec.getPaymentText());
						recordAmount = AccountingUtil.roundAmount(pRec.getTotalAmount());
						totalHeaderAmount += recordAmount;
						totalHeaderStudents += pRec.getPlacements();
						cell = row.createCell(cellNumber++);
						cell.setCellValue(pRec.getPlacements());
						cell.setCellStyle((getStyleAlignRight()));
						cell = row.createCell(cellNumber--);
						cell.setCellValue(getNumberFormat().format(recordAmount));
						cell.setCellStyle((getStyleAlignRight()));
						cellNumber--;
						if (!prIt.hasNext()) {
							cellNumber--;
							row = sheet.createRow(rowNumber++);
							row.createCell(cellNumber++).setCellValue("");
							row.createCell(cellNumber++).setCellValue("Summa");
							row.createCell(cellNumber++).setCellValue(totalHeaderStudents);
							row.createCell(cellNumber--).setCellValue(getNumberFormat().format(totalHeaderAmount));
						}
						firstRecord = false;
					}
					cellNumber -= 2;
					totalAmount += totalHeaderAmount;
					totalHeaderAmount = 0;
					totalStudents += totalHeaderStudents;
					totalHeaderStudents = 0;
					for (short i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++) {
						row.getCell(i).setCellStyle(getStyleItalicUnderlineAlignRight());
					}
				}
			}
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(cellNumber += 2);
			cell.setCellValue(getNumberFormat().format(totalStudents));
			cell.setCellStyle(getStyleBoldAlignRight());
			cell = row.createCell(cellNumber += 1);
			cell.setCellValue(getNumberFormat().format(totalAmount));
			cell.setCellStyle(getStyleBoldAlignRight());
			rowNumber++;
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(cellNumber -= 3);
			cell.setCellValue("Summa från egna kommunala anordnare");
			cell = row.createCell(cellNumber += 3);
			cell.setCellValue(getNumberFormat().format(getInCommuneSum()));
			cell.setCellStyle(getStyleAlignRight());
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(cellNumber -= 3);
			cell.setCellValue("Summa från övriga anordnare");
			cell = row.createCell(cellNumber += 3);
			cell.setCellValue(getNumberFormat().format(totalAmount - getInCommuneSum()));
			cell.setCellStyle(getStyleAlignRight());
			row = sheet.createRow(rowNumber++);
			cell = row.createCell(cellNumber -= 3);
			cell.setCellValue("Bruttosumma att utbetala");
			cell.setCellStyle(getStyleBold());
			cell = row.createCell(cellNumber += 3);
			cell.setCellValue(getNumberFormat().format(totalAmount));
			cell.setCellStyle(getStyleBoldAlignRight());
			createSigningFooter(sheet, rowNumber);
			saveExcelWorkBook(fileName, wb);
		}
	}

	private void createSigningFooter(HSSFSheet sheet, short rowNumber) {
		short cellNumber = 1;
		createStyleBold();
		row = sheet.createRow(rowNumber += 4);
		cell = row.createCell(cellNumber--);
		cell.setCellValue("Attestering");
		cell.setCellStyle(getStyleBold());
		rowNumber += 4;
		createSigningFooterDetail(sheet, rowNumber, cellNumber, "Granskingsattest");
		rowNumber = createSigningFooterDetail(sheet, rowNumber, cellNumber += 2, "Beslutsattest");
		rowNumber = createSigningFooterDetail(sheet, rowNumber += 5, cellNumber -= 2, "Behörighetsattest");
	}

	private short createSigningFooterDetail(HSSFSheet sheet, short rowNumber, short cellNumber, String text) {
		row = sheet.createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellValue(text);
		row = sheet.createRow(rowNumber += 2);
		cell = row.createCell(cellNumber);
		cell.setCellValue("Datum...............................");
		row = sheet.createRow(rowNumber += 2);
		cell = row.createCell(cellNumber);
		cell.setCellValue("...........................................");
		return rowNumber;
	}

	private void createExcelWorkBook(int[] columnWidths, String[] columnNames, String headerText) {
		wb = new HSSFWorkbook();
		createStyleBoldUnderlineAlignRight();
		createStyleBoldUnderline();
		HSSFSheet sheet = wb.createSheet("Excel");
		for (short i = 0; i < columnWidths.length; i++)
			sheet.setColumnWidth(i, (short) (columnWidths[i] * 256));
		short rowNumber = 0;
		row = sheet.createRow(rowNumber++);
		if (!headerText.equals("")) {
			row.createCell((short) 0).setCellValue(headerText);
			rowNumber++;
			row = sheet.createRow(rowNumber++);
		}
		for (short i = 0; i < columnNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(columnNames[i]);
			if (columnNames[i].equals("Belopp"))
				cell.setCellStyle(getStyleBoldUnderlineAlignRight());
			else
				cell.setCellStyle(getStyleBoldUnderline());
		}
	}

	private void saveExcelWorkBook(String fileName, HSSFWorkbook wb) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName);
		wb.write(out);
		out.close();
	}

	private void createNumberFormat() {
		numberFormat = NumberFormat.getInstance(Locale.FRENCH);
		numberFormat.setMaximumFractionDigits(0);
		numberFormat.setMinimumIntegerDigits(1);
	}

	private NumberFormat getNumberFormat() {
		return numberFormat;
	}

	private String getDeviationString() {
		return deviationString;
	}

	private void setDeviationString(String _deviationString) {
		deviationString = _deviationString;
	}

	private HSSFCellStyle getStyleAlignRight() {
		return styleAlignRight;
	}

	private HSSFCellStyle getStyleBold() {
		return styleBold;
	}

	private HSSFCellStyle getStyleBoldAlignRight() {
		return styleBoldAlignRight;
	}

	private HSSFCellStyle getStyleBoldUnderline() {
		return styleBoldUnderline;
	}

	private HSSFCellStyle getStyleBoldUnderlineAlignRight() {
		return styleBoldUnderlineAlignRight;
	}

	private HSSFCellStyle getStyleItalicUnderlineAlignRight() {
		return styleItalicUnderlineAlignRight;
	}

	private HSSFCellStyle createStyleAlignRight() {
		styleAlignRight = wb.createCellStyle();
		styleAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return styleAlignRight;
	}

	private HSSFCellStyle createStyleBold() {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleBold = wb.createCellStyle();
		styleBold.setFont(font);
		return styleBold;
	}

	private HSSFCellStyle createStyleBoldAlignRight() {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleBoldAlignRight = wb.createCellStyle();
		styleBoldAlignRight.setFont(font);
		styleBoldAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return styleBoldAlignRight;
	}

	private HSSFCellStyle createStyleBoldUnderline() {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleBoldUnderline = wb.createCellStyle();
		styleBoldUnderline.setFont(font);
		styleBoldUnderline.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		return styleBoldUnderline;
	}

	private HSSFCellStyle createStyleBoldUnderlineAlignRight() {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleBoldUnderlineAlignRight = wb.createCellStyle();
		styleBoldUnderlineAlignRight.setFont(font);
		styleBoldUnderlineAlignRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleBoldUnderlineAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		return styleBoldUnderlineAlignRight;
	}

	private HSSFCellStyle createStyleItalicUnderlineAlignRight() {
		HSSFFont italicFont = wb.createFont();
		italicFont.setItalic(true);
		styleItalicUnderlineAlignRight = wb.createCellStyle();
		styleItalicUnderlineAlignRight.setFont(italicFont);
		styleItalicUnderlineAlignRight.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleItalicUnderlineAlignRight.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		return styleItalicUnderlineAlignRight;
	}

	private long getInCommuneSum() {
		return inCommuneSum;
	}

	private void setInCommuneSum(long inCommuneSum) {
		this.inCommuneSum = inCommuneSum;
	}
	
	private IFSBusiness getIFSBusiness() {
		try {
			return (IFSBusiness) IBOLookup.getServiceInstance(iwac, IFSBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}