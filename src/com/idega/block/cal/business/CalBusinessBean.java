/*
 * Created on Feb 4, 2004
 */
package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceEntityHome;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.AttendanceMarkHome;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryGroup;
import com.idega.block.cal.data.CalendarEntryGroupHome;
import com.idega.block.cal.data.CalendarEntryHome;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarEntryTypeHome;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.block.cal.data.CalendarLedgerHome;
import com.idega.block.cal.presentation.CalendarEntryCreator;
import com.idega.block.cal.presentation.CalendarWindowPlugin;
import com.idega.block.category.business.CategoryBusiness;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author 
 */
public class CalBusinessBean extends IBOServiceBean implements CalBusiness,UserGroupPlugInBusiness{

	//GET methods for Entries
	/**
	 * @return a calendar entry with the specific entryID
	 */
	public CalendarEntry getEntry(int entryID) {
		CalendarEntry entry = null;
		Integer id = new Integer(entryID);
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			entry = entryHome.findByPrimaryKey(id);
		} catch(FinderException e) {
			entry = null;
		} catch(RemoteException re) {
			re.printStackTrace();
		}
		return entry;		
	}
	/**
	 * 
	 */
	public Collection getEntriesByTimestamp(Timestamp stamp) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntryByTimestamp(stamp));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;		
	}
	public Collection getEntriesBetweenTimestamps(Timestamp fromStamp, Timestamp toStamp) {
		List list = null; 
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesBetweenTimestamps(fromStamp,toStamp));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public Collection getEntriesByLedgerID(int ledgerID) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByLedgerID(ledgerID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public Collection getEntriesByEntryGroupID(int entryGroupID) {
		List list = null;
		try {
			CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
			list = new ArrayList(entryHome.findEntriesByEntryGroupID(entryGroupID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public Collection getPracticesByLedgerID(int ledgerID) {
		List list = new ArrayList();
		List entries = (List) getEntriesByLedgerID(ledgerID);
		Iterator entryIter = entries.iterator();
		while(entryIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) entryIter.next();
			if(entry.getEntryTypeName().equals("practice")) {
				list.add(entry);
			}
		}
		return list;
	}
	public Collection getMarkedEntriesByUserIDandLedgerID(int userID,int ledgerID) {
		List list = new ArrayList();
		List entries = (List) getPracticesByLedgerID(ledgerID);
		Iterator entIter = entries.iterator();
		while(entIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) entIter.next();
			AttendanceEntity attendance = getAttendanceByUserIDandEntry(userID,entry);
			if(attendance != null) {
				if(attendance.getAttendanceMark() != null && !attendance.getAttendanceMark().equals("")) {
					list.add(attendance);
				}
			}
			
		}
//		System.out.println("list: " + list.toString());
		return list;
	}
	public Collection getPracticesByLedIDandMonth(int ledgerID, int month, int year) {
		List list = new ArrayList();
		List practices = (List) getPracticesByLedgerID(ledgerID);
		Iterator prIter = practices.iterator();
		while(prIter.hasNext()) {
			CalendarEntry entry = (CalendarEntry) prIter.next();
			Timestamp entryDate = entry.getDate();
			IWTimestamp i = new IWTimestamp(entryDate);
			int m = i.getMonth();
			int y = i.getYear();
			if(m == month && y == year) {
				list.add(entry);
			}
		}
		return list;		
	}
	//GET methods for EntryGroup
	public CalendarEntryGroup getEntryGroup(int entryGroupID) {

		CalendarEntryGroup entryGroup = null;
		Integer id = new Integer(entryGroupID);
		try {
			CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
			entryGroup = entryGroupHome.findByPrimaryKey(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return entryGroup;				
	}	
	
	public Collection getEntryGroupsByLedgerID(int ledgerID) {
		List list = null;
		try {
			CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
			list = new ArrayList(entryGroupHome.findEntryGroupsByLedgerID(ledgerID));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
		
		
	}
	//GET methods for EntryTypes
	/**
	 * gets a CalendarEntryType with the name <code>entryTypeName</code>
	 */
	public CalendarEntryType getEntryTypeByName(String entryTypeName) {
		CalendarEntryType entryType = null;
//		CalendarEntryType tempEntry = null;
		List list = null;
		try {
			CalendarEntryTypeHome typeHome = (CalendarEntryTypeHome) getIDOHome(CalendarEntryType.class);
			list = new ArrayList(typeHome.findTypeByName(entryTypeName));
			entryType = (CalendarEntryType) list.get(0);
			return entryType;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 */
	public List getAllEntryTypes() {
		List types = null;
		try {
			CalendarEntryTypeHome typeHome = (CalendarEntryTypeHome) getIDOHome(CalendarEntryType.class);
			types = new ArrayList(typeHome.findTypes());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return types;
	}
	
	//GET methods for Ledger
	/**
	 * 
	 * @param entryID
	 * @return
	 */
	public CalendarLedger getLedger(int ledgerID) {
		CalendarLedger ledger = null;
		Integer id = new Integer(ledgerID);
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledger = ledgerHome.findByPrimaryKey(id);
		} catch(Exception e) {
			ledger = null;
		}
		return ledger;
	}
	
	public int getLedgerIDByName(String name) {

		CalendarLedger ledger =null;
		int ledgerID = 0;
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledger = ledgerHome.findLedgerByName(name);			
			ledgerID = ledger.getLedgerID();
			return ledgerID;
			
		} catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	/**
	 * 
	 */
	public List getAllLedgers() {
		List ledgers = null;
		try {
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			ledgers = new ArrayList(ledgerHome.findLedgers()); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ledgers;
	}
	//GET methods for Attendance
	public AttendanceEntity getAttendanceEntity(int attendanceID) {
		AttendanceEntity attendance = null;
		Integer id = new Integer(attendanceID);
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendance = attendanceHome.findByPrimaryKey(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return attendance;		
	}
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID, CalendarEntry entry) {
		AttendanceEntity attendance = null;
//		Timestamp stamp = entry.getDate();
		int entryID = entry.getEntryID();
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendance = attendanceHome.findAttendanceByUserIDandEntryID(userID,entryID);			
		} catch(Exception e) {
			attendance = null;
		}
		return attendance;
		
	}
	public List getAttendancesByLedgerID(int ledgerID) {

		List attendances = null;
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			attendances = new ArrayList(attendanceHome.findAttendancesByLedgerID(ledgerID)); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return attendances;		
	}
	public int getNumberOfPractices(int ledgerID) {
		List attendances = getAttendancesByLedgerID(ledgerID);
		return attendances.size();
	}
	public List getAttendanceMarks(int userID, int ledgerID, String mark) {
		List marks = null;
		try {
			AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
			marks = new ArrayList(attendanceHome.findAttendanceByMark(userID,ledgerID,mark));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marks;
	}
	//get methods for marks
	public List getAllMarks() {

		List marks = null;
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			marks = new ArrayList(markHome.findMarks()); 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return marks;
	}
	public AttendanceMark getMark(int markID) {
		AttendanceMark mark = null;
		Integer mID = new Integer(markID);
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			mark = markHome.findByPrimaryKey(mID);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mark;
	}
	
	public void deleteEntry(int entryID) {		
		CalendarEntry entry = getEntry(entryID);
		int entryGroupID = entry.getEntryGroupID();
		CalendarEntryGroup entryGroup = getEntryGroup(entryGroupID);
		if (entry != null) {
			try {
				entryGroup.removeOneEntryRelation(entry);
				entry.remove();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	public void deleteEntryGroup(int entryGroupID) {
		CalendarEntryGroup entryGroup = getEntryGroup(entryGroupID);
		try {
			entryGroup.removeEntryRelation();
			entryGroup.remove();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Collection entries = getEntriesByEntryGroupID(entryGroupID);
		Iterator entIter = entries.iterator();
		while(entIter.hasNext()) {
			CalendarEntry e = (CalendarEntry) entIter.next();
			try {
				e.remove();
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}		
	}
	
	public void deleteEntryGroupByEntryID(int entryID) {
		CalendarEntry entry = getEntry(entryID);
		if(entry != null) {
			deleteEntryGroup(entry.getEntryGroupID());
		}		
	}
	public void deleteLedger(int ledgerID) {
		CalendarLedger led = getLedger(ledgerID);	
		if(led != null) {
			/*
			 * get all the attendance markings for this ledger and delete them
			 */
			Collection att = getAttendancesByLedgerID(ledgerID);
			Iterator attIter = att.iterator();
			while(attIter.hasNext()) {
				AttendanceEntity attendance = (AttendanceEntity) attIter.next();
				try {
					attendance.remove();
				}catch (Exception e) {
					e.printStackTrace();
				}			
			}
			/*
			 * get all entryGroups for this ledger and delete them
			 */
//			Collection entries = getEntriesByLedgerID(ledgerID);
			Collection entryGroups = getEntryGroupsByLedgerID(ledgerID);
			Iterator entIter = entryGroups.iterator();
			while(entIter.hasNext()) {
				CalendarEntryGroup entryGroup = (CalendarEntryGroup) entIter.next();
				deleteEntryGroup(entryGroup.getEntryGroupID());
			}
			try {
				led.removeUserRelation();
				led.remove();
			}catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	
	
	public void deleteUserFromLedger(int userID, int ledgerID, IWContext iwc) {
		UserBusiness userBiz = getUserBusiness(iwc);
		GroupBusiness groupBiz = getGroupBusiness(iwc);
		CalendarLedger ledger = getLedger(ledgerID);
		User user = null;
		try{
			user = 	userBiz.getUser(userID);
		}catch (Exception e) {
			
		}
		if(user !=null) {
			Collection att = getAttendancesByLedgerID(ledgerID);
			Iterator attIter = att.iterator();
			while(attIter.hasNext()) {
				AttendanceEntity attendance = (AttendanceEntity) attIter.next();
				if(attendance.getUserID() == userID) {
					try {
						attendance.remove();
					}catch (Exception e) {
						e.printStackTrace();
					}	
				}
			}
			try {
				ledger.removeOneUserRelation(user);
			}catch (Exception e) {
				e.printStackTrace();
			}
			//try {
			//	userBiz.removeUserFromGroup(user,groupBiz.getGroupByGroupID(ledger.getGroupID()),iwc.getCurrentUser());
			//}catch (Exception e) {
			//	e.printStackTrace();
			//}
			//This delete was preformed weather the user had the privileges or not
			
		}
	}
	public void deleteMark(int markID) {
		AttendanceMark mark = getMark(markID); 		
		if (mark != null) {
			try {
				mark.remove();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}		
	}

	/**
	 * creates a new CalendarEntryType 
	 * @param typeName
	 */
	public boolean createNewEntryType(String typeName) {

		Iterator typeIter = getAllEntryTypes().iterator();
		while(typeIter.hasNext()) {
			CalendarEntryType type = (CalendarEntryType) typeIter.next();
			if(type.getName().equals(typeName)) {
				return false;
			}
		}
		CalendarEntryType type = null;
		try {
			CalendarEntryTypeHome typeHome = (CalendarEntryTypeHome) getIDOHome(CalendarEntryType.class);
			type = typeHome.create();
			type.setName(typeName);
			type.store();			
		}
		catch(Exception e) {
//			System.out.println("Couldn't add type: " + typeName);
			return false;
		}
		return true;
	
		
	}
	/**
	 * startDate and endDate have to be of the form  yyyy-MM-dd hh:mm:ss.S
	 */
	public void createNewEntry(String headline, User user, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location) {
		CalendarEntryGroup entryGroup = null;
//		if(repeat != null && !repeat.equals("")) {
			try {
				CalendarEntryGroupHome entryGroupHome = (CalendarEntryGroupHome) getIDOHome(CalendarEntryGroup.class);
				entryGroup = entryGroupHome.create();
				entryGroup.setName(repeat);
				if(Integer.parseInt(ledger) != -1) {
					entryGroup.setLedgerID(Integer.parseInt(ledger));
				}			
				entryGroup.store();
			} catch (Exception e) {
				e.printStackTrace();
			}			
//		}
			IWTimestamp st = new IWTimestamp(startDate);
		Timestamp startTime = st.getTimestamp();		
		//modifications of the time properties of the start timestamp
		if(startHour != null || !startHour.equals("")) {
			Integer sH =new Integer(startHour);		
			startTime.setHours(sH.intValue());
		}
		if(startMinute != null || !startMinute.equals("")) {
			Integer sM = new Integer(startMinute);
			startTime.setMinutes(sM.intValue());
		}
		startTime.setSeconds(0);
//		startTime.setNanos(0);
		IWTimestamp et = new IWTimestamp(endDate);
		Timestamp endTime = et.getTimestamp();
		//modifications of the time properties of the end timestamp
		if(endHour != null || !endHour.equals("")) {
			Integer eH =new Integer(endHour);
			endTime.setHours(eH.intValue());
		}
		if(endMinute != null || !endMinute.equals("")) {
			Integer eM =new Integer(endMinute);
			endTime.setMinutes(eM.intValue());
		}
		endTime.setSeconds(0);
//		endTime.setNanos(0);
		
		GregorianCalendar startCal = new GregorianCalendar(startTime.getYear(),startTime.getMonth(),startTime.getDate(),startTime.getHours(),startTime.getMinutes());
		GregorianCalendar endCal = new GregorianCalendar(endTime.getYear(),endTime.getMonth(),endTime.getDate(),endTime.getHours(),endTime.getMinutes());
				
		long start = startCal.getTimeInMillis();
		long end = endCal.getTimeInMillis();
		
		long year365 = 365L*24L*60L*60L*1000L;
		long year366 = 366L*24L*60L*60L*1000L;
		long month31 = 31L*24L*60L*60L*1000L;
		long month30 = 30L*24L*60L*60L*1000L;
		long month29 = 29L*24L*60L*60L*1000L;
		long month28 = 28L*24L*60L*60L*1000L;
		long week = 7L*24L*60L*60L*1000L;
		long day = 24L*60L*60L*1000L;
		
		Integer groupID = null;
		if(attendees != null && !attendees.equals("")) {
			groupID = new Integer(attendees);
		}	
		else {
			groupID = new Integer(-1);
		}
		Integer userID = null;
		if(user != null) {
			userID = (Integer) user.getPrimaryKey();
		}
		else {
			userID = new Integer(-1);
		}
		while(start < end) {	
			Timestamp endOfEntryTime = Timestamp.valueOf(startTime.toString());
			if(endHour != null || !endHour.equals("")) {
				Integer eH =new Integer(endHour);
				endOfEntryTime.setHours(eH.intValue());
			}
			if(endMinute != null || !endMinute.equals("")) {
				Integer eM =new Integer(endMinute);
				endOfEntryTime.setMinutes(eM.intValue());
			}
			endOfEntryTime.setSeconds(0);
			
			
			try {
				CalendarEntryType entryType = getEntryTypeByName(type);
				Integer entryTypePK = (Integer) entryType.getPrimaryKey();
				CalendarEntryHome entryHome = (CalendarEntryHome) getIDOHome(CalendarEntry.class);
				CalendarEntry entry = entryHome.create();
				entry.setName(headline);
				entry.setUserID(userID.intValue());
				entry.setEntryTypeID(entryTypePK.intValue());
				entry.setEntryType(entryType.getName());
				entry.setRepeat(repeat);
				entry.setDate(startTime);
				entry.setEndDate(endOfEntryTime);
				if(groupID != null) {
					entry.setGroupID(groupID.intValue());
				}	
				if(Integer.parseInt(ledger) != -1) {
					entry.setLedgerID(Integer.parseInt(ledger));
				}
				entry.setDescription(description);
				entry.setLocation(location);
				entry.store();
				if(entryGroup !=null) {
					entryGroup.addEntry(entry);
					entry.setEntryGroupID(entryGroup.getEntryGroupID());
					entry.store();
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(repeat.equals(CalendarEntryCreator.yearlyFieldParameterName)) {
				if(startTime.getYear()%4 == 0) {
					start += year366;
				}
				else {
					start += year365; //start up one year = 31536000000 milliseconds
				}
				startCal.set(startTime.getYear()+1,startTime.getMonth(),startTime.getDate());
				
			}
			
			else if(repeat.equals(CalendarEntryCreator.monthlyFieldParameterName)) {
				//if December
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add 1 to the year and set the month to January
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,startTime.getDate());
				}
				else {
					int month = startTime.getMonth();
					//if the month has 31 days
					if(month == Calendar.JANUARY ||
							month == Calendar.MARCH ||
							month == Calendar.MAY ||
							month == Calendar.AUGUST ||
							month == Calendar.OCTOBER) {						
						//and the date is the 31st
						if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
							//in this case the 2 months are added because the next months after
							//January, March, May, August and October do not have the 31st!
							startCal.set(startTime.getYear(),startTime.getMonth()+2,startTime.getDate());
							start += month31*2L; //start up two 31 day months
						}
						else {
							startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
							start += month31; //start up one 31 day month
						}
						
					}
					else if(startTime.getMonth() == Calendar.JULY) {	
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month31;//start up one 31 day month
					}
					
					else if(startTime.getMonth() == Calendar.FEBRUARY) {							 
						//leap year
						if(startTime.getYear()%4 == 0) {
							start += month29;//start up 29 day month
						}
						else {
							//"ordinary" February
							start += month28;//start up 28 day month
						}
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
					}						
					else {
						//this case is for months APRIL, JUNE, SEPTEMBER and NOVEMBER
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month30;	//start up 30 day month						
					}													
				}					
			}
			else if(repeat.equals(CalendarEntryCreator.weeklyFieldParameterName)) {
				startCal.add(Calendar.DAY_OF_MONTH,7);
				start += week;
			}
			else if(repeat.equals(CalendarEntryCreator.noRepeatFieldParameterName)) {
				start = end;
				//do nothing - the entry is just saved for the current day
			}
			//if the last day of the month
			else if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
				//if the the last day of month and last month of year
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add one to year, set month = January and day = 1
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,1);
				}
				else {
					//if last day of month and not last month of year
					//year is same, add 1 to month and day = 1
					startCal.set(startTime.getYear(),startTime.getMonth()+1,1);
				}		
				start += day; //start up one day = 86400000 milliseconds
			}
			else {
				//if not the last day of month
				//and not the last month of year
				//year is the same, month is the same, 1 added to day
				startCal.set(startTime.getYear(),startTime.getMonth(),startTime.getDate()+1);
				start += day; //start up one day = 86400000 milliseconds
			}
			
			Date sd = startCal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			String f = format.format(sd);
			
			startTime = Timestamp.valueOf(f);
			int year = sd.getYear() + 1900;
			startTime.setYear(year);	
			
			
		}			
		
	}
	public void updateEntry(int entryID, String headline, User user, String type, String repeat, String startDate, String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees, String ledger, String description, String location, String oneOrMany) {
		CalendarEntry entry = getEntry(entryID);
		
		IWTimestamp startD = new IWTimestamp(startDate);
		Timestamp startTime = startD.getTimestamp();//Timestamp.valueOf(startDate);		
		//modifications of the time properties of the start timestamp
		if(startHour != null || !startHour.equals("")) {
			Integer sH =new Integer(startHour);		
			startTime.setHours(sH.intValue());
		}
		if(startMinute != null || !startMinute.equals("")) {
			Integer sM = new Integer(startMinute);
			startTime.setMinutes(sM.intValue());
		}
		startTime.setSeconds(0);
//		startTime.setNanos(0);
		
		IWTimestamp endD = new IWTimestamp(endDate);
		Timestamp endTime = endD.getTimestamp();//Timestamp.valueOf(endDate);
		//modifications of the time properties of the end timestamp
		if(endHour != null || !endHour.equals("")) {
			Integer eH =new Integer(endHour);
			endTime.setHours(eH.intValue());
		}
		if(endMinute != null || !endMinute.equals("")) {
			Integer eM =new Integer(endMinute);
			endTime.setMinutes(eM.intValue());
		}
		endTime.setSeconds(0);
//		endTime.setNanos(0);
		
		GregorianCalendar startCal = new GregorianCalendar(startTime.getYear(),startTime.getMonth(),startTime.getDate(),startTime.getHours(),startTime.getMinutes());
		GregorianCalendar endCal = new GregorianCalendar(endTime.getYear(),endTime.getMonth(),endTime.getDate(),endTime.getHours(),endTime.getMinutes());
		
		long start = startCal.getTimeInMillis();
		long end = endCal.getTimeInMillis();
		

		long year365 = 365L*24L*60L*60L*1000L;
		long year366 = 366L*24L*60L*60L*1000L;
		long month31 = 31L*24L*60L*60L*1000L;
		long month30 = 30L*24L*60L*60L*1000L;
		long month29 = 29L*24L*60L*60L*1000L;
		long month28 = 28L*24L*60L*60L*1000L;
		long week = 7L*24L*60L*60L*1000L;
		long day = 24L*60L*60L*1000L;
		
		Integer groupID = null;
		if(attendees != null && !attendees.equals("")) {
			groupID = new Integer(attendees);
		}	
		Integer userID = null;
		if(user != null) {
			userID = (Integer) user.getPrimaryKey();
		}
		else {
			userID = new Integer(-1);
		}
		
		while(start < end) {
			Timestamp endOfEntryTime = Timestamp.valueOf(startTime.toString());
			if(endHour != null || !endHour.equals("")) {
				Integer eH =new Integer(endHour);
				endOfEntryTime.setHours(eH.intValue());
			}
			if(endMinute != null || !endMinute.equals("")) {
				Integer eM =new Integer(endMinute);
				endOfEntryTime.setMinutes(eM.intValue());
			}
			endOfEntryTime.setSeconds(0);
			
			try {
				CalendarEntryType entryType = getEntryTypeByName(type);
				Integer entryTypePK = (Integer) entryType.getPrimaryKey();
				Integer ledgerID = new Integer(ledger);
				entry.setName(headline);
				entry.setUserID(userID.intValue());
				entry.setEntryTypeID(entryTypePK.intValue());
				entry.setEntryType(entryType.getName());
				entry.setRepeat(repeat);
				entry.setDate(startTime);
				entry.setEndDate(endOfEntryTime);
				if(groupID != null) {
					entry.setGroupID(groupID.intValue());
				}	
				if(ledgerID.intValue() != -1) {
					entry.setLedgerID(ledgerID.intValue());
				}
				entry.setDescription(description);
				entry.setLocation(location);
				entry.store();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			if(repeat.equals(CalendarEntryCreator.yearlyFieldParameterName)) {
				if(startTime.getYear()%4 == 0) {
					start += year366;
				}
				else {
					start += year365; //start up one year = 31536000000 milliseconds
				}
				startCal.set(startTime.getYear()+1,startTime.getMonth(),startTime.getDate());
				
			}
			
			else if(repeat.equals(CalendarEntryCreator.monthlyFieldParameterName)) {
				//if December
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add 1 to the year and set the month to January
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,startTime.getDate());
				}
				else {
					int month = startTime.getMonth();
					//if the month has 31 days
					if(month == Calendar.JANUARY ||
							month == Calendar.MARCH ||
							month == Calendar.MAY ||
							month == Calendar.AUGUST ||
							month == Calendar.OCTOBER) {						
						//and the date is the 31st
						if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
							//in this case the 2 months are added because the next months after
							//January, March, May, August and October do not have the 31st!
							startCal.set(startTime.getYear(),startTime.getMonth()+2,startTime.getDate());
							start += month31*2L; //start up two 31 day months
						}
						else {
							startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
							start += month31; //start up one 31 day month
						}
						
					}
					else if(startTime.getMonth() == Calendar.JULY) {	
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month31;//start up one 31 day month
					}
					
					else if(startTime.getMonth() == Calendar.FEBRUARY) {							 
						//leap year
						if(startTime.getYear()%4 == 0) {
							start += month29;//start up 29 day month
						}
						else {
							//"ordinary" February
							start += month28;//start up 28 day month
						}
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
					}						
					else {
						//this case is for months APRIL, JUNE, SEPTEMBER and NOVEMBER
						startCal.set(startTime.getYear(),startTime.getMonth()+1,startTime.getDate());
						start += month30;	//start up 30 day month						
					}													
				}					
			}
			else if(repeat.equals(CalendarEntryCreator.weeklyFieldParameterName)) {
				startCal.add(Calendar.DAY_OF_MONTH,7);
				start += week;
			}
			//if the last day of the month
			else if(startTime.getDate() == startCal.getActualMaximum(Calendar.DATE)) {
				//if the the last day of month and last month of year
				if(startTime.getMonth() == startCal.getActualMaximum(Calendar.MONTH)) {
					//add one to year, set month = January and day = 1
					startCal.set(startTime.getYear()+1,Calendar.JANUARY,1);
				}
				else {
					//if last day of month and not last month of year
					//year is same, add 1 to month and day = 1
					startCal.set(startTime.getYear(),startTime.getMonth()+1,1);
				}		
				start += day; //start up one day = 86400000 milliseconds
			}
			else {
				//if not the last day of month
				//and not the last month of year
				//year is the same, month is the same, 1 added to day
				startCal.set(startTime.getYear(),startTime.getMonth(),startTime.getDate()+1);
				start += day; //start up one day = 86400000 milliseconds
			}
			
			Date sd = startCal.getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.S");
			String f = format.format(sd);
			
			startTime = Timestamp.valueOf(f);
			int year = sd.getYear() + 1900;
			startTime.setYear(year);				
		}					
	}
	public void createNewLedger(String name, int groupID, String coachName, String date,int coachGroupID) {
		IWContext iwc = IWContext.getInstance();
		Collection users = null;
		Group group = null;
		User user = null;
		User coach = null;
		IWTimestamp iwstamp = new IWTimestamp(date);
		Timestamp stamp = iwstamp.getTimestamp();
		stamp.setHours(0);
		stamp.setMinutes(0);
		stamp.setSeconds(0);
		stamp.setNanos(0);
		
		try {
			group = getGroupBusiness(iwc).getGroupByGroupID(groupID);
			users = this.getUserBusiness(iwc).getUsersInGroup(group);
			coach = iwc.getCurrentUser();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			Integer coachID = (Integer) coach.getPrimaryKey();
			CalendarLedgerHome ledgerHome = (CalendarLedgerHome) getIDOHome(CalendarLedger.class);
			CalendarLedger ledger = ledgerHome.create();
			ledger.setName(name);
			ledger.setGroupID(groupID);
			ledger.setCoachGroupID(coachGroupID);
			ledger.setCoachID(coachID.intValue());
			ledger.setDate(stamp);
			ledger.store();
			Iterator userIter = users.iterator();
			while(userIter.hasNext()) {
				user = (User) userIter.next();
				ledger.addUser(user);
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void createNewMark(int markID, String markName, String description) {
		try {
			AttendanceMarkHome markHome = (AttendanceMarkHome) getIDOHome(AttendanceMark.class);
			AttendanceMark mark = null;
			if(markID == -1) {
				mark = markHome.create();
			}
			else {
				mark = markHome.findByPrimaryKey(new Integer(markID));
			}
			mark.setMark(markName);
			mark.setMarkDescription(description);
			mark.store();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Collection getUsersInLedger(int ledgerID) {
		CalendarLedger ledger = getLedger(ledgerID);
		Collection users = ledger.getUsers();
//		User user = null;
//		Iterator userIter = users.iterator();
//		while(userIter.hasNext()) {
//			user = (User) userIter.next();
//		}	
		return users;
	}
	
	public void saveAttendance(int userID, int ledgerID, CalendarEntry entry, String mark) {
		
		try {
			AttendanceEntity attendance = getAttendanceByUserIDandEntry(userID,entry);
			if(attendance == null) {
				AttendanceEntityHome attendanceHome = (AttendanceEntityHome) getIDOHome(AttendanceEntity.class);
				attendance = attendanceHome.create();
			}
			Timestamp date = entry.getDate();
			int entryID = entry.getEntryID();
			attendance.setUserID(userID);
			attendance.setLedgerID(ledgerID);
			attendance.setEntryID(entryID);
			attendance.setAttendanceDate(date);
			attendance.setAttendanceMark(mark);
			attendance.store();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void updateAttendance(int attendanceID, int userID, int ledgerID, CalendarEntry entry, String mark) {
		AttendanceEntity attendance = getAttendanceEntity(attendanceID);
		try {
			Timestamp date = entry.getDate();
			int entryID = entry.getEntryID();
			attendance.setUserID(userID);
			attendance.setLedgerID(ledgerID);
			attendance.setEntryID(entryID);
			attendance.setAttendanceDate(date);
			attendance.setAttendanceMark(mark);
			attendance.store();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteEntryType(int typeID) {
		CalendarEntryType type = CalendarFinder.getInstance().getEntryType(typeID);
		if (type != null) {
			try {
				CalendarEntry[] entries = (CalendarEntry[]) com.idega.block.calendar.data.CalendarEntryBMPBean.getStaticInstance().findAllByColumn(com.idega.block.calendar.data.CalendarEntryBMPBean.getColumnNameEntryTypeID(), typeID);
				if (entries != null)
					for (int a = 0; a < entries.length; a++) {
//						entries[a].removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
						entries[a].remove();
					}
//				type.removeFrom(com.idega.block.text.data.LocalizedTextBMPBean.getStaticInstance(LocalizedText.class));
				type.remove();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}
	
	public boolean deleteBlock(int iObjectInstanceId) {
		return CategoryBusiness.getInstance().deleteBlock(iObjectInstanceId);
	}
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc) {
		GroupBusiness groupBiz = null;
		if (groupBiz == null) {
			try {
				groupBiz = (GroupBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return groupBiz;
	}
	protected UserBusiness getUserBusiness(IWApplicationContext iwc) {
		UserBusiness userBusiness = null;
		if (userBusiness == null) {
			try {
				userBusiness = (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return userBusiness;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new CalendarWindowPlugin());
		return list;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User, com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		return null;
	}
}
