/*
 * $Id: CareBusinessHomeImpl.java,v 1.4 2004/10/21 10:57:27 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/10/21 10:57:27 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.4 $
 */
public class CareBusinessHomeImpl extends IBOHomeImpl implements CareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CareBusiness.class;
	}

	public CareBusiness create() throws javax.ejb.CreateException {
		return (CareBusiness) super.createIBO();
	}
}
