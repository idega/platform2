/*
 * $Id: ImportBusinessHome.java 1.1 3.2.2005 gimmi Exp $
 * Created on 3.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.importer.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface ImportBusinessHome extends IBOHome {

	public ImportBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
