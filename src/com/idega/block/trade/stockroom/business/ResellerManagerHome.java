package com.idega.block.trade.stockroom.business;

import com.idega.business.IBOHome;


/**
 * @author gimmi
 */
public interface ResellerManagerHome extends IBOHome {

	public ResellerManager create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
