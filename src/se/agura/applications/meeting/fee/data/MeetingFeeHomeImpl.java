/*
 * $Id: MeetingFeeHomeImpl.java,v 1.1 2004/12/05 20:59:37 anna Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.meeting.fee.data;

import com.idega.data.IDOFactory;


/**
 * Last modified: 5.12.2004 16:16:46 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class MeetingFeeHomeImpl extends IDOFactory implements MeetingFeeHome {

	protected Class getEntityInterfaceClass() {
		return MeetingFee.class;
	}

	public MeetingFee create() throws javax.ejb.CreateException {
		return (MeetingFee) super.createIDO();
	}

	public MeetingFee findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (MeetingFee) super.findByPrimaryKeyIDO(pk);
	}
}
