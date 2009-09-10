/*
 * $Id: TradePermissionBusiness.java,v 1.1 2005/05/31 19:21:33 gimmi Exp $
 * Created on 10.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOService;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/05/31 19:21:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface TradePermissionBusiness extends IBOService {

	/**
	 * @see com.idega.block.trade.stockroom.business.TradePermissionBusinessBean#hasRole
	 */
	public boolean hasRole(Group supplierManager, String role) throws RemoteException;

	/**
	 * @see com.idega.block.trade.stockroom.business.TradePermissionBusinessBean#hasRole
	 */
	public boolean hasRole(Supplier supplier, String role) throws RemoteException;
}
