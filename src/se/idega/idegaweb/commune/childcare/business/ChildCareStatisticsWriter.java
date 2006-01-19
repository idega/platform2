/*
 * Created on 24.6.2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.FinderException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.data.CareTime;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.block.process.data.CaseLog;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
				HSSFRow row = sheet.createRow(cellRow++);
				HSSFCell cell = row.createCell((short)0);
				cell.setCellValue(iwrb.getLocalizedString("child_care.report", "Childcare report") + ": " + fromDate.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT) + " - " + toDate.getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT));
				cell.setCellStyle(style);
				cell = row.createCell((short)1);
				
				row = sheet.createRow(cellRow++);
				
				row = sheet.createRow(cellRow++);
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
					row = sheet.createRow(cellRow++);
					
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
	
					row.createCell((short)cellColumn++).setCellValue(getCareTime(getChildCareBusiness(iwc), iwrb, archive.getCareTime()));
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
	
	protected String getCareTime(ChildCareBusiness business, IWResourceBundle resourceBundle, String careTime) {
		try {
			Integer.parseInt(careTime);
		}
		catch (NumberFormatException nfe) {
			try {
				CareTime time = business.getCareTime(careTime);
				return resourceBundle.getLocalizedString(time.getLocalizedKey(), careTime);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				re.printStackTrace();
			}
		}
		return careTime;
	}

	protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}

	protected CommuneUserBusiness getCommuneUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);	
	}
	
	/*
	 * Contract and placement changes
	 * This is copy from ContractAndPlacementChangesExportWriter	  	 
	 */
	public static final String SEPARATOR = ";";
	
	IWContext iwc;
	
	private boolean isDebuggingGoingOn = false;
	
	public boolean createExportFile(IWContext iwc, ICFile folder, Date reportDate) {
		
		this.iwc = iwc;
		
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			
			
			Iterator iter = getExportData(reportDate).iterator(); 
			while (iter.hasNext()) {
				DataForExport row = (DataForExport) iter.next();
				try {
					mos.write(row.getContents().getBytes("UTF8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			buffer.setMimeType("text/plain"); 
			InputStream mis = new MemoryInputStream(buffer);
	
			ICFileHome icFileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
			ICFile file = icFileHome.create();
			file.setFileValue(mis);
			file.setMimeType("text/plain");
			
//			IWTimestamp stamp = new IWTimestamp();
//			String s = stamp.toString().replaceAll(":", "_"); // on windows filenames cannot contain colons
//			s = s.toString().replaceAll(" ", "_");
//			s = s.substring(0, s.length()- 3); //cut off seconds (last 3 characters)
			
			//real guys use DateFormatter :)
			//Date now = new Date();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm");  //export_2006-01-13_21_02.txt
		    String s = formatter.format(new Date());			 
			
			file.setName("export_" + s + ".txt");			
			file.setFileSize(buffer.length());
			file.store();
			
			mos.close();
			mis.close();
			
			folder.addChild(file);
			
			return true;			
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	private Date getFirstDateOfCurrentMonth() {		
	    Calendar cal = roundCalendarToDate(new GregorianCalendar());
	    
	    //cal.add(Calendar.MONTH, 1); //XXX debug
	        
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // It will be 1	    
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    cal = roundCalendarToDate(cal);
	    
	    //return new Date(cal.getTimeInMillis());
	    return roundDateToDate(cal.getTime());	
	    
	}	

	private Date getLastDateOfCurrentMonth() {		
	    Calendar cal = roundCalendarToDate(new GregorianCalendar());
	    
	    //cal.add(Calendar.MONTH, 1); //XXX debug
	        
	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);	    
	    cal.set(Calendar.DAY_OF_MONTH, days);
	    cal = roundCalendarToDate(cal);

	    //return new Date(cal.getTimeInMillis());
	    return roundDateToDate(cal.getTime());	
	}
	
	private Date getLastDateOfSpecifiedDatesMonth(Date date) {
		Calendar cal = roundCalendarToDate(new GregorianCalendar());
		cal.setTimeInMillis(date.getTime());		
	    
	    //cal.add(Calendar.MONTH, 1); //XXX debug
	        
	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);	    
	    cal.set(Calendar.DAY_OF_MONTH, days);	    

	    return roundDateToDate(cal.getTime());	
	}
	
	private Calendar roundCalendarToDate(Calendar cal){
	    cal.set(Calendar.HOUR, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal;
	}	

	private Date timestampToRoundedDate(Timestamp stamp) {
		if (stamp == null) return null;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(stamp.getTime());
		return roundDateToDate(roundCalendarToDate(cal).getTime());
		
	}
	
	private Date roundDateToDate(Date date) {
		if (date == null) {
			return null;
		}
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String s = formatter.format(date);
		
	    Date roundedDate = null;
	    try {
			roundedDate = formatter.parse(s);
		}
		catch (ParseException e) {}		
	    
	    return roundedDate;
	}
	
	/*
	private Date roundDateToDayOfMonth(Date date) {
		if (date == null) return null;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date.getTime());
		return roundCalendarToDate(cal).getTime();
	}
	
	private String nullStringToEmpty(String s) {
		if(s == null) {
			return "";
		}
		else {
			return s;
		}
	}
	*/
	
	private String dateToString(Date date) {
		if(date == null) return "";
		IWTimestamp stamp = new IWTimestamp(date);
		return stamp.getDateString("yyyy-MM-dd");	
	}
	
	private java.sql.Date utilDateToSqlDate(Date d) {
		if(d == null) return null;
		return new java.sql.Date(d.getTime());
	}
	
	private Collection getExportData(Date reportDate)  {
		
		//Date firstDateOfCurrentMonth = this.getFirstDateOfCurrentMonth();
		//Date lastDateOfCurrentMonth = this.getLastDateOfCurrentMonth();
		Date firstDateOfCurrentMonth = roundDateToDate(reportDate);
		Date lastDateOfCurrentMonth = getLastDateOfSpecifiedDatesMonth(reportDate);
		
//		long a = reportDate.getTime();
//		long b = firstDateOfCurrentMonth.getTime();
		
		Vector uberCollection = new Vector();
		Vector smallCollection = null;		
		
		try {
			//get all placements
			//retrieve the placements we need and add them to the ubercolection
			//Collection allPlacements = getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare());
			Collection allPlacements = getSchoolClassMemberHome().findAllByCategoryForPlacementChangesExport(getSchoolBusiness().getCategoryChildcare(), utilDateToSqlDate(firstDateOfCurrentMonth), utilDateToSqlDate(lastDateOfCurrentMonth));
			
			Iterator placementsIterator =  allPlacements.iterator();
			
			boolean takeThisPlacement = false;
			
			for(; placementsIterator.hasNext();) {
				SchoolClassMember placement = (SchoolClassMember) placementsIterator.next();
								
				smallCollection = new Vector();
				
				Date registerDate = timestampToRoundedDate(placement.getRegisterDate()); //java.sql.Timestamp; and only contains date
				if (registerDate == null ) continue;

				Date removedDate = timestampToRoundedDate(placement.getRemovedDate()); //java.sql.Timestamp; contains date and time
				
				/* moved to sql level
				 * SchoolClassMemberBMPBean.ejbFindAllByCategoryForPlacementChangesExport
				//XXX this should be done in SQL in the first place
				if (registerDate.compareTo(lastDateOfCurrentMonth) > 0)	continue;
				if (removedDate != null && removedDate.compareTo(firstDateOfCurrentMonth) < 0)	continue;				
				if (registerDate.compareTo(firstDateOfCurrentMonth) < 0 && 
						(removedDate == null || removedDate.compareTo(lastDateOfCurrentMonth) > 0)) continue;				
				*/				
				
				// here get all contracts for this placement 				
				Vector placementContracts = (Vector) getChildCareContractHome().findAllBySchoolClassMember(placement);				
				
				Iterator placementLogs = getSchoolClassMemberLogHome().findAllBySchoolClassMember(placement).iterator();
				while (placementLogs.hasNext()){
					takeThisPlacement = false;
					
					SchoolClassMemberLog log = (SchoolClassMemberLog) placementLogs.next();
					
					Date startDate = log.getStartDate(); 	//java.util.Date, contains date only
					Date endDate = log.getEndDate(); 		//java.util.Date, contains date only
					
//					2.	IF it’s a new placement where sch_class_member.register_date >= 1st of current month 
//					AND sch_class_member.register_date <= last date of current month					
					if (registerDate.equals(startDate) && isDateInInterval(registerDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth))  //placement started
						takeThisPlacement = true;

//					3.	IF it’s a placement that ends (has removed_date set in sch_class_member) and 
//					sch_class_member.removed date <= last date of current month
//					and sch_class_member.removed date >= first date of current month					
					if (removedDate != null && (isDateInInterval(removedDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth) & removedDate.equals(endDate))) //placement ended
						takeThisPlacement = true;
						
//					4.	IF a group has been changed (new entry in sch_class_member_log with 
//						sch_class_member_log.start date >= 1st of current month 
//						AND sch_class_member_log.start date <= last date of current month)
					if (takeThisPlacement || isDateInInterval(startDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth)) // group was changed
					{						
						String careTime = "care_time_string_not_found";
						
//				        step 1.  look for  caretime string, which contract.validFromDate = log.startDate
//			            if found, then:
//			                -   caretime string is found;
//			                -   throw away this contract from contracts array
						ChildCareContract contract = null;
						for (Iterator contractsIter = placementContracts.iterator();contractsIter.hasNext();) {
							ChildCareContract c = (ChildCareContract) contractsIter.next();						
							contract = null;
							
							Date validFromDate = c.getValidFromDate(); //java.util.Date , only date
							if (validFromDate == null) continue;
							
							if (validFromDate.equals(startDate)) {
								contract = c;
								break;
							}
						}
						if (contract != null) {							
							placementContracts.remove(contract);
						}
						
//				        step 2.  if caretime not found in step 1., then look for   contract.validFromDate < log.startDate (and the closest one to log.startDate)
//			            if found, then:
//			                -   caretime string is found;
						if (contract == null) {	
							for (Iterator contractsIter = placementContracts.iterator();contractsIter.hasNext();) {
								ChildCareContract c = (ChildCareContract) contractsIter.next();	
								
								Date validFromDate = c.getValidFromDate(); //java.util.Date , only date
								if (validFromDate == null) continue; //fool's protection
								
								if (validFromDate.before(startDate)) {
									if (contract != null ) {
										if (contract.getValidFromDate().before(c.getValidFromDate())) {
											contract = c;											
										}
									} else {
										contract = c;
									}
								}
								
							}
						}
						
						if (contract != null) {
							careTime = contract.getCareTime();
						}
						
						Date endOfPlacementDate = null;
						if (removedDate != null && endDate != null && removedDate.equals(endDate)) {
							endOfPlacementDate = removedDate;
						}
						smallCollection.add(new DataForExport(log.getSchoolClassMember(),startDate, endOfPlacementDate, careTime));					
					}
					
				}
				
				//this is the place for rule 5
				// if there's something left in placementContracts, then we will use them
				if (!placementContracts.isEmpty()) {
					for (Iterator contractsIter = placementContracts.iterator();contractsIter.hasNext();) {
						ChildCareContract contract = (ChildCareContract) contractsIter.next();
						
//						5.	IF a caretime has been changed (new entry in comm_childcare_archive with 
//							comm_childcare_archive.start date >=1st of current month AND comm_childcare_archive.start date <= last date of current month)
						Date validFromDate = contract.getValidFromDate(); //java.util.Date , only date
						if (validFromDate == null) continue;
						
						Date terminatedDate = contract.getTerminatedDate();
						Date endOfPlacementDate = null;
						if (removedDate != null && terminatedDate != null && removedDate.equals(terminatedDate)) {
							endOfPlacementDate = removedDate;
						}
						
						if ( isDateInInterval(validFromDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth)) {
							smallCollection.add(new DataForExport(contract.getSchoolClassMember(),validFromDate, endOfPlacementDate, contract.getCareTime()));					
						}
					}
					
				}
				
				/*
				there is a trick now: if placement has removed_date set, then in the last record for this placement we must show this removed date. And only in last!
				e.g. instead of this:
				null;null;0109286393;2006-01-10;          ;02;null
		        null;null;0109286393;2006-01-14;2006-01-24;02;null <caretime change
		        null;null;0109286393;2006-01-18;2006-01-24;33;null <placement change			
					
				this must be shown:
				null;null;0109286393;2006-01-10;          ;02;null
		        null;null;0109286393;2006-01-14;          ;02;null 
		        null;null;0109286393;2006-01-18;2006-01-24;33;null
				*/
				if (removedDate != null && (!smallCollection.isEmpty())) {
					Collections.sort(smallCollection);
					
					for (int i = 0; i < (smallCollection.size() - 1); i++) {
						DataForExport dfe = (DataForExport) smallCollection.elementAt(i);
						if (dfe.getEndDate() != null) {
							dfe.setEndDate(null);
						}
					}
					
				}	
				
				
				uberCollection.addAll(smallCollection);

			}
			
			//reshuffle the ubercollection so it is in correct order
			Collections.sort(uberCollection);	
			
			return uberCollection;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return new Vector();
		}		
		
	}


	private boolean isDateInInterval(Date date, Date startDate, Date endDate) {
		return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0; 
	}	
	
	private SchoolClassMemberHome getSchoolClassMemberHome() throws IDOLookupException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	private SchoolClassMemberLogHome getSchoolClassMemberLogHome() throws IDOLookupException {
		return (SchoolClassMemberLogHome) IDOLookup.getHome(SchoolClassMemberLog.class);
	}	

	private SchoolBusiness getSchoolBusiness() throws IBOLookupException{
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);   
	}
	
	private ChildCareContractHome getChildCareContractHome() throws IDOLookupException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}
	
	private class DataForExport implements Comparable {			
		
		SchoolClassMember member = null;
		Date startDate  = null;
		Date endDate  = null;
		String careTimeString = null;		
		
		String EMPTY_DATE = "          "; //10 spaces
		
		public DataForExport(SchoolClassMember member, Date startDate, Date endDate, String careTimeString) {
			this.member = member;
			this.startDate = startDate;
			this.endDate = endDate;
			this.careTimeString = careTimeString;
		}
		
		public String formatPersonalId(String personalId) {
			if (personalId == null) return null;
			return personalId.substring(2, personalId.length());
		}
		
		public String formatEndDate(String s) {
			if (s == null) return null;
			if (s.length() < 10)
				return EMPTY_DATE;
			else
				return s;
		}	
		
		public String formatCaretime(String s) {
			if (s == null) return null;
			if (s.length() == 1) 
				return "0" + s;
			else 
				return s;
		}
		

		public int compareTo(Object arg0) { // must return > 0 if argument is smaller
			try {
				Date date = ((DataForExport) arg0).getStartDate();
				if (this.getStartDate().after(date)) {
					return 1;
				}
				else if (this.getStartDate().before(date)) {
					return -1;
				}
				else {
					return 0;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		public Date getStartDate() {
			return startDate;
		}
		
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		
		public String getContents() {
			String contents = null;	
			try {
				contents = 
				member.getSchoolClass().getSchool().getExtraProviderId() + SEPARATOR +
				member.getSchoolClass().getGroupStringId() + SEPARATOR +
				formatPersonalId(member.getStudent().getPersonalID()) + SEPARATOR + 
				dateToString(startDate) + SEPARATOR +
				formatEndDate(dateToString(endDate)) + SEPARATOR +
				formatCaretime(careTimeString) + SEPARATOR +
				member.getSchoolType().getTypeStringId() + 
				"\r\n";	
			} catch (Exception e) {
				System.out.println("error in DataForExport");
				e.printStackTrace();
			}
			return contents;
		}

		
		public Date getEndDate() {
			return endDate;
		}

		
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}		
		
	}		
	// end of contract and placement changes
}