/*
 * $Id: SupplierManagerBusinessHome.java,v 1.7 2005/07/11 17:54:29 gimmi Exp $
 * Created on Jul 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2005/07/11 17:54:29 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.7 $
 */
public interface SupplierManagerBusinessHome extends IBOHome {

	public SupplierManagerBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
