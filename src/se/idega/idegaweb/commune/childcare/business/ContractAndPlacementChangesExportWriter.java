package se.idega.idegaweb.commune.childcare.business;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public class ContractAndPlacementChangesExportWriter {
	public static final String SEPARATOR = ";";
	public boolean createExportFile(IWContext iwc, ICFile folder) {
		
		Date firstDate = getFirstDateOfCurrentMonth();
		Date lastDate = getLastDateOfCurrentMonth();		
		
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			
			//XXX: data gathered here
			//lets get just first 1000 school class members for now
			//later this is going to business, now it's here
			SchoolClassMemberHome memberHome = null;	            
			memberHome = (SchoolClassMemberHome)IDOLookup.getHome(SchoolClassMember.class);		            
	        Collection collection =  memberHome.findAll();
			
	        if (collection.size() > 0) {
	        	Iterator iter = collection.iterator();
	        	while (iter.hasNext()) {
	        		// XXX use stringbuffer
	        		SchoolClassMember member = (SchoolClassMember) iter.next();	       		
	        		
	        		//sch_school.EXTRA_PROVIDER_ID
	        		String sss = member.getSchoolClass().getSchool().getName();
	        		sss += SEPARATOR;	        		
	        		//sch_school_class.GROUP_STRING_ID
	        		sss += nullToEmpty(member.getSchoolClass().getGroupStringId()); 
	        		sss += SEPARATOR;
	        		//ic_user.PERSONAL_ID
	        		sss += " ";
	        		sss += member.getStudent().getPersonalID();  //TODO: formatting 
	        		sss += SEPARATOR;	        		
	        		//sch_class_member_log	START_DATE
	        		sss += " ";
	        		sss += (new IWTimestamp(firstDate)).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT);
	        		sss += SEPARATOR;	        		
	        		//sch_class_member_log	END_DATE
	        		sss += " ";
	        		sss += (new IWTimestamp(lastDate)).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT);
	        		
	        		sss += SEPARATOR;	        		
	        		//comm_childcare_archive	CARE_TIME_STRING
	        		sss += "care_time_string";
	        		sss += SEPARATOR;	        		
	        		//sch_school_type.TYPE_STRING_ID
	        		sss += nullToEmpty(member.getSchoolType().getTypeStringId());
	        		sss += SEPARATOR;	        		
	        		sss+= "\r\n";
	        		mos.write(sss.getBytes("UTF8")); //XXX: hmm...   
	        	}
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
	
	//XXX: get first and last date of current month
	
	private Date getFirstDateOfCurrentMonth() {		
	    Calendar cal = new GregorianCalendar();	  
	        
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // It will be 1	    
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    
	    return new Date(cal.getTimeInMillis());
	}
	
	private Date getLastDateOfCurrentMonth() {		
	    Calendar cal = new GregorianCalendar();	  
	        
	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);	    
	    cal.set(Calendar.DAY_OF_MONTH, days);
	    
	    return new Date(cal.getTimeInMillis());
	}	
	
	
	private String nullToEmpty(String s) {
		if(s == null) {
			return "";
		}
		else {
			return "";
		}
	}
	
}
