/*
 * $Id: MeetingFeeBusinessBean.java,v 1.6 2004/12/14 00:29:06 laddi Exp $
 * Created on 1.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.agura.AguraConstants;
import se.agura.applications.business.ApplicationsBusinessBean;
import se.agura.applications.meeting.fee.data.MeetingFee;
import se.agura.applications.meeting.fee.data.MeetingFeeFormula;
import se.agura.applications.meeting.fee.data.MeetingFeeFormulaHome;
import se.agura.applications.meeting.fee.data.MeetingFeeHome;
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;
import se.agura.applications.meeting.fee.data.MeetingFeeInfoHome;

import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: 1.12.2004 12:57:51 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.6 $
 */
public class MeetingFeeBusinessBean extends ApplicationsBusinessBean  implements MeetingFeeBusiness{
	
	protected static final String IW_MEETING_BUNDLE_IDENTIFIER = "se.agura.applications.meeting.fee";
	
	protected String getBundleIdentifier() {
		return IW_MEETING_BUNDLE_IDENTIFIER;
	}

	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		MeetingFee request = getMeetingFeeInstance(theCase);
		Group parish = request.getCongregationGroup();
		Group participants = request.getParticipantGroup();
		Object[] arguments = { parish.getName(), participants.getName() };

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	protected MeetingFee getMeetingFeeInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (MeetingFeeConstants.CASE_CODE_KEY.equals(caseCode)) {
				return this.getMeetingFee(theCase.getPrimaryKey());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a vacation request");
	}
	
	/* (non-Javadoc)
	 * @see com.idega.block.process.business.CaseBusiness#getPrimaryKeyParameter()
	 */
	public String getPrimaryKeyParameter() {
		return MeetingFeeConstants.PARAMETER_PRIMARY_KEY;
	}
	
	private UserBusiness getUserBusiness() {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException ible) {
			throw new IBORuntimeException(ible);
		}
	}

  private MeetingFeeHome getMeetingFeeHome() {
		try {
			return (MeetingFeeHome) IDOLookup.getHome(MeetingFee.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private MeetingFeeInfoHome getMeetingFeeInfoHome() {
		try {
			return (MeetingFeeInfoHome) IDOLookup.getHome(MeetingFeeInfo.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private MeetingFeeFormulaHome getMeetingFeeFormulaHome() {
		try {
			return (MeetingFeeFormulaHome) IDOLookup.getHome(MeetingFeeFormula.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public MeetingFee getMeetingFee(Object primaryKey) throws FinderException {
		return getMeetingFeeHome().findByPrimaryKey(new Integer(primaryKey.toString()));
	}
	
	public Collection getMeetingFeeInfo(MeetingFee meetingFee) throws FinderException {
		return getMeetingFeeInfoHome().findByMeetingFee(meetingFee);
	}
	
	public MeetingFeeInfo getMeetingFeeInfo(MeetingFee meetingFee, User user) throws FinderException {
		return getMeetingFeeInfoHome().findByUserAndMeetingFee(user, meetingFee);
	}
	
	public MeetingFeeFormula getMeetingFeeFormula() throws FinderException {
		return getMeetingFeeFormulaHome().findLatestFormula();
	}
	
	public float calculateMeetingFee(int hours, int minutes) {
		try {
			return calculateMeetingFee(hours, minutes, getMeetingFeeFormula());
		}
		catch (FinderException fe) {
			log("No meeting fee formula found");
			return -1;
		}
	}

	public float calculateMeetingFee(int hours, int minutes, MeetingFeeFormula formula) {
		int firstHourAmount = formula.getFirstHourAmount();
		int proceedingHourAmount = formula.getProceedingTimeAmount();
		
		int totalMinutes = (hours * 60) + minutes;
		
		if (totalMinutes > 0) {
			float proceedingHours = (totalMinutes - 60) / 60f;
			float proceedingAmount = proceedingHours * proceedingHourAmount;
			return (firstHourAmount + proceedingAmount);
		}
		return 0;
 	}
	
	public Collection getParishes() {
		try {
			String[] types = { AguraConstants.GROUP_TYPE_PARISH, AguraConstants.GROUP_TYPE_PARISH_OFFICE };
			return getUserBusiness().getGroupBusiness().getGroups(types, true);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public Collection getMeetingGroups(User user) {
		try {
			String[] types = { AguraConstants.GROUP_TYPE_MEETING };
			return getUserBusiness().getGroupBusiness().getGroups(types, true);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public void storeApplication(User user, int parishID, String comment, int participantGroupID, Date meetingDate, boolean inCommune, String[] participants, String[] hours, String[] minutes, MeetingFeeFormula formula) throws CreateException {
		storeApplication(null, user, parishID, comment, participantGroupID, meetingDate, inCommune, participants, hours, minutes, formula);
	}
	
	public void storeApplication(Object primaryKey, User user, int parishID, String comment, int participantGroupID, Date meetingDate, boolean inCommune, String[] participants, String[] hours, String[] minutes, MeetingFeeFormula formula) throws CreateException {
		MeetingFee application = null;
		if (primaryKey != null) {
			try {
				application = getMeetingFeeHome().findByPrimaryKey(primaryKey);
				
				Collection times = getMeetingFeeInfoHome().findByMeetingFee(application);
				Iterator iter = times.iterator();
				while (iter.hasNext()) {
					MeetingFeeInfo info = (MeetingFeeInfo) iter.next();
					info.remove();
				}
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (EJBException e) {
				log(e);
			}
			catch (RemoveException e) {
				log(e);
			}
		}
		if (application == null) {
			application = getMeetingFeeHome().create();
		}
		Group handlerGroup = getParentGroup(user);
		
		application.setOwner(user);
		if (handlerGroup != null) {
			application.setHandler(handlerGroup);
		}
		application.setMeetingDate(meetingDate);
		application.setInCommune(inCommune);
		application.setComment(comment);
		application.setParticipantGroupID(participantGroupID);
		application.setCongregationID(parishID);
		application.store();
		
		MeetingFeeInfo info;
		for (int a = 0; a < participants.length; a++) {
			info = getMeetingFeeInfoHome().create();
			
			int hour = Integer.parseInt(hours[a]);
			int minute = Integer.parseInt(minutes[a]);
			int amount = (int) calculateMeetingFee(hour, minute, formula);
			int totalMinutes = (hour * 60) + minute;
			
			info.setUserID(Integer.parseInt(participants[a]));
			info.setMeetingDuration(totalMinutes);
			info.setMeetingFee(application);
			info.setMeetingFeeFormula(formula);
			info.setAmount(amount);
			info.store();
		}
	}
	
	private Group getParentGroup(User user) {
		Group primaryGroup = user.getPrimaryGroup();
		if (primaryGroup != null) {
			Group parentGroup = (Group) primaryGroup.getParentNode();
			return parentGroup;
		}
		return null;
	}

	public void acceptApplication(MeetingFee meetingFee, User performer) {
		changeCaseStatus(meetingFee, getCaseStatusGranted().getStatus(), performer);
	}

	public void rejectApplication(MeetingFee meetingFee, User performer) {
		changeCaseStatus(meetingFee, getCaseStatusDenied().getStatus(), performer);
	}
}