/*
 * $Id: CommuneUserBusinessHomeImpl.java,v 1.6 2005/11/03 18:29:29 eiki Exp $
 * Created on Nov 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/11/03 18:29:29 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.6 $
 */
public class CommuneUserBusinessHomeImpl extends IBOHomeImpl implements CommuneUserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CommuneUserBusiness.class;
	}

	public CommuneUserBusiness create() throws javax.ejb.CreateException {
		return (CommuneUserBusiness) super.createIBO();
	}
}
