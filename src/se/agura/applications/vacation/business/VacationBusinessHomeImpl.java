/*
 * $Id: VacationBusinessHomeImpl.java,v 1.1 2004/11/25 14:22:35 anna Exp $
 * Created on 24.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: 24.11.2004 09:27:13 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.1 $
 */
public class VacationBusinessHomeImpl extends IBOHomeImpl implements VacationBusinessHome {

	protected Class getBeanInterfaceClass() {
		return VacationBusiness.class;
	}

	public VacationBusiness create() throws javax.ejb.CreateException {
		return (VacationBusiness) super.createIBO();
	}
}
