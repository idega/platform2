/*
 * $Id: MeetingFeeBusinessHome.java,v 1.4 2005/03/10 09:10:47 laddi Exp $
 * Created on 10.3.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type MeetingFeeBusinessHome
 * </p>
 *  Last modified: $Date: 2005/03/10 09:10:47 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface MeetingFeeBusinessHome extends IBOHome {

	public MeetingFeeBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
