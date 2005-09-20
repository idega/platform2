/*
 * $Id: ChildCareBusinessHomeImpl.java 1.1 Sep 19, 2005 bluebottle Exp $
 * Created on Sep 19, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: bluebottle $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements ChildCareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareBusiness.class;
	}

	public ChildCareBusiness create() throws javax.ejb.CreateException {
		return (ChildCareBusiness) super.createIBO();
	}

}
