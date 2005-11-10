package se.idega.idegaweb.commune.childcare.presentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

/**
 * This class generates Excel output for ChildCareProviderDatesForChanges block
 * 
 * @author Dainis Brjuhoveckis
 * 
 */
public class ChildCareDatesForChangesWriter extends DownloadWriter implements
		MediaWritable {

	private MemoryFileBuffer buffer = null;
    
    private IWResourceBundle iwrb = null;

	public void init(HttpServletRequest req, IWContext iwc) {
		try {
            setIwrb(iwc.getIWMainApplication().getBundle(ChildCareProviderDatesForChanges.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc));
            
			// parse params
            Integer providerId = new Integer(req.getParameter(ChildCareProviderDatesForChanges.PARAMETER_PROVIDER_ID));
                        
            java.sql.Date startFromDate = getSqlDateParameter(req, ChildCareProviderDatesForChanges.PARAMETER_START_FROM);
            java.sql.Date startToDate = getSqlDateParameter(req, ChildCareProviderDatesForChanges.PARAMETER_START_TO);            
            java.sql.Date endFromDate = getSqlDateParameter(req, ChildCareProviderDatesForChanges.PARAMETER_END_FROM);
            java.sql.Date endToDate = getSqlDateParameter(req, ChildCareProviderDatesForChanges.PARAMETER_END_TO);
            
			// get data from business
            ChildCareBusiness business = getChildCareBusiness(iwc);
		    Collection contracts = business
					.getChildCareContractsByProviderAndClassMemberDates(
							providerId, startFromDate, startToDate,
							endFromDate, endToDate);
            
			// genereate xls
			buffer = writeXls(iwc, contracts);
			setAsDownload(iwc, "dates_for_changes.xls", buffer.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private IWTimestamp getIWTimestampParameter(HttpServletRequest req, String paramName) {
    	String date = req.getParameter(paramName);                                                        
        IWTimestamp stamp = null;            
        if (date.length() > 0)
            stamp = new IWTimestamp(date);
        return stamp;
    }
    
    private java.sql.Date getSqlDateParameter(HttpServletRequest req, String paramName) {
    	java.sql.Date date = null;                                                        
        IWTimestamp stamp = getIWTimestampParameter(req, paramName);            
        if (stamp != null)
            date = stamp.getDate();
        return date;
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

	
	private MemoryFileBuffer writeXls(IWContext iwc, Collection contracts) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Worksheet");
		
		int rowNum = 0;
        
        HSSFRow row = sheet.createRow((short) rowNum++);
        fillHeaderRow(wb, sheet, row);        
        
        //loop through data and add it to excel
        if (contracts != null) {
            ChildCareContract contract = null;
            ChildCareApplication application = null;
            SchoolClassMember classMember = null;
            User child = null;
            School provider = null;
            
            Iterator iter = contracts.iterator();            
            while (iter.hasNext()) {
                 contract = (ChildCareContract) iter.next();
                 application = contract.getApplication();
                 classMember = contract.getSchoolClassMember();
                 child = application.getChild();
                 provider = application.getProvider();
                 
                 // add data to row
                 row = sheet.createRow((short) rowNum++);
                 fillDataRow(iwc, wb, sheet, row, contract, application, classMember, child, provider);
                 
            }
        }
        
		wb.write(mos);

		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}
    
    
    /**
     * Fills given row with headers
     * 
     * @param wb
     * @param sheet
     * @param row
     */
    private void fillHeaderRow(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row) { 
        //create style of header font
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 11);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
        
        // here we could create array of strings, I mean headers
        String[] headers = { 
                getLocalizedString("name", "Name"), 
                getLocalizedString("persId", "PersId"), 
                getLocalizedString("provider", "Provider"), 
                getLocalizedString("reqStart", "Req. start"),
                getLocalizedString("systemStart", "System start"), 
                getLocalizedString("cancelMade", "Cancel made"), 
                getLocalizedString("reqCancel", "Req. cancel"), 
                getLocalizedString("systemCancel", "System cancel"),
                getLocalizedString("diffStartDates", "Diff start dates"), 
                getLocalizedString("diffEndDates", "Diff end dates") };
        
        HSSFCell cell;        
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell((short) i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            
            sheet.setColumnWidth((short) i, (short) (16 * 256)); 
        }  
        
    }
    
    protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc) throws RemoteException {
        return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);  
    }
    
    private void fillDataRow(IWContext iwc, HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
            ChildCareContract contract, ChildCareApplication application,
            SchoolClassMember classMember, User child, School provider){        
        row.createCell((short) 0).setCellValue(child.getFirstName() + " " + child.getLastName());
        row.createCell((short) 1).setCellValue(child.getPersonalID());
        row.createCell((short) 2).setCellValue(provider.getName());
        
        Date fromDateRequested = application.getFromDateRequested();
        Date registerDate = classMember.getRegisterDate();
        Date cancelRequestReceived = application.getCancelRequestReceived();
        Date cancelDateRequested = application.getCancelDateRequested();
        Date removedDate = classMember.getRemovedDate();      
           
        row.createCell((short) 3).setCellValue(dateToLocalizedString(iwc, fromDateRequested));  
        row.createCell((short) 4).setCellValue(dateToLocalizedString(iwc, registerDate));
        row.createCell((short) 5).setCellValue(dateToLocalizedString(iwc, cancelRequestReceived));
        row.createCell((short) 6).setCellValue(dateToLocalizedString(iwc, cancelDateRequested));
        row.createCell((short) 7).setCellValue(dateToLocalizedString(iwc, removedDate));        
        
        row.createCell((short) 8).setCellValue(daysBetweenDates(registerDate, fromDateRequested));
        row.createCell((short) 9).setCellValue(daysBetweenDates(cancelDateRequested, removedDate));
        
    }
    
    private String dateToLocalizedString(IWContext iwc, Date date){
        String s = "";        
        if(date != null) {
            IWCalendar iwcal = new IWCalendar(iwc.getCurrentLocale(), date);
            s = iwcal.getLocaleDate(IWCalendar.SHORT);
        }        
        return s;
    }
    
    private String daysBetweenDates(Date date1, Date date2) {
        String s = "";        
        if (date1 != null && date2 != null) {
            IWTimestamp stamp1 = new IWTimestamp(date1);
            IWTimestamp stamp2 = new IWTimestamp(date2);
            
            int days = IWTimestamp.getDaysBetween(stamp1, stamp2);
            s = Integer.toString(days);
        }       
        return s;
    }
    
    private String getLocalizedString(String key, String defaultValue) {
        String simpleName = null;        
        StringTokenizer parser = new StringTokenizer(this.getClass().getName(), ".");
        while (parser.hasMoreTokens()) {
            simpleName = parser.nextToken();
        }        
        String s = getIwrb().getLocalizedString(simpleName + "." + key, defaultValue);        
        return s;
    }

    public IWResourceBundle getIwrb() {
        return iwrb;
    }

    public void setIwrb(IWResourceBundle iwrb) {
        this.iwrb = iwrb;
    }     

}
