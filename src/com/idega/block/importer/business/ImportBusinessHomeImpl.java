/*
 * $Id: ImportBusinessHomeImpl.java 1.1 3.2.2005 gimmi Exp $
 * Created on 3.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.importer.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class ImportBusinessHomeImpl extends IBOHomeImpl implements ImportBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ImportBusiness.class;
	}

	public ImportBusiness create() throws javax.ejb.CreateException {
		return (ImportBusiness) super.createIBO();
	}
}
