/*
 * $Id: CalBusiness.java,v 1.12 2004/12/07 18:04:03 eiki Exp $ Created on Dec 7, 2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.cal.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.block.cal.data.AttendanceEntity;
import com.idega.block.cal.data.AttendanceMark;
import com.idega.block.cal.data.CalendarEntry;
import com.idega.block.cal.data.CalendarEntryGroup;
import com.idega.block.cal.data.CalendarEntryType;
import com.idega.block.cal.data.CalendarLedger;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * 
 * Last modified: $Date: 2004/12/07 18:04:03 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki </a>
 * @version $Revision: 1.12 $
 */
public interface CalBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntry
	 */
	public CalendarEntry getEntry(int entryID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntriesByTimestamp
	 */
	public Collection getEntriesByTimestamp(Timestamp stamp);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntriesBetweenTimestamps
	 */
	public Collection getEntriesBetweenTimestamps(Timestamp fromStamp, Timestamp toStamp);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntriesByLedgerID
	 */
	public Collection getEntriesByLedgerID(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntriesByEntryGroupID
	 */
	public Collection getEntriesByEntryGroupID(int entryGroupID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getPracticesByLedgerID
	 */
	public Collection getPracticesByLedgerID(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getMarkedEntriesByUserIDandLedgerID
	 */
	public Collection getMarkedEntriesByUserIDandLedgerID(int userID, int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getPracticesByLedIDandMonth
	 */
	public Collection getPracticesByLedIDandMonth(int ledgerID, int month, int year);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntryGroup
	 */
	public CalendarEntryGroup getEntryGroup(int entryGroupID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntryGroupsByLedgerID
	 */
	public Collection getEntryGroupsByLedgerID(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getEntryTypeByName
	 */
	public CalendarEntryType getEntryTypeByName(String entryTypeName);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAllEntryTypes
	 */
	public List getAllEntryTypes();

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getLedger
	 */
	public CalendarLedger getLedger(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getLedgerIDByName
	 */
	public int getLedgerIDByName(String name);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAllLedgers
	 */
	public List getAllLedgers();

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAttendanceEntity
	 */
	public AttendanceEntity getAttendanceEntity(int attendanceID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAttendanceByUserIDandEntry
	 */
	public AttendanceEntity getAttendanceByUserIDandEntry(int userID, CalendarEntry entry);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAttendancesByLedgerID
	 */
	public List getAttendancesByLedgerID(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getNumberOfPractices
	 */
	public int getNumberOfPractices(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAttendanceMarks
	 */
	public List getAttendanceMarks(int userID, int ledgerID, String mark);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getAllMarks
	 */
	public List getAllMarks();

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getMark
	 */
	public AttendanceMark getMark(int markID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteEntry
	 */
	public void deleteEntry(int entryID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteEntryGroup
	 */
	public void deleteEntryGroup(int entryGroupID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteEntryGroupByEntryID
	 */
	public void deleteEntryGroupByEntryID(int entryID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteLedger
	 */
	public void deleteLedger(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteUserFromLedger
	 */
	public void deleteUserFromLedger(int userID, int ledgerID, IWContext iwc);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteMark
	 */
	public void deleteMark(int markID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#createNewEntryType
	 */
	public boolean createNewEntryType(String typeName);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#createNewEntry
	 */
	public void createNewEntry(String headline, User user, String type, String repeat, String startDate,
			String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees,
			String ledger, String description, String location);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#updateEntry
	 */
	public void updateEntry(int entryID, String headline, User user, String type, String repeat, String startDate,
			String startHour, String startMinute, String endDate, String endHour, String endMinute, String attendees,
			String ledger, String description, String location, String oneOrMany);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#createNewLedger
	 */
	public void createNewLedger(String name, int groupID, String coachName, String date, int coachGroupID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#createNewMark
	 */
	public void createNewMark(int markID, String markName, String description);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getUsersInLedger
	 */
	public Collection getUsersInLedger(int ledgerID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#saveAttendance
	 */
	public void saveAttendance(int userID, int ledgerID, CalendarEntry entry, String mark);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#updateAttendance
	 */
	public void updateAttendance(int attendanceID, int userID, int ledgerID, CalendarEntry entry, String mark);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteEntryType
	 */
	public void deleteEntryType(int typeID);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#deleteBlock
	 */
	public boolean deleteBlock(int iObjectInstanceId);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getGroupBusiness
	 */
	public GroupBusiness getGroupBusiness(IWApplicationContext iwc);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup);

	/**
	 * @see com.idega.block.cal.business.CalBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException;
}