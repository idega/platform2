package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface SupplierManagerBusinessHome extends IBOHome {

	public SupplierManagerBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
