/*
 * $Id: CitizenAccountBusinessHomeImpl.java,v 1.3 2005/04/06 08:28:02 anna Exp $
 * Created on 6.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.account.citizen.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO anna Describe Type CitizenAccountBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2005/04/06 08:28:02 $ by $Author: anna $
 * 
 * @author <a href="mailto:anna@idega.com">anna</a>
 * @version $Revision: 1.3 $
 */
public class CitizenAccountBusinessHomeImpl extends IBOHomeImpl implements CitizenAccountBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CitizenAccountBusiness.class;
	}

	public CitizenAccountBusiness create() throws javax.ejb.CreateException {
		return (CitizenAccountBusiness) super.createIBO();
	}
}
