/*
 * $Id: MeetingFeeInfoHome.java,v 1.2 2004/12/06 21:30:34 laddi Exp $
 * Created on 6.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/12/06 21:30:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface MeetingFeeInfoHome extends IDOHome {

	public MeetingFeeInfo create() throws javax.ejb.CreateException;

	public MeetingFeeInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#ejbFindByMeetingFee
	 */
	public Collection findByMeetingFee(MeetingFee meetingFee) throws FinderException;

	/**
	 * @see se.agura.applications.meeting.fee.data.MeetingFeeInfoBMPBean#ejbFindByUserAndMeetingFee
	 */
	public MeetingFeeInfo findByUserAndMeetingFee(User user, MeetingFee meetingFee) throws FinderException;

}
