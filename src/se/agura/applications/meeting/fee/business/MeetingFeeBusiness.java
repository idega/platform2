/*
 * $Id: MeetingFeeBusiness.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
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
import se.agura.applications.meeting.fee.data.MeetingFeeInfo;
import com.idega.block.process.business.CaseBusiness;
import com.idega.user.data.User;


/**
 * Last modified: 5.12.2004 16:08:35 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public interface MeetingFeeBusiness extends CaseBusiness {

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
}
