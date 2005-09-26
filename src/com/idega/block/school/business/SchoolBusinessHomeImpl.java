/*
 * $Id: SchoolBusinessHomeImpl.java,v 1.15 2005/09/26 20:04:18 laddi Exp $
 * Created on Sep 26, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2005/09/26 20:04:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.15 $
 */
public class SchoolBusinessHomeImpl extends IBOHomeImpl implements SchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolBusiness.class;
	}

	public SchoolBusiness create() throws javax.ejb.CreateException {
		return (SchoolBusiness) super.createIBO();
	}
}
