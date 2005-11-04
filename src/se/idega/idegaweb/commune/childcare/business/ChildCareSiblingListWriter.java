package se.idega.idegaweb.commune.childcare.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
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
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareAdmin;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
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
 * Copyright:    Copyright (c) 2005
 * Company:      idega multimedia
 * @author       
 * @version 1.0
 */
public class ChildCareSiblingListWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private ChildCareBusiness business;
	
	private Locale locale;
	private IWResourceBundle iwrb;
	
	private String schoolName;
	private String groupName;

	public final static String PARAMETER_PROVIDER_ID = "provider_id";
	public final static String PARAMETER_GROUP_ID = "group_id";
	public final static String PARAMETER_SHOW_NOT_YET_ACTIVE = "show_not_yet_active";
	
	public final static String PARAMETER_SORT_BY = "cc_sort_by";
	public final static String PARAMETER_NUMBER_PER_PAGE = "cc_number_per_page";
	public final static String PARAMETER_FROM_DATE = "cc_from_date";
	public final static String PARAMETER_TO_DATE = "cc_to_date";
	public final static String PARAMETER_START = "cc_start";
	
	public final static String PARAMETER_TYPE = "print_type";

//	public final static String XLS = "xls";
	//public final static String PDF = "pdf";
    
    private int providerId = 0;
	
	public ChildCareSiblingListWriter() {
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			business = getChildCareBusiness(iwc);
		
			iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			
			if (req.getParameter(PARAMETER_PROVIDER_ID) != null) {
				//int groupID = Integer.parseInt(req.getParameter(PARAMETER_GROUP_ID));
				providerId = Integer.parseInt(req.getParameter(PARAMETER_PROVIDER_ID));
				int sortBy = Integer.parseInt(req.getParameter(PARAMETER_SORT_BY));
				int numberPerPage = Integer.parseInt(req.getParameter(PARAMETER_NUMBER_PER_PAGE));
				int start = Integer.parseInt(req.getParameter(PARAMETER_START));
				String fromDate = req.getParameter(PARAMETER_FROM_DATE);
				String toDate = req.getParameter(PARAMETER_TO_DATE);
															
				IWTimestamp stampFrom = null;
				IWTimestamp stampTo = null;
				
				
				if (fromDate != null)
					stampFrom = new IWTimestamp(fromDate);
				if (toDate != null) 
					stampTo = new IWTimestamp(toDate);
					
				Collection applications = null;
				if (stampFrom != null && stampTo != null)
					applications = getApplicationCollection(iwc, providerId, sortBy, numberPerPage, start, stampFrom.getDate(), stampTo.getDate());
				else {
					applications = getApplicationCollection(iwc, providerId, sortBy, numberPerPage, start, null, null);
				}
				schoolName = business.getSchoolBusiness().getSchool(new Integer(providerId)).getSchoolName();
								
				//String type = req.getParameter(PARAMETER_TYPE);
				//if (type.equals(PDF)) {
					//buffer = writePDF(applications, iwc);
					//setAsDownload(iwc,"childcare_siblinglist.pdf",buffer.length());
				//}
				//else if (type.equals(XLS)) {
					buffer = writeXLS(applications, iwc);
					setAsDownload(iwc,"childcare_siblinglist.xls",buffer.length());
			//	}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return super.getMimeType();
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
	
	public MemoryFileBuffer writeXLS(Collection applications, IWContext iwc)
            throws Exception {
        MemoryFileBuffer buffer = new MemoryFileBuffer();
        MemoryOutputStream mos = new MemoryOutputStream(buffer);
        if (!applications.isEmpty()) {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(schoolName);
            sheet.setColumnWidth((short) 0, (short) (30 * 256));
            sheet.setColumnWidth((short) 1, (short) (14 * 256));
            sheet.setColumnWidth((short) 2, (short) (14 * 256));
            sheet.setColumnWidth((short) 3, (short) (14 * 256));
            sheet.setColumnWidth((short) 4, (short) (14 * 256));
            sheet.setColumnWidth((short) 5, (short) (14 * 256));
            sheet.setColumnWidth((short) 6, (short) (14 * 256));
            sheet.setColumnWidth((short) 7, (short) (30 * 256));

            HSSFFont font = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setFontHeightInPoints((short) 12);
            HSSFCellStyle style = wb.createCellStyle();
            style.setFont(font);

            int cellRow = 0;
            HSSFRow row = sheet.createRow((short) cellRow++);
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellValue(schoolName);
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);

            if (groupName != null) {
                row = sheet.createRow((short) cellRow++);
                cell = row.createCell((short) 0);
                cell.setCellValue(groupName);
                cell.setCellStyle(style);
            }

            row = sheet.createRow((short) cellRow++);

            row = sheet.createRow((short) cellRow++);

            cell = row.createCell((short) 0);
            cell.setCellValue(iwrb
                    .getLocalizedString("child_care.name", "Name"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 1);
            cell.setCellValue(iwrb.getLocalizedString("child_care.personal_id",
                    "Personal ID"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 2);
            cell.setCellValue(iwrb.getLocalizedString("child_care.queue_date",
                    "Queue date"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 3);
            cell.setCellValue(iwrb.getLocalizedString(
                    "child_care.placement_date", "Placement date"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 4);
            cell.setCellValue(iwrb.getLocalizedString("child_care.order",
                    "Order"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 5);
            cell.setCellValue(iwrb.getLocalizedString("child_care.queue_order",
                    "Queue order"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 6);
            cell.setCellValue(iwrb.getLocalizedString("child_care.status",
                    "Status"));
            cell.setCellStyle(style);
            cell = row.createCell((short) 7);
            cell.setCellValue(iwrb.getLocalizedString(
                    "child_care.current_provider", "Current provider"));
            cell.setCellStyle(style);

            ChildCareApplication application;
            User child;
            IWCalendar queueDate;
            IWCalendar placementDate;
            String statusCC = null;
            // boolean hasOtherPlacing = false;
            // boolean hasMessage = false;
            int queueOrder = -1;
            int netOrder = -1;

            int ordering = getOrdering(providerId); 
            
            Iterator iter = applications.iterator();
            while (iter.hasNext()) {
                row = sheet.createRow((short) cellRow++);
                application = (ChildCareApplication) iter.next();
                child = application.getChild();

                queueDate = new IWCalendar(iwc.getCurrentLocale(), application
                        .getQueueDate());
                placementDate = new IWCalendar(iwc.getCurrentLocale(),
                        application.getFromDate());
                queueOrder = getChildCareBusiness(iwc).getNumberInQueue(application, ordering);
                statusCC = getChildCareBusiness(iwc).getStatusString(
                        application.getApplicationStatus());

                School provider = getChildCareBusiness(iwc)
                        .getCurrentProviderByPlacement(application.getChildId());
                String school = null;
                if (provider != null) {
                    school = provider.getName();
                } else {
                    school = "";
                }

                if (application.getApplicationStatus() == getChildCareBusiness(
                        iwc).getStatusSentIn())
                    netOrder = getChildCareBusiness(iwc)
                            .getNumberInQueueByStatus(application, ordering);
                else
                    netOrder = -1;

                // hasOtherPlacing =
                // getChildCareBusiness(iwc).hasBeenPlacedWithOtherProvider(application.getChildId(),
                // providerID);
                //hasMessage = application.getMessage() != null;

                Name name = new Name(child.getFirstName(), child
                        .getMiddleName(), child.getLastName());
                row.createCell((short) 0).setCellValue(
                        name.getName(locale, true));
                row.createCell((short) 1).setCellValue(
                        PersonalIDFormatter.format(child.getPersonalID(),
                                locale));

                if (queueDate != null) {
                    row.createCell((short) 2).setCellValue(
                            queueDate.getLocaleDate(IWCalendar.SHORT));
                }
                if (placementDate != null) {
                    row.createCell((short) 3).setCellValue(
                            placementDate.getLocaleDate(IWCalendar.SHORT));
                }
                if (netOrder != -1)
                    row.createCell((short) 4).setCellValue(netOrder);

                if (queueOrder != -1)
                    row.createCell((short) 5).setCellValue(queueOrder);

                row.createCell((short) 6).setCellValue(statusCC);

                row.createCell((short) 7).setCellValue(school);

            }
            wb.write(mos);
        }

        buffer.setMimeType("application/x-msexcel");
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

	private Collection getApplicationCollection(IWContext iwc, int childcareId, int sortBy, int numberPerPage, int start, Date fromDate, Date toDate) throws RemoteException {
		Collection applications;
        
        int ordering = getOrdering(childcareId); 
        
		if (sortBy != -1 && fromDate != null && toDate != null) // && sortBy != SORT_ALL)
			applications = getChildCareBusiness(iwc).getUnhandledApplicationsByProvider(childcareId, numberPerPage, start, sortBy, fromDate, toDate, ordering);
		else
		    //applications = getChildCareBusiness(iwc).getUnhandledApplicationsByProvider(childcareId);
            //Dainis 2005-09-30: lets use the same method as in ChildCareAdmin instead
            applications = getChildCareBusiness(iwc).getUnhandledApplicationsByProvider(childcareId, Integer.MAX_VALUE, 0, ordering);
		
		return applications;
	}
	
	protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
    
    private int getOrdering(int providerId) throws RemoteException {
        School provider = business.getSchoolBusiness().getSchool(new Integer(providerId));
        int ordering = provider.getSortByBirthdate() ? ChildCareAdmin.ORDER_BY_DATE_OF_BIRTH : ChildCareAdmin.ORDER_BY_QUEUE_DATE;
        return ordering;
    }    
    
}