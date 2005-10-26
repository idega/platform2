/*
 * $Id: CommuneUserBusinessHomeImpl.java,v 1.5 2005/10/26 18:13:35 eiki Exp $
 * Created on Oct 26, 2005
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
 *  Last modified: $Date: 2005/10/26 18:13:35 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.5 $
 */
public class CommuneUserBusinessHomeImpl extends IBOHomeImpl implements CommuneUserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CommuneUserBusiness.class;
	}

	public CommuneUserBusiness create() throws javax.ejb.CreateException {
		return (CommuneUserBusiness) super.createIBO();
	}
}
