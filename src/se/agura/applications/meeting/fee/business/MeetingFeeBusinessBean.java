/*
 * $Id: MeetingFeeBusinessBean.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 1.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import java.util.Collection;
import javax.ejb.FinderException;
import se.agura.applications.meeting.fee.data.MeetingFee;
import se.agura.applications.meeting.fee.data.MeetingFeeFormula;
import se.agura.applications.meeting.fee.data.MeetingFeeFormulaHome;
import se.agura.applications.meeting.fee.data.MeetingFeeHome;
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;
import se.agura.applications.meeting.fee.data.MeetingFeeInfoHome;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;


/**
 * Last modified: 1.12.2004 12:57:51 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingFeeBusinessBean extends CaseBusinessBean  implements MeetingFeeBusiness{
	
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
	
	
	
	/*public void storeApplication(User user, congregation, speaker, meetingPlace, meetingDate, participants, meetingHours, meetingMinutes, amount) throws CreateException {
		storeApplication(null, user, congregation, speaker, meetingPlace, meetingDate, participants, meetingHours, meetingMinutes, amount);
	}

	public void storeApplication(Object pk, User user, congregation, speaker, meetingPlace, meetingDate, participants, meetingHours, meetingMinutes, amount) throws CreateException {
	   hér koma kall á setFöllin
	}*/
}
