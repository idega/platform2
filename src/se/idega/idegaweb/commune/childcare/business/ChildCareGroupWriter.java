package se.idega.idegaweb.commune.childcare.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class ChildCareGroupWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private ChildCareBusiness business;
	private CommuneUserBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;
	
	private String schoolName;
	private String groupName;

	public final static String PARAMETER_PROVIDER_ID = "provider_id";
	public final static String PARAMETER_GROUP_ID = "group_id";
	public final static String PARAMETER_SHOW_NOT_YET_ACTIVE = "show_not_yet_active";
	
	public final static String PARAMETER_TYPE = "print_type";

	public final static String XLS = "xls";
	public final static String PDF = "pdf";
	
	public ChildCareGroupWriter() {
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			business = getChildCareBusiness(iwc);
			userBusiness = getCommuneUserBusiness(iwc);
			iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			
			if (req.getParameter(PARAMETER_PROVIDER_ID) != null && req.getParameter(PARAMETER_GROUP_ID) != null) {
				int groupID = Integer.parseInt(req.getParameter(PARAMETER_GROUP_ID));
				int providerID = Integer.parseInt(req.getParameter(PARAMETER_PROVIDER_ID));
				boolean showNotYetActive = false;
				boolean hasShowNotYetActive = false;
				if (req.getParameter(PARAMETER_SHOW_NOT_YET_ACTIVE) != null) {
					hasShowNotYetActive = true;
					showNotYetActive = Boolean.valueOf(req.getParameter(PARAMETER_SHOW_NOT_YET_ACTIVE)).booleanValue();
				}
				
				IWTimestamp stamp = new IWTimestamp();
				Collection students = null;
				if (hasShowNotYetActive)
					students = business.getSchoolBusiness().findStudentsInSchoolByDate(providerID, groupID, stamp.getDate(), showNotYetActive);
				else
					students = business.getSchoolBusiness().findStudentsInSchoolByDate(providerID, groupID, stamp.getDate());
				
				if (groupID != -1)
					groupName = business.getSchoolBusiness().findSchoolClass(new Integer(groupID)).getSchoolClassName();
				schoolName = business.getSchoolBusiness().getSchool(new Integer(providerID)).getSchoolName();
				
				String type = req.getParameter(PARAMETER_TYPE);
				if (type.equals(PDF)) {
					buffer = writePDF(students);
				}
				else if (type.equals(XLS)) {
					buffer = writeXLS(students);
				}
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
	
	public MemoryFileBuffer writeXLS(Collection students) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		if (!students.isEmpty()) {
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet(schoolName);
	    sheet.setColumnWidth((short)0, (short) (30 * 256));
	    sheet.setColumnWidth((short)1, (short) (14 * 256));
	    sheet.setColumnWidth((short)2, (short) (30 * 256));
	    sheet.setColumnWidth((short)3, (short) (14 * 256));
			sheet.setColumnWidth((short)4, (short) (14 * 256));
	    HSSFFont font = wb.createFont();
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    font.setFontHeightInPoints((short)12);
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setFont(font);

			int cellRow = 0;
			HSSFRow row = sheet.createRow((short)cellRow++);
			HSSFCell cell = row.createCell((short)0);
			cell.setCellValue(schoolName);
			cell.setCellStyle(style);
			cell = row.createCell((short)1);
			
			if (groupName != null) {
				row = sheet.createRow((short)cellRow++);
				cell = row.createCell((short)0);
				cell.setCellValue(groupName);
				cell.setCellStyle(style);
			}
			
			row = sheet.createRow((short)cellRow++);
			
	    row = sheet.createRow((short)cellRow++);
	    cell = row.createCell((short)0);
	    cell.setCellValue(iwrb.getLocalizedString("school.name","Name"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)1);
	    cell.setCellValue(iwrb.getLocalizedString("school.personal_id","Personal ID"));
	    cell.setCellStyle(style);
	    cell = row.createCell((short)2);
	    cell.setCellValue(iwrb.getLocalizedString("school.address","Address"));
	    cell.setCellStyle(style);
			cell = row.createCell((short)3);
			cell.setCellValue(iwrb.getLocalizedString("school.postal_code","Postal code"));
			cell.setCellStyle(style);
	    cell = row.createCell((short)4);
	    cell.setCellValue(iwrb.getLocalizedString("school.phone","Phone"));
	    cell.setCellStyle(style);

			User student;
			Address address;
			PostalCode postalCode = null;
			Phone phone;
			SchoolClassMember studentMember;
			
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				row = sheet.createRow((short)cellRow++);
				studentMember = (SchoolClassMember) iter.next();
				student = studentMember.getStudent();
				address = userBusiness.getUsersMainAddress(student);
				if (address != null)
					postalCode = address.getPostalCode();
				phone = userBusiness.getChildHomePhone(student);

		    row.createCell((short)0).setCellValue(student.getNameLastFirst(true));
		    row.createCell((short)1).setCellValue(PersonalIDFormatter.format(student.getPersonalID(), locale));
		    if (address != null) {
			    row.createCell((short)2).setCellValue(address.getStreetAddress());
			    if (postalCode != null)
						row.createCell((short)3).setCellValue(postalCode.getPostalAddress());
		    }
			  if (phone != null)
			    row.createCell((short)4).setCellValue(phone.getNumber());
			}
			wb.write(mos);
		}
		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
	
	public MemoryFileBuffer writePDF(Collection students) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		if (!students.isEmpty()) {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(document, mos);
			document.addTitle(schoolName);
			document.addAuthor("Idega Reports");
			document.addSubject(schoolName);
			document.open();
			
			document.add(new Phrase(schoolName+"\n", new Font(Font.HELVETICA, 12, Font.BOLD)));
			if (groupName != null)
				document.add(new Phrase(groupName+"\n", new Font(Font.HELVETICA, 12, Font.BOLD)));
			document.add(new Phrase("\n", new Font(Font.HELVETICA, 12, Font.BOLD)));
			
			User student;
			Address address;
			PostalCode postalCode = null;
			Phone phone;
			SchoolClassMember studentMember;
			Cell cell;
			
			String[] headers = {"Name", "PersonalID", "Address", "Postal code", "Phone"};
			int[] sizes = { 35, 20, 25, 10, 10 };

			Table datatable = getTable(headers, sizes);
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				studentMember = (SchoolClassMember) iter.next();
				student = studentMember.getStudent();
				address = userBusiness.getUsersMainAddress(student);
				if (address != null)
					postalCode = address.getPostalCode();
				phone = userBusiness.getChildHomePhone(student);

				cell = new Cell(new Phrase(student.getNameLastFirst(true), new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				cell = new Cell(new Phrase(PersonalIDFormatter.format(student.getPersonalID(), locale), new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				String streetAddress = "";
				if (address != null)
					streetAddress = address.getStreetAddress();
				cell = new Cell(new Phrase(streetAddress, new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				String postalAddress = "";
				if (address != null && postalCode != null)
				postalAddress = postalCode.getPostalAddress();
				cell = new Cell(new Phrase(postalAddress, new Font(Font.HELVETICA, 10, Font.BOLD)));
				cell.setBorder(Rectangle.NO_BORDER);
				datatable.addCell(cell);

				String phoneNumber = "";
				if (phone != null)
					phoneNumber = phone.getNumber();
				cell = new Cell(new Phrase(phoneNumber, new Font(Font.HELVETICA, 10, Font.BOLD)));
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

	protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
}