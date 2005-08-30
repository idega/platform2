/*
 * $Id: SchoolBusinessHomeImpl.java,v 1.12 2005/08/30 15:59:07 gimmi Exp $
 * Created on Aug 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2005/08/30 15:59:07 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.12 $
 */
public class SchoolBusinessHomeImpl extends IBOHomeImpl implements SchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolBusiness.class;
	}

	public SchoolBusiness create() throws javax.ejb.CreateException {
		return (SchoolBusiness) super.createIBO();
	}
}
