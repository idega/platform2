/*
 * $Id: VacationRequestHomeImpl.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 24.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.data;

import com.idega.data.IDOFactory;


/**
 * Last modified: 24.11.2004 09:37:41 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class VacationRequestHomeImpl extends IDOFactory implements VacationRequestHome {

	protected Class getEntityInterfaceClass() {
		return VacationRequest.class;
	}

	public VacationRequest create() throws javax.ejb.CreateException {
		return (VacationRequest) super.createIDO();
	}

	public VacationRequest findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (VacationRequest) super.findByPrimaryKeyIDO(pk);
	}
}
