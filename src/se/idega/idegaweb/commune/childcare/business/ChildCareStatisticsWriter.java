/*
 * Created on 24.6.2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.process.data.CaseLog;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareStatisticsWriter {
	
	public static final String PROPERTY_LAST_UPDATED = "child_care_report_last_update";

	public boolean createReport(IWContext iwc, ICFile folder, Locale locale) {
		try {
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
			IWTimestamp fromDate = new IWTimestamp(1, 6, 2003);
			IWTimestamp toDate = new IWTimestamp();
			String lastUpdated = iwrb.getIWBundleParent().getProperty(PROPERTY_LAST_UPDATED);
			if (lastUpdated != null)
				fromDate = new IWTimestamp(lastUpdated);
				
			Collection collection = getChildCareBusiness(iwc).getCaseLogNewContracts(fromDate.getTimestamp(), toDate.getTimestamp());
			collection.addAll(getChildCareBusiness(iwc).getCaseLogAlteredContracts(fromDate.getTimestamp(), toDate.getTimestamp()));
			collection.addAll(getChildCareBusiness(iwc).getCaseLogTerminatedContracts(fromDate.getTimestamp(), toDate.getTimestamp()));
			
			if (collection.size() > 0) {
				MemoryFileBuffer buffer = new MemoryFileBuffer();
				MemoryOutputStream mos = new MemoryOutputStream(buffer);
		
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet(iwrb.getLocalizedString("child_care.report", "Childcare report"));
				int cellColumn = 0;

				sheet.setColumnWidth((short)cellColumn++, (short) (20 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (30 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (18 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				sheet.setColumnWidth((short)cellColumn++, (short) (14 * 256));
				HSSFFont font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short)12);
				HSSFCellStyle style = wb.createCellStyle();
				style.setFont(font);
		
				cellColumn = 0;
				int cellRow = 0;
				HSSFRow row = sheet.createRow((short)cellRow++);
				HSSFCell cell = row.createCell((short)0);
				cell.setCellValue(iwrb.getLocalizedString("child_care.report", "Childcare report") + ": " + fromDate.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT) + " - " + toDate.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT));
				cell.setCellStyle(style);
				cell = row.createCell((short)1);
				
				row = sheet.createRow((short)cellRow++);
				
				row = sheet.createRow((short)cellRow++);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.provider","Provider"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.name","Name"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.personal_id","Personal ID"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.address","Address"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.postal_code","Postal code"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.phone","Phone"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.care_time","Care time"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.from_date","From date"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.terminated_date","Terminated date"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.parental_status","Parental status"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.childcare_type","Childcare type"));
				cell.setCellStyle(style);
				cell = row.createCell((short)cellColumn++);
				cell.setCellValue(iwrb.getLocalizedString("child_care.status","Status"));
				cell.setCellStyle(style);
		
				User child;
				Address address;
				PostalCode postalCode = null;
				Phone phone;
				CaseLog caseLog;
				ChildCareApplication application;
				ChildCareContract archive;
				School provider;
				String status;
				
				Iterator iter = collection.iterator();
				while (iter.hasNext()) {
					cellColumn = 0;
					row = sheet.createRow((short)cellRow++);
					
					caseLog = (CaseLog) iter.next();
					application = getChildCareBusiness(iwc).getApplication(((Integer)caseLog.getCase().getPrimaryKey()).intValue());
					archive = getChildCareBusiness(iwc).getContractFile(application.getContractFileId());
					if (archive == null)
						continue;
					child = application.getChild();
					provider = application.getProvider();
					address = getCommuneUserBusiness(iwc).getUsersMainAddress(child);
					if (address != null)
						postalCode = address.getPostalCode();
					phone = getCommuneUserBusiness(iwc).getChildHomePhone(child);
		
					row.createCell((short)cellColumn++).setCellValue(provider.getSchoolName());
					Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
					row.createCell((short)cellColumn++).setCellValue(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true));
					row.createCell((short)cellColumn++).setCellValue(PersonalIDFormatter.format(child.getPersonalID(), locale));
		
					if (address != null) {
						row.createCell((short)cellColumn++).setCellValue(address.getStreetAddress());
						if (postalCode != null)
							row.createCell((short)cellColumn++).setCellValue(postalCode.getPostalAddress());
					}
					else
						cellColumn = cellColumn + 2;
					
					if (phone != null)
						row.createCell((short)cellColumn++).setCellValue(phone.getNumber());
					else
						cellColumn++;
	
					row.createCell((short)cellColumn++).setCellValue(archive.getCareTime());
					row.createCell((short)cellColumn++).setCellValue(new IWTimestamp(archive.getValidFromDate()).getLocaleDate(locale, IWTimestamp.SHORT));
		
					if (application.getRejectionDate() != null) {
						row.createCell((short)cellColumn++).setCellValue(new IWTimestamp(archive.getTerminatedDate()).getLocaleDate(locale, IWTimestamp.SHORT));
						status = iwrb.getLocalizedString("child_care.status_cancelled","Cancelled");
					}
					else {
						if (caseLog.getCaseStatusBefore().getStatus().equals(getChildCareBusiness(iwc).getCaseStatusReady().getStatus()))
							status = iwrb.getLocalizedString("child_care.status_altered","Altered");
						else
							status = iwrb.getLocalizedString("child_care.status_ready","Ready");
						cellColumn++;
					}
						
					cellColumn++;
					cellColumn++;
					row.createCell((short)cellColumn++).setCellValue(status);
				}
				
				wb.write(mos);
				buffer.setMimeType("application/vnd.ms-excel");
				InputStream mis = new MemoryInputStream(buffer);
				
				ICFileHome icFileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
				ICFile file = icFileHome.create();
				file.setFileValue(mis);
				file.setMimeType("application/vnd.ms-excel");
				file.setName("report_" + toDate.toString() + ".xls");
				file.setFileSize(buffer.length());
				file.store();
				folder.addChild(file);
		
				iwrb.getIWBundleParent().setProperty(PROPERTY_LAST_UPDATED, toDate.getTimestamp().toString());
			
				return true;
			}
			else
				return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
}