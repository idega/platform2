/*
 * $Id: MeetingFeeBusinessHomeImpl.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: 5.12.2004 16:08:37 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingFeeBusinessHomeImpl extends IBOHomeImpl implements MeetingFeeBusinessHome {

	protected Class getBeanInterfaceClass() {
		return MeetingFeeBusiness.class;
	}

	public MeetingFeeBusiness create() throws javax.ejb.CreateException {
		return (MeetingFeeBusiness) super.createIBO();
	}
}
