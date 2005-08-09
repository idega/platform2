/*
 * $Id: CareBusinessHomeImpl.java,v 1.5 2005/08/09 16:34:50 laddi Exp $
 * Created on Aug 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/08/09 16:34:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class CareBusinessHomeImpl extends IBOHomeImpl implements CareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return CareBusiness.class;
	}

	public CareBusiness create() throws javax.ejb.CreateException {
		return (CareBusiness) super.createIBO();
	}
}
