/*
 * $Id: MeetingFeeInfoHome.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
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
 * Last modified: 5.12.2004 16:18:01 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
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
