/*
 * $Id: MeetingFeeBusinessHome.java,v 1.3 2004/12/13 14:35:10 anna Exp $
 * Created on 13.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import com.idega.business.IBOHome;


/**
 * Last modified: 13.12.2004 14:42:53 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.3 $
 */
public interface MeetingFeeBusinessHome extends IBOHome {

	public MeetingFeeBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
