/*
 * $Id: MeetingFee.java,v 1.3 2004/12/13 14:35:10 anna Exp $
 * Created on 13.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.sql.Date;
import com.idega.block.process.data.Case;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: 13.12.2004 09:56:23 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.3 $
 */
public interface MeetingFee extends Case {

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCaseStatusDescriptions
	 */
	public String[] getCaseStatusDescriptions();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCaseStatusKeys
	 */
	public String[] getCaseStatusKeys();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getInCommune
	 */
	public boolean getInCommune();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getParticipantGroup
	 */
	public Group getParticipantGroup();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getParticipantGroupID
	 */
	public int getParticipantGroupID();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCongregationGroup
	 */
	public Group getCongregationGroup();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getCongregationGroupID
	 */
	public int getCongregationGroupID();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getComment
	 */
	public String getComment();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getMeetingDate
	 */
	public Date getMeetingDate();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getSignedDate
	 */
	public Date getSignedDate();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#getSignedBy
	 */
	public User getSignedBy();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setInCommune
	 */
	public void setInCommune(boolean inCommune);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setParticipantGroup
	 */
	public void setParticipantGroup(Group participantGroup);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setParticipantGroupID
	 */
	public void setParticipantGroupID(int participantGroupID);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setCongregation
	 */
	public void setCongregation(Group congregationGroup);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setCongregationID
	 */
	public void setCongregationID(int congregationGroupID);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setComment
	 */
	public void setComment(String comment);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setMeetingDate
	 */
	public void setMeetingDate(Date meetingDate);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setSignedDate
	 */
	public void setSignedDate(Date signedDate);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeBMPBean#setSignedBy
	 */
	public void setSignedBy(User signedBy);
}
