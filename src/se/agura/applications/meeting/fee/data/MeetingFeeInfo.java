/*
 * $Id: MeetingFeeInfo.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: 5.12.2004 16:18:01 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface MeetingFeeInfo extends IDOEntity {

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#getMeetingFee
	 */
	public MeetingFee getMeetingFee();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#getMeetingFeeFormula
	 */
	public MeetingFeeFormula getMeetingFeeFormula();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#getMeetingDuration
	 */
	public int getMeetingDuration();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#getAmount
	 */
	public int getAmount();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setMeetingFee
	 */
	public void setMeetingFee(MeetingFee meetingFeeId);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setMeetingFeeFormula
	 */
	public void setMeetingFeeFormula(MeetingFeeFormula meetingFeeFormulaId);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setUser
	 */
	public void setUser(User icUserId);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setMeetingDuration
	 */
	public void setMeetingDuration(int meetingDuration);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setAmount
	 */
	public void setAmount(int amount);
}
