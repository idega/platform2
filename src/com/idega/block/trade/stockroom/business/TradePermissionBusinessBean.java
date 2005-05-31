/*
 * $Id: TradePermissionBusinessBean.java,v 1.1 2005/05/31 19:21:33 gimmi Exp $
 * Created on 10.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;


/**
 * 
 *  Last modified: $Date: 2005/05/31 19:21:33 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public class TradePermissionBusinessBean extends IBOServiceBean  implements TradePermissionBusiness{

	public boolean hasRole(Group supplierManager, String role) throws RemoteException {
		
		Collection coll = getSupplierManagerBusiness().getRolesAsString(supplierManager);
		if (coll != null) {
			return coll.contains(role);
		}
		
		return false;
	}
	
	public boolean hasRole(Supplier supplier, String role) throws RemoteException {
		
		try {
			Collection coll = getSupplierManagerBusiness().getRolesAsString(supplier);
			if (coll != null) {
				return coll.contains(role);
			}
		} catch (FinderException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	protected SupplierManagerBusiness getSupplierManagerBusiness() {
		try {
			return (SupplierManagerBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SupplierManagerBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
}
