/*
 * $Id: MeetingFeeBusiness.java,v 1.4 2004/12/13 14:35:10 anna Exp $
 * Created on 13.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import java.sql.Date;
import java.util.Collection;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.agura.applications.business.ApplicationsBusiness;
import se.agura.applications.meeting.fee.data.MeetingFee;
import se.agura.applications.meeting.fee.data.MeetingFeeFormula;
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;
import com.idega.block.process.data.Case;
import com.idega.user.data.User;


/**
 * Last modified: 13.12.2004 14:42:52 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.4 $
 */
public interface MeetingFeeBusiness extends ApplicationsBusiness {

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getLocalizedCaseDescription
	 */
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getPrimaryKeyParameter
	 */
	public String getPrimaryKeyParameter() throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getMeetingFee
	 */
	public MeetingFee getMeetingFee(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getMeetingFeeInfo
	 */
	public Collection getMeetingFeeInfo(MeetingFee meetingFee) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getMeetingFeeInfo
	 */
	public MeetingFeeInfo getMeetingFeeInfo(MeetingFee meetingFee, User user) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getMeetingFeeFormula
	 */
	public MeetingFeeFormula getMeetingFeeFormula() throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#calculateMeetingFee
	 */
	public float calculateMeetingFee(int hours, int minutes) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#calculateMeetingFee
	 */
	public float calculateMeetingFee(int hours, int minutes, MeetingFeeFormula formula) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getParishes
	 */
	public Collection getParishes() throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#getMeetingGroups
	 */
	public Collection getMeetingGroups(User user) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#storeApplication
	 */
	public void storeApplication(User user, int parishID, String comment, int participantGroupID, Date meetingDate,
			boolean inCommune, String[] participants, String[] hours, String[] minutes, MeetingFeeFormula formula)
			throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#storeApplication
	 */
	public void storeApplication(Object primaryKey, User user, int parishID, String comment, int participantGroupID,
			Date meetingDate, boolean inCommune, String[] participants, String[] hours, String[] minutes,
			MeetingFeeFormula formula) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#acceptApplication
	 */
	public void acceptApplication(MeetingFee meetingFee, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#rejectApplication
	 */
	public void rejectApplication(MeetingFee meetingFee, User performer) throws java.rmi.RemoteException;
}
