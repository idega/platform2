/*
 * $Id: MeetingFeeBusiness.java,v 1.5 2005/03/10 09:10:47 laddi Exp $
 * Created on 10.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
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
 * <p>
 * TODO laddi Describe Type MeetingFeeBusiness
 * </p>
 *  Last modified: $Date: 2005/03/10 09:10:47 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
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
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#closeApplication
	 */
	public void closeApplication(MeetingFee fee, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#acceptApplication
	 */
	public void acceptApplication(MeetingFee meetingFee, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#rejectApplication
	 */
	public void rejectApplication(MeetingFee meetingFee, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.meeting.fee.business.MeetingFeeBusinessBean#canDeleteCase
	 */
	public boolean canDeleteCase(Case theCase) throws java.rmi.RemoteException;
}
