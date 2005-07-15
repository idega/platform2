/*
 * $Id: AddressCoordinateImportHandlerHome.java,v 1.1 2005/07/15 17:35:03 thomas Exp $
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
 *  Last modified: $Date: 2005/07/15 17:35:03 $ by $Author: thomas $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface AddressCoordinateImportHandlerHome extends IBOHome {

	public AddressCoordinateImportHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
