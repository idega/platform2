/*
 * $Id: MeetingFeeFormula.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.util.Date;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;


/**
 * Last modified: 5.12.2004 15:25:51 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface MeetingFeeFormula extends IDOEntity {

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getMeetingFeeFormulaId
	 */
	public int getMeetingFeeFormulaId();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getCreationDate
	 */
	public Date getCreationDate();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getFirstHourAmount
	 */
	public int getFirstHourAmount();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getProceedingTimeAmount
	 */
	public int getProceedingTimeAmount();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getCreatedBy
	 */
	public User getCreatedBy();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#getProceedingTimeInterval
	 */
	public int getProceedingTimeInterval();

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setMeetingFeeFormulaId
	 */
	public void setMeetingFeeFormulaId(int meetingFeeFormulaId);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setCreationDate
	 */
	public void setCreationDate(Date creationDate);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setFirstHourAmount
	 */
	public void setFirstHourAmount(int firstHourAmount);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setProceedingTimeAmount
	 */
	public void setProceedingTimeAmount(int proceedingTimeAmount);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setCreatedBy
	 */
	public void setCreatedBy(User createdBy);

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeFormulaBMPBean#setProceedingTimeInterval
	 */
	public void setProceedingTimeInterval(int proceedingTimeInterval);
}
