/*
 * $Id: MeetingFeeInfo.java,v 1.2 2004/12/06 21:30:34 laddi Exp $
 * Created on 6.12.2004
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
 * Last modified: $Date: 2004/12/06 21:30:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
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
	public void setMeetingFee(MeetingFee meetingFee);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setMeetingFeeFormula
	 */
	public void setMeetingFeeFormula(MeetingFeeFormula meetingFeeFormula);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setUserID
	 */
	public void setUserID(int userID);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setMeetingDuration
	 */
	public void setMeetingDuration(int meetingDuration);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#setAmount
	 */
	public void setAmount(int amount);

}
