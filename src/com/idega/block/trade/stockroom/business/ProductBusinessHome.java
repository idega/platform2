package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface ProductBusinessHome extends IBOHome {

	public ProductBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
