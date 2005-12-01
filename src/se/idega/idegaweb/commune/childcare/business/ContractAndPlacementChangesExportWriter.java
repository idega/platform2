package se.idega.idegaweb.commune.childcare.business;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public class ContractAndPlacementChangesExportWriter {
	public static final String SEPARATOR = ";";
	
	IWContext iwc;
	
	private boolean isDebuggingGoingOn = false;
	
	public boolean createExportFile(IWContext iwc, ICFile folder) {
		
		this.iwc = iwc;
		
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			
			
			Iterator iter = getExportData().iterator(); 
			while (iter.hasNext()) {
				DataForExport row = (DataForExport) iter.next();
				mos.write(row.getContents().getBytes("UTF8")); //XXX check for npe!!
			}

			
			buffer.setMimeType("text/plain"); 
			InputStream mis = new MemoryInputStream(buffer);
	
			ICFileHome icFileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
			ICFile file = icFileHome.create();
			file.setFileValue(mis);
			file.setMimeType("text/plain");
			
			IWTimestamp toDate = new IWTimestamp();
			String s = toDate.toString().replaceAll(":", ""); // on windows filenames cannot contain colons
			
			file.setName("export " + s + ".txt");			
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
	    
	    if (isDebuggingGoingOn) {
		    cal.add(Calendar.YEAR, -1);
		    cal.add(Calendar.MONTH, -1);
	    }
	        
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // It will be 1	    
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    
	    return new Date(cal.getTimeInMillis());
	}	

	private Date getLastDateOfCurrentMonth() {		
	    Calendar cal = roundCalendarToDate(new GregorianCalendar());
	    
	    if (isDebuggingGoingOn) {
	    	cal.add(Calendar.YEAR, -1);	
	    } 	    	
	        
	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);	    
	    cal.set(Calendar.DAY_OF_MONTH, days);

	    return new Date(cal.getTimeInMillis());
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
		return roundCalendarToDate(cal).getTime();
		
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
	
	private Collection getExportData()  {
		Date firstDateOfCurrentMonth = this.getFirstDateOfCurrentMonth();
		Date lastDateOfCurrentMonth = this.getLastDateOfCurrentMonth();
		
		Vector uberCollection = new Vector();
		
		try {
			//get all placements
			//retrieve the placements we need and add them to the ubercolection

			Iterator placementsIterator =  getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare()).iterator();
			
			boolean takeThisPlacement = false;
			
			for(; placementsIterator.hasNext();) {
				SchoolClassMember placement = (SchoolClassMember) placementsIterator.next();

				Date registerDate = timestampToRoundedDate(placement.getRegisterDate()); //java.sql.Timestamp; and only contains date
				if (registerDate == null ) continue;

				Date removedDate = timestampToRoundedDate(placement.getRemovedDate()); //java.sql.Timestamp; contains date and time
				
				//XXX this should be done in SQL in the first place
				if (registerDate.compareTo(lastDateOfCurrentMonth) > 0)	continue;
				if (removedDate != null && removedDate.compareTo(firstDateOfCurrentMonth) < 0)	continue;
				
				Iterator placementLogs = getSchoolClassMemberLogHome().findAllBySchoolClassMember(placement).iterator();
				while (placementLogs.hasNext()){
					takeThisPlacement = false;
					
					SchoolClassMemberLog log = (SchoolClassMemberLog) placementLogs.next();
					
					Date startDate = log.getStartDate(); //java.util.Date, contains date only
					Date endDate = log.getEndDate(); //java.util.Date, contains date only
					
//					2.	IF it’s a new placement where sch_class_member.register_date >= 1st of current month 
//					AND sch_class_member.register_date <= last date of current month					
					if (registerDate.equals(startDate) & isDateInInterval(registerDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth))  //placement started
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
						uberCollection.add(new DataForExport(log.getSchoolClassMember(),startDate, endDate, "care_time_string"));					
					}
					
				}
				
			}
			
			//get all contracts
			//retrieve the contracts we need
			//... and add them to the ubercollection		

			Iterator contractsIterator = getChildCareContractHome().findChangedBetween(
					new java.sql.Date(firstDateOfCurrentMonth.getTime()), new java.sql.Date(lastDateOfCurrentMonth.getTime())).iterator(); //TODO: check, if this date conversion is okay.
			
			while (contractsIterator.hasNext()) {
				ChildCareContract contract = (ChildCareContract) contractsIterator.next();
				
//				5.	IF a caretime has been changed (new entry in comm_childcare_archive with 
//					comm_childcare_archive.start date >=1st of current month AND comm_childcare_archive.start date <= last date of current month)
				Date validFromDate = contract.getValidFromDate(); //java.util.Date , only date
				if (validFromDate == null) continue;
				
				if ( isDateInInterval(validFromDate, firstDateOfCurrentMonth, lastDateOfCurrentMonth)) {
					uberCollection.add(new DataForExport(contract.getSchoolClassMember(),validFromDate, null, contract.getCareTime()));					
				}				
				
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
		return date.compareTo(startDate) >= 0 || date.compareTo(endDate) <= 0;
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
		String contents;
		Date startDate;
		
		public DataForExport(SchoolClassMember member, Date startDate, Date endDate, String careTimeString) {
			this.startDate = startDate;
			try {
			this.contents = 
				member.getSchoolClass().getSchool().getExtraProviderId() + SEPARATOR +
				member.getSchoolClass().getGroupStringId() + SEPARATOR +
				member.getStudent().getPersonalID() + SEPARATOR + 
				dateToString(startDate) + SEPARATOR +
				dateToString(endDate) + SEPARATOR +
				careTimeString + SEPARATOR +
				member.getSchoolType().getTypeStringId() + 
				"\r\n";	
			} catch (Exception e) {
				System.out.println("error in DataForExport");
				this.contents = ""; //XXX 
				e.printStackTrace();
			}
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
			return contents;
		}
		
		public void setContents(String contents) {
			this.contents = contents;
		}
	}
	
}
